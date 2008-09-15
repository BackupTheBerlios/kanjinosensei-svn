/**
 * @author Escallier Pierre
 * @file DictionaryAnalyser.java
 * @date 9 juin 08
 */
package epsofts.KanjiNoSensei.metier;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.metier.elements.Sentence;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.OneStringList;



/**
 * 
 */
public class DictionaryAnalyser
{
	private Dictionary dico = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DictionaryAnalyser analyser = new DictionaryAnalyser();
		try
		{
			Dictionary dico = new Dictionary(new File(args[0]), analyser);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param dictionary
	 */
	public void setDictionary(Dictionary dictionary)
	{
		dico = dictionary;
	}

	/**
	 * @param element
	 * @return True if element were added within this method code, false if Dictionary still have to do it.
	 */
	public boolean addElement(Element element)
	{
		MyUtils.trace(Level.FINEST, "addElement "+element.getKey());
		TreeMap<String, Element> elements = dico.getElements();
		Iterator<Element> it = elements.values().iterator();
		int currentResult = Integer.MIN_VALUE;
		int mostSimilarElementResult = Integer.MIN_VALUE;
		Element mostSimilarElement = null;
		
		while(it.hasNext())
		{
			Element current = it.next();
			currentResult = compare(element, current);
			if (currentResult > mostSimilarElementResult)
			{
				mostSimilarElementResult = currentResult;
				mostSimilarElement = current;
			}
		}
		
		return false;
	}
	
	Comparator<String> comparator = MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd;
	
	private int compare(Element e1, Element e2)
	{
		int score = 0; int nbTests = 1;
		
		// Only Element attributes are compared
		Integer[] nbTestsScore = testStringsSet(e1.getSignificationsSet(), e2.getSignificationsSet());
		nbTests += nbTestsScore[0];
		score += nbTestsScore[1];
		
		if (e1.getClass() == e2.getClass())
		{
			// Element are from the same class
			if (Kanji.class.isInstance(e1))
			{
				Kanji k1 = (Kanji) e1;
				Kanji k2 = (Kanji) e2;
				
				// UTF8
				nbTests++;
				if (comparator.compare(k1.getCodeUTF8().toString(), k2.getCodeUTF8().toString()) == 0)
				{
					score++;
				}
				
				nbTestsScore = testStringsSet(k1.getLecturesKUNSet(), k2.getLecturesKUNSet());
				nbTests += nbTestsScore[0];
				score += nbTestsScore[1];
				
				nbTestsScore = testStringsSet(k1.getLecturesONSet(), k2.getLecturesONSet());
				nbTests += nbTestsScore[0];
				score += nbTestsScore[1];
			}
			
			if (Word.class.isInstance(e1))
			{
				Word w1 = (Word) e1;
				Word w2 = (Word) e2;
				
				nbTests++;
				if (comparator.compare(w1.getLecture(), w2.getLecture()) == 0)
				{
					score++;
				}
			}
			
			if (Sentence.class.isInstance(e1))
			{
				Sentence s1 = (Sentence) e1;
				Sentence s2 = (Sentence) e2;
				
				nbTests++;
				if (comparator.compare(s1.getLecture(), s2.getLecture()) == 0)
				{
					score++;
				}
			}
		}
		
		return (score*100 / nbTests);
	}
	
	private Integer[] testStringsSet(Collection<String> e1, Collection<String> e2)
	{
		int nbTests = 0;
		int score = 0;
		
		Iterator<String> it1 = e1.iterator();
		String currentString = null;
		while(it1.hasNext())
		{
			nbTests++;
			currentString = it1.next();
			Iterator<String> it2 = e2.iterator();
			while(it2.hasNext())
			{
				// If currents match
				if (comparator.compare(currentString, it2.next()) == 0)
				{
					score++;
					break;
				}
			}
		}
		
		return new Integer[]{nbTests, score};
	}

}
