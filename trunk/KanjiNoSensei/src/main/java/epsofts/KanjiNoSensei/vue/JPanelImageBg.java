package epsofts.KanjiNoSensei.vue;

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
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.utils.MyUtils;

/**
 * That class is used to display image with Swing GUI.
 */
public class JPanelImageBg extends JComponent implements ImageObserver
{
	/**
	 * Exception class, thrown when Image can't be load.
	 */
	public static class ImageLoadingException extends Exception
	{

		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

		/**
		 * Constructor, can define a message.
		 * 
		 * @param msg
		 *            Message.
		 */
		public ImageLoadingException(String msg)
		{
			super(msg);
		}
	}

	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;

	/** Image display mode. */
	//private eImageDisplayMode	mode;

	/** Image filename. */
	private String				fileName			= null;

	/** Texture. */
	// private TexturePaint texture = null;
	/** Buffered image. */
	private BufferedImage		bufferedImage		= null;

	/** Image. */
	private Image				sourceImage			= null;

	/**
	 * Display mode: CENTER, paint the loaded image. TEXTURE, paint the loaded texture. NONE, paint nothing.
	 */
	/*
	 * public static enum eImageDisplayMode { CENTER, TEXTURE, NONE }
	 */

	/**
	 * Constructor, define image filename and display mode.
	 * 
	 * @param fileName
	 *            Image filename.
	 * @param mode
	 *            Display mode (CENTER, TEXTURE or NONE)
	 * @throws ImageLoadingException
	 *             if image can't be loaded.
	 */
	public JPanelImageBg(final String fileName) throws ImageLoadingException
	{
		MyUtils.trace(Level.FINEST, "[3.1] JPanelImageBg creation"); //$NON-NLS-1$

		this.fileName = fileName;

		this.bufferedImage = getBufferedImage();

		this.setMinimumSize(this.getImageDimension());
		this.setPreferredSize(this.getImageDimension());
		this.setSize(this.getImageDimension());
	}

	/**
	 * Component paint method, paint the image depending on the current display mode.
	 * 
	 * @param g
	 *            Given by EDT.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// <NoJigloo>
		MyUtils.trace(Level.FINEST, "[6] JPanelImageBg.paintComponent"); //$NON-NLS-1$

		if (bufferedImage == null) return;
		//g.setColor(this.getBackground());
		//g.fillRect(0, 0, getWidth(), getHeight());
		//g.drawImage(bufferedImage, (getWidth() - bufferedImage.getWidth()) / 2, (getHeight() - bufferedImage.getHeight()) / 2, this);
		g.drawImage(bufferedImage, 0, 0, this);
		// </NoJigloo>
	}

	/**
	 * On the first call, the image, bufferedImage and texture is loaded from filename. Next call just return the previous loaded one.
	 * 
	 * @return BufferedImage.
	 * @throws ImageLoadingException
	 *             on image loading error.
	 */
	synchronized private BufferedImage getBufferedImage() throws ImageLoadingException
	{
		// <NoJigloo>
		if (bufferedImage == null)
		{
			MyUtils.trace(Level.FINEST, "[5.1] bufferedImage creation"); //$NON-NLS-1$

			try
			{
				bufferedImage = ImageIO.read(new File(fileName));
			}
			catch (IOException e)
			{
				throw new ImageLoadingException(e.getMessage());
			}
			
			/*
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
					throw new ImageLoadingException(Messages.getString("JPanelImageBg.ExceptionLoadingImage") + " : \"" + sourceImage.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			g.setColor(Color.yellow);
			g.drawString("loading...", 5, 5); //$NON-NLS-1$
			boolean result = g.drawImage(sourceImage, 0, 0, this);
			g.dispose();

			MyUtils.assertTrue(result, "toBufferedImage: drawImage returned false"); //$NON-NLS-1$

			texture = new TexturePaint(bufferedImage, new Rectangle(0, 0, width, height));
			*/
		}

		return bufferedImage;
		// </NoJigloo>
	}

	/**
	 * Return the loaded image dimension.
	 * 
	 * @return the loaded image dimension.
	 */
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
