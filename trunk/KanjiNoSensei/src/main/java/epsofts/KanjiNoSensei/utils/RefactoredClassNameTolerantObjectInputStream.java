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
import java.util.Map;
import java.util.logging.Level;

/**
 * Extends {@link ObjectInputStream} to implement Class/Package refactoring tolerance.
 * This mean you can use your {@link RefactoredClassNameTolerantObjectInputStream} to readObject from an old saved file where the data class name was oldPackage.ClassName instead of the new newPackage.NewClassName.
 */
public class RefactoredClassNameTolerantObjectInputStream extends ObjectInputStream
{

	/** Map of mapped class, key is old class name, value is the new Class object to be use. */
	private final Map<String, Class<?>>	mappedClasses;

	/** Map of mapped packages, key is old package name, value is the new Package object to be use. */
	private final Map<String, Package>	mappedPackages;

	/**
	 * Constructor with InputStream, mappedPackages and mappedClasses specified.
	 * Note that mapped class must be compatible with old version streamed objects.
	 * @param in InputStream to use.
	 * @param mappedPackages mapped packages such as key is the old package name, and value the new Package object.
	 * @param mappedClasses mapped classes such as key is the old class name, and value the new Class object.
	 * @throws IOException can be thrown by {@link ObjectInputStream#ObjectInputStream(InputStream)}.
	 */
	public RefactoredClassNameTolerantObjectInputStream(InputStream in, Map<String, Package> mappedPackages, Map<String, Class<?>> mappedClasses) throws IOException
	{
		super(in);
		this.mappedClasses = mappedClasses;
		this.mappedPackages = mappedPackages;
	}

	/**
	 * Constructor with InputStream and mappedPackages specified.
	 * Note that mapped class must be compatible with old version streamed objects.
	 * @param in InputStream to use.
	 * @param mappedPackages mapped packages such as key is the old package name, and value the new Package object.
	 * @throws IOException can be thrown by {@link ObjectInputStream#ObjectInputStream(InputStream)}.
	 */
	public RefactoredClassNameTolerantObjectInputStream(InputStream in, Map<String, Package> mappedPackages) throws IOException
	{
		super(in);
		this.mappedClasses = null;
		this.mappedPackages = mappedPackages;
	}

	/**
	 * Constructor with only mappedPackages specified.
	 * Note that mapped class must be compatible with old version streamed objects.
	 * @param mappedPackages mapped packages such as key is the old package name, and value the new Package object.
	 * @throws IOException can be thrown by {@link ObjectInputStream#ObjectInputStream()}.
	 */
	public RefactoredClassNameTolerantObjectInputStream(Map<String, Package> mappedPackages) throws IOException
	{
		super();
		this.mappedClasses = null;
		this.mappedPackages = mappedPackages;
	}

	/**
	 * Resolve the class from the ObjectStreamClass description, using the tolerance algorithm and given mapped packages and classes.
	 * Note that mapped class must be compatible with old version streamed objects.
	 * @param desc Current object stream class description.
	 * @throws IOException if {@link ObjectInputStream#resolveClass(ObjectStreamClass)} throws it.
	 * @throws ClassNotFoundException if {@link ObjectInputStream#resolveClass(ObjectStreamClass)} throws it, and no mapping class found.
	 * @see ObjectInputStream#resolveClass(ObjectStreamClass)
	 * @return Class object to use for reading current object from stream.
	 */
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
