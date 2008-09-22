/**
 * 
 */
package epsofts.KanjiNoSensei.vue;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import epsofts.KanjiNoSensei.RefactoringInfos;
import epsofts.KanjiNoSensei.metier.Messages;
import epsofts.KanjiNoSensei.metier.elements.Element;
import epsofts.KanjiNoSensei.utils.MyUtils;
import epsofts.KanjiNoSensei.utils.OneStringList;
import epsofts.KanjiNoSensei.utils.MyUtils.BadStringFormatException;
import epsofts.KanjiNoSensei.utils.MyUtils.BadTaggedStringFormatException;

/**
 * Interface des Vues de tous les types d'éléments pouvant entrer dans le dictionnaire.
 * 
 * @author Escallier Pierre
 */
public abstract class VueElement
{

	/** Full view height. */
	private static final int	VUE_DETAILLE_HEIGHT	= 100;

	/**
	 * Return the associated element.
	 * 
	 * @return the associated element.
	 */
	public abstract Element getElement();

	/**
	 * Return the VueXXX class associated with the given Element class. The VueXXX class must exist as {@code VueElement.class.getPackage().getName)+"."+elementClass.getSimpleName().toLowerCase()+".Vue"+elementClass.getSimpleName()}.
	 * 
	 * @param elementClass
	 *            Element class we need to know the associated Vue class.
	 * @return the VueXXX class associated with the given Element class.
	 * @throws ClassNotFoundException
	 *             if VueXXX class can't be found.
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	private static Class<? extends VueElement> getVueClassFromElementClass(Class<? extends Element> elementClass) throws ClassNotFoundException
	{
		String className = VueElement.class.getPackage().getName() + "." + elementClass.getSimpleName().toLowerCase() + ".Vue" + elementClass.getSimpleName(); //$NON-NLS-1$ //$NON-NLS-2$
		return (Class<? extends VueElement>) RefactoringInfos.forName(className);
	}

	/**
	 * Generate a VueXXX class for the given Element.
	 * 
	 * @param element
	 *            Element to generate the Vue class.
	 * @param useRomaji
	 *            Specify if Romaji is used instead of Kana.
	 * @return VueXXX object for the given element.
	 * @throws ClassNotFoundException
	 *             If VueXXX class can't be found for this element.
	 * @throws SecurityException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws NoSuchMethodException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws IllegalArgumentException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws InstantiationException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws IllegalAccessException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws InvocationTargetException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 */
	public static VueElement genererVueElement(Element element) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Class<? extends Element> classElement = element.getClass();
		Class<?> classeVue = getVueClassFromElementClass(classElement);
		Constructor<?> constructor = classeVue.getConstructor(classElement);
		VueElement vue = (VueElement) constructor.newInstance(element);
		return vue;
	}

	// 　TODO: C'est moche.. instancier une VueXXX lié à un objet "BLANK".. parcequ'on a pas trouvé de solution statique..
	/**
	 * Used to generate a blank VueXXX object. A blank VueXXX object is associated with the BLANK element of the given elementClass.
	 * 
	 * @param elementClass
	 *            element class for which to generate the VueXXX object.
	 * @param useRomaji
	 *            Specify if Romaji is used instead of Kana.
	 * @return VueXXX object for the BLANK element of the specified Element class.
	 * @throws ClassNotFoundException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws SecurityException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws NoSuchMethodException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws IllegalArgumentException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws InstantiationException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws IllegalAccessException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws InvocationTargetException
	 *             If VueXXX class has no expected constructor to be instanciate.
	 * @throws NoSuchFieldException
	 *             If elementClass does not define public static field {@code static public final Element BLANK = ...}.
	 */
	public static VueElement genererVueBlankElement(Class<? extends Element> elementClass, boolean useRomaji) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
	{
		Class<?> classeVue = getVueClassFromElementClass(elementClass);
		Field fieldBlank = elementClass.getDeclaredField("BLANK"); //$NON-NLS-1$
		ClassLoader.getSystemClassLoader().loadClass(elementClass.getCanonicalName());
		Element blank = (Element) fieldBlank.get(null);
		Constructor<?> constructor = classeVue.getConstructor(elementClass);
		VueElement vue = (VueElement) constructor.newInstance(blank);
		return vue;
	}

	/**
	 * Empty constructor.
	 */
	protected VueElement()
	{
		
	}

	/**
	 * Convert the subject to Romaji if the useRomaji flag is set. Do nothing if useRomaji flag is not set.
	 * 
	 * @param subject
	 *            String to process.
	 * @return Romaji converted subject if useRomaji flag is set, subject if not.
	 */
	public static String toRomajiIfNeeded(String subject)
	{
		if (KanjiNoSensei.USE_ROMAJI)
		{
			return MyUtils.kanaToRomaji(subject);
		}

		return subject;
	}

	/**
	 * Interface that define a complete view of an element.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface VueDetaillePanel
	{
		/**
		 * This method must return the complete view displayable JPanel.
		 * 
		 * @return displayable JPanel.
		 */
		public JPanel getPanel();

	};

	/**
	 * Interface that define edition dialog of an element.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface EditionDialog
	{
		/**
		 * This method must return the last edited element.
		 * 
		 * @return last edited element.
		 */
		public Element getElementEdite();

		/**
		 * Display the edition dialog.
		 * 
		 * @return true if an element was edited, false if not.
		 * @see getElementEdite()
		 */
		public boolean afficher();

	};

	/**
	 * Interface that define the configuration panel of an element type.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizConfigPanel
	{
		/**
		 * Must return the configuration panel.
		 * 
		 * @return the configuration panel to display.
		 */
		public JPanel getPanel();

		/**
		 * This method must be called to validate the configuration panel.
		 */
		public void valider();

		/**
		 * Refresh the panel display with current values.
		 */
		public void resetDisplay();

	};

	/**
	 * Exception thrown when element can't be displayed in specified mode.
	 */
	public static class NoAffException extends Exception
	{

		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

		/**
		 * Constructor which specify the display mode that was not supported.
		 * 
		 * @param typeAff
		 *            Display mode that was not supported.
		 */
		public NoAffException(String typeAff)
		{
			super(Messages.getString("VueElement.NoAffException") + " : " + typeAff); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Exception thrown when element can't use the specified input mode.
	 */
	public static class NoSaisieException extends Exception
	{
		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

		/**
		 * Constructor which specify the non supported input mode.
		 * 
		 * @param typeSaisie
		 *            Non supported input mode.
		 */
		public NoSaisieException(String typeSaisie)
		{
			super(Messages.getString("VueElement.NoSaisieException") + " : " + typeSaisie); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Interface that define an element question panel.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizQuestionPanel
	{
		/**
		 * Must return a displayable question panel.
		 * 
		 * @return a displayable question panel.
		 */
		JPanel getPanel();
	};

	/**
	 * Interface that define an answer input panel.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizSaisieReponsePanel
	{
		/**
		 * Must return a displayable answer input panel.
		 * 
		 * @return a displayable answer input panel.
		 */
		JPanel getPanel();

		/**
		 * Must test if the given answer was correct or not.
		 * 
		 * @return true if the answer was correct, false if not.
		 */
		boolean getResultat();
	};

	/**
	 * Interface that define a solution panel.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface QuizSolutionPanel
	{
		/**
		 * Must return a displayable solution panel.
		 * 
		 * @return a displayable solution panel.
		 */
		JPanel getPanel();
	};

	/**
	 * Return the element complete view.
	 * 
	 * @return the element complete view.
	 */
	public abstract VueDetaillePanel getVueDetaillePanel();

	/**
	 * Return the edition dialog of this element.
	 * 
	 * @return the edition dialog of this element.
	 */
	public abstract EditionDialog getEditionDialog();

	/**
	 * Return the configuration panel of this element class.
	 * 
	 * @return the configuration panel of this element class.
	 */
	public abstract QuizConfigPanel getQuizConfigPanel();

	/**
	 * Return the quizz question panel with the current question display mode (chosen with configuration panel).
	 * 
	 * @see getQuizConfigPanel
	 * @throws NoAffException
	 *             If element can't be displayed in the current question display mode.
	 */
	public abstract QuizQuestionPanel getQuizQuestionPanel() throws NoAffException;

	/**
	 * Return the quizz answer panel with the current answer input mode (chosen with configuration panel).
	 * 
	 * @see getQuizConfigPanel
	 * @throws NoSaisieException
	 *             If element can't be displayed in the current answer input mode.
	 */
	public abstract QuizSaisieReponsePanel getQuizSaisieReponsePanel() throws NoSaisieException;

	/**
	 * SUIS LA Return the quizz solution panel with the current solution display mode (chosen with configuration panel). If current solution display mode can't be rendered, the solution panel must fall back in a supported one. If newCopy is true, the method must return a new copy of the panel.
	 * 
	 * @see getQuizConfigPanel
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
		else
			return null;
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

		final JLabel jConsigne = new JLabel(consigne);
		
		component.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2))
				{
					e.consume();

					component.removeMouseListener(this);
					component.remove(jConsigne);

					JButton jButtonBON = new JButton(Messages.getString("VueElement.ButtonCorrect")); //$NON-NLS-1$
					jButtonBON.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							KanjiNoSensei.getApp().validerReponseQuiz(true, true);
						}

					});

					JButton jButtonMAUVAIS = new JButton(Messages.getString("VueElement.ButtonIncorrect")); //$NON-NLS-1$
					jButtonMAUVAIS.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							KanjiNoSensei.getApp().validerReponseQuiz(false, true);
						}

					});

					JPanel jPanelReponse = vue.getQuizSolutionPanel(true).getPanel();
					JPanel jPanelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER));
					jPanelBtns.add(jButtonBON);
					jPanelBtns.add(jButtonMAUVAIS);

					component.add(jPanelReponse, BorderLayout.CENTER);
					component.add(jPanelBtns, BorderLayout.SOUTH);

					component.doLayout();
					MyUtils.refreshComponent(component);
				}

				super.mouseClicked(e);
			}

		});

		component.add(jConsigne, BorderLayout.NORTH);
		component.doLayout();
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

		consigne = Messages.getString("VueElement.TextFieldInputDirective") + "(" + consigne + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (saisieReponseComplete)
		{
			consigne += "; " + Messages.getString("VueElement.TextFieldInputDirectiveCompleteAnswer"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final OneStringList reponses = new OneStringList(Element.FIELD_ALLOWED_SEPARATORS);
		reponses.addAll(reponsesMultiples);

		final JLabel jConsigne = new JLabel(consigne);

		final JTextField jSaisie = new JTextField();
		jSaisie.addActionListener(new ActionListener()
		{
			OneStringList	saisie	= new OneStringList(Element.FIELD_ALLOWED_SEPARATORS);

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

				KanjiNoSensei.getApp().validerReponseQuiz(result, false);
			}

		});

		component.add(jConsigne, BorderLayout.NORTH);
		component.add(jSaisie, BorderLayout.CENTER);
		component.doLayout();
	}

	public static String computeTemplate(String template, final VueElement vue)
	{
		String tagLabels = "label";
		String tagMethod = "method";
		String tagVueMethod = "vue";
		String content = template;

		final Element element = vue.getElement();
		
		try
		{
			content = MyUtils.parseTaggedString(content, "<" + tagLabels + ">", "</" + tagLabels + ">", new MyUtils.ITaggedStringParseListener()
			{

				@Override
				public String processTag(String tagValue)
				{
					if (tagValue.indexOf("</") >= 0)
					{
						tagValue = tagValue+"";
					}
					return Messages.getString(tagValue);
				}
			});
		}
		catch (BadTaggedStringFormatException e)
		{
			KanjiNoSensei.log(Level.SEVERE, "VueElement.computeTemplate: Error: "+e.getMessage());
			content = e.getParsedString();
		}
		
		try
		{
			content = MyUtils.parseTaggedString(content, "<" + tagMethod + ">", "</" + tagMethod + ">", new MyUtils.ITaggedStringParseListener()
			{

				@Override
				public String processTag(String tagValue) throws Exception
				{
					try
					{
						Method method = element.getClass().getMethod(tagValue);
						if (Modifier.isStatic(method.getModifiers()))
						{
							return method.invoke(null).toString();
						}
						else
						{
							return method.invoke(element).toString();
						}
					}
					catch (Exception e)
					{
						String errMsg = "Can't use method \"" + tagValue + "\" on Element subclass \"" + element.getClass().getName() + "\"";
						throw new Exception(errMsg, e);
					}
				}
			});
		}
		catch (BadTaggedStringFormatException e)
		{
			KanjiNoSensei.log(Level.SEVERE, "VueElement.computeTemplate: Error: "+e.getMessage());
			content = e.getParsedString();
		}
		
		try
		{
			content = MyUtils.parseTaggedString(content, "<" + tagVueMethod + ">", "</" + tagVueMethod + ">", new MyUtils.ITaggedStringParseListener()
			{

				@Override
				public String processTag(String tagValue) throws Exception
				{
					try
					{
						Method method = vue.getClass().getMethod(tagValue);
						if (Modifier.isStatic(method.getModifiers()))
						{
							return method.invoke(null).toString();
						}
						else
						{
							return method.invoke(vue).toString();
						}
					}
					catch (Exception e)
					{
						String errMsg = "Can't use method \"" + tagValue + "\" on VueElement subclass \"" + vue.getClass().getName() + "\"";
						throw new Exception(errMsg, e);
					}
				}
			});
		}
		catch (BadTaggedStringFormatException e)
		{
			KanjiNoSensei.log(Level.SEVERE, "VueElement.computeTemplate: Error: "+e.getMessage());
			content = e.getParsedString();
		}

		return content;
	}
}
