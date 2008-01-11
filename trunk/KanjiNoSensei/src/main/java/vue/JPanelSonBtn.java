/**
 * 
 */
package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import metier.Messages;

import utils.MySoundPlayer;
import utils.MySoundPlayer.MySoundPlayerEvent;


/**
 * @author axan
 *
 */
public class JPanelSonBtn extends JToggleButton
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private JPanelSonBtn btnSon = this;
	
	private String nomFichierSon = null;
	
	public JPanelSonBtn()
	{
		nomFichierSon = Messages.getString("JPanelSonBtn.None"); //$NON-NLS-1$
	}
	
	public JPanelSonBtn(String fileName, boolean play)
	{
		nomFichierSon = fileName;
		
		this.addActionListener(new ActionListener()
		{
		
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (btnSon.isSelected())
				{
					MySoundPlayer.playSound(nomFichierSon);
				}
				else
				{
					MySoundPlayer.stopSounds();
				}		
			}
		
		});	
		
		MySoundPlayer.addMySoundPlayerListener(new MySoundPlayer.MySoundPlayerAdapter()
		{
		
			@Override
			public void stateChanged(MySoundPlayerEvent evt)
			{
				setSelected((evt.isPlaying) && (nomFichierSon.compareToIgnoreCase(evt.fileName) == 0));
				super.stateChanged(evt);
			}
		
			@Override
			public void errorOccured(MySoundPlayerEvent evt)
			{
				if (nomFichierSon.compareToIgnoreCase(evt.fileName) == 0)
				{
					System.err.println(Messages.getString("JPanelSonBtn.ErrorPlayingSound")+" : \""+evt.fileName+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					setSelected((evt.isPlaying) && (nomFichierSon.compareToIgnoreCase(evt.fileName) == 0));
				}
				super.errorOccured(evt);
			}
		
		});
		
		if (play) MySoundPlayer.playSound(nomFichierSon);
	}
}
