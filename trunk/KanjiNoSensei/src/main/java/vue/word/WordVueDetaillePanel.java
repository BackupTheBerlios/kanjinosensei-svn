package vue.word;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import metier.Dictionary;
import metier.Messages;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Word;
import utils.MyAutoResizingText;
import vue.JPanelSonBtn;
import vue.VueElement;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.VueElement.VueDetaillePanel;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
class WordVueDetaillePanel extends javax.swing.JPanel implements VueDetaillePanel, QuizQuestionPanel, QuizSolutionPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID				= 1L;
	private VueWord				vue								= null;
	private JScrollPane				jPanelCentre;
	private JToggleButton		jButtonJouerSon;
	private JTextArea			jTextPaneMot;
	private JLabel				jLabelSignifications;
	private JLabel				jLabelThemes;
	private JPanel				jPanelSouth;
	private JLabel				jLabelLecture;
	private JPanel				jPanelNorth;

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

	public WordVueDetaillePanel(VueWord vue)
	{
		super();
		this.vue = vue;
		initGUI();
		SwingUtilities.invokeLater(new Runnable()
		{

			public void run()
			{
				dynamicInitialize();
			}

		});
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(600, 100));
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setSize(600, 100);
			{
				jPanelNorth = new JPanel();
				BorderLayout jPanelNorthLayout = new BorderLayout();
				jPanelNorth.setLayout(jPanelNorthLayout);
				this.add(jPanelNorth, BorderLayout.NORTH);
				jPanelNorth.setSize(600, 20);
				jPanelNorth.setPreferredSize(new java.awt.Dimension(600, 20));
				{
					jLabelLecture = new JLabel();
					jPanelNorth.add(jLabelLecture, BorderLayout.WEST);
					jLabelLecture.setText("Lecture : neko"); //$NON-NLS-1$
					MyAutoResizingText<JLabel> jAutoSizeLabelLecture = new MyAutoResizingText<JLabel>(jLabelLecture, VueWord.FONT_MIN_SIZE, VueWord.FONT_MAX_SIZE);
				}
				{
					jButtonJouerSon = new JPanelSonBtn(vue.getMot().getSoundFile(), false);
					jPanelNorth.add(jButtonJouerSon, BorderLayout.EAST);
					jButtonJouerSon.setText(Messages.getString("WordVueDetaillePanel.ButtonPlaySound")); //$NON-NLS-1$
					jButtonJouerSon.setSize(35, 20);
				}
			}
			{
				jPanelSouth = new JPanel();
				GridLayout jPanelSouthLayout = new GridLayout(2, 1);
				jPanelSouthLayout.setHgap(5);
				jPanelSouthLayout.setVgap(5);
				jPanelSouthLayout.setColumns(1);
				jPanelSouthLayout.setRows(2);
				this.add(jPanelSouth, BorderLayout.SOUTH);
				jPanelSouth.setLayout(jPanelSouthLayout);
				jPanelSouth.setPreferredSize(new java.awt.Dimension(600, 28));
				{
					jLabelSignifications = new JLabel();
					jPanelSouth.add(jLabelSignifications);
					jLabelSignifications.setText("Significations : chat, ..."); //$NON-NLS-1$
					MyAutoResizingText<JLabel> jAutoSizeLabelSignifications = new MyAutoResizingText<JLabel>(jLabelSignifications, VueWord.FONT_MIN_SIZE, VueWord.FONT_MAX_SIZE);
				}
				{
					jLabelThemes = new JLabel();
					jPanelSouth.add(jLabelThemes);
					jLabelThemes.setText("Thèmes : mot, patin, coufin"); //$NON-NLS-1$
					MyAutoResizingText<JLabel> jAutoSizeLabelThemes = new MyAutoResizingText<JLabel>(jLabelThemes, VueWord.FONT_MIN_SIZE, VueWord.FONT_MAX_SIZE);
				}
			}
			{
				jPanelCentre = new JScrollPane();
				this.add(jPanelCentre, BorderLayout.CENTER);
				{
					jTextPaneMot = new JTextArea();
					jPanelCentre.setViewportView(jTextPaneMot);
					jTextPaneMot.setText("\u7f8e\u5c11\u5973"); //$NON-NLS-1$
					jTextPaneMot.setFont(new java.awt.Font("Kochi Mincho",Font.PLAIN,44)); //$NON-NLS-1$
					jTextPaneMot.setBorder(BorderFactory.createEmptyBorder());
					jTextPaneMot.setEditable(false);
					jTextPaneMot.setPreferredSize(new java.awt.Dimension(0, 47));
					jTextPaneMot.setSize(0, 47);
					
					MyAutoResizingText<JTextArea> jAutoSizeTextPane = new MyAutoResizingText<JTextArea>(jTextPaneMot, VueWord.FONT_MIN_SIZE, Float.POSITIVE_INFINITY);
					
					jTextPaneMot.addMouseListener(vueDetaillePanelMouseAdapter);
					jTextPaneMot.addCaretListener(new CaretListener() {
						public void caretUpdate(CaretEvent evt) {
							String sel = jTextPaneMot.getSelectedText();
							if ((sel != null) && (!sel.isEmpty()))
							{
								System.out.println("Selection '"+sel+"'"); //$NON-NLS-1$ //$NON-NLS-2$
								Dictionary dictionnaire = vue.getApp().getDictionnaire();
								
								Element e = dictionnaire.chercherElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
								
								if (e == null)
								{
									System.err.println(Messages.getString("WordVueDetaillePanel.WarningKanjiMissing") + " : \"" + sel.charAt(0) + "(" + sel.substring(1) + ")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									return;
								}
					
								if (!Kanji.class.isInstance(e))
								{
									System.err.println(Messages.getString("WordVueDetaillePanel.WarningNotAKanji")+ " : \"" + e.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									return;
								}
								
								VueElement vueElement = null;
								try
								{
									vueElement = VueElement.genererVueElement(vue.getApp(), e, vue.useRomaji());
								}
								catch (Exception e1)
								{
									System.err.println(Messages.getString("WordVueDetaillePanel.ErrorViewGeneration")+" : \""+e.toString()+"\"\t"+e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									return;
								}
								
								JDialog kanjiDetail = new JDialog((JDialog) null, Messages.getString("WordVueDetaillePanel.Detail")+" : \""+e.toString()+"\"", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								kanjiDetail.add(vueElement.getVueDetaillePanel().getPanel());
								kanjiDetail.pack();
								kanjiDetail.setVisible(true);
							}
						}
					});
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// <NoJigloo>
	private void dynamicInitialize()
	{
		Word mot = vue.getMot();
		jLabelLecture.setText(Messages.getString("WordVueDetaillePanel.LabelLecture") + " : "+ vue.toRomajiIfNeeded(mot.getLecture())); //$NON-NLS-1$ //$NON-NLS-2$
		jTextPaneMot.setText(mot.getWord());
		//jTextPaneMot.setColumns(Math.max(2, jTextPaneMot.getText().length()) * 2);
		jLabelSignifications.setText(Messages.getString("WordVueDetaillePanel.LabelSignifications") + " : "+mot.getSignifications()); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelThemes.setText(Messages.getString("WordVueDetaillePanel.LabelThemes") + " : " +mot.getThemes()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// </NoJigloo>

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.VueDetaillePanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

}
