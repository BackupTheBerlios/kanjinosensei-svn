package epsofts.KanjiNoSensei.utils.myCheckBoxTree;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import epsofts.KanjiNoSensei.utils.myCheckBoxTree.MyCheckBoxTree.MyCheckBoxTreeSelectionModel;

public class MyCheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer
{
	private MyCheckBoxTreeSelectionModel	selectionModel;

	private TreeCellRenderer		delegate;

	private TristateCheckBox		checkBox	= new TristateCheckBox();

	public MyCheckBoxTreeCellRenderer(TreeCellRenderer delegate, MyCheckBoxTreeSelectionModel selectionModel)
	{
		this.delegate = delegate;
		this.selectionModel = selectionModel;
		setLayout(new BorderLayout());
		setOpaque(false);
		checkBox.setOpaque(false);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component renderer = delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
 
		TreePath path = tree.getPathForRow(row);
		if (path != null)
		{
			
			if (selectionModel.isPathSelected(path, true))
			{
				checkBox.setSelected(Boolean.TRUE);
				//checkBox.setState(Boolean.TRUE);
			}
			else
			{
				checkBox.setState(selectionModel.isPartiallySelected(path)?TristateCheckBox.PARTIALY_SELECTED:TristateCheckBox.NOT_SELECTED);
				//checkBox.setState(selectionModel.isPartiallySelected(path) ? null : Boolean.FALSE);
			}

		}
		removeAll();
		add(checkBox, BorderLayout.WEST);
		add(renderer, BorderLayout.CENTER);
		return this;
	}
}
