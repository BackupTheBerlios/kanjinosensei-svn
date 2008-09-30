/**
 * 
 */
package epsofts.KanjiNoSensei.vue.sentence;

import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Sentence;
import epsofts.KanjiNoSensei.vue.Config;
import epsofts.KanjiNoSensei.vue.VueElement;
import epsofts.KanjiNoSensei.vue.Config.ConfigListener;
import epsofts.KanjiNoSensei.vue.sentence.SentenceQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueSentence extends VueElement
{

	protected static float	FONT_MAX_SIZE	= 32;
	protected static float	FONT_MIN_SIZE	= 11;
	protected static String DETAILLE_DEFAULT_TEMPLATE = "<b><center><method>getSentence</method></center></b><label>SentenceVueDetaillePanel.LabelLecture</label> : <b><vue>getLecture</vue></b><br><label>SentenceVueDetaillePanel.LabelSignifications</label> : <b><method>getSignifications</method></b><br><label>SentenceVueDetaillePanel.LabelThemes</label> : <b><method>getThemes</method></b><br>";
	
	private Sentence					sentence				= null;
	private SentenceEditionDialog		jEditionDialog		= null;
	private QuizQuestionPanel		jQuizQuestionPanel	= null;
	private QuizSolutionPanel		jQuizSolutionPanel	= null;
	private SentenceVueDetaillePanel	jVueDetaillePanel	= null;

	private static ConfigListener configListener = new Config.ConfigListener()
	{
	
		@Override
		public void onConfigLoaded()
		{
			FONT_MAX_SIZE	= Config.getTypedValue("VueSentence.FontMaxSize", FONT_MAX_SIZE);
			FONT_MIN_SIZE	= Config.getTypedValue("VueSentence.FontMinSize", FONT_MIN_SIZE);
			DETAILLE_DEFAULT_TEMPLATE = Config.getString("VueSentence.DetailleTemplate", DETAILLE_DEFAULT_TEMPLATE);
		}
	};
	
	static
	{
		Config.addListener(configListener);
	}
	
	/**
	 * @param app
	 */
	public VueSentence(Sentence phrase)
	{
		super();
		this.sentence = phrase;
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
	public QuizSaisieReponsePanel getQuizSaisieReponsePanel() throws NoSaisieException
	{
		return SentenceQuizSaisiePanel.getPhraseQuizSaisiePanel(this);
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

	protected Sentence getSentence()
	{
		return sentence;
	}
	
	public String getLecture()
	{
		return toRomajiIfNeeded(sentence.getLecture());
	}

	/**
	 * @param phrase
	 */
	protected void setPhrase(Sentence phrase)
	{
		this.sentence = phrase;
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
		return sentence;
	}

}
