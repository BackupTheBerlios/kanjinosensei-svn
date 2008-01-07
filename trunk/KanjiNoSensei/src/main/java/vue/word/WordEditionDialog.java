package vue.word;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;

import metier.Dictionary;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Word;
import utils.MyUtils;
import vue.VueElement.EditionDialog;

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
class WordEditionDialog extends javax.swing.JDialog implements EditionDialog
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private VueWord				vue				= null;
	private JTextField			jTextFieldThemes;
	private JLabel				jLabelThemes;
	private JTextField			jTextFieldSignifications;
	private JLabel				jLabelSignification;
	private JButton				jButtonParcourir;
	private JTextField			jTextFieldSon;
	private JLabel				jLabelSon;
	private JLabel				jLabelMsg;
	private JTextField			jTextFieldLecture;
	private JLabel				jLabelLecture;
	private JPanel				jPanelMsg;
	private JButton				jButtonAnnuler;
	private JButton				jButtonValider;
	private JTextField			jTextFieldMot;
	private JLabel				jLabelMot;
	private JPanel				jPanelBtns;
	private JPanel				ajouterMotFormPanel;
	private JPanel				ajouterMotContentPane;
	private Dictionary		dictionnaire;
	
	private boolean motEdite = false;

	static private String[]		extFilterSons = {"wav", "mp3"};
	static private FileFilter	fileFilterSons = MyUtils.generateFileFilter("Sons", extFilterSons);
	
	public WordEditionDialog(VueWord vue)
	{
		super();
		this.vue = vue;
		this.dictionnaire = vue.getApp().getDictionnaire();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			this.setMinimumSize(new Dimension(400, 150));
			this.setPreferredSize(new java.awt.Dimension(390, 200));
			this.setResizable(true);
			this.setTitle("Edition d'un mot");
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setModal(true);
			this.setSize(390, 135);

			{
				ajouterMotContentPane = new JPanel();
				BorderLayout ajouterMotContentPaneLayout = new BorderLayout();
				ajouterMotContentPane.setLayout(ajouterMotContentPaneLayout);
				getContentPane().add(ajouterMotContentPane, BorderLayout.CENTER);
				ajouterMotContentPane.setPreferredSize(new java.awt.Dimension(422, 150));
				ajouterMotContentPane.setSize(422, 150);
				{
					jPanelMsg = new JPanel();
					ajouterMotContentPane.add(jPanelMsg, BorderLayout.NORTH);
					BorderLayout jPanelMsgLayout = new BorderLayout();
					jPanelMsg.setLayout(jPanelMsgLayout);
					jPanelMsg.setPreferredSize(new java.awt.Dimension(422, 20));
					{
						jLabelMsg = new JLabel();
						BorderLayout jLabelMsgLayout = new BorderLayout();
						jLabelMsg.setLayout(jLabelMsgLayout);
						jPanelMsg.add(jLabelMsg, BorderLayout.CENTER);
						jLabelMsg.setText("message");
					}
				}
				{
					ajouterMotFormPanel = new JPanel();
					GridBagLayout ajouterMotFormPanelLayout = new GridBagLayout();
					ajouterMotFormPanelLayout.columnWidths = new int[] {7, 100, 7};
					ajouterMotFormPanelLayout.rowHeights = new int[] {7, 20, 7, 7, 7};
					ajouterMotFormPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
					ajouterMotFormPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
					ajouterMotContentPane.add(ajouterMotFormPanel, BorderLayout.CENTER);
					ajouterMotFormPanel.setLayout(ajouterMotFormPanelLayout);
					ajouterMotFormPanel.setSize(422, 120);
					ajouterMotFormPanel.setPreferredSize(new java.awt.Dimension(422, 120));
					{
						jLabelMot = new JLabel();
						jLabelMot.setText("Mot :");
						jLabelMot.setPreferredSize(new java.awt.Dimension(100, 20));
						ajouterMotFormPanel.add(jLabelMot, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelMot.setSize(100, 20);
					}
					{
						jTextFieldMot = new JTextField();
						ajouterMotFormPanel.add(jTextFieldMot, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldMot.setSize(184, 20);
						jTextFieldMot.addCaretListener(new CaretListener()
						{
							public void caretUpdate(CaretEvent evt)
							{
								jLabelMsg.setText("");
								for (Character c : jTextFieldMot.getText().toCharArray())
								{
									if (Character.isSpaceChar(c) || Character.isWhitespace(c))
									{
										jLabelMsg.setText(jLabelMsg.getText() + "\t" + "Caractère blanc détecté");
									}
									

									if (dictionnaire.chercherElement(new Kanji(c, "", "", "", "", "").getKey()) == null)
									{
										jLabelMsg.setText(jLabelMsg.getText() + "\t" + "Kanji '" + c
												+ "' non présent dans le dictionnaire");
									}
								}
							}
						});
					}
					{
						jLabelSon = new JLabel();
						jLabelSon.setText("Son :");
						jLabelSon.setPreferredSize(new java.awt.Dimension(100, 20));
						ajouterMotFormPanel.add(jLabelSon, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelSon.setSize(100, 20);
					}
					{
						jTextFieldSon = new JTextField();
						jTextFieldSon.setEditable(false);
						ajouterMotFormPanel.add(jTextFieldSon, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldSon.setSize(184, 20);
					}
					{
						jButtonParcourir = new JButton();
						jButtonParcourir.setText("Parcourir...");
						jButtonParcourir.setPreferredSize(new java.awt.Dimension(100, 20));
						ajouterMotFormPanel.add(jButtonParcourir, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonParcourir.setSize(100, 20);
						jButtonParcourir.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								JFileChooser fc = new JFileChooser();

								fc.setFileFilter(fileFilterSons);
								if (fc.showOpenDialog(vue.getApp().getJFrame()) == JFileChooser.APPROVE_OPTION)
								{
									File fic = fc.getSelectedFile();
									jTextFieldSon.setText(fic.getAbsolutePath());
								}
							}
						});
					}
					{
						jLabelSignification = new JLabel();
						ajouterMotFormPanel.add(jLabelSignification, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelSignification.setText("Significations :");
						jLabelSignification.setSize(69, 20);
					}
					{
						jTextFieldSignifications = new JTextField();
						ajouterMotFormPanel.add(jTextFieldSignifications, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldSignifications.setSize(184, 20);
					}
					{
						jLabelThemes = new JLabel();
						ajouterMotFormPanel.add(jLabelThemes, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelThemes.setText("Thèmes :");
						jLabelThemes.setSize(44, 20);
					}
					{
						jTextFieldThemes = new JTextField();
						ajouterMotFormPanel.add(jTextFieldThemes, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldThemes.setSize(184, 20);
					}
					{
						jLabelLecture = new JLabel();
						ajouterMotFormPanel.add(jLabelLecture, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelLecture.setText("Lecture :");
					}
					{
						jTextFieldLecture = new JTextField();
						ajouterMotFormPanel.add(jTextFieldLecture, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					jPanelBtns = new JPanel();
					GridBagLayout jPanelBtnsLayout = new GridBagLayout();
					ajouterMotContentPane.add(jPanelBtns, BorderLayout.SOUTH);
					jPanelBtns.setLayout(jPanelBtnsLayout);
					jPanelBtns.setPreferredSize(new java.awt.Dimension(422, 20));
					{
						jButtonValider = new JButton();
						jPanelBtns.add(jButtonValider, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 5), 0, 0));
						jButtonValider.setText("Valider");
						jButtonValider.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								if ( !jLabelMsg.getText().isEmpty())
								{
									if (JOptionPane.showConfirmDialog(null,
											"Il y a des erreurs, êtes-vous sûr de vouloir continuer ?", "Attention",
											JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
									{
										return;
									}
								}
								motEdite = true;
								vue.setMot(new Word(jTextFieldMot.getText(), jTextFieldLecture.getText(), jTextFieldSon
										.getText(), jTextFieldSignifications.getText(), jTextFieldThemes.getText()));
								dispose();
							}
						});
					}
					{
						jButtonAnnuler = new JButton();
						jPanelBtns.add(jButtonAnnuler, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
						jButtonAnnuler.setText("Annuler");
						jButtonAnnuler.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								System.out.println("jButtonAnnuler.actionPerformed, event=" + evt);
								dispose();
							}
						});
					}
				}
			}
			
			//<NoJigloo>
			Word mot = vue.getMot();
			if (mot != null)
			{
				jTextFieldMot.setText(mot.getWord());
				jTextFieldLecture.setText(mot.getLecture());
				jTextFieldSon.setText(mot.getSoundFile());
				jTextFieldSignifications.setText(mot.getSignifications());
				jTextFieldThemes.setText(mot.getThemes());
				
			}
			//</NoJigloo>
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.EditionDialog#afficher()
	 */
	@Override
	public synchronized boolean afficher()
	{
		motEdite = false;
		pack();
		setVisible(true);
		return motEdite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.EditionDialog#getElementEdite()
	 */
	@Override
	public Element getElementEdite()
	{
		return vue.getMot();
	}

}
