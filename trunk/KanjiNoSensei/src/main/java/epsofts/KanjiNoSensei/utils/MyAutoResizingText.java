/**
 * @author Escallier Pierre
 * @file MyAutoResizingText.java
 * @date 29 mai 08
 */
package epsofts.KanjiNoSensei.utils;

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
import java.util.Stack;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
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
 * Generic class that can be applied to any JComponent class, MyAutoResizingText is used to make any JComponent text component resizing automated. MyAutoResizingText extends JScrollPane so you just have to instanciate it with {@link MyAutoResizingText#create(Class, float, float)} or {@link MyAutoResizingText#createSafely(Class, float, float)} static method.
 */
// TODO: Références croisées entre component et this empèche le GarbageCollector de libérer component et this.
public class MyAutoResizingText<T extends JComponent> extends JScrollPane
{
	/** Serialization version. */
	private static final long		serialVersionUID			= 1L;

	/** Specify if the default text size will be set to MIN or MAX. */
	private static final boolean	REFRESH_SIZE_BY_GROWING		= false;

	/** Default scrollbar size (width of VScrollBar, height of HScrollBar). */
	private static final int		SCROLLBAR_DEFAULT_SIZE		= 10;

	/** Scrollbar reducing factor. */
	private static final double		SCROLLBAR_REDUCED_FACTOR	= 0.3;

	/** Scrollbar min size. */
	private static final int		SCROLLBAR_MIN_SIZE			= 1;
	
	private Boolean					hideScrollBars = false;

	/** Inset width margin. */
	private final float				WIDTH_MARGIN_PERCENT;

	/** Inset height margin. */
	private final float				HEIGHT_MARGIN_PERCENT;

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @return new MyAutoResizingText object, or null if any exception was caught.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> createSafely(Class<U> fromClass)
	{
		return createSafely(fromClass, 9);
	}

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @param min
	 *            Minimum text size.
	 * @return new MyAutoResizingText object, or null if any exception was caught.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> createSafely(Class<U> fromClass, float min)
	{
		return createSafely(fromClass, min, Float.POSITIVE_INFINITY);
	}

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @param min
	 *            Minimum text size.
	 * @param max
	 *            Maximum test size.
	 * @return new MyAutoResizingText object, or null if any exception was caught.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> createSafely(Class<U> fromClass, float min, float max)
	{
		return createSafely(fromClass, min, max, 0, 0);
	}

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @param min
	 *            Minimum text size.
	 * @param max
	 *            Maximum test size.
	 * @param widthMargin
	 *            Width margin.
	 * @param heightMargin
	 *            Height margin.
	 * @return new MyAutoResizingText object, or null if any exception was caught.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> createSafely(Class<U> fromClass, float min, float max, float widthMargin, float heightMargin)
	{
		try
		{
			return create(fromClass, min, max, widthMargin, heightMargin);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @return new MyAutoResizingText object.
	 * @throws InstantiationException
	 *             on U instanciation error.
	 * @throws IllegalAccessException
	 *             on U instanciation error.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> create(Class<U> fromClass) throws InstantiationException, IllegalAccessException
	{
		return create(fromClass, 9);
	}

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @param min
	 *            Minimum text size.
	 * @return new MyAutoResizingText object.
	 * @throws InstantiationException
	 *             on U instanciation error.
	 * @throws IllegalAccessException
	 *             on U instanciation error.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> create(Class<U> fromClass, float min) throws InstantiationException, IllegalAccessException
	{
		return create(fromClass, min, Float.POSITIVE_INFINITY);
	}

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @param min
	 *            Minimum text size.
	 * @param max
	 *            Maximum text size.
	 * @return new MyAutoResizingText object.
	 * @throws InstantiationException
	 *             on U instanciation error.
	 * @throws IllegalAccessException
	 *             on U instanciation error.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> create(Class<U> fromClass, float min, float max) throws InstantiationException, IllegalAccessException
	{
		return create(fromClass, min, max, 0, 0);
	}

	/**
	 * Create a new MyAutoResizingText with given class associated component.
	 * 
	 * @param <U>
	 *            fromClass class.
	 * @param fromClass
	 *            Class object from which to instanciate the associated component, must implement a public empty constructor.
	 * @param min
	 *            Minimum text size.
	 * @param max
	 *            Maximum text size.
	 * @param widthMargin
	 *            Width margin.
	 * @param heightMargin
	 *            Height margin.
	 * @return new MyAutoResizingText object.
	 * @throws InstantiationException
	 *             on U instanciation error.
	 * @throws IllegalAccessException
	 *             on U instanciation error.
	 */
	public static <U extends JComponent> MyAutoResizingText<U> create(Class<U> fromClass, float min, float max, float widthMargin, float heightMargin) throws InstantiationException, IllegalAccessException
	{
		U component = fromClass.newInstance();
		return new MyAutoResizingText<U>(component, min, max, widthMargin, heightMargin);
	}

	/** Associated component. */
	final T		component;

	/** Minimum text font height. */
	final float	MIN_HEIGHT;

	/** Maximum text font height. */
	final float	MAX_HEIGHT;

	/**
	 * Return associated component.
	 * 
	 * @return associated component.
	 */
	public T getJComponent()
	{
		return component;
	}

	/**
	 * Private constructor.
	 * 
	 * @param textComp
	 *            Associated component instance.
	 * @param min
	 *            minimum font size.
	 * @param max
	 *            maximum font size.
	 */
	private MyAutoResizingText(T textComp, float min, float max, float widthMargin, float heightMargin)
	{
		component = textComp;
		MIN_HEIGHT = min;
		MAX_HEIGHT = max;
		WIDTH_MARGIN_PERCENT = widthMargin;
		HEIGHT_MARGIN_PERCENT = heightMargin;

		setAutoscrolls(true);
		setViewportView(component);
		setVisible(true);

		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

		setBorder(BorderFactory.createEmptyBorder());
		component.setBorder(BorderFactory.createEmptyBorder());

		// {
		// // Trace useful coloring
		// setBackground(Color.blue);
		// setBorder(BorderFactory.createLineBorder(Color.blue));
		//		
		// viewportPanel.setBackground(Color.gray);
		// viewportPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		//		
		// component.setBackground(Color.red);
		// component.setBorder(BorderFactory.createLineBorder(Color.red));
		//		
		//		
		// getVerticalScrollBar().setBackground(Color.green);
		// getVerticalScrollBar().setBorder(BorderFactory.createLineBorder(Color.green));
		//		
		// getHorizontalScrollBar().setBackground(Color.yellow);
		// getHorizontalScrollBar().setBorder(BorderFactory.createLineBorder(Color.yellow));
		// }

		addComponentListener(new ComponentAdapter()
		{
			/** Flag, mark if HScrollBar was reduced on last call. */
			private boolean	reducedHScrollBar	= false;

			/** Flag, mark if VScrollBar was reduced on last call. */
			private boolean	reducedVScrollBar	= false;

			@Override
			public void componentResized(ComponentEvent e)
			{
				MyUtils.trace(Level.FINE, "MyAutoResizingText scrollbar resized");

				setMinimumSize(getMinDim());
				setPreferredSize(getParent().getParent().getSize());
				doLayout();

				Dimension size = getSize();
				if ((size.height < SCROLLBAR_DEFAULT_SIZE * 3) && ( !reducedHScrollBar))
				{
					getHorizontalScrollBar().setPreferredSize(new Dimension(0, Math.max(SCROLLBAR_MIN_SIZE, Double.valueOf(size.height * SCROLLBAR_REDUCED_FACTOR).intValue())));
					reducedHScrollBar = true;
				}
				else if (reducedHScrollBar)
				{
					getHorizontalScrollBar().setPreferredSize(new Dimension(0, SCROLLBAR_DEFAULT_SIZE));
					reducedHScrollBar = false;
				}

				if ((size.height < SCROLLBAR_DEFAULT_SIZE * 3) && ( !reducedVScrollBar))
				{
					getVerticalScrollBar().setPreferredSize(new Dimension(Math.max(SCROLLBAR_MIN_SIZE, Double.valueOf(size.height * SCROLLBAR_REDUCED_FACTOR).intValue()), 0));
					reducedVScrollBar = true;
				}
				else if (reducedVScrollBar)
				{
					getVerticalScrollBar().setPreferredSize(new Dimension(SCROLLBAR_DEFAULT_SIZE, 0));
					reducedVScrollBar = false;
				}

				if ( !getMinimumSize().equals(getSize()))
				{
					refreshSize();
				}
			}

		});

		component.addComponentListener(new ComponentAdapter()
		{

			@Override
			public void componentResized(ComponentEvent e)
			{
				MyUtils.trace(Level.FINE, "[" + component.getClass().getName() + "] componentResized"); //$NON-NLS-1$ //$NON-NLS-2$

				if ( !getMinimumSize().equals(getSize()))
				{
					refreshSize();
				}
			}

		});

		if (JTextComponent.class.isInstance(component))
		{
			JTextComponent textComponent = (JTextComponent) component;

			textComponent.setFont(textComponent.getFont().deriveFont(REFRESH_SIZE_BY_GROWING ? MIN_HEIGHT : MAX_HEIGHT));

			textComponent.getDocument().addDocumentListener(new DocumentListener()
			{

				@Override
				public void removeUpdate(DocumentEvent e)
				{
					MyUtils.trace(Level.FINEST, "remove: " + e); //$NON-NLS-1$
					refreshSize();
				}

				@Override
				public void insertUpdate(DocumentEvent e)
				{
					MyUtils.trace(Level.FINEST, "insert: " + e); //$NON-NLS-1$
					refreshSize();
				}

				@Override
				public void changedUpdate(DocumentEvent e)
				{
					// Nothing.
				}

			});
		}

	}

	/**
	 * Try to return the associated component getText() value, if no getText() method are available on the associated component, an error is logged but no exception are thrown.
	 * 
	 * @return associated component getText() value, or null if none.
	 */
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
			MyUtils.trace(Level.SEVERE, Messages.getString("MyAutoResizingText.NoGetTextMethod")); //$NON-NLS-1$
			return null;
		}
	}

	/**
	 * Try to call the associated component setText(String) method, if no setText(String) method are available on the associated component, an error is logged but no exception are thrown.
	 * 
	 * @param text
	 *            text to set.
	 */
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
			MyUtils.trace(Level.SEVERE, Messages.getString("MyAutoResizingText.NoSetTextMethod for component")); //$NON-NLS-1$
			return;
		}
	}

	/**
	 * Return the minimum dimension that the associated component need to display the full text (with minimum font height).
	 * 
	 * @return minimum dimension needed to display the associated component full text in minimum font height.
	 */
	private Dimension getMinDim()
	{
		String text = getText();
		if (text == null) return new Dimension(0, 0);

		Graphics2D g = (Graphics2D) component.getGraphics();
		Rectangle2D rect = component.getFont().deriveFont(MIN_HEIGHT).getStringBounds(text, g.getFontRenderContext());
		MyUtils.trace(Level.FINEST, "autoSize min size: " + rect.toString());

		Dimension minDim = new Dimension((int) (rect.getWidth() * (1 + WIDTH_MARGIN_PERCENT)), (int) (rect.getHeight() * (1 + HEIGHT_MARGIN_PERCENT)));
		return minDim;
	}

	/**
	 * Compute the font size depending on current object state. This method is called every time the component size or text is changed.
	 */
	private void refreshSize()
	{
		String text = getText();
		if (text == null) return;

		MyUtils.trace(Level.INFO, "MyAutoResizingText resize (" + text + ")");

		float boxHeight = getHeight() * (1 - HEIGHT_MARGIN_PERCENT);
		float boxWidth = getWidth() * (1 - WIDTH_MARGIN_PERCENT);

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
			if ((rect.getWidth() > boxWidth) || (rect.getHeight() > boxHeight))
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

		if (height <= min)
		{
			MyUtils.trace(Level.INFO, "We should need ScrollBars");
			// setSize(new Dimension((int) rect.getWidth(), (int) rect.getHeight()));
		}

		final Font finalFont = font.deriveFont(min);

		rect = font.getStringBounds(text, g.getFontRenderContext());
		MyUtils.trace(Level.FINEST, "[" + component.getClass().getCanonicalName() + "] OK pour " + rect.getWidth() + ";" + rect.getHeight() + " < " + boxWidth + ";" + boxHeight + "\tfontHeight: " + min); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		component.setFont(finalFont);

		if (HEIGHT_MARGIN_PERCENT < 0 || WIDTH_MARGIN_PERCENT < 0)
		{
			centerScrollBars();
		}
		
		setScrollBarsVisibles(!hideScrollBars);

		MyUtils.refreshComponentAndSubs(this);
		/*
		 * } }, 200);
		 */
	}

	public void setScrollBarsVisibility(boolean visibles)
	{
		hideScrollBars = !visibles;
	}
	
	private void setScrollBarsVisibles(boolean visibles)
	{
		getHorizontalScrollBar().setVisible(visibles);
		getVerticalScrollBar().setVisible(visibles);
		
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	}
	
	public void centerScrollBars()
	{
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		getVerticalScrollBar().setValue((getVerticalScrollBar().getMaximum() + getVerticalScrollBar().getVisibleAmount()) / getVerticalScrollBar().getBlockIncrement());
		getHorizontalScrollBar().setValue((getHorizontalScrollBar().getMaximum() + getHorizontalScrollBar().getVisibleAmount()) / getHorizontalScrollBar().getBlockIncrement());
	}

	public static void main(String[] args)
	{
		final Object currentAR[] = {null};

		final JFrame frame = new JFrame();
		BorderLayout borderLayout = new BorderLayout();
		frame.setLayout(borderLayout);

		frame.setPreferredSize(new Dimension(400, 200));

		// final JScrollPane jScrollPane = new JScrollPane();
		final JPanel jPanelIntermediaire = new JPanel(new BorderLayout());

		final Stack<MyAutoResizingText<? extends JComponent>> myTexts = new Stack<MyAutoResizingText<? extends JComponent>>();

		frame.addComponentListener(new ComponentAdapter()
		{

			@SuppressWarnings("unchecked")
			@Override
			public void componentResized(ComponentEvent e)
			{
				final MyAutoResizingText<JComponent> current = ((MyAutoResizingText<JComponent>) currentAR[0]);

				MyUtils.trace(Level.FINEST, "frame resized"); //$NON-NLS-1$

				if (current == null) return;
				current.setVisible(false);
				jPanelIntermediaire.doLayout();
				current.setVisible(true);

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
							e.printStackTrace();
						}
						MyUtils.trace(Level.FINEST, "frame: \t\t\t" + frame.getSize());
						// MyUtils.trace(Level.FINEST, "scrollPanel: \t\t\t"+jScrollPane.getSize());
						MyUtils.trace(Level.FINEST, "panelIntermediaire: \t\t\t" + jPanelIntermediaire.getSize());
						MyUtils.trace(Level.FINEST, "autoSize: \t\t\t" + current.getSize());
						MyUtils.trace(Level.FINEST, "autoSize.component: \t\t\t" + current.getJComponent().getSize());
					}

				});
			}

		});

		final MyAutoResizingText<JLabel> jAutoResizingJLabelOneChar;
		final MyAutoResizingText<JLabel> jAutoResizingJLabel;
		final MyAutoResizingText<JTextPane> jAutoResizingJTextPane;
		final MyAutoResizingText<JTextField> jAutoResizingJTextField;
		final MyAutoResizingText<JTextArea> jAutoResizingJTextArea;
		final MyAutoResizingText<JEditorPane> jAutoResizingJEditorPane;

		float min = 10;
		try
		{
			jAutoResizingJLabelOneChar = create(JLabel.class, min, Float.POSITIVE_INFINITY, 0, (float) -0.5);
			jAutoResizingJLabelOneChar.setScrollBarsVisibility(false);
			
			jAutoResizingJLabel = create(JLabel.class, min);
			jAutoResizingJTextPane = create(JTextPane.class, min);
			jAutoResizingJTextField = create(JTextField.class, min);
			jAutoResizingJTextArea = create(JTextArea.class, min);
			
			jAutoResizingJEditorPane = create(JEditorPane.class, min);
			JEditorPane editorPane = jAutoResizingJEditorPane.getJComponent();
			editorPane.setContentType("text/html");
			editorPane.setText("<h1>Titre 1</h1><br>Deuxième ligne</br>");
		}
		catch (Exception e)
		{
			MyUtils.trace(Level.SEVERE, "MyAutoResizingText creating Exception: ");
			e.printStackTrace();
			return;
		}

		JTextArea jTextArea = jAutoResizingJTextArea.getJComponent();
		jTextArea.setWrapStyleWord(true);

		// JTextField jTextField = jAutoResizingJTextField.getJComponent(); does not extends JComponent

		myTexts.add(jAutoResizingJLabelOneChar);
		myTexts.add(jAutoResizingJEditorPane);
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
				it.next().setText("あ");
				while (it.hasNext())
				{
					MyAutoResizingText<? extends JComponent> current = it.next();

					current.setText("猫はとてもかわい です;" + current.getJComponent().getClass().getName()); //$NON-NLS-1$
				}
			}

		});

		JButton btnRefreshSize = new JButton("refreshSize");
		btnRefreshSize.addActionListener(new ActionListener()
		{

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				final MyAutoResizingText<JComponent> current = (MyAutoResizingText<JComponent>) currentAR[0];
				current.refreshSize();
				MyUtils.refreshComponentAndSubs(frame);

				MyUtils.InvokeLaterEDT(new Runnable()
				{

					@Override
					public void run()
					{
						// current.getViewport().scrollRectToVisible(new Rectangle(current.getSize()));
						// current.getVerticalScrollBar().setValueIsAdjusting(true);
						// current.getHorizontalScrollBar().setValueIsAdjusting(true);

						MyUtils.trace(Level.INFO, "H: value: " + current.getHorizontalScrollBar().getValue() + "; blockInc: " + current.getHorizontalScrollBar().getBlockIncrement());
						MyUtils.trace(Level.INFO, "V: " + current.getVerticalScrollBar().getValue() + "; blockInc: " + current.getVerticalScrollBar().getBlockIncrement());

						MyUtils.trace(Level.INFO, "H Max: " + current.getHorizontalScrollBar().getMaximum() + "; Min: " + current.getHorizontalScrollBar().getMinimum() + "; Vis: " + current.getHorizontalScrollBar().getVisibleAmount());
						MyUtils.trace(Level.INFO, "V Max: " + current.getVerticalScrollBar().getMaximum() + "; Min: " + current.getVerticalScrollBar().getMinimum() + "; Vis: " + current.getVerticalScrollBar().getVisibleAmount());

						current.getVerticalScrollBar().setValue((current.getVerticalScrollBar().getMaximum() + current.getVerticalScrollBar().getVisibleAmount()) / current.getVerticalScrollBar().getBlockIncrement());

						MyUtils.trace(Level.INFO, "H: " + current.getHorizontalScrollBar().getValue());
						MyUtils.trace(Level.INFO, "V: " + current.getVerticalScrollBar().getValue());
					}
				});
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
			private Iterator<MyAutoResizingText<? extends JComponent>>	it	= myTexts.iterator();

			@Override
			public void actionPerformed(ActionEvent e)
			{
				MyAutoResizingText<? extends JComponent> current = null;

				if ( !it.hasNext())
				{
					it = myTexts.iterator();
				}

				current = it.next();
				currentAR[0] = current;

				jPanelIntermediaire.removeAll();
				jPanelIntermediaire.add(current, BorderLayout.CENTER);
				// jScrollPane.setViewportView(myTexts.get(current));

				/*
				 * frame.remove(myTexts.get(last)); frame.add(myTexts.get(current), BorderLayout.CENTER);
				 */
				frame.setTitle(current.getJComponent().getClass().getName());

				MyUtils.refreshComponentAndSubs(frame);
			}

		});

		// jScrollPane.setViewportView(jPanelIntermediaire);
		// frame.add(myTexts.get(0), BorderLayout.CENTER);
		frame.add(jPanelIntermediaire, BorderLayout.CENTER);

		JPanel jPanelBtns = new JPanel(new FlowLayout());
		jPanelBtns.add(btnCycle);
		jPanelBtns.add(btnMoreText);
		jPanelBtns.add(btnRefreshSize);
		frame.add(jPanelBtns, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}
}
