package epsofts.KanjiNoSensei.vue.sentence;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;

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

import epsofts.KanjiNoSensei.metier.Dictionary;
import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.metier.elements.Sentence;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;
import epsofts.KanjiNoSensei.vue.VueElement.EditionDialog;


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
class SentenceEditionDialog extends javax.swing.JDialog implements EditionDialog
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private VueSentence				vue				= null;
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
	private JTextField			jTextFieldPhrase;
	private JLabel				jLabelPhrase;
	private JPanel				jPanelBtns;
	private JPanel				ajouterPhraseFormPanel;
	private JPanel				ajouterPhraseContentPane;
	private Dictionary		dictionnaire;
	
	private boolean phraseEdite = false;

	static private String[]		extFilterSons = {"wav", "mp3"}; //$NON-NLS-1$ //$NON-NLS-2$
	static private FileFilter	fileFilterSons = MyUtils.generateFileFilter(Messages.getString("SentenceEditionDialog.SoundFileFilterName"), extFilterSons); //$NON-NLS-1$
	
	public SentenceEditionDialog(VueSentence vue)
	{
		super();
		this.vue = vue;
		this.dictionnaire = KanjiNoSensei.getApp().getDictionnaire();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			this.setMinimumSize(new Dimension(400, 150));
			this.setPreferredSize(new java.awt.Dimension(504, 170));
			this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
			this.setResizable(true);
			this.setTitle(Messages.getString("SentenceEditionDialog.Title")); //$NON-NLS-1$
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setModal(true);
			this.setSize(504, 170);

			{
				ajouterPhraseContentPane = new JPanel();
				BorderLayout ajouterPhraseContentPaneLayout = new BorderLayout();
				ajouterPhraseContentPane.setLayout(ajouterPhraseContentPaneLayout);
				getContentPane().add(ajouterPhraseContentPane, BorderLayout.CENTER);
				ajouterPhraseContentPane.setPreferredSize(new java.awt.Dimension(422, 150));
				ajouterPhraseContentPane.setSize(422, 150);
				{
					jPanelMsg = new JPanel();
					ajouterPhraseContentPane.add(jPanelMsg, BorderLayout.NORTH);
					BorderLayout jPanelMsgLayout = new BorderLayout();
					jPanelMsg.setLayout(jPanelMsgLayout);
					jPanelMsg.setPreferredSize(new java.awt.Dimension(422, 20));
					{
						jLabelMsg = new JLabel();
						BorderLayout jLabelMsgLayout = new BorderLayout();
						jLabelMsg.setLayout(jLabelMsgLayout);
						jPanelMsg.add(jLabelMsg, BorderLayout.CENTER);
						jLabelMsg.setText("message"); //$NON-NLS-1$
					}
				}
				{
					ajouterPhraseFormPanel = new JPanel();
					GridBagLayout ajouterPhraseFormPanelLayout = new GridBagLayout();
					ajouterPhraseFormPanelLayout.columnWidths = new int[] {7, 100, 7};
					ajouterPhraseFormPanelLayout.rowHeights = new int[] {7, 20, 7, 7, 7};
					ajouterPhraseFormPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
					ajouterPhraseFormPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
					ajouterPhraseContentPane.add(ajouterPhraseFormPanel, BorderLayout.CENTER);
					ajouterPhraseFormPanel.setLayout(ajouterPhraseFormPanelLayout);
					ajouterPhraseFormPanel.setSize(422, 120);
					ajouterPhraseFormPanel.setPreferredSize(new java.awt.Dimension(422, 120));
					{
						jLabelPhrase = new JLabel();
						jLabelPhrase.setText(Messages.getString("SentenceEditionDialog.LabelSentence")); //$NON-NLS-1$
						jLabelPhrase.setPreferredSize(new java.awt.Dimension(100, 20));
						ajouterPhraseFormPanel.add(jLabelPhrase, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelPhrase.setSize(100, 20);
					}
					{
						jTextFieldPhrase = new JTextField();
						ajouterPhraseFormPanel.add(jTextFieldPhrase, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldPhrase.setSize(184, 20);
						jTextFieldPhrase.addCaretListener(new CaretListener()
						{
							public void caretUpdate(CaretEvent evt)
							{
								jLabelMsg.setText(""); //$NON-NLS-1$
								for (Character c : jTextFieldPhrase.getText().toCharArray())
								{
									if (Character.isSpaceChar(c)) continue;
									if (Character.isWhitespace(c)) continue;

									if (dictionnaire.getElement(new Kanji(c, "", "", "", "", "").getKey()) == null) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
									{
										jLabelMsg.setText(jLabelMsg.getText() + "\n" + Messages.getString("SentenceEditionDialog.WarningMissingKanji") + " : '" + c + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									}
								}
							}
						});
					}
					{
						jLabelSon = new JLabel();
						jLabelSon.setText(Messages.getString("SentenceEditionDialog.LabelSound")); //$NON-NLS-1$
						jLabelSon.setPreferredSize(new java.awt.Dimension(100, 20));
						ajouterPhraseFormPanel.add(jLabelSon, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelSon.setSize(100, 20);
					}
					{
						jTextFieldSon = new JTextField();
						jTextFieldSon.setEditable(false);
						ajouterPhraseFormPanel.add(jTextFieldSon, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldSon.setSize(184, 20);
					}
					{
						jButtonParcourir = new JButton();
						jButtonParcourir.setText(Messages.getString("SentenceEditionDialog.ButtonBrowse")); //$NON-NLS-1$
						jButtonParcourir.setPreferredSize(new java.awt.Dimension(100, 20));
						ajouterPhraseFormPanel.add(jButtonParcourir, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonParcourir.setSize(100, 20);
						jButtonParcourir.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								JFileChooser fc = new JFileChooser();

								fc.setFileFilter(fileFilterSons);
								if (fc.showOpenDialog(KanjiNoSensei.getApp().getJFrame()) == JFileChooser.APPROVE_OPTION)
								{
									File fic = fc.getSelectedFile();
									jTextFieldSon.setText(fic.getAbsolutePath());
								}
							}
						});
					}
					{
						jLabelSignification = new JLabel();
						ajouterPhraseFormPanel.add(jLabelSignification, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelSignification.setText(Messages.getString("SentenceEditionDialog.LabelSignifications")); //$NON-NLS-1$
						jLabelSignification.setSize(69, 20);
					}
					{
						jTextFieldSignifications = new JTextField();
						ajouterPhraseFormPanel.add(jTextFieldSignifications, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldSignifications.setSize(184, 20);
					}
					{
						jLabelThemes = new JLabel();
						ajouterPhraseFormPanel.add(jLabelThemes, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelThemes.setText(Messages.getString("SentenceEditionDialog.LabelThemes")); //$NON-NLS-1$
						jLabelThemes.setSize(44, 20);
					}
					{
						jTextFieldThemes = new JTextField();
						ajouterPhraseFormPanel.add(jTextFieldThemes, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldThemes.setSize(184, 20);
					}
					{
						jLabelLecture = new JLabel();
						ajouterPhraseFormPanel.add(jLabelLecture, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
						jLabelLecture.setText(Messages.getString("SentenceEditionDialog.LabelLecture")); //$NON-NLS-1$
					}
					{
						jTextFieldLecture = new JTextField();
						ajouterPhraseFormPanel.add(jTextFieldLecture, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					jPanelBtns = new JPanel();
					GridBagLayout jPanelBtnsLayout = new GridBagLayout();
					ajouterPhraseContentPane.add(jPanelBtns, BorderLayout.SOUTH);
					jPanelBtns.setLayout(jPanelBtnsLayout);
					jPanelBtns.setPreferredSize(new java.awt.Dimension(422, 20));
					{
						jButtonValider = new JButton();
						jPanelBtns.add(jButtonValider, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 5), 0, 0));
						jButtonValider.setText(Messages.getString("SentenceEditionDialog.ButtonValidate")); //$NON-NLS-1$
						jButtonValider.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								if ( !jLabelMsg.getText().isEmpty())
								{
									if (JOptionPane.showConfirmDialog(null,
											Messages.getString("SentenceEditionDialog.WarningBoxAreYouSureToContinue"), Messages.getString("SentenceEditionDialog.WarningBoxTitle"), //$NON-NLS-1$ //$NON-NLS-2$
											JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
									{
										return;
									}
								}
								phraseEdite = true;
								vue.setPhrase(new Sentence(jTextFieldPhrase.getText(), jTextFieldLecture.getText(), jTextFieldSon
										.getText(), jTextFieldSignifications.getText(), jTextFieldThemes.getText()));
								dispose();
							}
						});
					}
					{
						jButtonAnnuler = new JButton();
						jPanelBtns.add(jButtonAnnuler, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
						jButtonAnnuler.setText(Messages.getString("SentenceEditionDialog.ButtonCancel")); //$NON-NLS-1$
						jButtonAnnuler.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								MyUtils.trace(Level.FINEST, "jButtonAnnuler.actionPerformed, event=" + evt); //$NON-NLS-1$
								dispose();
							}
						});
					}
				}
			}
			
			//<NoJigloo>
			Sentence phrase = vue.getSentence();
			if (phrase != null)
			{
				jTextFieldPhrase.setText(phrase.getSentence());
				jTextFieldLecture.setText(phrase.getLecture());
				jTextFieldSon.setText(phrase.getSoundFile());
				jTextFieldSignifications.setText(phrase.getSignifications());
				jTextFieldThemes.setText(phrase.getThemes());
				
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
		phraseEdite = false;
		pack();
		setVisible(true);
		return phraseEdite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.EditionDialog#getElementEdite()
	 */
	@Override
	public Element getElementEdite()
	{
		return vue.getSentence();
	}

}
