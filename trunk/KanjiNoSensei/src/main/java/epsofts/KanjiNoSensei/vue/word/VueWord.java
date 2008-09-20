/**
 * 
 */
package epsofts.KanjiNoSensei.vue.word;

import epsofts.KanjiNoSensei.metier.Dictionary;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;
import epsofts.KanjiNoSensei.vue.VueElement;
import epsofts.KanjiNoSensei.vue.word.WordQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueWord extends VueElement
{

	public static final float	FONT_MAX_SIZE	= 50;
	public static final float	FONT_MIN_SIZE	= 11;
	private Word					mot					= null;
	private WordEditionDialog	jEditionDialog		= null;
	private QuizQuestionPanel	jQuizQuestionPanel	= null;
	private QuizSolutionPanel	jQuizSolutionPanel	= null;
	private WordVueDetaillePanel	jVueDetaillePanel	= null;

	/**
	 * @param app
	 */
	public VueWord(Word mot, boolean useRomaji)
	{
		super(useRomaji);
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
	public QuizSaisieReponsePanel getQuizSaisieReponsePanel() throws NoSaisieException
	{
		return WordQuizSaisiePanel.getMotQuizSaisiePanel(this);
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
