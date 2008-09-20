/**
 * @author Escallier Pierre
 * @file DictionaryAnalyser.java
 * @date 9 juin 08
 */
package epsofts.KanjiNoSensei.metier;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.lang.model.element.ElementVisitor;
import javax.swing.SwingUtilities;

import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

import epsofts.KanjiNoSensei.metier.Dictionary.DictionaryElementAlreadyPresentException;
import epsofts.KanjiNoSensei.metier.Dictionary.IDictionaryAnalyser;
import epsofts.KanjiNoSensei.metier.DictionaryAnalyser.ComparableElement.BadElementFormatException;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.metier.elements.Sentence;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.OneStringList;
import epsofts.KanjiNoSensei.utils.MyUtils.BadStringFormatException;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;
import epsofts.KanjiNoSensei.vue.VueElement;

/**
 * TODO
 */
public class DictionaryAnalyser implements IDictionaryAnalyser
{
	private static final float			DEBUG_TRIGGER	= (float) 0.75;

	private static final JaroWinkler	jw				= new JaroWinkler();

	private static final Jaro			j				= new Jaro();

	private final Set<Float>			results			= new TreeSet<Float>();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		args = new String[] {"/media/data/Code/Java_Workspace/KanjiNoSensei/dico/dico.csv"};

		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		File dicoFile = null;

		if (args.length > 0)
		{
			dicoFile = new File(args[0]);
		}

		while (dicoFile == null || !dicoFile.exists() || !dicoFile.canRead())
		{
			System.out.println("Fichier dictionnaire *.csv: ");
			String s = keyboard.readLine();
			dicoFile = new File(s);
		}

		DictionaryAnalyser analyser = new DictionaryAnalyser();
		try
		{
			Dictionary dico = new Dictionary(null, analyser);
			dico.importFile(dicoFile);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Results:\n" + MyUtils.explainStats(analyser.results));
	}

	Comparator<String>	comparator	= MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd;

	public static class ComparableElement extends TreeMap<ComparableElement.TypeChamp, String[]>
	{
		private static enum TypeChamp
		{
			natural, significations, strokeorders, lecture, sound
		};

		private static Map<TypeChamp, Integer>	TypeChampPoids	= new HashMap<TypeChamp, Integer>();
		static
		{
			TypeChampPoids.put(TypeChamp.natural, 35);
			TypeChampPoids.put(TypeChamp.lecture, 35);
			TypeChampPoids.put(TypeChamp.significations, 10);
			TypeChampPoids.put(TypeChamp.strokeorders, 10);
			TypeChampPoids.put(TypeChamp.sound, 10);
		}

		/*
		 * public static class BadElementFormatRuntimeException extends RuntimeException { private final int elementId;
		 * 
		 * public BadElementFormatRuntimeException(int elementId, String msg) { super(msg); this.elementId = elementId; }
		 * 
		 * public int getElementId() { return elementId; } }
		 */

		public static class BadElementFormatException extends Exception
		{
			public BadElementFormatException(String msg)
			{
				super(msg);
			}
		}

		private static class SeuilComparator<T> implements Comparator<T>
		{

			private final int			seuil;

			private final Comparator<T>	comparator;

			/**
			 * 
			 */
			public SeuilComparator(int seuil, Comparator<T> comparator)
			{
				this.seuil = seuil;
				this.comparator = comparator;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(T o1, T o2)
			{
				int result = comparator.compare(o1, o2);
				return (result >= seuil) ? result : 0;
			}

		}

		private static final Comparator<String>	JW_COMPARATOR		= new Comparator<String>()
																	{

																		@Override
																		public int compare(String o1, String o2)
																		{
																			String wo1 = null, wo2 = null;

																			try
																			{
																				wo1 = MyUtils.simplifiedString(o1);
																				wo2 = MyUtils.simplifiedString(o2);
																			}
																			catch (BadStringFormatException e)
																			{
																				throw new RuntimeException(e);
																			}

																			return (int) (j.getSimilarity(wo1, wo2) * 100);
																		}
																	};

		private static final Comparator<String>	EXACT_COMPARATOR	= new Comparator<String>()
																	{

																		@Override
																		public int compare(String o1, String o2)
																		{
																			if ((o1 == null) || (o2 == null)) return 0;

																			try
																			{
																				String wo1 = MyUtils.simplifiedString(o1);
																				String wo2 = MyUtils.simplifiedString(o2);

																				if (wo1.compareTo(wo2) == 0)
																				{
																					return 100;
																				}
																			}
																			catch (BadStringFormatException e)
																			{
																				throw new RuntimeException(e);
																			}

																			return 0;
																		}
																	};

		/**
		 * Constructor, convert the given Element.
		 * 
		 * @throws ClassNotFoundException
		 *             If e subclass is not supported.
		 */
		public ComparableElement(Element e) throws ClassNotFoundException
		{
			if (Kanji.class.isInstance(e))
			{
				Kanji kanji = Kanji.class.cast(e);
				put(TypeChamp.natural, new String[] {kanji.getCodeUTF8().toString()});
				put(TypeChamp.significations, kanji.getSignificationsSet().toArray(new String[kanji.getSignificationsSet().size()]));
				put(TypeChamp.strokeorders, new String[] {kanji.getStrokeOrderPicture()});
			}
			else if (Word.class.isInstance(e))
			{
				Word word = Word.class.cast(e);

				put(TypeChamp.natural, new String[] {word.getWord()});
				put(TypeChamp.significations, word.getSignificationsSet().toArray(new String[word.getSignificationsSet().size()]));
				put(TypeChamp.lecture, new String[] {word.getLecture()});
				put(TypeChamp.sound, new String[] {word.getSoundFile()});
			}
			else if (Sentence.class.isInstance(e))
			{
				Sentence sentence = Sentence.class.cast(e);

				put(TypeChamp.natural, new String[] {sentence.getSentence()});
				put(TypeChamp.significations, sentence.getSignificationsSet().toArray(new String[sentence.getSignificationsSet().size()]));
				put(TypeChamp.lecture, new String[] {sentence.getLecture()});
				put(TypeChamp.sound, new String[] {sentence.getSoundFile()});
			}
			else
			{
				throw new ClassNotFoundException("Unknown Element subclass \"" + e.getClass().getName() + "\"");
			}
		}

		/**
		 * This function convert any Element subclass (actualy Kanji, Word, Sentence) to a common comparable element object.
		 * 
		 * @see ComparableElement
		 * @param e
		 *            Element to convert to ComparableElement;
		 * @return ComparableElement from the given Element.
		 * @throws ClassNotFoundException
		 *             If e subclass is not supported.
		 */
		static public ComparableElement convertToComparableElement(Element e) throws ClassNotFoundException
		{
			return new ComparableElement(e);
		}

		static public float compare(Element e1, Element e2) throws ClassNotFoundException, BadElementFormatException
		{
			return compare(new ComparableElement(e1), new ComparableElement(e2));
		}

		static public int compare(String[] set1, String[] set2, Comparator<String> comparator)
		{
			int maxSimilarity = 0;
			for (String s1 : set1)
			{
				for (String s2 : set2)
				{
					int similarity = comparator.compare(s1, s2);
					maxSimilarity = Math.max(maxSimilarity, similarity);
				}
			}

			return maxSimilarity;
		}

		/**
		 * 
		 * @param e1
		 * @param e2
		 * @throws BadElementFormatRuntimeException
		 * @return
		 * @throws BadElementFormatException
		 */
		static public float compare(ComparableElement e1, ComparableElement e2) throws BadElementFormatException
		{
			Iterator<TypeChamp> it1 = e1.keySet().iterator();

			int maxSimilarity = 0;
			int weightSum = 0;
			int similarity = 0;
			int currentSimilarity = 0;

			while (it1.hasNext())
			{
				TypeChamp key1 = it1.next();

				Iterator<TypeChamp> it2 = e2.keySet().iterator();
				while (it2.hasNext())
				{
					TypeChamp key2 = it2.next();

					if (key1.compareTo(key2) == 0)
					{
						try
						{
							switch (key1)
							{
							case lecture:
							case natural:
							{
								currentSimilarity = compare(e1.get(key1), e2.get(key2), JW_COMPARATOR);
								break;
							}
							case significations:
							{
								currentSimilarity = compare(e1.get(key1), e2.get(key2), new SeuilComparator<String>(90, JW_COMPARATOR));
								break;
							}
							case sound:
							case strokeorders:
							{
								currentSimilarity = compare(e1.get(key1), e2.get(key2), EXACT_COMPARATOR);
							}
							}
						}
						catch (RuntimeException re)
						{
							if (BadStringFormatException.class.isInstance(re.getCause()))
							{
								BadStringFormatException bsoe = BadStringFormatException.class.cast(re.getCause());
								throw new BadElementFormatException(bsoe.getMessage());
							}
							else
							{
								throw re;
							}
						}

						maxSimilarity = Math.max(maxSimilarity, currentSimilarity);
						weightSum += TypeChampPoids.get(key1);
						similarity += (TypeChampPoids.get(key1) * currentSimilarity);
						break;
					}
				}
			}

			// return maxSimilarity / 100;
			return (weightSum > 0) ? ((float) similarity / (float) weightSum) / 100 : 0;
		}
	}

	/*
	 * private int compare(Element e1, Element e2) { int score = 0; int nbTests = 1;
	 * 
	 * 
	 * // Only Element attributes are compared Integer[] nbTestsScore = testStringsSet(e1.getSignificationsSet(), e2.getSignificationsSet()); nbTests += nbTestsScore[0]; score += nbTestsScore[1];
	 * 
	 * if (e1.getClass() == e2.getClass()) { // Element are from the same class if (Kanji.class.isInstance(e1)) { Kanji k1 = (Kanji) e1; Kanji k2 = (Kanji) e2;
	 * 
	 * // UTF8 nbTests++ ; if (comparator.compare(k1.getCodeUTF8().toString(), k2.getCodeUTF8().toString()) == 0) { score++ ; }
	 * 
	 * nbTestsScore = testStringsSet(k1.getLecturesKUNSet(), k2.getLecturesKUNSet()); nbTests += nbTestsScore[0]; score += nbTestsScore[1];
	 * 
	 * nbTestsScore = testStringsSet(k1.getLecturesONSet(), k2.getLecturesONSet()); nbTests += nbTestsScore[0]; score += nbTestsScore[1]; }
	 * 
	 * if (Word.class.isInstance(e1)) { Word w1 = (Word) e1; Word w2 = (Word) e2;
	 * 
	 * nbTests++ ; if (comparator.compare(w1.getLecture(), w2.getLecture()) == 0) { score++ ; } }
	 * 
	 * if (Sentence.class.isInstance(e1)) { Sentence s1 = (Sentence) e1; Sentence s2 = (Sentence) e2;
	 * 
	 * nbTests++ ; if (comparator.compare(s1.getLecture(), s2.getLecture()) == 0) { score++ ; } } }
	 * 
	 * return (score 100 / nbTests); }
	 */

	private Integer[] testStringsSet(Collection<String> e1, Collection<String> e2)
	{
		int nbTests = 0;
		int score = 0;

		Iterator<String> it1 = e1.iterator();
		String currentString = null;
		while (it1.hasNext())
		{
			nbTests++ ;
			currentString = it1.next();
			Iterator<String> it2 = e2.iterator();
			while (it2.hasNext())
			{
				// If currents match
				if (comparator.compare(currentString, it2.next()) == 0)
				{
					score++ ;
					break;
				}
			}
		}

		return new Integer[] {nbTests, score};
	}

	private Set<String>	ignoredElementUID	= new HashSet<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see epsofts.KanjiNoSensei.metier.Dictionary.IDictionaryAnalyser#canElementBeAdded(epsofts.KanjiNoSensei.metier.Dictionary, epsofts.KanjiNoSensei.metier.elements.Element)
	 */
	@Override
	public boolean addElement(Dictionary dictionary, Element element)
	{
		MyUtils.trace(Level.FINEST, "addElement " + element.getKey());

		Set<String> ignoredThemes = new HashSet<String>();
		ignoredThemes.add("hiragana");
		ignoredThemes.add("katakana");

		for(String s: ignoredThemes)
		{

			if (element.getThemes().matches(".*"+s+".*"))
			{
				return false;
			}
		}

		TreeMap<String, Element> elements = dictionary.getElements();
		Iterator<Element> it = elements.values().iterator();
		float currentResult = 0;
		float mostSimilarElementResult = 0;
		Element mostSimilarElement = null;
		final int loop = 20;
		int i = 0;

		while (it.hasNext())
		{
			++i;
			if (i % loop == 0)
			{
				System.out.println(i + "]\t" + MyUtils.explainStats(results));
			}

			Element current = it.next();

			if (ignoredElementUID.contains(current.getKey()))
			{
				continue;
			}

			try
			{
				currentResult = ComparableElement.compare(element, current);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				continue;
			}
			catch (BadElementFormatException befe)
			{
				// As "current" was already compared to previous element, the bad formated element must be the new "element".
				ignoredElementUID.add(element.getKey());
				System.out.format("Element suivant mal formaté:\n%s\nErreur: %s", element.exportString(), befe.getMessage());
				
				try
				{
					final VueElement vue = VueElement.genererVueElement(element, false);
					final Element[] newElementRef = new Element[]{null};
					
					MyUtils.InvokeAndWaitEDT(new Runnable()
					{
					
						@Override
						public void run()
						{
							newElementRef[0] = vue.editerElement();
						}
					});
					
					if (newElementRef[0] != null)
					{
						// No need to remove, element has not been added yet
						// dictionary.removeElement(element);
						try
						{
							dictionary.addElement(newElementRef[0]);
							
							// We assume have added a replacement element.
							return true;
						}
						catch (DictionaryElementAlreadyPresentException e1)
						{
							System.out.println(Messages.getString("KanjiNoSensei.ErrorElementAlreadyPresent") + " : " + e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
							
							// On error, we let the old element be added to dictionary.
							return false;
						}
					}
				}
				catch (Exception e)
				{
					System.err.println("VueElement Error: "+e.getMessage());
				}
				finally
				{
					try
					{
						System.in.read();
					}
					catch(Exception e)
					{
						// Nothing.
					}
				}
				
				return false;
			}
			results.add(currentResult);

			if (currentResult > DEBUG_TRIGGER)
			{
				System.out.format("%s\n%s\nProbabilité de doublons: %f", current.exportString(), element.exportString(), currentResult);
				try
				{
					// pause
					System.in.read();
				}
				catch (IOException e)
				{
					// Nothing
				}
			}

			if (currentResult > mostSimilarElementResult)
			{
				mostSimilarElementResult = currentResult;
				mostSimilarElement = current;
			}
		}

		return false;
	}

}
