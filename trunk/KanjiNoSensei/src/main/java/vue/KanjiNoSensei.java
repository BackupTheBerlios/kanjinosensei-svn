/**
 * 
 */
package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import metier.Dictionary;
import metier.Dictionary.DictionaryElementAlreadyPresentException;
import metier.Dictionary.DictionaryNoMoreElementException;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Sentence;
import metier.elements.Word;
import nl.jj.swingx.gui.modal.JModalConfiguration;
import nl.jj.swingx.gui.modal.JModalFrame;
import utils.MyCheckBoxTree;
import utils.MyUtils;
import utils.MyCheckBoxTree.MyCheckBoxTreeEvent;
import utils.MyCheckBoxTree.MyCheckBoxTreeListener;
import utils.MyUtils.DoItToThisComponent;
import utils.MyUtils.MyModalFrame;
import vue.VueElement.NoAffException;
import vue.VueElement.NoSaisieException;
import vue.VueElement.QuizConfigPanel;
import vue.VueElement.VueDetaillePanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Axan
 * 
 */
public class KanjiNoSensei
{
	
	private final boolean									DEV_ACCESS						= ("axan".compareToIgnoreCase(System.getProperty("user.name")) == 0);

	{
		// Set Look & Feel
		try
		{
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		MyUtils.tracesEnabled = DEV_ACCESS;
	}

	public final String										KanjiNoSensei_VERSION			= "1.0c";

	static boolean											USE_ROMAJI						= false;

	final KanjiNoSensei										app								= this;

	private Set<String>										themesSelectionnes				= new TreeSet<String>();												// @jve:decl-index=0:

	private JFrame											jConfigModalFrame;

	private JMenuItem										ConfigMenuItem;

	static private FileFilter								fileFilterDictionnaire			= MyUtils.generateFileFilter("Dictionnaire de 漢字", "kjd");

	static private FileFilter								fileFilterDictionnaireExport	= MyUtils.generateFileFilter("Dictionnaire exporté", "csv");

	private JFrame											jFrame							= null;

	private JPanel											jContentPane					= null;

	private JMenuBar										jJMenuBar						= null;

	private JMenu											fileMenu						= null;

	private JMenu											editMenu						= null;

	private JMenu											helpMenu						= null;

	private JMenuItem										exitMenuItem					= null;

	private JMenuItem										aboutMenuItem					= null;

	private JFrame											aboutFrame						= null;																// @jve:decl-index=0:visual-constraint="433,15"

	private JPanel											aboutContentPane				= null;

	private JLabel											aboutVersionLabel				= null;

	private JFrame											afficherBaseFrame				= null;																// @jve:decl-index=0:visual-constraint="23,536"

	private JPanel											afficherBaseContentPane			= null;

	private JScrollPane										jScrollPaneThemes				= null;

	private JPanel											jPanelThemesFiltres				= null;

	private JLabel											jLabelRechercher				= null;

	private JTextField										jTextFieldThemesFiltre			= null;

	private JCheckBox										jCheckBoxThemesFiltrer			= null;

	private JButton											jButtonSelectionnerTous			= null;

	private JButton											jButtonDeselectionner			= null;

	private JPanel											jPanelThemesBtns;

	private JCheckBox										jCheckBoxUseRomaji;

	private JTextPane										jLabelLien;

	private JPanel											jPanel1;

	private JLabel											jLabelAuthor;

	private JScrollPane										jScrollPane1;

	private JMenuItem										importMenuItem;

	private JMenuItem										exportDicoMenuItem;

	private JMenuItem										lancerQuizMenuItem;

	private JPanel											jPanelConfigGenerale;

	private JPanel											jPanelQuizSaisieReponse;

	private JPanel											jPanelQuizAffElement;

	private JPanel											jPanelQuizAffReponse;

	private JScrollPane										jScrollPaneMiddle;

	private JScrollPane										jScrollPaneTop;

	private JScrollPane										jScrollPaneBottom;

	private JSplitPane										jSplitPaneTopMiddle;

	private JSplitPane										jSplitPaneTopMiddleBottom;

	private JFrame											jFrameQuizz;

	private JButton											jButtonBaseConnaissanceAnnuler;

	private JButton											jButtonValider;

	private JButton											jButtonOK;

	private JButton											jButtonAnnuler;

	private JTabbedPane										jConfigFrameTabbedPane;

	private JPanel											jPanelConfigBtns;

	/** Dictionnaire complet disponible */
	final private Dictionary								dictionnaire;

	final private Vector<Class<? extends Element>>			plugins;

	final private Map<Class<? extends Element>, VueElement>	vuesBlank						= new Hashtable<Class<? extends Element>, VueElement>();

	final private Map<Class<? extends Element>, JMenuItem>	jMenusAjouterElement			= new Hashtable<Class<? extends Element>, JMenuItem>();

	public Dictionary getDictionnaire()
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
	private Dictionary		sousDictionnaire			= null;	// @jve:decl-index=0:

	/** Sous ensemble de départ du quiz */
	private Dictionary		dictionnaireQuiz			= null;

	/** Sous ensemble courant (restant) du quiz */
	private Dictionary		dictionnaireQuizEnCours		= null;

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

	private MyCheckBoxTree	jMyCheckBoxTree;

	private static Vector<Class<? extends Element>> listPluginsClasses()
	{
		Vector<Class<? extends Element>> v = new Vector<Class<? extends Element>>();
		v.add(Kanji.class);
		v.add(Word.class);
		v.add(Sentence.class);

		return v;
	}

	public KanjiNoSensei(File fic_dico) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, IOException
	{
		plugins = listPluginsClasses();

		Iterator<Class<? extends Element>> itPlugins = plugins.iterator();
		while (itPlugins.hasNext())
		{
			Class<? extends Element> plugin = itPlugins.next();
			vuesBlank.put(plugin, VueElement.genererVueBlankElement(app, plugin, USE_ROMAJI));
		}

		this.dictionnaire = new Dictionary(fic_dico);
		this.dictionnaireQuiz = this.dictionnaire.clone();
	}

	private MyCheckBoxTree getMyCheckBoxTree(boolean erase)
	{
		if (jMyCheckBoxTree == null)
		{
			jMyCheckBoxTree = new MyCheckBoxTree();

			jMyCheckBoxTree.setTreeListener(new MyCheckBoxTreeListener()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see utils.MyCheckBoxTree.MyCheckBoxTreeListener#treeNodesChanged(utils.MyCheckBoxTree.MyCheckBoxTreeEvent)
				 */
				@Override
				public void treeNodesChanged(MyCheckBoxTreeEvent e)
				{
					if (e.itemIsSelected)
					{
						System.out.println("'" + e.itemPath + "' ajouté");
						themesSelectionnes.add(e.itemPath);
					}
					else
					{
						if (themesSelectionnes.contains(e.itemPath))
						{
							System.out.println("'" + e.itemPath + "' retiré");
							themesSelectionnes.remove(e.itemPath);
						}
					}

					// On ne rafraichie que si le CheckBoxTree est stable.
					if (e.isTreeStable)
					{
						afficherBaseFrameMAJZoneElements();
					}
				}
			});

			jMyCheckBoxTree.setVisible(true);
		}

		if (erase)
		{
			jMyCheckBoxTree.clearModel();
		}

		jMyCheckBoxTree.treeDidChange();

		MyUtils.refreshComponent(jMyCheckBoxTree);

		return jMyCheckBoxTree;
	}

	private JMenuItem getAjouterElementMenuItem(final Class<? extends Element> classeElement)
	{
		if ( !jMenusAjouterElement.containsKey(classeElement))
		{
			JMenuItem jMenuItem = new JMenuItem();
			jMenuItem.setText("Ajouter " + classeElement.getSimpleName());
			jMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					Element nouvelElement = vuesBlank.get(classeElement).editerElement();

					if (nouvelElement != null)
					{
						System.out.println(classeElement.getSimpleName() + " edité : '" + nouvelElement + "'");
						try
						{
							dictionnaire.addElement(nouvelElement);
						}
						catch (DictionaryElementAlreadyPresentException ex)
						{
							System.out.println("Impossible d'ajouter l'élément car celui-ci existe déjà : " + ex.getMessage());
						}
					}
				}
			});

			jMenusAjouterElement.put(classeElement, jMenuItem);
		}
		return jMenusAjouterElement.get(classeElement);
	}

	/**
	 * This method initializes afficherBaseFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getAfficherBaseFrame()
	{
		if (afficherBaseFrame == null)
		{
			afficherBaseFrame = new JFrame();
			// <NoJigloo>
			afficherBaseFrame = new MyModalFrame(getJFrame(), true);
			// </NoJigloo>
			Dimension d = new Dimension(800, 600);
			afficherBaseFrame.setSize(d);
			afficherBaseFrame.setTitle("Affichage de la base de connaissance");
			afficherBaseFrame.setMinimumSize(new java.awt.Dimension(0, 0));
			afficherBaseFrame.setPreferredSize(d);
			afficherBaseFrame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			afficherBaseFrame.setName("Base de connaissance");
			// <JiglooProtected>
			afficherBaseFrame.addComponentListener(new ComponentAdapter()
			{
				// </JiglooProtected>
				public void componentShown(ComponentEvent evt)
				{
					getJTextFieldThemesFiltre().setText(filtreThemesTxt);
					getJCheckBoxThemesFiltrer().setSelected(filtreThemesActif);
					getJTextFieldElementsFiltre().setText(filtreElementsTxt);
					getJCheckBoxElementsFiltre().setSelected(filtreElementsActif);

					afficherBaseFrameMAJZoneThemes();
					afficherBaseFrameMAJZoneElements();
				}
			});
			afficherBaseFrame.setContentPane(getAfficherBaseContentPane());
			// <JiglooProtected>

			afficherBaseFrame.addWindowListener(new WindowAdapter()
			{
				// </JiglooProtected>
				public void windowOpened(WindowEvent e)
				{
					getJTextFieldThemesFiltre().setText(filtreThemesTxt);
					getJCheckBoxThemesFiltrer().setSelected(filtreThemesActif);
					getJTextFieldElementsFiltre().setText(filtreElementsTxt);
					getJCheckBoxElementsFiltre().setSelected(filtreElementsActif);

					afficherBaseFrameMAJZoneThemes();
					afficherBaseFrameMAJZoneElements();
				}
			});
		}

		return afficherBaseFrame;
	}

	private void afficherBaseFrameMAJZoneThemes()
	{
		getJFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		getJPanelThemesListe().removeAll();

		String filtreTheme = jTextFieldThemesFiltre.getText();

		if ( !jCheckBoxThemesFiltrer.isSelected())
		{
			filtreTheme = null;
		}

		Set<String> listeThemesFiltre = dictionnaire.getThemesList(filtreTheme);

		Iterator<String> itThemes = listeThemesFiltre.iterator();
		MyCheckBoxTree jCheckBoxTree = getMyCheckBoxTree(true);

		while (itThemes.hasNext())
		{
			String theme = itThemes.next();
			jCheckBoxTree.addNode(theme, themesSelectionnes.contains(theme));
		}

		jPanelThemesListe.add(getJScrollPane1(), BorderLayout.CENTER);

		getJPanelThemesListe().setPreferredSize(new Dimension(0, getJPanelThemesListe().getComponentCount() * 20));
		MyUtils.refreshComponent(getJPanelThemesListe());

		getJFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void afficherBaseFrameMAJZoneElements()
	{
		getJFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		sousDictionnaire = dictionnaire.getSubDictionary(themesSelectionnes);

		getJPanelElementsListe().removeAll();

		String filtreElement = jTextFieldElementsFiltre.getText();

		if ( !jCheckBoxElementsFiltre.isSelected())
		{
			filtreElement = null;
		}

		Set<Element> listeElementsFiltre = sousDictionnaire.getElementsList(filtreElement);
		Iterator<Element> itElements = listeElementsFiltre.iterator();
		Element element = null;
		while (itElements.hasNext())
		{
			element = itElements.next();
			try
			{
				final VueElement vueElement = VueElement.genererVueElement(this, element, USE_ROMAJI);
				JPanel vueElementDetaillePanel = vueElement.getVueDetaillePanel().getPanel();

				MyUtils.doItToAllSubComponents(vueElementDetaillePanel, new DoItToThisComponent()
				{

					@Override
					public void doIt(Component c)
					{
						c.addMouseWheelListener(new MouseWheelListener()
						{

							@Override
							public void mouseWheelMoved(MouseWheelEvent e)
							{
								MyUtils.trace("mouseWheelMoved : " + e);
								getJScrollPaneElements().dispatchEvent(e);
							}

						});

						if (c.getMouseListeners().length > 0)
						{
							MyUtils.trace("Component has already a mouseListener : "+c);
						}
						else
						{
							c.addMouseListener(new MouseAdapter()
							{
								@Override
								public void mouseClicked(MouseEvent e)
								{
									MyUtils.trace("mouseClicked : " + e);

									if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 1))
									{
										e.consume();
										afficherBaseDialogMAJSelection(vueElement);
									}
									else if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
									{
										e.consume();
										Element ancienElement = vueElement.getElement();
										Element nouveauElement = vueElement.editerElement();
										if (nouveauElement != null)
										{
											dictionnaire.removeElement(ancienElement);
											try
											{
												dictionnaire.addElement(nouveauElement);
											}
											catch (DictionaryElementAlreadyPresentException e1)
											{
												System.err.println("Erreur d'édition : " + e1.getMessage());
												try
												{
													dictionnaire.addElement(ancienElement);
												}
												catch (DictionaryElementAlreadyPresentException e2)
												{
													// TODO Auto-generated catch block
													e2.printStackTrace();
												}
											}
										}

										afficherBaseFrameMAJZoneElements();
									}

									super.mouseClicked(e);
								}

							});
						}

					}

				});

				getJPanelElementsListe().add(vueElementDetaillePanel);
			}
			catch (Exception e)
			{
				System.err.println("Erreur de création de la vue de l'élément \"" + element.toString() + "\"");
			}
		}

		getJListThemesSelectionnes().setListData(themesSelectionnes.toArray());

		getJPanelElementsListe().setPreferredSize(new Dimension(0, getJPanelElementsListe().getComponentCount() * VueElement.getVueDetailleHeight() + 20));
		MyUtils.refreshComponent(getJPanelElementsListe());

		getJScrollPaneElements().getVerticalScrollBar().setUnitIncrement(Math.max(10, Math.min(1000, (getJPanelElementsListe().getComponentCount() * 1))));

		getJFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
			jScrollPaneThemes.setPreferredSize(new java.awt.Dimension(500, 200));
			jScrollPaneThemes.setSize(649, 200);
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
			jLabelRechercher.setSize(0, 0);
			jPanelThemesFiltres = new JPanel();
			GridBagLayout jPanelThemesFiltresLayout = new GridBagLayout();
			jPanelThemesFiltres.setLayout(jPanelThemesFiltresLayout);
			jPanelThemesFiltresLayout.columnWidths = new int[] {110, 100, 0};
			jPanelThemesFiltresLayout.columnWeights = new double[] {0.0, 0.1, 1.0};
			jPanelThemesFiltresLayout.rowWeights = new double[] {0.1};
			jPanelThemesFiltresLayout.rowHeights = new int[] {7};
			jPanelThemesFiltres.setPreferredSize(new java.awt.Dimension(0, 30));
			jPanelThemesFiltres.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelThemesFiltres.add(jLabelRechercher, gridBagConstraints15);
			jPanelThemesFiltres.add(getJTextFieldThemesFiltre(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 20), 0, 0));
			jPanelThemesFiltres.add(getJPanelThemesBtns(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
			jTextFieldThemesFiltre.setPreferredSize(new java.awt.Dimension(100, 25));
			jTextFieldThemesFiltre.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jTextFieldThemesFiltre.setHorizontalAlignment(JTextField.LEADING);
			jTextFieldThemesFiltre.setSize(100, 25);
			jTextFieldThemesFiltre.addFocusListener(new FocusAdapter()
			{

				private String	lastText	= jTextFieldThemesFiltre.getText();

				public void focusGained(FocusEvent evt)
				{
					lastText = jTextFieldThemesFiltre.getText();
				}

				public void focusLost(FocusEvent evt)
				{

					if (lastText.compareTo(jTextFieldThemesFiltre.getText()) != 0)
					{
						jCheckBoxThemesFiltrer.setSelected( !jTextFieldThemesFiltre.getText().isEmpty());
						afficherBaseFrameMAJZoneThemes();
					}
				}
			});
			jTextFieldThemesFiltre.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					System.out.println("jTextFieldThemesFiltre.actionPerformed, event=" + evt);
					jTextFieldElementsFiltre.transferFocus();
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
			jCheckBoxThemesFiltrer.setPreferredSize(new java.awt.Dimension(100, 22));
			jCheckBoxThemesFiltrer.addItemListener(new java.awt.event.ItemListener()
			{
				public void itemStateChanged(java.awt.event.ItemEvent e)
				{
					afficherBaseFrameMAJZoneThemes();
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
			jButtonSelectionnerTous.setLayout(null);
			jButtonSelectionnerTous.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jButtonSelectionnerTous.setText("Tous");
			jButtonSelectionnerTous.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					MyCheckBoxTree jMyCheckBoxTree = getMyCheckBoxTree(false);
					jMyCheckBoxTree.setSubTreeSelected(jMyCheckBoxTree.getSelectionPath(), true, Integer.MAX_VALUE);

					afficherBaseFrameMAJZoneElements();
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
					MyCheckBoxTree jMyCheckBoxTree = getMyCheckBoxTree(false);
					jMyCheckBoxTree.setSubTreeSelected(jMyCheckBoxTree.getSelectionPath(), false, Integer.MAX_VALUE);

					afficherBaseFrameMAJZoneElements();
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
								dictionnaire.save(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null, "Erreur lors de l'enregistrement, voulez vous ré-essayer ?", "Erreur de fichier", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION);
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
					JFrame afficherBaseFrame = getAfficherBaseFrame();
					afficherBaseFrame.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					afficherBaseFrame.setLocation(loc);
					afficherBaseFrame.setVisible(true);
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
			jSplitPaneCentre.setPreferredSize(new Dimension(0, 0));
			jSplitPaneCentre.setDividerLocation(200);
			jSplitPaneCentre.setTopComponent(getJPanelThemesInSplit());
			jSplitPaneCentre.setBottomComponent(getJPanelElementsInSplit());
			jSplitPaneCentre.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneCentre.setContinuousLayout(true);
			jSplitPaneCentre.setOneTouchExpandable(true);
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
			BorderLayout gridLayout1 = new BorderLayout();
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
			jLabel.setSize(0, 0);
			jPanelElementsFiltres = new JPanel();
			GridBagLayout jPanelElementsFiltresLayout = new GridBagLayout();
			jPanelElementsFiltres.setLayout(jPanelElementsFiltresLayout);
			jPanelElementsFiltresLayout.columnWeights = new double[] {0, 0.1, 0.1};
			jPanelElementsFiltresLayout.columnWidths = new int[] {110, 7, 7};
			jPanelElementsFiltresLayout.rowWeights = new double[] {0.1};
			jPanelElementsFiltresLayout.rowHeights = new int[] {7};
			jPanelElementsFiltres.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelElementsFiltres.setPreferredSize(new java.awt.Dimension(0, 30));
			jPanelElementsFiltres.add(jLabel, gridBagConstraints20);
			jPanelElementsFiltres.add(getJTextFieldElementsFiltre(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 20), 0, 0));
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
			jPanelBaseBtnsLayout.setVgap(2);
			jPanelBaseBtns.setLayout(jPanelBaseBtnsLayout);
			jPanelBaseBtns.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelBaseBtns.setPreferredSize(new java.awt.Dimension(0, 30));
			jPanelBaseBtns.add(getJButtonValider());
			jPanelBaseBtns.add(getJButtonBaseConnaissanceAnnuler());
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
			jTextFieldElementsFiltre.setPreferredSize(new java.awt.Dimension(100, 25));
			jTextFieldElementsFiltre.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jTextFieldElementsFiltre.transferFocus();
				}
			});
			jTextFieldElementsFiltre.addFocusListener(new FocusAdapter()
			{
				private String	lastText	= jTextFieldElementsFiltre.getText();

				public void focusGained(FocusEvent e)
				{
					lastText = jTextFieldElementsFiltre.getText();
				}

				public void focusLost(FocusEvent evt)
				{
					if (lastText.compareTo(jTextFieldElementsFiltre.getText()) != 0)
					{
						jCheckBoxElementsFiltre.setSelected( !jTextFieldElementsFiltre.getText().isEmpty());
						afficherBaseFrameMAJZoneElements();
					}
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
					afficherBaseFrameMAJZoneElements();
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
			jSplitPaneThemes.setDividerLocation(600);
			jSplitPaneThemes.setOneTouchExpandable(true);
			jSplitPaneThemes.setContinuousLayout(true);
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
			jPanelThemesSelection.setPreferredSize(new java.awt.Dimension(0, 0));
			jPanelThemesSelection.setMinimumSize(new java.awt.Dimension(135, 200));
			jPanelThemesSelection.setSize(135, 200);
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
		if (MyUtils.checkJREVersion("1.6") == false)
		{
			System.err.println("JRE version 1.6 nécéssaire : http://www.java.com");
			return;
		}

		final File ficDico;

		if (args.length > 0)
		{
			ficDico = new File(args[0]);
		}
		else
		{
			ficDico = null;
		}

		if (args.length >= 2)
		{
			try
			{
				MyUtils.manageLookAndFeelsOption(args[1]);
			}
			catch (Exception e)
			{
				System.err.println("Exception lors de la gestion du Look&Feels : " + e.getMessage());
				return;
			}
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
			JModalConfiguration.disableBusyCursor();

			jFrame = new JFrame();
			// <NoJigloo>
			jFrame = new MyModalFrame(null, false);
			// </NoJigloo>
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(300, 200);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("漢字の先生");
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
			if (DEV_ACCESS)
			{
				jJMenuBar.add(getEditMenu());
			}
			Component[] roots = {getJFrame(), getAboutFrame(), getAfficherBaseFrame(), getJConfigFrame(), getJQuizFrame()};
			jJMenuBar.add(MyUtils.getUIMenu(roots));
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
			fileMenu.setText("Fichier");
			fileMenu.add(getAfficherBaseMenuItem());

			if (DEV_ACCESS)
			{
				fileMenu.add(getSaveDictionnaireMenuItem());
				fileMenu.add(getExportDicoMenuItem());
				fileMenu.add(getImportMenuItem());
			}

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
			editMenu.setText("Edition");
			for (Class<? extends Element> c : plugins)
			{
				editMenu.add(getAjouterElementMenuItem(c));
			}
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
			helpMenu.setText("Aide");
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
			exitMenuItem.setText("Quitter");
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
			aboutMenuItem.setText("A propos");
			aboutMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JFrame aboutFrame = getAboutFrame();
					aboutFrame.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutFrame.setLocation(loc);
					aboutFrame.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes aboutFrame
	 * 
	 * @return {@link javax.swing.JFrame}
	 */
	private JFrame getAboutFrame()
	{
		if (aboutFrame == null)
		{
			aboutFrame = new JFrame();
			// <NoJigloo>
			aboutFrame = new JModalFrame(getJFrame(), getJFrame(), true);
			// aboutFrame = new MyModalFrame(getJFrame(), true);
			// </NoJigloo>
			aboutFrame.setTitle("A propos");
			aboutFrame.setContentPane(getAboutContentPane());
		}
		return aboutFrame;
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
			aboutContentPane.setPreferredSize(new java.awt.Dimension(231, 70));
			aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
			aboutContentPane.add(getJPanel1(), BorderLayout.SOUTH);
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
			aboutVersionLabel.setText("Version " + KanjiNoSensei_VERSION);
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
					jConfigModalFrame = getJConfigFrame();
					jConfigModalFrame.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					jConfigModalFrame.setLocation(loc);
					jConfigModalFrame.setVisible(true);
				}
			});
		}
		return ConfigMenuItem;
	}

	private JFrame getJConfigFrame()
	{
		if (jConfigModalFrame == null)
		{
			jConfigModalFrame = new JFrame();
			// <NoJigloo>
			jConfigModalFrame = new MyModalFrame(getJFrame(), true);
			// </NoJigloo>
			jConfigModalFrame.setTitle("Configuration");
			jConfigModalFrame.setPreferredSize(new java.awt.Dimension(0, 0));
			jConfigModalFrame.setMinimumSize(new java.awt.Dimension(600, 300));
			jConfigModalFrame.addComponentListener(new ComponentAdapter()
			{
				// </JiglooProtected>
				public void componentShown(ComponentEvent evt)
				{
					for (Component c : getJConfigFrameTabbedPane().getComponents())
					{
						if (VueElement.QuizConfigPanel.class.isInstance(c))
						{
							((VueElement.QuizConfigPanel) c).resetDisplay();
						}
					}

					MyUtils.refreshComponent(getJConfigFrameTabbedPane().getSelectedComponent());
					MyUtils.refreshComponent(getJConfigFrameTabbedPane());
				}
			});

			jConfigModalFrame.setSize(704, 178);
			jConfigModalFrame.getContentPane().add(getJPanelConfigBtns(), BorderLayout.SOUTH);
			jConfigModalFrame.getContentPane().add(getJConfigFrameTabbedPane(), BorderLayout.CENTER);

			for (Class<? extends Element> c : plugins)
			{
				JPanel configPanel = vuesBlank.get(c).getQuizConfigPanel().getPanel();
				getJConfigFrameTabbedPane().add(c.getSimpleName(), configPanel);
			}
		}
		return jConfigModalFrame;
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

	private JTabbedPane getJConfigFrameTabbedPane()
	{
		if (jConfigFrameTabbedPane == null)
		{
			jConfigFrameTabbedPane = new JTabbedPane();
			jConfigFrameTabbedPane.setPreferredSize(new java.awt.Dimension(0, 0));
			jConfigFrameTabbedPane.setAutoscrolls(true);
			jConfigFrameTabbedPane.setMinimumSize(new java.awt.Dimension(600, 300));
			jConfigFrameTabbedPane.addTab("Configuration générale", null, getJPanelConfigGenerale(), null);
		}
		return jConfigFrameTabbedPane;
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
					getJConfigFrame().dispose();
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

					for (Component component : getJConfigFrameTabbedPane().getComponents())
					{
						if (QuizConfigPanel.class.isAssignableFrom(component.getClass()))
						{
							QuizConfigPanel configPanel = QuizConfigPanel.class.cast(component);
							configPanel.valider();
						}
					}

					// On valide la page de config générale.
					USE_ROMAJI = getJCheckBoxUseRomaji().isSelected();

					getJConfigFrame().dispose();
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
			jButtonValider.setPreferredSize(new java.awt.Dimension(100, 25));
			jButtonValider.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dictionnaireQuiz = sousDictionnaire;
					getAfficherBaseFrame().dispose();
				}
			});
		}
		return jButtonValider;
	}

	private JButton getJButtonBaseConnaissanceAnnuler()
	{
		if (jButtonBaseConnaissanceAnnuler == null)
		{
			jButtonBaseConnaissanceAnnuler = new JButton();
			jButtonBaseConnaissanceAnnuler.setText("Annuler");
			jButtonBaseConnaissanceAnnuler.setPreferredSize(new java.awt.Dimension(100, 25));
			jButtonBaseConnaissanceAnnuler.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					getAfficherBaseFrame().dispose();
				}
			});
		}
		return jButtonBaseConnaissanceAnnuler;
	}

	private JFrame getJQuizFrame()
	{
		if (jFrameQuizz == null)
		{
			jFrameQuizz = new JFrame();
			// <NoJigloo>
			jFrameQuizz = new MyModalFrame(getJFrame(), true);
			// </NoJigloo>
			jFrameQuizz.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			jFrameQuizz.setPreferredSize(new java.awt.Dimension(920, 420));
			jFrameQuizz.setTitle("Quiz");
			jFrameQuizz.setName("Quiz");
			// <JiglooProtected>
			jFrameQuizz.getContentPane().add(getJSplitPaneTopMiddleBottom(), BorderLayout.CENTER);
			jFrameQuizz.setSize(920, 420);
		}
		return jFrameQuizz;
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
				vueElementQuestionEnCours = VueElement.genererVueElement(this, elementQuestionEnCours, USE_ROMAJI);
			}
			catch (DictionaryNoMoreElementException e1)
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
				System.err.println("Erreur de création de la vue de l'élément \"" + elementQuestionEnCours.toString() + "\"");
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

		getJPanelQuizAffElement().add(panelQuestion, BorderLayout.CENTER);

		getJPanelQuizSaisieReponse().removeAll();
		panelSaisieReponse.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
		getJPanelQuizSaisieReponse().add(panelSaisieReponse, BorderLayout.CENTER);

		getJPanelQuizAffReponse().removeAll();
		getJPanelQuizAffReponse().setBackground(defaultBgColor);

		miseAJourQuizTitle();

		MyUtils.refreshComponent(getJQuizFrame());
		getJQuizFrame().setVisible(true);
	}

	private void miseAJourQuizTitle()
	{
		getJQuizFrame().setTitle("Quiz: question n°" + questionCourante + "\tbonnes réponses : " + bonnesReponses + "\tmauvaises réponses : " + erreurs);
	}

	public synchronized void validerReponseQuiz(boolean reponseCorrecte, boolean nextQuestion)
	{
		final Color bg;
		++questionCourante;
		if (reponseCorrecte)
		{
			++bonnesReponses;
			dictionnaireQuizEnCours.removeElement(elementQuestionEnCours);
			bg = Color.GREEN;
		}
		else
		{
			++erreurs;
			bg = Color.RED;
		}

		if (nextQuestion)
		{
			poserQuestionSuivanteQuiz();
			return;
		}

		getJPanelQuizAffReponse().setBackground(bg);
		miseAJourQuizTitle();

		MyUtils.lockPanel(getJPanelQuizSaisieReponse());

		JPanel panelSolution = vueElementQuestionEnCours.getQuizSolutionPanel(false).getPanel();

		MyUtils.doItToAllSubComponents(panelSolution, new DoItToThisComponent()
		{

			@Override
			public void doIt(Component c)
			{
				c.setBackground(bg);
			}

		});

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

		MyUtils.refreshComponent(getJPanelQuizAffReponse());
		getJPanelQuizAffReponse().setVisible(true);
		buttonContinuer.grabFocus();
	}

	protected void finirQuiz()
	{
		String msg = String.format("Nombre de questions :\t%d\nBonnes réponses :\t%d\nMauvaises réponses :\t%d", questionCourante, bonnesReponses, erreurs);
		JOptionPane.showMessageDialog(null, msg, "Quiz terminé", JOptionPane.INFORMATION_MESSAGE);
		getJQuizFrame().dispose();
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
			jSplitPaneTopMiddleBottom.setDividerLocation(240);
			jSplitPaneTopMiddleBottom.setSize(0, 0);
			jSplitPaneTopMiddleBottom.setMinimumSize(new java.awt.Dimension(0, 0));
			jSplitPaneTopMiddleBottom.setOneTouchExpandable(true);
			jSplitPaneTopMiddleBottom.setContinuousLayout(true);
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
			jSplitPaneTopMiddle.setDividerLocation(120);
			jSplitPaneTopMiddle.setContinuousLayout(true);
			jSplitPaneTopMiddle.setOneTouchExpandable(true);
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
			FormLayout jPanelConfigGeneraleLayout = new FormLayout("max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)", "max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			jPanelConfigGenerale.setLayout(jPanelConfigGeneraleLayout);
			jPanelConfigGenerale.setDoubleBuffered(false);
			jPanelConfigGenerale.setPreferredSize(new java.awt.Dimension(582, 201));
			jPanelConfigGenerale.addComponentListener(new ComponentAdapter()
			{
				public void componentShown(ComponentEvent evt)
				{
					// On rafraichi le formulaire
					getJCheckBoxUseRomaji().setSelected(USE_ROMAJI);
				}
			});
			jPanelConfigGenerale.add(getJCheckBoxUseRomaji(), new CellConstraints("1, 1, 1, 1, default, default"));
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

					jFrameQuizz = getJQuizFrame();

					jFrameQuizz.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					jFrameQuizz.setLocation(loc);
					initialiserQuiz();
					poserQuestionSuivanteQuiz();
				}
			});
		}
		return lancerQuizMenuItem;
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
								dictionnaire.exportFile(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null, "Erreur lors de l'exportation, voulez vous ré-essayer ?", "Erreur de fichier", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION);
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
					JFileChooser fc = new JFileChooser();

					fc.setFileFilter(fileFilterDictionnaireExport);
					fc.setSelectedFile(new File(System.getProperty("user.dir")));

					boolean retry = false;

					do
					{
						if (fc.showOpenDialog(getJFrame()) == JFileChooser.APPROVE_OPTION)
						{
							File fic = fc.getSelectedFile();
							try
							{
								dictionnaire.importFile(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null, "Erreur lors de l'importation, voulez vous ré-essayer ?", "Erreur de fichier", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION);
							}
						}
					} while (retry);
				}
			});
		}
		return importMenuItem;
	}

	private JScrollPane getJScrollPane1()
	{
		if (jScrollPane1 == null)
		{
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 200));
			jScrollPane1.setSize(500, 200);
			jScrollPane1.setViewportView(getMyCheckBoxTree(true));
		}
		return jScrollPane1;
	}

	private JLabel getJLabelAuthor()
	{
		if (jLabelAuthor == null)
		{
			jLabelAuthor = new JLabel();
			jLabelAuthor.setText("Auteur: Escallier Pierre");
		}
		return jLabelAuthor;
	}

	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			GridLayout jPanel1Layout = new GridLayout(2, 1);
			jPanel1Layout.setColumns(1);
			jPanel1Layout.setRows(2);
			jPanel1Layout.setHgap(5);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1.add(getJLabelAuthor());
			jPanel1.add(getJLabelLien());
		}
		return jPanel1;
	}

	private JTextPane getJLabelLien()
	{
		if (jLabelLien == null)
		{
			jLabelLien = new JTextPane();
			jLabelLien.setText("http://kanjinosensei.berlios.de");
			jLabelLien.setEditable(false);
			jLabelLien.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					try
					{
						Runtime.getRuntime().exec("firefox " + jLabelLien.getText());
					}
					catch (IOException e)
					{
						System.err.println("Impossible de lancer le navigateur.");
					}
				}

				public void mouseExited(MouseEvent evt)
				{
					Font f = jLabelLien.getFont();
					jLabelLien.setFont(new Font(f.getName(), Font.PLAIN, f.getSize()));
				}

				public void mouseEntered(MouseEvent evt)
				{
					Font f = jLabelLien.getFont();
					jLabelLien.setFont(new Font(f.getName(), Font.BOLD | Font.ITALIC, f.getSize()));
				}
			});
		}
		return jLabelLien;
	}

	private JCheckBox getJCheckBoxUseRomaji()
	{
		if (jCheckBoxUseRomaji == null)
		{
			jCheckBoxUseRomaji = new JCheckBox();
			jCheckBoxUseRomaji.setText("Utiliser le romaji");
		}
		return jCheckBoxUseRomaji;
	}

	private JPanel getJPanelThemesBtns()
	{
		if (jPanelThemesBtns == null)
		{
			jPanelThemesBtns = new JPanel();
			FlowLayout jPanelThemesBtnsLayout = new FlowLayout();
			jPanelThemesBtnsLayout.setVgap(0);
			jPanelThemesBtnsLayout.setAlignment(FlowLayout.LEFT);
			jPanelThemesBtnsLayout.setHgap(0);
			jPanelThemesBtns.setLayout(jPanelThemesBtnsLayout);
			jPanelThemesBtns.add(getJCheckBoxThemesFiltrer());
			jPanelThemesBtns.add(getJButtonSelectionnerTous());
			jPanelThemesBtns.add(getJButtonDeselectionner());
		}
		return jPanelThemesBtns;
	}

}