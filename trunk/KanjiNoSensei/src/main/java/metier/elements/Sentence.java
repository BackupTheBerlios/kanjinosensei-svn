package metier.elements;

import java.io.File;
import java.io.Serializable;

import metier.Messages;

import utils.MyUtils;

/**
 * Represent a Sentence element, defined by its unicode string, its lecture and a
 * sound. As all elements, it is defined by its significations and classed in
 * themes.
 * 
 * @author Escallier Pierre
 */
public class Sentence extends Element implements Serializable
{
	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;
	
	/** Blank sentence constant. */
	public static final Sentence BLANK = new Sentence(" ", "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	
	/**
	 * Constructor from all fields.
	 * 
	 * @param sentence
	 *            Unicode string of this sentence.
	 * @param lecture
	 *            Lecture of this sentence.
	 * @param sound
	 *            Path to a sound file representing this sentence.
	 * @param significations
	 * @param themes
	 */
	public Sentence(String sentence, String lecture, String sound, String significations, String themes)
	{
		super(significations, themes);
		constructor(sentence, lecture, sound);
	}
	
	/**
	 * Private method used by constructors.
	 * @param sentence Unicode string of this sentence.
	 * @param lecture Lecture of this sentence.
	 * @param sound Path to a sound file representing this sentence.
	 */
	private void constructor(String sentence, String lecture, String sound)
	{
		this.sentence = sentence;
		this.lecture = lecture;
		this.sound = sound;
	}

	/**
	 * Constructor from import line.
	 * 
	 * @param importLine
	 *            String to import the sentence.
	 */
	public Sentence(String importLine)
	{
		super();
		importString(importLine);
	}
	
	/** Imported ordered fields names. Used in warnings messages. */
	final static String[] IMPORT_FIELDS = {Messages.getString("Sentence.Field.Sentence"), Messages.getString("Sentence.Field.Lecture"), Messages.getString("Sentence.Field.Sound")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	/**
	 * Define this sentence values from an import string.
	 * 
	 * @param importLine
	 *            String to import the sentence.
	 * @return new importLine without consumed fields.
	 * @see Element#importString(String)
	 */
	@Override
	public String importString(String importLine)
	{
		importLine = super.importString(importLine);
		
		String[] attributs = importLine.split(EXPORT_SEPARATOR);
		
		String phrase = "", lecture = "", son = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		try
		{
			phrase = attributs[0];
			lecture = attributs[1];
			son = attributs[2];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			String errMsg = Messages.getString("Sentence.Import.WarningMissingFields") + " : \"" + importLine + "\" : " + MyUtils.joinStringElements(MyUtils.offsetObjectElements(IMPORT_FIELDS, Integer.valueOf(e.getMessage())), ", "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			System.err.println(errMsg);
		}
		
		constructor(MyUtils.stripQuotes(phrase), MyUtils.stripQuotes(lecture), MyUtils.stripQuotes(son));
		
		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 3), EXPORT_SEPARATOR);
	}

	/**
	 * Make this sentence export string.
	 * 
	 * @return export string of this sentence.
	 * @see Element#_exportString()
	 */
	@Override
	public String _exportString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\""+sentence+"\""+EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\""+lecture+"\""+EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\""+sound+"\""+EXPORT_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}
	
	/** Unicode string for this sentence. */
	private String sentence = null;
	
	/** Lecture. */
	private String lecture = null;
	
	/** Path to a sound file of this sentence. */
	private String sound = null;

	/**
	 * Return this sentence unicode string.
	 * @return this sentence unicode string.
	 */
	public String getSentence()
	{
		return sentence;
	}
	
	/**
	 * Return getSentence()
	 * @return getSentence()
	 * @see getSentence()
	 */
	public String toString()
	{
		return getSentence();
	}
	
	/**
	 * Return the path to this sentence sound file.
	 * @return the path to this sentence sound file.
	 */
	public String getSound()
	{
		return sound;
	}
	
	/**
	 * Do some cleaning tasks on values. fix sound file path with current file
	 * separator, check if the file exists, ensure sentence is in "sentence" theme.
	 */
	@Override
	public void pack()
	{
		sound = MyUtils.checkFileExists(sound, System.getProperty("KanjiNoSenseiWorkingDirectory")+File.separatorChar+DICO_DIR); //$NON-NLS-1$
		
		lecture = lecture.replace(' ', '　');
	}

	/**
	 * Test if the sentence match the element filter. The word match if filter is
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

		if (beginning.matches(getSentence())) return true;
		if (MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd.compare(getSentence(), beginning) == 0) return true;
		if (significations.contains(beginning, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd)) return true;

		return false;
	}

	/**
	 * Return this sentence lecture.
	 * @return this sentence lecture.
	 */
	public String getLecture()
	{
		return lecture;
	}

	/**
	 * The Sentence key is its unicode string.
	 * @return current unicode string.
	 */
	@Override
	protected String _getKey()
	{
		return getSentence();
	}
}
