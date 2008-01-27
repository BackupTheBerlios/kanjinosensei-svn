package vue.kanji;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLEditorKit;

import metier.Messages;
import vue.JPanelImageBg;
import vue.JPanelImageBg.ImageLoadingException;
import vue.VueElement.NoAffException;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.kanji.KanjiQuizConfigPanel.ETypeAff;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
class KanjiQuizAffPanel extends javax.swing.JPanel implements QuizQuestionPanel, QuizSolutionPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private JEditorPane			jEditorPane;

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
		this.setBackground(new java.awt.Color(255, 255, 255));
		
		this.add(compute(typeAff), BorderLayout.CENTER);
		this.doLayout();
	}

	private JPanel compute(ETypeAff typeAff) throws NoAffException
	{
		// <NoJigloo>
		String texte = "", image = ""; //$NON-NLS-1$ //$NON-NLS-2$
		int taille_fonte = -1;

		JPanel jPanel = new JPanel(new BorderLayout(0, 0));
		
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
				texte = vue.toRomajiIfNeeded(vue.getKanji().getLecturesON());
				taille_fonte = 16;
				break;
			}
	
			case LectureJaponaise:
			{
				texte = vue.toRomajiIfNeeded(vue.getKanji().getLecturesKUN());
				taille_fonte = 16;
				break;
			}
	
			case ImageTrace:
			{
				image = vue.getKanji().getStrokeOrderPicture();
				break;
			}
	
			case Signification:
			{
				texte = vue.getKanji().getSignifications();
				taille_fonte = 16;
				break;
			}
	
			case KanjiEtSignification:
			{
				texte = "<table border=\"0\"><tr><td><font style=\"font-size: 50pt\">" + vue.getKanji().getCodeUTF8().toString() + "</font></td>";
				texte += "<td><font style=\"font-size: 16pt\">" + Messages.getString("KanjiVueDetaillePanel.LabelSignifications") + " : " + vue.getKanji().getSignifications() + "</font></td></tr></table>";
				// taille_fonte = 100;
				break;
			}
	
			case KanjiEtLectures:
			{
				texte = "<table border=\"0\"><tr><td><font style=\"font-size: 50pt\">" + vue.getKanji().getCodeUTF8().toString() + "</font></td>";
				texte += "<td><font style=\"font-size: 16pt\">" + Messages.getString("KanjiVueDetaillePanel.LabelONLectures") + " : " + vue.getKanji().getLecturesON() + "<br/>";
				texte += Messages.getString("KanjiVueDetaillePanel.LabelKUNLectures") + " : " + vue.getKanji().getLecturesKUN() + "</font></td></tr></table>";
				// taille_fonte = 12;
				break;
			}
			
			case ImageTraceEtDetaille:
			{
				JPanel imgTrace = compute(ETypeAff.ImageTrace);
				JPanel detaille = vue.getVueDetaillePanel();
				jPanel.add(imgTrace, BorderLayout.WEST);
				jPanel.add(detaille, BorderLayout.CENTER);
				
				return jPanel;
			}
		}

		if (texte.isEmpty() && image.isEmpty())
		{
			throw new NoAffException(typeAff.toString());
		}

		if ( !texte.isEmpty())
		{
			addJLabel(jPanel, texte, taille_fonte);
			// setPanelSize(panel_w, panel_h);
		}

		if ( !image.isEmpty())
		{
			addImage(jPanel, image);
		}
		
		return jPanel;
		// </NoJigloo>
	}

	// <JiglooProtected>

	private void addJLabel(JPanel panel, String text, int taille)
	{
		jEditorPane = new JEditorPane("text/html", null);

		HTMLEditorKit htmlEditoKit = (HTMLEditorKit) jEditorPane.getEditorKit();

		if (taille > 0)
		{
			// jEditorPane.setFont(new java.awt.Font("SimSun", 0, taille)); //$NON-NLS-1$
			text = "<font style=\"font-size: " + taille + "pt\">" + text + "</font>";
		}

		jEditorPane.setText(text);
		jEditorPane.setEditable(false);

		panel.add(jEditorPane, BorderLayout.NORTH);
	}

	private void addImage(JPanel panel, String source)
	{
		if (source.compareTo(vue.getKanji().getStrokeOrderPicture()) == 0)
		{
			panel.add(vue.getStrokeOrdersImgComponent());
		}
		else
		{
			JPanelImageBg jPanelImageBg;
			try
			{
				jPanelImageBg = new JPanelImageBg(source, JPanelImageBg.eImageDisplayMode.CENTRE);
				jPanelImageBg.setPreferredSize(jPanelImageBg.getImageDimension());
				panel.add(jPanelImageBg, BorderLayout.CENTER);
				jPanelImageBg.setVisible(true);
			}
			catch (ImageLoadingException e)
			{
				System.err.println(Messages.getString("JPanelImageBg.ErrorLoadingImageFile") + " : \"" + source + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

		}
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
