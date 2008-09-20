/**
 * 
 */
package epsofts.KanjiNoSensei.vue;

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
import java.awt.Toolkit;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.UnexpectedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.attribute.standard.JobName;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import nl.jj.swingx.gui.modal.JModalConfiguration;
import nl.jj.swingx.gui.modal.JModalFrame;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import epsofts.KanjiNoSensei.metier.Dictionary;
import epsofts.KanjiNoSensei.metier.DictionaryAnalyser;
import epsofts.KanjiNoSensei.metier.LearningProfile;
import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.metier.Dictionary.DictionaryElementAlreadyPresentException;
import epsofts.KanjiNoSensei.metier.Dictionary.DictionaryNoMoreElementException;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.metier.elements.Sentence;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.utils.MyCheckBoxTree;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.MyCheckBoxTree.MyCheckBoxTreeEvent;
import epsofts.KanjiNoSensei.utils.MyCheckBoxTree.MyCheckBoxTreeListener;
import epsofts.KanjiNoSensei.utils.MyUtils.DoItToThisComponent;
import epsofts.KanjiNoSensei.utils.MyModalFrame;
import epsofts.KanjiNoSensei.vue.VueElement.NoAffException;
import epsofts.KanjiNoSensei.vue.VueElement.NoSaisieException;
import epsofts.KanjiNoSensei.vue.VueElement.QuizConfigPanel;
import epsofts.KanjiNoSensei.vue.VueElement.VueDetaillePanel;

/**
 * @author Axan
 * 
 */
public class KanjiNoSensei implements PropertyChangeListener
{
	private static final Logger	log	= Logger.getLogger(KanjiNoSensei.class.getName());

	public static void log(Level level, String msg)
	{
		log.log(level, msg);
	}

	private final boolean	DEV_ACCESS	= ("axan".compareToIgnoreCase(System.getProperty("user.name")) == 0);	//$NON-NLS-1$ //$NON-NLS-2$

	{
		// Set Look & Feel
		try
		{
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); //$NON-NLS-1$
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		MyUtils.logMinLevel = (DEV_ACCESS?Level.ALL:Level.WARNING);
	}

	private static class Presets
	{
		TreeMap<String, Vector<String>>	presets	= new TreeMap<String, Vector<String>>();

		public Presets()
		{

		}

		public void addPreset(String presetName, Vector<String> themes)
		{
			presets.put(presetName, themes);
		}

		public String[] getPresetsList()
		{
			String[] result = new String[presets.size()];
			return presets.keySet().toArray(result);
		}

		public static Presets open(File ficPresets) throws IOException
		{
			KanjiNoSensei.log(Level.INFO, Messages.getString("KanjiNoSensei.Presets.OpeningFile") + " : \"" + ficPresets.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			Presets presets = new Presets();

			FileInputStream fis = new FileInputStream(ficPresets);

			ObjectInputStream ois = new ObjectInputStream(fis);

			try
			{
				Object obj = ois.readObject();
				if ( !presets.presets.getClass().isInstance(obj)) throw new ClassNotFoundException();

				presets.presets = (TreeMap<String, Vector<String>>) obj;
			}
			catch (ClassNotFoundException e)
			{
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("Presets.Open.ErrorOnElement") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
				throw new IOException(Messages.getString("Presets.Open.ErrorOnElement") + " : " + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$)
			}

			ois.close();
			fis.close();

			return presets;
		}

		/**
		 * @throws IOException
		 * @throws FileNotFoundException
		 * 
		 */
		public void save(File ficPresets) throws FileNotFoundException, IOException
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficPresets));
			oos.writeObject(presets);
		}

		/**
		 * @param string
		 * @return
		 */
		public Vector<String> getPreset(String presetName)
		{
			return presets.get(presetName);
		}

		/**
		 * @param string
		 */
		public void removePreset(String presetName)
		{
			presets.remove(presetName);
		}
	};

	private Presets											presets							= null;

	public final String										KanjiNoSensei_VERSION			= "1.0-SNAPSHOT";																									//$NON-NLS-1$

	static boolean											USE_ROMAJI						= Boolean.parseBoolean(Config.getString("GeneralConfig.UseRomaji", "false"));								//$NON-NLS-1$ //$NON-NLS-2$

	static private KanjiNoSensei							app								= null;

	private Set<String>										themesSelectionnes				= new TreeSet<String>();																					// @jve:decl-index=0:

	private JFrame											jConfigModalFrame;

	private JMenuItem										ConfigMenuItem;

	static private FileFilter								fileFilterDictionnaire			= MyUtils.generateFileFilter(Messages.getString("KanjiNoSensei.DictionaryFileFilterName"), "kjd");			//$NON-NLS-1$ //$NON-NLS-2$

	static private FileFilter								fileFilterDictionnaireExport	= MyUtils.generateFileFilter(Messages.getString("KanjiNoSensei.ExportedDictionaryFileFilterName"), "csv");	//$NON-NLS-1$ //$NON-NLS-2$

	private JFrame											jFrame							= null;

	private JPanel											jContentPane					= null;

	private JMenuBar										jJMenuBar						= null;

	private JMenu											fileMenu						= null;

	private JMenu											editMenu						= null;

	private JMenu											helpMenu						= null;

	private JMenuItem										exitMenuItem					= null;

	private JMenuItem										aboutMenuItem					= null;

	private JFrame											aboutFrame						= null;																									// @jve:decl-index=0:visual-constraint="433,15"

	private JPanel											aboutContentPane				= null;

	private JLabel											aboutVersionLabel				= null;

	private JFrame											afficherBaseFrame				= null;																									// @jve:decl-index=0:visual-constraint="23,536"

	private JPanel											afficherBaseContentPane			= null;

	private JScrollPane										jScrollPaneThemes				= null;

	private JPanel											jPanelThemesFiltres				= null;

	private JLabel											jLabelRechercher				= null;

	private JTextField										jTextFieldThemesFiltre			= null;

	private JCheckBox										jCheckBoxThemesFiltrer			= null;

	private JButton											jButtonSelectionnerTous			= null;

	private JButton											jButtonDeselectionner			= null;

	private JButton											jButtonSave;

	private JButton											jButtonCharger;

	private JPanel											jPanelPresetsBtns;

	private JComboBox										jComboBoxPresets;

	private JLabel											jLabelPreset;

	private JPanel											jPanelPresets;

	private JPanel											jPanelTop;

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

	private JScrollPane										jScrollPaneListThemesSelectionnes;

	private JButton											jButtonSupprimer;

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

	private Box				jPanelElementsListe			= null;

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
	
	private int 			nbQuestions					= 0;

	private int				questionCourante			= 0;

	private int				bonnesReponses				= 0;

	private int				erreurs						= 0;

	private Element			elementQuestionEnCours		= null;

	private VueElement		vueElementQuestionEnCours	= null;

	private Color			defaultBgColor;

	private MyCheckBoxTree	jMyCheckBoxTree;

	private File			userLearningProfileFile		= null;

	private LearningProfile	userLearningProfile			= null;

	private File			ficPresets;

	private JList			jListElements;

	private static Vector<Class<? extends Element>> listPluginsClasses()
	{
		Vector<Class<? extends Element>> v = new Vector<Class<? extends Element>>();
		v.add(Kanji.class);
		v.add(Word.class);
		v.add(Sentence.class);

		return v;
	}

	public static KanjiNoSensei getApp()
	{
		return app;
	}
	
	private KanjiNoSensei(File fic_config, File fic_dico, File fic_profile, File fic_presets) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, IOException
	{
		assert (app == null);
		
		plugins = listPluginsClasses();

		Iterator<Class<? extends Element>> itPlugins = plugins.iterator();
		while (itPlugins.hasNext())
		{
			Class<? extends Element> plugin = itPlugins.next();
			vuesBlank.put(plugin, VueElement.genererVueBlankElement(plugin, USE_ROMAJI));
		}

		if (fic_config == null)
		{
			fic_config = new File(System.getProperty("KanjiNoSenseiWorkingDirectory") + File.separatorChar + Config.CONFIG_NAME); //$NON-NLS-1$
			Config.open(fic_config);
		}
		if (fic_dico == null)
		{
			fic_dico = new File(System.getProperty("KanjiNoSenseiWorkingDirectory") + File.separatorChar + Config.getString("GeneralConfig.DefaultDictionaryFile", "dico/dico.kjd")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (fic_profile == null)
		{
			fic_profile = new File(System.getProperty("KanjiNoSenseiWorkingDirectory") + File.separatorChar + Config.getString("GeneralConfig.DefaultUserProfile", "myProfile.ulp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (fic_presets == null)
		{
			fic_presets = new File(System.getProperty("KanjiNoSenseiWorkingDirectory") + File.separatorChar + Config.getString("GeneralConfig.DefaultPresets", "presets")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		if (fic_profile.exists() && fic_profile.isFile())
		{
			this.userLearningProfile = LearningProfile.open(fic_profile);
		}
		else if (fic_profile.createNewFile() || fic_profile.canWrite())
		{
			KanjiNoSensei.log(Level.INFO, Messages.getString("KanjiNoSensei.LearningProfile.CreatingFile") + " : \"" + fic_profile.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.userLearningProfile = new LearningProfile();
			this.userLearningProfile.save(fic_profile);
		}
		else
		{
			throw new IOException(Messages.getString("KanjiNoSensei.LearningProfile.Error.CreatingFile")); //$NON-NLS-1$
		}

		if (fic_presets.exists() && fic_presets.isFile())
		{
			this.presets = Presets.open(fic_presets);
		}
		else if (fic_presets.createNewFile() || fic_presets.canWrite())
		{
			KanjiNoSensei.log(Level.INFO, Messages.getString("KanjiNoSensei.Presets.CreatingFile") + " : \"" + fic_presets.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.presets = new Presets();
			this.presets.save(fic_presets);
		}
		else
		{
			throw new IOException(Messages.getString("KanjiNoSensei.Presets.Error.CreatingFile")); //$NON-NLS-1$
		}

		this.userLearningProfileFile = fic_profile;
		this.ficPresets = fic_presets;
		this.dictionnaire = new Dictionary(fic_dico);
		this.dictionnaireQuiz = this.dictionnaire.clone();
		
		app = this;
	}

	private MyCheckBoxTree getMyCheckBoxTree()
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
				public void treeNodesChanged(final MyCheckBoxTreeEvent e)
				{
					if (!SwingUtilities.isEventDispatchThread())
					{
						SwingUtilities.invokeLater(new Runnable()
						{
						
							@Override
							public void run()
							{
								treeNodesChanged(e);
							}
						});
					}
					// We are in EDT
					
					if (e.itemIsSelected)
					{
						MyUtils.trace(Level.FINE, "'" + e.itemPath + "' ajouté"); //$NON-NLS-1$ //$NON-NLS-2$
						themesSelectionnes.add(e.itemPath);
					}
					else
					{
						if (themesSelectionnes.contains(e.itemPath))
						{
							MyUtils.trace(Level.FINE, "'" + e.itemPath + "' retiré"); //$NON-NLS-1$ //$NON-NLS-2$
							themesSelectionnes.remove(e.itemPath);
						}
					}

					// On ne rafraichie que si le CheckBoxTree est stable.
					if (e.isTreeStable)
					{
						MyUtils.trace(Level.INFO, "CALLING afficherBaseFrameMAJZoneElements FROM MyCheckBoxTreeListener.treeNodesChanged");
						getJListThemesSelectionnes().setListData(themesSelectionnes.toArray());
						MyUtils.trace(Level.INFO, "getJListThemesSelectionnes().setListData("+themesSelectionnes+")");
						MyUtils.refreshComponentAndSubs(getJScrollPaneListThemesSelectionnes());
						afficherBaseFrameMAJZoneElements();
					}
				}
			});

			jMyCheckBoxTree.setVisible(true);
		}

		return jMyCheckBoxTree;
	}

	private JMenuItem getAjouterElementMenuItem(final Class<? extends Element> classeElement)
	{
		if ( !jMenusAjouterElement.containsKey(classeElement))
		{
			JMenuItem jMenuItem = new JMenuItem();
			jMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.AddElement") + Messages.getString(classeElement.getSimpleName())); //$NON-NLS-1$
			jMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					Element nouvelElement = vuesBlank.get(classeElement).editerElement();

					if (nouvelElement != null)
					{
						MyUtils.trace(Level.FINE, classeElement.getSimpleName() + " edité : '" + nouvelElement + "'"); //$NON-NLS-1$ //$NON-NLS-2$
						try
						{
							dictionnaire.addElement(nouvelElement);
						}
						catch (DictionaryElementAlreadyPresentException ex)
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("KanjiNoSensei.ErrorElementAlreadyPresent") + " : " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
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
			afficherBaseFrame.setTitle(Messages.getString("KanjiNoSensei.Base.Title")); //$NON-NLS-1$
			afficherBaseFrame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			afficherBaseFrame.setName("Base de connaissance"); //$NON-NLS-1$
			afficherBaseFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			afficherBaseFrame.setContentPane(getAfficherBaseContentPane());
			afficherBaseFrame.setMinimumSize(getAfficherBaseContentPane().getMinimumSize());
			afficherBaseFrame.setMaximumSize(getAfficherBaseContentPane().getMaximumSize());
			afficherBaseFrame.setPreferredSize(getAfficherBaseContentPane().getPreferredSize());
			
			Point loc = getJFrame().getLocation();
			loc.translate(20, 20);
			afficherBaseFrame.setLocation(loc);
			afficherBaseFrame.pack();
			
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
					
					//TODO: Eviter d'apeller ces méthodes lorsque le contenu n'as pas de raison d'avoir changé (lorsqu'on a validé avec "Valider").
					afficherBaseFrameMAJZoneThemes();
					afficherBaseFrameMAJZoneElements();
				}
			});
			// <JiglooProtected>

			/*
			afficherBaseFrame.addWindowListener(new WindowAdapter()
			{
				// </JiglooProtected>
				public void windowOpened(WindowEvent e)
				{
					getJTextFieldThemesFiltre().setText(filtreThemesTxt);
					getJCheckBoxThemesFiltrer().setSelected(filtreThemesActif);
					getJTextFieldElementsFiltre().setText(filtreElementsTxt);
					getJCheckBoxElementsFiltre().setSelected(filtreElementsActif);
					afficherBaseFrame.doLayout();

					afficherBaseFrameMAJZoneThemes();
					afficherBaseFrameMAJZoneElements();
				}
			});
			*/
		}

		return afficherBaseFrame;
	}

	class GetListeThemesTask extends SwingWorker<SortedMap<String, Boolean>, String>
	{

		private final String	filtreTheme;
		
		/**
		 * @return the filtreTheme
		 */
		public String getFiltreTheme()
		{
			return filtreTheme;
		}

		public GetListeThemesTask(String filtreTheme)
		{
			this.filtreTheme = filtreTheme;
		}

		@Override
		protected SortedMap<String, Boolean> doInBackground() throws Exception
		{
			MyUtils.trace(Level.INFO, "<GetListeThemesTask.doInBackground>");
			Set<String> listeThemesFiltre = dictionnaire.getThemesList(filtreTheme);
			SortedMap<String, Boolean> listeThemes = new TreeMap<String, Boolean>();

			for (String theme : listeThemesFiltre)
			{
				listeThemes.put(theme, themesSelectionnes.contains(theme));
			}

			MyUtils.trace(Level.INFO, "<GetListeThemesTask.doInBackground>");
			return listeThemes;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		protected void done()
		{
			MyUtils.trace(Level.INFO, "<GetListeThemesTask.done>");
			try
			{
				getMyCheckBoxTree().setTreeData(get());
				getJListThemesSelectionnes().setListData(themesSelectionnes.toArray());
				MyUtils.trace(Level.INFO, "getJListThemesSelectionnes().setListData("+themesSelectionnes+")");
			}
			catch(CancellationException c)
			{
				MyUtils.trace(Level.INFO, "</GetListeThemesTask.done canceled>");
				return;
			}
			catch (Exception e)
			{
				log(Level.SEVERE, "Unexpected GUI exception: " + e.getMessage());
				e.printStackTrace();
			}
			
			MyUtils.trace(Level.INFO, "</GetListeThemesTask.done>");
		}
	};

	private GetListeThemesTask getListeThemesTask = null;
	private synchronized void afficherBaseFrameMAJZoneThemes()
	{
		if ( !SwingUtilities.isEventDispatchThread())
		{
			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run()
				{
					afficherBaseFrameMAJZoneThemes();
				}
			});

			return;
		}
		// We are in EDT

		String filtreTheme = jTextFieldThemesFiltre.getText();
		if ( !jCheckBoxThemesFiltrer.isSelected())
		{
			filtreTheme = null;
		}

		if (getListeThemesTask != null)
		{
			/*
			String lastFiltreTheme = getListeThemesTask.getFiltreTheme();
			
			if (MyUtils.compareNullableStrings(lastFiltreTheme, filtreTheme))
			{
				return;
			}
			*/
			
			getListeThemesTask.cancel(true);
		}
		
		getListeThemesTask = new GetListeThemesTask(filtreTheme);
		getListeThemesTask.addPropertyChangeListener(this);
		getListeThemesTask.execute();
	}

	static class GetListeElementsTask extends SwingWorker<SortedMap<Element, JPanel>, JPanel>
	{

		final String	filtreElement;
		
		final static MouseWheelListener listedElementMouseWheelListener = new MouseWheelListener()
		{

			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				MyUtils.trace(Level.FINEST, "mouseWheelMoved : " + e); //$NON-NLS-1$
				KanjiNoSensei.app.getJScrollPaneElements().dispatchEvent(e);
			}

		};
		
		/**
		 * @return the filtreElement
		 */
		public String getFiltreElement()
		{
			return filtreElement;
		}

		/**
		 * 
		 */
		public GetListeElementsTask(String filtreElement)
		{
			this.filtreElement = filtreElement;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected SortedMap<Element, JPanel> doInBackground() throws Exception
		{	
			MyUtils.trace(Level.INFO, "<GetListeElementsTask.doInBackground>");
			
			MyUtils.trace(Level.INFO, "</System.gc()>");
			System.gc();
			
			KanjiNoSensei.app.sousDictionnaire = KanjiNoSensei.app.dictionnaire.getSubDictionary(KanjiNoSensei.app.themesSelectionnes);
			Set<Element> listeElementsFiltre = KanjiNoSensei.app.sousDictionnaire.getElementsList(filtreElement);

			SortedMap<Element, JPanel> elementsPanels = new TreeMap<Element, JPanel>();

			Iterator<Element> itElements = listeElementsFiltre.iterator();
			Element element = null;
			MyUtils.trace(Level.FINE, "<Pour Chaque Element>");
			int i = 0;
			while (itElements.hasNext())
			{
				++i;
				MyUtils.trace(Level.FINE, "<Nouvel Element id=" + i + ">");
				element = itElements.next();
				try
				{
					final VueElement vueElement = VueElement.genererVueElement(element, USE_ROMAJI);
					final JPanel vueElementDetaillePanel = vueElement.getVueDetaillePanel().getPanel();

					final JPanel selectablePanel = new JPanel(new BorderLayout(0, 0));
					selectablePanel.add(vueElementDetaillePanel, BorderLayout.WEST);
					selectablePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
					selectablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

					MyUtils.trace(Level.FINE, "<InvokeLater:selectablePanel sizing for Element id=" + i + ">");
					int margin = 4;
					selectablePanel.setMinimumSize(new Dimension(vueElementDetaillePanel.getMinimumSize().width + margin, vueElementDetaillePanel.getMinimumSize().height + margin));
					// selectablePanel.setMaximumSize(new Dimension(vueElementDetaillePanel.getMaximumSize().width + margin, vueElementDetaillePanel.getMaximumSize().height + margin));
					selectablePanel.setPreferredSize(new Dimension(vueElementDetaillePanel.getPreferredSize().width + margin, vueElementDetaillePanel.getPreferredSize().height + margin));

					MyUtils.trace(Level.FINE, "</InvokeLater:selectablePanel sizing for Element id=" + i + ">");

					MyUtils.doItToAllSubComponents(selectablePanel, new DoItToThisComponent()
					{

						@Override
						public void doIt(Component c)
						{
							c.addMouseWheelListener(listedElementMouseWheelListener);

							if (c.getMouseListeners().length > 0)
							{
								// MyUtils.trace("Component has already a mouseListener : "+c); //$NON-NLS-1$
							}
							else
							{
								c.addMouseListener(new MouseAdapter()
								{
									@Override
									public void mouseClicked(MouseEvent e)
									{
										MyUtils.trace(Level.FINEST, "mouseClicked : " + e); //$NON-NLS-1$

										if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 1))
										{
											e.consume();
											KanjiNoSensei.app.afficherBaseDialogMAJSelection(vueElement);
										}
										else if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
										{
											e.consume();
											Element ancienElement = vueElement.getElement();
											Element nouveauElement = vueElement.editerElement();
											if (nouveauElement != null)
											{
												KanjiNoSensei.app.dictionnaire.removeElement(ancienElement);
												try
												{
													KanjiNoSensei.app.dictionnaire.addElement(nouveauElement);
												}
												catch (DictionaryElementAlreadyPresentException e1)
												{
													KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.ErrorElementAlreadyPresent") + " : " + e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
													try
													{
														KanjiNoSensei.app.dictionnaire.addElement(ancienElement);
													}
													catch (DictionaryElementAlreadyPresentException e2)
													{
														KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.Dictionary.AlreadyExistingElement")); //$NON-NLS-1$
													}
												}
											}

											KanjiNoSensei.app.afficherBaseFrameMAJZoneElements();
										}

										super.mouseClicked(e);
									}

								});
							}

						}

					}, true);

					elementsPanels.put(element, selectablePanel);
				}
				catch (Exception e)
				{
					KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.ErrorViewGeneration") + " : \"" + element.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}

				MyUtils.trace(Level.FINE, "</Nouvel Element id=" + i + ">");
			}
			MyUtils.trace(Level.FINE, "</Pour Chaque Element>");

			MyUtils.trace(Level.INFO, "</GetListeElementsTask.doInBackground>");
			return elementsPanels;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		protected void done()
		{
			MyUtils.trace(Level.INFO, "<GetListeElementsTask.done>");
			KanjiNoSensei.app.getJPanelElementsListe().removeAll();

			SortedMap<Element, JPanel> elementsPanels = null;

			try
			{
				elementsPanels = get();
			}
			catch(CancellationException c)
			{
				MyUtils.trace(Level.INFO, "</GetListeElements.done>");
				return;
			}
			catch (Exception e)
			{
				log(Level.SEVERE, "Unexpected GUI exception: " + e.getMessage());
				e.printStackTrace();
			}

			if (elementsPanels != null)
			{
				for (Element e : elementsPanels.keySet())
				{
					JPanel panel = elementsPanels.get(e);
					KanjiNoSensei.app.getJPanelElementsListe().add(panel);
				}
			}
			
			int incMin = 10, incMax = 100; 
			KanjiNoSensei.app.getJScrollPaneElements().getVerticalScrollBar().setUnitIncrement(Math.max(incMin, Math.min(incMax, (KanjiNoSensei.app.getJPanelElementsListe().getComponentCount() * 1))));
			MyUtils.refreshComponentAndSubs(KanjiNoSensei.app.getAfficherBaseContentPane());
			MyUtils.trace(Level.INFO, "</GetListeElements.done>");
		}
	}

	private GetListeElementsTask getListeElementsTask = null;
	
	private synchronized void afficherBaseFrameMAJZoneElements()
	{
		if ( !SwingUtilities.isEventDispatchThread())
		{
			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run()
				{
					afficherBaseFrameMAJZoneElements();
				}
			});

			return;
		}
		// We are in EDT

		String filtreElement = getJTextFieldElementsFiltre().getText();
		if ( !jCheckBoxElementsFiltre.isSelected())
		{
			filtreElement = null;
		}

		if (getListeElementsTask != null)
		{
			/*
			String lastFiltreElements = getListeElementsTask.getFiltreElement();
			if (MyUtils.compareNullableStrings(lastFiltreElements, filtreElement))
			{
				return;
			}
			*/
			
			getListeElementsTask.cancel(true);
		}
		
		getListeElementsTask = new GetListeElementsTask(filtreElement);
		getListeElementsTask.addPropertyChangeListener(this);
		getListeElementsTask.execute();
	}

	public void afficherBaseDialogMAJSelection(final VueElement nouvelleSelection)
	{
		if ( !SwingUtilities.isEventDispatchThread())
		{
			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run()
				{
					afficherBaseDialogMAJSelection(nouvelleSelection);
				}
			});

			return;
		}
		// We are in EDT
		
		boolean deselectionner = false, selectionner = false;

		for (int i = 0; i < getJPanelElementsListe().getComponentCount(); ++i)
		{
			JPanel selectablePanel = (JPanel) getJPanelElementsListe().getComponent(i);
			JPanel panel = ((VueDetaillePanel) selectablePanel.getComponent(0)).getPanel();

			if ((elementSelectionne != null) && (elementSelectionne.getVueDetaillePanel().equals(panel)))
			{
				selectablePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				deselectionner = true;
			}

			if (nouvelleSelection.getVueDetaillePanel().equals(panel))
			{
				selectablePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
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
			afficherBaseContentPane.doLayout();
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
			jLabelRechercher.setText(Messages.getString("KanjiNoSensei.LabelSearch")); //$NON-NLS-1$
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
					MyUtils.trace(Level.FINEST, "jTextFieldThemesFiltre.actionPerformed, event=" + evt); //$NON-NLS-1$
					jTextFieldThemesFiltre.transferFocus();
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
			jCheckBoxThemesFiltrer.setText(Messages.getString("KanjiNoSensei.LabelFilterOn")); //$NON-NLS-1$
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
			jButtonSelectionnerTous.setText(Messages.getString("KanjiNoSensei.ButtonSelectAllSubs")); //$NON-NLS-1$
			jButtonSelectionnerTous.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					MyCheckBoxTree jMyCheckBoxTree = getMyCheckBoxTree();
					jMyCheckBoxTree.setSubTreeSelected(jMyCheckBoxTree.getSelectionPath(), true, Integer.MAX_VALUE);

					//afficherBaseFrameMAJZoneElements();
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
			jButtonDeselectionner.setText(Messages.getString("KanjiNoSensei.ButtonUnselectAllSubs")); //$NON-NLS-1$
			jButtonDeselectionner.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					MyCheckBoxTree jMyCheckBoxTree = getMyCheckBoxTree();
					jMyCheckBoxTree.setSubTreeSelected(jMyCheckBoxTree.getSelectionPath(), false, Integer.MAX_VALUE);

					//afficherBaseFrameMAJZoneElements();
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
			saveDictionnaireMenuItem.setText(Messages.getString("KanjiNoSensei.DictionarySave")); //$NON-NLS-1$

			final JFileChooser fc = new JFileChooser();

			fc.setFileFilter(fileFilterDictionnaire);
			fc.setSelectedFile(new File(System.getProperty("KanjiNoSenseiWorkingDirectory"))); //$NON-NLS-1$

			saveDictionnaireMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					boolean retry = false;

					do
					{
						if (fc.showSaveDialog(getJFrame()) == JFileChooser.APPROVE_OPTION)
						{
							File fic = fc.getSelectedFile();
							try
							{
								// TODO: SwingWorker + boite de progression ?
								dictionnaire.save(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null, Messages.getString("KanjiNoSensei.ErrorBoxDictionarySavingFailed"), Messages.getString("KanjiNoSensei.ErrorBoxDictionarySavingTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
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
			afficherBaseMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.Base")); //$NON-NLS-1$
			afficherBaseMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					JFrame afficherBaseFrame = getAfficherBaseFrame();
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
			jPanelThemesInSplit.add(getJPanelTop(), BorderLayout.NORTH);
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
			jScrollPaneElements = new JScrollPane(getJPanelElementsListe());
			// jScrollPaneElements = new JScrollPane(getJListDisplayedElements());
			jScrollPaneElements.setAutoscrolls(true);
		}
		return jScrollPaneElements;
	}

	/**
	 * This method initializes jPanelElementsListe
	 * 
	 * @return javax.swing.JPanel
	 */
	private Box getJPanelElementsListe()
	{
		if (jPanelElementsListe == null)
		{
			jPanelElementsListe = Box.createVerticalBox();
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
			jPanelThemesListe.add(getJScrollPane1(), BorderLayout.CENTER);
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
			jLabel.setText(Messages.getString("KanjiNoSensei.LabelSearch")); //$NON-NLS-1$
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
			jCheckBoxElementsFiltre.setText(Messages.getString("KanjiNoSensei.LabelFilterOn")); //$NON-NLS-1$
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
			BorderLayout jPanelThemesSelectionLayout = new BorderLayout();
			jPanelThemesSelection.setLayout(jPanelThemesSelectionLayout);
			jPanelThemesSelection.setPreferredSize(new java.awt.Dimension(0, 0));
			jPanelThemesSelection.setMinimumSize(new java.awt.Dimension(135, 200));
			jPanelThemesSelection.setSize(135, 200);
			jPanelThemesSelection.add(getJScrollPaneListThemesSelectionnes(), BorderLayout.CENTER);
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
			jListThemesSelectionnes.setFocusable(false);
		}
		return jListThemesSelectionnes;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		if (MyUtils.checkJREVersion("1.6") == false) //$NON-NLS-1$
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.ErrorBadJREVersion")); //$NON-NLS-1$
			return;
		}

		File ficDico = null;
		File ficLearningProfile = null;
		File ficPresets = null;
		File ficConfig = null;

		// KanjiNoSensei [--dic DICO_FILE] [--profile PROFILE_FILE] [--workingdir WORKING_DIR] [L&F commands]]
		// If files are given in relative path, they are considered relative to specified WORKING_DIR.

		System.setProperty("KanjiNoSenseiWorkingDirectory", System.getProperty("user.dir")); //$NON-NLS-1$ //$NON-NLS-2$

		while ((args.length >= 2) && (args[0].matches("--profile|--presets|--workingdir|--dic|--config"))) //$NON-NLS-1$
		{
			if (args[0].compareTo("--help") == 0)
			{
				System.out.println("KanjiNoSensei command line help:");
				System.out.println("--dic [file]\tDictionary file to use.");
				System.out.println("--config [file]\tConfig file to use.");
				System.out.println("--workingdir [dir]\tWorking directory to use.");
				System.out.println("--profile [file]\tProfile file to use.");
				System.out.println("--presets [file]\tPresets file to use.");
			}
			if (args[0].compareTo("--dic") == 0) //$NON-NLS-1$
			{
				ficDico = new File(args[1]);
			}

			if (args[0].compareTo("--config") == 0) //$NON-NLS-1$
			{
				ficConfig = new File(args[1]);
			}

			if (args[0].compareTo("--workingdir") == 0) //$NON-NLS-1$
			{
				File userdir = new File(args[1]);
				if ( !userdir.isDirectory() || !userdir.canRead())
				{
					KanjiNoSensei.log(Level.SEVERE, "--workingdir is not a directory or does not have read access."); //$NON-NLS-1$
					return;
				}
				else
				{
					System.setProperty("KanjiNoSenseiWorkingDirectory", userdir.getAbsolutePath()); //$NON-NLS-1$
				}
			}
			if (args[0].compareTo("--profile") == 0) //$NON-NLS-1$
			{
				ficLearningProfile = new File(args[1]);
			}
			if (args[0].compareTo("--presets") == 0) //$NON-NLS-1$
			{
				ficPresets = new File(args[1]);
			}
			args = MyUtils.offsetObjectElements(args, 2);
		}
		
		KanjiNoSensei.log(Level.INFO, Messages.getString("KanjiNoSensei.Info.WorkingDirectory") + " : \"" + System.getProperty("KanjiNoSenseiWorkingDirectory") + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		if (args.length > 0)
		{
			try
			{
				MyUtils.manageLookAndFeelsOption(args[0]);
			}
			catch (Exception e)
			{
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.ErrorLook&FeelsManagement") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
		}

		final File finalLearningProfileFile = ficLearningProfile;
		final File finalPresetsFile = ficPresets;
		final File finalDicoFile = ficDico;
		final File finalConfigFile = ficConfig;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				KanjiNoSensei application;
				try
				{
					application = new KanjiNoSensei(finalConfigFile, finalDicoFile, finalLearningProfileFile, finalPresetsFile);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.ErrorRunningApp") + " : " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
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
			jFrame.setTitle(Messages.getString("KanjiNoSensei.Title")); //$NON-NLS-1$
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
			fileMenu.setText(Messages.getString("KanjiNoSensei.Menu.File")); //$NON-NLS-1$
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
			editMenu.setText(Messages.getString("KanjiNoSensei.Menu.Edition")); //$NON-NLS-1$
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
			helpMenu.setText(Messages.getString("KanjiNoSensei.Menu.Help")); //$NON-NLS-1$
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
			exitMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.Quit")); //$NON-NLS-1$
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
			aboutMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.Help.About")); //$NON-NLS-1$
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
			aboutFrame.setTitle(Messages.getString("KanjiNoSensei.Menu.Help.About")); //$NON-NLS-1$
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
			aboutVersionLabel.setText(Messages.getString("KanjiNoSensei.LabelVersion") + " : " + KanjiNoSensei_VERSION); //$NON-NLS-1$ //$NON-NLS-2$
			aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return aboutVersionLabel;
	}

	private JMenuItem getConfigMenuItem()
	{
		if (ConfigMenuItem == null)
		{
			ConfigMenuItem = new JMenuItem();
			ConfigMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.ConfigureQuiz")); //$NON-NLS-1$
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
			jConfigModalFrame.setTitle(Messages.getString("KanjiNoSensei.QuizConfiguration.Title")); //$NON-NLS-1$
			jConfigModalFrame.setPreferredSize(new java.awt.Dimension(0, 0));
			jConfigModalFrame.setMinimumSize(new java.awt.Dimension(600, 300));
			jConfigModalFrame.addComponentListener(new ComponentAdapter()
			{
				// </JiglooProtected>
				public void componentShown(ComponentEvent evt)
				{
					MyUtils.trace(Level.FINEST, "jConfigModalFrame.componentShown"); //$NON-NLS-1$
					for (Component c : getJConfigFrameTabbedPane().getComponents())
					{
						if (VueElement.QuizConfigPanel.class.isInstance(c))
						{
							((VueElement.QuizConfigPanel) c).resetDisplay();
						}
					}

					MyUtils.refreshComponentAndSubs(getJConfigFrameTabbedPane().getSelectedComponent());
					MyUtils.refreshComponentAndSubs(getJConfigFrameTabbedPane());
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
			jConfigFrameTabbedPane.addTab(Messages.getString("KanjiNoSensei.QuizConfiguration.General"), null, getJPanelConfigGenerale(), null); //$NON-NLS-1$
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
			jButtonAnnuler.setText(Messages.getString("KanjiNoSensei.ButtonCancel")); //$NON-NLS-1$
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
			jButtonOK.setText(Messages.getString("KanjiNoSensei.ButtonValidate")); //$NON-NLS-1$
			jButtonOK.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					MyUtils.trace(Level.FINEST, "jButtonOK.actionPerformed, event=" + evt); //$NON-NLS-1$

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
					Config.setString("GeneralConfig.UseRomaji", Boolean.toString(USE_ROMAJI)); //$NON-NLS-1$

					Config.save();
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
			jButtonValider.setText(Messages.getString("KanjiNoSensei.ButtonValidate")); //$NON-NLS-1$
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
			jButtonBaseConnaissanceAnnuler.setText(Messages.getString("KanjiNoSensei.ButtonCancel")); //$NON-NLS-1$
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
			jFrameQuizz.setTitle(Messages.getString("KanjiNoSensei.Quizz.Title")); //$NON-NLS-1$
			jFrameQuizz.setName("Quiz"); //$NON-NLS-1$
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

		MyUtils.trace(Level.INFO, "<Boucle getNextElementFromLearningProfile(...)>");
		do
		{
			try
			{
				elementQuestionEnCours = dictionnaireQuizEnCours.getNextElementFromLearningProfile(dejaVus, userLearningProfile);
				// elementQuestionEnCours = dictionnaireQuizEnCours.getRandomElement(dejaVus);
				vueElementQuestionEnCours = VueElement.genererVueElement(elementQuestionEnCours, USE_ROMAJI);
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
				--nbQuestions;

				e.printStackTrace();
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.ErrorViewGeneration") + " : \"" + elementQuestionEnCours.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				continue;
			}

			try
			{
				panelQuestion = vueElementQuestionEnCours.getQuizQuestionPanel().getPanel();
				panelSaisieReponse = vueElementQuestionEnCours.getQuizSaisieReponsePanel().getPanel();
			}
			catch (NoAffException e)
			{
				MyUtils.trace(Level.INFO, "NoAffException: "+e.getMessage());
				panelQuestion = null;
				panelSaisieReponse = null;
				dejaVus.add(elementQuestionEnCours);
				--nbQuestions;
			}
			catch (NoSaisieException e)
			{
				MyUtils.trace(Level.INFO, "NoSaisieException: "+e.getMessage());
				panelQuestion = null;
				panelSaisieReponse = null;
				dejaVus.add(elementQuestionEnCours);
				--nbQuestions;
			}

		} while ((panelQuestion == null) || (panelSaisieReponse == null));
		MyUtils.trace(Level.INFO, "</Boucle getNextElementFromLearningProfile(...)>");

		getJPanelQuizAffElement().add(panelQuestion, BorderLayout.CENTER);

		getJPanelQuizSaisieReponse().removeAll();
		panelSaisieReponse.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
		getJPanelQuizSaisieReponse().add(panelSaisieReponse, BorderLayout.CENTER);

		getJPanelQuizAffReponse().removeAll();
		getJPanelQuizAffReponse().setBackground(defaultBgColor);

		miseAJourQuizTitle();

		MyUtils.refreshComponent(getJQuizFrame());
		getJQuizFrame().setVisible(true);

		MyUtils.InvokeNoEDT(new Runnable()
		{
		
			@Override
			public void run()
			{
				try
				{
					MyUtils.trace(Level.INFO, "<userLearningProfile.save(...)>");
					userLearningProfile.save(userLearningProfileFile);
					MyUtils.trace(Level.INFO, "</userLearningProfile.save(...)>");
				}
				catch (IOException e)
				{
					KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.LearningProfile.Error.SavingFile") + e.getMessage()); //$NON-NLS-1$
				}
			}
		});
		
	}

	private void miseAJourQuizTitle()
	{
		getJQuizFrame().setTitle(Messages.getString("KanjiNoSensei.QuizCurrentQuestionNumber") + " : " + questionCourante +"; " + Messages.getString("KanjiNoSensei.QuizCorrectsAnswers") + " : " + bonnesReponses + "/"+ nbQuestions + "; " + Messages.getString("KanjiNoSensei.QuizIncorrectsAnswers") + " : " + erreurs); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		getJQuizFrame().setTitle(getJQuizFrame().getTitle() + "   " + "Stats [" + userLearningProfile.getElementStats(elementQuestionEnCours.getKey()) + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

		userLearningProfile.addToStats(elementQuestionEnCours.getKey(), reponseCorrecte);

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

		}, true);

		jPanelQuizAffReponse.add(panelSolution, BorderLayout.CENTER);
		final JButton buttonContinuer = new JButton(Messages.getString("KanjiNoSensei.ButtonNext")); //$NON-NLS-1$

		buttonContinuer.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				poserQuestionSuivanteQuiz();
			}

		});

		jPanelQuizAffReponse.add(buttonContinuer, BorderLayout.SOUTH);
		buttonContinuer.grabFocus();
		
		MyUtils.refreshComponent(getJQuizFrame());
		getJQuizFrame().setVisible(true);
	}

	protected void finirQuiz()
	{
		String msg = String.format(Messages.getString("KanjiNoSensei.QuizCurrentQuestionNumber") + " :\t%d\n" + Messages.getString("KanjiNoSensei.QuizCorrectsAnswers") + " :\t%d\n" + Messages.getString("KanjiNoSensei.QuizIncorrectsAnswers") + " :\t%d", questionCourante, bonnesReponses, erreurs); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

		JOptionPane.showMessageDialog(null, msg, Messages.getString("KanjiNoSensei.QuizFinished"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
		getJQuizFrame().dispose();
	}

	/**
	 * Initialise les variables utile au lancement d'un Quiz.
	 */
	protected void initialiserQuiz()
	{
		dictionnaireQuizEnCours = dictionnaireQuiz.clone();
		nbQuestions = dictionnaireQuiz.getNbElements();
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
			FormLayout jPanelConfigGeneraleLayout = new FormLayout("max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)", "max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$ //$NON-NLS-2$
			jPanelConfigGenerale.setLayout(jPanelConfigGeneraleLayout);
			jPanelConfigGenerale.setDoubleBuffered(false);
			jPanelConfigGenerale.setPreferredSize(new java.awt.Dimension(582, 201));
			getJCheckBoxUseRomaji().setSelected(USE_ROMAJI);

			jPanelConfigGenerale.add(getJCheckBoxUseRomaji(), new CellConstraints("1, 1, 1, 1, default, default")); //$NON-NLS-1$
		}
		return jPanelConfigGenerale;
	}

	private JMenuItem getLancerQuizMenuItem()
	{
		if (lancerQuizMenuItem == null)
		{
			lancerQuizMenuItem = new JMenuItem();
			lancerQuizMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.RunQuiz")); //$NON-NLS-1$
			lancerQuizMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					MyUtils.trace(Level.FINEST, "lancerQuizMenuItem.actionPerformed, event=" + evt); //$NON-NLS-1$

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
			exportDicoMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.DictionaryExport")); //$NON-NLS-1$

			final JFileChooser fc = new JFileChooser();

			fc.setFileFilter(fileFilterDictionnaireExport);
			fc.setSelectedFile(new File(System.getProperty("KanjiNoSenseiWorkingDirectory"))); //$NON-NLS-1$

			exportDicoMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					boolean retry = false;
					do
					{
						if (fc.showSaveDialog(getJFrame()) == JFileChooser.APPROVE_OPTION)
						{
							File fic = fc.getSelectedFile();
							try
							{
								//TODO: SwingWorker + progress bar ?
								dictionnaire.exportFile(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null, Messages.getString("KanjiNoSensei.ErrorDictionaryExport"), Messages.getString("KanjiNoSensei.ErrorDictionaryExportTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
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
			importMenuItem.setText(Messages.getString("KanjiNoSensei.Menu.DictionaryImport")); //$NON-NLS-1$

			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter(fileFilterDictionnaireExport);
			fc.setSelectedFile(new File(System.getProperty("KanjiNoSenseiWorkingDirectory"))); //$NON-NLS-1$

			importMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					boolean retry = false;

					do
					{
						if (fc.showOpenDialog(getJFrame()) == JFileChooser.APPROVE_OPTION)
						{
							File fic = fc.getSelectedFile();
							try
							{
								// TODO: SwingWorker + progress bar ?
								dictionnaire.importFile(fic);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
								retry = (JOptionPane.showConfirmDialog(null, Messages.getString("KanjiNoSensei.ErrorDictionaryImport"), Messages.getString("KanjiNoSensei.ErrorDictionaryImportTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
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
			jScrollPane1.setViewportView(getMyCheckBoxTree());
		}
		return jScrollPane1;
	}

	private JLabel getJLabelAuthor()
	{
		if (jLabelAuthor == null)
		{
			jLabelAuthor = new JLabel();
			jLabelAuthor.setText(Messages.getString("KanjiNoSensei.Author") + " : Pierre Escallier"); //$NON-NLS-1$ //$NON-NLS-2$
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
			jLabelLien.setText("http://kanjinosensei.axan.org"); //$NON-NLS-1$
			jLabelLien.setEditable(false);
			jLabelLien.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					MyUtils.InvokeNoEDT(new Runnable()
					{
					
						@Override
						public void run()
						{
							String[] browsers = {"firefox", "iexplore", "netscape", "opera"};					
							
							for(int i=0; i < browsers.length; ++i)
							{
								try
								{
									Runtime.getRuntime().exec(browsers[i] + " "+jLabelLien.getText());
								}
								catch(IOException e)
								{
									if (i < browsers.length)
									{
										continue;
									}
									else
									{
										KanjiNoSensei.log(Level.SEVERE, Messages.getString("KanjiNoSensei.ErrorUnableToFindInternetBrowser")); //$NON-NLS-1$
										return;
									}
								}
								
								return;
							}					
						}
					});					
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
			jCheckBoxUseRomaji.setText(Messages.getString("KanjiNoSensei.UseRomaji")); //$NON-NLS-1$
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

	private JPanel getJPanelTop()
	{
		if (jPanelTop == null)
		{
			jPanelTop = new JPanel();
			BorderLayout jPanelTopLayout = new BorderLayout();
			jPanelTop.setLayout(jPanelTopLayout);
			jPanelTop.add(getJPanelPresets(), BorderLayout.NORTH);
			jPanelTop.add(getJPanelThemesFiltres(), BorderLayout.CENTER);
		}
		return jPanelTop;
	}

	private JPanel getJPanelPresets()
	{
		if (jPanelPresets == null)
		{
			jPanelPresets = new JPanel();
			GridBagLayout jPanelPresetsLayout = new GridBagLayout();
			jPanelPresetsLayout.rowWeights = new double[] {0.1};
			jPanelPresetsLayout.rowHeights = new int[] {7};
			jPanelPresetsLayout.columnWeights = new double[] {0.0, 0.0, 0.1};
			jPanelPresetsLayout.columnWidths = new int[] {110, 262, 7};
			jPanelPresets.setLayout(jPanelPresetsLayout);
			jPanelPresets.setPreferredSize(new java.awt.Dimension(788, 30));
			jPanelPresets.add(getJLabelPreset(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
			jPanelPresets.add(getJComboBoxPresets(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPresets.add(getJPanelPresetsBtns(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		}
		return jPanelPresets;
	}

	private JLabel getJLabelPreset()
	{
		if (jLabelPreset == null)
		{
			jLabelPreset = new JLabel();
			jLabelPreset.setText(Messages.getString("KanjiNoSensei.Presets.LabelPresets")); //$NON-NLS-1$
			jLabelPreset.setLayout(null);
		}
		return jLabelPreset;
	}

	private JComboBox getJComboBoxPresets()
	{
		if (jComboBoxPresets == null)
		{

			ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(presets.getPresetsList());
			jComboBoxPresets = new JComboBox();
			jComboBoxPresets.setModel(jComboBox1Model);
			jComboBoxPresets.setEditable(true);
		}
		return jComboBoxPresets;
	}

	private JPanel getJPanelPresetsBtns()
	{
		if (jPanelPresetsBtns == null)
		{
			jPanelPresetsBtns = new JPanel();
			FlowLayout jPanelPresetsBtnsLayout = new FlowLayout();
			jPanelPresetsBtnsLayout.setAlignment(FlowLayout.LEFT);
			jPanelPresetsBtnsLayout.setHgap(0);
			jPanelPresetsBtns.setLayout(jPanelPresetsBtnsLayout);
			jPanelPresetsBtns.add(getJButtonCharger());
			jPanelPresetsBtns.add(getJButtonSave());
			jPanelPresetsBtns.add(getJButtonSupprimer());
		}
		return jPanelPresetsBtns;
	}

	private JButton getJButtonCharger()
	{
		if (jButtonCharger == null)
		{
			jButtonCharger = new JButton();
			jButtonCharger.setText(Messages.getString("KanjiNoSensei.Presets.BtnCharger")); //$NON-NLS-1$
			jButtonCharger.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					MyUtils.trace(Level.FINEST, "jButtonCharger.actionPerformed, event=" + evt); //$NON-NLS-1$
					Vector<String> themes = presets.getPreset(getJComboBoxPresets().getSelectedItem().toString());
					Map<String, Boolean> listeThemes = new HashMap<String, Boolean>();
					
					for(String theme : themes)
					{
						listeThemes.put(theme, true);
					}
					
					getMyCheckBoxTree().addTreeData(listeThemes);

					// afficherBaseFrameMAJZoneThemes();
					// afficherBaseFrameMAJZoneElements();
				}
			});
		}
		return jButtonCharger;
	}

	private JButton getJButtonSave()
	{
		if (jButtonSave == null)
		{
			jButtonSave = new JButton();
			jButtonSave.setText(Messages.getString("KanjiNoSensei.Presets.BtnSauver")); //$NON-NLS-1$
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					MyUtils.trace(Level.FINEST, "jButtonSave.actionPerformed, event=" + evt); //$NON-NLS-1$

					Vector<String> themes = new Vector<String>();

					for (int i = 0; i < getJListThemesSelectionnes().getModel().getSize(); ++i)
					{
						themes.add((String) getJListThemesSelectionnes().getModel().getElementAt(i));
					}

					presets.addPreset(getJComboBoxPresets().getSelectedItem().toString(), themes);
					try
					{
						presets.save(ficPresets);
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getJComboBoxPresets().setModel(new DefaultComboBoxModel(presets.getPresetsList()));
				}
			});
		}
		return jButtonSave;
	}

	private JButton getJButtonSupprimer()
	{
		if (jButtonSupprimer == null)
		{
			jButtonSupprimer = new JButton();
			jButtonSupprimer.setText(Messages.getString("KanjiNoSensei.Presets.BtnSupprimer")); //$NON-NLS-1$
			jButtonSupprimer.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					MyUtils.trace(Level.FINEST, "jButtonSupprimer.actionPerformed, event=" + evt); //$NON-NLS-1$
					presets.removePreset(getJComboBoxPresets().getSelectedItem().toString());
					try
					{
						presets.save(ficPresets);
					}
					catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getJComboBoxPresets().setModel(new DefaultComboBoxModel(presets.getPresetsList()));
				}
			});
		}
		return jButtonSupprimer;
	}

	private JScrollPane getJScrollPaneListThemesSelectionnes()
	{
		if (jScrollPaneListThemesSelectionnes == null)
		{
			jScrollPaneListThemesSelectionnes = new JScrollPane();
			jScrollPaneListThemesSelectionnes.setViewportView(getJListThemesSelectionnes());
		}
		return jScrollPaneListThemesSelectionnes;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public synchronized void propertyChange(PropertyChangeEvent evt)
	{
		Object swingWorker = evt.getSource().getClass().getName();
		boolean doesWork = (workingSwingWorker.get(swingWorker) == null)?false:workingSwingWorker.get(swingWorker);
		
		if ("state".equals(evt.getPropertyName()))
		{
			switch(((SwingWorker.StateValue) evt.getNewValue()))
			{
			case STARTED:
			{
				//MyUtils.assertFalse(doesWork, swingWorker+" already in work");
				workingSwingWorker.put(swingWorker, true);
				break;
			}
			case DONE:
			{
				//MyUtils.assertTrue(doesWork, swingWorker+" was not working");
				workingSwingWorker.put(swingWorker, false);
				break;
			}
			}
		}
		
		int cur = Cursor.DEFAULT_CURSOR;
		for(Boolean work: workingSwingWorker.values())
		{
			if (work == true)
			{
				cur = Cursor.WAIT_CURSOR;
				break;
			}
		}
		
		MyUtils.trace(Level.INFO, swingWorker+" "+evt.getNewValue()+"; workingSwingWorker: "+workingSwingWorker);
		getAfficherBaseFrame().setCursor(Cursor.getPredefinedCursor(cur));
	}
	private HashMap<Object, Boolean> workingSwingWorker = new HashMap<Object, Boolean>();
	
}
