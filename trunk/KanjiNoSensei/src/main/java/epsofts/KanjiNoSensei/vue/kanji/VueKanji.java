/**
 * 
 */
package epsofts.KanjiNoSensei.vue.kanji;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import epsofts.KanjiNoSensei.metier.Dictionary;
import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.utils.MyAutoResizingText;
import epsofts.KanjiNoSensei.vue.JPanelImageBg;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;
import epsofts.KanjiNoSensei.vue.VueElement;
import epsofts.KanjiNoSensei.vue.JPanelImageBg.ImageLoadingException;
import epsofts.KanjiNoSensei.vue.kanji.KanjiQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueKanji extends VueElement
{
	private static enum StrokeOrdersType
	{
		FONT, IMG
	};

	static private final StrokeOrdersType	STROKE_ORDERS_TYPE			= StrokeOrdersType.FONT;

	static private final Boolean			STROKE_ORDERS_TYPE_FALLBACK	= true;

	public static final float				FONT_MAX_SIZE				= 98;						// 22;

	public static final float				FONT_MIN_SIZE				= 11;

	private static Font						strokeOrdersFont			= null;

	private Kanji							kanji						= null;

	private KanjiVueDetaillePanel			jVueDetaillePanel			= null;

	private KanjiEditionDialog				jEditionDialog				= null;

	private QuizQuestionPanel				jQuizQuestionPanel			= null;

	private QuizSolutionPanel				jQuizSolutionPanel			= null;

	private static Font getStrokeOrdersFont()
	{
		if (strokeOrdersFont == null)
		{
			try
			{
				strokeOrdersFont = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("KanjiNoSenseiWorkingDirectory") + File.separatorChar + Element.DICO_DIR + File.separatorChar + "fonts" + File.separatorChar + "KanjiStrokeOrders.ttf")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			catch (Exception e)
			{
				strokeOrdersFont = null;
			}
		}

		return strokeOrdersFont;
	}

	public VueKanji(Kanji kanji, boolean useRomaji)
	{
		super(useRomaji);
		this.kanji = kanji;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getEditionDialog()
	 */
	public EditionDialog getEditionDialog()
	{
		if (jEditionDialog == null)
		{
			jEditionDialog = new KanjiEditionDialog(this);
		}

		return jEditionDialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizQuestionPanel(java.lang.String)
	 */
	public QuizQuestionPanel getQuizQuestionPanel() throws NoAffException
	{
		if (jQuizQuestionPanel == null)
		{
			if (KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getAffichageElementQuiz() == ETypeAff.Detaille)
			{
				jQuizQuestionPanel = getVueDetaillePanel();
			}
			else
			{
				jQuizQuestionPanel = new KanjiQuizAffPanel(this, KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getAffichageElementQuiz());
			}
		}
		return jQuizQuestionPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizSaisieReponsePanel(java.lang.String)
	 */
	public QuizSaisieReponsePanel getQuizSaisieReponsePanel() throws NoSaisieException
	{
		return KanjiQuizSaisiePanel.getKanjiQuizSaisiePanel(this);
	}

	public synchronized QuizSolutionPanel getQuizSolutionPanelCopy()
	{
		QuizSolutionPanel ref = jQuizSolutionPanel;
		QuizSolutionPanel copy = getQuizSolutionPanel(false);
		jQuizSolutionPanel = ref;
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizSolutionPanel(java.lang.String)
	 */
	public QuizSolutionPanel getQuizSolutionPanel(boolean newCopy)
	{
		if (newCopy)
		{
			return getQuizSolutionPanelCopy();
		}

		if (jQuizSolutionPanel == null)
		{
			if (KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getAffichageReponseQuiz() == ETypeAff.Detaille)
			{
				jQuizSolutionPanel = getVueDetaillePanel();
			}
			else
			{
				try
				{
					jQuizSolutionPanel = new KanjiQuizAffPanel(this, KanjiQuizConfigPanel.getKanjiQuizConfigPanel().getAffichageReponseQuiz());
				}
				catch (NoAffException e)
				{
					try
					{
						jQuizSolutionPanel = new KanjiQuizAffPanel(this, ETypeAff.Kanji);
					}
					catch (NoAffException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}
		return jQuizSolutionPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getVueDetaille()
	 */
	public KanjiVueDetaillePanel getVueDetaillePanel()
	{
		if (jVueDetaillePanel == null)
		{
			jVueDetaillePanel = new KanjiVueDetaillePanel(this);
		}

		return jVueDetaillePanel;
	}

	/**
	 * @return
	 */
	protected Kanji getKanji()
	{
		return kanji;
	}

	/**
	 * @param kanji
	 */
	protected void setKanji(Kanji kanji)
	{
		this.kanji = kanji;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getQuizConfigPanel()
	 */
	@Override
	public QuizConfigPanel getQuizConfigPanel()
	{
		return KanjiQuizConfigPanel.getKanjiQuizConfigPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement#getElement()
	 */
	@Override
	public Element getElement()
	{
		return kanji;
	}

	/**
	 * This method initializes jPanelImageBg
	 * 
	 * @return vue.JPanelImageBg
	 */
	private JPanelImageBg	jPanelImageBg			= null;

	private JPanel			jPanelStrokeOrdersFont	= null;

	private JPanel			jPanelStrokeOrders		= null;

	private void addStrokeOrderImgToPanel(JPanel panel) throws ImageLoadingException
	{
		JPanelImageBg panelImgBg = new JPanelImageBg(getKanji().getStrokeOrderPicture());
		panel.add(panelImgBg, BorderLayout.CENTER);
	}

	private void addStrokeOrderFontToPanel(JPanel panel) throws Exception
	{
		if (getStrokeOrdersFont() == null)
		{
			throw new Exception("Font can't loaded");
		}

		MyAutoResizingText<JLabel> jAutoSizeLabelStrokeFont = MyAutoResizingText.createSafely(JLabel.class, 100, Float.POSITIVE_INFINITY, 0, (float) -0.5);
		jAutoSizeLabelStrokeFont.setScrollBarsVisibility(false);
		
		JLabel jLabelStrokeFont = jAutoSizeLabelStrokeFont.getJComponent();
		
		jLabelStrokeFont.setFont(getStrokeOrdersFont());
		jLabelStrokeFont.setText(getKanji().getCodeUTF8().toString());
		jLabelStrokeFont.setHorizontalAlignment(JLabel.CENTER);

		panel.add(jAutoSizeLabelStrokeFont, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(0, 0));
	}

	protected JPanel getStrokeOrdersPanel()
	{
		if (jPanelStrokeOrders == null)
		{
			jPanelStrokeOrders = new JPanel(new BorderLayout());

			try
			{
				switch (STROKE_ORDERS_TYPE)
				{
				case FONT:
				{
					try
					{
						addStrokeOrderFontToPanel(jPanelStrokeOrders);
					}
					catch (Exception e)
					{
						if (STROKE_ORDERS_TYPE_FALLBACK)
						{
							addStrokeOrderImgToPanel(jPanelStrokeOrders);
						}
						else
						{
							throw e;
						}
					}
					break;
				}
				case IMG:
				{
					try
					{
						addStrokeOrderImgToPanel(jPanelStrokeOrders);
					}
					catch (Exception e)
					{
						if (STROKE_ORDERS_TYPE_FALLBACK)
						{
							addStrokeOrderFontToPanel(jPanelStrokeOrders);
						}
						else
						{
							throw e;
						}
					}
					break;
				}
				default:
				{
					throw new Exception("StrokeOrderType \"" + STROKE_ORDERS_TYPE + "\" not supported.");
				}
				}
			}
			catch (Exception e)
			{
				String errMsg = (STROKE_ORDERS_TYPE_FALLBACK?"Aucun tracé disponible.":"Tracé ("+STROKE_ORDERS_TYPE+") introuvable.");
				jPanelStrokeOrders.add(new JLabel("Erreur: "+errMsg), BorderLayout.CENTER);
			}

			jPanelStrokeOrders.doLayout();
		}

		return jPanelStrokeOrders;
	}
}
