/**
 * 
 */
package epsofts.KanjiNoSensei.vue.kanji;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import epsofts.KanjiNoSensei.metier.Dictionary;
import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.MyUtils.BadStringFormatException;
import epsofts.KanjiNoSensei.vue.Config;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;
import epsofts.KanjiNoSensei.vue.VueElement;
import epsofts.KanjiNoSensei.vue.VueElement.QuizQuestionPanel;
import epsofts.KanjiNoSensei.vue.VueElement.QuizSolutionPanel;
import epsofts.KanjiNoSensei.vue.VueElement.VueDetaillePanel;

/**
 * @author Axan
 * 
 */
class KanjiVueDetaillePanel extends JPanel implements VueDetaillePanel, QuizQuestionPanel, QuizSolutionPanel
{

	private static final long	serialVersionUID				= 1L;

	private static final Font	DEFAULT_FONT_KOCHIMINCHO_MAX	= new Font("Kochi Mincho", Font.PLAIN, (int) VueKanji.FONT_MAX_SIZE);

	private static final int	INFOS_PANEL_H_MARGIN			= 20;

	private static final int	INFOS_PANEL_V_MARGIN			= 5;

	private static final String	DEFAULT_TEMPLATE				= "<label>KanjiVueDetaillePanel.LabelONLectures</label> : <b><vue>getLecturesON</vue></b><br>" + "<label>KanjiVueDetaillePanel.LabelKUNLectures</label> : <b><vue>getLecturesKUN</vue></b><br>" + "<label>KanjiVueDetaillePanel.LabelSignifications</label> : <b><method>getSignifications</method></b><br>" + "<label>KanjiVueDetaillePanel.LabelThemes</label> : <b><method>getThemes</method></b><br>" + "<label>KanjiVueDetaillePanel.LabelMotsExemples</label> : <b><vue>getMotsExemples</vue></b><br>";

	private VueKanji			vue								= null;

	private JPanel				jPanelCodeUTF8					= null;

	private JScrollPane			jScrollPaneInfos				= null;

	private JLabel				jLabelCodeUTF8					= null;

	private JEditorPane			jInfosEditorPane;

	private JPanel				jPanelInfos						= null;

	private JDialog				ImgTraceDialog					= null;

	private final MouseAdapter	vueDetaillePanelMouseAdapter	= new MouseAdapter()
																{

																	@Override
																	public void mouseClicked(MouseEvent e)
																	{
																		KanjiVueDetaillePanel.this.dispatchEvent(e);
																		super.mouseClicked(e);
																	}

																};

	public KanjiVueDetaillePanel(VueKanji vue)
	{
		super();
		this.vue = vue;
		initialize();
		// <NoJigloo>
		dynamicInitialize();
		// </NoJigloo>
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		// this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setMinimumSize(new java.awt.Dimension(400, 100));
		this.setPreferredSize(new java.awt.Dimension(400, 100));
		this.setAlignmentX(0.0f);
		this.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 100));
		this.add(getJPanelCodeUTF8(), BorderLayout.WEST);
		this.add(getJScrollPaneInfos(), BorderLayout.CENTER);
	}

	// <NoJigloo>
	private void dynamicInitialize()
	{
		miseAJourInfos();
	}

	protected void setSizes()
	{
		getJPanelCodeUTF8().setMinimumSize(getJPanelCodeUTF8().getPreferredSize());
		getJPanelCodeUTF8().setMaximumSize(getJPanelCodeUTF8().getPreferredSize());
		getJInfosEditorPane().setMinimumSize(getJInfosEditorPane().getPreferredSize());
		getJInfosEditorPane().setMaximumSize(getJInfosEditorPane().getPreferredScrollableViewportSize());
		/*
		 * getJPanelInfos().setMinimumSize(getJPanelInfos().getPreferredSize()); getJPanelInfos().setMaximumSize(getJPanelInfos().getPreferredSize());
		 */

		int minX = getJInfosEditorPane().getPreferredSize().width + getJPanelCodeUTF8().getPreferredSize().width + getJScrollPaneInfos().getVerticalScrollBar().getMaximumSize().width + INFOS_PANEL_H_MARGIN;
		// int minX = getJPanelInfos().getPreferredSize().width + getJPanelCodeUTF8().getPreferredSize().width + getJScrollPaneInfos().getVerticalScrollBar().getMaximumSize().width + INFOS_PANEL_H_MARGIN;
		int minY = 0 + Math.max(getJInfosEditorPane().getPreferredSize().height, getJPanelCodeUTF8().getPreferredSize().height) + getJScrollPaneInfos().getHorizontalScrollBar().getMaximumSize().height + INFOS_PANEL_V_MARGIN * 2;

		int maxX = minX;
		int maxY = minY;
		int prefX = minX, prefY = minY;

		setMinimumSize(new Dimension(minX, minY));
		setMaximumSize(new Dimension(maxX, maxY));
		setPreferredSize(new Dimension(prefX, prefY));
	}

	protected void miseAJourInfos()
	{
		jLabelCodeUTF8.setText(vue.getKanji().getCodeUTF8().toString());
		getJInfosEditorPane().setText(VueElement.computeTemplate(Config.getString("KanjiVueDetaillePanel.Template", DEFAULT_TEMPLATE), vue));

		doLayout();
		setSizes();
	}

	// </NoJigloo>

	/**
	 * This method initializes jPanelCodeUTF8
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelCodeUTF8()
	{
		if (jPanelCodeUTF8 == null)
		{
			// MyAutoResizingText<JLabel> jAutoSizeLabelCodeUTF8 = MyAutoResizingText.createSafely(JLabel.class, VueKanji.FONT_MIN_SIZE, Float.POSITIVE_INFINITY);
			// jLabelCodeUTF8 = jAutoSizeLabelCodeUTF8.getJComponent();
			jLabelCodeUTF8 = new JLabel();
			jLabelCodeUTF8.setText("猫"); //$NON-NLS-1$
			jLabelCodeUTF8.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelCodeUTF8.setFont(DEFAULT_FONT_KOCHIMINCHO_MAX); //$NON-NLS-1$
			jLabelCodeUTF8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jLabelCodeUTF8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			jPanelCodeUTF8 = new JPanel();
			jPanelCodeUTF8.setLayout(new BorderLayout());
			jPanelCodeUTF8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

			// jPanelCodeUTF8.setPreferredSize(new Dimension(100, 0));

			jLabelCodeUTF8.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					MyUtils.trace(Level.FINEST, "jLabelCodeUTF8.mouseClicked"); //$NON-NLS-1$
					synchronized (jLabelCodeUTF8)
					{
						if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
						{
							ImgTraceDialog = null; // Force recreate the ImgTraceDialog
							JDialog imageTraceDialog = getImgTraceDialog();
							// imageTraceDialog.pack();
							Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
							Point loc;
							if ((imageTraceDialog.getPreferredSize().width >= (screen.width / 2)) || (imageTraceDialog.getPreferredSize().height >= (screen.height / 2)))
							{
								loc = new Point(0, 0);
							}
							else
							{
								loc = new Point(screen.width / 4, screen.height / 4);
							}

							/*
							 * Point loc = e.getLocationOnScreen(); loc.translate(20, 20);
							 */
							imageTraceDialog.setLocation(loc);
							MyUtils.trace(Level.FINEST, "jLabelCodeUTF8 double clicked should display imageTraceDialog"); //$NON-NLS-1$

							imageTraceDialog.setVisible(true);
						}
						else
						{
							vueDetaillePanelMouseAdapter.mouseClicked(e);
						}
					}
				}
			});

			jPanelCodeUTF8.add(jLabelCodeUTF8, BorderLayout.CENTER);
		}
		return jPanelCodeUTF8;
	}

	/**
	 * This method initializes jScrollPaneInfos
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneInfos()
	{
		if (jScrollPaneInfos == null)
		{
			jScrollPaneInfos = new JScrollPane();
			jScrollPaneInfos.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jScrollPaneInfos.setBorder(BorderFactory.createEmptyBorder(INFOS_PANEL_V_MARGIN, INFOS_PANEL_H_MARGIN, INFOS_PANEL_V_MARGIN, 0));
			jScrollPaneInfos.setViewportView(getJInfosEditorPane());
			jScrollPaneInfos.setAutoscrolls(true);
			jScrollPaneInfos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPaneInfos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			/*
			 * jScrollPaneInfos.getHorizontalScrollBar().setPreferredSize(new java.awt.Dimension(0, 16)); jScrollPaneInfos.getHorizontalScrollBar().setMinimumSize(new java.awt.Dimension(0, 16));
			 */
			jScrollPaneInfos.addMouseListener(vueDetaillePanelMouseAdapter);

		}
		return jScrollPaneInfos;
	}

	/**
	 * This method initializes jPanelInfos
	 * 
	 * @return javax.swing.JPanel
	 */
	/*
	 * private JPanel getJPanelInfos() { if (jPanelInfos == null) { jPanelInfos = new JPanel(); BorderLayout jPanelInfosLayout = new BorderLayout(); jPanelInfos.setLayout(jPanelInfosLayout); jPanelInfos.addMouseListener(vueDetaillePanelMouseAdapter); jPanelInfos.add(getJInfosEditorPane(), BorderLayout.CENTER); } return jPanelInfos; }
	 */

	/**
	 * This method initializes ImgTraceDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JDialog getImgTraceDialog()
	{
		if (ImgTraceDialog == null)
		{
			MyUtils.trace(Level.FINEST, "[1] ImgTraceFrame creation"); //$NON-NLS-1$
			// ImgTraceDialog = new MyModalFrame(KanjiNoSensei.getApp().getJFrame(), true);
			ImgTraceDialog = new JDialog();
			ImgTraceDialog.setTitle(Messages.getString("KanjiVueDetaillePanel.LabelStrokesOrder")); //$NON-NLS-1$
			ImgTraceDialog.setModal(true);
			ImgTraceDialog.setContentPane(vue.getStrokeOrdersPanel());
			ImgTraceDialog.setUndecorated(false);
			ImgTraceDialog.setBackground(Color.black);

			Dimension d = vue.getStrokeOrdersPanel().getPreferredSize();
			Dimension nd;

			if ((d.height == 0) && (d.width == 0))
			{
				nd = Toolkit.getDefaultToolkit().getScreenSize();
				// nd = new Dimension(nd.width / 2, nd.height / 2);

				// ImgTraceDialog.setMinimumSize(nd);
				ImgTraceDialog.setPreferredSize(nd);
				ImgTraceDialog.setSize(ImgTraceDialog.getPreferredSize());
			}
			else
			{
				nd = new Dimension(d.width + 20, d.height + 30);
				MyUtils.assertFalse((d.width <= 10) || (d.height <= 10), "ImgTraceDialog.windowOpened : incorrect image dimension"); //$NON-NLS-1$

				ImgTraceDialog.setMinimumSize(nd);
				ImgTraceDialog.setPreferredSize(ImgTraceDialog.getMinimumSize());
				ImgTraceDialog.setSize(ImgTraceDialog.getPreferredSize());
			}

			final MouseListener closer = new MouseAdapter()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
				 */
				@Override
				public void mouseClicked(MouseEvent e)
				{
					MyUtils.trace(Level.FINEST, "ImgTraceDialog clicked");
					ImgTraceDialog.dispose();
				}
			};

			MyUtils.doItToAllSubComponents(ImgTraceDialog, new MyUtils.DoItToThisComponent()
			{

				@Override
				public void doIt(Component c)
				{
					c.addMouseListener(closer);
				}
			}, true);

		}
		return ImgTraceDialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.VueDetaillePanel#getPanel()
	 */
	public JPanel getPanel()
	{
		return this;
	}

	private JEditorPane getJInfosEditorPane()
	{
		if (jInfosEditorPane == null)
		{
			jInfosEditorPane = new JEditorPane();
			jInfosEditorPane.setEditable(false);
			jInfosEditorPane.setContentType("text/html");
			jInfosEditorPane.setText("Loading..");
			// Dimension d = new Dimension(800, 100);
			// jInfosEditorPane.setPreferredSize(d);

			jInfosEditorPane.addMouseListener(vueDetaillePanelMouseAdapter);

			jInfosEditorPane.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					String sel = jInfosEditorPane.getSelectedText();
					if ((sel != null) && ( !sel.isEmpty()))
					{
						MyUtils.trace(Level.FINE, "Selection '" + sel + "'"); //$NON-NLS-1$ //$NON-NLS-2$
						Dictionary dictionnaire = KanjiNoSensei.getApp().getDictionnaire();

						Element e = dictionnaire.getElement(new Word(sel, "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

						if (e == null)
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("KanjiVueDetaillePanel.ErrorMissingWord") + " : \"" + sel.charAt(0) + "(" + sel.substring(1) + ")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							e = dictionnaire.getElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
						}

						if (e == null)
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("KanjiVueDetaillePanel.ErrorMissingKanji") + " : \"" + sel.charAt(0) + "(" + sel.substring(1) + ")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							return;
						}

						if ( !Kanji.class.isInstance(e) && !Word.class.isInstance(e))
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("KanjiVueDetaillePanel.ErrorSelectionIsNotCorrect") + " : " + e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
							return;
						}

						VueElement vueElement = null;
						try
						{
							vueElement = VueElement.genererVueElement(e);
						}
						catch (Exception e1)
						{
							KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiVueDetaillePanel.ErrorViewGeneration") + e.toString() + "\"\tMessage : " + e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
							return;
						}

						JDialog motDetail = new JDialog((JDialog) null, Messages.getString("SentenceVueDetaillePanel.Detail") + " : (" + Messages.getString(e.getClass().getSimpleName()) + ") \"" + e.toString() + "\"", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						motDetail.add(vueElement.getVueDetaillePanel().getPanel());
						motDetail.pack();
						motDetail.setVisible(true);
					}
				}
			});
		}
		
		return jInfosEditorPane;
	}
}
