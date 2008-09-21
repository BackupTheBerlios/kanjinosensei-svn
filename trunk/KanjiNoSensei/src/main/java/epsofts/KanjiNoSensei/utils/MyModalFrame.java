/**
 * @author Escallier Pierre
 * @file MyModalFrame.java
 * @date 17 sept. 2008
 */
package epsofts.KanjiNoSensei.utils;

import java.awt.Window;

import nl.jj.swingx.gui.modal.JModalFrame;

/**
 * Class that represent a Modal Frame, able to block parent frame access while this is showing.
 */
public class MyModalFrame extends JModalFrame
{

	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;

	/*
	private static final Component getFirstComponent(Window window)
	{
		if (window == null) return null;

		if (window.getComponentCount() <= 0)
		{
			JButton jButton = new JButton("FAKE"); //$NON-NLS-1$
			jButton.setVisible(true);
			window.add(jButton, BorderLayout.CENTER);
		}

		return window.getComponent(0);
	}
	*/

	/**
	 * Constructor
	 * @param owner Owner window.
	 * @param modal Is this frame a modal frame.
	 */
	public MyModalFrame(Window owner, boolean modal)
	{
		super(owner, modal);
		//super(owner, getFirstComponent(owner), modal);
	}

}
