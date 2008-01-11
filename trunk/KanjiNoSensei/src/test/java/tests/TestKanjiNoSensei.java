/**
 * 
 */
package tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import vue.KanjiNoSensei;

/**
 * @author axan
 *
 */
public class TestKanjiNoSensei
{

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link vue.KanjiNoSensei#main(java.lang.String[])}.
	 */
	@Test
	public void testMain()
	{
		String[] args = {"dico/dico.kjd"}; //$NON-NLS-1$
		KanjiNoSensei.main(args);
	}

}
