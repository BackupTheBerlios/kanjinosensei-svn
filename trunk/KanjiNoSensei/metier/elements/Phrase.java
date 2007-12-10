/**
 * 
 */
package metier.elements;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author Axan
 *
 */
public class Phrase extends Element implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * @param significations
	 * @param themes
	 */
	public Phrase(String significations, String themes)
	{
		super(significations, themes);
		// TODO Auto-generated constructor stub
	}

	/** Mots composants la phrase, dans l'ordre. */
	private Vector<Mot> mots = new Vector<Mot>();
	
	/** Lien vers le son lecture de la phrase. */
	private String son = null;

	/* (non-Javadoc)
	 * @see metier.elements.Element#_export()
	 */
	@Override
	protected String _export()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see metier.elements.Element#_getKey()
	 */
	@Override
	protected String _getKey()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see metier.elements.Element#correspondA(java.lang.String)
	 */
	@Override
	public boolean correspondA(String debut)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
