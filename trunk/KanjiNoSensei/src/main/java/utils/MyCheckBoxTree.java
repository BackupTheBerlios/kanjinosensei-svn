package utils;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * This class represent a Tree view with checkbox on each node. It support adding node by string path, tree listener to fire on tree change (item [un]checked) and automatic subs [de]selection.
 * 
 * @author Escallier Pierre
 */
public class MyCheckBoxTree extends CheckboxTree
{
	/** Serialization version. */
	private static final long				serialVersionUID	= 1L;

	/** Allowed separator for nodes paths. */
	private static final String[]			NODEPATH_SEPARATORS	= {">", ";", "/", "\"", ":", ","};	//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	/** Root node. */
	private final DefaultMutableTreeNode	root;

	/** Current tree listener. */
	private MyCheckBoxTreeListener			listener			= null;

	/** State flag. */
	private Boolean							isTreeStable		= true;

	/**
	 * Add a node given by its path, to the current tree. All non existing parents nodes are created. If the node already exists, this method only update the selection value.
	 * 
	 * @param completeNodePath
	 *            New node given by its full node path.
	 * @param isSelected
	 *            Selection state for the given node.
	 */
	public void addNode(String completeNodePath, boolean isSelected)
	{
		for (String sep : NODEPATH_SEPARATORS)
		{
			completeNodePath = completeNodePath.replace(sep, NODEPATH_SEPARATORS[0]);
		}

		String[] nodesLabels = completeNodePath.split(NODEPATH_SEPARATORS[0]);
		for (int i = 0; i < nodesLabels.length; ++i)
		{
			nodesLabels[i] = nodesLabels[i].trim();
		}

		addNode(nodesLabels, root, isSelected);

		treeDidChange();
		repaint();
	}

	/**
	 * Private recursive method used to add Node.
	 * 
	 * @param nodesLabels
	 *            Array of nodes in the path.
	 * @param parentNode
	 *            Curent parent node.
	 * @param isSelected
	 *            Is the path selected.
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	private void addNode(String[] nodesLabels, DefaultMutableTreeNode parentNode, boolean isSelected)
	{
		if (nodesLabels.length == 0)
		{
			if (isSelected)
			{
				getCheckingModel().addCheckingPath(new TreePath(parentNode.getPath()));
			}
			else
			{
				getCheckingModel().removeCheckingPath(new TreePath(parentNode.getPath()));
			}

			return;
		}

		Enumeration<DefaultMutableTreeNode> children = parentNode.children();
		while (children.hasMoreElements())
		{
			DefaultMutableTreeNode sub = children.nextElement();

			if (sub.toString().compareToIgnoreCase(nodesLabels[0]) == 0)
			{
				nodesLabels = MyUtils.offsetObjectElements(nodesLabels, 1);
				addNode(nodesLabels, sub, isSelected);
				return;
			}
		}

		DefaultMutableTreeNode sub = new DefaultMutableTreeNode(nodesLabels[0]);
		parentNode.add(sub);
		nodesLabels = MyUtils.offsetObjectElements(nodesLabels, 1);
		addNode(nodesLabels, sub, isSelected);
		return;
	}

	/**
	 * Constructor. Create the CheckBoxTree with a default tree root.
	 */
	public MyCheckBoxTree()
	{
		super(new DefaultMutableTreeNode(Messages.getString("MyCheckBoxTree.RootName"))); //$NON-NLS-1$
		root = (DefaultMutableTreeNode) getCheckingModel().getTreeModel().getRoot();

		getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);

		getModel().addTreeModelListener(new TreeModelListener()
		{

			@Override
			public void treeStructureChanged(TreeModelEvent e)
			{
			}

			@Override
			public void treeNodesRemoved(TreeModelEvent e)
			{
			}

			@Override
			public void treeNodesInserted(TreeModelEvent e)
			{
			}

			@Override
			public void treeNodesChanged(TreeModelEvent e)
			{
				processTreeNodesChanged(new TreePath(e.getPath()));
			}

		});

		addTreeCheckingListener(new TreeCheckingListener()
		{

			@Override
			public void valueChanged(TreeCheckingEvent e)
			{
				processTreeNodesChanged(e.getLeadingPath());
			}

		});
	}

	/**
	 * Private method called to fire {@link MyCheckBoxTreeListener#treeNodesChanged(utils.MyCheckBoxTree.MyCheckBoxTreeEvent)} on listener.
	 * 
	 * @param path
	 *            The path who changed.
	 */
	private void processTreeNodesChanged(TreePath path)
	{
		if (listener != null)
		{
			Vector<String> v = new Vector<String>();
			for (Object o : path.getPath())
			{
				v.add(o.toString());
			}

			v.remove(0);

			String completeNodePath = null, item = null;
			boolean selected = false;

			completeNodePath = MyUtils.joinStringElements((String[]) v.toArray(new String[v.size()]), ">"); //$NON-NLS-1$
			item = path.getLastPathComponent().toString();
			selected = (getCheckingModel().isPathChecked(path));

			MyCheckBoxTreeEvent chkEvent = new MyCheckBoxTreeEvent(completeNodePath, item, selected, isTreeStable);
			listener.treeNodesChanged(chkEvent);

		}
	}

	/**
	 * Set the TreeListener, there can be only one tree listener, the previous one is automaticaly unbind.
	 * 
	 * @param l
	 *            Listener object to bind to this CheckBoxTree.
	 */
	public void setTreeListener(MyCheckBoxTreeListener l)
	{
		listener = l;
	}

	/**
	 * Event class used to fire {@link MyCheckBoxTreeListener} methods. This event provide item path wich fire the event, its label and selection state.
	 * 
	 * @author Escallier Pierre
	 */
	public static class MyCheckBoxTreeEvent
	{
		/** Item complete path. */
		public final String		itemPath;

		/** Item label. */
		public final String		itemLabel;

		/** Item selection state. */
		public final boolean	itemIsSelected;

		/** When event was processed Tree was in stable state, this mean it is not processing a setSubTreeSelected() or some kind of work. */
		public final boolean	isTreeStable;

		/**
		 * Event constructor, needs all fields.
		 * 
		 * @param itemPath
		 *            Item full path.
		 * @param itemLabel
		 *            Item label.
		 * @param itemIsSelected
		 *            Item selection state.
		 */
		MyCheckBoxTreeEvent(String itemPath, String itemLabel, boolean itemIsSelected, boolean isTreeStable)
		{
			this.itemPath = itemPath;
			this.itemLabel = itemLabel;
			this.itemIsSelected = itemIsSelected;
			this.isTreeStable = isTreeStable;
		}
	}

	/**
	 * Listener interface for MyCheckBoxTree.
	 * 
	 * @author Escallier Pierre
	 */
	public static interface MyCheckBoxTreeListener
	{
		/**
		 * Fire when a tree node has changed (check state changed).
		 * 
		 * @param e
		 *            MyCheckBoxTreeEvent providing usefull informations.
		 */
		void treeNodesChanged(MyCheckBoxTreeEvent e);
	}

	/**
	 * Set the selection state of all the given path subnodes.
	 * 
	 * @param path
	 *            Parent path from where to set the selection state.
	 * @param selected
	 *            Selection state to set.
	 * @param subLevel
	 *            Maximum sublevel depth to visit.
	 */
	public synchronized void setSubTreeSelected(TreePath path, boolean selected, int subLevel)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

		checkSubsFrom(node, selected, node.getLevel(), subLevel);
		treeDidChange();
		repaint();
	}

	/**
	 * Private recursive method used to set subnodes selection state.
	 * 
	 * @param node
	 *            Current visited node.
	 * @param parentState
	 *            Current parent selection state.
	 * @param level
	 *            Current depth level.
	 * @param subLevel
	 *            Maxmimum sublevel depth to visit.
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	private void checkSubsFrom(DefaultMutableTreeNode node, boolean parentState, int level, int subLevel)
	{
		TreePath path = new TreePath(node.getPath());

		if (subLevel < 0) subLevel = Integer.MAX_VALUE;

		// If this is parent call, we mark this tree as UnStable (because we will scan and change all the sub tree).
		if (node.getLevel() == level)
		{
			isTreeStable = false;
		}

		if ((node.getLevel() - level) <= subLevel)
		{
			Enumeration<DefaultMutableTreeNode> children = node.children();
			while (children.hasMoreElements())
			{
				DefaultMutableTreeNode sub = children.nextElement();
				checkSubsFrom(sub, parentState, level, subLevel);
			}
		}
		
		// If this is parent call, then we can mark this tree Stable again just BEFORE to fire the last "nodesChange" event.
		if (node.getLevel() == level)
		{
			isTreeStable = true;
		}
		
		if (parentState)
		{
			getCheckingModel().addCheckingPath(path);
		}
		else
		{
			getCheckingModel().removeCheckingPath(path);
		}
		
		/*
		 * Useless untill getCheckingModel().add/removeCheckingPath() fire treeModelListener.treeNodesChanged. processTreeNodesChanged(path);
		 */
	}

	/**
	 * Remove all root subnodes.
	 */
	public void clearModel()
	{
		DefaultTreeModel model = (DefaultTreeModel) getModel();

		root.removeAllChildren();
		model.reload();

		treeDidChange();
		repaint();
	}
	
	/**
	 * Force isTreeStable flag. Must be used by external tree scanning methods.
	 * Warning not to set tree stable if another method is running concurrently.
	 */
	public synchronized void setTreeStable(boolean isTreeStable)
	{
		this.isTreeStable = isTreeStable;
	}
}
