/**
 * 
 */
package metier.elements;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import utils.MyUtils;

/**
 * @author Axan
 * 
 */
public abstract class Element implements Serializable, Comparable<Element>
{

	/**
	 * Version des méthodes de sérialisation.
	 */
	private static final long		serialVersionUID			= 1L;

	public static final String	EXPORT_SEPARATOR			= ";";

	protected static final String[]	FIELD_ALLOWED_SEPARATORS	= {", ", ": ", "; "};
	protected static final String	FIELD_SEPARATOR				= ", ";

	/** Signification(s) de l'élément. */
	protected Set<String>			significations				= new TreeSet<String>();

	/** Thèmes dans lesquels est classé l'élément. */
	protected Set<String>			themes						= new TreeSet<String>();

	@SuppressWarnings("unchecked")
	public static Element genererElementImport(String nomClasse, String importLine) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Class<? extends Element> classElement = (Class<? extends Element>) Class.forName(nomClasse);
		Constructor<?> constructor = classElement.getConstructor(String.class);
		Element element = (Element) constructor.newInstance(importLine);
		return element;
	}

	public final String export()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\"" + this.getClass().getName() + "\"" + EXPORT_SEPARATOR);
		sb.append("\"" + getThemes() + "\"" + EXPORT_SEPARATOR);
		sb.append("\"" + getSignifications() + "\"" + EXPORT_SEPARATOR);
		sb.append(_export());
		return sb.toString();
	}
	protected abstract String _export();

	public final String getKey()
	{
		return this.getClass().getName() + _getKey();
	}
	
	protected abstract String _getKey();
	
	public int compareTo(Element o)
	{
		return getKey().compareTo(o.getKey());
	}
	
	/**
	 * @param importLine
	 * @return
	 */
	public String importer(String importLine)
	{
		String[] attributs = importLine.split(EXPORT_SEPARATOR);

		String significations = "", themes = "";

		try
		{
			themes = attributs[1];
			significations = attributs[2];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Warning : Import element ligne \"" + importLine + "\" : " + e.getLocalizedMessage());
		}
		constructor(MyUtils.stripQuotes(significations), MyUtils.stripQuotes(themes));

		return MyUtils.joinStringElements(MyUtils.offsetObjectElements(attributs, 3), EXPORT_SEPARATOR);
	}

	protected Element()
	{

	}

	/**
	 * @param significations2
	 * @param themes2
	 */
	public Element(String significations, String themes)
	{
		constructor(significations, themes);
	}

	private void constructor(String significations, String themes)
	{
		this.significations.clear();
		this.themes.clear();

		for (String sujet : FIELD_ALLOWED_SEPARATORS)
		{
			significations = significations.replace(sujet, FIELD_SEPARATOR);
		}

		String[] l = significations.split(FIELD_SEPARATOR);
		for (String s : l)
		{
			if ( !s.isEmpty())
			{
				this.significations.add(s);
			}
		}

		for (String sujet : FIELD_ALLOWED_SEPARATORS)
		{
			themes = themes.replace(sujet, FIELD_SEPARATOR);
		}
		l = themes.split(FIELD_SEPARATOR);
		for (String s : l)
		{
			if ( !s.isEmpty())
			{
				this.themes.add(s);
			}
		}
	}

	public Set<String> getThemesSet(String debut)
	{
		if (debut == null) debut = "";

		Set<String> themesReponses = new TreeSet<String>();

		if (debut.isEmpty())
		{
			themesReponses.addAll(themes);
			return themesReponses;
		}

		Iterator<String> it = themes.iterator();
		while (it.hasNext())
		{
			String theme = it.next();
			if (theme.matches(".*" + debut + ".*"))
			{
				themesReponses.add(theme);
			}
		}

		return themesReponses;
	}

	public String getThemes()
	{
		return MyUtils.joinStringElements(themes.toArray(new String[themes.size()]), FIELD_SEPARATOR);

		/*
		 * StringBuffer sb = new StringBuffer(); Iterator<String> it =
		 * themes.iterator(); while(it.hasNext()) { sb.append(it.next()); if
		 * (it.hasNext()) { sb.append(FIELD_SEPARATOR); } }
		 * 
		 * return sb.toString();
		 */
	}

	public boolean testerThemes(Set<String> i_themes)
	{
		Iterator<String> it = themes.iterator();
		while (it.hasNext())
		{
			if (i_themes.contains(it.next()))
			{
				return true;
			}
		}

		return false;
	}

	public Set<String> getSignificationsSet()
	{
		return new TreeSet<String>(significations);
	}

	public String getSignifications()
	{
		return getSignifications(FIELD_SEPARATOR);
	}

	public String getSignifications(String separator)
	{
		return MyUtils.joinStringElements(significations.toArray(new String[significations.size()]), separator);

		/*
		 * StringBuffer sb = new StringBuffer(); Iterator<String> it =
		 * significations.iterator(); while(it.hasNext()) {
		 * sb.append(it.next()); if (it.hasNext()) { sb.append(separator); } }
		 * 
		 * return sb.toString();
		 */
	}

	public void pack()
	{
		MyUtils.removeEmptyElements(significations);
		MyUtils.removeEmptyElements(themes);
	}

	protected static final String	IMG_DIR	= "dico";

	/**
	 * @param debut
	 * @return
	 */
	public abstract boolean correspondA(String debut);

}
