package epsofts.KanjiNoSensei.vue.word;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.utils.MyAutoResizingText;
import epsofts.KanjiNoSensei.vue.JPanelSonBtn;
import epsofts.KanjiNoSensei.vue.VueElement.NoAffException;
import epsofts.KanjiNoSensei.vue.VueElement.QuizQuestionPanel;
import epsofts.KanjiNoSensei.vue.VueElement.QuizSolutionPanel;
import epsofts.KanjiNoSensei.vue.word.WordQuizConfigPanel.ETypeAff;


class WordQuizAffPanel extends javax.swing.JPanel implements QuizQuestionPanel, QuizSolutionPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private JLabel				jLabel;
	VueWord						vue					= null;
	ETypeAff					typeAff				= null;

	public WordQuizAffPanel(VueWord vue, ETypeAff typeAff) throws NoAffException
	{
		super();
		this.vue = vue;
		this.typeAff = typeAff;
		initGUI();
	}

	private void initGUI() throws NoAffException
	{
		BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		this.setBackground(new java.awt.Color(255,255,255));
		{
			// <NoJigloo>
			String texte = "", son = ""; //$NON-NLS-1$ //$NON-NLS-2$
			int taille_fonte = -1;

			switch (typeAff)
			{
				case Kanji:
				{
					texte = vue.getMot().getWord();
					taille_fonte = 40;
					break;
				}

				case Lecture:
				{
					texte = vue.toRomajiIfNeeded(vue.getMot().getLecture());
					taille_fonte = 16;
					break;
				}


				case Son:
				{
					son = vue.getMot().getSoundFile();
					break;
				}

				case Signification:
				{
					texte = vue.getMot().getSignifications();
					taille_fonte = 16;
					break;
				}
			}

			if (texte.isEmpty() && son.isEmpty())
			{
				throw new NoAffException(typeAff.toString());
			}

			if ( !texte.isEmpty())
			{
				addJLabel(texte, taille_fonte);
				//setPanelSize(panel_w, panel_h);
			}

			if ( !son.isEmpty())
			{
				addSon(son);
			}
			// </NoJigloo>
		}
	}

	// <JiglooProtected>

	private void addJLabel(String text, int taille)
	{
		MyAutoResizingText<JLabel> jAutoSizeLabel = MyAutoResizingText.createSafely(JLabel.class, taille, VueWord.FONT_MAX_SIZE);
		jLabel = jAutoSizeLabel.getJComponent();
		jLabel.setText(text);
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		jLabel.setFont(new java.awt.Font("SimSun", 0, taille)); //$NON-NLS-1$		
		
		this.add(jAutoSizeLabel, BorderLayout.NORTH);
	}

	private void addSon(String source)
	{
		JPanelSonBtn jPanelSonBtn = new JPanelSonBtn(source, true);
		jPanelSonBtn.setText(Messages.getString("WordQuizAffPanel.ButtonPlaySound")); //$NON-NLS-1$
		this.add(jPanelSonBtn, BorderLayout.CENTER);
		jPanelSonBtn.setVisible(true);
	}

	// </JiglooProtected>
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.QuizQuestionPanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

}
