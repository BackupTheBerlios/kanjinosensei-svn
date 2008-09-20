/**
 * @author Escallier Pierre
 * @file Messages.java
 * @date 6 janv. 08
 */
package epsofts.KanjiNoSensei.vue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.utils.MyUtils;



/**
 * Class used to manage a config file. The config file is used like a properties file, which can be saved and loaded.
 * Config is a singleton class.
 */
public class Config
{
	/** Default config file name. */
	public static final String			CONFIG_NAME		= "KanjiNoSensei.cfg";						//$NON-NLS-1$

	/** Properties object for this config object. */
	private final Properties CONFIG = new Properties();
	
	/** Singleton object. */
	private static final Config SINGLETON = new Config();
	
	/** Private constructor, to ensure Config is a singleton class. */
	private Config()
	{	
	}
	
	/**
	 * Open given config file.
	 * If the given file does not exist, it is created empty.
	 * @param fic Config file to open.
	 * @throws IOException on file error.
	 */
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
		
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(fic);
			SINGLETON.CONFIG.load(fis);
		}
		catch (IOException e)
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("Config.Error.OpeningFile")); //$NON-NLS-1$
			throw e;
		}
		finally
		{
			try
			{
				if (fis != null) fis.close();
			}
			catch(Exception e)
			{
				// Nothing.
			}
		}
	}

	/**
	 * Get value from key. If key isn't found, return null.
	 * @param key Key to get.
	 * @return key value, or null.
	 */
	public static String getString(String key)
	{
		return getString(key, null);
	}
	
	/**
	 * Get value from key. If key isn't found, return defaultValue.
	 * @param key Key to get.
	 * @param defaultValue Default value to return.
	 * @return key value, or defaultValue.
	 */
	public static String getString(String key, String defaultValue)
	{
		return SINGLETON.CONFIG.getProperty(key, defaultValue);
	}
	
	/**
	 * Set given key to the specified value.
	 * @param key Key to set.
	 * @param value Value to be set.
	 */
	public static void setString(String key, String value)
	{
		SINGLETON.CONFIG.setProperty(key, value);
	}
	
	/**
	 * Save current config. The filename is the same as the open call.
	 */
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
