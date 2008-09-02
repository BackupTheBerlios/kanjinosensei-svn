/**
 * @author Escallier Pierre
 * @file OneStringList.java
 * @date 27 déc. 07
 */
package utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class represent a list of elements which support "one string format" input/output.
 * This class extends TreeSet&lt;E&gt; so each element is unique on the list.
 */
public class OneStringList extends Vector<String>
{
	
	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;
	
	/** Allowed input separators. */
	private final String[] ALLOWED_SEPARATOR;
	
	/** Separator used in one string list format. */
	private final String SEPARATOR;
	
	/**
	 * Empty constructor, use default separator ", ".
	 */
	public OneStringList()
	{
		ALLOWED_SEPARATOR = new String[1];
		ALLOWED_SEPARATOR[0] = ", "; //$NON-NLS-1$
		SEPARATOR = ALLOWED_SEPARATOR[0];
	}
	
	/**
	 * Copy constructor.
	 * @param oneStringList Copy source.
	 */
	public OneStringList(OneStringList oneStringList)
	{
		super(oneStringList);
		ALLOWED_SEPARATOR = oneStringList.ALLOWED_SEPARATOR;
		SEPARATOR = oneStringList.SEPARATOR;
		
	}
	
	/**
	 * Constructor with specified the default separator which is the only one allowed.
	 * @param separator Default separator (and the only one allowed).
	 */
	public OneStringList(String separator)
	{
		ALLOWED_SEPARATOR = new String[1];
		ALLOWED_SEPARATOR[0] = separator;
		SEPARATOR = ALLOWED_SEPARATOR[0];
	}
	
	/**
	 * Constructor with specified allowed separators, the first is the default one. 
	 * @param allowedSeparators Allowed separators, the first is the default one.
	 */
	public OneStringList(String[] allowedSeparators)
	{
		ALLOWED_SEPARATOR = allowedSeparators;
		SEPARATOR = ALLOWED_SEPARATOR[0];
	}

	/**
	 * Return this list in one string format, with default separator.
	 * <code>"element1" + SEPARATOR + "element2" + SEPARATOR + "element3" ...</code>
	 * @return this list in one string format.
	 */
	@Override
	public String toString()
	{
		return toString(SEPARATOR);
	}
	
	/**
	 * Return this list in one string format, with specified separator.
	 * @param separator Specified separator to output the list in one string format.
	 * @return this list in one string format with the specified separator.
	 */
	public String toString(String separator)
	{
		return MyUtils.joinStringElements(toArray(new String[size()]), separator);
	}
	
	/**
	 * Add elements to this list, elements given in one string format.
	 * @param oneStringFormat One string list format string of elements to add.
	 */
	public void addFromString(String oneStringFormat)
	{
		if (oneStringFormat.isEmpty()) return;
		
		if ((oneStringFormat.length() > 5) && (oneStringFormat.indexOf(">") < 0)) //$NON-NLS-1$
		{
			oneStringFormat = oneStringFormat+""; //$NON-NLS-1$
		}
		
		for (String sujet : ALLOWED_SEPARATOR)
		{
			oneStringFormat = oneStringFormat.replace(sujet, SEPARATOR);
		}

		String[] l = oneStringFormat.split(SEPARATOR);
		for (String s : l)
		{
			if ( !s.isEmpty())
			{
				s = s.replace('　', ' ').trim();
				add(s);
			}
		}
	}
	
	/**
	 * Get all elements which starts with the begenning filter.
	 * 
	 * @param beginning filter.
	 * @return Set of matching elements.
	 */
	public OneStringList getElementsBeginWith(String beginning)
	{
		if (beginning == null) beginning = ""; //$NON-NLS-1$

		OneStringList matchingElements = new OneStringList(ALLOWED_SEPARATOR);

		if (beginning.isEmpty())
		{
			matchingElements.addAll(this);
			return matchingElements;
		}

		Iterator<String> it = iterator();
		while (it.hasNext())
		{
			String element = it.next();
			if (element.matches(".*" + beginning + ".*")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				matchingElements.add(element);
			}
		}

		return matchingElements;
	}
	
	/**
	 * Contains method with a comparator parameter, which can be used to redefined the contains conditions.
	 * @param o Object to test if contained.
	 * @param comparator Comparator to use for contained conditions.
	 * @return true if object o is contained in this list, according to the comparator compare() implementation, false otherwise.
	 */
	public boolean contains(Object o, Comparator<String> comparator)
	{
		Iterator<String> it = iterator();
		while(it.hasNext())
		{
			String s = it.next();
			if (comparator.compare(o.toString(), s) == 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * ContainsAll method with a comparator parameter, which can be used to redefined the contains conditions.
	 * @param c Collection to test.
	 * @param comparator Comparator to be used to test each element.
	 * @return true is all c elements are contained in this list according to the comparator compare method implementation.
	 * @see OneStringList#contains(Object, Comparator)
	 */
	public boolean containsAll(Collection<?> c, Comparator<String> comparator)
	{
		Iterator<?> it = c.iterator();
		while(it.hasNext())
		{
			if (!contains(it.next(), comparator))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Test if this list contains at least one element of given collection.
	 * @param c Collection to test.
	 * @return true if at least one o c elements is contained in this list, false otherwise.
	 */
	public boolean containsAtLeastOne(Collection<?> c)
	{
		Iterator<?> it = c.iterator();
		while(it.hasNext())
		{
			if (contains(it.next()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Test if this list contains at least one element of given collection, according to the given comparator.
	 * @param c collection to test elements of.
	 * @param comparator comparator implementation to use.
	 * @return true if at least one element of c is contained in this list according to the comparator compare method.
	 */
	public boolean containsAtLeastOne(Collection<?> c, Comparator<String> comparator)
	{
		Iterator<?> it = c.iterator();
		while (it.hasNext())
		{
			if (contains(it.next(), comparator))
			{
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Remove empty elements of this list.
	 */
	public void removeEmptyElements()
	{
		MyUtils.removeEmptyElements(this);
	}

}
