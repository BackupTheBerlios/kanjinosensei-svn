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
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import metier.Dictionary;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Word;
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
	private JPanel				jPanelCentre;
	private JToggleButton		jButtonJouerSon;
	private JTextField			jTextFieldMot;
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
					jLabelLecture.setText("Lecture : neko");
				}
				{
					jButtonJouerSon = new JPanelSonBtn(vue.getMot().getSoundFile(), false);
					jPanelNorth.add(jButtonJouerSon, BorderLayout.EAST);
					jButtonJouerSon.setText("Lire");
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
					jLabelSignifications.setText("Significations : chat, ...");
				}
				{
					jLabelThemes = new JLabel();
					jPanelSouth.add(jLabelThemes);
					jLabelThemes.setText("Thèmes : mot, patin, coufin");
				}
			}
			{
				jPanelCentre = new JPanel();
				FlowLayout jPanelCentreLayout = new FlowLayout();
				jPanelCentreLayout.setVgap(0);
				this.add(jPanelCentre, BorderLayout.CENTER);
				jPanelCentre.setLayout(jPanelCentreLayout);
				jPanelCentre.setMinimumSize(new java.awt.Dimension(600, 45));
				jPanelCentre.setSize(600, 45);
				jPanelCentre.setPreferredSize(new java.awt.Dimension(10, 45));
				{
					jTextFieldMot = new JTextField();
					jPanelCentre.add(jTextFieldMot);
					jTextFieldMot.setText("\u7f8e\u5c11\u5973");
					jTextFieldMot.setFont(new java.awt.Font("Kochi Mincho",Font.PLAIN,44));
					jTextFieldMot.setHorizontalAlignment(SwingConstants.CENTER);
					jTextFieldMot.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
					jTextFieldMot.setEditable(false);
					jTextFieldMot.setPreferredSize(new java.awt.Dimension(47, 47));
					jTextFieldMot.setSize(0, 47);
					jTextFieldMot.addMouseListener(vueDetaillePanelMouseAdapter);
					jTextFieldMot.addCaretListener(new CaretListener() {
						public void caretUpdate(CaretEvent evt) {
							String sel = jTextFieldMot.getSelectedText();
							if ((sel != null) && (!sel.isEmpty()))
							{
								System.out.println("Selection '"+sel+"'");
								Dictionary dictionnaire = vue.getApp().getDictionnaire();
								
								Element e = dictionnaire.chercherElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey());
								
								if (e == null)
								{
									System.err.println("Kanji '" + sel.charAt(0) + "' non présent dans le dictionnaire");
									return;
								}
					
								if (!Kanji.class.isInstance(e))
								{
									System.err.println("La sélection correspond à un élément qui n'est pas un Kanji : "+e.toString());
									return;
								}
								
								VueElement vueElement = null;
								try
								{
									vueElement = VueElement.genererVueElement(vue.getApp(), e, vue.useRomaji());
								}
								catch (Exception e1)
								{
									System.err.println("Impossible de créer la vue pour l'élément sélectionné : \""+e.toString()+"\"\tMessage : "+e1.getMessage());
									return;
								}
								
								JDialog kanjiDetail = new JDialog((JDialog) null, "Détail Kanji '"+e.toString()+"'", true);
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
		jLabelLecture.setText("Lecture : " + vue.toRomajiIfNeeded(mot.getLecture()));
		jTextFieldMot.setText(mot.getWord());
		jTextFieldMot.setColumns(jTextFieldMot.getText().length() * 2);
		jLabelSignifications.setText("Significations : " + mot.getSignifications());
		jLabelThemes.setText("Thèmes : " + mot.getThemes());
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