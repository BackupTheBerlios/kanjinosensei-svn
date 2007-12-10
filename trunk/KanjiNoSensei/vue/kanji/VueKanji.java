/**
 * 
 */
package vue.kanji;

import metier.Dictionnaire;
import metier.elements.Element;
import metier.elements.Kanji;
import vue.KanjiNoSensei;
import vue.VueElement;
import vue.kanji.KanjiQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueKanji extends VueElement
{
	private Kanji				kanji				= null;

	private KanjiVueDetaillePanel	jVueDetaillePanel	= null;

	private KanjiEditionDialog	jEditionDialog		= null;
	
	private KanjiQuizAffPanel	jQuizAffPanel	= null;

	private KanjiQuizAffPanel	jQuizSolutionPanel = null;

	public VueKanji(KanjiNoSensei app, Kanji kanji)
	{
		super(app);
		this.kanji = kanji;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getEditionDialog()
	 */
	public EditionDialog getEditionDialog()
	{
		if (jEditionDialog == null)
		{
			jEditionDialog = new KanjiEditionDialog(this);
		}

		return jEditionDialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizQuestionPanel(java.lang.String)
	 */
	public QuizQuestionPanel getQuizQuestionPanel() throws NoAffException
	{
		if (jQuizAffPanel == null)
		{
			jQuizAffPanel = new KanjiQuizAffPanel(this, KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getAffichageElementQuiz());
		}
		return jQuizAffPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizSaisieReponsePanel(java.lang.String)
	 */
	public QuizSaisieReponsePanel getQuizSaisieReponsePanel(Dictionnaire dico) throws NoSaisieException
	{
		return KanjiQuizSaisiePanel.getKanjiQuizSaisiePanel(this, dico);
	}

	public synchronized QuizSolutionPanel getQuizSolutionPanelCopy()
	{
		KanjiQuizAffPanel ref = jQuizSolutionPanel;
		QuizSolutionPanel copy = getQuizSolutionPanel();
		jQuizSolutionPanel = ref;
		return copy;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizSolutionPanel(java.lang.String)
	 */
	public QuizSolutionPanel getQuizSolutionPanel()
	{
		if (jQuizSolutionPanel == null)
		{
			try
			{
				jQuizSolutionPanel  = new KanjiQuizAffPanel(this, KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getAffichageReponseQuiz());
			}
			catch (NoAffException e)
			{
				try
				{
					jQuizSolutionPanel = new KanjiQuizAffPanel(this, ETypeAff.Kanji);
				}
				catch (NoAffException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return jQuizSolutionPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getVueDetaille()
	 */
	public VueDetaillePanel getVueDetaillePanel()
	{
		if (jVueDetaillePanel == null)
		{
			jVueDetaillePanel = new KanjiVueDetaillePanel(this);
		}

		return jVueDetaillePanel;
	}

	/**
	 * @return
	 */
	protected Kanji getKanji()
	{
		return kanji;
	}

	/**
	 * @param kanji
	 */
	protected void setKanji(Kanji kanji)
	{
		this.kanji = kanji;
	}

	/* (non-Javadoc)
	 * @see vue.VueElement#getQuizConfigPanel()
	 */
	@Override
	public QuizConfigPanel getQuizConfigPanel()
	{
		return KanjiQuizConfigPanel.getKanjiQuizConfigPanel();
	}

	/* (non-Javadoc)
	 * @see vue.VueElement#getElement()
	 */
	@Override
	public Element getElement()
	{
		return kanji;
	}
}
