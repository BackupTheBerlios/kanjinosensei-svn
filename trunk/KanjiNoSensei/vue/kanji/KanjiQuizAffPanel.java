package vue.kanji;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import vue.JPanelImageBg;
import vue.VueElement.NoAffException;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.kanji.KanjiQuizConfigPanel.ETypeAff;

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
class KanjiQuizAffPanel extends javax.swing.JPanel implements QuizQuestionPanel, QuizSolutionPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private JLabel				jLabel;
	VueKanji					vue					= null;
	ETypeAff					typeAff				= null;

	public KanjiQuizAffPanel(VueKanji vue, ETypeAff typeAff) throws NoAffException
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
			String texte = "", image = "";
			int taille_fonte = -1;

			switch (typeAff)
			{
				case Kanji:
				{
					texte = vue.getKanji().getCodeUTF8().toString();
					taille_fonte = 50;
					break;
				}

				case LectureOrigineChinoise:
				{
					texte = vue.getKanji().getLecturesChinoises();
					taille_fonte = 16;
					break;
				}

				case LectureJaponaise:
				{
					texte = vue.getKanji().getLecturesJaponaises();
					taille_fonte = 16;
					break;
				}

				case ImageTrace:
				{
					image = vue.getKanji().getOrdreTraits();
					break;
				}

				case Signification:
				{
					texte = vue.getKanji().getSignifications();
					taille_fonte = 16;
					break;
				}
			}

			if (texte.isEmpty() && image.isEmpty())
			{
				throw new NoAffException(typeAff.toString());
			}

			if ( !texte.isEmpty())
			{
				addJLabel(texte, taille_fonte);
				//setPanelSize(panel_w, panel_h);
			}

			if ( !image.isEmpty())
			{
				addImage(image);
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
		this.add(jLabel, BorderLayout.NORTH);
	}

	private void addImage(String source)
	{
		JPanelImageBg jPanelImageBg = new JPanelImageBg(source, JPanelImageBg.CENTRE);
		jPanelImageBg.setPreferredSize(jPanelImageBg.getImageDimension());
		this.add(jPanelImageBg, BorderLayout.CENTER);
		jPanelImageBg.setVisible(true);
	}

	// </JiglooProtected>

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.QuizQuestionPanel#getPanel()
	 */
	public JPanel getPanel()
	{
		return this;
	}
}
