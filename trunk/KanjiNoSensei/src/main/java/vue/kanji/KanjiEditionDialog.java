/**
 * 
 */
package vue.kanji;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import metier.elements.Element;
import metier.elements.Kanji;
import utils.MyUtils;
import vue.VueElement.EditionDialog;


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
/**
 * @author Axan
 * 
 */
class KanjiEditionDialog extends JDialog implements EditionDialog
{

	static final private String[] extFilterTrace = {"png", "jpg", "bmp"};
	static final private FileFilter fileFilterTrace = MyUtils.generateFileFilter("Image de tracé du 漢字", extFilterTrace);
	
	private static final long	serialVersionUID	= 1L;

	private JPanel ajouterKanjiContentPane = null;
	private JPanel ajouterKanjiFormPanel = null;

	private JLabel jLabelThemes = null;
	private JLabel	jLabelCodeUTF8 = null;
	private JLabel	jLabelLecturesChinoises = null;
	private JLabel	jLabelOrdreTraits = null;
	private JLabel	jLabelSignifications = null;
	private JLabel	jLabelLecturesJaponaises = null;
	private JTextField	jTextFieldCodeUTF8 = null;
	private JTextField	jTextFieldTrace = null;
	private JButton	jButtonTraceParcourir = null;
	private JTextField	jTextFieldLecturesChinoises = null;
	private JTextField	jTextFieldLecturesJaponaises = null;
	private JTextField	jTextFieldSignifications = null;
	private JTextField	jTextFieldThemes = null;
	private JButton	jButtonCANCEL = null;
	private JButton	jButtonOK = null;
	private JPanel	jPanelBtns = null;
	
	private VueKanji vue = null;
	
	private KanjiEditionDialog jKanjiEditionDialog = this;
	
	private boolean kanjiEdite = false;
	
	/**
	 * Constructeur
	 * 
	 */
	public KanjiEditionDialog(VueKanji vue)
	{
		super();
		this.vue = vue;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this.setMinimumSize(new Dimension(430, 200));
        this.setPreferredSize(new Dimension(430, 200));
        this.setResizable(true);
        this.setTitle("Edition \u6f22\u5b57");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getAjouterKanjiContentPane());
        this.setModal(true);
        
        Kanji kanji = vue.getKanji();
        if (kanji != null)
        {
        	this.jTextFieldCodeUTF8.setText(kanji.getCodeUTF8().toString());
        	this.jTextFieldLecturesChinoises.setText(kanji.getLecturesON());
        	this.jTextFieldLecturesJaponaises.setText(kanji.getLecturesKUN());
        	this.jTextFieldSignifications.setText(kanji.getSignifications());
        	this.jTextFieldThemes.setText(kanji.getThemes());
        	this.jTextFieldTrace.setText(kanji.getStrokeOrderPicture());
        }
	}

	/**
	 * This method initializes ajouterKanjiContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAjouterKanjiContentPane()
	{
		if (ajouterKanjiContentPane == null)
		{
			ajouterKanjiContentPane = new JPanel();
			ajouterKanjiContentPane.setLayout(new BorderLayout());
			ajouterKanjiContentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			ajouterKanjiContentPane.add(getAjouterKanjiFormPanel(), BorderLayout.CENTER);
			ajouterKanjiContentPane.add(getJPanelBtns(), java.awt.BorderLayout.SOUTH);
		}
		return ajouterKanjiContentPane;
	}

	/**
	 * This method initializes ajouterKanjiFormPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAjouterKanjiFormPanel()
	{
		if (ajouterKanjiFormPanel == null)
		{
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.gridy = 5;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 5;
			gridBagConstraints13.gridx = 0;
			jLabelThemes = new JLabel();
			jLabelThemes.setName("jLabelThemes");
			jLabelThemes.setText("Thèmes :");
			jLabelThemes.setPreferredSize(new Dimension(100, 20));
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 4;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridy = 4;
			gridBagConstraints11.gridx = 0;
			jLabelSignifications = new JLabel();
			jLabelSignifications.setName("jLabelSignifications");
			jLabelSignifications.setText("Significations :");
			jLabelSignifications.setPreferredSize(new Dimension(100, 20));
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 3;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.gridx = 0;
			jLabelLecturesJaponaises = new JLabel();
			jLabelLecturesJaponaises.setName("jLabelLecturesJaponaises");
			jLabelLecturesJaponaises.setText("Lectures Japonaises :");
			jLabelLecturesJaponaises.setPreferredSize(new Dimension(130, 20));
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.gridx = 0;
			jLabelLecturesChinoises = new JLabel();
			jLabelLecturesChinoises.setName("jLabelLecturesChinoises");
			jLabelLecturesChinoises.setText("Lectures Chinoises :");
			jLabelLecturesChinoises.setPreferredSize(new Dimension(130, 20));
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.gridx = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.fill = GridBagConstraints.NONE;
			jLabelOrdreTraits = new JLabel();
			jLabelOrdreTraits.setName("jLabelOrdreTraits");
			jLabelOrdreTraits.setText("Tracé :");
			jLabelOrdreTraits.setPreferredSize(new Dimension(100, 20));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(0, 0, 0, 20);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			jLabelCodeUTF8  = new JLabel();
			jLabelCodeUTF8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jLabelCodeUTF8.setPreferredSize(new Dimension(100, 20));
			jLabelCodeUTF8.setHorizontalAlignment(SwingConstants.LEADING);
			jLabelCodeUTF8.setText("Code UTF8 :");
			jLabelCodeUTF8.setName("jLabelCodeUTF8");
			ajouterKanjiFormPanel = new JPanel();
			ajouterKanjiFormPanel.setLayout(new GridBagLayout());
			ajouterKanjiFormPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			ajouterKanjiFormPanel.setPreferredSize(new Dimension(430, 200));
			ajouterKanjiFormPanel.add(jLabelCodeUTF8, gridBagConstraints);
			ajouterKanjiFormPanel.add(getJTextFieldCodeUTF8(), gridBagConstraints1);
			ajouterKanjiFormPanel.add(jLabelOrdreTraits, gridBagConstraints2);
			ajouterKanjiFormPanel.add(getJTextFieldTrace(), gridBagConstraints3);
			ajouterKanjiFormPanel.add(getJButtonTraceParcourir(), gridBagConstraints4);
			ajouterKanjiFormPanel.add(jLabelLecturesChinoises, gridBagConstraints5);
			ajouterKanjiFormPanel.add(getJTextFieldLecturesChinoises(), gridBagConstraints6);
			ajouterKanjiFormPanel.add(jLabelLecturesJaponaises, gridBagConstraints7);
			ajouterKanjiFormPanel.add(getJTextFieldLecturesJaponaises(), gridBagConstraints8);
			ajouterKanjiFormPanel.add(jLabelSignifications, gridBagConstraints11);
			ajouterKanjiFormPanel.add(getJTextFieldSignifications(), gridBagConstraints12);
			ajouterKanjiFormPanel.add(jLabelThemes, gridBagConstraints13);
			ajouterKanjiFormPanel.add(getJTextFieldThemes(), gridBagConstraints14);
		}
		return ajouterKanjiFormPanel;
	}

	/**
	 * This method initializes jTextFieldCodeUTF8
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldCodeUTF8()
	{
		if (jTextFieldCodeUTF8 == null)
		{
			jTextFieldCodeUTF8 = new JTextField();
			jTextFieldCodeUTF8.setName("jTextFieldCodeUTF8");
			jTextFieldCodeUTF8.setColumns(0);
			jTextFieldCodeUTF8.setPreferredSize(new Dimension(50, 20));
		}
		return jTextFieldCodeUTF8;
	}
	
	/**
	 * This method initializes jTextFieldTrace
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldTrace()
	{
		if (jTextFieldTrace == null)
		{
			jTextFieldTrace = new JTextField();
			jTextFieldTrace.setName("jTextFieldTrace");
			jTextFieldTrace.setEditable(false);
			jTextFieldTrace.setPreferredSize(new Dimension(150, 20));
		}
		return jTextFieldTrace;
	}

	/**
	 * This method initializes jButtonTraceParcourir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonTraceParcourir()
	{
		if (jButtonTraceParcourir == null)
		{
			jButtonTraceParcourir = new JButton();
			jButtonTraceParcourir.setName("jButtonTraceParcourir");
			jButtonTraceParcourir.setText("Parcourir...");
			jButtonTraceParcourir.setPreferredSize(new java.awt.Dimension(100, 20));
			
			jButtonTraceParcourir.addActionListener(new ActionListener()
			{
			
				public void actionPerformed(ActionEvent e)
				{
					JFileChooser fc = new JFileChooser();

					fc.setFileFilter(fileFilterTrace);
					if (fc.showOpenDialog(vue.getApp().getJFrame()) == JFileChooser.APPROVE_OPTION)
					{
						File fic = fc.getSelectedFile();
						jTextFieldTrace.setText(fic.getAbsolutePath());
					}			
				}
			
			});
		}
		return jButtonTraceParcourir;
	}

	/**
	 * This method initializes jTextFieldLecturesChinoises
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldLecturesChinoises()
	{
		if (jTextFieldLecturesChinoises == null)
		{
			jTextFieldLecturesChinoises = new JTextField();
			jTextFieldLecturesChinoises.setName("jTextFieldLecturesChinoises");
		}
		return jTextFieldLecturesChinoises;
	}

	/**
	 * This method initializes jTextFieldLecturesJaponaises
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldLecturesJaponaises()
	{
		if (jTextFieldLecturesJaponaises == null)
		{
			jTextFieldLecturesJaponaises = new JTextField();
			jTextFieldLecturesJaponaises.setName("jTextFieldLecturesJaponaises");
		}
		return jTextFieldLecturesJaponaises;
	}

	/**
	 * This method initializes jTextFieldSignifications
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSignifications()
	{
		if (jTextFieldSignifications == null)
		{
			jTextFieldSignifications = new JTextField();
			jTextFieldSignifications.setName("jTextFieldSignifications");
		}
		return jTextFieldSignifications;
	}

	/**
	 * This method initializes jTextFieldThemes
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldThemes()
	{
		if (jTextFieldThemes == null)
		{
			jTextFieldThemes = new JTextField();
			jTextFieldThemes.setName("jTextFieldThemes");
		}
		return jTextFieldThemes;
	}
	
	/**
	 * This method initializes jPanelBtns
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelBtns()
	{
		if (jPanelBtns == null)
		{
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(0, 0, 0, 5);
			jPanelBtns = new JPanel();
			jPanelBtns.setLayout(new GridBagLayout());
			jPanelBtns.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jPanelBtns.add(getJButtonOK(), gridBagConstraints9);
			jPanelBtns.add(getJButtonCANCEL(), gridBagConstraints10);
		}
		return jPanelBtns;
	}

	/**
	 * This method initializes jButtonOK
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK()
	{
		if (jButtonOK == null)
		{
			jButtonOK = new JButton();
			jButtonOK.setPreferredSize(new Dimension(80, 20));
			jButtonOK.setText("Valider");
			jButtonOK.setHorizontalTextPosition(SwingConstants.CENTER);
			
			jButtonOK.addActionListener(new ActionListener()
			{
			
				public void actionPerformed(ActionEvent e)
				{
					vue.setKanji(new Kanji(jTextFieldCodeUTF8.getText().charAt(0), jTextFieldTrace.getText(), jTextFieldLecturesChinoises.getText(), jTextFieldLecturesJaponaises.getText(), jTextFieldSignifications.getText(), jTextFieldThemes.getText()));
					kanjiEdite = true;
					jKanjiEditionDialog.dispose();
				}
			
			});
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCANCEL
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCANCEL()
	{
		if (jButtonCANCEL == null)
		{
			jButtonCANCEL = new JButton();
			jButtonCANCEL.setPreferredSize(new Dimension(80, 20));
			jButtonCANCEL.setText("Annuler");
			
			jButtonCANCEL.addActionListener(new ActionListener()
			{
			
				public void actionPerformed(ActionEvent e)
				{
					jKanjiEditionDialog.dispose();
				}
			
			});
		}
		return jButtonCANCEL;
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.EditionDialog#getElementEdite()
	 */
	public Element getElementEdite()
	{
		return vue.getKanji();
	}

	/* (non-Javadoc)
	 * @see vue.VueElement.EditionDialog#afficher()
	 */
	public synchronized boolean afficher()
	{
		kanjiEdite = false;
		pack();
		setVisible(true);
		return kanjiEdite;
	}
}
