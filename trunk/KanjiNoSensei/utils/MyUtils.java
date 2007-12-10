/**
 * 
 */
package utils;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

/**
 * @author axan
 * 
 */
public class MyUtils
{
	static public FileFilter generateFileFilter(String description, String extension)
	{
		return generateFileFilter(description, new String[] {extension});
	}
	static public FileFilter generateFileFilter(final String description, final String[] extensions)
	{
		final StringBuffer sbdesc = new StringBuffer(description);
		sbdesc.append("(");
		for(String ext: extensions)
		{
			sbdesc.append("*."+ext+"; ");
		}
		sbdesc.append(")");
		
		return new FileFilter()
		{
		
			@Override
			public String getDescription()
			{
				return sbdesc.toString();
			}
		
			@Override
			public boolean accept(File f)
			{
				if (f.isDirectory()) return true;

				String ext = getExtension(f);
				if (ext == null) return false;
				
				for(String s : extensions)
				{
					if (ext.compareToIgnoreCase(s) == 0)
					{
						return true;
					}
				}
				
				return false;

			}
		
		};
	}
	
	public static String getExtension(File f)
	{
		return getExtension(f.getName());
	}
	public static String getExtension(String s)
	{
		String ext = null;
		
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
		{
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
	
	public static <E> Vector<E> arrayToVector(E[] objs)
	{
		Vector<E> v = new Vector<E>(objs.length);
		for(E e : objs)
		{
			v.add(e);
		}
		
		return v;
	}
	
	public static void lockPanel(Component c)
	{
		System.out.println(c.toString());
		c.setEnabled(false);
//		c.setFocusable(false);
//		c.setBackground(Color.red);
			
		if (Container.class.isInstance(c))
		{
			for(Component sc : ((Container) c).getComponents())
			{
				lockPanel(sc);
			}
		}
	}
	
	public static void removeEmptyElements(Set<String> set)
	{
		Iterator<String> it = set.iterator();
		while (it.hasNext())
		{
			String s = it.next().replace(" ", "");
			if (s.isEmpty()) it.remove();
		}
	}

	public static String joinStringElements(String[] elements, String glue)
	{
		StringBuffer sb = new StringBuffer();
		boolean first = true;

		for (String s : elements)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(glue);
			}
			sb.append(s);
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] offsetObjectElements(T[] elements, int offset)
	{
		if (elements.length <= 0) return elements;

		Vector<T> restants = new Vector<T>();

		for (int i = offset; i < elements.length; ++i)
		{
			restants.add(elements[i]);
		}

		T[] array = (T[]) Array.newInstance(elements[0].getClass(), restants.size());

		return restants.toArray(array);
	}

	public static String stripQuotes(String chaine)
	{
		if ((chaine.startsWith("\"")) && (chaine.endsWith("\"")))
		{
			return chaine.substring(1, chaine.length() - 1);
		}

		return chaine;
	}
}
