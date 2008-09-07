package vue.sentence;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import metier.Dictionary;
import metier.Messages;
import metier.elements.Element;
import metier.elements.Kanji;
import metier.elements.Sentence;
import metier.elements.Word;
import utils.MyAutoResizingText;
import utils.MyUtils;
import vue.JPanelSonBtn;
import vue.KanjiNoSensei;
import vue.VueElement;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.VueElement.VueDetaillePanel;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
class SentenceVueDetaillePanel extends javax.swing.JPanel implements VueDetaillePanel, QuizQuestionPanel, QuizSolutionPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID				= 1L;

	private VueSentence			vue								= null;

	private JScrollPane			jScrollPaneInfos;

	private JTextArea			jTextPane;

	private JToggleButton		jButtonJouerSon;

	private final JPanel		vueDetaillePanel				= this;

	private final MouseAdapter	vueDetaillePanelMouseAdapter	= new MouseAdapter()
																{

																	@Override
																	public void mouseClicked(MouseEvent e)
																	{
																		vueDetaillePanel.dispatchEvent(e);
																		super.mouseClicked(e);
																	}

																};

	private JEditorPane			jEditorPane;

	public SentenceVueDetaillePanel(VueSentence vue)
	{
		super();
		this.vue = vue;
		initGUI();
		SwingUtilities.invokeLater(new Runnable()
		{

			public void run()
			{
				dynamicInitialize();
			}

		});
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(600, 100));
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setSize(600, 100);
			{
				jButtonJouerSon = new JPanelSonBtn(vue.getPhrase().getSound(), false);
				this.add(jButtonJouerSon, BorderLayout.EAST);
				jButtonJouerSon.setText(Messages.getString("SentenceVueDetaillePanel.ButtonPlaySound")); //$NON-NLS-1$
				jButtonJouerSon.setSize(35, 20);
			}
			this.add(getJScrollPaneInfos(), BorderLayout.CENTER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private JScrollPane getJScrollPaneInfos()
	{
		if (jScrollPaneInfos == null)
		{
			jScrollPaneInfos = new JScrollPane();
			jScrollPaneInfos.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jScrollPaneInfos.setViewportView(getJEditorPane());
			
			jScrollPaneInfos.getHorizontalScrollBar().setPreferredSize(new java.awt.Dimension(0, 16));
			jScrollPaneInfos.getHorizontalScrollBar().setMinimumSize(new java.awt.Dimension(0, 16));
			jScrollPaneInfos.addMouseListener(vueDetaillePanelMouseAdapter);
		}
		
		return jScrollPaneInfos;
	}
	
	private JEditorPane getJEditorPane()
	{
		if (jEditorPane == null)
		{
			jEditorPane = new JEditorPane();
			jEditorPane.setContentType("text/html");
			jEditorPane.setEditable(false);

			jEditorPane.addMouseListener(vueDetaillePanelMouseAdapter);
			jEditorPane.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					String sel = jEditorPane.getSelectedText();
					if ((sel != null) && ( !sel.isEmpty()))
					{
						MyUtils.trace(Level.FINE, "Selection '" + sel + "'"); //$NON-NLS-1$ //$NON-NLS-2$
						Dictionary dictionnaire = vue.getApp().getDictionnaire();

						Element e = dictionnaire.chercherElement(new Word(sel, "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

						if (e == null)
						{
							e = dictionnaire.chercherElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
						}

						if (e == null)
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("SentenceVueDetaillePanel.ErrorMissingWordOrKanji") + " : \"" + sel.charAt(0) + "(" + sel.substring(1) + ")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							return;
						}

						if (( !Kanji.class.isInstance(e)) && ( !Word.class.isInstance(e)))
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("SentenceVueDetaillePanel.ErrorSelectionIsNotCorrect") + " : " + e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
							return;
						}

						VueElement vueElement = null;
						try
						{
							vueElement = VueElement.genererVueElement(vue.getApp(), e, vue.useRomaji());
						}
						catch (Exception e1)
						{
							KanjiNoSensei.log(Level.SEVERE, Messages.getString("SentenceVueDetaillePanel.ErrorViewGeneration") + e.toString() + "\"\tMessage : " + e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
							return;
						}

						JDialog motkanjiDetail = new JDialog((JDialog) null, Messages.getString("SentenceVueDetaillePanel.Detail") + " : (" + Messages.getString(e.getClass().getSimpleName()) + ") \"" + e.toString() + "\"", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						motkanjiDetail.add(vueElement.getVueDetaillePanel().getPanel());
						motkanjiDetail.pack();
						motkanjiDetail.setVisible(true);
					}
				}
			});
		}
		return jEditorPane;
	}

	// <NoJigloo>
	private void dynamicInitialize()
	{
		Sentence phrase = vue.getPhrase();
		StringBuilder sb = new StringBuilder();

		sb.append("<b><center>" + phrase.getSentence() + "</center></b>");
		sb.append(Messages.getString("SentenceVueDetaillePanel.LabelLecture") + " : <b>" + vue.toRomajiIfNeeded(phrase.getLecture()) + "</b><br>"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(Messages.getString("SentenceVueDetaillePanel.LabelSignifications") + " : <b>" + phrase.getSignifications() + "</b><br>"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(Messages.getString("SentenceVueDetaillePanel.LabelThemes") + " : <b>" + phrase.getThemes() + "</b><br>"); //$NON-NLS-1$ //$NON-NLS-2$

		getJEditorPane().setText(sb.toString());
		setVisible(false);
		setVisible(true);
	}

	// </NoJigloo>

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.VueDetaillePanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

}
