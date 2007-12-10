package metier.elements;

import java.io.File;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import utils.MyUtils;

/**
 * @author Escallier Pierre
 * 
 */
public class Kanji extends Element implements Serializable
{

	/**
	 * Version des méthodes de sérialisation.
	 */
	private static final long	serialVersionUID	= 1L;

	public static final Kanji	BLANK	= new Kanji(' ', "", "", "", "", "");
	
	/** Code UTF8 du caractère */
	private Character			codeUTF8			= null;

	/** Lien vers l'image donnant l'ordre des traits */
	private String				imageOrdreTraits	= null;

	/** Liste des lectures chinoises */
	private Set<String>			lecturesChinoises	= new TreeSet<String>();

	/** Liste des lectures japonaises */
	private Set<String>			lecturesJaponaises	= new TreeSet<String>();
	
	/**
	 * @param c
	 * @param text
	 * @param text2
	 * @param text3
	 * @param text4
	 * @param text5
	 */
	public Kanji(char codeUTF8, String imageTrace, String lecturesChinoises, String lecturesJaponaises,
			String significations, String themes)
	{
		super(significations, themes);
		constructor(codeUTF8, imageTrace, lecturesChinoises, lecturesJaponaises);
	}
	
	private void constructor(char codeUTF8, String imageTrace, String lecturesChinoises, String lecturesJaponaises)
	{
		this.codeUTF8 = codeUTF8;
		this.imageOrdreTraits = imageTrace;

		for(String sujet: FIELD_ALLOWED_SEPARATORS)
		{
			lecturesChinoises = lecturesChinoises.replace(sujet, FIELD_SEPARATOR);
		}
		String[] l = lecturesChinoises.split(FIELD_SEPARATOR);
		for (String s : l)
		{
			if ( !s.isEmpty())
			{
				this.lecturesChinoises.add(s);
			}
		}

		for(String sujet: FIELD_ALLOWED_SEPARATORS)
		{
			lecturesJaponaises = lecturesJaponaises.replace(sujet, FIELD_SEPARATOR);
		}
		l = lecturesJaponaises.split(FIELD_SEPARATOR);
		for (String s : l)
		{
			if ( !s.isEmpty())
			{
				this.lecturesJaponaises.add(s);
			}
		}

	}

	/**
	 * @param line
	 */
	public Kanji(String importLine)
	{
		super();
		importer(importLine);
	}
	
	public String importer(String importLine)
	{
		importLine = super.importer(importLine);
		
		String[] attributs = importLine.split(EXPORT_SEPARATOR);
		
		String codeUTF8 = " ", imageOrdreTraits = "", lecturesJaponaises = "", lectureChinoises = "";
		
		try
		{
			codeUTF8 = attributs[0];
			imageOrdreTraits = attributs[1];
			lecturesJaponaises = attributs[2];
			lectureChinoises = attributs[3];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Warning : Import kanji ligne \""+importLine+"\" : "+e.getLocalizedMessage());
		}
		
		constructor(MyUtils.stripQuotes(codeUTF8).charAt(0), MyUtils.stripQuotes(imageOrdreTraits), MyUtils.stripQuotes(lectureChinoises), MyUtils.stripQuotes(lecturesJaponaises));
		
		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 4), EXPORT_SEPARATOR);
	}

	public String _export()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\""+codeUTF8+"\""+EXPORT_SEPARATOR);
		sb.append("\""+imageOrdreTraits+"\""+EXPORT_SEPARATOR);
		sb.append("\""+getLecturesJaponaises()+"\""+EXPORT_SEPARATOR);
		sb.append("\""+getLecturesChinoises()+"\""+EXPORT_SEPARATOR);
		return sb.toString();
	}
	
	public String toString()
	{
		return getCodeUTF8().toString();
	}
	
	public Character getCodeUTF8()
	{
		return codeUTF8;
	}

	public String getOrdreTraits()
	{
		return imageOrdreTraits;
	}

	public Set<String> getLecturesChinoisesSet()
	{
		return new TreeSet<String>(lecturesChinoises);
	}

	public String getLecturesChinoises()
	{
		return MyUtils.joinStringElements(lecturesChinoises.toArray(new String[lecturesChinoises.size()]), FIELD_SEPARATOR); 
		
		/*
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = lecturesChinoises.iterator();
		while (it.hasNext())
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(FIELD_SEPARATOR);
			}
		}

		return sb.toString();
		*/
	}

	public Set<String> getLecturesJaponaisesSet()
	{
		return new TreeSet<String>(lecturesJaponaises);
	}

	public String getLecturesJaponaises()
	{
		return MyUtils.joinStringElements(lecturesJaponaises.toArray(new String[lecturesJaponaises.size()]), FIELD_SEPARATOR);
		
		/*
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = lecturesJaponaises.iterator();
		while (it.hasNext())
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(FIELD_SEPARATOR);
			}
		}

		return sb.toString();
		*/
	}

	/**
	 * @param debut
	 * @return
	 */
	public boolean correspondA(String debut)
	{
		if ((debut == null) || (debut.isEmpty())) return true;

		if (debut.matches(getCodeUTF8().toString())) return true;

		if (lecturesChinoises.contains(debut)) return true;
		if (lecturesJaponaises.contains(debut)) return true;
		if (significations.contains(debut)) return true;

		return false;
	}

	public void pack()
	{
		super.pack();
		
		MyUtils.removeEmptyElements(lecturesChinoises);
		MyUtils.removeEmptyElements(lecturesJaponaises);
		
		// On ne garde que le nom du fichier trace
		File f = new File(imageOrdreTraits.replace('/', File.separatorChar).replace('\\', File.separatorChar));
		if (!f.exists())
		{
			
			File f2 = new File(IMG_DIR+File.separatorChar+f.getName());
			if (!f2.exists())
			{
				System.err.println("Fichier \""+f.getAbsolutePath()+"\" introuvable.");
			}
			else
			{
				imageOrdreTraits = f2.toString();
			}
		}
		
		// On ajoute "kanji" au thèmes
		if (!themes.contains("kanji"))
		{
			themes.add("kanji");
		}
	}

	/* (non-Javadoc)
	 * @see metier.elements.Element#_getKey()
	 */
	@Override
	protected String _getKey()
	{
		return getCodeUTF8().toString();
	}
}
