package vue.kanji;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import metier.Dictionary;
import vue.VueElement;
import vue.VueElement.NoSaisieException;
import vue.VueElement.QuizSaisieReponsePanel;
import vue.kanji.KanjiQuizConfigPanel.ETypeSaisie;

class KanjiQuizSaisiePanel extends javax.swing.JPanel implements QuizSaisieReponsePanel
{

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;
	private static KanjiQuizSaisiePanel	singleton			= null;
	private VueKanji					solution			= null;
	private Boolean						resultat			= null;
	private ETypeSaisie					typeSaisie			= null;
	private Boolean						saisieReponseComplete = null;

	private KanjiQuizSaisiePanel()
	{
		super();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setBackground(new java.awt.Color(255, 255, 255));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.QuizSaisieReponsePanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

	/**
	 * @param kanji
	 * @param dictionnaireQuizEnCours
	 * @return
	 * @throws NoSaisieException 
	 */
	public static QuizSaisieReponsePanel getKanjiQuizSaisiePanel(VueKanji vue, Dictionary dictionnaire) throws NoSaisieException
	{
		if (singleton == null)
		{
			singleton = new KanjiQuizSaisiePanel();
		}

		singleton.solution = vue;
		return singleton.computePanel();

	}

	// <NoJigloo>
	/**
	 * @throws NoSaisieException 
	 * 
	 */
	private QuizSaisieReponsePanel computePanel() throws NoSaisieException
	{
		setVisible(false);
		removeAll();

		typeSaisie = KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getSaisieReponseQuiz();
		saisieReponseComplete = KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getSaisieReponseCompleteQuiz();

		try
		{
			switch (typeSaisie)
			{
				case AttenteClick:
				{
					VueElement.addInputMethodClickWait(null, solution, singleton);
					break;
				}
				
				case Kanji:
				{
					VueElement.addInputMethodTextField("kanji", solution, singleton, solution.getKanji().getCodeUTF8().toString());
					break;
				}
				
				case LectureOrigineChinoise:
				{
					VueElement.addInputMethodTextField("lectures ON", solution, this, solution.getKanji().getLecturesONSet(), saisieReponseComplete);
					break;
				}
				
				case LectureJaponaise:
				{
					VueElement.addInputMethodTextField("lectures KUN", solution, this, solution.getKanji().getLecturesKUNSet(), saisieReponseComplete);
					break;
				}
				
				case Signification:
				{
					VueElement.addInputMethodTextField("significations", solution, this, solution.getKanji().getSignificationsSet(), saisieReponseComplete);
					break;
				}
				
				//TODO: case ListeChoix:
				default:
				{
					throw new NoSaisieException(typeSaisie.toString());
				}
			}
		}
		// We catch NoSaisieException to specify the typeSaisie it fails to generate.
		catch(NoSaisieException e)
		{
			throw new NoSaisieException(typeSaisie.toString());
		}
		
		setVisible(true);
		return this;
	}
	// </NoJigloo>

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizSaisieReponsePanel#getResultat()
	 */
	@Override
	public boolean getResultat()
	{
		return resultat;
	}

}
