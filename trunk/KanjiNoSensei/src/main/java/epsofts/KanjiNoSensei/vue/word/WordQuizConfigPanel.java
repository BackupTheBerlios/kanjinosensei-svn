package epsofts.KanjiNoSensei.vue.word;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.vue.Config;
import epsofts.KanjiNoSensei.vue.VueElement.QuizConfigPanel;

class WordQuizConfigPanel extends javax.swing.JPanel implements QuizConfigPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public enum ETypeAff
	{
		Kanji, Lecture, Son, Signification, Detaille;
	};
	static private HashMap<String, ETypeAff> typesAff = new HashMap<String, ETypeAff>();
	{
		for(ETypeAff type : ETypeAff.values())
		{
			typesAff.put(epsofts.KanjiNoSensei.metier.Messages.getString(Word.class.getSimpleName()+".OM."+type.toString()), type); //$NON-NLS-1$
		}
	}

	public enum ETypeSaisie
	{
		Kanji, Lecture, Signification, ListeChoix, AttenteClick;
	};
	static private HashMap<String, ETypeSaisie> typesSaisie = new HashMap<String, ETypeSaisie>();
	{
		for(ETypeSaisie type : ETypeSaisie.values())
		{
			typesSaisie.put(epsofts.KanjiNoSensei.metier.Messages.getString(Word.class.getSimpleName()+".IM."+type.toString()), type); //$NON-NLS-1$
		}
	}
	
	private static WordQuizConfigPanel	MotQuizConfigPanelSingleton	= null;

	public static WordQuizConfigPanel getWordQuizConfigPanel()
	{
		if (MotQuizConfigPanelSingleton == null)
		{
			MotQuizConfigPanelSingleton = new WordQuizConfigPanel();
		}

		MotQuizConfigPanelSingleton.miseAJourForm();
		return MotQuizConfigPanelSingleton;
	}

	private JLabel	jLabelTitre;
	private JLabel	jLabelAffMot;
	private JComboBox	jComboBoxAffQuiz;
	private JLabel	jLabelMethodeSaisie;
	private JComboBox	jComboBoxSaisieReponse;
	private JComboBox	jComboBoxSaisieReponseChoixAff;
	private JLabel	jLabelAffReponse;
	private JComboBox	jComboBoxAffReponse;
	
	private ETypeAff	affichageElementQuiz = ETypeAff.Kanji;
	private ETypeSaisie saisieReponseQuiz = ETypeSaisie.Lecture;
	private ETypeAff	affichageChoixReponsesQuiz = ETypeAff.Lecture;
	private ETypeAff	affichageReponseQuiz = ETypeAff.Lecture;
	private JLabel	jLabelSaisieReponseListe;
	
	private void miseAJourForm()
	{
		jComboBoxAffQuiz.setSelectedItem(epsofts.KanjiNoSensei.metier.Messages.getString(Word.class.getSimpleName()+".OM."+affichageElementQuiz.toString())); //$NON-NLS-1$
		jComboBoxSaisieReponse.setSelectedItem(epsofts.KanjiNoSensei.metier.Messages.getString(Word.class.getSimpleName()+".IM."+saisieReponseQuiz.toString())); //$NON-NLS-1$
		jComboBoxSaisieReponseChoixAff.setSelectedItem(epsofts.KanjiNoSensei.metier.Messages.getString(Word.class.getSimpleName()+".OM."+affichageChoixReponsesQuiz.toString())); //$NON-NLS-1$
		jComboBoxAffReponse.setSelectedItem(epsofts.KanjiNoSensei.metier.Messages.getString(Word.class.getSimpleName()+".OM."+affichageReponseQuiz.toString())); //$NON-NLS-1$
	}

	private WordQuizConfigPanel()
	{
		super();
		initGUI();
		loadConfig();
	}

	private void loadConfig()
	{
		affichageElementQuiz = ETypeAff.valueOf(Config.getString("WordQuizConfig.affichageElementQuiz", ETypeAff.Signification.toString())); //$NON-NLS-1$
		saisieReponseQuiz = ETypeSaisie.valueOf(Config.getString("WordQuizConfig.saisieReponseQuiz", ETypeSaisie.AttenteClick.toString())); //$NON-NLS-1$
		affichageChoixReponsesQuiz = ETypeAff.valueOf(Config.getString("WordQuizConfig.affichageChoixReponsesQuiz", ETypeAff.Kanji.toString())); //$NON-NLS-1$
		affichageReponseQuiz = ETypeAff.valueOf(Config.getString("WordQuizConfig.affichageReponseQuiz", ETypeAff.Detaille.toString())); //$NON-NLS-1$
	}
	
	private void saveConfig()
	{
		Config.setString("WordQuizConfig.affichageElementQuiz", affichageElementQuiz.toString()); //$NON-NLS-1$
		Config.setString("WordQuizConfig.saisieReponseQuiz", saisieReponseQuiz.toString()); //$NON-NLS-1$
		Config.setString("WordQuizConfig.affichageChoixReponsesQuiz", affichageChoixReponsesQuiz.toString()); //$NON-NLS-1$
		Config.setString("WordQuizConfig.affichageReponseQuiz", affichageReponseQuiz.toString()); //$NON-NLS-1$
	}
	private void initGUI()
	{
		try
		{
			FormLayout thisLayout = new FormLayout(
					"min(m;73dlu):grow, min(p;200dlu):grow",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;15dlu), max(p;5dlu)"); //$NON-NLS-1$
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(400, 119));
			this.setMinimumSize(new java.awt.Dimension(600, 300));
			{
				jLabelTitre = new JLabel();
				this.add(jLabelTitre, new CellConstraints("1, 1, 1, 1, right, default")); //$NON-NLS-1$
				jLabelTitre.setText(Messages.getString("WordQuizConfigPanel.LabelTitle")); //$NON-NLS-1$
				jLabelTitre.setFont(new java.awt.Font("SimSun", 1, 16)); //$NON-NLS-1$
			}
			{
				jLabelAffMot = new JLabel();
				this.add(jLabelAffMot, new CellConstraints("1, 2, 1, 1, default, default")); //$NON-NLS-1$
				jLabelAffMot.setText(Messages.getString("WordQuizConfigPanel.QuizDisplay")); //$NON-NLS-1$
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
				jLabelMethodeSaisie.setText(Messages.getString("WordQuizConfigPanel.AnswerInputMethod")); //$NON-NLS-1$
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
					}
				});
			}
			{
				ComboBoxModel jComboBoxSaisieReponseChoixAffModel = new DefaultComboBoxModel(typesAff.keySet().toArray());
				jComboBoxSaisieReponseChoixAff = new JComboBox();
				this.add(jComboBoxSaisieReponseChoixAff, new CellConstraints("2, 4, 1, 1, left, default")); //$NON-NLS-1$
				jComboBoxSaisieReponseChoixAff.setModel(jComboBoxSaisieReponseChoixAffModel);
			}
			{
				jLabelAffReponse = new JLabel();
				this.add(jLabelAffReponse, new CellConstraints("1, 5, 1, 1, left, default")); //$NON-NLS-1$
				jLabelAffReponse.setText(Messages.getString("WordQuizConfigPanel.AnswerDisplay")); //$NON-NLS-1$
			}
			{
				ComboBoxModel jComboBoxAffReponseModel = new DefaultComboBoxModel(typesAff.keySet().toArray());
				jComboBoxAffReponse = new JComboBox();
				this.add(jComboBoxAffReponse, new CellConstraints("2, 5, 1, 1, left, default")); //$NON-NLS-1$
				jLabelSaisieReponseListe = new JLabel();
				jLabelSaisieReponseListe.setText(Messages.getString("WordQuizConfigPanel.ChoiceListDisplay")); //$NON-NLS-1$
				this.add(jLabelSaisieReponseListe, new CellConstraints("1, 4, 1, 1, left, default")); //$NON-NLS-1$
				jComboBoxAffReponse.setModel(jComboBoxAffReponseModel);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizConfigPanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizConfigPanel#valider()
	 */
	@Override
	public void valider()
	{
		affichageElementQuiz = typesAff.get(jComboBoxAffQuiz.getSelectedItem().toString());
		saisieReponseQuiz = typesSaisie.get(jComboBoxSaisieReponse.getSelectedItem().toString());
		affichageChoixReponsesQuiz = typesAff.get(jComboBoxSaisieReponseChoixAff.getSelectedItem().toString());
		affichageReponseQuiz = typesAff.get(jComboBoxAffReponse.getSelectedItem().toString());
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

	/**
	 * @return
	 */
	public boolean getSaisieReponseCompleteQuiz()
	{
		// TODO Auto-generated method stub
		return true;
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
