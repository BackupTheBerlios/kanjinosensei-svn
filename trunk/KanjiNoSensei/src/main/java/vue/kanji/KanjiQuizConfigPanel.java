package vue.kanji;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import metier.elements.Kanji;
import utils.Messages;
import utils.MyUtils;
import vue.Config;
import vue.VueElement.QuizConfigPanel;

import com.jgoodies.forms.layout.CellConstraints;
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
class KanjiQuizConfigPanel extends javax.swing.JPanel implements QuizConfigPanel
{

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); //$NON-NLS-1$
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * @author Axan
	 * 
	 */
	static public enum ETypeAff
	{
		Kanji, LectureOrigineChinoise, LectureJaponaise, ImageTrace, Signification, KanjiEtSignification, KanjiEtLectures, Detaille, ImageTraceEtDetaille;
	}
	static private HashMap<String, ETypeAff> typesAff = new HashMap<String, ETypeAff>();
	{
		for(ETypeAff type : ETypeAff.values())
		{
			typesAff.put(metier.Messages.getString(Kanji.class.getSimpleName()+".OM."+type.toString()), type);
		}
	}

	public enum ETypeSaisie
	{
		Kanji, LectureOrigineChinoise, LectureJaponaise, Signification, ListeChoix, AttenteClick;
	}
	static private HashMap<String, ETypeSaisie> typesSaisie = new HashMap<String, ETypeSaisie>();
	{
		for(ETypeSaisie type : ETypeSaisie.values())
		{
			typesSaisie.put(metier.Messages.getString(Kanji.class.getSimpleName()+".IM."+type.toString()), type);
		}
	}

	private JLabel						jLabelTitre;
	private JLabel						jLabelAffKanji;
	private JComboBox					jComboBoxAffQuiz;

	private JLabel						jLabelAffReponse;
	private JCheckBox jCheckBoxSaisieReponseComplete;
	private JLabel jLabelSaisieReponseListe;
	private JComboBox					jComboBoxAffReponse;
	private JComboBox					jComboBoxSaisieReponseChoixAff;
	private JComboBox					jComboBoxSaisieReponse;
	private JLabel						jLabelMethodeSaisie;

	private ETypeAff	affichageElementQuiz = ETypeAff.Kanji;
	private ETypeSaisie saisieReponseQuiz = ETypeSaisie.LectureJaponaise;
	private ETypeAff	affichageChoixReponsesQuiz = ETypeAff.LectureJaponaise;
	private ETypeAff	affichageReponseQuiz = ETypeAff.LectureJaponaise;
	private boolean		saisieReponseCompleteQuiz = false;
	
	private static KanjiQuizConfigPanel	KanjiQuizConfigPanelSingleton	= null;

	public static KanjiQuizConfigPanel getKanjiQuizConfigPanel()
	{
		if (KanjiQuizConfigPanelSingleton == null)
		{
			KanjiQuizConfigPanelSingleton = new KanjiQuizConfigPanel();
		}

		KanjiQuizConfigPanelSingleton.miseAJourForm();
		return KanjiQuizConfigPanelSingleton;
	}

	private KanjiQuizConfigPanel()
	{
		super();
		initGUI();
		loadConfig();
	}

	private void loadConfig()
	{
		affichageElementQuiz = ETypeAff.valueOf(Config.getString("KanjiQuizConfig.affichageElementQuiz", ETypeAff.Signification.toString()));
		saisieReponseQuiz = ETypeSaisie.valueOf(Config.getString("KanjiQuizConfig.saisieReponseQuiz", ETypeSaisie.AttenteClick.toString()));
		affichageChoixReponsesQuiz = ETypeAff.valueOf(Config.getString("KanjiQuizConfig.affichageChoixReponsesQuiz", ETypeAff.LectureJaponaise.toString()));
		affichageReponseQuiz = ETypeAff.valueOf(Config.getString("KanjiQuizConfig.affichageReponseQuiz", ETypeAff.ImageTraceEtDetaille.toString()));
		saisieReponseCompleteQuiz = Boolean.valueOf(Config.getString("KanjiQuizConfig.saisieReponseCompleteQuiz", "false"));
	}
	
	private void saveConfig()
	{
		Config.setString("KanjiQuizConfig.affichageElementQuiz", affichageElementQuiz.toString());
		Config.setString("KanjiQuizConfig.saisieReponseQuiz", saisieReponseQuiz.toString());
		Config.setString("KanjiQuizConfig.affichageChoixReponsesQuiz", affichageChoixReponsesQuiz.toString());
		Config.setString("KanjiQuizConfig.affichageReponseQuiz", affichageReponseQuiz.toString());
		Config.setString("KanjiQuizConfig.saisieReponseCompleteQuiz", Boolean.toString(saisieReponseCompleteQuiz));
	}
	
	private void initGUI()
	{
		try
		{
			FormLayout thisLayout = new FormLayout(
					"min(m;73dlu):grow, min(p;200dlu):grow",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;15dlu), max(p;15dlu), max(p;5dlu)"); //$NON-NLS-1$
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(400, 150));
			this.setMinimumSize(new java.awt.Dimension(600, 300));
			{
				jLabelTitre = new JLabel();
				this.add(jLabelTitre, new CellConstraints("1, 1, 1, 1, right, default")); //$NON-NLS-1$
				jLabelTitre.setText(Messages.getString("KanjiQuizConfigPanel.LabelTitle")); //$NON-NLS-1$
				jLabelTitre.setFont(new java.awt.Font("SimSun", 1, 16)); //$NON-NLS-1$
			}
			{
				jLabelAffKanji = new JLabel();
				this.add(jLabelAffKanji, new CellConstraints("1, 2, 1, 1, default, default")); //$NON-NLS-1$
				jLabelAffKanji.setText(Messages.getString("KanjiQuizConfigPanel.LabelQuizDisplay")); //$NON-NLS-1$
			}
			{	
				ComboBoxModel jComboBoxAffQuizModel = new DefaultComboBoxModel(typesAff.keySet().toArray());
				jComboBoxAffQuiz = new JComboBox();
				this.add(jComboBoxAffQuiz, new CellConstraints("2, 2, 1, 1, left, default")); //$NON-NLS-1$
				jComboBoxAffQuiz.setModel(jComboBoxAffQuizModel);
			}
			{
				jLabelMethodeSaisie = new JLabel();
				this.add(jLabelMethodeSaisie, new CellConstraints("1, 3, 1, 1, left, default")); //$NON-NLS-1$
				jLabelMethodeSaisie.setText(Messages.getString("KanjiQuizConfigPanel.LabelAnswerInputMethod")); //$NON-NLS-1$
			}
			{
				ComboBoxModel jComboBoxSaisieReponseModel = new DefaultComboBoxModel(typesSaisie.keySet().toArray());
				jComboBoxSaisieReponse = new JComboBox();
				this.add(jComboBoxSaisieReponse, new CellConstraints("2, 3, 1, 1, left, default")); //$NON-NLS-1$
				jComboBoxSaisieReponse.setModel(jComboBoxSaisieReponseModel);
				jComboBoxSaisieReponse.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent evt)
					{
						jComboBoxSaisieReponseChoixAff.setEnabled((typesSaisie.get(jComboBoxSaisieReponse.getSelectedItem()).equals(ETypeSaisie.ListeChoix)));
						switch (typesSaisie.get(jComboBoxSaisieReponse.getSelectedItem()))
						{
							case Signification:
							case LectureJaponaise:
							case LectureOrigineChinoise:								
								jCheckBoxSaisieReponseComplete.setEnabled(true);
							break;
							
							default:
								jCheckBoxSaisieReponseComplete.setEnabled(false);
						}
					}
				});
			}
			{
				ComboBoxModel jComboBoxSaisieReponseChoixAffModel = new DefaultComboBoxModel(typesAff.keySet().toArray());
				jComboBoxSaisieReponseChoixAff = new JComboBox();
				this.add(jComboBoxSaisieReponseChoixAff, new CellConstraints("2, 5, 1, 1, left, default")); //$NON-NLS-1$
				jComboBoxSaisieReponseChoixAff.setModel(jComboBoxSaisieReponseChoixAffModel);
			}
			{
				jLabelAffReponse = new JLabel();
				this.add(jLabelAffReponse, new CellConstraints("1, 6, 1, 1, left, default")); //$NON-NLS-1$
				jLabelAffReponse.setText(Messages.getString("KanjiQuizConfigPanel.LabelAnswerDisplay")); //$NON-NLS-1$
			}
			{
				ComboBoxModel jComboBoxAffReponseModel = new DefaultComboBoxModel(typesAff.keySet().toArray());
				jComboBoxAffReponse = new JComboBox();
				this.add(jComboBoxAffReponse, new CellConstraints("2, 6, 1, 1, left, default")); //$NON-NLS-1$
				this.add(getJLabelSaisieReponseListe(), new CellConstraints("1, 5, 1, 1, left, default")); //$NON-NLS-1$
				this.add(getJCheckBoxSaisieReponseComplete(), new CellConstraints("2, 4, 1, 1, default, default")); //$NON-NLS-1$
				jComboBoxAffReponse.setModel(jComboBoxAffReponseModel);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void miseAJourForm()
	{
		jComboBoxAffQuiz.setSelectedItem(metier.Messages.getString(Kanji.class.getSimpleName()+".OM."+affichageElementQuiz.toString()));
		jComboBoxSaisieReponse.setSelectedItem(metier.Messages.getString(Kanji.class.getSimpleName()+".IM."+saisieReponseQuiz.toString()));
		jComboBoxSaisieReponseChoixAff.setSelectedItem(metier.Messages.getString(Kanji.class.getSimpleName()+".OM."+affichageChoixReponsesQuiz.toString()));
		jComboBoxAffReponse.setSelectedItem(metier.Messages.getString(Kanji.class.getSimpleName()+".OM."+affichageReponseQuiz.toString()));
		jCheckBoxSaisieReponseComplete.setSelected(saisieReponseCompleteQuiz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.QuizConfigPanel#getPanel()
	 */
	public JPanel getPanel()
	{
		return this;
	}
	
	private JLabel getJLabelSaisieReponseListe() {
		if(jLabelSaisieReponseListe == null) {
			jLabelSaisieReponseListe = new JLabel();
			jLabelSaisieReponseListe.setText(Messages.getString("KanjiQuizConfigPanel.LabelListDisplay")); //$NON-NLS-1$
		}
		return jLabelSaisieReponseListe;
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizConfigPanel#valider()
	 */
	public void valider()
	{
		affichageElementQuiz = typesAff.get(jComboBoxAffQuiz.getSelectedItem().toString());
		saisieReponseQuiz = typesSaisie.get(jComboBoxSaisieReponse.getSelectedItem().toString());
		affichageChoixReponsesQuiz = typesAff.get(jComboBoxSaisieReponseChoixAff.getSelectedItem().toString());
		affichageReponseQuiz = typesAff.get(jComboBoxAffReponse.getSelectedItem().toString());
		saisieReponseCompleteQuiz = jCheckBoxSaisieReponseComplete.isSelected();
		
		saveConfig();
	}

	protected ETypeAff getAffichageElementQuiz()
	{
		return affichageElementQuiz;
	}

	protected ETypeSaisie getSaisieReponseQuiz()
	{
		return saisieReponseQuiz;
	}

	protected ETypeAff getAffichageChoixReponsesQuiz()
	{
		return affichageChoixReponsesQuiz;
	}

	protected ETypeAff getAffichageReponseQuiz()
	{
		return affichageReponseQuiz;
	}
	
	protected boolean getSaisieReponseCompleteQuiz()
	{
		return saisieReponseCompleteQuiz;
	}
	
	private JCheckBox getJCheckBoxSaisieReponseComplete() {
		if(jCheckBoxSaisieReponseComplete == null) {
			jCheckBoxSaisieReponseComplete = new JCheckBox();
			jCheckBoxSaisieReponseComplete.setText(Messages.getString("KanjiQuizConfigPanel.LabelCompleteAnswer")); //$NON-NLS-1$
		}
		return jCheckBoxSaisieReponseComplete;
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizConfigPanel#resetDisplay()
	 */
	@Override
	public void resetDisplay()
	{
		miseAJourForm();
	}

}
