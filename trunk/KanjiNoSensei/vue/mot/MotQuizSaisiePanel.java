package vue.mot;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import metier.Dictionnaire;
import vue.VueElement.NoSaisieException;
import vue.VueElement.QuizSaisieReponsePanel;
import vue.mot.MotQuizConfigPanel.ETypeSaisie;

class MotQuizSaisiePanel extends javax.swing.JPanel implements QuizSaisieReponsePanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static MotQuizSaisiePanel	singleton			= null;
	private VueMot						solution			= null;
	private Boolean						resultat			= null;
	private ETypeSaisie					typeSaisie			= null;
	
	/**
	 * @param vue
	 * @param dictionnaireQuizEnCours
	 * @return
	 * @throws NoSaisieException 
	 */
	public static QuizSaisieReponsePanel getMotQuizSaisiePanel(VueMot vue, Dictionnaire dictionnaire) throws NoSaisieException
	{
		if (singleton == null)
		{
			singleton = new MotQuizSaisiePanel();
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

		typeSaisie = MotQuizConfigPanel.getMotQuizConfigPanel().getSaisieReponseQuiz();

		switch (typeSaisie)
		{
			case AttenteClick:
			{
				singleton.addMouseListener(new MouseAdapter()
				{

					@Override
					public void mouseClicked(MouseEvent e)
					{
						if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
						{
							e.consume();
							
							singleton.removeMouseListener(this);

							JButton jButtonBON = new JButton("Bon");
							jButtonBON.addActionListener(new ActionListener()
							{

								@Override
								public void actionPerformed(ActionEvent e)
								{
									solution.getApp().validerReponseQuiz(true);
								}

							});

							JButton jButtonMAUVAIS = new JButton("Mauvais");
							jButtonMAUVAIS.addActionListener(new ActionListener()
							{

								@Override
								public void actionPerformed(ActionEvent e)
								{
									solution.getApp().validerReponseQuiz(false);
								}

							});
							
							JPanel jPanelReponse = solution.getQuizSolutionPanelCopy().getPanel();
							JPanel jPanelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER));
							jPanelBtns.add(jButtonBON);
							jPanelBtns.add(jButtonMAUVAIS);
							
							singleton.add(jPanelReponse, BorderLayout.CENTER);
							singleton.add(jPanelBtns, BorderLayout.SOUTH);

							singleton.setVisible(false);
							singleton.setVisible(true);
						}
						
						super.mouseClicked(e);
					}

				});

				break;
			}
		
			case Kanji:
			{
				addTextField(solution.getMot().getMot());
				break;
			}
			
			case Lecture:
			{
				addTextField(solution.getMot().getLecture());
				break;
			}
			
			case Signification:
			{
				addTextField(solution.getMot().getSignificationsSet());
				break;
			}
			
			//TODO case ListeChoix:
			default:
			{
				throw new NoSaisieException(typeSaisie.toString());
			}
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
		frame.getContentPane().add(new MotQuizSaisiePanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public MotQuizSaisiePanel()
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
	
	// TODO factoriser avec KanjiQuizSaisiePanel#addTextField
	private void addTextField(String reponseUnique) throws NoSaisieException
	{
		if (reponseUnique.isEmpty()) throw new NoSaisieException(typeSaisie.toString());
		
		final TreeSet<String> reponse = new TreeSet<String>();
		reponse.add(reponseUnique);
		addTextField(reponse);
	}
	private void addTextField(final Set<String> reponsesMultiples) throws NoSaisieException
	{
		if (reponsesMultiples.isEmpty()) throw new NoSaisieException(typeSaisie.toString());
		
		final JTextField jSaisie = new JTextField();
		jSaisie.addActionListener(new ActionListener()
		{
		
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				Iterator<String> it = reponsesMultiples.iterator();
				while(it.hasNext())
				{
					if (it.next().compareToIgnoreCase(jSaisie.getText()) == 0)
					{
						solution.getApp().validerReponseQuiz(true);
						return;
					}
				}
				
				solution.getApp().validerReponseQuiz(false);
			}
		
		});
		
		add(jSaisie, BorderLayout.CENTER);
		setVisible(false);
		setVisible(true);
	}

}
