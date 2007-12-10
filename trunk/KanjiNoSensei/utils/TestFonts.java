/**
 * 
 */
package utils;


public class TestFonts
{
	public static void main(String[] args) throws java.lang.Exception
	{
		String sample = new String(new char[] {(char) 0x3042, (char) 0x3044});// choose
																				// your
																				// own
																				// characters
																				// in
																				// the
																				// array
																				// char[]
		java.awt.Font[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		int q = 0;
		for (int j = 0; j < fonts.length; j++ )
		{
			q = fonts[j].canDisplayUpTo(sample);
			if ((q == -1) || (q == sample.length()))
			{
				System.out.print(fonts[j].getFamily());
				System.out.print(" :    ");
				System.out.println("YES ");
			}
		}// end of for
	}
}
