/**
 * 
 */
package vue;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import metier.Dictionary;
import metier.Messages;
import metier.elements.Element;
import utils.MyUtils;
import utils.OneStringList;

/**
 * Interface des Vues de tous les types d'éléments pouvant entrer dans le
 * dictionnaire.
 * 
 * @author Escallier Pierre
 */
public abstract class VueElement
{

	private static final int								VUE_DETAILLE_HEIGHT	= 100;

	protected static KanjiNoSensei							app					= null;
	
	protected boolean useRomaji = false;
	public abstract Element getElement();

	@SuppressWarnings("unchecked") //$NON-NLS-1$
	private static Class<? extends VueElement> getVueClassFromElementClass(Class<? extends Element> elementClass)
			throws ClassNotFoundException
	{
		String className = "vue." + elementClass.getSimpleName().toLowerCase() + ".Vue" + elementClass.getSimpleName(); //$NON-NLS-1$ //$NON-NLS-2$
		return (Class<? extends VueElement>) Class.forName(className);
	}

	public static VueElement genererVueElement(KanjiNoSensei app, Element element, boolean useRomaji) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Class<? extends Element> classElement = element.getClass();
		Class<?> classeVue = getVueClassFromElementClass(classElement);
		Constructor<?> constructor = classeVue.getConstructor(KanjiNoSensei.class, classElement, boolean.class);
		VueElement vue = (VueElement) constructor.newInstance(app, element, useRomaji);
		return vue;
	}
	
	public static VueElement genererVueBlankElement(KanjiNoSensei app, Class<? extends Element> elementClass, boolean useRomaji) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
	{
		Class<?> classeVue = getVueClassFromElementClass(elementClass);
		Field fieldBlank = elementClass.getDeclaredField("BLANK"); //$NON-NLS-1$
		Element blank = (Element) fieldBlank.get(null);
		Constructor<?> constructor = classeVue.getConstructor(KanjiNoSensei.class, elementClass, boolean.class);
		VueElement vue = (VueElement) constructor.newInstance(app, blank, useRomaji);
		return vue;
	}

	protected VueElement(KanjiNoSensei app, boolean useRomaji)
	{
		VueElement.app = app;
		this.useRomaji = useRomaji;
	}

	public static enum EFormat
	{
		Romaji, Katakana, Hiragana
	}
	public boolean useRomaji()
	{
		return useRomaji;
	}
	
	public String toRomajiIfNeeded(String subject)
	{
		if (useRomaji)
		{
			return MyUtils.kanasToRomaji(subject);
		}
		
		return subject;
	}
	
	public KanjiNoSensei getApp()
	{
		return app;
	}

	/**
	 * Interface de la vue détaillé de tous les types d'éléments.
	 * 
	 * @author Escallier Pierre
	 */
	// public static abstract class VueDetaillePanel extends JPanel
	public static interface VueDetaillePanel
	{
		/**
		 * Méthode apellée pour récupérer l'objet JPanel.
		 */
		public JPanel getPanel();

	};

	/**
	 * Interface de la boite de dialogue d'édition de tout type d'éléménts.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface EditionDialog
	{

		public Element getElementEdite();

		/**
		 * 
		 */
		public boolean afficher();

	};

	/**
	 * Interface du panel de configuration de tout type d'élément.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizConfigPanel
	{
		/**
		 * Récupère l'objet JPanel.
		 * 
		 * @return Objet JPanel formulaire de config de la vue.
		 */
		public JPanel getPanel();

		/**
		 * Valide le formulaire de config.
		 */
		public void valider();
		
		/**
		 * Redéfini le formulaire avec les valeurs en cours.
		 */
		public void resetDisplay();
		
	};

	public static class NoAffException extends Exception
	{

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;

		public NoAffException(String typeAff)
		{
			super(Messages.getString("VueElement.NoAffException") + " : "+ typeAff); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static class NoSaisieException extends Exception
	{
		private static final long	serialVersionUID	= 1L;

		public NoSaisieException(String typeSaisie)
		{
			super(Messages.getString("VueElement.NoSaisieException") + " : "+typeSaisie); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Interface du panel Question de Quiz.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizQuestionPanel
	{
		JPanel getPanel();
	};

	/**
	 * Interface du panel Saisie Réponse de Quiz.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizSaisieReponsePanel
	{
		JPanel getPanel();

		/**
		 * @return
		 */
		boolean getResultat();
	};

	/**
	 * Interface du panel Solution de Quiz.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizSolutionPanel
	{
		JPanel getPanel();
	};

	/**
	 * Retourne la vue détaillée de l'élément.
	 * 
	 * @return La vue détaillée de l'élément.
	 */
	public abstract VueDetaillePanel getVueDetaillePanel();

	/**
	 * Retourne le dialogue d'édition de l'élément.
	 * 
	 * @return Le dialogue d'édition de l'élémént.
	 */
	public abstract EditionDialog getEditionDialog();

	/**
	 * Retourne le panel de configuration de la classe d'élément.
	 * 
	 * @return Le panel de configuration de la classe d'élément.
	 */
	public abstract QuizConfigPanel getQuizConfigPanel();

	/**
	 * Affiche l'élément dans le cadre d'un quiz, suivant le type d'affichage
	 * choisi. Le type d'affichage devrait être parmis ceux proposé sur le panel
	 * de configuration de Quiz de l'élément.
	 * 
	 * @see getQuizConfigPanel
	 * @param typeAffichage
	 *            Type d'affichage souhaité.
	 * @throws NoAffException
	 */
	public abstract QuizQuestionPanel getQuizQuestionPanel() throws NoAffException;

	/**
	 * Affiche la zone de saisie de réponse suivant le type de réponse choisi.
	 * Le type d'affichage devrait être parmis ceux proposé sur le panel de
	 * configuration de Quiz de l'élément.
	 * 
	 * @param typeReponse
	 *            Type d'affichage de saisi réponse souhaité.
	 * @throws NoSaisieException
	 */
	public abstract QuizSaisieReponsePanel getQuizSaisieReponsePanel(Dictionary dico) throws NoSaisieException;

	/**
	 * Affiche la solution de réponse suivant le type de réponse choisi.
	 * 
	 * @param newCopy
	 */
	public abstract QuizSolutionPanel getQuizSolutionPanel(boolean newCopy);

	public Element editerElement()
	{
		EditionDialog editionDialog = getEditionDialog();
		if (editionDialog.afficher())
		{
			return editionDialog.getElementEdite();
		}
		else return null;
	}

	/**
	 * @return
	 */
	public static int getVueDetailleHeight()
	{
		return VUE_DETAILLE_HEIGHT;
	}

	/**
	 * @param component
	 */
	public static void addInputMethodClickWait(String consigne, final VueElement vue, final JComponent component)
	{
		if (consigne == null)
		{
			consigne = Messages.getString("VueElement.ClickWaitDirective"); //$NON-NLS-1$
		}
		
		final KanjiNoSensei app = vue.getApp();
		component.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
				{
					e.consume();
					
					component.removeMouseListener(this);

					JButton jButtonBON = new JButton(Messages.getString("VueElement.ButtonCorrect")); //$NON-NLS-1$
					jButtonBON.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							app.validerReponseQuiz(true, true);
						}

					});

					JButton jButtonMAUVAIS = new JButton(Messages.getString("VueElement.ButtonIncorrect")); //$NON-NLS-1$
					jButtonMAUVAIS.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							app.validerReponseQuiz(false, true);
						}

					});

					JPanel jPanelReponse = vue.getQuizSolutionPanel(true).getPanel();
					JPanel jPanelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER));
					jPanelBtns.add(jButtonBON);
					jPanelBtns.add(jButtonMAUVAIS);
					
					component.add(jPanelReponse, BorderLayout.CENTER);
					component.add(jPanelBtns, BorderLayout.SOUTH);

					component.setVisible(false);
					component.setVisible(true);
				}
				
				super.mouseClicked(e);
			}

		});
		
		JLabel jConsigne = new JLabel(consigne);
		component.add(jConsigne, BorderLayout.NORTH);
	}

	public static void addInputMethodTextField(String consigne, final VueElement vue, final JComponent component, String reponseUnique) throws NoSaisieException
	{
		if (reponseUnique.isEmpty()) throw new NoSaisieException(""); //$NON-NLS-1$
		
		final List<String> reponse = new Vector<String>();
		reponse.add(reponseUnique);
		addInputMethodTextField(consigne, vue, component, reponse, true);
	}
	
	public static void addInputMethodTextField(String consigne, final VueElement vue, final JComponent component, List<String> reponsesMultiples, final boolean saisieReponseComplete) throws NoSaisieException
	{
		if (reponsesMultiples.isEmpty()) throw new NoSaisieException(""); //$NON-NLS-1$
	
		consigne = Messages.getString("VueElement.TextFieldInputDirective") + "("+consigne+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (saisieReponseComplete)
		{
			consigne += "; "+Messages.getString("VueElement.TextFieldInputDirectiveCompleteAnswer"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		final OneStringList reponses = new OneStringList(Element.FIELD_ALLOWED_SEPARATORS);
		reponses.addAll(reponsesMultiples);
		
		final JLabel jConsigne = new JLabel(consigne);
		
		final JTextField jSaisie = new JTextField();
		jSaisie.addActionListener(new ActionListener()
		{
			OneStringList saisie = new OneStringList(Element.FIELD_ALLOWED_SEPARATORS);
			
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				saisie.clear();
				saisie.addFromString(jSaisie.getText());
			
				if (saisie.isEmpty())
				{
					JOptionPane.showMessageDialog(jSaisie, Messages.getString("VueElement.WarningBoxNoInput"), Messages.getString("VueElement.WarningBoxNoInputTitle"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					return;
				}
				
				boolean result = false;
				
				if (saisieReponseComplete)
				{
					// All inputs must be valid answers, and all answers must be covered.
					result = (reponses.containsAll(saisie, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd) && saisie.containsAll(reponses, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd));
				}
				else
				{
					// All inputs must be valid answers
					result = reponses.containsAll(saisie, MyUtils.STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd);
				}
				
				vue.getApp().validerReponseQuiz(result, false);
			}
		
		});
		
		component.add(jConsigne, BorderLayout.NORTH);
		component.add(jSaisie, BorderLayout.CENTER);
		component.setVisible(false);
		component.setVisible(true);
	}
}
