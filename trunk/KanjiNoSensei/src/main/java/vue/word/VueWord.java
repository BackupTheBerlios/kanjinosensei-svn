/**
 * 
 */
package vue.word;

import metier.Dictionary;
import metier.elements.Element;
import metier.elements.Word;
import vue.KanjiNoSensei;
import vue.VueElement;
import vue.word.WordQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueWord extends VueElement
{

	private Word					mot					= null;
	private WordEditionDialog	jEditionDialog		= null;
	private QuizQuestionPanel	jQuizQuestionPanel	= null;
	private QuizSolutionPanel	jQuizSolutionPanel	= null;
	private WordVueDetaillePanel	jVueDetaillePanel	= null;

	/**
	 * @param app
	 */
	public VueWord(KanjiNoSensei app, Word mot, boolean useRomaji)
	{
		super(app, useRomaji);
		this.mot = mot;
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
			jEditionDialog = new WordEditionDialog(this);
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
			if (WordQuizConfigPanel.getWordQuizConfigPanel().getAffichageElementQuiz() == ETypeAff.Detaille)
			{
				jQuizQuestionPanel = getVueDetaillePanel();
			}
			else
			{
				jQuizQuestionPanel = new WordQuizAffPanel(this, WordQuizConfigPanel.getWordQuizConfigPanel()
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
		return WordQuizSaisiePanel.getMotQuizSaisiePanel(this, dico);
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
			if (WordQuizConfigPanel.getWordQuizConfigPanel().getAffichageReponseQuiz() == ETypeAff.Detaille)
			{
				jQuizSolutionPanel = getVueDetaillePanel();
			}
			else
			{
				try
				{
					jQuizSolutionPanel = new WordQuizAffPanel(this, WordQuizConfigPanel.getWordQuizConfigPanel()
							.getAffichageReponseQuiz());
				}
				catch (NoAffException e)
				{
					try
					{
						jQuizSolutionPanel = new WordQuizAffPanel(this, ETypeAff.Kanji);
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
	public WordVueDetaillePanel getVueDetaillePanel()
	{
		if (jVueDetaillePanel == null)
		{
			jVueDetaillePanel = new WordVueDetaillePanel(this);
		}

		return jVueDetaillePanel;
	}

	protected Word getMot()
	{
		return mot;
	}

	/**
	 * @param mot
	 */
	protected void setMot(Word mot)
	{
		this.mot = mot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizConfigPanel()
	 */
	@Override
	public QuizConfigPanel getQuizConfigPanel()
	{
		return WordQuizConfigPanel.getWordQuizConfigPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getElement()
	 */
	@Override
	public Element getElement()
	{
		return mot;
	}

}
