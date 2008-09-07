/**
 * @author Escallier Pierre
 * @file Messages.java
 * @date 6 janv. 08
 */
package metier;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import utils.MyUtils;
import vue.KanjiNoSensei;

/**
 * 
 */
public class Messages
{
	private static final String			BUNDLE_NAME		= "metier.messages";						//$NON-NLS-1$

	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages()
	{
	}

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
