package vue.sentence;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import metier.Dictionary;
import vue.VueElement;
import vue.VueElement.NoSaisieException;
import vue.VueElement.QuizSaisieReponsePanel;
import vue.sentence.SentenceQuizConfigPanel.ETypeSaisie;

class SentenceQuizSaisiePanel extends javax.swing.JPanel implements QuizSaisieReponsePanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static SentenceQuizSaisiePanel	singleton			= null;
	private VueSentence						solution			= null;
	private Boolean						resultat			= null;
	private ETypeSaisie					typeSaisie			= null;
	private Boolean						saisieReponseComplete = null;
	
	/**
	 * @param vue
	 * @param dictionnaireQuizEnCours
	 * @return
	 * @throws NoSaisieException 
	 */
	public static QuizSaisieReponsePanel getPhraseQuizSaisiePanel(VueSentence vue, Dictionary dictionnaire) throws NoSaisieException
	{
		if (singleton == null)
		{
			singleton = new SentenceQuizSaisiePanel();
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

		typeSaisie = SentenceQuizConfigPanel.getSentenceQuizConfigPanel().getSaisieReponseQuiz();
		saisieReponseComplete = SentenceQuizConfigPanel.getSentenceQuizConfigPanel().getSaisieReponseCompleteQuiz();

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
				VueElement.addInputMethodTextField("kanji", solution, this, solution.getPhrase().getSentence());
				break;
			}
			
			case Lecture:
			{
				VueElement.addInputMethodTextField("lecture", solution, this, solution.getPhrase().getLecture());
				break;
			}
			
			case Signification:
			{
				VueElement.addInputMethodTextField("signification", solution, this, solution.getPhrase().getSignificationsSet(), saisieReponseComplete);
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
		frame.getContentPane().add(new SentenceQuizSaisiePanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public SentenceQuizSaisiePanel()
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
