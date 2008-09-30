package epsofts.KanjiNoSensei.vue.word;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import epsofts.KanjiNoSensei.metier.Dictionary;
import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.metier.elements.Kanji;
import epsofts.KanjiNoSensei.metier.elements.Word;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.vue.Config;
import epsofts.KanjiNoSensei.vue.JPanelSonBtn;
import epsofts.KanjiNoSensei.vue.KanjiNoSensei;
import epsofts.KanjiNoSensei.vue.VueElement;
import epsofts.KanjiNoSensei.vue.VueElement.QuizQuestionPanel;
import epsofts.KanjiNoSensei.vue.VueElement.QuizSolutionPanel;
import epsofts.KanjiNoSensei.vue.VueElement.VueDetaillePanel;


/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
class WordVueDetaillePanel extends javax.swing.JPanel implements VueDetaillePanel, QuizQuestionPanel, QuizSolutionPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID				= 1L;

	private VueWord				vue								= null;

	private JScrollPane			jScrollPaneInfos				= null;

	private JToggleButton		jButtonJouerSon					= null;

	private JEditorPane			jEditorPane						= null;

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

	public WordVueDetaillePanel(VueWord vue)
	{
		super();
		this.vue = vue;
		initGUI();
		//<NoJigloo>
		dynamicInitialize();
		//</NoJigloo>
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(600, 100));
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setSize(600, 100);
			this.add(getJButtonJouerSon(), BorderLayout.EAST);
			this.add(getJScrollPaneInfos(), BorderLayout.CENTER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private JToggleButton getJButtonJouerSon()
	{
		if (jButtonJouerSon == null)
		{
			jButtonJouerSon = new JPanelSonBtn(vue.getMot().getSoundFile(), false);
			jButtonJouerSon.setText(Messages.getString("WordVueDetaillePanel.ButtonPlaySound")); //$NON-NLS-1$
			jButtonJouerSon.setSize(35, 20);
		}
		return jButtonJouerSon;
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
						Dictionary dictionnaire = KanjiNoSensei.getApp().getDictionnaire();

						Element e = dictionnaire.getElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

						if (e == null)
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("WordVueDetaillePanel.WarningKanjiMissing") + " : \"" + sel.charAt(0) + "(" + sel.substring(1) + ")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							return;
						}

						if ( !Kanji.class.isInstance(e))
						{
							KanjiNoSensei.log(Level.WARNING, Messages.getString("WordVueDetaillePanel.WarningNotAKanji") + " : \"" + e.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							return;
						}

						VueElement vueElement = null;
						try
						{
							vueElement = VueElement.genererVueElement(e);
						}
						catch (Exception e1)
						{
							KanjiNoSensei.log(Level.SEVERE, Messages.getString("WordVueDetaillePanel.ErrorViewGeneration") + " : \"" + e.toString() + "\"\t" + e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							return;
						}

						JDialog kanjiDetail = new JDialog((JDialog) null, Messages.getString("WordVueDetaillePanel.Detail") + " : \"" + e.toString() + "\"", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						kanjiDetail.setContentPane(vueElement.getVueDetaillePanel().getPanel());
						kanjiDetail.pack();
						kanjiDetail.setVisible(true);
					}
				}
			});
		}
		return jEditorPane;
	}

	// <NoJigloo>

	protected void setSizes()
	{
		getJEditorPane().setMinimumSize(getJEditorPane().getPreferredSize());
		getJEditorPane().setMaximumSize(getJEditorPane().getPreferredSize());
		
		getJButtonJouerSon().setMinimumSize(getJButtonJouerSon().getPreferredSize());
		getJButtonJouerSon().setMaximumSize(getJButtonJouerSon().getPreferredSize());

		int minX = getJEditorPane().getPreferredSize().width + getJButtonJouerSon().getPreferredSize().width + getJScrollPaneInfos().getVerticalScrollBar().getMaximumSize().width;
		int minY = getJEditorPane().getPreferredSize().height + getJScrollPaneInfos().getHorizontalScrollBar().getMaximumSize().height;

		Dimension pref = MyUtils.wrapTextDimension(new Dimension(minX, minY), Toolkit.getDefaultToolkit().getScreenSize(), 50);
		
		setMinimumSize(pref);
		setMaximumSize(pref);
		setPreferredSize(pref);
	}

	private void dynamicInitialize()
	{
		Word mot = vue.getMot();
		String text = VueElement.computeTemplate(VueWord.DETAILLE_DEFAULT_TEMPLATE, vue);
		getJEditorPane().setText(text);

		doLayout();
		setSizes();
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
