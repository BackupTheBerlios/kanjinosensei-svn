/**
 * @author Escallier Pierre
 * @file Messages.java
 * @date 6 janv. 08
 */
package metier;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
			return '!' + key + '!';
		}
	}
}
