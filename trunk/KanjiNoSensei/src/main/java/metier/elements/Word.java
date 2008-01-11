package metier.elements;

import java.io.Serializable;

import metier.Messages;

import utils.MyUtils;

/**
 * Represent a Word element, defined by its unicode string, its lecture and a
 * sound. As all elements, it is defined by its significations and classed in
 * themes.
 * 
 * @author Escallier Pierre
 */
public class Word extends Element implements Serializable
{
	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;

	/** Blank word constant. */
	public static final Word	BLANK				= new Word(" ", "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/**
	 * Constructor from all fields.
	 * 
	 * @param word
	 *            Unicode string of this word.
	 * @param lecture
	 *            Lecture of this word.
	 * @param sound
	 *            Path to a sound file representing this word.
	 * @param significations
	 *            One string list of significations.
	 * @param themes
	 *            One string list of themes.
	 */
	public Word(String word, String lecture, String sound, String significations, String themes)
	{
		super(significations, themes);
		constructor(word, lecture, sound);
	}

	/**
	 * Private method used by constructors.
	 * 
	 * @param word
	 *            Unicode string of this word.
	 * @param lecture
	 *            Lecture of this word.
	 * @param sound
	 *            Path to a sound file representing this word.
	 */
	private void constructor(String word, String lecture, String sound)
	{
		this.word = word;
		this.lecture = lecture;
		this.sound = sound;
	}

	/**
	 * Constructor from import line.
	 * 
	 * @param importLine
	 *            String to import the word.
	 */
	public Word(String importLine)
	{
		super();
		importString(importLine);
	}

	/** Imported ordered fields names. Used in warnings messages. */
	final static String[] IMPORT_FIELDS = {Messages.getString("Word.Field.Word"), Messages.getString("Word.Field.Lecture"), Messages.getString("Word.Field.Sound")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	/**
	 * Define this word values from an import string.
	 * 
	 * @param importLine
	 *            String to import the word.
	 * @return new importLine without consumed fields.
	 * @see Element#importString(String)
	 */
	@Override
	public String importString(String importLine)
	{
		importLine = super.importString(importLine);

		String[] attributs = importLine.split(EXPORT_SEPARATOR);

		String mot = "", lecture = "", son = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		try
		{
			mot = attributs[0];
			lecture = attributs[1];
			son = attributs[2];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			String errMsg = Messages.getString("Word.Import.WarningMissingFields") + " : \"" + importLine + "\" : " + MyUtils.joinStringElements(MyUtils.offsetObjectElements(IMPORT_FIELDS, Integer.valueOf(e.getMessage())), ", "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			System.err.println(errMsg);
		}

		constructor(MyUtils.stripQuotes(mot), MyUtils.stripQuotes(lecture), MyUtils.stripQuotes(son));

		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 3), EXPORT_SEPARATOR);
	}

	/**
	 * Make this word export string.
	 * 
	 * @return export string of this word.
	 * @see Element#_exportString()
	 */
	@Override
	public String _exportString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\"" + word + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"" + lecture + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"" + sound + "\"" + EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	/** Unicode string of this word. */
	private String	word	= null;

	/** Lecture. */
	private String	lecture	= null;

	/** Path to a sound file of this word. */
	private String	sound	= null;

	/**
	 * Return this word unicode string.
	 * 
	 * @return this word unicode string.
	 */
	public String getWord()
	{
		return word;
	}

	/**
	 * Return this word unicode string, like getMot() does.
	 * 
	 * @return getMot()
	 * @see getMot()
	 */
	public String toString()
	{
		return getWord();
	}

	/**
	 * Return the sound file of this word.
	 * 
	 * @return the sound file of this word.
	 */
	public String getSoundFile()
	{
		return sound;
	}

	/**
	 * Do some cleaning tasks on values. fix sound file path with current file
	 * separator, check if the file exists, ensure word is in "word" theme.
	 */
	@Override
	public void pack()
	{
		sound = MyUtils.checkFileExists(sound, DICO_DIR);
	}

	/**
	 * Test if the word match the element filter. The word match if filter is
	 * null or empty, if it match the unicode string, or any signification.
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

		if (beginning.matches(getWord())) return true;
		if (MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd.compare(beginning, getWord()) == 0) return true;
		if (significations.contains(beginning, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd)) return true;

		return false;
	}

	/**
	 * Return this word lecture.
	 * 
	 * @return this word lecture.
	 */
	public String getLecture()
	{
		return lecture;
	}

	/**
	 * The Word key is its unicode string.
	 * @return current unicode string.
	 */
	@Override
	protected String _getKey()
	{
		return getWord();
	}
}
