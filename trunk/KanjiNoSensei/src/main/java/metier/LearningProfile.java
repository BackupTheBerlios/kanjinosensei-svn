/**
 * @author Escallier Pierre
 * @file LearningProfile.java
 * @date 7 mars 08
 */
package metier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import utils.MyUtils;

/**
 * This class represent a User learning profile, it keep quizz statistics foreach elements, so the quizz can choose the bests elements to ask (the less known).
 */
public class LearningProfile implements Serializable
{
	/**
	 * Serialization version.
	 */
	private static final long	serialVersionUID	= 1L;

	/** Default user learning profile filename. */
	public static final String DEFAULT_PROFILE = "myProfile.ulp";
	
	/** Elements Unique ID / statistics map. */
	TreeMap<String, Statistics>	statistics	= new TreeMap<String, Statistics>();
	
	/** Constructor. */
	public LearningProfile()
	{
		
	}
	
	/**
	 * Loads a learning profile from File, throws IOException on ClassNotFound. 
	 * @param file
	 *            Learning profile file to open.
	 * @throws IOException
	 *             on any Input/Output error.
	 */
	public static LearningProfile open(File file) throws IOException
	{
		LearningProfile learningProfile = null;
		
		// Open file stream
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try
		{
			Object obj = ois.readObject();

			if ( !LearningProfile.class.isInstance(obj)) throw new ClassNotFoundException();

			learningProfile = (LearningProfile) obj;
		}
		catch (ClassNotFoundException e)
		{
			System.err.println(Messages.getString("LearningProfile.Open.ErrorOnElement") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			throw new IOException(Messages.getString("LearningProfile.Open.ErrorOnElement") + " : " + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$)
		}

		ois.close();
		fis.close();
		
		return learningProfile;
	}

	/**
	 * Save the learning profile in a binary file (*.ulp) using ObjectOutputStream.
	 * @param file Destination file to save the learning profile.
	 * @throws IOException on any Input/Output error.
	 */
	public void save(File file) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(this);
		oos.close();
	}
	
	public void addToStats(String elementUID, boolean answerIsGood)
	{
		Statistics stats = null;
		if (statistics.containsKey(elementUID))
		{
			stats = statistics.get(elementUID);
		}
		else
		{
			stats = new Statistics();
			statistics.put(elementUID, stats);
		}
		
		stats.addToStats(answerIsGood);
	}
	
	/**
	 * This class keep informations about an element to be learn.
	 */
	private static class Statistics implements Serializable
	{
		/** Serializable version. */
		private static final long	serialVersionUID	= 1L;
		
		public static final int HEURE_MS = 3600000;
		public static final float FACTOR_SuccessRate = 100;
		
		// Maximum age (millis) to force the element to be quizzed, even if success rate is 100%
		public static float FACTOR_LastQuestionMaxAge_MS = HEURE_MS * 24 * 7; // 7 days
		
		// Minimum time to wait before to quizz a bad known element
		public static float MIN_TimeToWaitBeforeQuizzBadElements = 1000 * 60 * 2; // 2 min
		
		private int		nbQuestions			= 0;
		private int		nbGoodAnswers		= 0;
		private long	lastQuestionDate	= 0;
		
		public int getNbQuestions()
		{
			return nbQuestions;
		}
		
		public int getNbGoodAnswers()
		{
			return nbGoodAnswers;
		}
		
		public float getSuccessRate()
		{
			return (Float.valueOf(nbGoodAnswers) / Float.valueOf(nbQuestions));
		}
		
		public long getLastQuestionAge()
		{
			return (System.currentTimeMillis() - lastQuestionDate);
		}
		
		/**
		 * Update this statistic with new answer.
		 * @param answerIsGood
		 */
		public synchronized void addToStats(boolean answerIsGood)
		{
			nbQuestions++;
			if (answerIsGood) nbGoodAnswers++;
			lastQuestionDate = System.currentTimeMillis();
		}
		
		/**
		 * Calculate the need of this element to be quizzed.
		 * The more the score is high, the more the user is supposed to need to be questioned about this element.
		 * @return float Need Score of this element.
		 */
		public synchronized float getNeedScore()
		{
			long age = System.currentTimeMillis() - lastQuestionDate;
			if (age < MIN_TimeToWaitBeforeQuizzBadElements) return -FACTOR_SuccessRate;
			
			float score = (-FACTOR_SuccessRate * (Float.valueOf(nbGoodAnswers) / Float.valueOf(nbQuestions)) ) + ((FACTOR_SuccessRate / FACTOR_LastQuestionMaxAge_MS) * age);
			return score;
		}
	}

	/**
	 * @return
	 */
	public Set<String> getElementsUID()
	{
		return statistics.keySet();
	}

	/**
	 * Compute which element should be quizzed next, according to the statistics of each elements.
	 * @param dico 
	 * @return String Element Unique ID
	 */
	public String getNextElement(Vector<String> dico)
	{
		String bestElementUID = null;
		String currentElementUID = null;
		float bestScore = Float.NEGATIVE_INFINITY;
		float currentScore = bestScore;
		
		Iterator<String> itElements = dico.iterator();
		while(itElements.hasNext())
		{
			currentElementUID = itElements.next();
			
			if (!statistics.containsKey(currentElementUID))
			{
				System.err.println("Le profil utilisateur ne contient aucune donné sur l'élément '"+currentElementUID+"' qui est donc prioritaire.");
				return currentElementUID;
			}
			
			currentScore = statistics.get(currentElementUID).getNeedScore();
			if (currentScore > bestScore)
			{
				bestElementUID = currentElementUID;
				bestScore = currentScore;
			}
		}
		
		return bestElementUID;
	}

	/**
	 * @param key
	 * @return
	 */
	public String getElementStats(String elementUID)
	{
		Statistics stats = statistics.get(elementUID);
		if (stats == null) return null;
		
		StringBuilder sb = new StringBuilder(String.format("%d/%d = %f\t", stats.getNbGoodAnswers(), stats.getNbQuestions(), stats.getSuccessRate()));
		
		long time = stats.getLastQuestionAge();
		
		sb.append(MyUtils.timeToString(time));
		
		return sb.toString();
	}
}
