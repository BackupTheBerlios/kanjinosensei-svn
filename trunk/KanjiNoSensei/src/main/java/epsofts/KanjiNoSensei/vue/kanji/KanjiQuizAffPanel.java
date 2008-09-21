package epsofts.KanjiNoSensei.vue.kanji;

import java.awt.BorderLayout;
import java.util.logging.Level;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.utils.MyAutoResizingText;
import epsofts.KanjiNoSensei.vue.JPanelImageBg;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;
import epsofts.KanjiNoSensei.vue.JPanelImageBg.ImageLoadingException;
import epsofts.KanjiNoSensei.vue.VueElement.NoAffException;
import epsofts.KanjiNoSensei.vue.VueElement.QuizQuestionPanel;
import epsofts.KanjiNoSensei.vue.VueElement.QuizSolutionPanel;
import epsofts.KanjiNoSensei.vue.kanji.KanjiQuizConfigPanel.ETypeAff;


/**
 * This class is used to compute Kanji display panel.
 */
class KanjiQuizAffPanel extends javax.swing.JPanel implements QuizQuestionPanel, QuizSolutionPanel
{
	/** Serialization version. */
	private static final long	serialVersionUID	= 1L;

	private JEditorPane			jEditorPane;

	/** Kanji view associated with this panel. */
	VueKanji					vue					= null;

	/** Display mode. */
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
				texte = "<table border=\"0\"><tr><td><font style=\"font-size: 50pt\">" + vue.getKanji().getCodeUTF8().toString() + "</font></td>"; //$NON-NLS-1$ //$NON-NLS-2$
				texte += "<td><font style=\"font-size: 16pt\">" + Messages.getString("KanjiVueDetaillePanel.LabelSignifications") + " : " + vue.getKanji().getSignifications() + "</font></td></tr></table>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				// taille_fonte = 100;
				break;
			}
	
			case KanjiEtLectures:
			{
				texte = "<table border=\"0\"><tr><td><font style=\"font-size: 50pt\">" + vue.getKanji().getCodeUTF8().toString() + "</font></td>"; //$NON-NLS-1$ //$NON-NLS-2$
				texte += "<td><font style=\"font-size: 16pt\">" + Messages.getString("KanjiVueDetaillePanel.LabelONLectures") + " : " + vue.getKanji().getLecturesON() + "<br/>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				texte += Messages.getString("KanjiVueDetaillePanel.LabelKUNLectures") + " : " + vue.getKanji().getLecturesKUN() + "</font></td></tr></table>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				// taille_fonte = 12;
				break;
			}
			
			case ImageTraceEtDetaille:
			{
				JPanel imgTrace = compute(ETypeAff.ImageTrace);
				JPanel detaille = vue.getVueDetaillePanel();
				jPanel.add(imgTrace, BorderLayout.WEST);
				jPanel.add(detaille, BorderLayout.CENTER);
				jPanel.doLayout();
				
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
		
		jPanel.doLayout();
		return jPanel;
		// </NoJigloo>
	}

	// <JiglooProtected>

	private void addJLabel(JPanel panel, String text, int taille)
	{
		MyAutoResizingText<JEditorPane> jAutoSizeEditorPane = MyAutoResizingText.createSafely(JEditorPane.class, taille, VueKanji.FONT_MAX_SIZE);
		jEditorPane = jAutoSizeEditorPane.getJComponent();

		jEditorPane.setContentType("text/html");
		//HTMLEditorKit htmlEditoKit = (HTMLEditorKit) jEditorPane.getEditorKit();

		if (taille > 0)
		{
			// jEditorPane.setFont(new java.awt.Font("SimSun", 0, taille)); //$NON-NLS-1$
			text = "<font style=\"font-size: " + taille + "pt\">" + text + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		jEditorPane.setText(text);
		jEditorPane.setEditable(false);

		
		
		panel.add(jAutoSizeEditorPane, BorderLayout.NORTH);
	}

	private void addImage(JPanel panel, String source)
	{
		if (source.compareTo(vue.getKanji().getStrokeOrderPicture()) == 0)
		{
			panel.add(vue.getStrokeOrdersPanel());
		}
		else
		{
			JPanelImageBg jPanelImageBg;
			try
			{
				jPanelImageBg = new JPanelImageBg(source);
				jPanelImageBg.setPreferredSize(jPanelImageBg.getImageDimension());
				panel.add(jPanelImageBg, BorderLayout.CENTER);
				jPanelImageBg.setVisible(true);
			}
			catch (ImageLoadingException e)
			{
				KanjiNoSensei.log(Level.SEVERE, Messages.getString("JPanelImageBg.ErrorLoadingImageFile") + " : \"" + source + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
