/**
 * 
 */
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
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * @author axan
 * 
 */
public class MySoundPlayer extends Thread
{
	final static String[]	JMF_PLAYER_SUPPORT	= {"gsm", "wav", "aif", "au"};
	final static String[]	ZOOM_PLAYER			= {"mp3"};

	public static enum MySoundPlayerEventId
	{
		stateChanged, noPlayer
	}

	public static class MySoundPlayerEvent extends AWTEvent
	{

		public boolean				isPlaying;
		public String				fileName;
		public MySoundPlayerEventId	id;

		/**
		 * @param source
		 * @param id
		 */
		public MySoundPlayerEvent(Object source, MySoundPlayerEventId id, boolean isPlaying, String fileName)
		{
			super(source, id.ordinal());
			this.id = id;
			this.isPlaying = isPlaying;
			this.fileName = fileName;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.AWTEvent#toString()
		 */
		@Override
		public String toString()
		{
			return "[id "+id+"; isPlaying "+isPlaying+"; fileName "+fileName+"] "+super.toString();
		}

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;

	}

	public static interface MySoundPlayerListener
	{
		void stateChanged(MySoundPlayerEvent evt);
		void errorOccured(MySoundPlayerEvent evt);
	}
	public static abstract class MySoundPlayerAdapter implements MySoundPlayerListener
	{
		public void stateChanged(MySoundPlayerEvent evt) {}
		public void errorOccured(MySoundPlayerEvent evt) {}
	}

	private String									filename		= null;
	private static MySoundPlayer					playerThread	= null;
	private static AnyPlayer						player			= null;
	private static Set<MySoundPlayerListener>	listeners	= new HashSet<MySoundPlayerListener>();

	private static abstract class AnyPlayer
	{	
		/**
		 * @return the fileName
		 */
		public String getFileName() {return "unknown";}
		
		/**
		 * @return the playerName
		 */
		public String getPlayerName() {return "unknown";}

		void play()
		{
			System.out.println(getPlayerName() + " play " + getFileName());
		}

		void stop()
		{
			System.out.println(getPlayerName() + " stop " + getFileName());
		}

		void close()
		{
			System.out.println(getPlayerName() + " close " + getFileName());
		}
	}

	synchronized public static void addMySoundPlayerListener(MySoundPlayerListener listener)
	{
		listeners.add(listener);
	}

	synchronized public static void removeMySoundPlayerListener(MySoundPlayerListener listener)
	{
		listeners.remove(listener);
	}

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
					throw new RuntimeException("MySoundPlayer.processEvent : Unknown event type : "+evt);
			}
		}
	}

	private MySoundPlayer(String filename)
	{
		this.filename = filename;
	}

	@Override
	public void run()
	{
		System.out.println("MySoundPlayer running " + filename + "...");

		String ext = MyUtils.getExtension(filename);

		if (MyUtils.arrayToVector(JMF_PLAYER_SUPPORT).contains(ext))
		{
			File f = new File(filename);
			try
			{
				final javax.media.Player jmPlayer = Manager.createPlayer(f.toURI().toURL());
				
				player = new AnyPlayer()
				{
					/* (non-Javadoc)
					 * @see utils.MySoundPlayer.AnyPlayer#getFileName()
					 */
					@Override
					public String getFileName()
					{
						return filename;
					}
					
					/* (non-Javadoc)
					 * @see utils.MySoundPlayer.AnyPlayer#getPlayerName()
					 */
					@Override
					public String getPlayerName()
					{
						return "JMF_PLAYER";
					}

					@Override
					void stop()
					{
						super.stop();
						jmPlayer.stop();
					}

					@Override
					void play()
					{
						super.play();
						jmPlayer.start();
					}

					@Override
					void close()
					{
						super.close();
						jmPlayer.close();
					}

				};

				ext = "";
			}
			catch (Exception e)
			{
				System.err.println("Erreur chargement JMF_PLAYER : "+e.getMessage());
			}
		}

		if (MyUtils.arrayToVector(ZOOM_PLAYER).contains(ext))
		{
			try
			{
				InputStream fin = new FileInputStream(filename);
				final BufferedInputStream bin = new BufferedInputStream(fin);
				final AdvancedPlayer advPlayer = new AdvancedPlayer(bin, getAudioDevice());
				
				player = new AnyPlayer()
				{


					/* (non-Javadoc)
					 * @see utils.MySoundPlayer.AnyPlayer#getPlayerName()
					 */
					@Override
					public String getPlayerName()
					{
						return "ZOOM_PLAYER";
					}
					
					/* (non-Javadoc)
					 * @see utils.MySoundPlayer.AnyPlayer#getFileName()
					 */
					@Override
					public String getFileName()
					{
						return filename;
					}
					
					@Override
					void stop()
					{
						super.stop();
						advPlayer.stop();
					}

					@Override
					void play()
					{
						super.play();
						try
						{
							advPlayer.play();
						}
						catch (JavaLayerException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					void close()
					{
						super.close();
						advPlayer.close();
					}

				};

				ext = "";
			}
			catch (Exception e)
			{
				System.err.println("Erreur chargement ZOOM_PLAYER : "+e.getMessage());
			}
		}

		if (!ext.isEmpty())
		{
			System.err.println("Aucun player trouvé pour jouer "+filename);
			processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.noPlayer, false, filename));
			return;
		}
		
		processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.stateChanged, true, filename));
		player.play();
		System.out.println(player.getPlayerName() + " stoped " + player.getFileName());
		processEvent(new MySoundPlayerEvent(this, MySoundPlayerEventId.stateChanged, false, filename));

		System.out.println("MySoundPlayer finished " + filename + "...");

	}

	public static void playSound(String filename)
	{
		if (playerThread != null)
		{
			playerThread.stopSound();
		}
		playerThread = new MySoundPlayer(filename);
		playerThread.start();
	}

	public static void stopSounds()
	{
		if (playerThread != null)
		{
			playerThread.stopSound();
		}
	}

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

	//private static AudioDevice	zoomAudioDevice	= null;

	protected static AudioDevice getAudioDevice() throws JavaLayerException
	{
		return FactoryRegistry.systemRegistry().createAudioDevice();
		/*
		if (zoomAudioDevice == null)
		{
			zoomAudioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
		}
		return zoomAudioDevice;
		*/
	}

}
