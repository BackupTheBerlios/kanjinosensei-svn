package utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;

import vue.KanjiNoSensei;

import nl.jj.swingx.gui.modal.JModalFrame;

/**
 * Utilities class, provide lot of functions.
 * 
 * @author Escallier Pierre
 */
public abstract class MyUtils
{
	static private final Logger	log = Logger.getLogger(MyUtils.class.getName());
	
	/**
	 * Test the system fonts and report which ones are able to display testing character (unicode).
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
	private static final String[]	tRomaji		= {"a", "i", "u", "e", "o", "ka", "ki", "ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta", "chi", "tsu", "te", "to", "na", "ni", "nu", "ne", "no", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$
		"ha", "hi", "fu", "he", "ho", "ma", "mi", "mu", "me", "mo", "ya", "yu", "yo", "ra", "ri", "ru", "re", "ro", "wa", "wo", "n", "ga", "gi", "gu", "ge", "go", "za", "ji", "zu", "ze", "zo", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$
		"da", "ji", "zu", "de", "do", "ba", "bi", "bu", "be", "bo", "pa", "pi", "pu", "pe", "po"};				//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$

	/** Romaji composed characters table. */
	private static final String[]	tRomaYaYuYo	= {"kya", "kyu", "kyo", "sha", "shu", "sho", "cha", "chu", "cho", "nya", "nyu", "nyo", "hya", "hyu", "hyo", "mya", "myu", "myo", "rya", "ryu", "ryo", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$
		"gya", "gyu", "gyo", "ja", "ju", "jo", "bya", "byu", "byo", "pya", "pyu", "pyo"};						//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

	/** Hiragana standard table. */
	private static final String[]	tHiragana	= {"あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$
		"ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ", "を", "ん", "が", "ぎ", "ぐ", "げ", "ご", "ざ", "じ", "ず", "ぜ", "ぞ", "だ", "ぢ", "づ", "で", "ど", "ば", "び", "ぶ", "べ", "ぼ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$ //$NON-NLS-32$ //$NON-NLS-33$ //$NON-NLS-34$ //$NON-NLS-35$ //$NON-NLS-36$ //$NON-NLS-37$
		"ぱ", "ぴ", "ぷ", "ぺ", "ぽ"					};																//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** Hiragana composed characters table. */
	private static final String[]	tHiraYaYuYo	= {"きゃ", "きゅ", "きょ", "しゃ", "しゅ", "しょ", "ちゃ", "ちゅ", "ちょ", "にゃ", "にゅ", "にょ", "ひゃ", "ひゅ", "ひょ", "みゃ", "みゅ", "みょ", "りゃ", "りゅ", "りょ", "ぎゃ", "ぎゅ", "ぎょ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$
		"じゃ", "じゅ", "じょ", "びゃ", "びゅ", "びょ", "ぴゃ", "ぴゅ", "ぴょ"};													//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

	/** Hiragana little TSU character. */
	private static final String		hiraTsu		= "っ";															//$NON-NLS-1$

	/** Katakana standard table. */
	private static final String[]	tKatakana	= {"ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト", "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$
		"ホ", "マ", "ミ", "ム", "メ", "モ", "ヤ", "ユ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ワ", "ヲ", "ン", "ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ", "ダ", "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$ //$NON-NLS-32$ //$NON-NLS-33$ //$NON-NLS-34$ //$NON-NLS-35$ //$NON-NLS-36$ //$NON-NLS-37$
		"パ", "ピ", "プ", "ペ", "ポ"					};																//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** Katakana composed characters table. */
	private static final String[]	tKataYaYuYo	= {"キャ", "キュ", "キョ", "シャ", "シュ", "ショ", "チャ", "チュ", "チョ", "ニャ", "ニュ", "ニョ", "ヒャ", "ヒュ", "ヒョ", "ミャ", "ミュ", "ミョ", "リャ", "リュ", "リョ", "ギャ", "ギュ", "ギョ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$
		"ジャ", "ジュ", "ジョ", "ビャ", "ビュ", "ビョ", "ピャ", "ピュ", "ピョ"};													//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

	/** Katakana little TSU character. */
	private static final String		kataTsu		= "ッ";															//$NON-NLS-1$

	/** Kanas punctuation. */
	private static final String[]	tKanaPunc	= {"　", "、", "。", "？", "！", "；", "−", "ー", "（", "）", "「", "」"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

	/** Romaji punctuation. */
	private static final String[]	tRomaPunc	= {" ", ",", ".", "?", "!", ";", "-", "-", "(", ")", "[", "]"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

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
	 * @param kanas
	 *            Kana string to convert.
	 * @return romaji converted string.
	 */
	public static String kanasToRomaji(String kanas)
	{
		return katakanaToRomaji(hiraganaToRomaji(kanas));
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
	 * @throws FileNotFoundException If filename cannot be fixed nor found.
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

																															// o1 in kanas, o2 romaji
																															String wo1 = kanasToRomaji(o1);
																															if (( !wo1.isEmpty()) && (wo1.compareToIgnoreCase(o2) == 0)) return 0;

																															// o1 in romaji, o2 in kanas
																															String wo2 = kanasToRomaji(o2);
																															if (( !wo2.isEmpty()) && (wo2.compareToIgnoreCase(o1) == 0)) return 0;

																															// o1 and o2 in kanas (one in hiragana, the other in katakana),
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

	public static boolean	logDisable	= false;

	public static void trace(Level level, String trace)
	{
		log.log(level, trace);
	}

	public static void assertFalse(boolean condition, String errMsg)
	{
		assertTrue( !condition, errMsg);
	}

	public static void assertTrue(boolean condition, String errMsg)
	{
		if ( !condition)
		{
			throw new RuntimeException(errMsg);
		}
	}

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

	public static class MyModalFrame extends JModalFrame
	{

		private static final long	serialVersionUID	= 1L;

		private static final Component getFirstComponent(Window window)
		{
			if (window == null) return null;

			if (window.getComponentCount() <= 0)
			{
				JButton jButton = new JButton("FAKE"); //$NON-NLS-1$
				jButton.setVisible(true);
				window.add(jButton, BorderLayout.CENTER);
			}

			return window.getComponent(0);
		}

		public MyModalFrame(Window owner, boolean modal)
		{
			super(owner, getFirstComponent(owner), modal);
		}

	}

	public static interface DoItToThisComponent
	{
		void doIt(Component c);
	};

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
		
		if (!thisComponentFirst)
		{
			doIt.doIt(c);
		}
	}

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

	public static JMenu getUIMenu(final Component rootComponent)
	{
		Component[] roots = {rootComponent};
		return getUIMenu(roots);
	}

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
	
	public static void refreshComponent(Component c)
	{
		c.invalidate();
		c.validate();
		c.repaint();
	}
	
	public static String timeToString(long time)
	{		
		long days = time / (1000*60*60*24);
		time -= days * (1000*60*60*24);
		long hours = time / (1000*60*60);
		time -= hours * (1000*60*60);
		long minutes = time / (1000*60);
		time -= minutes * (1000*60);
		long seconds = time / 1000;
		
		if (days > 0)
		{
			return days+Messages.getString("MyUtils.SuffixDays")+hours+Messages.getString("MyUtils.SuffixHours")+minutes+Messages.getString("MyUtils.SuffixMinutes")+seconds+Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		
		if (hours > 0)
		{
			return hours+Messages.getString("MyUtils.SuffixHours")+minutes+Messages.getString("MyUtils.SuffixMinutes")+seconds+Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		if (minutes > 0)
		{
			return minutes+Messages.getString("MyUtils.SuffixMinutes")+seconds+Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		if (seconds > 0)
		{
			return seconds+Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$
		}
		
		return "0"+Messages.getString("MyUtils.SuffixSeconds"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
