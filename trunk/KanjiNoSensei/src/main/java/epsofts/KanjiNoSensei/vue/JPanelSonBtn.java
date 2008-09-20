/**
 * 
 */
package epsofts.KanjiNoSensei.vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JToggleButton;

import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.utils.MySoundPlayer;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.MySoundPlayer.MySoundPlayerEvent;
import epsofts.KanjiNoSensei.utils.MySoundPlayer.MySoundPlayerListener;




/**
 * Class that represent a sound toggle button panel.
 */
public class JPanelSonBtn extends JToggleButton
{

	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;
	
	/** Sound file name. */
	private String nomFichierSon = null;
	
	/** MySoundPlayerListener. */
	private final MySoundPlayerListener soundPlayerListener = new MySoundPlayer.MySoundPlayerAdapter()
	{
		
		@Override
		public void stateChanged(MySoundPlayerEvent evt)
		{
			MyUtils.trace(Level.INFO, "JPanelSonBtn.MySoundPlayerListener.stateChanged("+evt.isPlaying+", "+evt.fileName+")");
			setSelected((evt.isPlaying) && (nomFichierSon.compareToIgnoreCase(evt.fileName) == 0));
			super.stateChanged(evt);
		}
	
		@Override
		public void errorOccured(MySoundPlayerEvent evt)
		{
			if (nomFichierSon.compareToIgnoreCase(evt.fileName) == 0)
			{
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("JPanelSonBtn.ErrorPlayingSound")+" : \""+evt.fileName+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				setSelected((evt.isPlaying) && (nomFichierSon.compareToIgnoreCase(evt.fileName) == 0));
			}
			super.errorOccured(evt);
		}
	
	};
	
	/**
	 * Empty constructor, sound file name is set to {@link Messages} resource key "JPanelSonBtn.None".
	 */
	public JPanelSonBtn()
	{
		nomFichierSon = Messages.getString("JPanelSonBtn.None"); //$NON-NLS-1$
	}
	
	/**
	 * Constructor define the sound filename, and if the sound is played right now.
	 * @param fileName Sound file name.
	 * @param play If true, the sound is played now.
	 */
	public JPanelSonBtn(String fileName, boolean play)
	{
		this(new String[] {fileName}, play);
	}
	
	/**
	 * Constructor define sounds sequence with a list of sounds filenames. A flag "play" mark if the sounds have to be played right now.
	 * @param fileNames List of sounds filenames to play.
	 * @param play If true, the sounds are played now. 
	 */
	public JPanelSonBtn(String[] fileNames, boolean play)
	{
		//TODO: A voir pour jouer des séquences..
		nomFichierSon = fileNames[0];
		
		this.addActionListener(new ActionListener()
		{
		
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (JPanelSonBtn.this.isSelected())
				{
					MySoundPlayer.playSound(nomFichierSon);
				}
				else
				{
					MySoundPlayer.stopSounds();
				}		
			}
		
		});	
		
		MySoundPlayer.addMySoundPlayerListener(soundPlayerListener);
		
		if (play) MySoundPlayer.playSound(nomFichierSon);
	}
}
