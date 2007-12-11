/**
 * 
 */
package metier.elements;

import java.io.Serializable;

import utils.MyUtils;

/**
 * @author Axan
 *
 */
public class Phrase extends Element implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	public static final Phrase BLANK = new Phrase(" ", "", "", "", "");
	
	/**
	 * @param significations
	 * @param themes
	 */
	public Phrase(String phrase, String lecture, String son, String significations, String themes)
	{
		super(significations, themes);
		constructor(phrase, lecture, son);
	}
	
	private void constructor(String phrase, String lecture, String son)
	{
		this.phrase = phrase;
		this.lecture = lecture;
		this.son = son;
	}

	/**
	 * @param line
	 */
	public Phrase(String importLine)
	{
		super();
		importer(importLine);
	}
	
	public String importer(String importLine)
	{
		importLine = super.importer(importLine);
		
		String[] attributs = importLine.split(EXPORT_SEPARATOR);
		
		String phrase = "", lecture = "", son = "";
		
		try
		{
			phrase = attributs[0];
			lecture = attributs[1];
			son = attributs[2];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Warning : Import phrase ligne \""+importLine+"\" : "+e.getLocalizedMessage());
		}
		
		constructor(MyUtils.stripQuotes(phrase), MyUtils.stripQuotes(lecture), MyUtils.stripQuotes(son));
		
		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 3), EXPORT_SEPARATOR);
	}

	public String _export()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\""+phrase+"\""+EXPORT_SEPARATOR);
		sb.append("\""+lecture+"\""+EXPORT_SEPARATOR);
		sb.append("\""+son+"\""+EXPORT_SEPARATOR);
		return sb.toString();
	}
	
	/** Mots composants la phrase, dans l'ordre. */
	private String phrase = null;
	
	/** Lecture. */
	private String lecture = null;
	
	/** Lien vers le son lecture de la phrase. */
	private String son = null;

	public String getPhrase()
	{
		return phrase;
	}
	
	public String toString()
	{
		return getPhrase();
	}
	
	public String getSon()
	{
		return son;
	}
	
	public void pack()
	{
		// On ajoute "phrase" aux thèmes
		if (!themes.contains("phrase"))
		{
			themes.add("phrase");
		}
	}

	/**
	 * @param debut
	 * @return
	 */
	public boolean correspondA(String debut)
	{
		if ((debut == null) || (debut.isEmpty())) return true;

		if (debut.matches(getPhrase())) return true;
		if (significations.contains(debut)) return true;

		return false;
	}

	/**
	 * @return
	 */
	public String getLecture()
	{
		return lecture;
	}

	/* (non-Javadoc)
	 * @see metier.elements.Element#_getKey()
	 */
	@Override
	protected String _getKey()
	{
		return getPhrase();
	}
}
