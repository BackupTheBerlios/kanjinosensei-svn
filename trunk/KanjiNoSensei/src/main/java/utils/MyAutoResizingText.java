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

import javax.swing.BorderFactory;
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
public class MyAutoResizingText<T extends JComponent> extends JScrollPane
{
	public static <U extends JComponent> MyAutoResizingText<U> createSafely(Class<U> fromClass)
	{
		return createSafely(fromClass, 9);
	}
	public static <U extends JComponent> MyAutoResizingText<U> createSafely(Class<U> fromClass, float min)
	{
		return createSafely(fromClass, min, Float.POSITIVE_INFINITY);
	}
	public static <U extends JComponent> MyAutoResizingText<U> createSafely(Class<U> fromClass, float min, float max)
	{
		try
		{
			return create(fromClass, min, max);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static <U extends JComponent> MyAutoResizingText<U> create(Class<U> fromClass) throws InstantiationException, IllegalAccessException
	{
		return create(fromClass, 9);
	}

	public static <U extends JComponent> MyAutoResizingText<U> create(Class<U> fromClass, float min) throws InstantiationException, IllegalAccessException
	{
		return create(fromClass, min, Float.POSITIVE_INFINITY);
	}

	public static <U extends JComponent> MyAutoResizingText<U> create(Class<U> fromClass, float min, float max) throws InstantiationException, IllegalAccessException
	{
		U component = fromClass.newInstance();
		return new MyAutoResizingText<U>(component, min, max);
	}

	final T						component;

	final MyAutoResizingText<T>	thisObject	= this;

	long						lastCall	= 0;

	Timer						timer		= new Timer();

	final float					MIN_HEIGHT;

	final float					MAX_HEIGHT;

	public T getJComponent()
	{
		return component;
	}

	/**
	 * 
	 */
	private MyAutoResizingText(T textComp, float min, float max)
	{
		component = textComp;
		MIN_HEIGHT = min;
		MAX_HEIGHT = max;
		
		setAutoscrolls(true);
		
		setViewportView(component);
		setVisible(true);
			
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
		
		
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		
		
		setBorder(BorderFactory.createEmptyBorder());
		component.setBorder(BorderFactory.createEmptyBorder());
		
		/*
		setBackground(Color.blue);
		setBorder(BorderFactory.createLineBorder(Color.blue));
		getHorizontalScrollBar().setBackground(Color.yellow);
		getHorizontalScrollBar().setBorder(BorderFactory.createLineBorder(Color.yellow));
		getVerticalScrollBar().setBackground(Color.green);
		getVerticalScrollBar().setBorder(BorderFactory.createLineBorder(Color.green));
		component.setBackground(Color.red);
		component.setBorder(BorderFactory.createLineBorder(Color.red));
		*/

		addComponentListener(new ComponentAdapter()
		{
			private static final int SCROLLBAR_DEFAULT_SIZE = 10;
			private static final double SCROLLBAR_REDUCED_FACTOR = 0.3;
			private static final int SCROLLBAR_MIN_SIZE = 1;
			
			private boolean reducedHScrollBar = false;
			private boolean reducedVScrollBar = false;
			
			@Override
			public void componentResized(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				System.out.println("MyAutoResizingText scrollbar resized");
				super.componentResized(e);
				
				Dimension size = getSize();
				if ((size.height < SCROLLBAR_DEFAULT_SIZE*3) && (!reducedHScrollBar))
				{
					getHorizontalScrollBar().setPreferredSize(new Dimension(0, Math.max(SCROLLBAR_MIN_SIZE, Double.valueOf(size.height * SCROLLBAR_REDUCED_FACTOR).intValue())));
					reducedHScrollBar = true;
				}
				else if (reducedHScrollBar)
				{
					getHorizontalScrollBar().setPreferredSize(new Dimension(0, SCROLLBAR_DEFAULT_SIZE));
					reducedHScrollBar = false;
				}
				
				if ((size.height < SCROLLBAR_DEFAULT_SIZE*3) && (!reducedVScrollBar))
				{
					getVerticalScrollBar().setPreferredSize(new Dimension(Math.max(SCROLLBAR_MIN_SIZE, Double.valueOf(size.height * SCROLLBAR_REDUCED_FACTOR).intValue()), 0));
					reducedVScrollBar = true;
				}
				else if (reducedVScrollBar)
				{
					getVerticalScrollBar().setPreferredSize(new Dimension(SCROLLBAR_DEFAULT_SIZE, 0));
					reducedVScrollBar = false;
				}
				
				refreshSize();
			}
		
		});
		
		component.addComponentListener(new ComponentAdapter()
		{

			@Override
			public void componentResized(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				System.out.println("[" + component.getClass().getName() + "] componentResized"); //$NON-NLS-1$ //$NON-NLS-2$
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

		//refreshSize();
	}

	private int	loopWatchDog	= 0;

	public float	fontHeight;

	public String getText()
	{
		Method getText = null;
		try
		{
			getText = component.getClass().getMethod("getText"); //$NON-NLS-1$
			return (String) getText.invoke(component);
		}
		catch (Exception e)
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
		catch (Exception e)
		{
			System.err.println(Messages.getString("MyAutoResizingText.NoSetTextMethod for component")); //$NON-NLS-1$
			return;
		}
	}

	public void refreshSize()
	{
		/*
		 * timer.cancel(); timer.purge();
		 * 
		 * timer = new Timer(); timer.schedule(new TimerTask() {
		 * 
		 * @Override public void run() { timer.purge();
		 */

		/*
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
		*/

		String text = getText();
		if (text == null) return;
		
		float lastFontHeight = fontHeight;
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

		rect = font.deriveFont(min).getStringBounds(text, g.getFontRenderContext());
		System.out.println("autoSize min size: "+rect.toString());
		Dimension minDim = new Dimension((int) rect.getWidth()+rightMargin, (int) rect.getHeight()+topMargin);
		
		//getParent().setPreferredSize(minDim);
		//setPreferredSize(minDim);
		//component.setPreferredSize(minDim);
		
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

		} while ((height > min) && (height < max));

		final Font finalFont = font.deriveFont(min);
		fontHeight = min;
		
		rect = font.getStringBounds(text, g.getFontRenderContext());
		System.out.println("[" + component.getClass().getCanonicalName() + "] OK pour " + rect.getWidth() + ";" + rect.getHeight() + " < " + boxWidth + ";" + boxHeight+"\tfontHeight: "+fontHeight); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		// jTextPane1.setFont(finalFont);

		lastCall = System.currentTimeMillis();

		component.setFont(finalFont);
		
		MyUtils.refreshComponentAndSubs(this);
		
		/*
		 * } }, 200);
		 */
	}

	private static MyAutoResizingText<? extends JComponent> currentAR = null;
	public static void main(String[] args)
	{
		final JFrame frame = new JFrame();
		BorderLayout borderLayout = new BorderLayout();
		frame.setLayout(borderLayout);

		frame.setPreferredSize(new Dimension(200, 100));
		
		final JScrollPane jScrollPane = new JScrollPane();
		final JPanel jPanelIntermediaire = new JPanel(new BorderLayout());
		jPanelIntermediaire.setPreferredSize(new Dimension(0, 0));
		
		final Vector<MyAutoResizingText<? extends JComponent>> myTexts = new Vector<MyAutoResizingText<? extends JComponent>>();

		frame.addComponentListener(new ComponentAdapter()
		{

			@Override
			public void componentResized(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				super.componentResized(e);
				System.out.println("frame resized"); //$NON-NLS-1$
				currentAR.setVisible(false);
				jPanelIntermediaire.doLayout();
				currentAR.setVisible(true);
				
				SwingUtilities.invokeLater(new Runnable()
				{
				
					@Override
					public void run()
					{
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("frame: \t\t\t"+frame.getSize());
						System.out.println("scrollPanel: \t\t\t"+jScrollPane.getSize());
						System.out.println("panelIntermediaire: \t\t\t"+jPanelIntermediaire.getSize());
						System.out.println("autoSize: \t\t\t"+currentAR.getSize());
						System.out.println("autoSize.component: \t\t\t"+currentAR.getJComponent().getSize());
					}
				
				});
			}

		});
		
		final MyAutoResizingText<JLabel> jAutoResizingJLabel;
		final MyAutoResizingText<JTextPane> jAutoResizingJTextPane;
		final MyAutoResizingText<JTextField> jAutoResizingJTextField;
		final MyAutoResizingText<JTextArea> jAutoResizingJTextArea;
		
		float min = 30;
		try
		{
			jAutoResizingJLabel = create(JLabel.class, min);
			jAutoResizingJTextPane = create(JTextPane.class, min);
			jAutoResizingJTextField = create(JTextField.class, min);
			jAutoResizingJTextArea = create(JTextArea.class, min);
		}
		catch (Exception e)
		{
			System.err.println("creating Exception: ");
			e.printStackTrace();
			return;
		}

		JTextArea jTextArea = jAutoResizingJTextArea.getJComponent();
		jTextArea.setWrapStyleWord(true);
		
		JTextField jTextField = jAutoResizingJTextField.getJComponent();
		
		
		myTexts.add(jAutoResizingJLabel);
		myTexts.add(jAutoResizingJTextArea);
		myTexts.add(jAutoResizingJTextField);
		myTexts.add(jAutoResizingJTextPane);

		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				Iterator<MyAutoResizingText<? extends JComponent>> it = myTexts.iterator();
				while (it.hasNext())
				{
					MyAutoResizingText<? extends JComponent> current = it.next();
					current.setText("猫はとてもかわい です;" + current.getJComponent().getClass().getName()); //$NON-NLS-1$
				}
			}

		});

		JButton btnMoreText = new JButton("+Text"); //$NON-NLS-1$
		btnMoreText.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Iterator<MyAutoResizingText<? extends JComponent>> it = myTexts.iterator();
				while (it.hasNext())
				{
					MyAutoResizingText<? extends JComponent> text = it.next();
					text.setText(text.getText() + ";" + text.getText()); //$NON-NLS-1$
				}
			}

		});

		JButton btnCycle = new JButton("NextComp"); //$NON-NLS-1$
		btnCycle.addActionListener(new ActionListener()
		{
			private int	current	= 0;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int last = current;
				
				if ( ++current >= myTexts.size())
				{
					current = 0;
				}

				currentAR = myTexts.get(current);
				jPanelIntermediaire.removeAll();
				jPanelIntermediaire.add(myTexts.get(current), BorderLayout.CENTER);
				//jScrollPane.setViewportView(myTexts.get(current));
				
				/*
				frame.remove(myTexts.get(last));
				frame.add(myTexts.get(current), BorderLayout.CENTER);
				*/
				frame.setTitle(myTexts.get(current).getJComponent().getClass().getName());
				
				MyUtils.refreshComponentAndSubs(frame);
			}

		});

		jScrollPane.setViewportView(jPanelIntermediaire);
		//frame.add(myTexts.get(0), BorderLayout.CENTER);
		frame.add(jScrollPane, BorderLayout.CENTER);
		
		JPanel jPanelBtns = new JPanel(new FlowLayout());
		jPanelBtns.add(btnCycle);
		jPanelBtns.add(btnMoreText);
		frame.add(jPanelBtns, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}
}
