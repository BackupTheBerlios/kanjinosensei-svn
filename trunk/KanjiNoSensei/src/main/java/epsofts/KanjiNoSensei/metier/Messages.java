/**
 * @author Escallier Pierre
 * @file Messages.java
 * @date 6 janv. 08
 */
package epsofts.KanjiNoSensei.metier;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;


/**
 * Utility class that provide access to Messages resource.
 * Use {@link Messages#getString(String)} to get message for given key.
 */
public class Messages
{
	/** Resource bundle name. */
	private static final String			BUNDLE_NAME		= Messages.class.getPackage().getName() + ".messages";						//$NON-NLS-1$

	/** Resource bundle. */
	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME);

	/** Must not be instanciate. */
	private Messages()
	{
	}

	/**
	 * Return message for the given key.
	 * If the key isn't found, error is logged, {@code "!"+key+"!"} value is returned, and no exception is thrown.
	 * @param key Message key.
	 * @return message for the given key.
	 */
	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			KanjiNoSensei.log(Level.SEVERE, "MissingResourceException : key \""+key+"\" is not found in resource file \""+BUNDLE_NAME+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return '!' + key + '!';
		}
	}
}
