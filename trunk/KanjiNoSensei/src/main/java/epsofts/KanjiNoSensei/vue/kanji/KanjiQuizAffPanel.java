package epsofts.KanjiNoSensei.vue.kanji;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.utils.MyAutoResizingText;
import epsofts.KanjiNoSensei.utils.MyUtils;
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
		
		compute(typeAff, this);
		this.doLayout();
	}

	private void compute(ETypeAff typeAff, JPanel jPanel) throws NoAffException
	{
		// <NoJigloo>
		String texte = "", image = ""; //$NON-NLS-1$ //$NON-NLS-2$
		int taille_fonte = -1;
		boolean useHTML = false;
		
		switch (typeAff)
		{
			case Kanji:
			{
				texte = vue.getKanji().getCodeUTF8().toString();
				taille_fonte = 50;
				useHTML = false;
				break;
			}
	
			case LectureOrigineChinoise:
			{
				texte = vue.toRomajiIfNeeded(vue.getKanji().getLecturesON());
				taille_fonte = 16;
				useHTML = false;
				break;
			}
	
			case LectureJaponaise:
			{
				texte = vue.toRomajiIfNeeded(vue.getKanji().getLecturesKUN());
				taille_fonte = 16;
				useHTML = false;
				break;
			}
	
			case ImageTrace:
			{
				image = vue.getKanji().getStrokeOrderPicture();
				useHTML = false;
				break;
			}
	
			case Signification:
			{
				texte = vue.getKanji().getSignifications();
				taille_fonte = 16;
				useHTML = false;
				break;
			}
	
			case KanjiEtSignification:
			{
				texte = "<table border=\"0\"><tr><td><font style=\"font-size: 50pt\">" + vue.getKanji().getCodeUTF8().toString() + "</font></td>"; //$NON-NLS-1$ //$NON-NLS-2$
				texte += "<td><font style=\"font-size: 16pt\">" + Messages.getString("KanjiVueDetaillePanel.LabelSignifications") + " : " + vue.getKanji().getSignifications() + "</font></td></tr></table>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				useHTML = true;
				break;
			}
	
			case KanjiEtLectures:
			{
				texte = "<table border=\"0\"><tr><td><font style=\"font-size: 50pt\">" + vue.getKanji().getCodeUTF8().toString() + "</font></td>"; //$NON-NLS-1$ //$NON-NLS-2$
				texte += "<td><font style=\"font-size: 16pt\">" + Messages.getString("KanjiVueDetaillePanel.LabelONLectures") + " : " + vue.getKanji().getLecturesON() + "<br/>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				texte += Messages.getString("KanjiVueDetaillePanel.LabelKUNLectures") + " : " + vue.getKanji().getLecturesKUN() + "</font></td></tr></table>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				useHTML = true;
				break;
			}
			
			case ImageTraceEtDetaille:
			{
				JPanel imgTrace = new JPanel(new BorderLayout());
				compute(ETypeAff.ImageTrace, imgTrace);
				JPanel detaille = vue.getVueDetaillePanel();
				jPanel.add(imgTrace, BorderLayout.WEST);
				jPanel.add(detaille, BorderLayout.CENTER);
				jPanel.doLayout();
				return;
			}
		}

		if (texte.isEmpty() && image.isEmpty())
		{
			throw new NoAffException(typeAff.toString());
		}

		if ( !texte.isEmpty())
		{
			addLabel(jPanel, texte, taille_fonte, useHTML, (image.isEmpty()?BorderLayout.CENTER:BorderLayout.NORTH));
		}

		if ( !image.isEmpty())
		{
			addImage(jPanel, image);
		}
		
		int panelPrefWidth = 0, panelPrefHeight = 0, panelMinWidth = 0, panelMinHeight = 0; 
		for(Component c: jPanel.getComponents())
		{
			panelMinWidth = Math.max(panelMinWidth, c.getMinimumSize().width);
			panelMinHeight += c.getMinimumSize().height;
			
			panelPrefWidth = Math.max(panelPrefWidth, c.getPreferredSize().width);
			panelPrefHeight += c.getPreferredSize().height;
		}
		
		MyUtils.fixComponentSizes(jPanel, panelMinWidth, panelMinHeight, panelPrefWidth, panelPrefHeight);
		
		jPanel.doLayout();
		return;
		// </NoJigloo>
	}

	// <JiglooProtected>

	private void addLabel(JPanel panel, String text, int taille, boolean useHTML, String borderLayout)
	{
		if (useHTML)
		{
			JEditorPane jEditorPane = new JEditorPane("text/html", text);
			jEditorPane.setEditable(false);
			panel.add(jEditorPane, borderLayout);
		}
		else
		{
			MyAutoResizingText<JLabel> jAutoSizeLabel = MyAutoResizingText.createSafely(JLabel.class, taille, VueKanji.FONT_MAX_SIZE);
			jAutoSizeLabel.setText(text);
			jAutoSizeLabel.setScrollBarsVisibility(true);
			panel.add(jAutoSizeLabel, borderLayout);
		}
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
