package vue.phrase;

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

import metier.Dictionnaire;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Mot;
import metier.elements.Phrase;
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
class PhraseVueDetaillePanel extends javax.swing.JPanel implements VueDetaillePanel, QuizQuestionPanel, QuizSolutionPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID				= 1L;
	private VuePhrase				vue								= null;
	private JPanel				jPanelCentre;
	private JToggleButton		jButtonJouerSon;
	private JTextField			jTextFieldPhrase;
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
																		System.out
																				.println("vueDetaillePanelMouseAdapter.mouseClicked : "
																						+ e);
																		vueDetaillePanel.dispatchEvent(e);
																		super.mouseClicked(e);
																	}

																};

	public PhraseVueDetaillePanel(VuePhrase vue)
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
					jButtonJouerSon = new JPanelSonBtn(vue.getPhrase().getSon(), false);
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
					jLabelThemes.setText("Thèmes : phrase, patin, coufin");
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
					jTextFieldPhrase = new JTextField();
					jPanelCentre.add(jTextFieldPhrase);
					jTextFieldPhrase.setText("\u7f8e\u5c11\u5973");
					jTextFieldPhrase.setFont(new java.awt.Font("Kochi Mincho",Font.PLAIN,44));
					jTextFieldPhrase.setHorizontalAlignment(SwingConstants.CENTER);
					jTextFieldPhrase.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
					jTextFieldPhrase.setEditable(false);
					jTextFieldPhrase.setPreferredSize(new java.awt.Dimension(47, 47));
					jTextFieldPhrase.setSize(0, 47);
					jTextFieldPhrase.addMouseListener(vueDetaillePanelMouseAdapter);
					jTextFieldPhrase.addCaretListener(new CaretListener() {
						public void caretUpdate(CaretEvent evt) {
							System.out.println("jTextFieldPhrase.caretUpdate, event="+evt);
							String sel = jTextFieldPhrase.getSelectedText();
							if ((sel != null) && (!sel.isEmpty()))
							{
								System.out.println("Selection '"+sel+"'");
								Dictionnaire dictionnaire = vue.getApp().getDictionnaire();
								
								Element e = dictionnaire.chercherElement(new Mot(sel, "", "", "", "").getKey());
								
								if (e == null)
								{
									e = dictionnaire.chercherElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey());
								}
								
								if (e == null)
								{
									System.err.println("Le mot '"+sel+"' et le kanji '" + sel.charAt(0) + "' sont absent du dictionnaire");
									return;
								}
					
								if ((!Kanji.class.isInstance(e)) && (!Mot.class.isInstance(e)))
								{
									System.err.println("La sélection correspond à un élément qui n'est ni un mot ni un kanji : "+e.toString());
									return;
								}
								
								VueElement vueElement = null;
								try
								{
									vueElement = VueElement.genererVueElement(vue.getApp(), e);
								}
								catch (Exception e1)
								{
									System.err.println("Impossible de créer la vue pour l'élément sélectionné : \""+e.toString()+"\"\tMessage : "+e1.getMessage());
									return;
								}
								
								JDialog motkanjiDetail = new JDialog((JDialog) null, "Détail "+e.getClass().getSimpleName()+" '"+e.toString()+"'", true);
								motkanjiDetail.add(vueElement.getVueDetaillePanel().getPanel());
								motkanjiDetail.pack();
								motkanjiDetail.setVisible(true);
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
		Phrase phrase = vue.getPhrase();
		jLabelLecture.setText("Lecture : " + phrase.getLecture());
		jTextFieldPhrase.setText(phrase.getPhrase());
		jTextFieldPhrase.setColumns(jTextFieldPhrase.getText().length() * 2);
		jLabelSignifications.setText("Significations : " + phrase.getSignifications());
		jLabelThemes.setText("Thèmes : " + phrase.getThemes());
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
