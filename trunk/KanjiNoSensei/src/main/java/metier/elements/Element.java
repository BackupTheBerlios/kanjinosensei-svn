package metier.elements;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import metier.Messages;

import utils.MyUtils;
import utils.OneStringList;

/**
 * Abstract class that represent an element of dictionary. Elements contains
 * data to be learned by user. There are different kinds of Elements, but all
 * elements must extends this abstract class which implements common mechanisms.
 * Significations and Themes are oftenly used in a "one string list format",
 * which is a string with all value listed separated by the FIELD_SEPARATOR
 * constant.
 * Every Element final subclass must define a constant such as :
 * <code>public static final MyElementSubClass BLANK = new MyElementSubClass(blank parameters..);</code>
 * 
 * @author Escallier Pierre
 */
public abstract class Element implements Serializable, Comparable<Element>
{

	/** Serialization version. */
	private static final long		serialVersionUID			= 1L;
	
	/** Default dictionary content directory if the specified one is not found */
	public static final String	DICO_DIR	= "dico"; //$NON-NLS-1$

	/** String used to separate fields in export format. */
	public static final String		EXPORT_SEPARATOR			= ";"; //$NON-NLS-1$

	/** Separators that are allowed in one string list format. */
	public static final String[]	FIELD_ALLOWED_SEPARATORS	= {",", ":", ";", "、", "；"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	/** Separator to be used to export one string lists. */
	public static final String	FIELD_SEPARATOR				= FIELD_ALLOWED_SEPARATORS[0];

	/** Significations of the element. */
	protected OneStringList	significations				= new OneStringList(FIELD_ALLOWED_SEPARATORS);

	/** Themes in which the element is classed. */
	protected OneStringList	themes						= new OneStringList(FIELD_ALLOWED_SEPARATORS);

	/**
	 * Static method used as Element factory. It generate an element from class
	 * name and import string.
	 * 
	 * @param className
	 *            Name of the class of this element.
	 * @param importLine
	 *            Import line of the element, this final element class must
	 *            implement a correct importLine() method for this parameter
	 *            value.
	 * @return New element instanciated from the given class and given import
	 *         line.
	 * @throws ClassNotFoundException
	 *             If class name is incorrect, or class unavailable.
	 * @throws SecurityException
	 *             If there is a problem loading the given class.
	 * @throws NoSuchMethodException
	 *             If there is a problem founding the constructor.
	 * @throws IllegalArgumentException
	 *             If there is a problem calling the class constructor.
	 * @throws InstantiationException
	 *             If there is a problem instantiating the given class.
	 * @throws IllegalAccessException
	 *             If there is a problem accessing the given class methods.
	 * @throws InvocationTargetException
	 *             If there is a problem with instantiation.
	 */
	@SuppressWarnings("unchecked") //$NON-NLS-1$
	public static Element generateImportedElement(String className, String importLine) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException
	{
		Class<? extends Element> classElement = (Class<? extends Element>) Class.forName(className);
		Constructor<?> constructor = classElement.getConstructor(String.class);
		Element element = (Element) constructor.newInstance(importLine);
		return element;
	}

	/**
	 * This method add element common fields to a string buffer, and then call
	 * _export() method which must be implemented in final classes.
	 * 
	 * @return String export line of this element.
	 */
	public final String exportString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\"" + this.getClass().getName() + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"" + getThemes() + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"" + getSignifications() + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(_exportString());
		return sb.toString();
	}

	/**
	 * Abstract export method which must be implemented by final classes.
	 * 
	 * @return String export line of final classes fields.
	 */
	protected abstract String _exportString();

	/**
	 * This method generate an unique Key for this element, beginning with its
	 * final class name. Final class must implement _getKey() method.
	 * 
	 * @return Unique key string for this element.
	 */
	public final String getKey()
	{
		return this.getClass().getName() + _getKey();
	}

	/**
	 * Abstract getKey method which must be implemented by final classes, and
	 * return an unique value for each elements.
	 * 
	 * @return Unique key string for the final class element.
	 */
	protected abstract String _getKey();

	/**
	 * Method used to sort elements of different final class in a collection.
	 * Works with getKey() string.
	 * 
	 * @param o
	 *            Element to compare this element with.
	 * @return difference between this element key and the given one.
	 */
	public int compareTo(Element o)
	{
		return getKey().compareTo(o.getKey());
	}

	/** Imported ordered fields names. Used in warnings messages. */
	final static String[] IMPORT_FIELDS = {Messages.getString("Element.Field.Themes"), Messages.getString("Element.Field.Significations")}; //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * Import element from import line. And return a new import line without the
	 * consumed fields.
	 * 
	 * @param importLine
	 *            Import line of the element.
	 * @return importLine substring, without consumed fields.
	 */
	protected String importString(String importLine)
	{
		String[] attributs = importLine.split(EXPORT_SEPARATOR);

		String significations = "", themes = ""; //$NON-NLS-1$ //$NON-NLS-2$

		try
		{
			themes = attributs[1];
			significations = attributs[2];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			String errMsg = Messages.getString("Element.Import.WarningMissingFields") + " : \"" + importLine + "\" : " + MyUtils.joinStringElements(MyUtils.offsetObjectElements(IMPORT_FIELDS, Integer.valueOf(e.getMessage())), ", "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			System.err.println(errMsg);
		}
		constructor(MyUtils.stripQuotes(significations), MyUtils.stripQuotes(themes));

		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 3), EXPORT_SEPARATOR);
	}

	/**
	 * Empty constructor.
	 */
	protected Element()
	{

	}

	/**
	 * Constructor with signification and themes fields.
	 * 
	 * @param significations
	 *            Element significations string (can use any
	 *            FIELD_ALLOWED_SEPARATORS to separate each significations in
	 *            the string).
	 * @param themes
	 *            Element themes (can use any FIELD_ALLOWED_SEPARATORS to
	 *            separate each themes in the string).
	 */
	public Element(String significations, String themes)
	{
		constructor(significations, themes);
	}

	/**
	 * Private method used as constructor.
	 * 
	 * @param significations
	 *            Element significations string (can use any
	 *            FIELD_ALLOWED_SEPARATORS to separate each significations in
	 *            the string).
	 * @param themes
	 *            Element themes (can use any FIELD_ALLOWED_SEPARATORS to
	 *            separate each themes in the string).
	 */
	private void constructor(String significations, String themes)
	{
		this.significations.clear();
		this.significations.addFromString(significations);
		
		this.themes.clear();
		this.themes.addFromString(themes);
	}

	/**
	 * Get all this elements themes which starts with the begenning filter.
	 * 
	 * @param beginning
	 *            Theme filter.
	 * @return Set of matching themes.
	 */
	public List<String> getThemesSet(String beginning)
	{
		return themes.getElementsBeginWith(beginning);		
	}

	/**
	 * Return all this element themes in one string format.
	 * <code>"theme1" + FIELD_SEPARATOR + "theme2" + FIELD_SEPARATOR + "theme3" ...</code>
	 * 
	 * @return String of all this element themes separated by FIELD_SEPARATOR.
	 */
	public String getThemes()
	{
		return themes.toString();
	}

	/**
	 * Test if this element is at least in one of the given themes.
	 * 
	 * @param testedThemes
	 *            List of themes to test.
	 * @return true if at least one theme match this element.
	 */
	public boolean testThemes(Set<String> testedThemes)
	{
		return themes.containsAtLeastOne(testedThemes);
	}

	/**
	 * Get list of this element significations.
	 * 
	 * @return this element significations.
	 */
	public List<String> getSignificationsSet()
	{
		return new OneStringList(significations);
	}

	/**
	 * Get this element significations in a one string list format, using FIELD_SEPARATOR constant.
	 * 
	 * @return
	 */
	public String getSignifications()
	{
		return significations.toString();
	}

	/**
	 * Get this element significations in a one string list format, using specified separator.
	 * @param separator String to use to separate significations in the one string list format result.
	 * @return a one string list with all this element significations.
	 */
	public String getSignifications(String separator)
	{
		return significations.toString(separator);
	}

	/**
	 * Do some cleaning task on the object, used to ensure compatibility with previous import/export method versions
	 * This method can be overridden in subclasses, but must always call the superclass.pack().
	 * This method should be always call before to export/save, or just after import/load an element.
	 */
	public void pack()
	{
		significations.removeEmptyElements();
		themes.removeEmptyElements();
		
		if (themes.contains(this.getClass().getSimpleName()))
		{
			themes.remove(this.getClass().getSimpleName());
		}
		
		if (!themes.contains(Messages.getString(this.getClass().getSimpleName())))
		{
			themes.add(Messages.getString(this.getClass().getSimpleName()));
		}
	}

	/**
	 * Test if this element match the beginning filter. This method must be implemented in subclasses and must return true if beginning is null or empty.
	 * @param beginning Element beginning filter.
	 * @return true if this element match the given filter (or the filter is null or empty), false if not.
	 */
	public abstract boolean match(String beginning);
}
