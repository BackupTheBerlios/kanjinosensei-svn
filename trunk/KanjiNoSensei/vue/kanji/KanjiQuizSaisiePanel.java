package vue.kanji;

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
import javax.swing.JPanel;
import javax.swing.JTextField;

import metier.Dictionnaire;
import vue.VueElement.NoSaisieException;
import vue.VueElement.QuizSaisieReponsePanel;
import vue.kanji.KanjiQuizConfigPanel.ETypeSaisie;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
class KanjiQuizSaisiePanel extends javax.swing.JPanel implements QuizSaisieReponsePanel
{

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;
	private static KanjiQuizSaisiePanel	singleton			= null;
	private VueKanji					solution			= null;
	private Boolean						resultat			= null;
	private ETypeSaisie					typeSaisie			= null;

	private KanjiQuizSaisiePanel()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.QuizSaisieReponsePanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

	/**
	 * @param kanji
	 * @param dictionnaireQuizEnCours
	 * @return
	 * @throws NoSaisieException 
	 */
	public static QuizSaisieReponsePanel getKanjiQuizSaisiePanel(VueKanji vue, Dictionnaire dictionnaire) throws NoSaisieException
	{
		if (singleton == null)
		{
			singleton = new KanjiQuizSaisiePanel();
		}

		singleton.solution = vue;
		return singleton.computePanel();

	}

	// <NoJigloo>
	/**
	 * @throws NoSaisieException 
	 * 
	 */
	private QuizSaisieReponsePanel computePanel() throws NoSaisieException
	{
		setVisible(false);
		removeAll();

		typeSaisie = KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getSaisieReponseQuiz();

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
				addTextField(solution.getKanji().getCodeUTF8().toString());
				break;
			}
			
			case LectureOrigineChinoise:
			{
				addTextField(solution.getKanji().getLecturesChinoisesSet());
				break;
			}
			
			case LectureJaponaise:
			{
				addTextField(solution.getKanji().getLecturesJaponaisesSet());
				break;
			}
			
			case Signification:
			{
				addTextField(solution.getKanji().getSignificationsSet());
				break;
			}
			
			//TODO: case ListeChoix:
			default:
			{
				throw new NoSaisieException(typeSaisie.toString());
			}
		}
		setVisible(true);

		return this;
	}
	// </NoJigloo>

	/* (non-Javadoc)
	 * @see vue.VueElement.QuizSaisieReponsePanel#getResultat()
	 */
	@Override
	public boolean getResultat()
	{
		return resultat;
	}
	
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
