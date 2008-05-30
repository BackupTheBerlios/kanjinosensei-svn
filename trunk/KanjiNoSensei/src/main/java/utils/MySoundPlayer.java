package utils;

import java.awt.AWTEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.media.Manager;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * Util class used for sound playing.
 * This class must be used via static methods playSound, stopSounds.
 * This class maps severals libraries to play severals sounds format.
 * Library to use is detected with the file extension.
 * Listener can be added to this class to be fired on player control events.
 * @author Escallier Pierre
 */
public class MySoundPlayer extends Thread
{
	/** JMF library supported extensions. */
	final static String[]	JMF_PLAYER_SUPPORT	= {"gsm", "wav", "aif", "au"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	
	/** ZOOM library suppoerted extentions. */
	final static String[]	ZOOM_PLAYER			= {"mp3"}; //$NON-NLS-1$

	/** MySoundPlayer event Id enumerated type. */
	public static enum MySoundPlayerEventId
	{
		stateChanged, noPlayer
	}

	/**
	 * MySoundPlayer event class.
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
		 * @see MySoundPlayerEventId
		 */
		public MySoundPlayerEventId	id;

		/**
		 * MySoundPlayerEvent constructor, from all fields.
		 * @param source Object source of the event.
		 * @param id This event id.
		 * @param isPlaying Is the player currently playing.
		 * @param fileName The current or last filename the player used.
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
		 * @return Quick display format of the event.
		 */
		@Override
		public String toString()
		{
			return "[id "+id+"; isPlaying "+isPlaying+"; fileName "+fileName+"] "+super.toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

	}

	/**
	 * Interface for MySoundPlayer listeners.
	 * Used on player state changed, and when error occurs.
	 * @author Escallier Pierre
	 */
	public static interface MySoundPlayerListener
	{
		/**
		 * Fire when the player state changed.
		 * @param evt Informations about the event.
		 */
		void stateChanged(MySoundPlayerEvent evt);
		
		/**
		 * Fire when error occurs. 
		 * @param evt Informations about the event.
		 */
		void errorOccured(MySoundPlayerEvent evt);
	}
	
	/**
	 * Adapter class for MySoundPlayerListener.
	 * @author Escallier Pierre
	 */
	public static abstract class MySoundPlayerAdapter implements MySoundPlayerListener
	{
		public void stateChanged(MySoundPlayerEvent evt) {}
		public void errorOccured(MySoundPlayerEvent evt) {}
	}

	/** Current filename in use. */
	private String									filename		= null;
	
	/** Current player thread. */
	private static MySoundPlayer					playerThread	= null;
	
	/** Current player. */
	private static AnyPlayer						player			= null;
	
	/** List of all listeners. */
	private static Set<MySoundPlayerListener>	listeners	= new HashSet<MySoundPlayerListener>();

	/**
	 * Abstract class to interface any player library.
	 * All methods should be overridden.
	 * @author Escallier Pierre
	 */
	private static abstract class AnyPlayer
	{	
		protected String fileName = null;
		protected String playerName = null;
		
		/**
		 * AnyPlayer constructor, should provide the fileName of the playing sound, and playerName used.
		 * @param fileName	Filename of the playing sound.
		 * @param playerName	Name of the player used.
		 */
		protected AnyPlayer(String fileName, String playerName)
		{
			this.fileName = fileName;
			this.playerName = playerName;
		}
		
		/**
		 * @return the fileName
		 */
		public String getFileName() {return fileName;}
		
		/**
		 * @return the playerName
		 */
		public String getPlayerName() {return playerName;}

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
	 * @param listener Listener to add.
	 */
	synchronized public static void addMySoundPlayerListener(MySoundPlayerListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Remove a listener of this player.
	 * @param listener Listener to remove.
	 */
	synchronized public static void removeMySoundPlayerListener(MySoundPlayerListener listener)
	{
		listeners.remove(listener);
	}

	/**
	 * Private method used to process Event on listeners.
	 * @param evt Event to fire, the event id indicate wich listeners method is fired.
	 */
	synchronized private static void processEvent(MySoundPlayerEvent evt)
	{
		Iterator<MySoundPlayerListener> it = listeners.iterator();
		while (it.hasNext())
		{
			MySoundPlayerListener listener = it.next();
			
			switch (evt.id)
			{
				case stateChanged:
					listener.stateChanged(evt);					
					break;

				case noPlayer:
					listener.errorOccured(evt);
					break;
				
				default:
					throw new RuntimeException("MySoundPlayer.processEvent : Unknown event type : "+evt); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Constructor, with filename parameter.
	 * To play it, use start().
	 * @param filename Path to the file to play.
	 */
	private MySoundPlayer(String filename)
	{
		this.filename = filename;
	}

	/**
	 * Main thread method. This detect which library to use for the given file, then start playing it.
	 */
	@Override
	public void run()
	{
		MyUtils.trace("MySoundPlayer running " + filename + "..."); //$NON-NLS-1$ //$NON-NLS-2$

		String ext = MyUtils.getExtension(filename);

		if (MyUtils.arrayToVector(JMF_PLAYER_SUPPORT).contains(ext))
		{
			File f = new File(filename);
			try
			{
				final javax.media.Player jmPlayer = Manager.createPlayer(f.toURI().toURL());
				
				player = new AnyPlayer(filename, "JMF_PLAYER") //$NON-NLS-1$
				{
					@Override
					void stop()
					{
						jmPlayer.stop();
					}

					@Override
					void play()
					{
						jmPlayer.start();
					}

					@Override
					void close()
					{
						jmPlayer.close();
					}

				};

				ext = ""; //$NON-NLS-1$
			}
			catch (Exception e)
			{
				System.err.println(Messages.getString("MySoundPlayer.JMF_Player.Error")+ " : " +e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if (MyUtils.arrayToVector(ZOOM_PLAYER).contains(ext))
		{
			try
			{
				InputStream fin = new FileInputStream(filename);
				final BufferedInputStream bin = new BufferedInputStream(fin);
				final AdvancedPlayer advPlayer = new AdvancedPlayer(bin, FactoryRegistry.systemRegistry().createAudioDevice());
				
				player = new AnyPlayer(filename, "ZOOM_PLAYER") //$NON-NLS-1$
				{
					@Override
					void stop()
					{
						advPlayer.stop();
					}

					@Override
					void play()
					{
						try
						{
							advPlayer.play();
						}
						catch (JavaLayerException e)
						{
							System.err.println(Messages.getString("MySoundPlayer.MySoundPlayer.ZOOM_Player.Error")); //$NON-NLS-1$
							e.printStackTrace();
						}
					}

					@Override
					void close()
					{
						advPlayer.close();
					}

				};

				ext = ""; //$NON-NLS-1$
			}
			catch (Exception e)
			{
				System.err.println(Messages.getString("MySoundPlayer.ZOOM_Player.Error")+ " : "+e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if (!ext.isEmpty())
		{
			System.err.println(Messages.getString("MySoundPlayer.ErrorNoPlayerFoundToPlayFile")+" : \""+filename+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.noPlayer, false, filename));
			return;
		}
		
		processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.stateChanged, true, filename));
		player.play();
		MyUtils.trace(player.getPlayerName() + " stoped " + player.getFileName()); //$NON-NLS-1$
		processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.stateChanged, false, filename));

		MyUtils.trace("MySoundPlayer finished " + filename + "..."); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/**
	 * Try to found an appropriate player for the given file and play it.
	 * @param filename Sound file to play.
	 */
	public static void playSound(String filename)
	{
		if (playerThread != null)
		{
			playerThread.stopSound();
		}
		playerThread = new MySoundPlayer(filename);
		playerThread.start();
	}

	/**
	 * Stop all currently playing sounds (from this class).
	 */
	public static void stopSounds()
	{
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
			player.close();
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

}
