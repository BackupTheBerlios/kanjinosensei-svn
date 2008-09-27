/**
 * @author Escallier Pierre
 * @file LearningProfile.java
 * @date 7 mars 08
 */
package epsofts.KanjiNoSensei.metier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.Level;

import epsofts.KanjiNoSensei.RefactoringInfos;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.RefactoredClassNameTolerantObjectInputStream;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;

/**
 * This class represent a User learning profile, it keep quizz statistics for each elements, so the quizz can choose the bests elements to ask (the less known).
 */
public class LearningProfile implements Serializable
{
	/**
	 * Serialization version.
	 */
	private static final long	serialVersionUID	= 1L;

	/** Default user learning profile filename. */
	public static final String	DEFAULT_PROFILE		= "myProfile.ulp";						//$NON-NLS-1$

	/** Elements Unique ID / statistics map. */
	TreeMap<String, Statistics>	statistics			= new TreeMap<String, Statistics>();

	/** Randomizer. */
	private static final Random	random				= new Random();

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
		ObjectInputStream ois = null;
		try
		{
			ois = new RefactoredClassNameTolerantObjectInputStream(new FileInputStream(file), RefactoringInfos.REFACTORED_PACKAGES);
			Object obj = ois.readObject();
			if ( !LearningProfile.class.isInstance(obj)) throw new ClassNotFoundException();
			learningProfile = (LearningProfile) obj;
			learningProfile.pack();
		}
		catch (ClassNotFoundException e)
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("LearningProfile.Open.ErrorOnElement") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			throw new IOException(Messages.getString("LearningProfile.Open.ErrorOnElement") + " : " + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$)
		}
		finally
		{
			try
			{
				if (ois != null) ois.close();
			}
			catch (Exception e)
			{
				// Nothing.
			}
		}

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
		pack();
		ObjectOutputStream oos = null;
		try
		{
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
		}
		finally
		{
			try
			{
				if (oos != null) oos.close();
			}
			catch (Exception e)
			{
				// Nothing.
			}
		}
	}

	/**
	 * This method export current LearningProfile in *.csv format.
	 * |UID|Age|NbGoods|NbQuestions|SuccessRate|NeedScore|
	 * @see Statistics#toString()
	 * @param file File to export to.
	 * @throws FileNotFoundException On file error.
	 */
	public void export(File file) throws FileNotFoundException
	{
		pack();
		PrintStream ps = null;
		try
		{
			ps = new PrintStream(file);
			Iterator<String> it = statistics.keySet().iterator();
			while (it.hasNext())
			{
				String uid = it.next();
				Statistics stats = statistics.get(uid);
				ps.format("\"%s\";%s\n", uid, stats.toString());
			}
		}
		finally
		{
			MyUtils.safeClose(ps);
		}
	}

	/**
	 * Update the stats for the given element and answer result.
	 * 
	 * @param elementUID
	 * @param answerIsGood
	 * @see Statistics
	 */
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

		/** One hour in miliseconds. */
		public static final int		HEURE_MS								= 3600000;

		/** SuccessRate weight in choice algorithm. */
		public static final float	FACTOR_SuccessRate						= 100;

		/** Maximum age (ms) to force the element to be quizzed, even if success rate is 100%. */
		public static float			FACTOR_LastQuestionMaxAge_MS			= HEURE_MS * 24 * 7;	// 7 days

		/** Minimum time to wait before to quizz a bad known element. */
		public static float			MIN_TimeToWaitBeforeQuizzBadElements	= 1000 * 60 * 2;		// 2 min

		/** NeedScore precision (must not be to precise for randomization to work fine), number of digits after the unit (0 = integer). */
		public static final int		NEED_SCORE_PRECISION					= 0;

		/** Size of the unknown series to roll, all the fist [UNKNOWN_SERIE_SIZE] unknown elements are loop quizzed. 0 disable the unknown series loop. */
		public static final int		UNKNOWN_SERIE_LOOP_SIZE					= 5;

		/** Unknown series current size. */
		public static int					unknownSerieLoopCurrentSize		= 0;

		/** Flag set to true if quizz is currently in unknown series. */
		transient private boolean	inUnknownSerie							= false;

		/** Number of time the element was quizzed. */
		private int					nbQuestions								= 0;

		/** Number of good answers for this element. */
		private int					nbGoodAnswers							= 0;

		/** Last date this element was quizzed. */
		private long				lastQuestionDate						= 0;
		
		protected void merge(Statistics oldStats)
		{
			nbQuestions += oldStats.nbQuestions;
			nbGoodAnswers += oldStats.nbGoodAnswers;
			lastQuestionDate = Math.max(lastQuestionDate, oldStats.lastQuestionDate);
		}

		/**
		 * Return number of time the element was quizzed.
		 * 
		 * @return number of time the element was quizzed.
		 */
		public int getNbQuestions()
		{
			return nbQuestions;
		}

		/**
		 * Number of good answers for this element.
		 * 
		 * @return Number of good answers for this element.
		 */
		public int getNbGoodAnswers()
		{
			return nbGoodAnswers;
		}

		/**
		 * Success rate defined such as {@code SuccessRate = nbGoodAnswers / nbQuestions}
		 * 
		 * @return success rate.
		 */
		public float getSuccessRate()
		{
			if (nbQuestions == 0) return 0;
			return (Float.valueOf(nbGoodAnswers) / Float.valueOf(nbQuestions));
		}

		/**
		 * Return last time this element was quizzed.
		 * 
		 * @return last time this element was quizzed.
		 */
		public long getLastQuestionAge()
		{
			return (System.currentTimeMillis() - lastQuestionDate);
		}

		/**
		 * Update this statistic with new answer.
		 * 
		 * @param answerIsGood
		 *            whether the answer was good or not.
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return String.format("\"%d\";\"%d\";\"%d\";\"%f\";\"%f\"", getLastQuestionAge(), getNbGoodAnswers(), getNbQuestions(), getSuccessRate(), getNeedScore());
		}
	}

	/**
	 * Return set of elements UID for which statistics are available.
	 * 
	 * @return set of elements UID for which statistics are available.
	 */
	public Set<String> getElementsUID()
	{
		return statistics.keySet();
	}

	/**
	 * Compute which element should be quizzed next from the given set of elements UID, according to the statistics of each elements.
	 * 
	 * @param elementsUID
	 *            Set of elementsUID from which to found the next element.
	 * @return String Element Unique ID
	 */
	public String getNextElement(Vector<String> elementsUID)
	{
		String currentElementUID = null;
		float bestScore = Float.NEGATIVE_INFINITY;
		float currentScore = bestScore;
		Vector<String> bestsUIDs = new Vector<String>();

		Iterator<String> itElements = elementsUID.iterator();
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
	 * Return printable statistics for a given element UID.
	 * 
	 * @param elementUID
	 *            of which we want statistic for.
	 * @return printable statistics.
	 */
	public String getElementStats(String elementUID)
	{
		Statistics stats = statistics.get(elementUID);
		if (stats == null) return null;

		StringBuilder sb = new StringBuilder(String.format("%d/%d = %.2f\t", stats.getNbGoodAnswers(), stats.getNbQuestions(), stats.getSuccessRate())); //$NON-NLS-1$

		long time = stats.getLastQuestionAge();

		sb.append("Age : "+MyUtils.timeToString(time) + "\t"); //$NON-NLS-1$
		sb.append(String.format(Messages.getString("LearningProfile.LearningProfile.Label.NeedScore") + " : %.2f", stats.getNeedScore())); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();
	}

	/**
	 * Add elements with empty statistics. If an element from neverSeenElements is already known and has statistics, a SEVERE error is logged, but no exception is thrown.
	 * 
	 * @param neverSeenElements
	 *            Set of elements UID of never seen elements.
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
	
	/**
	 * This method must be called every time LearningProfile is loaded/saved/imported/exported.
	 * It ensure compatibility with previous version (refactored class/packages tolerance).
	 */
	private void pack()
	{
		Map<String, Statistics> newStats = new TreeMap<String, Statistics>();
		
		Iterator<Entry<String, Statistics>> it = statistics.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String, Statistics> entry = it.next();
			String uid = entry.getKey();
			Statistics stats = entry.getValue();
			
			for(String name: RefactoringInfos.REFACTORED_PACKAGES.keySet())
			{
				Package pack = RefactoringInfos.REFACTORED_PACKAGES.get(name);
				
				if (uid.startsWith(name))
				{	
					String newUid = uid.replaceFirst(name, pack.getName());
					
					if (statistics.containsKey(newUid))
					{
						MyUtils.trace(Level.INFO, "Stats merge ("+uid+": "+stats+") and ("+newUid+": "+statistics.get(newUid)+")");
						statistics.get(newUid).merge(stats);
					}
					else
					{
						MyUtils.trace(Level.INFO, "Stats replace ("+uid+": "+stats+") by ("+newUid+": "+stats+")");
						newStats.put(newUid, stats);
					}
					
					it.remove();
					break;
				}
			}
		}
		
		statistics.putAll(newStats);
	}

	/**
	 * Export program, ask for LearningProfile source file, export file, and export it. 
	 * @param args not used.
	 */
	public static void main(String[] args)
	{
		BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
		LearningProfile profile = null;
		do
		{
			System.out.println("Profile file: ");
			try
			{
				String filename = fromKeyboard.readLine();
				profile = LearningProfile.open(new File(filename));
			}
			catch (Exception e)
			{
				System.err.println("Error: " + e.getMessage());
			}
		} while (profile == null);

		do
		{
			System.out.println("Export file: ");
			try
			{
				String filename = fromKeyboard.readLine();
				File file = new File(filename);
				profile.export(file);
				break;
			}
			catch (Exception e)
			{
				System.err.println("Error: " + e.getMessage());
			}
		} while (true);
	}
}
