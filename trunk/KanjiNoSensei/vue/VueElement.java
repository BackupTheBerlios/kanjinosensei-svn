/**
 * 
 */
package vue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;

import metier.Dictionnaire;
import metier.elements.Element;

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
	
	public abstract Element getElement();

	@SuppressWarnings("unchecked")
	private static Class<? extends VueElement> getVueClassFromElementClass(Class<? extends Element> elementClass)
			throws ClassNotFoundException
	{
		String className = "vue." + elementClass.getSimpleName().toLowerCase() + ".Vue" + elementClass.getSimpleName();
		return (Class<? extends VueElement>) Class.forName(className);
	}

	public static VueElement genererVueElement(KanjiNoSensei app, Element element) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Class<? extends Element> classElement = element.getClass();
		Class<?> classeVue = getVueClassFromElementClass(classElement);
		Constructor<?> constructor = classeVue.getConstructor(KanjiNoSensei.class, classElement);
		VueElement vue = (VueElement) constructor.newInstance(app, element);
		return vue;
	}

	protected VueElement(KanjiNoSensei app)
	{
		VueElement.app = app;
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
	};

	public static class NoAffException extends Exception
	{

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;

		public NoAffException(String typeAff)
		{
			super("Affichage '" + typeAff + "' impossible");
		}
	}

	public static class NoSaisieException extends Exception
	{
		private static final long	serialVersionUID	= 1L;

		public NoSaisieException(String typeSaisie)
		{
			super("Saisie '" + typeSaisie + "' impossible");
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
	public abstract QuizSaisieReponsePanel getQuizSaisieReponsePanel(Dictionnaire dico) throws NoSaisieException;

	/**
	 * Affiche la solution de réponse suivant le type de réponse choisi.
	 * 
	 * @param typeSolution
	 */
	public abstract QuizSolutionPanel getQuizSolutionPanel();

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

}
