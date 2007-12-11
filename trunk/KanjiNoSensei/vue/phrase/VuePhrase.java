/**
 * 
 */
package vue.phrase;

import metier.Dictionnaire;
import metier.elements.Element;
import metier.elements.Phrase;
import vue.KanjiNoSensei;
import vue.VueElement;
import vue.phrase.PhraseQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VuePhrase extends VueElement
{

	private Phrase					phrase				= null;
	private PhraseEditionDialog		jEditionDialog		= null;
	private QuizQuestionPanel		jQuizQuestionPanel	= null;
	private QuizSolutionPanel		jQuizSolutionPanel	= null;
	private PhraseVueDetaillePanel	jVueDetaillePanel	= null;

	/**
	 * @param app
	 */
	public VuePhrase(KanjiNoSensei app, Phrase phrase)
	{
		super(app);
		this.phrase = phrase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getEditionDialog()
	 */
	@Override
	public EditionDialog getEditionDialog()
	{
		if (jEditionDialog == null)
		{
			jEditionDialog = new PhraseEditionDialog(this);
		}

		return jEditionDialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizQuestionPanel()
	 */
	@Override
	public QuizQuestionPanel getQuizQuestionPanel() throws NoAffException
	{

		if (jQuizQuestionPanel == null)
		{
			if (PhraseQuizConfigPanel.getPhraseQuizConfigPanel().getAffichageElementQuiz() == ETypeAff.Detaille)
			{
				jQuizQuestionPanel = getVueDetaillePanel();
			}
			else
			{
				jQuizQuestionPanel = new PhraseQuizAffPanel(this, PhraseQuizConfigPanel.getPhraseQuizConfigPanel()
						.getAffichageElementQuiz());
			}
		}

		return jQuizQuestionPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizSaisieReponsePanel(metier.Dictionnaire)
	 */
	@Override
	public QuizSaisieReponsePanel getQuizSaisieReponsePanel(Dictionnaire dico) throws NoSaisieException
	{
		return PhraseQuizSaisiePanel.getPhraseQuizSaisiePanel(this, dico);
	}

	public synchronized QuizSolutionPanel getQuizSolutionPanelCopy()
	{
		QuizSolutionPanel ref = jQuizSolutionPanel;
		QuizSolutionPanel copy = getQuizSolutionPanel();
		jQuizSolutionPanel = ref;
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizSolutionPanel()
	 */
	@Override
	public QuizSolutionPanel getQuizSolutionPanel()
	{
		if (jQuizSolutionPanel == null)
		{
			if (PhraseQuizConfigPanel.getPhraseQuizConfigPanel().getAffichageReponseQuiz() == ETypeAff.Detaille)
			{
				jQuizSolutionPanel = getVueDetaillePanel();
			}
			else
			{
				try
				{
					jQuizSolutionPanel = new PhraseQuizAffPanel(this, PhraseQuizConfigPanel.getPhraseQuizConfigPanel()
							.getAffichageReponseQuiz());
				}
				catch (NoAffException e)
				{
					try
					{
						jQuizSolutionPanel = new PhraseQuizAffPanel(this, ETypeAff.Kanji);
					}
					catch (NoAffException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}

		return jQuizSolutionPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getVueDetaillePanel()
	 */
	@Override
	public PhraseVueDetaillePanel getVueDetaillePanel()
	{
		if (jVueDetaillePanel == null)
		{
			jVueDetaillePanel = new PhraseVueDetaillePanel(this);
		}

		return jVueDetaillePanel;
	}

	protected Phrase getPhrase()
	{
		return phrase;
	}

	/**
	 * @param phrase
	 */
	protected void setPhrase(Phrase phrase)
	{
		this.phrase = phrase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizConfigPanel()
	 */
	@Override
	public QuizConfigPanel getQuizConfigPanel()
	{
		return PhraseQuizConfigPanel.getPhraseQuizConfigPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getElement()
	 */
	@Override
	public Element getElement()
	{
		return phrase;
	}

}
