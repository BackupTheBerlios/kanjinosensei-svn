/**
 * 
 */
package vue.kanji;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import metier.Dictionary;
import metier.Messages;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Word;
import utils.MyAutoResizingText;
import utils.MyUtils;
import vue.VueElement;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.VueElement.VueDetaillePanel;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * @author Axan
 * 
 */
class KanjiVueDetaillePanel extends JPanel implements VueDetaillePanel, QuizQuestionPanel, QuizSolutionPanel
{

	private static final long	serialVersionUID				= 1L;

	public static final int		vueHeight						= 100;

	private VueKanji			vue								= null;

	private JPanel				jPanelCodeUTF8					= null;

	private JScrollPane			jScrollPaneInfos				= null;

	private JLabel				jLabelCodeUTF8					= null;

	private JPanel				jPanelInfos						= null;

	private JDialog				ImgTraceDialog					= null;

	private JLabel				jLabelThemes					= null;

	private JLabel				jLabelSignifications			= null;

	private JLabel				jLabelLecturesJaponaises		= null;

	private JLabel				jLabelLecturesChinoises			= null;
	
	private JTextArea				jTextAreaMotsExemples				= null;

	private JPanel				ImgTraceContentPane				= null;

	//private JPanelImageBg		jPanelImageBg					= null;
	
	//private JPanel				jPanelStrokeOrdersFont		= null;

	private final JPanel		vueDetaillePanel				= this;

	private final MouseAdapter	vueDetaillePanelMouseAdapter	= new MouseAdapter()
																{

																	@Override
																	public void mouseClicked(MouseEvent e)
																	{
																		vueDetaillePanel.dispatchEvent(e);
																		super.mouseClicked(e);
																	}

																};

	public KanjiVueDetaillePanel(VueKanji vue)
	{
		super();
		this.vue = vue;
		initialize();
		SwingUtilities.invokeLater(new Runnable()
		{

			public void run()
			{
				dynamicInitialize();
			}

		});
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setLayout(new BorderLayout());
		this.setBounds(0, 0, 600, 100);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setPreferredSize(new java.awt.Dimension(600, 147));
		this.setSize(600, 100);
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.add(getJPanelCodeUTF8(), BorderLayout.WEST);
		this.add(getJScrollPaneInfos(), BorderLayout.CENTER);

	}

	// <NoJigloo>
	private void dynamicInitialize()
	{
		this.setBounds(new Rectangle(0, 0, 600, vueHeight));
		this.setPreferredSize(new Dimension(0, vueHeight));
		this.setSize(0, vueHeight);

		//getJPanelCodeUTF8().setFont(new Font("SimSun", Font.PLAIN, vueHeight)); //$NON-NLS-1$
		
		jPanelCodeUTF8.setPreferredSize(new java.awt.Dimension(100, 100));

		miseAJourInfos();
	}

	protected void miseAJourInfos()
	{
		jLabelCodeUTF8.setText(vue.getKanji().getCodeUTF8().toString());
		jLabelLecturesChinoises.setText(Messages.getString("KanjiVueDetaillePanel.LabelONLectures") + " : " + vue.toRomajiIfNeeded(vue.getKanji().getLecturesON())); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelLecturesJaponaises.setText(Messages.getString("KanjiVueDetaillePanel.LabelKUNLectures") + " : " + vue.toRomajiIfNeeded(vue.getKanji().getLecturesKUN())); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelSignifications.setText(Messages.getString("KanjiVueDetaillePanel.LabelSignifications") + " : " + vue.getKanji().getSignifications()); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelThemes.setText(Messages.getString("KanjiVueDetaillePanel.LabelThemes") + " : " + vue.getKanji().getThemes()); //$NON-NLS-1$ //$NON-NLS-2$
		
		Dictionary dico = vue.getApp().getDictionnaire();
		Set<Element> motsExemples = dico.getElementsSelection(new Dictionary.DictionarySorter()
		{
		
			@Override
			public boolean testElement(Element e)
			{
				if (!Word.class.isInstance(e)) return false;				
				Word w = (Word) e;
				return w.getWord().matches(".*"+vue.getKanji().getCodeUTF8().toString()+".*"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		
		});
		
		StringBuilder listeMotsExemples = new StringBuilder(""); //$NON-NLS-1$
		Iterator<Element> it = motsExemples.iterator();
		while(it.hasNext())
		{
			Word w = (Word) it.next();
			if (listeMotsExemples.length() > 0) listeMotsExemples.append("; "); //$NON-NLS-1$
			listeMotsExemples.append(String.format("%s (%s)", w.getWord(), w.getSignifications())); //$NON-NLS-1$
		}
		
		jTextAreaMotsExemples.setText(Messages.getString("KanjiVueDetaillePanel.LabelMotsExemples") + " : " + listeMotsExemples.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		
		setVisible(false);
		setVisible(true);
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
			jLabelCodeUTF8 = new JLabel();
			jLabelCodeUTF8.setText("猫"); //$NON-NLS-1$
			jLabelCodeUTF8.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelCodeUTF8.setPreferredSize(new java.awt.Dimension(100, 100));
			jLabelCodeUTF8.setFont(new Font("Kochi Mincho", Font.PLAIN, 98)); //$NON-NLS-1$
			jLabelCodeUTF8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelCodeUTF8 = new JPanel();
			jPanelCodeUTF8.setLayout(new BorderLayout());
			jPanelCodeUTF8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelCodeUTF8.setSize(100, 100);
			jPanelCodeUTF8.setPreferredSize(new Dimension(100, 0));
			jLabelCodeUTF8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			MyAutoResizingText<JLabel> jAutoSizeLabelCodeUTF8 = new MyAutoResizingText<JLabel>(jLabelCodeUTF8, VueKanji.FONT_MIN_SIZE, Float.POSITIVE_INFINITY);
			
			jLabelCodeUTF8.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					MyUtils.trace("jLabelCodeUTF8.mouseClicked"); //$NON-NLS-1$
					synchronized (jLabelCodeUTF8)
					{
						if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
						{
							ImgTraceDialog = null; // Force recreate the ImgTraceDialog
							JDialog imageTraceDialog = getImgTraceDialog();
							imageTraceDialog.pack();
							Point loc = e.getLocationOnScreen();
							loc.translate(20, 20);
							imageTraceDialog.setLocation(loc);
							MyUtils.trace("jLabelCodeUTF8 double clicked should display imageTraceDialog"); //$NON-NLS-1$

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
			jLabelCodeUTF8.setSize(100, 100);
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
			jScrollPaneInfos.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
			jScrollPaneInfos.setViewportView(getJPanelInfos());
			jScrollPaneInfos.setPreferredSize(new java.awt.Dimension(0, 100));
			jScrollPaneInfos.setOpaque(false);
			jScrollPaneInfos.setSize(600, 100);
			jScrollPaneInfos.getHorizontalScrollBar().setPreferredSize(new java.awt.Dimension(0, 16));
			jScrollPaneInfos.getHorizontalScrollBar().setMinimumSize(new java.awt.Dimension(0, 16));
			jScrollPaneInfos.addMouseListener(vueDetaillePanelMouseAdapter);

		}
		return jScrollPaneInfos;
	}

	/**
	 * This method initializes jPanelInfos
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelInfos()
	{
		if (jPanelInfos == null)
		{
			jLabelThemes = new JLabel();
			jLabelThemes.setText(Messages.getString("KanjiVueDetaillePanel.LabelThemes")); //$NON-NLS-1$
			jLabelThemes.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelThemes.setVerticalAlignment(SwingConstants.TOP);
			jLabelThemes.setPreferredSize(new Dimension(0, 0));
			jLabelThemes.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			MyAutoResizingText<JLabel> jAutoSizeLabelThemes = new MyAutoResizingText<JLabel>(jLabelThemes, VueKanji.FONT_MIN_SIZE, VueKanji.FONT_MAX_SIZE);
			
			jLabelSignifications = new JLabel();
			jLabelSignifications.setText(Messages.getString("KanjiVueDetaillePanel.LabelSignifications")); //$NON-NLS-1$
			jLabelSignifications.setPreferredSize(new Dimension(0, 0));
			jLabelSignifications.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelSignifications.setVerticalAlignment(SwingConstants.TOP);
			jLabelSignifications.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			MyAutoResizingText<JLabel> jAutoSizeLabelSignifications = new MyAutoResizingText<JLabel>(jLabelSignifications, VueKanji.FONT_MIN_SIZE, VueKanji.FONT_MAX_SIZE);
			
			jLabelLecturesJaponaises = new JLabel();
			jLabelLecturesJaponaises.setText(Messages.getString("KanjiVueDetaillePanel.LabelKUNLectures")); //$NON-NLS-1$
			jLabelLecturesJaponaises.setPreferredSize(new java.awt.Dimension(600, 25));
			jLabelLecturesJaponaises.setVerticalAlignment(SwingConstants.TOP);
			jLabelLecturesJaponaises.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelLecturesJaponaises.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			MyAutoResizingText<JLabel> jAutoSizeLabelLecturesJaponaises = new MyAutoResizingText<JLabel>(jLabelLecturesJaponaises, VueKanji.FONT_MIN_SIZE, VueKanji.FONT_MAX_SIZE);
			
			jLabelLecturesChinoises = new JLabel();
			jLabelLecturesChinoises.setText(Messages.getString("KanjiVueDetaillePanel.LabelONLectures")); //$NON-NLS-1$
			jLabelLecturesChinoises.setPreferredSize(new java.awt.Dimension(600, 25));
			jLabelLecturesChinoises.setVerticalAlignment(SwingConstants.TOP);
			jLabelLecturesChinoises.setHorizontalTextPosition(SwingConstants.LEFT);
			jLabelLecturesChinoises.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			MyAutoResizingText<JLabel> jAutoSizeLabelLecturesChinoises = new MyAutoResizingText<JLabel>(jLabelLecturesChinoises, VueKanji.FONT_MIN_SIZE, VueKanji.FONT_MAX_SIZE);
			
			jTextAreaMotsExemples = new JTextArea();
			jTextAreaMotsExemples.setText(Messages.getString("KanjiVueDetaillePanel.LabelMotsExemples")); //$NON-NLS-1$
			//jLabelMotsExemples.setHorizontalAlignment(SwingConstants.LEFT);
			//jLabelMotsExemples.setVerticalAlignment(SwingConstants.TOP);
			jTextAreaMotsExemples.setPreferredSize(new Dimension(0, 0));
			jTextAreaMotsExemples.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jTextAreaMotsExemples.setEditable(false);
			MyAutoResizingText<JTextArea> jAutoSizeLabelMotsExemples = new MyAutoResizingText<JTextArea>(jTextAreaMotsExemples, VueKanji.FONT_MIN_SIZE, VueKanji.FONT_MAX_SIZE);
			jTextAreaMotsExemples.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent evt) {
					String sel = jTextAreaMotsExemples.getSelectedText();
					if ((sel != null) && (!sel.isEmpty()))
					{
						System.out.println("Selection '"+sel+"'"); //$NON-NLS-1$ //$NON-NLS-2$
						Dictionary dictionnaire = vue.getApp().getDictionnaire();
						
						Element e = dictionnaire.chercherElement(new Word(sel, "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						
						if (e == null)
						{
							System.err.println(Messages.getString("KanjiVueDetaillePanel.ErrorMissingWord") + " : \"" + sel.charAt(0) + "("+sel.substring(1)+")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							e = dictionnaire.chercherElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
						}
						
						if (e == null)
						{
							System.err.println(Messages.getString("KanjiVueDetaillePanel.ErrorMissingKanji") + " : \"" + sel.charAt(0) + "("+sel.substring(1)+")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							return;
						}
			
						if (!Kanji.class.isInstance(e) && !Word.class.isInstance(e))
						{
							System.err.println(Messages.getString("KanjiVueDetaillePanel.ErrorSelectionIsNotCorrect")+ " : "+e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
							return;
						}
						
						VueElement vueElement = null;
						try
						{
							vueElement = VueElement.genererVueElement(vue.getApp(), e, vue.useRomaji());
						}
						catch (Exception e1)
						{
							System.err.println(Messages.getString("KanjiVueDetaillePanel.ErrorViewGeneration")+e.toString()+"\"\tMessage : "+e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
							return;
						}
						
						JDialog motDetail = new JDialog((JDialog) null, Messages.getString("SentenceVueDetaillePanel.Detail")+" : ("+Messages.getString(e.getClass().getSimpleName())+") \""+e.toString()+"\"", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						motDetail.add(vueElement.getVueDetaillePanel().getPanel());
						motDetail.pack();
						motDetail.setVisible(true);
					}
				}
			});
			
			GridLayout gridLayout = new GridLayout(0, 1, 0, 0);
			jPanelInfos = new JPanel();
			jPanelInfos.setPreferredSize(new java.awt.Dimension(0, 0));
			jPanelInfos.setBounds(new Rectangle(0, 0, 0, 0));
			jPanelInfos.setLayout(gridLayout);
			jPanelInfos.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelInfos.addMouseListener(vueDetaillePanelMouseAdapter);
			jPanelInfos.add(jLabelLecturesChinoises);
			jPanelInfos.add(jLabelLecturesJaponaises);
			jPanelInfos.add(jLabelSignifications);
			jPanelInfos.add(jTextAreaMotsExemples);
			jLabelSignifications.setOpaque(true);
			jPanelInfos.add(jLabelThemes);
			
			jPanelInfos.doLayout();
		}
		return jPanelInfos;
	}

	/**
	 * This method initializes ImgTraceDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JDialog getImgTraceDialog()
	{
		if (ImgTraceDialog == null)
		{
			MyUtils.trace("[1] ImgTraceDialog creation"); //$NON-NLS-1$
			ImgTraceDialog = new JDialog();
			ImgTraceDialog.setSize(new Dimension(0, 0));
			ImgTraceDialog.setTitle(Messages.getString("KanjiVueDetaillePanel.LabelStrokesOrder")); //$NON-NLS-1$
			ImgTraceDialog.setModal(true);
			ImgTraceDialog.setMinimumSize(new Dimension(100, 100));
			ImgTraceDialog.setPreferredSize(new Dimension(0, 0));
			ImgTraceDialog.setContentPane(getImgTraceContentPane());
			ImgTraceDialog.setUndecorated(false);
			ImgTraceDialog.setBackground(Color.black);

			ImgTraceDialog.addComponentListener(new ComponentAdapter()
			{

				@Override
				public void componentShown(ComponentEvent e)
				{
					MyUtils.trace("[4] ImgTraceDialog < componentShown"); //$NON-NLS-1$
					MyUtils.assertTrue((ImgTraceDialog.getContentPane() == getImgTraceContentPane()) && (ImgTraceDialog.getContentPane() != null), "ImgTraceDialog unexpected contentPane"); //$NON-NLS-1$

					Dimension d = vue.getStrokeOrdersImgComponent().getPreferredSize();
					Dimension nd = new Dimension(d.width+20, d.height+30);
					MyUtils.assertFalse((d.width <= 10) || (d.height <= 10), "ImgTraceDialog.windowOpened : incorrect image dimension"); //$NON-NLS-1$

					ImgTraceDialog.setMinimumSize(nd);
					ImgTraceDialog.setPreferredSize(ImgTraceDialog.getMinimumSize());
					ImgTraceDialog.setSize(ImgTraceDialog.getPreferredSize());

					ImgTraceContentPane.doLayout();
					ImgTraceContentPane.repaint();
					ImgTraceContentPane.revalidate();
					ImgTraceContentPane.setVisible(true);

					MyUtils.assertTrue(ImgTraceDialog.getContentPane().isDisplayable(), "ContentPane not displayable"); //$NON-NLS-1$
					MyUtils.assertTrue(ImgTraceDialog.getContentPane().isVisible(), "ContentPane not visible"); //$NON-NLS-1$

					super.componentShown(e);
				}

			});

		}
		return ImgTraceDialog;
	}

	/**
	 * This method initializes ImgTraceContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getImgTraceContentPane()
	{
		if (ImgTraceContentPane == null)
		{
			MyUtils.trace("[2] ImgTraceContentPane creation"); //$NON-NLS-1$
			ImgTraceContentPane = new JPanel();
			ImgTraceContentPane.setLayout(new BorderLayout());
			ImgTraceContentPane.add(vue.getStrokeOrdersImgComponent(), BorderLayout.CENTER);
		}
		return ImgTraceContentPane;
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

}
