/**
 * @author Escallier Pierre
 * @file RefactoredClassNameTolerantObjectInputStream.java
 * @date 12 sept. 2008
 */
package epsofts.KanjiNoSensei.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

/**
 * 
 */
public class RefactoredClassNameTolerantObjectInputStream extends ObjectInputStream
{

	private final Map<String, Class<?>>	mappedClasses;

	private final Map<String, Package>	mappedPackages;

	/**
	 * @param in
	 * @throws IOException
	 */
	public RefactoredClassNameTolerantObjectInputStream(InputStream in, Map<String, Package> mappedPackages, Map<String, Class<?>> mappedClasses) throws IOException
	{
		super(in);
		this.mappedClasses = mappedClasses;
		this.mappedPackages = mappedPackages;
	}

	/**
	 * @param in
	 * @throws IOException
	 */
	public RefactoredClassNameTolerantObjectInputStream(InputStream in, Map<String, Package> mappedPackages) throws IOException
	{
		super(in);
		this.mappedClasses = null;
		this.mappedPackages = mappedPackages;
	}

	/**
	 * @throws IOException
	 * 
	 */
	public RefactoredClassNameTolerantObjectInputStream(Map<String, Package> mappedPackages) throws IOException
	{
		super();
		this.mappedClasses = null;
		this.mappedPackages = mappedPackages;
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException
	{
		Class<?> resultClass = null;

		try
		{
			resultClass = super.resolveClass(desc);
		}
		catch (ClassNotFoundException e)
		{
			if (mappedClasses != null)
			{
				if (mappedClasses.containsKey(desc.getName()))
				{
					MyUtils.trace(Level.WARNING, "Class " + desc.getName() + " mapped to " + mappedClasses.get(desc.getName()));
					return mappedClasses.get(desc.getName());
				}
			}

			if (mappedPackages != null)
			{
				String baseName = desc.getName();
				
				for(String name: mappedPackages.keySet())
				{
					Package pack = mappedPackages.get(name);
					
					if (baseName.startsWith(name))
					{
						try
						{
							resultClass = Class.forName(baseName.replaceFirst(name, pack.getName()));
							return resultClass;
						}
						catch(ClassNotFoundException e2)
						{
							// Let's try next mapped name.
						}
					}
				}
			}

			throw e;
		}

		return resultClass;
	}

}
