/**
 * @author Escallier Pierre
 * @file RefactoringInfos.java
 * @date 12 sept. 2008
 */
package epsofts.KanjiNoSensei;

import java.util.HashMap;
import java.util.Map;

import epsofts.KanjiNoSensei.utils.RefactoredClassNameTolerantObjectInputStream;

/**
 * Constants defining class.
 * This class defined refactored packages and class maps to be used with {@link RefactoredClassNameTolerantObjectInputStream}.
 */
public class RefactoringInfos
{
	/** Refactored packages map. Statically initialized. */
	public static final Map<String, Package>	REFACTORED_PACKAGES	= new HashMap<String, Package>();

	static
	{
		// In firsts versions, there was no root package.
		REFACTORED_PACKAGES.put("metier", epsofts.KanjiNoSensei.metier.Dictionary.class.getPackage());
		REFACTORED_PACKAGES.put("utils", epsofts.KanjiNoSensei.utils.MyUtils.class.getPackage());
		REFACTORED_PACKAGES.put("vue", epsofts.KanjiNoSensei.vue.KanjiNoSensei.class.getPackage());
	}
	
	public static Class<?> forName(String className) throws ClassNotFoundException
	{
		Class<?> resultClass = null;
		ClassNotFoundException notFoundEx = null;
		try
		{
			resultClass = Class.forName(className);
			return resultClass;
		}
		catch(ClassNotFoundException e)
		{
			notFoundEx = e;
		}
		
		for(String name: REFACTORED_PACKAGES.keySet())
		{
			Package pack = REFACTORED_PACKAGES.get(name);
			
			if (className.startsWith(name))
			{
				try
				{
					resultClass = Class.forName(className.replaceFirst(name, pack.getName()));
					return resultClass;
				}
				catch(ClassNotFoundException e2)
				{
					// Let's try next mapped name.
				}
			}
		}
		
		throw notFoundEx;
	}
}
