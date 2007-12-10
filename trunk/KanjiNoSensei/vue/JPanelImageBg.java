package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class JPanelImageBg extends JComponent
{
	private static final long	serialVersionUID	= 1L;

	private int					mode;

	private TexturePaint		texture;

	private BufferedImage		bufferedImage;

	public static final int		CENTRE				= 0;

	public static final int		TEXTURE				= 1;
	
	public static final int		NONE				= 2;

	public JPanelImageBg(String fileName, int mode)
	{
		this.mode = mode;
		try
		{
			this.bufferedImage = this.toBufferedImage(Toolkit.getDefaultToolkit().getImage(fileName));
			this.texture = new TexturePaint(bufferedImage, new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage
					.getHeight()));
		}
		catch (Exception e)
		{
			this.mode = NONE;
			System.err.println("Erreur de chargement du fichier image '"+fileName+"' en mode "+mode);
			//e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g)
	{
		switch (mode)
		{
			case TEXTURE:
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(texture);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				break;
			case CENTRE:
				g.setColor(this.getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(bufferedImage, (getWidth() - bufferedImage.getWidth()) / 2, (getHeight() - bufferedImage
						.getHeight()) / 2, null);
				break;
			case NONE:
			default:
				super.paintComponents(g);
		}
	}

	private BufferedImage toBufferedImage(Image image)
	{
		image = new ImageIcon(image).getImage();

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.createGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	public Dimension getImageDimension()
	{
		return new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
	}

}
