package vue.word;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import vue.VueElement.QuizConfigPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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

	public enum ETypeSaisie
	{
		Kanji, Lecture, Signification, ListeChoix, AttenteClick;
	};
	
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
		jComboBoxAffQuiz.setSelectedItem(affichageElementQuiz);
		jComboBoxSaisieReponse.setSelectedItem(saisieReponseQuiz);
		jComboBoxSaisieReponseChoixAff.setSelectedItem(affichageChoixReponsesQuiz);
		jComboBoxAffReponse.setSelectedItem(affichageReponseQuiz);
	}

	private WordQuizConfigPanel()
	{
		super();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			FormLayout thisLayout = new FormLayout(
					"min(m;73dlu):grow, min(p;200dlu):grow", 
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;15dlu), max(p;5dlu)");
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(400, 119));
			this.setMinimumSize(new java.awt.Dimension(600, 300));
			{
				jLabelTitre = new JLabel();
				this.add(jLabelTitre, new CellConstraints("1, 1, 1, 1, right, default"));
				jLabelTitre.setText("Configuration Mots");
				jLabelTitre.setFont(new java.awt.Font("SimSun", 1, 16));
			}
			{
				jLabelAffMot = new JLabel();
				this.add(jLabelAffMot, new CellConstraints("1, 2, 1, 1, default, default"));
				jLabelAffMot.setText("Affichage quiz");
			}
			{
				ComboBoxModel jComboBoxAffQuizModel = new DefaultComboBoxModel(ETypeAff.values());
				jComboBoxAffQuiz = new JComboBox();
				this.add(jComboBoxAffQuiz, new CellConstraints("2, 2, 1, 1, left, default"));
				jComboBoxAffQuiz.setModel(jComboBoxAffQuizModel);
			}
			{
				jLabelMethodeSaisie = new JLabel();
				this.add(jLabelMethodeSaisie, new CellConstraints("1, 3, 1, 1, left, default"));
				jLabelMethodeSaisie.setText("Saisie de la réponse");
			}
			{
				ComboBoxModel jComboBoxSaisieReponseModel = new DefaultComboBoxModel(ETypeSaisie.values());
				jComboBoxSaisieReponse = new JComboBox();
				this.add(jComboBoxSaisieReponse, new CellConstraints("2, 3, 1, 1, left, default"));
				jComboBoxSaisieReponse.setModel(jComboBoxSaisieReponseModel);
				jComboBoxSaisieReponse.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent evt)
					{
						jComboBoxSaisieReponseChoixAff.setEnabled((jComboBoxSaisieReponse.getSelectedItem().equals(ETypeSaisie.ListeChoix)));
					}
				});
			}
			{
				ComboBoxModel jComboBoxSaisieReponseChoixAffModel = new DefaultComboBoxModel(ETypeAff.values());
				jComboBoxSaisieReponseChoixAff = new JComboBox();
				this.add(jComboBoxSaisieReponseChoixAff, new CellConstraints("2, 4, 1, 1, left, default"));
				jComboBoxSaisieReponseChoixAff.setModel(jComboBoxSaisieReponseChoixAffModel);
			}
			{
				jLabelAffReponse = new JLabel();
				this.add(jLabelAffReponse, new CellConstraints("1, 5, 1, 1, left, default"));
				jLabelAffReponse.setText("Affichage réponse");
			}
			{
				ComboBoxModel jComboBoxAffReponseModel = new DefaultComboBoxModel(ETypeAff.values());
				jComboBoxAffReponse = new JComboBox();
				this.add(jComboBoxAffReponse, new CellConstraints("2, 5, 1, 1, left, default"));
				jLabelSaisieReponseListe = new JLabel();
				jLabelSaisieReponseListe.setText("Saisie de la réponse en liste");
				this.add(jLabelSaisieReponseListe, new CellConstraints("1, 4, 1, 1, left, default"));
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
		affichageElementQuiz = ETypeAff.valueOf(jComboBoxAffQuiz.getSelectedItem().toString());
		saisieReponseQuiz = ETypeSaisie.valueOf(jComboBoxSaisieReponse.getSelectedItem().toString());
		affichageChoixReponsesQuiz = ETypeAff.valueOf(jComboBoxSaisieReponseChoixAff.getSelectedItem().toString());
		affichageReponseQuiz = ETypeAff.valueOf(jComboBoxAffReponse.getSelectedItem().toString());
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
