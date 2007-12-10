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
public class Mot extends Element implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	public static final Mot BLANK = new Mot(" ", "", "", "", "");
	
	/**
	 * @param significations
	 * @param themes
	 */
	public Mot(String mot, String lecture, String son, String significations, String themes)
	{
		super(significations, themes);
		constructor(mot, lecture, son);
	}
	
	private void constructor(String mot, String lecture, String son)
	{
		this.mot = mot;
		this.lecture = lecture;
		this.son = son;
	}

	/**
	 * @param line
	 */
	public Mot(String importLine)
	{
		super();
		importer(importLine);
	}
	
	public String importer(String importLine)
	{
		importLine = super.importer(importLine);
		
		String[] attributs = importLine.split(EXPORT_SEPARATOR);
		
		String mot = "", lecture = "", son = "";
		
		try
		{
			mot = attributs[0];
			lecture = attributs[1];
			son = attributs[2];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Warning : Import mot ligne \""+importLine+"\" : "+e.getLocalizedMessage());
		}
		
		constructor(MyUtils.stripQuotes(mot), MyUtils.stripQuotes(lecture), MyUtils.stripQuotes(son));
		
		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 3), EXPORT_SEPARATOR);
	}

	public String _export()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\""+mot+"\""+EXPORT_SEPARATOR);
		sb.append("\""+lecture+"\""+EXPORT_SEPARATOR);
		sb.append("\""+son+"\""+EXPORT_SEPARATOR);
		return sb.toString();
	}
	
	/** Caractères composants le mot, dans l'ordre. */
	private String mot = null;
	
	/** Lecture. */
	private String lecture = null;
	
	/** Lien vers le son lecture du mot. */
	private String son = null;

	public String getMot()
	{
		return mot;
	}
	
	public String toString()
	{
		return getMot();
	}
	
	public String getSon()
	{
		return son;
	}
	
	public void pack()
	{
		// On ajoute "mot" aux thèmes
		if (!themes.contains("mot"))
		{
			themes.add("mot");
		}
	}

	/**
	 * @param debut
	 * @return
	 */
	public boolean correspondA(String debut)
	{
		if ((debut == null) || (debut.isEmpty())) return true;

		if (debut.matches(getMot())) return true;
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
		return getMot();
	}
}
