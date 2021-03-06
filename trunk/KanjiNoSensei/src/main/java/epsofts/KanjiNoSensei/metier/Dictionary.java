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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;

import epsofts.KanjiNoSensei.RefactoringInfos;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.RefactoredClassNameTolerantObjectInputStream;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;

/**
 * Class that represent a dictionary with all available elements. A dictionary is opened with a source file (*.kjd). It can be exported/imported with *.csv extension, the file structure depends on the elements the dictionary contains.
 * 
 * @author Escallier Pierre
 */
public class Dictionary implements Serializable
{
	/**
	 * Interface used to implement just in time dictionary sorters.
	 */
	public static interface DictionarySorter
	{
		/**
		 * This method is called for each element of the dictionary, and must return a boolean value that define if the current element will be selected.
		 * 
		 * @param e
		 *            Current element to test.
		 * @return true if the current element must be selected, false if it must be filtered.
		 */
		boolean testElement(Element e);
	}
	
	/**
	 * Interface to define dictionary analysers.
	 * Dictionary analysers are sort of listener called by Dictionary on some events, they may be used to extend or profile the Dictionary use.
	 */
	public static interface IDictionaryAnalyser
	{

		/** Called by {@link Dictionary#addElement(Element)}, this method can access the current Dictionary to work, and must return a boolean to say if the element must or be added after this method call or not.
		 * @param dictionary Dictionary listened.
		 * @param element Element to be added.
		 * @return true if the element must be added to the dictionary after this method call, false if not.
		 */
		boolean addElement(Dictionary dictionary, Element element);
		
	}

	/**
	 * Exception class, thrown when no element is available to match a correct result of the calling method.
	 */
	public static class DictionaryNoMoreElementException extends Exception
	{
		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

	}

	/**
	 * Exception class, thrown when trying to add an element already present in the dictionary.
	 */
	public static class DictionaryElementAlreadyPresentException extends Exception
	{

		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

		/**
		 * Constructor, with informations fields.
		 * 
		 * @param alreadyPresent
		 *            The previous corresponding element in the dictionary.
		 * @param candidate
		 *            The element candidate to replace.
		 */
		DictionaryElementAlreadyPresentException(Element alreadyPresent, Element candidate)
		{
			super(Messages.getString("Dictionary.Exception.ElementAlreadyPresent") + " : " + alreadyPresent.exportString() + "\t" + candidate.exportString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	/** Serialization version. */
	private static final long			serialVersionUID	= 1L;

	/** Random object use to generate random data */
	private static final Random			dice				= new Random();

	/** Collection of all elements of the dictionnary. Ordered by a key that should always be {@link Element#getKey()}. */
	private TreeMap<String, Element>	elements			= new TreeMap<String, Element>();

	/** Dictionary Analyser, if none, null. */
	private IDictionaryAnalyser			analyser			= null;

	/**
	 * Getter for elements map.
	 * 
	 * @return elements map.
	 */
	protected TreeMap<String, Element> getElements()
	{
		return elements;
	}

	/**
	 * Construction from existing element collection.
	 * 
	 * @param i_elements
	 *            existing element collection.
	 */
	private Dictionary(TreeMap<String, Element> elements)
	{
		this.elements = elements;
	}

	/**
	 * Export method. Open the export file and write all elements sorted by key (it includes by-type sorting).
	 * 
	 * @param file
	 *            Destination file to export.
	 * @throws FileNotFoundException
	 *             if file is not found.
	 */
	public void exportFile(File file) throws FileNotFoundException
	{
		PrintStream ps = null;
		try
		{
			ps = new PrintStream(file);
			// We put in file stream all element one by one.
			Iterator<String> it_element = elements.keySet().iterator();
			while (it_element.hasNext())
			{
				Element element = elements.get(it_element.next());
				element.pack();
				ps.println(element.exportString());
			}
		}
		finally
		{
			if (ps != null) ps.close();
		}
	}

	/**
	 * Save the dictionary in a binary file (*.kjd) using ObjectOutputStream.
	 * 
	 * @param file
	 *            Destination file to save the dictionary.
	 * @throws IOException
	 *             on any Input/Output error.
	 */
	public void save(File file) throws IOException
	{
		ObjectOutputStream oos = null;

		try
		{
			oos = new ObjectOutputStream(new FileOutputStream(file));

			// We put in ObjectOutputStream all elements one by one.
			Iterator<String> it_element = elements.keySet().iterator();
			while (it_element.hasNext())
			{
				Element element = elements.get(it_element.next());
				element.pack();
				oos.writeObject(element);
			}
		}
		finally
		{
			try
			{
				if (oos != null) oos.close();
			}
			catch(Exception e)
			{
				// Nothing.
			}
		}
	}

	/**
	 * Dictionary constructor using source file to open (*.kjd). The file must have been saved with {@link Dictionary#save(File)}.
	 * 
	 * @param file
	 *            Source file to open.
	 * @throws IOException
	 *             on any Input/Output error.
	 */
	public Dictionary(File file) throws IOException
	{
		if (file != null)
		{
			open(file);
		}
		else
		{
			KanjiNoSensei.log(Level.WARNING, Messages.getString("Dictionary.Warning.NoDictionnaryFile")); //$NON-NLS-1$
		}
	}

	/**
	 * Dictionary constructor using source file to open (*.kjd) and a IDictionaryAnalyser.
	 * 
	 * @param file
	 *            Source file to open.
	 * @param analyser
	 *            Dictionary Analyser to use.
	 * @throws IOException
	 *             On file error.
	 */
	public Dictionary(File file, IDictionaryAnalyser analyser) throws IOException
	{
		this.analyser = analyser;

		if (file != null)
		{
			open(file);
		}
		else
		{
			KanjiNoSensei.log(Level.WARNING, Messages.getString("Dictionary.Warning.NoDictionnaryFile")); //$NON-NLS-1$
		}
	}

	/**
	 * Import elements from *.csv file. Previous collection is cleared. Invalids lines or unavailables elements class are ignored, no Exception is thrown but an error is logged.
	 * 
	 * @param file
	 *            Source file to import.
	 * @throws IOException
	 *             on any Input/Output error.
	 */
	public void importFile(File file) throws IOException
	{
		// Open file stream
		BufferedReader br = null;

		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			elements.clear();

			String current = null;

			// For each line of the file
			String line = null;
			int lineNb = 0, end = -1;
			while (br.ready())
			{
				line = br.readLine();
				++lineNb;

				end = (line.contains(Element.EXPORT_SEPARATOR) ? line.indexOf(Element.EXPORT_SEPARATOR) : line.length());
				current = MyUtils.stripQuotes(line.substring(0, end));

				// Try to translate it as an element
				try
				{
					Element element = Element.generateImportedElement(current, line);
					addElement(element);
				}
				catch (Exception e)
				{
					// If translation fails, we try to guess if line is just a useless one, or if there was realy an element on it, and then we display apropriate error message.
					if (current.contains(".")) //$NON-NLS-1$
					{
						if ( !DictionaryElementAlreadyPresentException.class.isInstance(e))
						{
							e.printStackTrace();
						}
						KanjiNoSensei.log(Level.SEVERE, Messages.getString("Dictionary.Import.ErrorOnElement") + " : \"" + line + "\" : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
					else
					{
						KanjiNoSensei.log(Level.WARNING, Messages.getString("Dictionary.Import.WarningLineIgnored") + " : \"" + line + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
				}
			}
		}
		finally
		{
			try
			{
				if (br != null) br.close();
			}
			catch(Exception e)
			{
				// Nothing.
			}
		}
	}

	/**
	 * Open dictionary from binary file (*.kjd). Previous collection is cleared. Unavailable class elements are ignored but an error is logged.
	 * 
	 * @param file
	 *            Dictionary file to open.
	 * @throws IOException
	 *             on any Input/Output error.
	 */
	public void open(File file) throws IOException
	{
		KanjiNoSensei.log(Level.INFO, Messages.getString("Dictionary.OpeningFile") + " \"" + file.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// Open file stream
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);
			ois = new RefactoredClassNameTolerantObjectInputStream(fis, RefactoringInfos.REFACTORED_PACKAGES);

			elements.clear();

			// While objects are available on stream, add them to the dictionnary.
			Object obj = null;
			while (fis.available() > 0) // TODO: ObjectInputStream.available() == FileInputStream.available() ?
			{
				Element element = null;
				try
				{
					obj = ois.readObject();

					if ( !Element.class.isInstance(obj)) throw new ClassNotFoundException();

					element = (Element) obj;
					element.pack();
					addElement(element);
				}
				catch (ClassNotFoundException e)
				{
					KanjiNoSensei.log(Level.SEVERE, Messages.getString("Dictionary.Open.ErrorOnElement") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
				}
				catch (DictionaryElementAlreadyPresentException e)
				{
					KanjiNoSensei.log(Level.WARNING, Messages.getString("Dictionary.Open.WarningElementAlreadyPresent") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		finally
		{
			try
			{
				if (fis != null) fis.close();
				if (ois != null) ois.close();				
			}
			catch(Exception e)
			{
				// Nothing.
			}
		}
	}

	/**
	 * Add an element to the collection. If the element is already present, throw DictionaryElementAlreadyPresentException.
	 * If analyser is set, {@link IDictionaryAnalyser#addElement(Dictionary, Element)} is called before.
	 * @param element
	 *            Element to add to the dictionary.
	 * @throws DictionaryElementAlreadyPresentException
	 *             if the element is already present in the dictionary.
	 */
	public void addElement(Element element) throws DictionaryElementAlreadyPresentException
	{
		if ((analyser == null) || !analyser.addElement(this, element))
		{
			if (elements.containsKey(element.getKey()))
			{
				throw new DictionaryElementAlreadyPresentException(elements.get(element.getKey()), element);
			}

			elements.put(element.getKey(), element);
		}
	}

	/**
	 * List all the elements themes wich contains the "pattern" string. If pattern is "" or null, return all themes.
	 * 
	 * @param pattern
	 *            Pattern used to filter the wanted themes.
	 * @return Set of all elements themes wich matches the beginning string.
	 */
	public Set<String> getThemesList(String pattern)
	{
		if (pattern == null) pattern = ""; //$NON-NLS-1$

		Set<String> themes = new TreeSet<String>();

		Iterator<String> itElements = elements.keySet().iterator();
		while (itElements.hasNext())
		{
			Element element = elements.get(itElements.next());
			themes.addAll(element.getThemesContaining(pattern));
		}

		return themes;
	}

	/**
	 * Return all elements themes. As getThemesList(null) would do.
	 * 
	 * @return Set of all elements themes.
	 */
	public Set<String> getThemesList()
	{
		return getThemesList(null);
	}

	/**
	 * Return a sub dictionary containing only the elements of the given themes list.
	 * 
	 * @param themesList
	 *            List of wanted themes.
	 * @return Dictionary of the elements of this wich match to at least one of the given themes.
	 */
	public Dictionary getSubDictionary(Set<String> themesList)
	{
		TreeMap<String, Element> sousListeElements = new TreeMap<String, Element>();

		Iterator<String> itElements = elements.keySet().iterator();
		while (itElements.hasNext())
		{
			Element element = elements.get(itElements.next());

			if (Word.class.isInstance(element))
			{
				element = element;
			}
			if (element.testThemes(themesList))
			{
				sousListeElements.put(element.getKey(), element);
			}
		}

		return new Dictionary(sousListeElements);
	}

	/**
	 * Return a random element from the dictionary. Avoiding ones which are in the alreadySeen list.
	 * 
	 * @param alreadySeen
	 *            List of the already seen elements, this elements can't be returned by this method.
	 * @return A random element if a non seen one is available, DictionaryNoMoreElementException is thrown if not.
	 * @throws DictionaryNoMoreElementException
	 *             when all dictionary elements are in alreadySeen list.
	 */
	public Element getRandomElement(Vector<Element> alreadySeen) throws DictionaryNoMoreElementException
	{
		Vector<Element> dico = new Vector<Element>(elements.values());
		if (alreadySeen != null) dico.removeAll(alreadySeen);

		if (dico.isEmpty()) throw new DictionaryNoMoreElementException();

		return dico.get(dice.nextInt(dico.size()));
	}

	/**
	 * Return a random element from the dictionary, chosen by LearningProfile algorithm, avoiding ones which are in the alreadySeen list.
	 * @param alreadySeen List of the already seen elements, this elements can't be return by this method.
	 * @param learningProfile Learning profile to use for element choice algorithm.
	 * @return A random element chosen by learning profile algorithm.
	 * @throws DictionaryNoMoreElementException if there's no more element available.
	 */
	public Element getNextElementFromLearningProfile(Vector<Element> alreadySeen, LearningProfile learningProfile) throws DictionaryNoMoreElementException
	{
		Vector<String> dico = new Vector<String>(elements.keySet());

		if (alreadySeen != null)
		{
			Iterator<Element> itElements = alreadySeen.iterator();
			while (itElements.hasNext())
			{
				dico.remove(itElements.next().getKey());
			}
		}

		if (dico.isEmpty()) throw new DictionaryNoMoreElementException();

		Vector<String> neverSeenElements = (Vector<String>) dico.clone();
		neverSeenElements.removeAll(learningProfile.getElementsUID());

		// Never seen elements are return in first.
		if (neverSeenElements.size() > 0)
		{
			learningProfile.addNeverSeenElements(neverSeenElements);
		}

		// When the learning profile knows all elements, then it can be used to get the next element according to the user statistics.
		return elements.get(learningProfile.getNextElement(dico));
	}

	/**
	 * Return Element by key.
	 * 
	 * @param key
	 *            Key of the wanted element.
	 * @return The element if key is present on the dictionary, null if not (no exception are thrown).
	 */
	public Element getElement(String key)
	{
		if ( !elements.containsKey(key))
		{
			return null;
		}

		return elements.get(key);
	}

	/**
	 * Clone this dictionary.
	 * 
	 * @return cloned dictionary.
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public Dictionary clone()
	{
		TreeMap<String, Element> clonedElements = (TreeMap<String, Element>) elements.clone();
		return new Dictionary(clonedElements);
	}

	/**
	 * Delete given element from the dictionary. Work similar to : <code>elements.remove(element.getKey())</code>
	 * 
	 * @param element
	 *            Element we want to delete the key from the dictionary.
	 */
	public void removeElement(Element element)
	{
		if (elements.containsKey(element.getKey()))
		{
			elements.remove(element.getKey());
		}
	}

	/**
	 * Get a list of all dictionary elements wich start with given beginning string.
	 * 
	 * @param beginning
	 *            Beginning string to filter elements.
	 * @return List of elements wich match the beginning filter.
	 */
	public Set<Element> getElementsList(String beginning)
	{
		if ((beginning == null) || (beginning.isEmpty()))
		{
			return new TreeSet<Element>(elements.values());
		}
		
		final String finalBeginning = beginning;

		return getElementsSelection(new DictionarySorter()
		{

			@Override
			public boolean testElement(Element e)
			{
				try
				{
					return e.match(finalBeginning);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					return false;
				}
			}

		});

	}

	public Set<Element> getElementsSelection(DictionarySorter sorter)
	{
		Set<Element> elementsReponses = new TreeSet<Element>();

		Iterator<String> it = elements.keySet().iterator();
		while (it.hasNext())
		{
			Element element = elements.get(it.next());

			if (sorter.testElement(element))
			{
				elementsReponses.add(element);
			}
		}

		return elementsReponses;
	}

	/**
	 * @return
	 */
	public int getNbElements()
	{
		return elements.size();
	}

}
