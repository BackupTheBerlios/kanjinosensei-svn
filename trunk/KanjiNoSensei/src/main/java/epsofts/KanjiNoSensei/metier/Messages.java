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
 * 
 */
public class Messages
{
	private static final String			BUNDLE_NAME		= Messages.class.getPackage().getName() + ".messages";						//$NON-NLS-1$

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
