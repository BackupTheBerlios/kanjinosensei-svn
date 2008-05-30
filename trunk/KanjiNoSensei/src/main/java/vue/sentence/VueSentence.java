/**
 * 
 */
package vue.sentence;

import metier.Dictionary;
import metier.elements.Element;
import metier.elements.Sentence;
import vue.KanjiNoSensei;
import vue.VueElement;
import vue.sentence.SentenceQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueSentence extends VueElement
{

	public static final float	FONT_MAX_SIZE	= 50;
	public static final float	FONT_MIN_SIZE	= 11;
	private Sentence					phrase				= null;
	private SentenceEditionDialog		jEditionDialog		= null;
	private QuizQuestionPanel		jQuizQuestionPanel	= null;
	private QuizSolutionPanel		jQuizSolutionPanel	= null;
	private SentenceVueDetaillePanel	jVueDetaillePanel	= null;

	/**
	 * @param app
	 */
	public VueSentence(KanjiNoSensei app, Sentence phrase, boolean useRomaji)
	{
		super(app, useRomaji);
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
			jEditionDialog = new SentenceEditionDialog(this);
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
			if (SentenceQuizConfigPanel.getSentenceQuizConfigPanel().getAffichageElementQuiz() == ETypeAff.Detaille)
			{
				jQuizQuestionPanel = getVueDetaillePanel();
			}
			else
			{
				jQuizQuestionPanel = new SentenceQuizAffPanel(this, SentenceQuizConfigPanel.getSentenceQuizConfigPanel()
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
	public QuizSaisieReponsePanel getQuizSaisieReponsePanel(Dictionary dico) throws NoSaisieException
	{
		return SentenceQuizSaisiePanel.getPhraseQuizSaisiePanel(this, dico);
	}

	public synchronized QuizSolutionPanel getQuizSolutionPanelCopy()
	{
		QuizSolutionPanel ref = jQuizSolutionPanel;
		QuizSolutionPanel copy = getQuizSolutionPanel(false);
		jQuizSolutionPanel = ref;
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizSolutionPanel(boolean newCopy)
	 */
	@Override
	public QuizSolutionPanel getQuizSolutionPanel(boolean newCopy)
	{
		if (newCopy)
		{
			return getQuizSolutionPanelCopy();
		}
		
		if (jQuizSolutionPanel == null)
		{
			if (SentenceQuizConfigPanel.getSentenceQuizConfigPanel().getAffichageReponseQuiz() == ETypeAff.Detaille)
			{
				jQuizSolutionPanel = getVueDetaillePanel();
			}
			else
			{
				try
				{
					jQuizSolutionPanel = new SentenceQuizAffPanel(this, SentenceQuizConfigPanel.getSentenceQuizConfigPanel()
							.getAffichageReponseQuiz());
				}
				catch (NoAffException e)
				{
					try
					{
						jQuizSolutionPanel = new SentenceQuizAffPanel(this, ETypeAff.Kanji);
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
	public SentenceVueDetaillePanel getVueDetaillePanel()
	{
		if (jVueDetaillePanel == null)
		{
			jVueDetaillePanel = new SentenceVueDetaillePanel(this);
		}

		return jVueDetaillePanel;
	}

	protected Sentence getPhrase()
	{
		return phrase;
	}

	/**
	 * @param phrase
	 */
	protected void setPhrase(Sentence phrase)
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
		return SentenceQuizConfigPanel.getSentenceQuizConfigPanel();
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
