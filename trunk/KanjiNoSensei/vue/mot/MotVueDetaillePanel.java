package vue.mot;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import metier.elements.Mot;
import vue.JPanelSonBtn;
import vue.VueElement.VueDetaillePanel;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
class MotVueDetaillePanel extends javax.swing.JPanel implements VueDetaillePanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private VueMot vue = null;
	private JPanel jPanelCentre;
	private JToggleButton jButtonJouerSon;
	private JLabel jLabelMot;
	private JLabel jLabelSignifications;
	private JLabel jLabelThemes;
	private JPanel jPanelSouth;
	private JLabel jLabelLecture;
	private JPanel jPanelNorth;
	
	public MotVueDetaillePanel(VueMot vue)
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
				jPanelNorth = new JPanel();
				BorderLayout jPanelNorthLayout = new BorderLayout();
				jPanelNorth.setLayout(jPanelNorthLayout);
				this.add(jPanelNorth, BorderLayout.NORTH);
				jPanelNorth.setSize(600, 20);
				jPanelNorth.setPreferredSize(new java.awt.Dimension(600, 20));
				{
					jLabelLecture = new JLabel();
					jPanelNorth.add(jLabelLecture, BorderLayout.WEST);
					jLabelLecture.setText("Lecture : neko");
				}
				{
					jButtonJouerSon = new JPanelSonBtn(vue.getMot().getSon(), false);
					jPanelNorth.add(jButtonJouerSon, BorderLayout.EAST);
					jButtonJouerSon.setText("Lire");
					jButtonJouerSon.setSize(35, 20);
				}
			}
			{
				jPanelSouth = new JPanel();
				GridLayout jPanelSouthLayout = new GridLayout(2, 1);
				jPanelSouthLayout.setHgap(5);
				jPanelSouthLayout.setVgap(5);
				jPanelSouthLayout.setColumns(1);
				jPanelSouthLayout.setRows(2);
				this.add(jPanelSouth, BorderLayout.SOUTH);
				jPanelSouth.setLayout(jPanelSouthLayout);
				jPanelSouth.setPreferredSize(new java.awt.Dimension(600, 28));
				{
					jLabelSignifications = new JLabel();
					jPanelSouth.add(jLabelSignifications);
					jLabelSignifications.setText("Significations : chat, ...");
				}
				{
					jLabelThemes = new JLabel();
					jPanelSouth.add(jLabelThemes);
					jLabelThemes.setText("Thèmes : mot, patin, coufin");
				}
			}
			{
				jPanelCentre = new JPanel();
				BorderLayout jPanelCentreLayout = new BorderLayout();
				this.add(jPanelCentre, BorderLayout.CENTER);
				jPanelCentre.setLayout(jPanelCentreLayout);
				jPanelCentre.setMinimumSize(new java.awt.Dimension(600, 45));
				jPanelCentre.setSize(600, 45);
				jPanelCentre.setPreferredSize(new java.awt.Dimension(10, 45));
				{
					jLabelMot = new JLabel();
					jPanelCentre.add(jLabelMot, BorderLayout.CENTER);
					jLabelMot.setText("\u7f8e\u5c11\u5973");
					jLabelMot.setFont(new java.awt.Font("Kochi Mincho",Font.PLAIN,44));
					jLabelMot.setHorizontalTextPosition(SwingConstants.CENTER);
					jLabelMot.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//<NoJigloo>
	private void dynamicInitialize()
	{
		Mot mot = vue.getMot();
		jLabelLecture.setText("Lecture : "+mot.getLecture());
		jLabelMot.setText(mot.getMot());
		jLabelSignifications.setText("Significations : "+mot.getSignifications());
		jLabelThemes.setText("Thèmes : "+mot.getThemes());
	}
	//</NoJigloo>

	/* (non-Javadoc)
	 * @see vue.VueElement.VueDetaillePanel#getPanel()
	 */
	@Override
	public JPanel getPanel()
	{
		return this;
	}

}
