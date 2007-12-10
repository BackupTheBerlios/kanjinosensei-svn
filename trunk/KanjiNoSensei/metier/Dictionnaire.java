/**
 * 
 */
package metier;

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
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import metier.elements.Element;
import utils.MyUtils;

/**
 * @author Axan
 * 
 */
public class Dictionnaire implements Serializable
{
	public static class DictionnaireNoMoreElementException extends Exception
	{

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;

	}

	private static final long			serialVersionUID	= 1L;
	private static final Random			dice				= new Random();

	private TreeMap<String, Element>	elements			= new TreeMap<String, Element>();

	private Dictionnaire(TreeMap<String, Element> i_elements)
	{
		elements = i_elements;
	}

	/**
	 * @param fic
	 * @throws FileNotFoundException
	 */
	public void exporter(File fic) throws FileNotFoundException
	{
		PrintStream ps = new PrintStream(fic);

		/*
		 * String hiragana = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん";
		 * String katakana = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン";
		 * String[] romaji =
		 * {"a","i","u","e","o","ka","ki","ku","ke","ko","sa","shi","su","se","so","ta","chi","tsu","te","to","na","ni","nu","ne","no","ha","hi","fu","he","ho","ma","mi","mu","me","mo","ya","yu","yo","ra","ri","ru","re","ro","wa","wo","n"};
		 * 
		 * for(int i=0; i < hiragana.length(); ++i) { Kanji kanji = new
		 * Kanji(hiragana.charAt(i),
		 * "dico/hiragana/"+hiragana.charAt(i)+"-bw.png", "", romaji[i], "",
		 * "kanji, hiragana"); kanji.pack(); ps.println(kanji.export()); }
		 * 
		 * for(int i=0; i < katakana.length(); ++i) { Kanji kanji = new
		 * Kanji(katakana.charAt(i),
		 * "dico/katakana/"+katakana.charAt(i)+"-bw.png", "", romaji[i], "",
		 * "kanji, katakana"); kanji.pack(); ps.println(kanji.export()); }
		 */

		Iterator<String> it_element = elements.keySet().iterator();
		while (it_element.hasNext())
		{
			Element element = elements.get(it_element.next());
			element.pack();
			ps.println(element.export());
		}

		ps.close();

	}

	public void enregistrer(File dest_dico) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dest_dico));

		Iterator<String> it_element = elements.keySet().iterator();
		while (it_element.hasNext())
		{
			Element element = elements.get(it_element.next());
			element.pack();
			oos.writeObject(element);
		}

		oos.close();
	}

	/**
	 * @param string
	 * @throws IOException 
	 */
	public Dictionnaire(File source_dico) throws IOException
	{
		if (source_dico != null)
		{
			ouvrir(source_dico);
		}
		else
		{
			System.err.println("Fichier dictionnaire non fourni, dictionnaire vide utilisé.");
		}
	}

	public void importer(File fic) throws IOException
	{

		FileInputStream fis = new FileInputStream(fic);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		elements.clear();

		String current = null;

		String line = null;
		int lineNb = 0;
		while (br.ready())
		{
			line = br.readLine();
			++lineNb;

			current = MyUtils.stripQuotes(line.substring(0, line.indexOf(Element.EXPORT_SEPARATOR)));
			current = current.replace("metier.Kanji", "metier.elements.Kanji").replace("metier.Mot", "metier.elements.Mot");

			try
			{
				Element element = Element.genererElementImport(current, line);
				ajouterElement(element);
			}
			catch (Exception e)
			{
				if (current.contains("."))
				{
					e.printStackTrace();
					System.err.println("Erreur import element \"" + line + "\" : " + e.getMessage());
				}
				else
				{
					System.err.println("Ligne ignorée \"" + line + "\"");
				}
			}
		}
	}

	public void ouvrir(File source_dico) throws IOException
	{

		FileInputStream fis = new FileInputStream(source_dico);
		ObjectInputStream ois = new ObjectInputStream(fis);

		elements.clear();

		Object obj = null;
		while (fis.available() > 0)
		{
			try
			{
				obj = ois.readObject();
				
				if (!Element.class.isInstance(obj)) throw new ClassNotFoundException();
				
				
				Element element = (Element) obj;
				element.pack();
				ajouterElement(element);
			}
			catch (ClassNotFoundException e)
			{
				System.err.println("Instance d'Element attendue, objet ignoré : \""+e.getMessage()+"\"");
			}
		}

		ois.close();
		fis.close();
	}

	public void ajouterElement(Element element)
	{
		if (elements.containsKey(element.getKey()))
		{
			throw new RuntimeException("L'élément '" + element.toString() + "' existe déjà.");
		}

		elements.put(element.getKey(), element);
	}

	public Set<String> listerThemes(String debut)
	{
		if (debut == null) debut = "";

		Set<String> themes = new TreeSet<String>();

		Iterator<String> itElements = elements.keySet().iterator();
		while (itElements.hasNext())
		{
			Element element = elements.get(itElements.next());
			themes.addAll(element.getThemesSet(debut));
		}

		return themes;
	}

	public Set<String> listerThemes()
	{
		return listerThemes(null);
	}

	public Dictionnaire getSousDictionnaire(Set<String> i_listeThemes)
	{
		TreeMap<String, Element> sousListeElements = new TreeMap<String, Element>();

		Iterator<String> itElements = elements.keySet().iterator();
		while (itElements.hasNext())
		{
			Element element = elements.get(itElements.next());

			if (element.testerThemes(i_listeThemes))
			{
				sousListeElements.put(element.getKey(), element);
			}
		}

		return new Dictionnaire(sousListeElements);
	}

	/**
	 * @param deja_vus
	 * @return
	 * @throws DictionnaireNoMoreElementException
	 */
	public Element getRandomElement(Vector<Element> deja_vus) throws DictionnaireNoMoreElementException
	{
		Vector<Element> dico = new Vector<Element>(elements.values());
		if (deja_vus != null) dico.removeAll(deja_vus);

		if (dico.isEmpty()) throw new DictionnaireNoMoreElementException();

		return dico.get(dice.nextInt(dico.size()));
	}

	public Element chercherElement(String key)
	{
		if ( !elements.containsKey(key))
		{
			return null;
		}

		return elements.get(key);
	}

	@SuppressWarnings("unchecked")
	public Dictionnaire clone()
	{
		TreeMap<String, Element> clonedElements = (TreeMap<String, Element>) elements.clone();
		return new Dictionnaire(clonedElements);
	}

	/**
	 * @param elementQuestionEnCours
	 */
	public void retirerElement(Element element)
	{
		if (elements.containsKey(element.getKey()))
		{
			elements.remove(element.getKey());
		}
	}

	/**
	 * @param filtreElement
	 * @return
	 */
	public Set<Element> listerElements(String debut)
	{
		if (debut == null) debut = "";

		Set<Element> elementsReponses = new TreeSet<Element>();

		Iterator<String> it = elements.keySet().iterator();
		while (it.hasNext())
		{
			Element element = elements.get(it.next());

			if (element.correspondA(debut))
			{
				elementsReponses.add(element);
			}
		}

		return elementsReponses;		
	}

}
