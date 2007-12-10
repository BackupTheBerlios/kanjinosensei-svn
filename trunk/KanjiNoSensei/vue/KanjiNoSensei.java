/**
 * 
 */
package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import metier.Dictionnaire;
import metier.Dictionnaire.DictionnaireNoMoreElementException;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Mot;
import utils.MyUtils;
import vue.VueElement.NoAffException;
import vue.VueElement.NoSaisieException;
import vue.VueElement.QuizConfigPanel;
import vue.VueElement.VueDetaillePanel;

import com.jgoodies.forms.layout.FormLayout;

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
/**
 * @author Axan
 * 
 */
public class KanjiNoSensei
{
	final KanjiNoSensei			app								= this;
	private Set<String>			themesSelectionnes				= new TreeSet<String>();				// @jve:decl-index=0:
	private JDialog				jConfigDialog;
	private JMenuItem			ConfigMenuItem;

	static private FileFilter	fileFilterDictionnaire			= MyUtils.generateFileFilter(
																		"Dictionnaire de 漢字", "kjd");
	static private FileFilter	fileFilterDictionnaireExport	= MyUtils.generateFileFilter(
																		"Dictionnaire exporté", "csv");

	private JFrame				jFrame							= null;

	private JPanel				jContentPane					= null;

	private JMenuBar			jJMenuBar						= null;

	private JMenu				fileMenu						= null;

	private JMenu				editMenu						= null;

	private JMenu				helpMenu						= null;

	private JMenuItem			exitMenuItem					= null;

	private JMenuItem			aboutMenuItem					= null;

	private JFrame				aboutDialog						= null;								// @jve:decl-index=0:visual-constraint="433,15"

	private JPanel				aboutContentPane				= null;

	private JLabel				aboutVersionLabel				= null;

	private JMenuItem			ajouterKanjiMenuItem			= null;

	private JDialog				afficherBaseDialog				= null;								// @jve:decl-index=0:visual-constraint="23,536"

	private JPanel				afficherBaseContentPane			= null;

	private JScrollPane			jScrollPaneThemes				= null;

	private JPanel				jPanelThemesFiltres				= null;

	private JLabel				jLabelRechercher				= null;

	private JTextField			jTextFieldThemesFiltre			= null;

	private JCheckBox			jCheckBoxThemesFiltrer			= null;

	private JButton				jButtonSelectionnerTous			= null;

	private JButton				jButtonDeselectionner			= null;
	private JMenuItem			importMenuItem;
	private JMenuItem			exportDicoMenuItem;
	private JMenuItem			ajouterMotMenuItem;
	private JMenuItem			lancerQuizMenuItem;
	private JPanel				jPanelConfigGenerale;
	private JPanel				jPanelQuizSaisieReponse;
	private JPanel				jPanelQuizAffElement;
	private JPanel				jPanelQuizAffReponse;
	private JScrollPane			jScrollPaneMiddle;
	private JScrollPane			jScrollPaneTop;
	private JScrollPane			jScrollPaneBottom;
	private JSplitPane			jSplitPaneTopMiddle;
	private JSplitPane			jSplitPaneTopMiddleBottom;
	private JDialog				jQuizDialog;
	private JButton				jButton1;
	private JButton				jButtonValider;
	private JButton				jButtonOK;
	private JButton				jButtonAnnuler;
	private JTabbedPane			jConfigDialogTabbedPane;
	private JPanel				jPanelConfigBtns;

	/** Dictionnaire complet disponible */
	final private Dictionnaire	dictionnaire;

	final private VueElement	vueKanjiBlank;
	final private VueElement	vueMotBlank;

	public Dictionnaire getDictionnaire()
	{
		return dictionnaire.clone();
	}

	private JMenuItem		saveDictionnaireMenuItem	= null;

	private JMenuItem		afficherBaseMenuItem		= null;

	private JSplitPane		jSplitPaneCentre			= null;

	private JPanel			jPanelThemesInSplit			= null;

	private JPanel			jPanelElementsInSplit		= null;

	private JScrollPane		jScrollPaneElements			= null;

	private JPanel			jPanelElementsListe			= null;

	private JPanel			jPanelThemesListe			= null;

	private JPanel			jPanelElementsFiltres		= null;

	private JPanel			jPanelBaseBtns				= null;

	private JLabel			jLabel						= null;

	private JTextField		jTextFieldElementsFiltre	= null;

	private JCheckBox		jCheckBoxElementsFiltre		= null;

	/** Sous ensemble sélectionné dans le dictionnaire complet */
	private Dictionnaire	sousDictionnaire			= null;	// @jve:decl-index=0:
	/** Sous ensemble de départ du quiz */
	private Dictionnaire	dictionnaireQuiz			= null;
	/** Sous ensemble courant (restant) du quiz */
	private Dictionnaire	dictionnaireQuizEnCours		= null;

	private String			filtreThemesTxt				= null;
	private boolean			filtreThemesActif			= false;
	private String			filtreElementsTxt			= null;
	private boolean			filtreElementsActif			= false;

	private VueElement		elementSelectionne			= null;	// @jve:decl-index=0:

	private JSplitPane		jSplitPaneThemes			= null;

	private JPanel			jPanelThemesSelection		= null;

	private JList			jListThemesSelectionnes		= null;
	private int				questionCourante			= 0;
	private int				bonnesReponses				= 0;
	private int				erreurs						= 0;
	private Element			elementQuestionEnCours		= null;
	private VueElement		vueElementQuestionEnCours	= null;

	private Color			defaultBgColor;

	public KanjiNoSensei(File fic_dico) throws SecurityException, IllegalArgumentException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException,
			IOException
	{
		this.vueKanjiBlank = VueElement.genererVueElement(this, Kanji.BLANK);
		this.vueMotBlank = VueElement.genererVueElement(this, Mot.BLANK);

		this.dictionnaire = new Dictionnaire(fic_dico);
		this.dictionnaireQuiz = this.dictionnaire.clone();
	}

	/**
	 * This method initializes ajouterKanjiMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAjouterKanjiMenuItem()
	{
		if (ajouterKanjiMenuItem == null)
		{
			ajouterKanjiMenuItem = new JMenuItem();
			ajouterKanjiMenuItem.setText("Ajouter 漢字");
			ajouterKanjiMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					Kanji nouveau_kanji = (Kanji) vueKanjiBlank.editerElement();

					if (nouveau_kanji != null)
					{
						System.out.println("漢字 edité : '" + nouveau_kanji + "'");
						dictionnaire.ajouterElement(nouveau_kanji);
					}
				}
			});
		}
		return ajouterKanjiMenuItem;
	}

	/**
	 * This method initializes afficherBaseDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JDialog getAfficherBaseDialog()
	{
		if (afficherBaseDialog == null)
		{
			Dimension d = new Dimension(800, 600);
			afficherBaseDialog = new JDialog(getJFrame());
			afficherBaseDialog.setSize(d);
			afficherBaseDialog.setTitle("Affichage de la base de connaissance");
			afficherBaseDialog.setMinimumSize(d);
			afficherBaseDialog.setPreferredSize(d);
			afficherBaseDialog.setModal(true);
			afficherBaseDialog.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			// <JiglooProtected>
			afficherBaseDialog.addComponentListener(new ComponentAdapter()
			{
				// </JiglooProtected>
				public void componentShown(ComponentEvent evt)
				{
					System.out.println("afficherBaseDialog.componentShown, event=" + evt);

					getJTextFieldThemesFiltre().setText(filtreThemesTxt);
					getJCheckBoxThemesFiltrer().setSelected(filtreThemesActif);
					getJTextFieldElementsFiltre().setText(filtreElementsTxt);
					getJCheckBoxElementsFiltre().setSelected(filtreElementsActif);

					afficherBaseDialogMAJ();
				}
			});
			afficherBaseDialog.setContentPane(getAfficherBaseContentPane());
			// <JiglooProtected>
			afficherBaseDialog.addWindowListener(new WindowAdapter()
			{
				// </JiglooProtected>
				public void windowOpened(WindowEvent e)
				{
					getJTextFieldThemesFiltre().setText(filtreThemesTxt);
					getJCheckBoxThemesFiltrer().setSelected(filtreThemesActif);
					getJTextFieldElementsFiltre().setText(filtreElementsTxt);
					getJCheckBoxElementsFiltre().setSelected(filtreElementsActif);

					afficherBaseDialogMAJ();
				}
			});
		}

		return afficherBaseDialog;
	}

	private void afficherBaseDialogMAJ()
	{
		getJPanelThemesListe().removeAll();

		String filtreTheme = jTextFieldThemesFiltre.getText();

		if ( !jCheckBoxThemesFiltrer.isSelected())
		{
			filtreTheme = null;
		}

		Set<String> listeThemesFiltre = dictionnaire.listerThemes(filtreTheme);

		Iterator<String> itThemes = listeThemesFiltre.iterator();

		while (itThemes.hasNext())
		{
			String theme = itThemes.next();

			JCheckBox jCheckBoxTheme = new JCheckBox();
			jCheckBoxTheme.setText(theme);

			jCheckBoxTheme.setSelected(themesSelectionnes.contains(theme));
			jCheckBoxTheme.addItemListener(new ItemListener()
			{

				public void itemStateChanged(ItemEvent e)
				{
					JCheckBox jCheckBoxTheme = (JCheckBox) e.getItem();
					String theme = jCheckBoxTheme.getText();

					if (e.getStateChange() == ItemEvent.SELECTED)
					{
						System.out.println("'" + theme + "' ajouté");
						themesSelectionnes.add(theme);
					}
					else
					{
						if (themesSelectionnes.contains(theme))
						{
							System.out.println("'" + theme + "' retiré");
							themesSelectionnes.remove(theme);
						}
					}

					afficherBaseDialogMAJ();
				}

			});

			getJPanelThemesListe().add(jCheckBoxTheme);
		}

		getJPanelThemesListe().setPreferredSize(new Dimension(0, getJPanelThemesListe().getComponentCount() * 20));
		getJPanelThemesListe().setVisible(false);
		getJPanelThemesListe().setVisible(true);

		sousDictionnaire = dictionnaire.getSousDictionnaire(themesSelectionnes);

		getJPanelElementsListe().removeAll();

		String filtreElement = jTextFieldElementsFiltre.getText();

		if ( !jCheckBoxElementsFiltre.isSelected())
		{
			filtreElement = null;
		}

		Set<Element> listeElementsFiltre = sousDictionnaire.listerElements(filtreElement);
		Iterator<Element> itElements = listeElementsFiltre.iterator();
		Element element = null;
		while (itElements.hasNext())
		{
			element = itElements.next();
			try
			{
				final VueElement vueElement = VueElement.genererVueElement(this, element);
				JPanel vueElementDetaillePanel = vueElement.getVueDetaillePanel().getPanel();
				vueElementDetaillePanel.addMouseListener(new MouseAdapter()
				{
				
					@Override
					public void mouseClicked(MouseEvent e)
					{
						System.out.println("vueElementDetaillePanel.mouseClicked(): "+e);
						System.out.println("e.isConsumed() == "+e.isConsumed());
					
						if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 1))
						{
							e.consume();
							System.out.println("Simple Left Click");
							afficherBaseDialogMAJSelection(vueElement);
						}
						else if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
						{
							e.consume();
							System.out.println("Double Left Click");
							Element ancienElement = vueElement.getElement();
							Element nouveauElement = vueElement.editerElement();
							if (nouveauElement != null)
							{
								dictionnaire.retirerElement(ancienElement);
								dictionnaire.ajouterElement(nouveauElement);
							}
							
							afficherBaseDialogMAJ();
						}
						
						super.mouseClicked(e);
					}
				
				});
				
				getJPanelElementsListe().add(vueElementDetaillePanel);
			}
			catch(Exception e)
			{
				System.err.println("Erreur de création de la vue de l'élément \"" + element.toString()
						+ "\"");
			}
		}

		getJListThemesSelectionnes().setListData(themesSelectionnes.toArray());

		getJPanelElementsListe()
				.setPreferredSize(
						new Dimension(0, getJPanelElementsListe().getComponentCount()
								* VueElement.getVueDetailleHeight() + 20));
		getJPanelElementsListe().setVisible(false);
		getJPanelElementsListe().setVisible(true);
	}

	public void afficherBaseDialogMAJSelection(VueElement nouvelleSelection)
	{
		boolean deselectionner = false, selectionner = false;

		for (int i = 0; i < getJPanelElementsListe().getComponentCount(); ++i)
		{
			JPanel panel = ((VueDetaillePanel) getJPanelElementsListe().getComponent(i)).getPanel();

			if ((elementSelectionne != null) && (elementSelectionne.getVueDetaillePanel().equals(panel)))
			{
				panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				deselectionner = true;
			}

			if (nouvelleSelection.getVueDetaillePanel().equals(panel))
			{
				panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				selectionner = true;
			}

			if (selectionner && deselectionner) break;
		}

		elementSelectionne = nouvelleSelection;
	}

	/**
	 * This method initializes afficherBaseContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAfficherBaseContentPane()
	{
		if (afficherBaseContentPane == null)
		{
			afficherBaseContentPane = new JPanel();
			afficherBaseContentPane.setLayout(new BorderLayout());
			afficherBaseContentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			afficherBaseContentPane.add(getJSplitPaneCentre(), BorderLayout.CENTER);
			afficherBaseContentPane.add(getJPanelBaseBtns(), BorderLayout.SOUTH);
		}
		return afficherBaseContentPane;
	}

	/**
	 * This method initializes jScrollPaneThemes
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneThemes()
	{
		if (jScrollPaneThemes == null)
		{
			ScrollPaneLayout gridLayout = new ScrollPaneLayout();
			gridLayout.setHorizontalScrollBarPolicy(ScrollPaneLayout.HORIZONTAL_SCROLLBAR_ALWAYS);
			gridLayout.setVerticalScrollBarPolicy(ScrollPaneLayout.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPaneThemes = new JScrollPane();
			jScrollPaneThemes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPaneThemes.setBorder(null);
			jScrollPaneThemes.setLayout(gridLayout);
			jScrollPaneThemes.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jScrollPaneThemes.setViewportView(getJPanelThemesListe());
			jScrollPaneThemes.setPreferredSize(new Dimension(500, 240));
		}
		return jScrollPaneThemes;
	}

	/**
	 * This method initializes jPanelThemesFiltres
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelThemesFiltres()
	{
		if (jPanelThemesFiltres == null)
		{
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 4;
			gridBagConstraints19.anchor = GridBagConstraints.EAST;
			gridBagConstraints19.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints19.gridy = 0;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 3;
			gridBagConstraints18.anchor = GridBagConstraints.EAST;
			gridBagConstraints18.gridheight = 2;
			gridBagConstraints18.insets = new Insets(0, 20, 0, 0);
			gridBagConstraints18.weightx = 4.0D;
			gridBagConstraints18.gridy = 0;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 2;
			gridBagConstraints17.fill = GridBagConstraints.NONE;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.weightx = 2.0D;
			gridBagConstraints17.gridy = 0;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.gridwidth = 1;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.insets = new Insets(0, 0, 0, 20);
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.insets = new Insets(0, 0, 0, 20);
			gridBagConstraints15.gridy = 0;
			jLabelRechercher = new JLabel();
			jLabelRechercher.setText("Rechercher");
			jPanelThemesFiltres = new JPanel();
			jPanelThemesFiltres.setLayout(new GridBagLayout());
			jPanelThemesFiltres.setPreferredSize(new Dimension(50, 50));
			jPanelThemesFiltres.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelThemesFiltres.add(jLabelRechercher, gridBagConstraints15);
			jPanelThemesFiltres.add(getJTextFieldThemesFiltre(), gridBagConstraints16);
			jPanelThemesFiltres.add(getJCheckBoxThemesFiltrer(), gridBagConstraints17);
			jPanelThemesFiltres.add(getJButtonSelectionnerTous(), gridBagConstraints18);
			jPanelThemesFiltres.add(getJButtonDeselectionner(), gridBagConstraints19);
		}
		return jPanelThemesFiltres;
	}

	/**
	 * This method initializes jTextFieldThemesFiltre
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldThemesFiltre()
	{
		if (jTextFieldThemesFiltre == null)
		{
			jTextFieldThemesFiltre = new JTextField();
			jTextFieldThemesFiltre.setPreferredSize(new Dimension(100, 20));
			jTextFieldThemesFiltre.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jTextFieldThemesFiltre.setHorizontalAlignment(JTextField.LEADING);
			jTextFieldThemesFiltre.addCaretListener(new javax.swing.event.CaretListener()
			{
				public void caretUpdate(javax.swing.event.CaretEvent e)
				{
					jCheckBoxThemesFiltrer.setSelected( !jTextFieldThemesFiltre.getText().isEmpty());
					afficherBaseDialogMAJ();
				}
			});
		}
		return jTextFieldThemesFiltre;
	}

	/**
	 * This method initializes jCheckBoxThemesFiltrer
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxThemesFiltrer()
	{
		if (jCheckBoxThemesFiltrer == null)
		{
			jCheckBoxThemesFiltrer = new JCheckBox();
			jCheckBoxThemesFiltrer.setText("Filtre actif");
			jCheckBoxThemesFiltrer.addItemListener(new java.awt.event.ItemListener()
			{
				public void itemStateChanged(java.awt.event.ItemEvent e)
				{
					afficherBaseDialogMAJ();
				}
			});
		}
		return jCheckBoxThemesFiltrer;
	}

	/**
	 * This method initializes jButtonSelectionnerTous
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSelectionnerTous()
	{
		if (jButtonSelectionnerTous == null)
		{
			jButtonSelectionnerTous = new JButton();
			jButtonSelectionnerTous.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jButtonSelectionnerTous.setText("Tous");
			jButtonSelectionnerTous.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					for (int i = 0; i < getJPanelThemesListe().getComponentCount(); ++i)
					{
						JCheckBox checkboxTheme = (JCheckBox) getJPanelThemesListe().getComponent(i);
						checkboxTheme.setSelected(true);
					}
				}
			});
		}
		return jButtonSelectionnerTous;
	}

	/**
	 * This method initializes jButtonDeselectionner
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDeselectionner()
	{
		if (jButtonDeselectionner == null)
		{
			jButtonDeselectionner = new JButton();
			jButtonDeselectionner.setText("Aucun");
			jButtonDeselectionner.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					for (int i = 0; i < getJPanelThemesListe().getComponentCount(); ++i)
					{
						JCheckBox checkboxTheme = (JCheckBox) getJPanelThemesListe().getComponent(i);
						checkboxTheme.setSelected(false);
					}
				}
			});
		}
		return jButtonDeselectionner;
	}

	/**
	 * This method initializes saveDictionnaireMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveDictionnaireMenuItem()
	{
		if (saveDictionnaireMenuItem == null)
		{
			saveDictionnaireMenuItem = new JMenuItem();
			saveDictionnaireMenuItem.setText("Enregistrer le dictionnaire");
			saveDictionnaireMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					JFileChooser fc = new JFileChooser();

					fc.setFileFilter(fileFilterDictionnaire);

					boolean retry = false;

					do
					{
						if (fc.showSaveDialog(getJFrame()) == JFileChooser.APPROVE_OPTION)
						{
							File fic = fc.getSelectedFile();
							try
							{
								dictionnaire.enregistrer(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null,
										"Erreur lors de l'enregistrement, voulez vous ré-essayer ?",
										"Erreur de fichier", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION);
							}
						}
					} while (retry);
				}
			});
		}
		return saveDictionnaireMenuItem;
	}

	/**
	 * This method initializes afficherBaseMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAfficherBaseMenuItem()
	{
		if (afficherBaseMenuItem == null)
		{
			afficherBaseMenuItem = new JMenuItem();
			afficherBaseMenuItem.setText("Afficher la base de connaissance");
			afficherBaseMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					JDialog afficherBaseDialog = getAfficherBaseDialog();
					afficherBaseDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					afficherBaseDialog.setLocation(loc);
					afficherBaseDialog.setVisible(true);
				}
			});
		}
		return afficherBaseMenuItem;
	}

	/**
	 * This method initializes jSplitPaneCentre
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPaneCentre()
	{
		if (jSplitPaneCentre == null)
		{
			jSplitPaneCentre = new JSplitPane();
			jSplitPaneCentre.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jSplitPaneCentre.setOneTouchExpandable(true);
			jSplitPaneCentre.setPreferredSize(new Dimension(0, 0));
			jSplitPaneCentre.setDividerLocation(300);
			jSplitPaneCentre.setTopComponent(getJPanelThemesInSplit());
			jSplitPaneCentre.setBottomComponent(getJPanelElementsInSplit());
			jSplitPaneCentre.setOrientation(JSplitPane.VERTICAL_SPLIT);
		}
		return jSplitPaneCentre;
	}

	/**
	 * This method initializes jPanelThemesInSplit
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelThemesInSplit()
	{
		if (jPanelThemesInSplit == null)
		{
			jPanelThemesInSplit = new JPanel();
			jPanelThemesInSplit.setLayout(new BorderLayout());
			jPanelThemesInSplit.setPreferredSize(new Dimension(500, 500));
			jPanelThemesInSplit.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelThemesInSplit.add(getJPanelThemesFiltres(), BorderLayout.NORTH);
			jPanelThemesInSplit.add(getJSplitPaneThemes(), BorderLayout.CENTER);
		}
		return jPanelThemesInSplit;
	}

	/**
	 * This method initializes jPanelElementsInSplit
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelElementsInSplit()
	{
		if (jPanelElementsInSplit == null)
		{
			jPanelElementsInSplit = new JPanel();
			jPanelElementsInSplit.setLayout(new BorderLayout());
			jPanelElementsInSplit.add(getJPanelElementsFiltres(), BorderLayout.NORTH);
			jPanelElementsInSplit.add(getJScrollPaneElements(), BorderLayout.CENTER);
		}
		return jPanelElementsInSplit;
	}

	/**
	 * This method initializes jScrollPaneElements
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneElements()
	{
		if (jScrollPaneElements == null)
		{
			jScrollPaneElements = new JScrollPane();
			jScrollPaneElements.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jScrollPaneElements.setPreferredSize(new Dimension(0, 0));
			jScrollPaneElements.setViewportView(getJPanelElementsListe());
		}
		return jScrollPaneElements;
	}

	/**
	 * This method initializes jPanelElementsListe
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelElementsListe()
	{
		if (jPanelElementsListe == null)
		{
			GridLayout gridLayout2 = new GridLayout();
			gridLayout2.setColumns(1);
			gridLayout2.setRows(0);
			jPanelElementsListe = new JPanel();
			jPanelElementsListe.setLocation(new Point(0, 0));
			jPanelElementsListe.setSize(new Dimension(0, 0));
			jPanelElementsListe.setLayout(gridLayout2);
			jPanelElementsListe.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		return jPanelElementsListe;
	}

	/**
	 * This method initializes jPanelThemesListe
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelThemesListe()
	{
		if (jPanelThemesListe == null)
		{
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setColumns(1);
			gridLayout1.setRows(0);
			jPanelThemesListe = new JPanel();
			jPanelThemesListe.setLocation(new Point(0, 0));
			jPanelThemesListe.setSize(new Dimension(0, 0));
			jPanelThemesListe.setLayout(gridLayout1);
			jPanelThemesListe.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		return jPanelThemesListe;
	}

	/**
	 * This method initializes jPanelElementsFiltres
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelElementsFiltres()
	{
		if (jPanelElementsFiltres == null)
		{
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 2;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.weightx = 10.0D;
			gridBagConstraints22.gridy = 0;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.insets = new Insets(0, 0, 0, 20);
			gridBagConstraints21.gridx = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new Insets(0, 0, 0, 20);
			gridBagConstraints20.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Rechercher");
			jLabel.setPreferredSize(new Dimension(67, 16));
			jPanelElementsFiltres = new JPanel();
			jPanelElementsFiltres.setLayout(new GridBagLayout());
			jPanelElementsFiltres.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelElementsFiltres.setPreferredSize(new Dimension(50, 50));
			jPanelElementsFiltres.add(jLabel, gridBagConstraints20);
			jPanelElementsFiltres.add(getJTextFieldElementsFiltre(), gridBagConstraints21);
			jPanelElementsFiltres.add(getJCheckBoxElementsFiltre(), gridBagConstraints22);
		}
		return jPanelElementsFiltres;
	}

	/**
	 * This method initializes jPanelBaseBtns
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelBaseBtns()
	{
		if (jPanelBaseBtns == null)
		{
			jPanelBaseBtns = new JPanel();
			FlowLayout jPanelBaseBtnsLayout = new FlowLayout();
			jPanelBaseBtns.setLayout(jPanelBaseBtnsLayout);
			jPanelBaseBtns.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelBaseBtns.setPreferredSize(new Dimension(30, 30));
			jPanelBaseBtns.add(getJButtonValider());
			jPanelBaseBtns.add(getJButton1());
		}
		return jPanelBaseBtns;
	}

	/**
	 * This method initializes jTextFieldElementsFiltre
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldElementsFiltre()
	{
		if (jTextFieldElementsFiltre == null)
		{
			jTextFieldElementsFiltre = new JTextField();
			jTextFieldElementsFiltre.setPreferredSize(new Dimension(100, 20));
			jTextFieldElementsFiltre.addCaretListener(new javax.swing.event.CaretListener()
			{
				public void caretUpdate(javax.swing.event.CaretEvent e)
				{
					jCheckBoxElementsFiltre.setSelected( !jTextFieldElementsFiltre.getText().isEmpty());
					afficherBaseDialogMAJ();
				}
			});
		}
		return jTextFieldElementsFiltre;
	}

	/**
	 * This method initializes jCheckBoxElementsFiltre
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxElementsFiltre()
	{
		if (jCheckBoxElementsFiltre == null)
		{
			jCheckBoxElementsFiltre = new JCheckBox();
			jCheckBoxElementsFiltre.setText("Filtre actif");
			jCheckBoxElementsFiltre.addItemListener(new java.awt.event.ItemListener()
			{
				public void itemStateChanged(java.awt.event.ItemEvent e)
				{
					afficherBaseDialogMAJ();
				}
			});
		}
		return jCheckBoxElementsFiltre;
	}

	/**
	 * This method initializes jSplitPaneThemes
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPaneThemes()
	{
		if (jSplitPaneThemes == null)
		{
			jSplitPaneThemes = new JSplitPane();
			jSplitPaneThemes.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jSplitPaneThemes.setDividerLocation(650);
			jSplitPaneThemes.setContinuousLayout(false);
			jSplitPaneThemes.setRightComponent(getJPanelThemesSelection());
			jSplitPaneThemes.setLeftComponent(getJScrollPaneThemes());
		}
		return jSplitPaneThemes;
	}

	/**
	 * This method initializes jPanelThemesSelection
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelThemesSelection()
	{
		if (jPanelThemesSelection == null)
		{
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.BOTH;
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.weighty = 1.0;
			gridBagConstraints23.gridx = 0;
			jPanelThemesSelection = new JPanel();
			jPanelThemesSelection.setLayout(new GridBagLayout());
			jPanelThemesSelection.add(getJListThemesSelectionnes(), gridBagConstraints23);
		}
		return jPanelThemesSelection;
	}

	/**
	 * This method initializes jListThemesSelectionnes
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListThemesSelectionnes()
	{
		if (jListThemesSelectionnes == null)
		{
			jListThemesSelectionnes = new JList();
			jListThemesSelectionnes.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jListThemesSelectionnes.setEnabled(false);
		}
		return jListThemesSelectionnes;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		final File ficDico;

		if (args.length > 0)
		{
			ficDico = new File(args[0]);
		}
		else
		{
			ficDico = null;
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				KanjiNoSensei application;
				try
				{
					application = new KanjiNoSensei(ficDico);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.err.println("Erreur de lancement de KanjiNoSensei : \"" + e.getMessage() + "\"");
					return;
				}

				application.getJFrame().setVisible(true);
			}
		});
	}

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	public JFrame getJFrame()
	{
		if (jFrame == null)
		{
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(300, 200);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Application");
			jFrame.setPreferredSize(new java.awt.Dimension(300, 200));
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar()
	{
		if (jJMenuBar == null)
		{
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu()
	{
		if (fileMenu == null)
		{
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getAfficherBaseMenuItem());
			fileMenu.add(getSaveDictionnaireMenuItem());
			fileMenu.add(getExportDicoMenuItem());
			fileMenu.add(getImportMenuItem());
			fileMenu.add(getConfigMenuItem());
			fileMenu.add(getLancerQuizMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getEditMenu()
	{
		if (editMenu == null)
		{
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getAjouterKanjiMenuItem());
			editMenu.add(getAjouterMotMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu()
	{
		if (helpMenu == null)
		{
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExitMenuItem()
	{
		if (exitMenuItem == null)
		{
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem()
	{
		if (aboutMenuItem == null)
		{
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jFrame.setFocusable(false);
					jFrame.setFocusableWindowState(false);
					jFrame.setVisible(false);
					jFrame.setVisible(true);
					JFrame aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
					aboutDialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes aboutDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JFrame getAboutDialog()
	{
		if (aboutDialog == null)
		{
			aboutDialog = new JFrame();
			// aboutDialog = new JDialog(getJFrame(), true);
			aboutDialog.setTitle("About");
			aboutDialog.setContentPane(getAboutContentPane());
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane()
	{
		if (aboutContentPane == null)
		{
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
		}
		return aboutContentPane;
	}

	/**
	 * This method initializes aboutVersionLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getAboutVersionLabel()
	{
		if (aboutVersionLabel == null)
		{
			aboutVersionLabel = new JLabel();
			aboutVersionLabel.setText("Version 1.0");
			aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return aboutVersionLabel;
	}

	private JMenuItem getConfigMenuItem()
	{
		if (ConfigMenuItem == null)
		{
			ConfigMenuItem = new JMenuItem();
			ConfigMenuItem.setText("Configurer");
			ConfigMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("ConfigMenuItem.actionPerformed, event=" + evt);

					jConfigDialog = getJConfigDialog();
					jConfigDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					jConfigDialog.setLocation(loc);
					jConfigDialog.setVisible(true);
				}
			});
		}
		return ConfigMenuItem;
	}

	private JDialog getJConfigDialog()
	{
		if (jConfigDialog == null)
		{
			jConfigDialog = new JDialog(getJFrame());
			jConfigDialog.setTitle("Configuration");
			jConfigDialog.setPreferredSize(new java.awt.Dimension(0, 0));
			jConfigDialog.setMinimumSize(new java.awt.Dimension(600, 300));
			jConfigDialog.setModal(true);
			jConfigDialog.addComponentListener(new ComponentAdapter()
			{
				// </JiglooProtected>
				public void componentShown(ComponentEvent evt)
				{
					System.out.println("jConfigDialog.componentShown, event=" + evt);

					for (Component configPanel : getJConfigDialogTabbedPane().getComponents())
					{
						configPanel.setVisible(false);
						configPanel.setVisible(true);
						configPanel.invalidate();
					}

					getJConfigDialogTabbedPane().setVisible(false);
					getJConfigDialogTabbedPane().setSelectedIndex(0);
					getJConfigDialogTabbedPane().setVisible(true);
					getJConfigDialogTabbedPane().invalidate();
				}
			});

			jConfigDialog.setSize(704, 178);
			jConfigDialog.getContentPane().add(getJPanelConfigBtns(), BorderLayout.SOUTH);
			jConfigDialog.getContentPane().add(getJConfigDialogTabbedPane(), BorderLayout.CENTER);

			JPanel configPanel = vueKanjiBlank.getQuizConfigPanel().getPanel();
			getJConfigDialogTabbedPane().add("漢字", configPanel);

			configPanel = vueMotBlank.getQuizConfigPanel().getPanel();
			getJConfigDialogTabbedPane().add("Mots", configPanel);

			configPanel.setDoubleBuffered(false);
		}
		return jConfigDialog;
	}

	private JPanel getJPanelConfigBtns()
	{
		if (jPanelConfigBtns == null)
		{
			jPanelConfigBtns = new JPanel();
			FlowLayout jPanel1Layout = new FlowLayout();
			jPanelConfigBtns.setLayout(jPanel1Layout);
			jPanelConfigBtns.setPreferredSize(new java.awt.Dimension(0, 32));
			jPanelConfigBtns.add(getJButtonOK());
			jPanelConfigBtns.add(getJButtonAnnuler());
		}
		return jPanelConfigBtns;
	}

	private JTabbedPane getJConfigDialogTabbedPane()
	{
		if (jConfigDialogTabbedPane == null)
		{
			jConfigDialogTabbedPane = new JTabbedPane();
			jConfigDialogTabbedPane.setPreferredSize(new java.awt.Dimension(0, 0));
			jConfigDialogTabbedPane.setAutoscrolls(true);
			jConfigDialogTabbedPane.setMinimumSize(new java.awt.Dimension(600, 300));
			jConfigDialogTabbedPane.addTab("Configuration générale", null, getJPanelConfigGenerale(), null);
		}
		return jConfigDialogTabbedPane;
	}

	private JButton getJButtonAnnuler()
	{
		if (jButtonAnnuler == null)
		{
			jButtonAnnuler = new JButton();
			BorderLayout jButtonAnnulerLayout = new BorderLayout();
			jButtonAnnuler.setLayout(jButtonAnnulerLayout);
			jButtonAnnuler.setText("Annuler");
			jButtonAnnuler.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("jButtonAnnuler.actionPerformed, event=" + evt);
					getJConfigDialog().dispose();
				}
			});
		}
		return jButtonAnnuler;
	}

	private JButton getJButtonOK()
	{
		if (jButtonOK == null)
		{
			jButtonOK = new JButton();
			jButtonOK.setText("Valider");
			jButtonOK.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("jButtonOK.actionPerformed, event=" + evt);

					for (Component component : getJConfigDialogTabbedPane().getComponents())
					{
						if (QuizConfigPanel.class.isAssignableFrom(component.getClass()))
						{
							QuizConfigPanel configPanel = QuizConfigPanel.class.cast(component);
							configPanel.valider();
						}
					}

					getJConfigDialog().dispose();
				}

			});
		}
		return jButtonOK;
	}

	private JButton getJButtonValider()
	{
		if (jButtonValider == null)
		{
			jButtonValider = new JButton();
			jButtonValider.setText("Valider");
			jButtonValider.setMargin(new java.awt.Insets(2, 14, 0, 14));
			jButtonValider.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("jButtonValider.actionPerformed, event=" + evt);
					dictionnaireQuiz = sousDictionnaire;
					getAfficherBaseDialog().dispose();
				}
			});
		}
		return jButtonValider;
	}

	private JButton getJButton1()
	{
		if (jButton1 == null)
		{
			jButton1 = new JButton();
			jButton1.setText("Annuler");
			jButton1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("jButton1.actionPerformed, event=" + evt);
					getAfficherBaseDialog().dispose();
				}
			});
		}
		return jButton1;
	}

	private JDialog getJQuizDialog()
	{
		if (jQuizDialog == null)
		{
			jQuizDialog = new JDialog();
			jQuizDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			jQuizDialog.setPreferredSize(new java.awt.Dimension(600, 480));
			jQuizDialog.setTitle("Quiz");
			jQuizDialog.setModal(true);
			jQuizDialog.setName("Quiz");
			// <JiglooProtected>
			jQuizDialog.addWindowListener(new WindowAdapter()
			{
				// </JiglooProtected>
				public void windowOpened(WindowEvent evt)
				{
					System.out.println("jQuizDialog.windowOpened, event=" + evt);
				}
			});
			jQuizDialog.getContentPane().add(getJSplitPaneTopMiddleBottom(), BorderLayout.CENTER);
			jQuizDialog.setSize(600, 480);
		}
		return jQuizDialog;
	}

	/**
	 * 
	 */
	protected synchronized void poserQuestionSuivanteQuiz()
	{
		getJPanelQuizAffElement().removeAll();

		Vector<Element> dejaVus = new Vector<Element>();
		JPanel panelQuestion = null;
		JPanel panelSaisieReponse = null;

		do
		{
			try
			{
				elementQuestionEnCours = dictionnaireQuizEnCours.getRandomElement(dejaVus);
				vueElementQuestionEnCours = VueElement.genererVueElement(this, elementQuestionEnCours);
			}
			catch (DictionnaireNoMoreElementException e1)
			{
				finirQuiz();
				return;
			}
			catch (Exception e)
			{
				panelQuestion = null;
				panelSaisieReponse = null;
				dejaVus.add(elementQuestionEnCours);

				e.printStackTrace();
				System.err.println("Erreur de création de la vue de l'élément \"" + elementQuestionEnCours.toString()
						+ "\"");
				continue;
			}

			try
			{
				panelQuestion = vueElementQuestionEnCours.getQuizQuestionPanel().getPanel();
				panelSaisieReponse = vueElementQuestionEnCours.getQuizSaisieReponsePanel(dictionnaire).getPanel();
			}
			catch (NoAffException e)
			{
				panelQuestion = null;
				panelSaisieReponse = null;
				dejaVus.add(elementQuestionEnCours);
			}
			catch (NoSaisieException e)
			{
				panelQuestion = null;
				panelSaisieReponse = null;
				dejaVus.add(elementQuestionEnCours);
			}

		} while ((panelQuestion == null) || (panelSaisieReponse == null));

		panelQuestion.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
		getJPanelQuizAffElement().add(panelQuestion, BorderLayout.CENTER);

		getJPanelQuizSaisieReponse().removeAll();
		panelSaisieReponse.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
		getJPanelQuizSaisieReponse().add(panelSaisieReponse, BorderLayout.CENTER);

		getJPanelQuizAffReponse().removeAll();
		getJPanelQuizAffReponse().setBackground(defaultBgColor);

		miseAJourQuizTitle();
		getJQuizDialog().setVisible(false);
		getJQuizDialog().setVisible(true);
	}

	private void miseAJourQuizTitle()
	{
		getJQuizDialog().setTitle(
				"Quiz: question n°" + questionCourante + "\tbonnes réponses : " + bonnesReponses
						+ "\tmauvaises réponses : " + erreurs);
	}

	public synchronized void validerReponseQuiz(boolean reponseCorrecte)
	{
		Color bg = null;
		++questionCourante;
		if (reponseCorrecte)
		{
			++bonnesReponses;
			dictionnaireQuizEnCours.retirerElement(elementQuestionEnCours);
			bg = Color.GREEN;
		}
		else
		{
			++erreurs;
			bg = Color.RED;
		}

		getJPanelQuizAffReponse().setBackground(bg);
		miseAJourQuizTitle();
		
		MyUtils.lockPanel(getJPanelQuizSaisieReponse());

		JPanel panelSolution = vueElementQuestionEnCours.getQuizSolutionPanel().getPanel();
		jPanelQuizAffReponse.add(panelSolution, BorderLayout.CENTER);
		final JButton buttonContinuer = new JButton("Suivant");
		buttonContinuer.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				poserQuestionSuivanteQuiz();
			}

		});
		jPanelQuizAffReponse.add(buttonContinuer, BorderLayout.SOUTH);
		getJPanelQuizAffReponse().setVisible(false);
		getJPanelQuizAffReponse().setVisible(true);
	}
	
	protected void finirQuiz()
	{
		String msg = String.format("Nombre de questions :\t%d\nBonnes réponses :\t%d\nMauvaises réponses :\t%d",
				questionCourante, bonnesReponses, erreurs);
		JOptionPane.showMessageDialog(null, msg, "Quiz terminé", JOptionPane.INFORMATION_MESSAGE);
		getJQuizDialog().dispose();
	}

	/**
	 * Initialise les variables utile au lancement d'un Quiz.
	 */
	protected void initialiserQuiz()
	{
		dictionnaireQuizEnCours = dictionnaireQuiz.clone();
		questionCourante = 0;
		bonnesReponses = 0;
		erreurs = 0;
	}

	private JSplitPane getJSplitPaneTopMiddleBottom()
	{
		if (jSplitPaneTopMiddleBottom == null)
		{
			jSplitPaneTopMiddleBottom = new JSplitPane();
			jSplitPaneTopMiddleBottom.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneTopMiddleBottom.setPreferredSize(new java.awt.Dimension(0, 0));
			jSplitPaneTopMiddleBottom.setDividerLocation(200);
			jSplitPaneTopMiddleBottom.setSize(0, 0);
			jSplitPaneTopMiddleBottom.setDividerSize(10);
			jSplitPaneTopMiddleBottom.setMinimumSize(new java.awt.Dimension(0, 0));
			jSplitPaneTopMiddleBottom.add(getJSplitPaneTopMiddle(), JSplitPane.TOP);
			jSplitPaneTopMiddleBottom.add(getJScrollPaneBottom(), JSplitPane.BOTTOM);
		}
		return jSplitPaneTopMiddleBottom;
	}

	private JSplitPane getJSplitPaneTopMiddle()
	{
		if (jSplitPaneTopMiddle == null)
		{
			jSplitPaneTopMiddle = new JSplitPane();
			jSplitPaneTopMiddle.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneTopMiddle.setDividerLocation(100);
			jSplitPaneTopMiddle.add(getJScrollPaneTop(), JSplitPane.TOP);
			jSplitPaneTopMiddle.add(getJScrollPaneMiddle(), JSplitPane.BOTTOM);
		}
		return jSplitPaneTopMiddle;
	}

	private JScrollPane getJScrollPaneBottom()
	{
		if (jScrollPaneBottom == null)
		{
			jScrollPaneBottom = new JScrollPane();
			jScrollPaneBottom.setViewportView(getJPanelQuizAffReponse());
		}
		return jScrollPaneBottom;
	}

	private JScrollPane getJScrollPaneTop()
	{
		if (jScrollPaneTop == null)
		{
			jScrollPaneTop = new JScrollPane();
			jScrollPaneTop.setViewportView(getJPanelQuizAffElement());
		}
		return jScrollPaneTop;
	}

	private JScrollPane getJScrollPaneMiddle()
	{
		if (jScrollPaneMiddle == null)
		{
			jScrollPaneMiddle = new JScrollPane();
			jScrollPaneMiddle.setViewportView(getJPanelQuizSaisieReponse());
		}
		return jScrollPaneMiddle;
	}

	private JPanel getJPanelQuizAffReponse()
	{
		if (jPanelQuizAffReponse == null)
		{
			jPanelQuizAffReponse = new JPanel();
			BorderLayout jPanelQuizAffReponseLayout = new BorderLayout();
			jPanelQuizAffReponse.setLayout(jPanelQuizAffReponseLayout);
			defaultBgColor = jPanelQuizAffReponse.getBackground();
		}
		return jPanelQuizAffReponse;
	}

	private JPanel getJPanelQuizAffElement()
	{
		if (jPanelQuizAffElement == null)
		{
			jPanelQuizAffElement = new JPanel();
			BorderLayout jPanelQuizAffElementLayout = new BorderLayout();
			jPanelQuizAffElement.setLayout(jPanelQuizAffElementLayout);
		}
		return jPanelQuizAffElement;
	}

	private JPanel getJPanelQuizSaisieReponse()
	{
		if (jPanelQuizSaisieReponse == null)
		{
			jPanelQuizSaisieReponse = new JPanel();
			BorderLayout jPanelQuizSaisieReponseLayout = new BorderLayout();
			jPanelQuizSaisieReponse.setLayout(jPanelQuizSaisieReponseLayout);
		}
		return jPanelQuizSaisieReponse;
	}

	private JPanel getJPanelConfigGenerale()
	{
		if (jPanelConfigGenerale == null)
		{
			jPanelConfigGenerale = new JPanel();
			FormLayout jPanelConfigGeneraleLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)",
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			jPanelConfigGenerale.setLayout(jPanelConfigGeneraleLayout);
			jPanelConfigGenerale.setDoubleBuffered(false);
		}
		return jPanelConfigGenerale;
	}

	private JMenuItem getLancerQuizMenuItem()
	{
		if (lancerQuizMenuItem == null)
		{
			lancerQuizMenuItem = new JMenuItem();
			lancerQuizMenuItem.setText("Lancer Quiz");
			lancerQuizMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("lancerQuizMenuItem.actionPerformed, event=" + evt);

					jQuizDialog = getJQuizDialog();
					jQuizDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					jQuizDialog.setLocation(loc);
					initialiserQuiz();
					poserQuestionSuivanteQuiz();
				}
			});
		}
		return lancerQuizMenuItem;
	}

	private JMenuItem getAjouterMotMenuItem()
	{
		if (ajouterMotMenuItem == null)
		{
			ajouterMotMenuItem = new JMenuItem();
			ajouterMotMenuItem.setText("Ajouter un mot");
			ajouterMotMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					Mot nouveauMot = (Mot) vueMotBlank.editerElement();

					if (nouveauMot != null)
					{
						System.out.println("Mot edité : '" + nouveauMot + "'");
						dictionnaire.ajouterElement(nouveauMot);
					}
				}
			});
		}
		return ajouterMotMenuItem;
	}

	private JMenuItem getExportDicoMenuItem()
	{
		if (exportDicoMenuItem == null)
		{
			exportDicoMenuItem = new JMenuItem();
			exportDicoMenuItem.setText("Exporter dictionnaire");
			exportDicoMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("exportDicoMenuItem.actionPerformed, event=" + evt);

					JFileChooser fc = new JFileChooser();

					fc.setFileFilter(fileFilterDictionnaireExport);

					boolean retry = false;
					do
					{
						if (fc.showSaveDialog(getJFrame()) == JFileChooser.APPROVE_OPTION)
						{
							File fic = fc.getSelectedFile();
							try
							{
								dictionnaire.exporter(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null,
										"Erreur lors de l'exportation, voulez vous ré-essayer ?", "Erreur de fichier",
										JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION);
							}
						}
					} while (retry);
				}
			});
		}
		return exportDicoMenuItem;
	}

	private JMenuItem getImportMenuItem()
	{
		if (importMenuItem == null)
		{
			importMenuItem = new JMenuItem();
			importMenuItem.setText("Importer dictionnaire");
			importMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("importMenuItem.actionPerformed, event=" + evt);

					JFileChooser fc = new JFileChooser();

					fc.setFileFilter(fileFilterDictionnaireExport);

					boolean retry = false;

					do
					{
						if (fc.showOpenDialog(getJFrame()) == JFileChooser.APPROVE_OPTION)
						{
							File fic = fc.getSelectedFile();
							try
							{
								dictionnaire.importer(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null,
										"Erreur lors de l'importation, voulez vous ré-essayer ?", "Erreur de fichier",
										JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION);
							}
						}
					} while (retry);
				}
			});
		}
		return importMenuItem;
	}

}
