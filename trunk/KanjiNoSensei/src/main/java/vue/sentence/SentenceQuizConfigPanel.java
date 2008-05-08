package vue.sentence;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import metier.Messages;
import metier.elements.Sentence;
import metier.elements.Word;

import vue.Config;
import vue.VueElement.QuizConfigPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

class SentenceQuizConfigPanel extends javax.swing.JPanel implements QuizConfigPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public enum ETypeAff
	{
		Kanji, Lecture, Son, Signification, Detaille;
	}
	static private HashMap<String, ETypeAff> typesAff = new HashMap<String, ETypeAff>();
	{
		for(ETypeAff type : ETypeAff.values())
		{
			typesAff.put(metier.Messages.getString(Sentence.class.getSimpleName()+".OM."+type.toString()), type);
		}
	}

	public enum ETypeSaisie
	{
		Kanji, Lecture, Signification, ListeChoix, AttenteClick;
	}
	static private HashMap<String, ETypeSaisie> typesSaisie = new HashMap<String, ETypeSaisie>();
	{
		for(ETypeSaisie type : ETypeSaisie.values())
		{
			typesSaisie.put(metier.Messages.getString(Sentence.class.getSimpleName()+".IM."+type.toString()), type);
		}
	}
	
	private static SentenceQuizConfigPanel	PhraseQuizConfigPanelSingleton	= null;

	public static SentenceQuizConfigPanel getSentenceQuizConfigPanel()
	{
		if (PhraseQuizConfigPanelSingleton == null)
		{
			PhraseQuizConfigPanelSingleton = new SentenceQuizConfigPanel();
		}

		PhraseQuizConfigPanelSingleton.miseAJourForm();
		return PhraseQuizConfigPanelSingleton;
	}

	private JLabel	jLabelTitre;
	private JLabel	jLabelAffPhrase;
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
		jComboBoxAffQuiz.setSelectedItem(metier.Messages.getString(Sentence.class.getSimpleName()+".OM."+affichageElementQuiz.toString()));
		jComboBoxSaisieReponse.setSelectedItem(metier.Messages.getString(Sentence.class.getSimpleName()+".IM."+saisieReponseQuiz.toString()));
		jComboBoxSaisieReponseChoixAff.setSelectedItem(metier.Messages.getString(Sentence.class.getSimpleName()+".OM."+affichageChoixReponsesQuiz.toString()));
		jComboBoxAffReponse.setSelectedItem(metier.Messages.getString(Sentence.class.getSimpleName()+".OM."+affichageReponseQuiz.toString()));
	}

	private SentenceQuizConfigPanel()
	{
		super();
		initGUI();
		loadConfig();
	}
	
	private void loadConfig()
	{
		affichageElementQuiz = ETypeAff.valueOf(Config.getString("SentenceQuizConfig.affichageElementQuiz", ETypeAff.Signification.toString()));
		saisieReponseQuiz = ETypeSaisie.valueOf(Config.getString("SentenceQuizConfig.saisieReponseQuiz", ETypeSaisie.AttenteClick.toString()));
		affichageChoixReponsesQuiz = ETypeAff.valueOf(Config.getString("SentenceQuizConfig.affichageChoixReponsesQuiz", ETypeAff.Kanji.toString()));
		affichageReponseQuiz = ETypeAff.valueOf(Config.getString("SentenceQuizConfig.affichageReponseQuiz", ETypeAff.Detaille.toString()));
	}
	
	private void saveConfig()
	{
		Config.setString("SentenceQuizConfig.affichageElementQuiz", affichageElementQuiz.toString());
		Config.setString("SentenceQuizConfig.saisieReponseQuiz", saisieReponseQuiz.toString());
		Config.setString("SentenceQuizConfig.affichageChoixReponsesQuiz", affichageChoixReponsesQuiz.toString());
		Config.setString("SentenceQuizConfig.affichageReponseQuiz", affichageReponseQuiz.toString());
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
				jLabelTitre.setText(Messages.getString("SentenceQuizConfigPanel.LabelTitle")); //$NON-NLS-1$
				jLabelTitre.setFont(new java.awt.Font("SimSun", 1, 16)); //$NON-NLS-1$
			}
			{
				jLabelAffPhrase = new JLabel();
				this.add(jLabelAffPhrase, new CellConstraints("1, 2, 1, 1, default, default")); //$NON-NLS-1$
				jLabelAffPhrase.setText(Messages.getString("SentenceQuizConfigPanel.QuizDisplay")); //$NON-NLS-1$
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
				jLabelMethodeSaisie.setText(Messages.getString("SentenceQuizConfigPanel.AnswerInputMethod")); //$NON-NLS-1$
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
				jLabelAffReponse.setText(Messages.getString("SentenceQuizConfigPanel.AnswerDisplay")); //$NON-NLS-1$
			}
			{
				ComboBoxModel jComboBoxAffReponseModel = new DefaultComboBoxModel(typesAff.keySet().toArray());
				jComboBoxAffReponse = new JComboBox();
				this.add(jComboBoxAffReponse, new CellConstraints("2, 5, 1, 1, left, default")); //$NON-NLS-1$
				jLabelSaisieReponseListe = new JLabel();
				jLabelSaisieReponseListe.setText(Messages.getString("SentenceQuizConfigPanel.ListChoiceDisplay")); //$NON-NLS-1$
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
	public Boolean getSaisieReponseCompleteQuiz()
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
