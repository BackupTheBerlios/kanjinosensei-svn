/**
 * 
 */
package vue.kanji;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import utils.MyUtils;
import vue.JPanelImageBg;
import vue.VueElement.QuizQuestionPanel;
import vue.VueElement.QuizSolutionPanel;
import vue.VueElement.VueDetaillePanel;

/**
 * @author Axan
 * 
 */
class KanjiVueDetaillePanel extends JPanel implements VueDetaillePanel, QuizQuestionPanel, QuizSolutionPanel
{

	private static final long	serialVersionUID				= 1L;

	public static final int		vueHeight						= 100;

	private VueKanji			vue								= null;

	private JPanel				jPanelCodeUTF8					= null;

	private JScrollPane			jScrollPaneInfos				= null;

	private JLabel				jLabelCodeUTF8					= null;

	private JPanel				jPanelInfos						= null;

	private JDialog				ImgTraceDialog					= null;

	private JLabel				jLabelThemes					= null;

	private JLabel				jLabelSignifications			= null;

	private JLabel				jLabelLecturesJaponaises		= null;

	private JLabel				jLabelLecturesChinoises			= null;

	private JPanel				ImgTraceContentPane				= null;

	private JPanelImageBg		jPanelImageBg					= null;

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

	public KanjiVueDetaillePanel(VueKanji vue)
	{
		super();
		this.vue = vue;
		initialize();
		SwingUtilities.invokeLater(new Runnable()
		{

			public void run()
			{
				dynamicInitialize();
			}

		});
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setLayout(new BorderLayout());
		this.setBounds(new Rectangle(0, 0, 600, 100));
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setPreferredSize(new Dimension(600, 100));
		this.setSize(600, 100);
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.add(getJPanelCodeUTF8(), BorderLayout.WEST);
		this.add(getJScrollPaneInfos(), BorderLayout.CENTER);

	}

	// <NoJigloo>
	private void dynamicInitialize()
	{
		this.setBounds(new Rectangle(0, 0, 600, vueHeight));
		this.setPreferredSize(new Dimension(0, vueHeight));
		this.setSize(0, vueHeight);

		getJPanelCodeUTF8().setFont(new Font("SimSun", Font.PLAIN, vueHeight));
		jPanelCodeUTF8.setPreferredSize(new java.awt.Dimension(100, 100));

		miseAJourInfos();
	}

	protected void miseAJourInfos()
	{
		jLabelCodeUTF8.setText(vue.getKanji().getCodeUTF8().toString());
		jLabelLecturesChinoises.setText("Lectures Chinoises : " + vue.toRomajiIfNeeded(vue.getKanji().getLecturesON()));
		jLabelLecturesJaponaises.setText("Lectures Japonaises : "
				+ vue.toRomajiIfNeeded(vue.getKanji().getLecturesKUN()));
		jLabelSignifications.setText("Significations : " + vue.getKanji().getSignifications());
		jLabelThemes.setText("Thèmes : " + vue.getKanji().getThemes());

		setVisible(false);
		setVisible(true);
	}

	// </NoJigloo>

	/**
	 * This method initializes jPanelCodeUTF8
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelCodeUTF8()
	{
		if (jPanelCodeUTF8 == null)
		{
			jLabelCodeUTF8 = new JLabel();
			jLabelCodeUTF8.setText("猫");
			jLabelCodeUTF8.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelCodeUTF8.setPreferredSize(new java.awt.Dimension(100, 100));
			jLabelCodeUTF8.setFont(new Font("Kochi Mincho", Font.PLAIN, 98));
			jLabelCodeUTF8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelCodeUTF8 = new JPanel();
			jPanelCodeUTF8.setLayout(new BorderLayout());
			jPanelCodeUTF8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelCodeUTF8.setSize(100, 100);
			jPanelCodeUTF8.setPreferredSize(new Dimension(100, 0));
			jLabelCodeUTF8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jLabelCodeUTF8.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					MyUtils.trace("jLabelCodeUTF8.mouseClicked");
					synchronized (jLabelCodeUTF8)
					{
						if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
						{
							ImgTraceDialog = null; // Force recreate the ImgTraceDialog
							JDialog imageTraceDialog = getImgTraceDialog();
							imageTraceDialog.pack();
							Point loc = e.getLocationOnScreen();
							loc.translate(20, 20);
							imageTraceDialog.setLocation(loc);
							MyUtils.trace("jLabelCodeUTF8 double clicked should display imageTraceDialog");
							
							imageTraceDialog.setVisible(true);
						}
						else
						{
							vueDetaillePanelMouseAdapter.mouseClicked(e);
						}
					}
				}
			});

			jPanelCodeUTF8.add(jLabelCodeUTF8, BorderLayout.CENTER);
			jLabelCodeUTF8.setSize(100, 100);
		}
		return jPanelCodeUTF8;
	}

	/**
	 * This method initializes jScrollPaneInfos
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneInfos()
	{
		if (jScrollPaneInfos == null)
		{
			jScrollPaneInfos = new JScrollPane();
			jScrollPaneInfos.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jScrollPaneInfos.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
			jScrollPaneInfos.setViewportView(getJPanelInfos());
			jScrollPaneInfos.setPreferredSize(new java.awt.Dimension(0, 100));
			jScrollPaneInfos.setOpaque(false);
			jScrollPaneInfos.setSize(600, 100);
			jScrollPaneInfos.getHorizontalScrollBar().setPreferredSize(new java.awt.Dimension(0, 16));
			jScrollPaneInfos.getHorizontalScrollBar().setMinimumSize(new java.awt.Dimension(0, 16));
			jScrollPaneInfos.addMouseListener(vueDetaillePanelMouseAdapter);

		}
		return jScrollPaneInfos;
	}

	/**
	 * This method initializes jPanelInfos
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelInfos()
	{
		if (jPanelInfos == null)
		{
			jLabelThemes = new JLabel();
			jLabelThemes.setText("Thèmes");
			jLabelThemes.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelThemes.setVerticalAlignment(SwingConstants.TOP);
			jLabelThemes.setPreferredSize(new Dimension(0, 0));
			jLabelThemes.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jLabelSignifications = new JLabel();
			jLabelSignifications.setText("Significations");
			jLabelSignifications.setPreferredSize(new Dimension(0, 0));
			jLabelSignifications.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelSignifications.setVerticalAlignment(SwingConstants.TOP);
			jLabelSignifications.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jLabelLecturesJaponaises = new JLabel();
			jLabelLecturesJaponaises.setText("Lectures Japonaises");
			jLabelLecturesJaponaises.setVerticalAlignment(SwingConstants.TOP);
			jLabelLecturesJaponaises.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelLecturesChinoises = new JLabel();
			jLabelLecturesChinoises.setText("Lectures Chinoises");
			jLabelLecturesChinoises.setPreferredSize(new java.awt.Dimension(600, 25));
			jLabelLecturesChinoises.setVerticalAlignment(SwingConstants.TOP);
			jLabelLecturesChinoises.setHorizontalTextPosition(SwingConstants.LEFT);
			jLabelLecturesChinoises.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			GridLayout gridLayout = new GridLayout(0, 1, 0, 0);
			jPanelInfos = new JPanel();
			jPanelInfos.setPreferredSize(new java.awt.Dimension(0, 0));
			jPanelInfos.setBounds(new Rectangle(0, 0, 0, 0));
			jPanelInfos.setLayout(gridLayout);
			jPanelInfos.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelInfos.addMouseListener(vueDetaillePanelMouseAdapter);
			jPanelInfos.add(jLabelLecturesChinoises);
			jPanelInfos.add(jLabelLecturesJaponaises);
			jPanelInfos.add(jLabelSignifications);
			jLabelSignifications.setOpaque(true);
			jPanelInfos.add(jLabelThemes);
		}
		return jPanelInfos;
	}

	/**
	 * This method initializes ImgTraceDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JDialog getImgTraceDialog()
	{
		if (ImgTraceDialog == null)
		{
			MyUtils.trace("[1] ImgTraceDialog creation");
			ImgTraceDialog = new JDialog();
			ImgTraceDialog.setSize(new Dimension(0, 0));
			ImgTraceDialog.setTitle("Ordre des traits");
			ImgTraceDialog.setModal(true);
			ImgTraceDialog.setMinimumSize(new Dimension(100, 100));
			ImgTraceDialog.setPreferredSize(new Dimension(0, 0));
			ImgTraceDialog.setContentPane(getImgTraceContentPane());
			ImgTraceDialog.setUndecorated(false);
			ImgTraceDialog.setBackground(Color.black);

			ImgTraceDialog.addComponentListener(new ComponentAdapter()
			{
			
				@Override
				public void componentShown(ComponentEvent e)
				{
					MyUtils.trace("[4] ImgTraceDialog < componentShown");
					MyUtils.assertTrue((ImgTraceDialog.getContentPane() == getImgTraceContentPane()) && (ImgTraceDialog.getContentPane() != null), "ImgTraceDialog unexpected contentPane");

					Dimension d = getJPanelImageBg().getImageDimension();
					MyUtils.assertFalse((d.width <= 10) || (d.height <= 10),
							"ImgTraceDialog.windowOpened : incorrect image dimension");

					ImgTraceDialog.setPreferredSize(new Dimension(d.width + 20, d.height + 30));
					ImgTraceDialog.setSize(ImgTraceDialog.getPreferredSize());
	
					ImgTraceContentPane.doLayout();
					ImgTraceContentPane.repaint();
					ImgTraceContentPane.revalidate();
					ImgTraceContentPane.setVisible(true);
					
					MyUtils.assertTrue(ImgTraceDialog.getContentPane().isDisplayable(), "ContentPane not displayable");
					MyUtils.assertTrue(ImgTraceDialog.getContentPane().isVisible(), "ContentPane not visible");
					
					super.componentShown(e);
				}
			
			});

		}
		return ImgTraceDialog;
	}

	/**
	 * This method initializes ImgTraceContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getImgTraceContentPane()
	{
		if (ImgTraceContentPane == null)
		{
			MyUtils.trace("[2] ImgTraceContentPane creation");
			ImgTraceContentPane = new JPanel();
			ImgTraceContentPane.setLayout(new BorderLayout());
			ImgTraceContentPane.add(getJPanelImageBg(), BorderLayout.CENTER);
		}
		return ImgTraceContentPane;
	}
	
	/**
	 * This method initializes jPanelImageBg
	 * 
	 * @return vue.JPanelImageBg
	 */
	private JPanelImageBg getJPanelImageBg()
	{
		if (jPanelImageBg == null)
		{
			jPanelImageBg = new JPanelImageBg(vue.getKanji().getStrokeOrderPicture(),
					JPanelImageBg.eImageDisplayMode.CENTRE);
		}
		return jPanelImageBg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vue.VueElement.VueDetaillePanel#getPanel()
	 */
	public JPanel getPanel()
	{
		return this;
	}

}
