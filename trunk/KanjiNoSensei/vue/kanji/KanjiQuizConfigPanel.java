package vue.kanji;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * @author Axan
	 * 
	 */
	public enum ETypeAff
	{
		Kanji, LectureOrigineChinoise, LectureJaponaise, ImageTrace, Signification;
	}

	public enum ETypeSaisie
	{
		LectureOrigineChinoise, LectureJaponaise, Kanji, Signification, ListeChoix, AttenteClick;
	}

	private JLabel						jLabelTitre;
	private JLabel						jLabelAffKanji;
	private JComboBox					jComboBoxAffQuiz;

	private JLabel						jLabelAffReponse;
	private JLabel jLabelSaisieReponseListe;
	private JComboBox					jComboBoxAffReponse;
	private JComboBox					jComboBoxSaisieReponseChoixAff;
	private JComboBox					jComboBoxSaisieReponse;
	private JLabel						jLabelMethodeSaisie;

	private ETypeAff	affichageElementQuiz = ETypeAff.Kanji;
	private ETypeSaisie saisieReponseQuiz = ETypeSaisie.LectureJaponaise;
	private ETypeAff	affichageChoixReponsesQuiz = ETypeAff.LectureJaponaise;
	private ETypeAff	affichageReponseQuiz = ETypeAff.LectureJaponaise;
	
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
			this.addComponentListener(new ComponentAdapter() {
				public void componentShown(ComponentEvent evt) {
					miseAJourForm();
				}
			});
			{
				jLabelTitre = new JLabel();
				this.add(jLabelTitre, new CellConstraints("1, 1, 1, 1, right, default"));
				jLabelTitre.setText("Configuration \u6f22\u5b57");
				jLabelTitre.setFont(new java.awt.Font("SimSun", 1, 16));
			}
			{
				jLabelAffKanji = new JLabel();
				this.add(jLabelAffKanji, new CellConstraints("1, 2, 1, 1, default, default"));
				jLabelAffKanji.setText("Affichage quiz");
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
				this.add(getJLabelSaisieReponseListe(), new CellConstraints("1, 4, 1, 1, left, default"));
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
		jComboBoxAffQuiz.setSelectedItem(affichageElementQuiz);
		jComboBoxSaisieReponse.setSelectedItem(saisieReponseQuiz);
		jComboBoxSaisieReponseChoixAff.setSelectedItem(affichageChoixReponsesQuiz);
		jComboBoxAffReponse.setSelectedItem(affichageReponseQuiz);
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
			jLabelSaisieReponseListe.setText("Saisie de la réponse en liste");
		}
		return jLabelSaisieReponseListe;
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizConfigPanel#valider()
	 */
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

}
