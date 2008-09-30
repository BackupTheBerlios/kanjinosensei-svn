/**
 * 
 */
package epsofts.KanjiNoSensei.vue.word;

import java.awt.Font;

import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.vue.Config;
import epsofts.KanjiNoSensei.vue.VueElement;
import epsofts.KanjiNoSensei.vue.Config.ConfigListener;
import epsofts.KanjiNoSensei.vue.kanji.VueKanji;
import epsofts.KanjiNoSensei.vue.word.WordQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueWord extends VueElement
{

	protected static float		FONT_MAX_SIZE		= 32;

	protected static float		FONT_MIN_SIZE		= 11;
	
	protected static String DETAILLE_DEFAULT_TEMPLATE = "<b><center><method>getWord</method></center></b><label>WordVueDetaillePanel.LabelLecture</label> : <b><vue>getLecture</vue></b><br><label>WordVueDetaillePanel.LabelSignifications</label> : <b><method>getSignifications</method></b><br><label>WordVueDetaillePanel.LabelThemes</label> : <b><method>getThemes</method></b><br>";

	private Word					word				= null;

	private WordEditionDialog		jEditionDialog		= null;

	private QuizQuestionPanel		jQuizQuestionPanel	= null;

	private QuizSolutionPanel		jQuizSolutionPanel	= null;

	private WordVueDetaillePanel	jVueDetaillePanel	= null;

	private static ConfigListener	configListener		= new Config.ConfigListener()
														{

															@Override
															public void onConfigLoaded()
															{													
																FONT_MAX_SIZE = Config.getTypedValue("VueWord.FontMaxSize", FONT_MAX_SIZE);
																FONT_MIN_SIZE = Config.getTypedValue("VueWord.FontMinSize", FONT_MIN_SIZE);
																DETAILLE_DEFAULT_TEMPLATE = Config.getString("VueWord.DetailleTemplate", DETAILLE_DEFAULT_TEMPLATE);									
															}
														};

	static
	{
		Config.addListener(configListener);
	}

	/**
	 * @param app
	 */
	public VueWord(Word mot)
	{
		super();
		this.word = mot;
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
				jQuizQuestionPanel = new WordQuizAffPanel(this, WordQuizConfigPanel.getWordQuizConfigPanel().getAffichageElementQuiz());
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
					jQuizSolutionPanel = new WordQuizAffPanel(this, WordQuizConfigPanel.getWordQuizConfigPanel().getAffichageReponseQuiz());
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
		return word;
	}

	/**
	 * @param mot
	 */
	protected void setMot(Word mot)
	{
		this.word = mot;
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
		return word;
	}

	public String getLecture()
	{
		return toRomajiIfNeeded(word.getLecture());
	}
}
