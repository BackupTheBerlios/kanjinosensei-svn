/**
 * @author Escallier Pierre
 * @file Messages.java
 * @date 6 janv. 08
 */
package vue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import utils.MyUtils;

import metier.Messages;

/**
 * 
 */
public class Config
{
	public static final String			CONFIG_NAME		= "KanjiNoSensei.cfg";						//$NON-NLS-1$

	private final Properties CONFIG = new Properties();
	
	private static final Config SINGLETON = new Config();
	
	private Config()
	{
		
	}
	
	public static void open(File fic) throws IOException
	{
		KanjiNoSensei.log(Level.INFO, Messages.getString("Config.OpeningFile") + " : \""+fic.getAbsolutePath()+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		if (!fic.exists())
		{
			KanjiNoSensei.log(Level.WARNING, Messages.getString("Config.Warning.FileDoesNotExist")); //$NON-NLS-1$
			
			try
			{
				fic.createNewFile();
			}
			catch (IOException e)
			{
				//e.printStackTrace();
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("Config.Error.CantCreateFile") + " \""+fic.getAbsolutePath()+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				throw e;
			}
		}
		
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(fic);
			SINGLETON.CONFIG.load(fis);
			fis.close();
		}
		catch (IOException e)
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("Config.Error.OpeningFile")); //$NON-NLS-1$
			throw e;
		}
	}

	public static String getString(String key)
	{
		return getString(key, null);
	}
	public static String getString(String key, String defaultValue)
	{
		return SINGLETON.CONFIG.getProperty(key, defaultValue);
	}
	
	public static void setString(String key, String value)
	{
		SINGLETON.CONFIG.setProperty(key, value);
	}
	
	public static void save()
	{
		try
		{
			SINGLETON.CONFIG.store(new FileOutputStream(CONFIG_NAME), "KanjiNoSensei config file"); //$NON-NLS-1$
		}
		catch (IOException e)
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("Config.Error.SavingFile")); //$NON-NLS-1$
			e.printStackTrace();
		}
	}
}
