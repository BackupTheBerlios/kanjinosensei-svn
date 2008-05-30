/**
 * @author Escallier Pierre
 * @file MyAutoResizingText.java
 * @date 29 mai 08
 */
package utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Timer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * 
 */
public class MyAutoResizingText<T extends JComponent>
{
	final T						component;

	final MyAutoResizingText<T>	thisObject	= this;

	long						lastCall	= 0;

	Timer						timer		= new Timer();

	final float MIN_HEIGHT;
	final float MAX_HEIGHT;

	public T getJComponent()
	{
		return component;
	}
	
	/**
	 * 
	 */
	public MyAutoResizingText(T textComp)
	{
		component = textComp;
		MIN_HEIGHT = 9;
		MAX_HEIGHT = Float.POSITIVE_INFINITY;
		Init();
	}
	
	/**
	 * 
	 */
	public MyAutoResizingText(T textComp, float min)
	{
		component = textComp;
		MIN_HEIGHT = min;
		MAX_HEIGHT = Float.POSITIVE_INFINITY;
		Init();
	}
	
	/**
	 * 
	 */
	public MyAutoResizingText(T textComp, float min, float max)
	{
		component = textComp;
		MIN_HEIGHT = min;
		MAX_HEIGHT = max;
		Init();
	}

	private void Init()
	{
		component.addComponentListener(new ComponentAdapter()
		{

			@Override
			public void componentResized(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				System.out.println("["+component.getClass().getName()+"] componentResized"); //$NON-NLS-1$ //$NON-NLS-2$
				super.componentResized(e);
				refreshSize();
			}

		});

		if (JTextComponent.class.isInstance(component))
		{
			JTextComponent textComponent = (JTextComponent) component;
			textComponent.getDocument().addDocumentListener(new DocumentListener()
			{

				@Override
				public void removeUpdate(DocumentEvent e)
				{
					// TODO Auto-generated method stub
					System.out.println("remove: " + e); //$NON-NLS-1$
					refreshSize();
				}

				@Override
				public void insertUpdate(DocumentEvent e)
				{
					// TODO Auto-generated method stub
					System.out.println("insert: " + e); //$NON-NLS-1$
					refreshSize();
				}

				@Override
				public void changedUpdate(DocumentEvent e)
				{
					// TODO Auto-generated method stub
					// System.out.println("update: "+e);
					// refreshSize();
				}

			});
		}

		refreshSize();
	}

	private int	loopWatchDog	= 0;

	public String getText()
	{
		Method getText = null;
		try
		{
			getText = component.getClass().getMethod("getText"); //$NON-NLS-1$
			return (String) getText.invoke(component);
		}
		catch(Exception e)
		{
			System.err.println(Messages.getString("MyAutoResizingText.NoGetTextMethod")); //$NON-NLS-1$
			return null;
		}
	}
	
	public void setText(String text)
	{
		Method setText = null;
		try
		{
			setText = component.getClass().getMethod("setText", String.class); //$NON-NLS-1$
			setText.invoke(component, text);
		}
		catch(Exception e)
		{
			System.err.println(Messages.getString("MyAutoResizingText.NoSetTextMethod")); //$NON-NLS-1$
			return;
		}
	}
	
	protected int getHeight()
	{
		return component.getHeight();
	}
	
	protected int getWidth()
	{
		return component.getWidth();
	}
	
	public void refreshSize()
	{
		/*
		timer.cancel();
		timer.purge();
		
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
		
			@Override
			public void run()
			{
				timer.purge();
		*/
				
				// TODO Auto-generated method stub
				long diff = System.currentTimeMillis() - lastCall;
				if (diff < 1000)
				{
					++loopWatchDog;
				}
				if (loopWatchDog > 10)
				{
					loopWatchDog = 0;
					return;
				}
				
				String text = getText();
				if (text == null) return;
				
				int rightMargin = 30;
				int topMargin = 3;
				
				float boxHeight = getHeight() - topMargin;
				float boxWidth = getWidth() - rightMargin;
				
				float min = MIN_HEIGHT;
				float max = Math.min(MAX_HEIGHT, boxHeight);
				
				Graphics2D g = (Graphics2D) component.getGraphics();

				if (max == 0 || g == null) return;

				Font font = component.getFont();
				Rectangle2D rect = null;

				float height = min;
				do
				{
					font = font.deriveFont(height);

					rect = font.getStringBounds(text, g.getFontRenderContext());

					// Too big
					if ((rect.getWidth() >= boxWidth) || (rect.getHeight() >= boxHeight))
					{
						max = height;
						height -= (max - min) / 2;
					}
					else
					{
						min = height;
						height += (max - min) / 2;
					}

				} while((height > min) && (height < max));

				final Font finalFont = font.deriveFont(min);
				rect = font.getStringBounds(text, g.getFontRenderContext());
				System.out.println("["+component.getClass().getCanonicalName()+"] OK pour "+rect.getWidth()+";"+rect.getHeight()+" < "+boxWidth+";"+boxHeight); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				// jTextPane1.setFont(finalFont);

				lastCall = System.currentTimeMillis();
				
				component.setFont(finalFont);
				MyUtils.refreshComponent(component);
				
/*				
			}		
		}, 200);
*/		
	}

	public static void main(String[] args)
	{
		final JFrame frame = new JFrame();
		BorderLayout borderLayout = new BorderLayout();
		frame.setLayout(borderLayout);

		frame.setPreferredSize(new Dimension(200, 100));

		final JScrollPane jScrollPanel = new JScrollPane();
		final JPanel jPanel = new JPanel(new BorderLayout());
		
		frame.addComponentListener(new ComponentAdapter()
		{
		
			@Override
			public void componentResized(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				super.componentResized(e);
				jPanel.setSize(jScrollPanel.getViewportBorderBounds().getSize());
				//jScrollPanel.setSize(frame.getSize());
				System.out.println("frame resized"); //$NON-NLS-1$
			}
		
		});
		
		final MyAutoResizingText<JLabel> jAutoResizingJLabel = new MyAutoResizingText<JLabel>(new JLabel(), 20, 30);
		final MyAutoResizingText<JTextPane> jAutoResizingJTextPane = new MyAutoResizingText<JTextPane>(new JTextPane());
		final MyAutoResizingText<JTextField> jAutoResizingJTextField = new MyAutoResizingText<JTextField>(new JTextField());
		final MyAutoResizingText<JTextArea> jAutoResizingJTextArea = new MyAutoResizingText<JTextArea>(new JTextArea());
	
		final Vector<MyAutoResizingText<? extends JComponent>> myTexts = new Vector<MyAutoResizingText<? extends JComponent>>();
		myTexts.add(jAutoResizingJLabel);
		myTexts.add(jAutoResizingJTextArea);
		myTexts.add(jAutoResizingJTextField);
		myTexts.add(jAutoResizingJTextPane);
		
		//jPanel.add(jAutoResizingJLabel.getJComponent(), BorderLayout.NORTH);
		jPanel.add(jAutoResizingJTextPane.getJComponent(), BorderLayout.CENTER);
		// jTextPhrase.setText("猫はとてもかわい です\r\nから猫は とてもかわ いですから"); //$NON-NLS-1$

		jScrollPanel.setViewportView(jPanel);

		frame.add(jScrollPanel, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();

		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				Iterator<MyAutoResizingText<? extends JComponent>> it = myTexts.iterator();
				while(it.hasNext())
				{
					MyAutoResizingText<? extends JComponent> current = it.next();
					current.setText("猫はとてもかわい です;"+current.getJComponent().getClass().getName()); //$NON-NLS-1$
				}
				// jTextPhrase.setText("猫はとてもかわい です\r\nから猫は とてもかわ いですから"); //$NON-NLS-1$
			}

		});

		JButton btnMoreText = new JButton("+Text"); //$NON-NLS-1$
		btnMoreText.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Iterator<MyAutoResizingText<? extends JComponent>> it = myTexts.iterator();
				while(it.hasNext())
				{
					MyAutoResizingText<? extends JComponent> text = it.next();
					text.setText(text.getText()+";"+text.getText()); //$NON-NLS-1$
				}
			}

		});
		
		JButton btnCycle = new JButton("NextComp"); //$NON-NLS-1$
		btnCycle.addActionListener(new ActionListener()
		{
			private int current = 0;
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (++current >= myTexts.size()) {current=0;}
				
				jPanel.removeAll();
				jPanel.add(myTexts.get(current).getJComponent(), BorderLayout.CENTER);
				MyUtils.refreshComponentAndSubs(jPanel);
			}
		
		});

		JPanel jPanelBtns = new JPanel(new FlowLayout());
		jPanelBtns.add(btnCycle);
		jPanelBtns.add(btnMoreText);
		frame.add(jPanelBtns, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
}
