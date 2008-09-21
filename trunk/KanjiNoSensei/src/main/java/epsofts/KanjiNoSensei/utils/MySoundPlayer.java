package epsofts.KanjiNoSensei.utils;

import java.awt.AWTEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;

import javax.media.Manager;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;

/**
 * Util class used for sound playing. This class must be used via static methods playSound, stopSounds. This class maps severals libraries to play severals sounds format. Library to use is detected with the file extension. Listener can be added to this class to be fired on player control events.
 * 
 * @author Escallier Pierre
 */
public class MySoundPlayer extends Thread
{
	/** JMF library supported extensions. */
	final static String[]	JMF_PLAYER_SUPPORT	= {"gsm", "wav", "aif", "au"};	//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	/** ZOOM library supported extensions. */
	final static String[]	ZOOM_PLAYER			= {"mp3"};						//$NON-NLS-1$

	/** MySoundPlayer event Id enumerated type. */
	public static enum MySoundPlayerEventId
	{
		stateChanged, noPlayer
	}

	/**
	 * MySoundPlayer event class.
	 * 
	 * @author Escallier Pierre
	 */
	public static class MySoundPlayerEvent extends AWTEvent
	{

		/** Indicate if the player is currently playing. */
		public boolean				isPlaying;

		/** Indicate the current or last filename the player used. */
		public String				fileName;

		/**
		 * Event Id.
		 * 
		 * @see MySoundPlayerEventId
		 */
		public MySoundPlayerEventId	id;

		/**
		 * MySoundPlayerEvent constructor, from all fields.
		 * 
		 * @param source
		 *            Object source of the event.
		 * @param id
		 *            This event id.
		 * @param isPlaying
		 *            Is the player currently playing.
		 * @param fileName
		 *            The current or last filename the player used.
		 */
		public MySoundPlayerEvent(Object source, MySoundPlayerEventId id, boolean isPlaying, String fileName)
		{
			super(source, id.ordinal());
			this.id = id;
			this.isPlaying = isPlaying;
			this.fileName = fileName;
		}

		/**
		 * Return quick display format of the event.
		 * 
		 * @return Quick display format of the event.
		 */
		@Override
		public String toString()
		{
			return "[id " + id + "; isPlaying " + isPlaying + "; fileName " + fileName + "] " + super.toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

	}

	/**
	 * Interface for MySoundPlayer listeners. Used on player state changed, and when error occurs.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface MySoundPlayerListener
	{
		/**
		 * Fire when the player state changed.
		 * 
		 * @param evt
		 *            Informations about the event.
		 */
		void stateChanged(MySoundPlayerEvent evt);

		/**
		 * Fire when error occurs.
		 * 
		 * @param evt
		 *            Informations about the event.
		 */
		void errorOccured(MySoundPlayerEvent evt);
	}

	/**
	 * Adapter class for MySoundPlayerListener.
	 * 
	 * @author Escallier Pierre
	 */
	public static abstract class MySoundPlayerAdapter implements MySoundPlayerListener
	{
		@Override
		public void stateChanged(MySoundPlayerEvent evt)
		{
		}

		@Override
		public void errorOccured(MySoundPlayerEvent evt)
		{
		}
	}

	/** Current filename in use. */
	private String												filename				= null;
	
	/** Current delay to use. */
	private int													delay					= 0;

	/** Current player thread. */
	private static MySoundPlayer								playerThread			= null;

	/** Current sequence player thread. */
	private static Thread										sequencePlayerThread	= null;

	/** Current player. */
	private static AnyPlayer									player					= null;

	/** List of all listeners. */
	// TODO: Big Memory Leak if we don't use WeakReference here.
	private static Set<WeakReference<MySoundPlayerListener>>	listeners				= new HashSet<WeakReference<MySoundPlayerListener>>();
	
	/**
	 * Abstract class to interface any player library. All methods should be overridden.
	 * 
	 * @author Escallier Pierre
	 */
	private static abstract class AnyPlayer
	{
		protected String	fileName	= null;

		protected String	playerName	= null;

		/**
		 * AnyPlayer constructor, should provide the fileName of the playing sound, and playerName used.
		 * 
		 * @param fileName
		 *            Filename of the playing sound.
		 * @param playerName
		 *            Name of the player used.
		 */
		protected AnyPlayer(String fileName, String playerName)
		{
			this.fileName = fileName;
			this.playerName = playerName;
		}

		/**
		 * @return the fileName
		 */
		public String getFileName()
		{
			return fileName;
		}

		/**
		 * @return the playerName
		 */
		public String getPlayerName()
		{
			return playerName;
		}

		/**
		 * Should play the file given by implementation class.
		 */
		abstract void play();

		/**
		 * Should stop the player (or do nothing if it is already stopped).
		 */
		abstract void stop();

		/**
		 * Should ensure the player (and file) are closed correctly.
		 */
		abstract void close();
	}

	/**
	 * Add a listener to this player.
	 * 
	 * @param listener
	 *            Listener to add.
	 */
	synchronized public static void addMySoundPlayerListener(MySoundPlayerListener listener)
	{
		listeners.add(new WeakReference<MySoundPlayerListener>(listener));
	}

	/**
	 * Remove a listener of this player.
	 * 
	 * @param listener
	 *            Listener to remove.
	 */
	synchronized public static void removeMySoundPlayerListener(MySoundPlayerListener listener)
	{
		// TODO: à tester..
		listeners.remove(new WeakReference<MySoundPlayerListener>(listener));
	}

	/**
	 * Private method used to process Event on listeners.
	 * 
	 * @param evt
	 *            Event to fire, the event id indicate wich listeners method is fired.
	 */
	synchronized private static void processEvent(MySoundPlayerEvent evt)
	{
		Iterator<WeakReference<MySoundPlayerListener>> it = listeners.iterator();
		while (it.hasNext())
		{
			MySoundPlayerListener listener = it.next().get();
			if (listener == null)
			{
				it.remove();
				continue;
			}

			switch (evt.id)
			{
			case stateChanged:
				listener.stateChanged(evt);
				break;

			case noPlayer:
				listener.errorOccured(evt);
				break;

			default:
				throw new RuntimeException("MySoundPlayer.processEvent : Unknown event type : " + evt); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Constructor, with filename parameter. To play it, use start().
	 * 
	 * @param filename
	 *            Path to the file to play.
	 * @param delay Delay in milliseconds before to play the sound file.
	 */
	private MySoundPlayer(String filename, int delay)
	{
		this.filename = filename;
		this.delay = delay;
	}

	/**
	 * Main thread method. This detect which library to use for the given file, then start playing it.
	 */
	@Override
	public void run()
	{
		MyUtils.trace(Level.FINEST, "MySoundPlayer running " + filename + "..."); //$NON-NLS-1$ //$NON-NLS-2$

		String ext = MyUtils.getExtension(filename);

		// We try to find a player to play the given file extension

		if (MyUtils.arrayToVector(JMF_PLAYER_SUPPORT).contains(ext))
		{
			File f = new File(filename);
			javax.media.Player jmPlayer = null;
			try
			{
				jmPlayer = Manager.createPlayer(f.toURI().toURL());
				final javax.media.Player finalJmPlayer = jmPlayer;

				player = new AnyPlayer(filename, "JMF_PLAYER") //$NON-NLS-1$
				{
					@Override
					void stop()
					{
						finalJmPlayer.stop();
					}

					@Override
					void play()
					{
						finalJmPlayer.start();
					}

					@Override
					void close()
					{
						finalJmPlayer.close();
					}

				};

				ext = ""; //$NON-NLS-1$
			}
			catch (Exception e)
			{
				MyUtils.safeClose(jmPlayer);
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("MySoundPlayer.JMF_Player.Error") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if (MyUtils.arrayToVector(ZOOM_PLAYER).contains(ext))
		{
			InputStream fis = null;
			BufferedInputStream bis = null;
			AdvancedPlayer advPlayer = null;
			try
			{
				fis = new FileInputStream(filename);
				bis = new BufferedInputStream(fis);
				advPlayer = new AdvancedPlayer(bis, FactoryRegistry.systemRegistry().createAudioDevice());
				final AdvancedPlayer finalAdvPlayer = advPlayer;

				player = new AnyPlayer(filename, "ZOOM_PLAYER") //$NON-NLS-1$
				{
					@Override
					void stop()
					{
						finalAdvPlayer.stop();
					}

					@Override
					void play()
					{
						try
						{
							finalAdvPlayer.play();
						}
						catch (JavaLayerException e)
						{
							KanjiNoSensei.log(Level.SEVERE, Messages.getString("MySoundPlayer.MySoundPlayer.ZOOM_Player.Error")); //$NON-NLS-1$
							e.printStackTrace();
						}
					}

					@Override
					void close()
					{
						finalAdvPlayer.close();
					}

				};

				ext = ""; //$NON-NLS-1$
			}
			catch (Exception e)
			{
				MyUtils.safeClose(fis);
				MyUtils.safeClose(bis);
				MyUtils.safeClose(advPlayer);
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("MySoundPlayer.ZOOM_Player.Error") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if ( !ext.isEmpty())
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("MySoundPlayer.ErrorNoPlayerFoundToPlayFile") + " : \"" + filename + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.noPlayer, false, filename));
			return;
		}

		try
		{
			Thread.sleep(delay);
		}
		catch (InterruptedException e)
		{
			if (isInterrupted())
			{
				return;
			}
		}
		
		processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.stateChanged, true, filename));
		player.play();
		MyUtils.trace(Level.FINEST, player.getPlayerName() + " stoped " + player.getFileName()); //$NON-NLS-1$
		processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.stateChanged, false, filename));

		MyUtils.trace(Level.FINEST, "MySoundPlayer finished " + filename + "..."); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/**
	 * Try to found an appropriate player for the given file and play it.
	 * 
	 * @param filename
	 *            Sound file to play.
	 */
	public static void playSound(String filename)
	{
		playSound(filename, 0);
	}
	
	/**
	 * Try to found an appropriate player for the given file and play it after delay milliseconds.
	 * @param filename Sound file to play.
	 * @param delay Delay in milliseconds before playing.
	 */
	public static void playSound(String filename, int delay)
	{
		if (playerThread != null)
		{
			playerThread.stopSound();
		}
		playerThread = new MySoundPlayer(filename, delay);
		playerThread.start();
	}

	/**
	 * Play a list of sounds in sequence.
	 * 
	 * @param filenames
	 */
	public static void playSounds(Stack<String> filenames, String sequenceName)
	{
		// TODO: L'algo est pourri.. on repique sur la même séquence dès lors qu'un même fichier est utilisé plusieurs fois.. :(
		// Revoir les méthodes de base pour s'assurer d'avoir une méthode "playAndWait()" qui joue et bloque jusqu'à ce que le soit ai effectivement fini de jouer.
		// Ensuite on n'aura qu'a enchainer les playAndWait ici.
		
		final Stack<MySoundPlayerListener> listeners = new Stack<MySoundPlayerListener>();
		
		Iterator<String> it = filenames.iterator();
		final String firstFilename = it.next();
		String filename = firstFilename;
		int blank;
		while(it.hasNext())
		{	
			final String finalFilename = filename;
			
			for(blank = 1; (it.hasNext()) && ((filename = it.next()) == null); ++blank);
			
			final String finalNextFilename = (finalFilename.equals(filename)?null:filename);
			final int finalBlank = blank;
			
			listeners.add(new MySoundPlayerListener()
			{

				@Override
				public void stateChanged(MySoundPlayerEvent evt)
				{
					if (evt.fileName.compareTo(finalFilename) == 0)
					{
						if (evt.isPlaying)
						{
							MyUtils.trace(Level.INFO, "playSounds: "+evt.fileName+" is playing");
						}
						else
						{
							if (finalNextFilename != null)
							{
								new Thread(new Runnable()
								{
								
									@Override
									public void run()
									{
										playSound(finalNextFilename, finalBlank*1000);
									}
								}).start();
							}
							else
							{
								new Thread(new Runnable()
								{
								
									@Override
									public void run()
									{
										stopSounds();
									}
								}).start();
							}
						}
					}
				}

				@Override
				public void errorOccured(MySoundPlayerEvent evt)
				{
					if (evt.fileName.compareTo(finalFilename) == 0)
					{
						MyUtils.trace(Level.WARNING, "playSounds error: " + evt.toString());
						new Thread(new Runnable()
						{
						
							@Override
							public void run()
							{
								playSound(finalNextFilename, finalBlank*1000);
							}
						}).start();
					}
				}
			});
		}
		
		stopSounds();
		sequencePlayerThread = new Thread(new Runnable()
		{
		
			@Override
			public void run()
			{
				Iterator<MySoundPlayerListener> it = listeners.iterator();
				while(it.hasNext())
				{
					MySoundPlayerListener listener = it.next();
					addMySoundPlayerListener(listener);
				}
				
				playSound(firstFilename);
			}
		});
		
		sequencePlayerThread.start();
	}

	/**
	 * Stop all currently playing sounds (from this class).
	 */
	public static void stopSounds()
	{
		if (sequencePlayerThread != null)
		{
			sequencePlayerThread.interrupt();
			try
			{
				sequencePlayerThread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			sequencePlayerThread = null;
		}

		if (playerThread != null)
		{
			playerThread.stopSound();
		}
	}

	/**
	 * Stop the currently playing sound. Close the player.
	 */
	public void stopSound()
	{
		if (player != null)
		{
			MyUtils.safeClose(player);
			try
			{
				this.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		String baseDir = "/media/data/Code/Java_Workspace/KanjiNoSensei/dico/kana_female";
		Stack<String> filenames = new Stack<String>();
		
		BufferedReader	fromKeyboard	= new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		do
		{
			System.out.println("Tapez en kana la phrase à lire; 'exit' pour quitter.");
			try
			{
				line = fromKeyboard.readLine();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
			
			filenames.clear();
			for(int i=0; i < line.length(); ++i)
			{
				if (line.charAt(i) == ' ')
				{
					filenames.add(null);
				}
				else
				{
					filenames.add(baseDir+"/"+MyUtils.kanaToRomaji(line.substring(i, i+1))+".wav");
				}
			}
			MySoundPlayer.playSounds(filenames, line);
		}while(line.compareToIgnoreCase("exit") != 0);
	}
}
