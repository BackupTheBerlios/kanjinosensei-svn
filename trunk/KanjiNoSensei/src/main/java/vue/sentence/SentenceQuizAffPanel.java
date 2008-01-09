package vue.sentence;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import vue.JPanelSonBtn;
import vue.VueElement.NoAffException;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.sentence.SentenceQuizConfigPanel.ETypeAff;

class SentenceQuizAffPanel extends javax.swing.JPanel implements QuizQuestionPanel, QuizSolutionPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private JLabel				jLabel;
	VueSentence						vue					= null;
	ETypeAff					typeAff				= null;

	public SentenceQuizAffPanel(VueSentence vue, ETypeAff typeAff) throws NoAffException
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
			String texte = "", son = "";
			int taille_fonte = -1;

			switch (typeAff)
			{
				case Kanji:
				{
					texte = vue.getPhrase().getSentence();
					taille_fonte = 40;
					break;
				}

				case Lecture:
				{
					texte = vue.toRomajiIfNeeded(vue.getPhrase().getLecture());
					taille_fonte = 16;
					break;
				}


				case Son:
				{
					son = vue.getPhrase().getSound();
					break;
				}

				case Signification:
				{
					texte = vue.getPhrase().getSignifications();
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
		jLabel = new JLabel();
		jLabel.setText(text);
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		jLabel.setFont(new java.awt.Font("SimSun", 0, taille));
		jLabel.setOpaque(false);
		this.add(jLabel, BorderLayout.NORTH);
	}

	private void addSon(String source)
	{
		JPanelSonBtn jPanelSonBtn = new JPanelSonBtn(source, true);
		jPanelSonBtn.setText("Lire");
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