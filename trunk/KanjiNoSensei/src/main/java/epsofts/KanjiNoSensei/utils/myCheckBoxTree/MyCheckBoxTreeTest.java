/**
 * @author Escallier Pierre
 * @file MyCheckBoxTreeTest.java
 * @date 24 sept. 2008
 */
package epsofts.KanjiNoSensei.utils.myCheckBoxTree;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 */
public class MyCheckBoxTreeTest extends JFrame
{
	private MyCheckBoxTreeTest()
	{
		MyCheckBoxTree tree = new MyCheckBoxTree("Root");
		
		Map<String, Boolean> nodes = new TreeMap<String, Boolean>();
		nodes.put("Un>Ichi", true);
		nodes.put("Un>Ni", true);
		nodes.put("Deux>Ichi", true);
		nodes.put("Deux>Ni", true);
		
		tree.setTreeNodes(nodes, true);
		
		Map<String, Boolean> invisiblesNodes = new TreeMap<String, Boolean>();
		invisiblesNodes.put("Un>San", true);
		invisiblesNodes.put("Deux>San", false);
		
		tree.addTreeNodes(invisiblesNodes, false);
		
		tree.treeDidChange();
		tree.repaint();
		
		JScrollPane sp = new JScrollPane(tree);

		JTextArea textArea = new JTextArea(3, 10);
		JScrollPane textPanel = new JScrollPane(textArea);

		getContentPane().add(sp, BorderLayout.CENTER);
		getContentPane().add(textPanel, BorderLayout.SOUTH);
	}

	/**
	 * @param args
	 */
	public static void main(String args[])
	{
		MyCheckBoxTreeTest frame = new MyCheckBoxTreeTest();
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}
