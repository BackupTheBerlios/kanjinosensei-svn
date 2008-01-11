package metier.elements;

import java.io.Serializable;
import java.util.Set;

import metier.Messages;

import utils.MyUtils;
import utils.OneStringList;

/**
 * Represent a Kanji element, defined by its unicode character, an image showing
 * how to draw it, its ON and KUN lectures. As all elements, it is defined by
 * its significations and classed in themes.
 * 
 * @author Escallier Pierre
 */
public class Kanji extends Element implements Serializable
{

	/** Version des méthodes de sérialisation. */
	private static final long	serialVersionUID	= 1L;

	/** Blank kanji constant. */
	public static final Kanji	BLANK				= new Kanji(' ', "", "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** UTF8 code of this kanji. */
	private Character			codeUTF8			= null;

	/** Path to the picture showing the stroke order. */
	private String				strokeOrderPicture	= null;

	/** List of ON lectures. */
	private OneStringList		lecturesON			= new OneStringList(Element.FIELD_ALLOWED_SEPARATORS);

	/** Liste of KUN lectures. */
	private OneStringList		lecturesKUN			= new OneStringList(Element.FIELD_ALLOWED_SEPARATORS);

	/**
	 * Constructor from all fields.
	 * 
	 * @param codeUTF8
	 *            UTF8 code of the kanji.
	 * @param strokeOrderPicture
	 *            Path to the picture showing the stroke order.
	 * @param lecturesON
	 *            One string list of ON lectures.
	 * @param lecturesKUN
	 *            One string list of KUN lectures.
	 * @param significations
	 *            One string list of significations.
	 * @param themes
	 *            One string list of themes.
	 */
	public Kanji(char codeUTF8, String strokeOrderPicture, String lecturesON, String lecturesKUN,
			String significations, String themes)
	{
		super(significations, themes);
		constructor(codeUTF8, strokeOrderPicture, lecturesON, lecturesKUN);
	}

	/**
	 * Private method used by differents constructors to set this kanji from all
	 * fields.
	 * 
	 * @param codeUTF8
	 *            UTF8 code of the kanji.
	 * @param strokeOrderPicture
	 *            Path to the picture showing the stroke order.
	 * @param lecturesON
	 *            One string list of ON lectures.
	 * @param lecturesKUN
	 *            One string list of KUN lectures.
	 */
	private void constructor(char codeUTF8, String strokeOrderPicture, String lecturesON, String lecturesKUN)
	{
		this.codeUTF8 = codeUTF8;
		this.strokeOrderPicture = strokeOrderPicture;

		this.lecturesKUN.clear();
		this.lecturesKUN.addFromString(lecturesKUN);

		this.lecturesON.clear();
		this.lecturesON.addFromString(lecturesON);
	}

	/**
	 * Constructor from import line.
	 * 
	 * @param importLine
	 *            String to import the kanji.
	 */
	public Kanji(String importLine)
	{
		super();
		importString(importLine);
	}

	/** Imported ordered fields names. Used in warnings messages. */
	final static String[] IMPORT_FIELDS = {Messages.getString("Kanji.Field.UTF8code"), Messages.getString("Kanji.Field.strokeOrder"), Messages.getString("Kanji.Field.KUNlectures"), Messages.getString("Kanji.ONlectures")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	
	/**
	 * Define this kanji values from an import string.
	 * 
	 * @param importLine
	 *            String to import the kanji.
	 * @return new importLine without consumed fields.
	 * @see Element#importString(String)
	 */
	@Override
	protected String importString(String importLine)
	{
		importLine = super.importString(importLine);

		String[] attributs = importLine.split(EXPORT_SEPARATOR);

		String codeUTF8 = " ", imageOrdreTraits = "", lecturesJaponaises = "", lectureChinoises = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		try
		{
			codeUTF8 = attributs[0];
			imageOrdreTraits = attributs[1];
			lecturesJaponaises = attributs[2];
			lectureChinoises = attributs[3];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			String errMsg = Messages.getString("Kanji.Import.WarningMissingFields") + " : \"" + importLine + "\" : " + MyUtils.joinStringElements(MyUtils.offsetObjectElements(IMPORT_FIELDS, Integer.valueOf(e.getMessage())), ", "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			System.err.println(errMsg);
		}

		constructor(MyUtils.stripQuotes(codeUTF8).charAt(0), MyUtils.stripQuotes(imageOrdreTraits), MyUtils
				.stripQuotes(lectureChinoises), MyUtils.stripQuotes(lecturesJaponaises));

		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 4), EXPORT_SEPARATOR);
	}

	/**
	 * Make kanji export string.
	 * 
	 * @return export string of this kanji.
	 * @see Element#_exportString()
	 */
	@Override
	protected String _exportString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\"" + codeUTF8 + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"" + strokeOrderPicture + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"" + getLecturesKUN() + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"" + getLecturesON() + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	/**
	 * Return this kanji character as String.
	 * 
	 * @return This kanji character as String.
	 */
	public String toString()
	{
		return getCodeUTF8().toString();
	}

	/**
	 * Return this kanji character.
	 * 
	 * @return This kanji character.
	 */
	public Character getCodeUTF8()
	{
		return codeUTF8;
	}

	/**
	 * Get stroke order picture path.
	 * 
	 * @return Stroke order picture path.
	 */
	public String getStrokeOrderPicture()
	{
		return strokeOrderPicture;
	}

	/**
	 * Get ON lectures set.
	 * 
	 * @return ON lectures set.
	 */
	public Set<String> getLecturesONSet()
	{
		return new OneStringList(lecturesON);
	}

	/**
	 * Get ON lectures in one string list format.
	 * 
	 * @return ON lectures in one string list format.
	 */
	public String getLecturesON()
	{
		return lecturesON.toString();
	}

	/**
	 * Get KUN lectures set.
	 * 
	 * @return KUN lectures set.
	 */
	public Set<String> getLecturesKUNSet()
	{
		return new OneStringList(lecturesKUN);
	}

	/**
	 * Get KUN lectures in one string list format.
	 * 
	 * @return KUN lectures in one string list format.
	 */
	public String getLecturesKUN()
	{
		return lecturesKUN.toString();
	}

	/**
	 * Test if the kanji match the element filter. The kanji match if filter is
	 * null or empty, if it match the unicode, or any lectures or signification.
	 * 
	 * @param beginning
	 *            Element beginning filter.
	 * @return true if this element match the given filter (or the filter is
	 *         null or empty), false if not.
	 * @see Element#match(String)
	 */
	@Override
	public boolean match(String beginning)
	{
		if ((beginning == null) || (beginning.isEmpty())) return true;

		if (beginning.matches(getCodeUTF8().toString())) return true;

		if (lecturesON.contains(beginning, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd)) return true;
		if (lecturesKUN.contains(beginning, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd)) return true;
		if (significations.contains(beginning, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd)) return true;

		return false;
	}

	/**
	 * Do some cleaning tasks on values. Remove empty elements from lectures
	 * sets, fix strokeOrderPicture path with current file separator, check if
	 * the image file exists, ensure kanji is in "kanji" theme.
	 */
	@Override
	public void pack()
	{
		super.pack();

		lecturesKUN.removeEmptyElements();
		lecturesON.removeEmptyElements();

		strokeOrderPicture = MyUtils.checkFileExists(strokeOrderPicture, DICO_DIR);
	}

	/**
	 * The Kanji key is its unicode character.
	 * 
	 * @return current unicode kanji character.
	 */
	@Override
	protected String _getKey()
	{
		return getCodeUTF8().toString();
	}
}
