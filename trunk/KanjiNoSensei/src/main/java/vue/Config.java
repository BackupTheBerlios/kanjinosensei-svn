/**
 * @author Escallier Pierre
 * @file Messages.java
 * @date 6 janv. 08
 */
package vue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

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
		System.out.println("Using config file : \""+fic.getAbsolutePath()+"\"");
		
		if (!fic.exists())
		{
			System.err.println("Warning: Config file does not exist, trying to create a new one.");
			
			try
			{
				fic.createNewFile();
			}
			catch (IOException e)
			{
				//e.printStackTrace();
				System.err.println("Impossible de créer le fichier de config \""+fic.getAbsolutePath()+"\"");
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
			System.err.println("Erreur de lecture du fichier de config.");
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
			SINGLETON.CONFIG.store(new FileOutputStream(CONFIG_NAME), "KanjiNoSensei config file");
		}
		catch (IOException e)
		{
			System.err.println("Erreur de sauvegarde du fichier de config.");
			e.printStackTrace();
		}
	}
}
