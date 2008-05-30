/**
 * 
 */
package vue.kanji;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import metier.Dictionary;
import metier.elements.Element;
import metier.elements.Kanji;
import utils.MyAutoResizingText;
import vue.JPanelImageBg;
import vue.KanjiNoSensei;
import vue.VueElement;
import vue.JPanelImageBg.ImageLoadingException;
import vue.kanji.KanjiQuizConfigPanel.ETypeAff;

/**
 * @author Axan
 * 
 */
public class VueKanji extends VueElement
{
	final private static Boolean	USE_ONLY_STROKEORDERS_FONT	= true;

	public static final float	FONT_MAX_SIZE	= 22;

	public static final float	FONT_MIN_SIZE	= 11;

	private static Font				strokeOrdersFont			= null;
	{
		try
		{
			strokeOrdersFont = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("KanjiNoSenseiWorkingDirectory")+File.separatorChar+Element.DICO_DIR + File.separatorChar + "fonts" + File.separatorChar + "KanjiStrokeOrders.ttf")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		catch (Exception e)
		{
			strokeOrdersFont = null;
		}
	}

	private Kanji					kanji						= null;

	private KanjiVueDetaillePanel	jVueDetaillePanel			= null;

	private KanjiEditionDialog		jEditionDialog				= null;

	private QuizQuestionPanel		jQuizQuestionPanel			= null;

	private QuizSolutionPanel		jQuizSolutionPanel			= null;

	public VueKanji(KanjiNoSensei app, Kanji kanji, boolean useRomaji)
	{
		super(app, useRomaji);
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
	public QuizSaisieReponsePanel getQuizSaisieReponsePanel(Dictionary dico) throws NoSaisieException
	{
		return KanjiQuizSaisiePanel.getKanjiQuizSaisiePanel(this, dico);
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

	protected JComponent getStrokeOrdersImgComponent()
	{
		if (jPanelImageBg == null)
		{
			try
			{
				if ((strokeOrdersFont != null) && USE_ONLY_STROKEORDERS_FONT) throw new ImageLoadingException("USE_ONLY_STROKEORDERS_FONT"); //$NON-NLS-1$
				jPanelImageBg = new JPanelImageBg(getKanji().getStrokeOrderPicture(), JPanelImageBg.eImageDisplayMode.CENTRE);
				return jPanelImageBg;
			}
			catch (ImageLoadingException e)
			{
				jPanelImageBg = null;

				if (jPanelStrokeOrdersFont == null)
				{
					jPanelStrokeOrdersFont = new JPanel(new BorderLayout(2, 2));

					if (strokeOrdersFont != null)
					{
						JLabel jLabelStrokeFont = new JLabel(getKanji().getCodeUTF8().toString());
						jLabelStrokeFont.setFont(strokeOrdersFont);
						MyAutoResizingText<JLabel> jAutoSizeLabelStrokeFont = new MyAutoResizingText<JLabel>(jLabelStrokeFont, 100, Float.POSITIVE_INFINITY);
						jLabelStrokeFont.setVisible(true);
						jPanelStrokeOrdersFont.add(jLabelStrokeFont, BorderLayout.CENTER);
						jPanelStrokeOrdersFont.setVisible(true);
						jPanelStrokeOrdersFont.doLayout();
					}
				}

				return jPanelStrokeOrdersFont;
			}
		}

		return jPanelImageBg;
	}
}
