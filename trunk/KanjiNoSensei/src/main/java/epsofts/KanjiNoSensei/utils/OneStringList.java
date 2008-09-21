/**
 * @author Escallier Pierre
 * @file OneStringList.java
 * @date 27 déc. 07
 */
package epsofts.KanjiNoSensei.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

/**
 * This class represent a list of elements which support "one string format" input/output. This class extends Vector&lt;String&gt; so each element is unique on the list.
 */
public class OneStringList extends Vector<String>
{

	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;

	/** Allowed input separators. */
	private final String[]		ALLOWED_SEPARATOR;

	/** Separator used in one string list format. */
	private final String		SEPARATOR;

	/** Allowed braces. */
	private final String[]		ALLOWED_BRACES;

	/** Starting braces. */
	private final String[]		STARTING_BRACES;

	/** Ending braces. */
	private final String[]		ENDING_BRACES;

	private void initBraces()
	{
		int braceLength = ALLOWED_BRACES[0].length();

		Vector<String> separators = MyUtils.arrayToVector(ALLOWED_SEPARATOR);
		for (int i = 0; i < ALLOWED_BRACES.length; i += 2)
		{
			if ((ALLOWED_BRACES[i].length() != braceLength) || (ALLOWED_BRACES[i + 1].length() != braceLength))
			{
				throw new RuntimeException("OneStringList ALLOWED_BRACES must all have the same size: " + ALLOWED_BRACES.toString());
			}
			if (separators.contains(ALLOWED_BRACES[i]))
			{
				throw new RuntimeException("OneStringList can't use BRACE \"" + ALLOWED_BRACES[i] + "\" as SEPARATOR");
			}
			if (separators.contains(ALLOWED_BRACES[i + 1]))
			{
				throw new RuntimeException("OneStringList can't use BRACE \"" + ALLOWED_BRACES[i + 1] + "\" as SEPARATOR");
			}

			STARTING_BRACES[i / 2] = ALLOWED_BRACES[i];
			ENDING_BRACES[i / 2] = ALLOWED_BRACES[i + 1];
		}
	}

	/**
	 * Empty constructor, use default separator ", ".
	 */
	public OneStringList()
	{
		ALLOWED_SEPARATOR = new String[] {", "};
		SEPARATOR = ALLOWED_SEPARATOR[0];
		ALLOWED_BRACES = MyUtils.braces;
		STARTING_BRACES = new String[ALLOWED_BRACES.length / 2];
		ENDING_BRACES = new String[ALLOWED_BRACES.length / 2];
		initBraces();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param oneStringList
	 *            Copy source.
	 */
	public OneStringList(OneStringList oneStringList)
	{
		super(oneStringList);
		ALLOWED_SEPARATOR = oneStringList.ALLOWED_SEPARATOR;
		SEPARATOR = oneStringList.SEPARATOR;
		ALLOWED_BRACES = MyUtils.braces;
		STARTING_BRACES = new String[ALLOWED_BRACES.length / 2];
		ENDING_BRACES = new String[ALLOWED_BRACES.length / 2];
		initBraces();
	}

	/**
	 * Constructor with specified default separator, it will be the only one allowed.
	 * 
	 * @param separator
	 *            Default separator (and the only one allowed).
	 */
	public OneStringList(String separator)
	{
		ALLOWED_SEPARATOR = new String[] {separator};
		SEPARATOR = ALLOWED_SEPARATOR[0];
		ALLOWED_BRACES = MyUtils.braces;
		STARTING_BRACES = new String[ALLOWED_BRACES.length / 2];
		ENDING_BRACES = new String[ALLOWED_BRACES.length / 2];
		initBraces();
	}

	/**
	 * Constructor with specified allowed separators, the first is the default one.
	 * 
	 * @param allowedSeparators
	 *            Allowed separators, the first is the default one.
	 */
	public OneStringList(String[] allowedSeparators)
	{
		ALLOWED_SEPARATOR = allowedSeparators;
		SEPARATOR = ALLOWED_SEPARATOR[0];
		ALLOWED_BRACES = MyUtils.braces;
		STARTING_BRACES = new String[ALLOWED_BRACES.length / 2];
		ENDING_BRACES = new String[ALLOWED_BRACES.length / 2];
		initBraces();
	}

	/**
	 * Return this list in one string format, with default separator. {@code "element1" + SEPARATOR + "element2" + SEPARATOR + "element3" ...}
	 * 
	 * @return this list in one string format.
	 */
	@Override
	public String toString()
	{
		return toString(SEPARATOR);
	}

	/**
	 * Return this list in one string format, with specified separator.
	 * 
	 * @param separator
	 *            Specified separator to output the list in one string format.
	 * @return this list in one string format with the specified separator.
	 */
	public String toString(String separator)
	{
		return MyUtils.joinStringElements(toArray(new String[size()]), separator);
	}

	/**
	 * Add elements to this list, elements given in one string format.
	 * 
	 * @param oneStringFormat
	 *            One string list format string of elements to add.
	 */
	public void addFromString(String oneStringFormat)
	{
		if (oneStringFormat.isEmpty()) return;

		// We make all starting braces the same STARTING_BACES[0]; and all ending braces the same ENDING_BRACES[0]
		String startBrace = STARTING_BRACES[0];
		String endBrace = ENDING_BRACES[0];
		String maskedStringFormat = MyUtils.replaceAll(oneStringFormat, STARTING_BRACES, startBrace);
		maskedStringFormat = MyUtils.replaceAll(maskedStringFormat, ENDING_BRACES, endBrace);

		// We test the string is valid before to work with it.
		int sc, ec, pos;
		for (sc = 0, pos = 0; (pos = maskedStringFormat.indexOf(startBrace, pos) + 1) > 0; ++sc);
		for (ec = 0, pos = 0; (pos = maskedStringFormat.indexOf(endBrace, pos) + 1) > 0; ++ec);

		if (ec != sc)
		{
			MyUtils.trace(Level.WARNING, "OneStringList.addFromString: Braces count does not match in \"" + oneStringFormat + "\"");
			// throw new BadStringFormatException("Braces count does not match in \""+subject+"\"");
		}

		// SEParator, StartingBrace, EndingBrace, InBraces
		int sep = 0, sb = 0, eb = 0, ib = 0, fromIndex = 0;
		String trueSeparatorMark = getClass().getCanonicalName();
		do
		{
			sep = MyUtils.firstIndexOf(maskedStringFormat, ALLOWED_SEPARATOR, fromIndex);
			sb = maskedStringFormat.indexOf(startBrace, fromIndex);
			eb = maskedStringFormat.indexOf(endBrace, fromIndex);

			sep = (sep<0)?Integer.MAX_VALUE:sep;
			sb = (sb<0)?Integer.MAX_VALUE:sb;
			eb = (eb<0)?Integer.MAX_VALUE:eb;
			
			if (sep < sb && sep < eb)
			{
				if (ib <= 0)
				{
					String separator = MyUtils.bestStartsWith(maskedStringFormat, ALLOWED_SEPARATOR, sep);
					maskedStringFormat = maskedStringFormat.substring(0, sep) + trueSeparatorMark + maskedStringFormat.substring(sep + separator.length());
					oneStringFormat = oneStringFormat.substring(0, sep) + trueSeparatorMark + oneStringFormat.substring(sep + separator.length());
				}
				
				fromIndex = sep + trueSeparatorMark.length();
			}
			else if (sb < sep && sb < eb)
			{
				++ib;
				
				fromIndex = sb + startBrace.length();
			}
			else if (eb < sb && eb < sep)
			{
				--ib;
				
				fromIndex = eb + endBrace.length();
			}
			else
			{
				fromIndex = -1;
			}

		} while (fromIndex >= 0);

		String[] l = oneStringFormat.split(trueSeparatorMark);
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
	 * @param beginning
	 *            filter.
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
	 * 
	 * @param o
	 *            Object to test if contained.
	 * @param comparator
	 *            Comparator to use for contained conditions.
	 * @return true if object o is contained in this list, according to the comparator compare() implementation, false otherwise.
	 */
	public boolean contains(Object o, Comparator<String> comparator)
	{
		Iterator<String> it = iterator();
		while (it.hasNext())
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
	 * 
	 * @param c
	 *            Collection to test.
	 * @param comparator
	 *            Comparator to be used to test each element.
	 * @return true is all c elements are contained in this list according to the comparator compare method implementation.
	 * @see OneStringList#contains(Object, Comparator)
	 */
	public boolean containsAll(Collection<?> c, Comparator<String> comparator)
	{
		Iterator<?> it = c.iterator();
		while (it.hasNext())
		{
			if ( !contains(it.next(), comparator))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Test if this list contains at least one element of given collection.
	 * 
	 * @param c
	 *            Collection to test.
	 * @return true if at least one o c elements is contained in this list, false otherwise.
	 */
	public boolean containsAtLeastOne(Collection<?> c)
	{
		Iterator<?> it = c.iterator();
		while (it.hasNext())
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
	 * 
	 * @param c
	 *            collection to test elements of.
	 * @param comparator
	 *            comparator implementation to use.
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
