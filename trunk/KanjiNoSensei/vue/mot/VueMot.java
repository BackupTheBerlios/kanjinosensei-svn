/**
 * 
 */
package vue.mot;

import metier.Dictionnaire;
import metier.elements.Element;
import metier.elements.Mot;
import vue.KanjiNoSensei;
import vue.VueElement;
import vue.mot.MotQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueMot extends VueElement
{

	private Mot					mot					= null;
	private MotEditionDialog	jEditionDialog		= null;
	private QuizQuestionPanel	jQuizQuestionPanel	= null;
	private QuizSolutionPanel	jQuizSolutionPanel	= null;
	private MotVueDetaillePanel	jVueDetaillePanel	= null;

	/**
	 * @param app
	 */
	public VueMot(KanjiNoSensei app, Mot mot)
	{
		super(app);
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
			jEditionDialog = new MotEditionDialog(this);
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
			if (MotQuizConfigPanel.getMotQuizConfigPanel().getAffichageElementQuiz() == ETypeAff.Detaille)
			{
				jQuizQuestionPanel = getVueDetaillePanel();
			}
			else
			{
				jQuizQuestionPanel = new MotQuizAffPanel(this, MotQuizConfigPanel.getMotQuizConfigPanel()
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
		return MotQuizSaisiePanel.getMotQuizSaisiePanel(this, dico);
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
			if (MotQuizConfigPanel.getMotQuizConfigPanel().getAffichageReponseQuiz() == ETypeAff.Detaille)
			{
				jQuizSolutionPanel = getVueDetaillePanel();
			}
			else
			{
				try
				{
					jQuizSolutionPanel = new MotQuizAffPanel(this, MotQuizConfigPanel.getMotQuizConfigPanel()
							.getAffichageReponseQuiz());
				}
				catch (NoAffException e)
				{
					try
					{
						jQuizSolutionPanel = new MotQuizAffPanel(this, ETypeAff.Kanji);
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
	public MotVueDetaillePanel getVueDetaillePanel()
	{
		if (jVueDetaillePanel == null)
		{
			jVueDetaillePanel = new MotVueDetaillePanel(this);
		}

		return jVueDetaillePanel;
	}

	protected Mot getMot()
	{
		return mot;
	}

	/**
	 * @param mot
	 */
	protected void setMot(Mot mot)
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
		return MotQuizConfigPanel.getMotQuizConfigPanel();
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
