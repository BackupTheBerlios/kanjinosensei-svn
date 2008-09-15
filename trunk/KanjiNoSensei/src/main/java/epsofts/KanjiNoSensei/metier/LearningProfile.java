/**
 * @author Escallier Pierre
 * @file LearningProfile.java
 * @date 7 mars 08
 */
package epsofts.KanjiNoSensei.metier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;

import epsofts.KanjiNoSensei.RefactoringInfos;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.RefactoredClassNameTolerantObjectInputStream;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;

/**
 * This class represent a User learning profile, it keep quizz statistics foreach elements, so the quizz can choose the bests elements to ask (the less known).
 */
public class LearningProfile implements Serializable
{
	/**
	 * Serialization version.
	 */
	private static final long					serialVersionUID	= 1L;

	/** Default user learning profile filename. */
	public static final String					DEFAULT_PROFILE		= "myProfile.ulp";						//$NON-NLS-1$

	/** Elements Unique ID / statistics map. */
	TreeMap<String, Statistics>					statistics			= new TreeMap<String, Statistics>();

	/** Randomizer. */
	private static final Random					random				= new Random();

	/** Constructor. */
	public LearningProfile()
	{

	}

	/**
	 * Loads a learning profile from File, throws IOException on ClassNotFound.
	 * 
	 * @param file
	 *            Learning profile file to open.
	 * @throws IOException
	 *             on any Input/Output error.
	 */
	public static LearningProfile open(File file) throws IOException
	{
		KanjiNoSensei.log(Level.INFO, Messages.getString("LearningProfile.OpeningFile") + " : \"" + file.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		LearningProfile learningProfile = null;

		// Open file stream
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new RefactoredClassNameTolerantObjectInputStream(fis, RefactoringInfos.REFACTORED_PACKAGES);
		try
		{
			Object obj = ois.readObject();

			if ( !LearningProfile.class.isInstance(obj)) throw new ClassNotFoundException();

			learningProfile = (LearningProfile) obj;
		}
		catch (ClassNotFoundException e)
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("LearningProfile.Open.ErrorOnElement") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			throw new IOException(Messages.getString("LearningProfile.Open.ErrorOnElement") + " : " + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$)
		}

		ois.close();
		fis.close();

		return learningProfile;
	}

	/**
	 * Save the learning profile in a binary file (*.ulp) using ObjectOutputStream.
	 * 
	 * @param file
	 *            Destination file to save the learning profile.
	 * @throws IOException
	 *             on any Input/Output error.
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
		private static final long	serialVersionUID						= 1L;

		public static final int		HEURE_MS								= 3600000;

		public static final float	FACTOR_SuccessRate						= 100;

		// Maximum age (millis) to force the element to be quizzed, even if success rate is 100%
		public static float			FACTOR_LastQuestionMaxAge_MS			= HEURE_MS * 24 * 7;	// 7 days

		// Minimum time to wait before to quizz a bad known element
		public static float			MIN_TimeToWaitBeforeQuizzBadElements	= 1000 * 60 * 2;		// 2 min

		// NeedScore precision (musn't be to precise for randomization to work fine), numbe of digits after the unit (0 = integer).
		public static final int		NEED_SCORE_PRECISION					= 0;

		// Size of the unknown serie to roll, all the fist [UNKNOWN_SERIE_SIZE] unknown elements are loop quizzed. 0 disable the unknown serie loop.
		public static final int		UNKNOWN_SERIE_LOOP_SIZE					= 5;

		public static int			unknownSerieLoopCurrentSize				= 0;

		transient private boolean	inUnknownSerie							= false;

		private int					nbQuestions								= 0;

		private int					nbGoodAnswers							= 0;

		private long				lastQuestionDate						= 0;

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
			if (nbQuestions == 0) return 0;
			return (Float.valueOf(nbGoodAnswers) / Float.valueOf(nbQuestions));
		}

		public long getLastQuestionAge()
		{
			return (System.currentTimeMillis() - lastQuestionDate);
		}

		/**
		 * Update this statistic with new answer.
		 * 
		 * @param answerIsGood
		 */
		public synchronized void addToStats(boolean answerIsGood)
		{
			nbQuestions++ ;
			if (answerIsGood) nbGoodAnswers++ ;
			lastQuestionDate = System.currentTimeMillis();

			if ((answerIsGood) && (inUnknownSerie))
			{
				inUnknownSerie = false;
				unknownSerieLoopCurrentSize-- ;
			}

			if (( !answerIsGood) && (UNKNOWN_SERIE_LOOP_SIZE > 0) && ( !inUnknownSerie))
			{
				inUnknownSerie = true;
				unknownSerieLoopCurrentSize++ ;
			}
		}

		/**
		 * Calculate the need of this element to be quizzed. The more the score is high, the more the user is supposed to need to be questioned about this element.
		 * 
		 * @return float Need Score of this element.
		 */
		public synchronized float getNeedScore()
		{
			// First, we calculate the base score from SucessRate and Age.
			// Then, the long time seen elements are prior.
			// The never seen elements are naturaly prior (lastQuestionDate = 01/01/1970)
			long age = getLastQuestionAge();
			Float successRate = getSuccessRate();
			Float ageRate = age / FACTOR_LastQuestionMaxAge_MS;

			Float score = ageRate - successRate;
			score = Math.min(1, score);

			// Then, the recently already seen elements are less prior.
			if (age < MIN_TimeToWaitBeforeQuizzBadElements)
			{
				score -= 1;
			}

			// Current unknown serie is prior if serie stack is full.
			if ((UNKNOWN_SERIE_LOOP_SIZE > 0) && (unknownSerieLoopCurrentSize >= UNKNOWN_SERIE_LOOP_SIZE) && (inUnknownSerie))
			{
				score += 3;
			}

			Float precision = Float.parseFloat(Double.toString(Math.pow(10, NEED_SCORE_PRECISION)));
			score = Float.valueOf(score * 100 * precision).intValue() / precision;
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
	 * 
	 * @param dico
	 * @return String Element Unique ID
	 */
	public String getNextElement(Vector<String> dico)
	{
		String currentElementUID = null;
		float bestScore = Float.NEGATIVE_INFINITY;
		float currentScore = bestScore;
		Vector<String> bestsUIDs = new Vector<String>();

		Iterator<String> itElements = dico.iterator();
		while (itElements.hasNext())
		{
			currentElementUID = itElements.next();

			if ( !statistics.containsKey(currentElementUID))
			{
				KanjiNoSensei.log(Level.WARNING, Messages.getString("LearningProfile.LearningProfile.WarningNeverSeenElement") + " : \"" + currentElementUID + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return currentElementUID;
			}

			currentScore = statistics.get(currentElementUID).getNeedScore();
			if (currentScore > bestScore)
			{
				bestsUIDs.removeAllElements();
				bestsUIDs.add(currentElementUID);
				bestScore = currentScore;
			}
			else if (currentScore == bestScore)
			{
				bestsUIDs.add(currentElementUID);
			}
		}

		return bestsUIDs.get(random.nextInt(bestsUIDs.size()));
	}

	/**
	 * @param key
	 * @return
	 */
	public String getElementStats(String elementUID)
	{
		Statistics stats = statistics.get(elementUID);
		if (stats == null) return null;

		StringBuilder sb = new StringBuilder(String.format("%d/%d = %f\t", stats.getNbGoodAnswers(), stats.getNbQuestions(), stats.getSuccessRate())); //$NON-NLS-1$

		long time = stats.getLastQuestionAge();

		sb.append(MyUtils.timeToString(time) + "\t"); //$NON-NLS-1$
		sb.append(String.format(Messages.getString("LearningProfile.LearningProfile.Label.NeedScore") + " : %f", stats.getNeedScore())); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();
	}

	/**
	 * @param neverSeenElements
	 */
	public void addNeverSeenElements(Vector<String> neverSeenElements)
	{
		Iterator<String> itElements = neverSeenElements.iterator();
		String elementUID;
		while (itElements.hasNext())
		{
			elementUID = itElements.next();

			if (statistics.containsKey(elementUID))
			{
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("LearningProfile.LearningProfile.ErrorAlreadyKnownElement")); //$NON-NLS-1$
			}
			else
			{
				statistics.put(elementUID, new Statistics());
			}
		}
	}
}
