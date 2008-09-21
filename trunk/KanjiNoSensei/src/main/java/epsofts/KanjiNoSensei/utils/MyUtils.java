package epsofts.KanjiNoSensei.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;

import epsofts.KanjiNoSensei.vue.KanjiNoSensei;

/**
 * Utilities class, provide lot of functions.
 * 
 * @author Escallier Pierre
 */
public abstract class MyUtils
{
	/**
	 * Trace Logger.
	 * 
	 * @see MyUtils#trace(Level, String)
	 */
	static private final Logger	log			= Logger.getLogger(MyUtils.class.getName());

	static public Level			logMinLevel	= Level.ALL;

	static public class BadStringFormatException extends Exception
	{
		/** Serialization version. */
		private static final long	serialVersionUID	= 1L;

		/**
		 * Message constructor.
		 * 
		 * @param msg
		 *            Message.
		 */
		public BadStringFormatException(String msg)
		{
			super(msg);
		}
	}

	/**
	 * Test the system fonts and report which ones are able to display testing character (unicode). Use System I/O.
	 */
	static public void testFonts()
	{
		String sample = new String(new char[] {(char) 0x3042, (char) 0x3044}); // May
		// change
		// the
		// testing
		// character.

		java.awt.Font[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		int q = 0;
		for (int j = 0; j < fonts.length; j++ )
		{
			q = fonts[j].canDisplayUpTo(sample);
			if ((q == -1) || (q == sample.length()))
			{
				System.out.print(fonts[j].getFamily());
				System.out.print(" :    "); //$NON-NLS-1$
				System.out.println("YES "); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Generate a file filter from given parameters.
	 * 
	 * @param description
	 *            Description of the file filter.
	 * @param extension
	 *            Extension of the file filter.
	 * @return generated FileFilter.
	 * @see MyUtils#generateFileFilter(String, String[])
	 */
	static public FileFilter generateFileFilter(String description, String extension)
	{
		return generateFileFilter(description, new String[] {extension});
	}

	/**
	 * Generate a file filter from given parameters.
	 * 
	 * @param description
	 *            Description of the file filter.
	 * @param extensions
	 *            Extensions allowed by the file filter.
	 * @return generated FileFilter.
	 */
	static public FileFilter generateFileFilter(final String description, final String[] extensions)
	{
		final StringBuffer sbdesc = new StringBuffer(description);
		sbdesc.append("("); //$NON-NLS-1$
		for (String ext : extensions)
		{
			sbdesc.append("*." + ext + "; "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sbdesc.append(")"); //$NON-NLS-1$

		return new FileFilter()
		{

			@Override
			public String getDescription()
			{
				return sbdesc.toString();
			}

			@Override
			public boolean accept(File f)
			{
				if (f.isDirectory()) return true;

				String ext = getExtension(f);
				if (ext == null) return false;

				for (String s : extensions)
				{
					if (ext.compareToIgnoreCase(s) == 0)
					{
						return true;
					}
				}

				return false;

			}

		};
	}

	/**
	 * Return the extension of a file.
	 * 
	 * @param file
	 *            File.
	 * @return extension of the file.
	 */
	public static String getExtension(File file)
	{
		return getExtension(file.getName());
	}

	/**
	 * Return the extension of a filename.
	 * 
	 * @param fileName
	 *            File name.
	 * @return extenstion of the filename.
	 */
	public static String getExtension(String fileName)
	{
		String ext = null;

		if ((fileName == null) || (fileName.isEmpty())) return null;

		int i = fileName.lastIndexOf('.');

		if (i > 0 && i < fileName.length() - 1)
		{
			ext = fileName.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * Convert array E[] to a Vector&lt;E&gt;.
	 * 
	 * @param <E>
	 *            Type of the array objects (implicit).
	 * @param objs
	 *            Array of E objects.
	 * @return Vector&lt;E&gt; of the given array.
	 */
	public static <E> Vector<E> arrayToVector(E[] objs)
	{
		Vector<E> v = new Vector<E>(objs.length);
		for (E e : objs)
		{
			v.add(e);
		}

		return v;
	}

	/**
	 * Lock a component and all its sub components by setEnable(false).
	 * 
	 * @param c
	 *            Parent component to lock.
	 */
	public static void lockPanel(Component c)
	{
		trace(Level.FINEST, "lockPanel : " + c.toString()); //$NON-NLS-1$
		c.setEnabled(false);

		if (Container.class.isInstance(c))
		{
			for (Component sc : ((Container) c).getComponents())
			{
				lockPanel(sc);
			}
		}
	}

	/**
	 * Remove empty strings from a string set.
	 * 
	 * @param set
	 *            Set of string to clean.
	 */
	public static void removeEmptyElements(Iterable<String> iterable)
	{
		Iterator<String> it = iterable.iterator();
		while (it.hasNext())
		{
			String s = it.next();
			if (s == null)
			{
				it.remove();
			}
			else
			{
				s = s.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
				if (s.isEmpty()) it.remove();
			}
		}
	}

	/**
	 * Join a string array with specified glue.
	 * 
	 * @param elements
	 *            String array to join.
	 * @param glue
	 *            Glue string to add between each elements.
	 * @return One string list joined by glue string.
	 */
	public static String joinStringElements(String[] elements, String glue)
	{
		StringBuffer sb = new StringBuffer();
		boolean first = true;

		for (String s : elements)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(glue);
			}
			sb.append(s);
		}

		return sb.toString();
	}

	/**
	 * Offset the elements of a T array. Such as :<br>
	 * <code>offsetObjectElemenelements(elements, offset)[0] == elements[offset]</code><br>
	 * and<br>
	 * <code>offsetObjectElemenelements(elements, offset).lentgh == (elements.lengh - offset)</code>
	 * 
	 * @param <T>
	 *            Type parameter (implicit)
	 * @param elements
	 *            Array of T to offset.
	 * @param offset
	 *            number of elements to offset.
	 * @return
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public static <T> T[] offsetObjectElements(T[] elements, int offset)
	{
		if (elements.length <= 0) return elements;

		Vector<T> restants = new Vector<T>();

		for (int i = offset; i < elements.length; ++i)
		{
			restants.add(elements[i]);
		}

		T[] array = (T[]) Array.newInstance(elements[0].getClass(), restants.size());

		return restants.toArray(array);
	}

	/**
	 * Return the string without begenning and ending quotes : "
	 * 
	 * @param string
	 *            String to unquote.
	 * @return Unquoted string.
	 */
	public static String stripQuotes(String string)
	{
		if ((string.startsWith("\"")) && (string.endsWith("\""))) //$NON-NLS-1$ //$NON-NLS-2$
		{
			return string.substring(1, string.length() - 1);
		}

		return string;
	}

	/** Romaji standard table. */
	private static final String[]	tRomaji				= {"a", "i", "u", "e", "o", "ka", "ki", "ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta", "chi", "tsu", "te", "to", "na", "ni", "nu", "ne", "no", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$
		"ha", "hi", "fu", "he", "ho", "ma", "mi", "mu", "me", "mo", "ya", "yu", "yo", "ra", "ri", "ru", "re", "ro", "wa", "wo", "n", "ga", "gi", "gu", "ge", "go", "za", "ji", "zu", "ze", "zo", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$
		"da", "ji", "zu", "de", "do", "ba", "bi", "bu", "be", "bo", "pa", "pi", "pu", "pe", "po"};												//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$

	/** Romaji composed characters table. */
	private static final String[]	tRomaYaYuYo			= {"kya", "kyu", "kyo", "sha", "shu", "sho", "cha", "chu", "cho", "nya", "nyu", "nyo", "hya", "hyu", "hyo", "mya", "myu", "myo", "rya", "ryu", "ryo", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$
		"gya", "gyu", "gyo", "ja", "ju", "jo", "bya", "byu", "byo", "pya", "pyu", "pyo"};														//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

	/** Hiragana standard table. */
	private static final String[]	tHiragana			= {"あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$
		"ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ", "を", "ん", "が", "ぎ", "ぐ", "げ", "ご", "ざ", "じ", "ず", "ぜ", "ぞ", "だ", "ぢ", "づ", "で", "ど", "ば", "び", "ぶ", "べ", "ぼ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$ //$NON-NLS-32$ //$NON-NLS-33$ //$NON-NLS-34$ //$NON-NLS-35$ //$NON-NLS-36$ //$NON-NLS-37$
		"ぱ", "ぴ", "ぷ", "ぺ", "ぽ"							};																						//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** Hiragana composed characters table. */
	private static final String[]	tHiraYaYuYo			= {"きゃ", "きゅ", "きょ", "しゃ", "しゅ", "しょ", "ちゃ", "ちゅ", "ちょ", "にゃ", "にゅ", "にょ", "ひゃ", "ひゅ", "ひょ", "みゃ", "みゅ", "みょ", "りゃ", "りゅ", "りょ", "ぎゃ", "ぎゅ", "ぎょ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$
		"じゃ", "じゅ", "じょ", "びゃ", "びゅ", "びょ", "ぴゃ", "ぴゅ", "ぴょ"};																					//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

	/** Hiragana little TSU character. */
	private static final String		hiraTsu				= "っ";																					//$NON-NLS-1$

	/** Katakana standard table. */
	private static final String[]	tKatakana			= {"ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト", "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$
		"ホ", "マ", "ミ", "ム", "メ", "モ", "ヤ", "ユ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ワ", "ヲ", "ン", "ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ", "ダ", "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$ //$NON-NLS-32$ //$NON-NLS-33$ //$NON-NLS-34$ //$NON-NLS-35$ //$NON-NLS-36$ //$NON-NLS-37$
		"パ", "ピ", "プ", "ペ", "ポ"							};																						//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** Katakana composed characters table. */
	private static final String[]	tKataYaYuYo			= {"キャ", "キュ", "キョ", "シャ", "シュ", "ショ", "チャ", "チュ", "チョ", "ニャ", "ニュ", "ニョ", "ヒャ", "ヒュ", "ヒョ", "ミャ", "ミュ", "ミョ", "リャ", "リュ", "リョ", "ギャ", "ギュ", "ギョ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$
		"ジャ", "ジュ", "ジョ", "ビャ", "ビュ", "ビョ", "ピャ", "ピュ", "ピョ"};																					//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

	/** Katakana little TSU character. */
	private static final String		kataTsu				= "ッ";																					//$NON-NLS-1$

	/** Kanas punctuation. */
	protected static final String[]	tKanaPunc			= {"　", "、", "。", "？", "！", "；", "−", "ー", "（", "）", "「", "」", "｛", "｝"};				//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

	/** Kanas braces. */
	protected static final String[]	tKanaBraces			= {"（", "）", "「", "」", "｛", "｝"};

	/** Romaji punctuation. */
	protected static final String[]	tRomaPunc			= {" ", ",", ".", "?", "!", ";", "-", "-", "(", ")", "[", "]", "{", "}"};				//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

	/** Romaji braces. */
	protected static final String[]	tRomaBraces			= {"(", ")", "[", "]", "{", "}"};

	/** Kana starting braces. */
	protected static final String[]	tKanaStartingBraces	= (String[]) Array.newInstance(String.class, tKanaBraces.length / 2);

	/** Romaji starting braces. */
	protected static final String[]	tRomaStartingBraces	= (String[]) Array.newInstance(String.class, tRomaBraces.length / 2);

	/** Kana ending braces. */
	protected static final String[]	tKanaEndingBraces	= (String[]) Array.newInstance(String.class, tKanaBraces.length / 2);

	/** Romaji ending braces. */
	protected static final String[]	tRomaEndingBraces	= (String[]) Array.newInstance(String.class, tRomaBraces.length / 2);

	/** All braces. */
	protected static final String[]	braces				= (String[]) Array.newInstance(String.class, tKanaBraces.length + tRomaBraces.length);

	/** All starting braces. */
	protected static final String[]	startingBraces		= (String[]) Array.newInstance(String.class, braces.length / 2);

	/** All ending braces. */
	protected static final String[]	endingBraces		= (String[]) Array.newInstance(String.class, braces.length / 2);

	static
	{
		for (int i = 0; i < tKanaBraces.length; i += 2)
		{
			tKanaStartingBraces[i / 2] = tKanaBraces[i];
			tKanaEndingBraces[i / 2] = tKanaBraces[i + 1];
		}

		for (int i = 0; i < tRomaBraces.length; i += 2)
		{
			tRomaStartingBraces[i / 2] = tRomaBraces[i];
			tRomaEndingBraces[i / 2] = tRomaBraces[i + 1];
		}

		int j;
		for (j = 0; j < tKanaBraces.length; ++j)
		{
			braces[j] = tKanaBraces[j];
		}

		for (int i = 0; i < tRomaBraces.length; ++i)
		{
			braces[i + j] = tRomaBraces[i];
		}

		for (int i = 0; i < braces.length; i += 2)
		{
			startingBraces[i / 2] = braces[i];
			endingBraces[i / 2] = braces[i + 1];
		}
	}

	/**
	 * Private method used to convert romaji to kana. This method is not perfect, because it uses algorithms to replace characters, instead of a real knowledge of the language.
	 * 
	 * @param subject
	 *            String to convert.
	 * @param tKanaYaYuYo
	 *            Kana composed characters table to use.
	 * @param tKana
	 *            Kana standard table to use.
	 * @param tsu
	 *            Kana little TSU to use.
	 * @return converted string.
	 */
	private static String romajiToKana(String subject, final String[] tKanaYaYuYo, final String[] tKana, final String tsu)
	{
		String result = subject.toLowerCase();
		Comparator<String> descendingComparator = new Comparator<String>()
		{

			@Override
			public int compare(String o1, String o2)
			{
				if (o1.length() != o2.length()) return (o2.length() - o1.length());
				return o2.compareTo(o1);
			}

		};

		TreeMap<String, String> yaYuYo = new TreeMap<String, String>(descendingComparator);

		for (int i = 0; i < tRomaYaYuYo.length; ++i)
		{
			yaYuYo.put(tRomaYaYuYo[i], tKanaYaYuYo[i]);
		}

		TreeMap<String, String> romaji = new TreeMap<String, String>(descendingComparator);

		for (int i = 0; i < tRomaji.length; ++i)
		{
			romaji.put(tRomaji[i], tKana[i]);
		}

		Iterator<String> itYaYuYo = yaYuYo.keySet().iterator();
		while (itYaYuYo.hasNext())
		{
			String key = itYaYuYo.next();
			result = result.replace(key, yaYuYo.get(key));
		}

		Iterator<String> itRomaji = romaji.keySet().iterator();
		while (itRomaji.hasNext())
		{
			String key = itRomaji.next();
			result = result.replace(key, romaji.get(key));
		}

		result = result.replaceAll("[a-zA-Z]", tsu); //$NON-NLS-1$

		result = replaceAll(result, tRomaPunc, tKanaPunc);

		return result;
	}

	/**
	 * Convert romaji string to hiragana.
	 * 
	 * @param romaji
	 *            Romaji string to convert.
	 * @return converted string.
	 */
	public static String romajiToHiragana(String romaji)
	{
		return romajiToKana(romaji, tHiraYaYuYo, tHiragana, hiraTsu);
	}

	/**
	 * Convert romaji string to katakana.
	 * 
	 * @param romaji
	 * @return converted string.
	 */
	public static String romajiToKatakana(String romaji)
	{
		return romajiToKana(romaji, tKataYaYuYo, tKatakana, kataTsu);
	}

	/**
	 * Private method used to convert kana to romaji. This method is not perfect, because it uses algorithms to replace characters, instead of a real knowledge of the language.
	 * 
	 * @param subject
	 *            Kana string to convert.
	 * @param tKanaYaYuYo
	 *            Kana composed table to use.
	 * @param tKana
	 *            Kana table to use.
	 * @param tsu
	 *            Kana little TSU to use.
	 * @return converted string.
	 */
	private static String kanaToRomaji(String subject, final String[] tKanaYaYuYo, final String[] tKana, final String tsu)
	{

		String result = subject;
		result = replaceAll(result, tKanaYaYuYo, tRomaYaYuYo);
		result = replaceAll(result, tKana, tRomaji);

		int indexTsu = result.indexOf(tsu), indexTsuPlus1, indexTsuPlus2;
		while (indexTsu >= 0)
		{
			indexTsuPlus1 = Math.min(result.length(), indexTsu + 1);
			indexTsuPlus2 = Math.min(result.length(), indexTsu + 2);

			result = result.substring(0, indexTsu) + result.substring(indexTsuPlus1, indexTsuPlus2) + result.substring(indexTsuPlus1);
			indexTsu = result.indexOf(tsu);
		}

		result = replaceAll(result, tKanaPunc, tRomaPunc);

		return result;
	}

	/**
	 * Convert hiragana string to romaji.
	 * 
	 * @param hiragana
	 *            Hiragana string to convert.
	 * @return romaji converted string.
	 */
	public static String hiraganaToRomaji(String hiragana)
	{
		return kanaToRomaji(hiragana, tHiraYaYuYo, tHiragana, hiraTsu);
	}

	/**
	 * Convert katakana string to romaji.
	 * 
	 * @param katakana
	 *            Katakana string to convert.
	 * @return romaji converted string.
	 */
	public static String katakanaToRomaji(String katakana)
	{
		return kanaToRomaji(katakana, tKataYaYuYo, tKatakana, kataTsu);
	}

	/**
	 * Convert kana string to romaji, whatever the kana type.
	 * 
	 * @param kana
	 *            Kana string to convert.
	 * @return romaji converted string.
	 */
	public static String kanaToRomaji(String kana)
	{
		return katakanaToRomaji(hiraganaToRomaji(kana));
	}

	/** List of all available Look&Feels. */
	final static HashMap<String, String>	vLFclass	= new HashMap<String, String>();

	/**
	 * List available Look&Feels.
	 * 
	 * @return HashMap of available Look&Feels, key is the look&feels name, value is the look&feels class name.
	 */
	public static HashMap<String, String> listLookAndFeels()
	{
		vLFclass.clear();

		if (UIManager.getAuxiliaryLookAndFeels() != null) for (LookAndFeel lf : UIManager.getAuxiliaryLookAndFeels())
		{
			vLFclass.put(lf.getName(), lf.getID());
		}

		if (UIManager.getInstalledLookAndFeels() != null) for (LookAndFeelInfo lfi : UIManager.getInstalledLookAndFeels())
		{
			vLFclass.put(lfi.getName(), lfi.getClassName());
		}

		vLFclass.put(UIManager.getCrossPlatformLookAndFeelClassName(), UIManager.getCrossPlatformLookAndFeelClassName());
		vLFclass.put(UIManager.getSystemLookAndFeelClassName(), UIManager.getSystemLookAndFeelClassName());

		return vLFclass;
	}

	/**
	 * Check if the current JRE version match the needed one (greater or equal).
	 * 
	 * @param requiredVersion
	 *            Required JRE version, ex: "1.6"
	 * @return true if the current JRE version is >= the required one, false if not.
	 */
	public static boolean checkJREVersion(String requiredVersion)
	{
		String[] versionReq = requiredVersion.split("\\."); //$NON-NLS-1$
		String[] version = System.getProperty("java.runtime.version").split("\\."); //$NON-NLS-1$ //$NON-NLS-2$

		for (int i = 0; i < versionReq.length; ++i)
		{
			if (versionReq[i].compareTo(version[i]) == 0) continue;

			int req = Integer.valueOf(versionReq[i]);
			int cur;

			try
			{
				cur = Integer.valueOf(version[i]);
			}
			catch (Exception e)
			{
				cur = 0;
			}

			return (cur > req);
		}

		return true;
	}

	/**
	 * Sub application to manage look&feels, it support severals commands :<br>
	 * list : List the available look&feels.<br>
	 * system : Try to use the system look&feels.<br>
	 * default : Try to use the default (cross platform) look&feels.<br>
	 * a look&feels name : Try to use the look&feels name in the look&feels list.<br>
	 * 
	 * @param arg
	 *            Argument of the application.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 */
	public static void manageLookAndFeelsOption(String arg) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		String lf = UIManager.getSystemLookAndFeelClassName();

		HashMap<String, String> vLFclass = MyUtils.listLookAndFeels();

		if (arg.compareToIgnoreCase("list") == 0) //$NON-NLS-1$
		{
			System.out.println(Messages.getString("MyUtils.L&FManager.ListAvailableL&F")); //$NON-NLS-1$
			for (String s : vLFclass.keySet().toArray(new String[vLFclass.size()]))
			{
				System.out.println(s + "\t\t" + vLFclass.get(s)); //$NON-NLS-1$
			}
			return;
		}
		if (arg.compareToIgnoreCase("system") == 0) //$NON-NLS-1$
		{
			lf = UIManager.getSystemLookAndFeelClassName();
		}
		else if (arg.compareToIgnoreCase("default") == 0) //$NON-NLS-1$
		{
			lf = UIManager.getCrossPlatformLookAndFeelClassName();
		}
		else if (vLFclass.containsKey(arg))
		{
			lf = vLFclass.get(arg);
		}
		else
		{
			KanjiNoSensei.log(Level.SEVERE, Messages.getString("MyUtils.L&FManager.ErrorUnknownL&F") + " : \"" + arg + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			lf = arg;
		}

		UIManager.setLookAndFeel(lf);
	}

	/**
	 * Fix '\' and '/' to the current system separator, then look if the fileName exists. If it does not, try to look at it in the defaultDirectory. If file is found returns the fixed fileName, if not, FileNotFoundException is thrown.
	 * 
	 * @param fileName
	 *            File to check.
	 * @param defaultDirectory
	 *            Default directory to check in if filename is not full path.
	 * @return fixed filename.
	 * @throws FileNotFoundException
	 *             If filename cannot be fixed nor found.
	 */
	public static String checkFileExists(String fileName, String defaultDirectory) throws FileNotFoundException
	{
		defaultDirectory = defaultDirectory.replace('/', File.separatorChar).replace('\\', File.separatorChar);
		File f = new File(fileName.replace('/', File.separatorChar).replace('\\', File.separatorChar));
		if ( !f.exists())
		{

			File f2 = new File(defaultDirectory + File.separatorChar + f.getName());
			if ( !f2.exists())
			{
				throw new FileNotFoundException(Messages.getString("MyUtils.CheckFileExists.ErrorFileNotFound") + " : \"" + f.getAbsolutePath() + "\" ou \"" + f2.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
			else
			{
				fileName = f2.toString();
			}
		}

		return fileName;
	}

	/**
	 * String comparator which ignore case.
	 */
	public static final Comparator<String>	STRING_COMPARATOR_IgnoreCase											= new Comparator<String>()
																													{
																														@Override
																														public int compare(String o1, String o2)
																														{
																															return o1.compareToIgnoreCase(o2);
																														}
																													};

	/**
	 * String comparator which allow kana or romaji strings. Each string is converted to romaji and compared to the other one, but if both are kana and not naturaly equal, that is to say one is in katakana and the other in hiragana, the comparison is false.
	 */
	public static final Comparator<String>	STRING_COMPARATOR_IgnoreCase_AllowRomajiKana							= new Comparator<String>()
																													{
																														@Override
																														public int compare(String o1, String o2)
																														{
																															int natural = o1.compareToIgnoreCase(o2);

																															// Naturaly equals (ignore case)
																															if (natural == 0) return 0;

																															// o1 in kana, o2 romaji
																															String wo1 = kanaToRomaji(o1);
																															if (( !wo1.isEmpty()) && (wo1.compareToIgnoreCase(o2) == 0)) return 0;

																															// o1 in romaji, o2 in kana
																															String wo2 = kanaToRomaji(o2);
																															if (( !wo2.isEmpty()) && (wo2.compareToIgnoreCase(o1) == 0)) return 0;

																															// o1 and o2 in kana (one in hiragana, the other in katakana),
																															// we assume it is not ok.
																															// no condition met, return natural comparison result.
																															return natural;
																														}
																													};

	/**
	 * String comparator like STRING_COMPARATOR_IgnoreCase_AllowRomajiKana with punctuation tolerance, the comparator first remove all punctuation and then compare using STRING_COMPARATOR_IgnoreCase_AllowRomajiKana comparator.
	 * 
	 * @see STRING_COMPARATOR_IgnoreCase_AllowRomajiKana
	 */
	public static final Comparator<String>	STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation				= new Comparator<String>()
																													{
																														@Override
																														public int compare(String o1, String o2)
																														{
																															// Get o1 without any punctuation.
																															String wo1 = replaceAll(replaceAll(o1, tKanaPunc, ""), tRomaPunc, ""); //$NON-NLS-1$ //$NON-NLS-2$

																															// Get o2 without any punctuation.
																															String wo2 = replaceAll(replaceAll(o2, tKanaPunc, ""), tRomaPunc, ""); //$NON-NLS-1$ //$NON-NLS-2$

																															if (wo1.isEmpty()) wo1 = o1;
																															if (wo2.isEmpty()) wo2 = o2;

																															return STRING_COMPARATOR_IgnoreCase_AllowRomajiKana.compare(wo1, wo2);
																														}
																													};

	/**
	 * String comparator which support optional ending element (marked with braces '(', ')' ). ex: mana(bu) will match "mana" and "manabu". Warning, this support only one optional element (only one couple of parenthesis).
	 */
	public static final Comparator<String>	STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation_OptionalEnd	= new Comparator<String>()
																													{
																														/**
																														 * Return s string without the last ( ) marked substring. If the entire string is between only one couple of ( ), then the string without ( ) is returned (not empty string).
																														 * 
																														 * @param s
																														 *            String to work on.
																														 * @return s string without the last ( ) marked substring, or s without ( ) if the whole string is between ( ).
																														 */
																														private String withoutEndingBracedSubstring(String s)
																														{
																															int lastClose = s.lastIndexOf(')');
																															if (lastClose < 0) return s;

																															int lastOpen = s.substring(0, lastClose).indexOf('(');
																															if (lastOpen < 0) return s;

																															String ws = s.substring(0, lastOpen) + s.substring(lastClose + 1);
																															return (ws.isEmpty() ? s : ws);
																														}

																														@Override
																														public int compare(String o1, String o2)
																														{
																															int natural = STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation.compare(o1, o2);
																															if (natural == 0) return 0;

																															String wo1 = withoutEndingBracedSubstring(o1);
																															if (( !wo1.isEmpty()) && (STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation.compare(wo1, o2) == 0)) return 0;

																															String wo2 = withoutEndingBracedSubstring(o2);
																															if (( !wo2.isEmpty()) && (STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation.compare(o1, wo2) == 0)) return 0;

																															if (( !wo1.isEmpty()) && ( !wo2.isEmpty()) && (STRING_COMPARATOR_IgnoreCase_AllowRomajiKana_NoPunctuation.compare(wo1, wo2) == 0)) return 0;

																															return natural;
																														}
																													};

	/**
	 * Replace all searches substring in the subject with the replace string.
	 * 
	 * @param subject
	 *            String to work on.
	 * @param searches
	 *            Searches pattern.
	 * @param replace
	 *            Replace string.
	 * @return subject with replaced patterns.
	 */
	public static String replaceAll(String subject, String[] searches, String replace)
	{
		String[] replaces = new String[searches.length];
		for (int i = 0; i < replaces.length; ++i)
			replaces[i] = replace;

		return replaceAll(subject, searches, replaces);
	}

	/**
	 * This method return every declined strings of the subject in a String array. The subject is declined without braces (), {}, [], （）, 「」
	 * 
	 * @see tKanaBraces
	 * @see tRomaBraces
	 * @see tKanaPunc
	 * @see tRomaPunc
	 * @param subject
	 *            Subject string to decline.
	 * @return
	 * @throws OperationNotSupportedException
	 */
	public static String[] declinedStrings(String subject) throws OperationNotSupportedException
	{
		// TODO if needed, or use simplifiedString(String)
		throw new OperationNotSupportedException("Not implemented");
	}

	public static String simplifiedString(String subject) throws BadStringFormatException
	{
		if (subject == null || subject.isEmpty()) return subject;

		subject = replaceAll(subject, tRomaStartingBraces, "(");
		subject = replaceAll(subject, tKanaStartingBraces, "(");
		subject = replaceAll(subject, tRomaEndingBraces, ")");
		subject = replaceAll(subject, tKanaEndingBraces, ")");

		subject = removeBraced(subject, "(", ")");

		subject = replaceAll(subject, tKanaPunc, "");
		subject = replaceAll(subject, tRomaPunc, "");

		subject = subject.toLowerCase();

		return subject;
	}

	public static String removeBraced(String subject, String startBrace, String endBrace) throws BadStringFormatException
	{
		if (subject == null || subject.isEmpty()) return subject;

		int sc, ec, pos;
		for (sc = 0, pos = 0; (pos = subject.indexOf(startBrace, pos) + 1) > 0; ++sc);
		for (ec = 0, pos = 0; (pos = subject.indexOf(endBrace, pos) + 1) > 0; ++ec);

		if (ec != sc)
		{
			trace(Level.WARNING, "Braces count does not match in \"" + subject + "\"");
			throw new BadStringFormatException("Braces count does not match in \"" + subject + "\"");
		}

		int startPos = 0, nextStartPos, endPos;
		do
		{
			startPos = subject.indexOf(startBrace, startPos + 1);
			nextStartPos = subject.indexOf(startBrace, startPos + 1);
			endPos = subject.indexOf(endBrace);

			if (startPos >= 0)
			{
				if ((nextStartPos < 0) || (endPos < nextStartPos))
				{
					if (endPos < startPos)
					{
						subject = subject.substring(0, endPos) + subject.substring(endPos + endBrace.length());
					}
					else
					{
						subject = subject.substring(0, startPos) + subject.substring(endPos + endBrace.length());
					}
					startPos = 0;
				}
			}

		} while (startPos >= 0);

		return subject;
	}

	public static class BadTaggedStringFormatException extends Exception
	{
		private final String	parsedString;

		public String getParsedString()
		{
			return parsedString;
		}

		public BadTaggedStringFormatException(String msg, String parsedString)
		{
			super(msg);
			this.parsedString = parsedString;
		}
	}

	public static interface ITaggedStringParseListener
	{
		String processTag(String tagValue) throws Exception;
	}

	// TODO: refaire en récursif
	public static String parseTaggedString(String taggedString, String startTag, String endTag, ITaggedStringParseListener tagProcessor) throws BadTaggedStringFormatException
	{
		if (taggedString == null || taggedString.isEmpty()) return taggedString;
		String exMsg = null;

		// We test the string is valid before to work with it.
		int sc, ec, pos;
		for (sc = 0, pos = 0; (pos = taggedString.indexOf(startTag, pos) + startTag.length()) >= startTag.length(); ++sc);
		for (ec = 0, pos = 0; (pos = taggedString.indexOf(endTag, pos) + endTag.length()) >= endTag.length(); ++ec);

		if (ec != sc)
		{
			String errMsg = "parseTaggedString: Starting tags (" + startTag + ") and ending tags (" + endTag + ") count does not match in \"" + taggedString + "\"";
			MyUtils.trace(Level.WARNING, errMsg);
			throw new BadTaggedStringFormatException(errMsg, taggedString);
		}

		if (sc == 0)
		{
			return taggedString;
		}

		// The startingTag the most at right must be an leaf.
		int st = taggedString.lastIndexOf(startTag);

		while (st >= 0)
		{
			// The nearest endingTag from the last startingTag must match it.
			int et = taggedString.indexOf(endTag, st + startTag.length());

			String subTaggedString = taggedString.substring(st + startTag.length(), et);
			String parsedSubTaggedString = parseTaggedString(subTaggedString, startTag, endTag, tagProcessor);
			String processedSubTaggedString = null;

			try
			{
				processedSubTaggedString = tagProcessor.processTag(parsedSubTaggedString);
			}
			catch (Exception e)
			{
				MyUtils.trace(Level.WARNING, "VueElement.computeTemplate: " + e.getMessage());
				processedSubTaggedString = "!TemplateError!";
				exMsg = e.getMessage();
			}

			taggedString = taggedString.substring(0, st) + processedSubTaggedString + taggedString.substring(et + endTag.length());

			st = taggedString.lastIndexOf(startTag);
		}

		if (exMsg != null)
		{
			throw new BadTaggedStringFormatException(exMsg, taggedString);
		}

		return taggedString;
	}

	public static int firstIndexOf(String subject, String[] patterns)
	{
		return firstIndexOf(subject, patterns, 0);
	}

	public static int firstIndexOf(String subject, String[] patterns, int fromIndex)
	{
		if (subject == null || subject.isEmpty() || patterns == null || patterns.length == 0) return -1;

		int first = -1;
		for (String pattern : patterns)
		{
			int pos = subject.indexOf(pattern, fromIndex);
			if (pos >= 0)
			{
				first = Math.min((first < 0) ? Integer.MAX_VALUE : first, pos);
			}
		}

		return first;
	}

	public static String bestStartsWith(String subject, String[] patterns, int toffset)
	{
		String bestMatch = null;
		int bestMatchLength = 0;

		for (String pattern : patterns)
		{
			if (subject.startsWith(pattern, toffset))
			{
				if (pattern.length() > bestMatchLength)
				{
					bestMatchLength = pattern.length();
					bestMatch = pattern;
				}
			}
		}

		return bestMatch;
	}

	/*
	 * private String withoutEndingBracedSubstring(String s) { int lastClose = s.lastIndexOf(')'); if (lastClose < 0) return s;
	 * 
	 * int lastOpen = s.substring(0, lastClose).indexOf('('); if (lastOpen < 0) return s;
	 * 
	 * String ws = s.substring(0, lastOpen) + s.substring(lastClose + 1); return (ws.isEmpty() ? s : ws); }
	 */

	/**
	 * Replace all searches substring in the subject with it's corresponding replace string.<br>
	 * <code>subject = subject.replace(searches[i], replaces[i]);</code>
	 * 
	 * @param subject
	 *            String to work on.
	 * @param searches
	 *            Searches pattern.
	 * @param replaces
	 *            Replaces pattern.
	 * @return subject with replaced patterns.
	 */
	public static String replaceAll(String subject, String[] searches, String[] replaces)
	{
		if (replaces.length < searches.length)
		{
			throw new IllegalArgumentException("Less replaces string than searches pattern : searches: " + searches + " replaces : " + replaces); //$NON-NLS-1$ //$NON-NLS-2$
		}

		for (int i = 0; i < searches.length; ++i)
		{
			subject = subject.replace(searches[i], replaces[i]);
		}

		return subject;
	}

	/**
	 * Log in MyUtils.log logger. Logger can be defined in properties file, using JVM option {@literal -Djava.util.logging.config.file=[properties file]}
	 * 
	 * @see Logger
	 * @param level
	 *            Trace level.
	 * @param trace
	 *            Trace message.
	 */
	public static void trace(Level level, String trace)
	{
		if (level.intValue() < logMinLevel.intValue()) return;
		log.log(level, trace);
	}

	/**
	 * Throw RuntimeException with given error message if condition is not false.
	 * 
	 * @param condition
	 *            Condition supposed to be false.
	 * @param errMsg
	 *            Error message if condition is true.
	 * @throws RuntimeException
	 *             if condition is true.
	 */
	public static void assertFalse(boolean condition, String errMsg)
	{
		assertTrue( !condition, errMsg);
	}

	/**
	 * Throw RuntimeException with given error message if condition is false.
	 * 
	 * @param condition
	 *            Condition supposed to be true.
	 * @param errMsg
	 *            Error message if condition is false.
	 * @throws RuntimeException
	 *             if condition is false.
	 */
	public static void assertTrue(boolean condition, String errMsg)
	{
		if ( !condition)
		{
			throw new RuntimeException(errMsg);
		}
	}

	/**
	 * Safe sleep, call {@link Thread#sleep(long)} and catch InterruptedException.
	 * 
	 * @param millis
	 *            time to sleep (milliseconds).
	 */
	public static void sleep(long millis)
	{
		trace(Level.FINEST, "Debut sleep " + millis); //$NON-NLS-1$
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			trace(Level.FINEST, "Error during sleep " + millis); //$NON-NLS-1$
			return;
		}
		trace(Level.FINEST, "Fin sleep " + millis); //$NON-NLS-1$
	}

	/**
	 * Interface that define something can be done on a Component.
	 */
	public static interface DoItToThisComponent
	{
		/**
		 * Implement what must be done on the given component.
		 * 
		 * @param c
		 *            The target component.
		 */
		void doIt(Component c);
	};

	/**
	 * Apply a {@link DoItToThisComponent} from the given Component c to every of its sub component (if c is instance of {@link Container} or {@link Window}.
	 * 
	 * @param c
	 *            Parent component.
	 * @param doIt
	 *            DoItToThisComponent object to apply.
	 * @param thisComponentFirst
	 *            Specify is the parent component is processed first (true) or last (false).
	 */
	public static void doItToAllSubComponents(Component c, DoItToThisComponent doIt, boolean thisComponentFirst)
	{
		if (thisComponentFirst)
		{
			doIt.doIt(c);
		}

		if (Container.class.isInstance(c))
		{
			for (Component sc : ((Container) c).getComponents())
			{
				doItToAllSubComponents(sc, doIt, thisComponentFirst);
			}
		}

		if (Window.class.isInstance(c))
		{
			for (Window w : ((Window) c).getOwnedWindows())
			{
				doItToAllSubComponents(w, doIt, thisComponentFirst);
			}
		}

		if ( !thisComponentFirst)
		{
			doIt.doIt(c);
		}
	}

	/** Static DoItToThisComponent used to refresh UI. */
	public static DoItToThisComponent	DO_UPDATEUI_REFRESH	= new DoItToThisComponent()
															{

																@Override
																public void doIt(Component c)
																{
																	SwingUtilities.updateComponentTreeUI(c);
																	c.invalidate();
																	c.validate();
																	c.repaint();
																}

															};

	/**
	 * Generate the L&F menu, which will be applied from the given rootComponent.
	 * 
	 * @param rootComponent
	 *            Root component on which to apply refresh when L&F is changed.
	 * @return L&F menu.
	 */
	public static JMenu getUIMenu(final Component rootComponent)
	{
		Component[] roots = {rootComponent};
		return getUIMenu(roots);
	}

	/**
	 * Generate the L&F menu, which will be applied from all the given rootComponents.
	 * 
	 * @param rootComponents
	 *            Root components on which to apply refresh when L&F is changed.
	 * @return L&F menu.
	 */
	public static JMenu getUIMenu(final Component[] rootComponents)
	{
		final HashMap<String, String> vLFclass = listLookAndFeels();

		JMenu jMenuUI = new JMenu(Messages.getString("MyUtils.L&FMenuName")); //$NON-NLS-1$

		Iterator<String> it = vLFclass.keySet().iterator();
		while (it.hasNext())
		{
			final String lf = it.next();
			JMenuItem jMenuItemLF = new JMenuItem(lf);
			jMenuItemLF.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						UIManager.setLookAndFeel(vLFclass.get(lf));
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}

					for (Component c : rootComponents)
					{
						doItToAllSubComponents(c, DO_UPDATEUI_REFRESH, false);
					}
				}

			});

			jMenuUI.add(jMenuItemLF);
		}

		return jMenuUI;
	}

	/**
	 * Call {@link refreshComponent} on the given component and all its sub components.
	 * 
	 * @see doItToAllSubComponents
	 * @see refreshComponent
	 * @param c
	 *            Root component to refresh.
	 */
	public static void refreshComponentAndSubs(Component c)
	{
		doItToAllSubComponents(c, new DoItToThisComponent()
		{

			@Override
			public void doIt(Component c)
			{
				refreshComponent(c);
			}

		}, false);
	}

	/**
	 * Call {@link Component#invalidate()}, {@link Component#validate()} and {@link Component#repaint()} on the given component.
	 * 
	 * @param c
	 *            Component to refresh.
	 */
	public static void refreshComponent(Component c)
	{
		c.invalidate();
		c.validate();
		c.repaint();
	}

	/**
	 * Convert milliseconds time to String, using {@link Messages} strings resource.
	 * 
	 * @param time
	 *            Time to convert.
	 * @return Readable time.
	 */
	public static String timeToString(long time)
	{
		long days = time / (1000 * 60 * 60 * 24);
		time -= days * (1000 * 60 * 60 * 24);
		long hours = time / (1000 * 60 * 60);
		time -= hours * (1000 * 60 * 60);
		long minutes = time / (1000 * 60);
		time -= minutes * (1000 * 60);
		long seconds = time / 1000;

		if (days > 0)
		{
			return days + Messages.getString("MyUtils.SuffixDays") + hours + Messages.getString("MyUtils.SuffixHours") + minutes + Messages.getString("MyUtils.SuffixMinutes") + seconds + Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		if (hours > 0)
		{
			return hours + Messages.getString("MyUtils.SuffixHours") + minutes + Messages.getString("MyUtils.SuffixMinutes") + seconds + Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		if (minutes > 0)
		{
			return minutes + Messages.getString("MyUtils.SuffixMinutes") + seconds + Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (seconds > 0)
		{
			return seconds + Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$
		}

		return "0" + Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Ensure the given runnable will be run in the EventDispatchThread. Test if current thread {@link SwingUtilities#isEventDispatchThread()}, if it is, run the given runnable, if not, {@link SwingUtilities#invokeLater(Runnable)} the given runnable.
	 * 
	 * @param runnable
	 *            Runnable to run in EDT.
	 */
	public static void InvokeLaterEDT(Runnable runnable)
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			runnable.run();
		}
		else
		{
			SwingUtilities.invokeLater(runnable);
		}
	}

	/**
	 * Ensure the given runnable will be run in the EventDispatchThread, and wait it finishes. Test if current thread {@link SwingUtilities#isEventDispatchThread()}, if it is, run the given runnable, if not, {@link SwingUtilities#invokeAndWait(Runnable)} the given runnable.
	 * 
	 * @param runnable
	 *            Runnable to run in EDT.
	 */
	public static void InvokeAndWaitEDT(Runnable runnable) throws InterruptedException, InvocationTargetException
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			runnable.run();
		}
		else
		{
			SwingUtilities.invokeAndWait(runnable);
		}
	}

	/**
	 * Ensure the given runnable will not be run in the EventDispatchThread. Test if current thread {@link SwingUtilities#isEventDispatchThread()}. If it is, create and start a new {@link Thread} with the given runnable, else, run the given runnable (in the current Thread).
	 * 
	 * @param runnable
	 *            Runnable to run out of EDT.
	 */
	public static void InvokeNoEDT(Runnable runnable)
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			new Thread(runnable).start();
		}
		else
		{
			runnable.run();
		}
	}

	/**
	 * Compare strings that can be null. Strings are assumed to be equals if they're both null or if {@code s1.compareTo(s2) == 0}.
	 * 
	 * @see String#compareTo(String)
	 * @param s1
	 *            String to compare.
	 * @param s2
	 *            String to compare.
	 * @return True if {@code s1.compareTo(s2) == 0} or if both s1 and s2 are null.
	 */
	public static boolean compareNullableStrings(String s1, String s2)
	{
		if (s1 == null)
		{
			return (s2 == null);
		}

		return (s1.compareTo(s2) == 0);
	}

	/**
	 * Try to find a close() method on the object, and call it. Every exception are caught, NoSuchMethodException is logged.
	 * 
	 * @param obj
	 *            Object to call close() method on.
	 */
	public static void safeClose(Object obj)
	{
		if (obj != null)
		{
			try
			{
				Method close = obj.getClass().getMethod("close"); //$NON-NLS-1$
				close.invoke(obj);
			}
			catch (NoSuchMethodException e1)
			{
				MyUtils.trace(Level.WARNING, "Try to close an object that has no close() method : " + e1.getMessage());
			}
			catch (Exception e)
			{
				// Nothing.
			}
		}
	}

	/**
	 * Try to call close() method on the given closeable, and catch every exception thrown. This method is safe if the object is already closed, or never been opened.
	 * 
	 * @param closeable
	 *            Closeable to close.
	 */
	public static void safeClose(Closeable closeable)
	{
		if (closeable != null)
		{
			try
			{
				closeable.close();
			}
			catch (Exception e)
			{
				// Nothing.
			}
		}
	}

	public static <T extends Number> double sum(Set<T> values)
	{
		double sum = 0;
		Iterator<T> it = values.iterator();
		while (it.hasNext())
		{
			T value = it.next();
			sum = sum + value.doubleValue();
		}

		return sum;
	}

	public static <T extends Number> double esperence(Set<T> values)
	{
		return sum(values) / values.size();
	}

	public static <T extends Number> double variance(Set<T> values)
	{
		double esperence = esperence(values);
		Iterator<T> it = values.iterator();
		double dv = 0;
		while (it.hasNext())
		{
			T value = it.next();
			dv += Math.pow((value.doubleValue() - esperence), 2);
		}

		return dv / values.size();
	}

	public static <T extends Number> double ecartType(Set<T> values)
	{
		return Math.sqrt(variance(values));
	}

	public static <T extends Number> double[] minmax(Set<T> values)
	{
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;

		Iterator<T> it = values.iterator();
		while (it.hasNext())
		{
			T value = it.next();
			min = Math.min(min, value.doubleValue());
			max = Math.max(max, value.doubleValue());
		}

		return new double[] {min, max};
	}

	public static <T extends Number> String explainStats(Set<T> values)
	{
		double[] minmax = minmax(values);
		return String.format("Min: %f\tMax: %f\tSum: %f\tE: %f\tV: %f\tð: %f", minmax[0], minmax[1], sum(values), esperence(values), variance(values), ecartType(values));
	}
}
