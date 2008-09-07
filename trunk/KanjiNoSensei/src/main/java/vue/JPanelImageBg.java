package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.logging.Level;

import javax.swing.JComponent;

import metier.Messages;
import utils.MyUtils;

public class JPanelImageBg extends JComponent
{
	public static class ImageLoadingException extends Exception
	{

		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

		public ImageLoadingException(String msg)
		{
			super(msg);
		}
	}

	private static final long	serialVersionUID	= 1L;

	private eImageDisplayMode	mode;

	private String				fileName			= null;
	private TexturePaint		texture				= null;

	private BufferedImage		bufferedImage		= null;
	private Image				sourceImage			= null;

	public static enum eImageDisplayMode
	{
		CENTRE, TEXTURE, NONE
	}

	public JPanelImageBg(final String fileName, final eImageDisplayMode mode) throws ImageLoadingException
	{
		MyUtils.trace(Level.FINEST, "[3.1] JPanelImageBg creation"); //$NON-NLS-1$

		this.mode = mode;
		this.fileName = fileName;
		final JPanelImageBg thisPanel = this;

		bufferedImage = getBufferedImage();
		
		this.setMinimumSize(this.getImageDimension());
		this.setPreferredSize(this.getImageDimension());
		this.setSize(this.getImageDimension());
		
		repaint();
		/*
		catch (ImageLoadingException e1)
		{
			MyUtils.trace(Level.SEVERE, Messages.getString("JPanelImageBg.ErrorLoadingImageFile") + " : \""+fileName + "\"\t(" + mode + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			thisPanel.mode = eImageDisplayMode.NONE;
			// return;
		}
		*/
		
		/*
		this.addComponentListener(new ComponentAdapter()
		{

			@Override
			public void componentShown(ComponentEvent e)
			{
				MyUtils.trace("[5] JPanelImageBg < componentShown"); //$NON-NLS-1$

				
				super.componentShown(e);
			}

		});
		*/
	}

	public void paintComponent(Graphics g)
	{
		// <NoJigloo>
		MyUtils.trace(Level.FINEST, "[6] JPanelImageBg.paintComponent"); //$NON-NLS-1$

		switch (this.mode)
		{
			case TEXTURE:
				if (texture == null) break;
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(texture);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				break;
			case CENTRE:
				if (bufferedImage == null) break;
				g.setColor(this.getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(bufferedImage, (getWidth() - bufferedImage.getWidth()) / 2, (getHeight() - bufferedImage
						.getHeight()) / 2, this);
				break;
		}
		// </NoJigloo>

		super.paintComponents(g);
	}

	synchronized private BufferedImage getBufferedImage() throws ImageLoadingException
	{
		// <NoJigloo>
		if (bufferedImage == null)
		{
			MyUtils.trace(Level.FINEST, "[5.1] bufferedImage creation"); //$NON-NLS-1$

			sourceImage = Toolkit.getDefaultToolkit().getImage(fileName);

			// while(!Toolkit.getDefaultToolkit().prepareImage(sourceImage, -1,
			// -1, this)) {}
			Toolkit.getDefaultToolkit().prepareImage(sourceImage, -1, -1, this);
			int status = 0, prevStatus = 0;
			boolean complete, aborted, error;
			int cpt = 1;
			do
			{
				status = Toolkit.getDefaultToolkit().checkImage(sourceImage, -1, -1, this);
				complete = ((status & ImageObserver.ALLBITS) > 0);
				aborted = ((status & ImageObserver.ABORT) > 0);
				error = ((status & ImageObserver.ERROR) > 0);

				if (status != prevStatus)
				{
					prevStatus = status;
				}

				if ( !complete)
				{
					++cpt;
				}
				if (aborted || error)
				{
					mode = eImageDisplayMode.NONE;
					throw new ImageLoadingException(Messages.getString("JPanelImageBg.ExceptionLoadingImage") + " : \""+sourceImage.toString()+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}

			} while ( !complete);

			int width = sourceImage.getWidth(this);
			int height = sourceImage.getHeight(this);
			MyUtils.assertFalse((width <= 10) || (height <= 10), "image dimension is small: (" + width + ";" + height //$NON-NLS-1$ //$NON-NLS-2$
					+ ")"); //$NON-NLS-1$

			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			MyUtils.assertFalse(bufferedImage == null, "toBufferedImage: bufferedImage is null"); //$NON-NLS-1$

			Graphics g = bufferedImage.createGraphics();

			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			g.setColor(Color.black);
			g.drawString("loading...", 5, 5); //$NON-NLS-1$
			boolean result = g.drawImage(sourceImage, 0, 0, this);
			g.dispose();

			MyUtils.assertTrue(result, "toBufferedImage: drawImage returned false"); //$NON-NLS-1$

			texture = new TexturePaint(bufferedImage, new Rectangle(0, 0, width, height));
		}

		this.repaint();
		return bufferedImage;
		// </NoJigloo>
	}

	public Dimension getImageDimension()
	{
		int width, height;
		try
		{
			width = getBufferedImage().getWidth(this);
			height = getBufferedImage().getHeight(this);
		}
		catch (ImageLoadingException e)
		{
			width = height = 0;
		}

		return new Dimension(width, height);
	}

}
