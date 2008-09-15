package epsofts.KanjiNoSensei.vue.word;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import epsofts.KanjiNoSensei.metier.Dictionary;
import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.vue.VueElement;
import epsofts.KanjiNoSensei.vue.VueElement.NoSaisieException;
import epsofts.KanjiNoSensei.vue.VueElement.QuizSaisieReponsePanel;
import epsofts.KanjiNoSensei.vue.word.WordQuizConfigPanel.ETypeSaisie;


class WordQuizSaisiePanel extends javax.swing.JPanel implements QuizSaisieReponsePanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static WordQuizSaisiePanel	singleton			= null;
	private VueWord						solution			= null;
	private Boolean						resultat			= null;
	private ETypeSaisie					typeSaisie			= null;
	private Boolean						saisieReponseComplete = null;
	
	/**
	 * @param vue
	 * @param dictionnaireQuizEnCours
	 * @return
	 * @throws NoSaisieException 
	 */
	public static QuizSaisieReponsePanel getMotQuizSaisiePanel(VueWord vue, Dictionary dictionnaire) throws NoSaisieException
	{
		if (singleton == null)
		{
			singleton = new WordQuizSaisiePanel();
		}

		singleton.solution = vue;
		return singleton.computePanel();

	}

	//<NoJigloo>
	/**
	 * @return
	 * @throws NoSaisieException 
	 */
	private QuizSaisieReponsePanel computePanel() throws NoSaisieException
	{
		setVisible(false);
		removeAll();

		typeSaisie = WordQuizConfigPanel.getWordQuizConfigPanel().getSaisieReponseQuiz();
		saisieReponseComplete = WordQuizConfigPanel.getWordQuizConfigPanel().getSaisieReponseCompleteQuiz();

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
				VueElement.addInputMethodTextField(Messages.getString("WordQuizSaisiePanel.IM.Kanji"), solution, this, solution.getMot().getWord()); //$NON-NLS-1$
				break;
			}
			
			case Lecture:
			{
				VueElement.addInputMethodTextField(Messages.getString("WordQuizSaisiePanel.IM.Lecture"), solution, this, solution.getMot().getLecture()); //$NON-NLS-1$
				break;
			}
			
			case Signification:
			{
				VueElement.addInputMethodTextField(Messages.getString("WordQuizSaisiePanel.IM.Signification"), solution, this, solution.getMot().getSignificationsSet(), saisieReponseComplete); //$NON-NLS-1$
				break;
			}
			
			//TODO case ListeChoix:
			default:
			{
				throw new NoSaisieException(typeSaisie.toString());
			}
		}
		}
		catch(NoSaisieException e)
		{
			throw new NoSaisieException(typeSaisie.toString());
		}
		
		setVisible(true);
		return this;
	}
	//</NoJigloo>

	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.getContentPane().add(new WordQuizSaisiePanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public WordQuizSaisiePanel()
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

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizSaisieReponsePanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizSaisieReponsePanel#getResultat()
	 */
	@Override
	public boolean getResultat()
	{
		return resultat;
	}
	
}
