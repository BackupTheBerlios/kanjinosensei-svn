package vue.word;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
import vue.JPanelSonBtn;
import vue.VueElement;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.VueElement.VueDetaillePanel;

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
				jButtonJouerSon = new JPanelSonBtn(vue.getMot().getSoundFile(), false);
				this.add(jButtonJouerSon, BorderLayout.EAST);
				jButtonJouerSon.setText(Messages.getString("WordVueDetaillePanel.ButtonPlaySound")); //$NON-NLS-1$
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
						System.out.println("Selection '" + sel + "'"); //$NON-NLS-1$ //$NON-NLS-2$
						Dictionary dictionnaire = vue.getApp().getDictionnaire();

						Element e = dictionnaire.chercherElement(new Kanji(sel.charAt(0), "", "", "", "", "").getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

						if (e == null)
						{
							System.err.println(Messages.getString("WordVueDetaillePanel.WarningKanjiMissing") + " : \"" + sel.charAt(0) + "(" + sel.substring(1) + ")\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							return;
						}

						if ( !Kanji.class.isInstance(e))
						{
							System.err.println(Messages.getString("WordVueDetaillePanel.WarningNotAKanji") + " : \"" + e.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							return;
						}

						VueElement vueElement = null;
						try
						{
							vueElement = VueElement.genererVueElement(vue.getApp(), e, vue.useRomaji());
						}
						catch (Exception e1)
						{
							System.err.println(Messages.getString("WordVueDetaillePanel.ErrorViewGeneration") + " : \"" + e.toString() + "\"\t" + e1.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							return;
						}

						JDialog kanjiDetail = new JDialog((JDialog) null, Messages.getString("WordVueDetaillePanel.Detail") + " : \"" + e.toString() + "\"", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						kanjiDetail.add(vueElement.getVueDetaillePanel().getPanel());
						kanjiDetail.pack();
						kanjiDetail.setVisible(true);
					}
				}
			});
		}
		return jEditorPane;
	}

	// <NoJigloo>
	private void dynamicInitialize()
	{
		Word mot = vue.getMot();
		StringBuilder sb = new StringBuilder();
		
		sb.append("<b><center>" + mot.getWord() + "</center></b>");
		sb.append(Messages.getString("WordVueDetaillePanel.LabelLecture") + " : <b>" + vue.toRomajiIfNeeded(mot.getLecture()) + "</b><br>"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(Messages.getString("WordVueDetaillePanel.LabelSignifications") + " : <b>" + mot.getSignifications() + "</b><br>"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(Messages.getString("WordVueDetaillePanel.LabelThemes") + " : <b>" + mot.getThemes() + "</b><br>"); //$NON-NLS-1$ //$NON-NLS-2$
		
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
