package epsofts.KanjiNoSensei.utils.myCheckBoxTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import epsofts.KanjiNoSensei.utils.MyUtils;

/*
 MyCheckBoxTree marche comme attendu, mais ce n'est pas tout à fait compatible avec les thèmes dans KanjiNoSensei
 Il faut mapper les éléments apartenant à un thèmes NOEUD vers un sous thème FEUILLE, pour que les mécanismes de sélections ajouté aux noeuds n'interfere pas avec la sélection des éléments relié au thème noeud. 

 ex: Elements de thème L1>Leçon 3 (noeud) => L1>Leçon 3>Leçon 3 (feuille)

 Intégrer à MyCheckBoxTree ? comment ? Intégrer à KanjiNoSensei ?
 */

/**
 * This class represent a Tree view with checkbox on each node. It support adding node by string path, tree listener to fire on tree change (item [un]checked) and automatic subs [de]selection.
 * 
 * @author Escallier Pierre
 */
public class MyCheckBoxTree extends JTree implements TreeSelectionListener
{
	public static class MyCheckBoxTreeModel extends DefaultTreeModel
	{
		/**
		 * @param root
		 */
		public MyCheckBoxTreeModel(TreeNode root)
		{
			super(root);
		}

	}

	public static class MyCheckBoxTreeSelectionModel extends DefaultTreeSelectionModel
	{
		static private enum eState
		{
			SELECTED, PARTIAL, NOT_SELECTED
		};

		private final Map<TreePath, Boolean>	invisiblesPaths	= new HashMap<TreePath, Boolean>();

		private final MyCheckBoxTreeModel		model;

		/**
		 * 
		 */
		public MyCheckBoxTreeSelectionModel(MyCheckBoxTreeModel model)
		{
			this.model = model;
			setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		}

		public void removeInvisiblePath(TreePath path)
		{
			invisiblesPaths.remove(path);
		}

		public void addInvisiblePath(TreePath path, boolean isSelected)
		{
			invisiblesPaths.put(path, isSelected);
		}

		// tests whether there is any unselected node in the subtree of given path
		public boolean isPartiallySelected(TreePath path)
		{
			if (isNode(path))
			{
				return false;
			}

			if (areAllChildren(path, eState.SELECTED))
			{
				return false;
			}

			if (areAllChildren(path, eState.NOT_SELECTED))
			{
				return false;
			}

			return true;
		}

		public boolean isPathSelected(TreePath path, boolean render)
		{
			if ( !render) return super.isPathSelected(path);

			if (isNode(path))
			{
				return super.isPathSelected(path);
			}
			else
			{
				return areAllChildren(path, eState.SELECTED);
			}
		}

		public boolean isVisiblySelected(TreePath path)
		{
			return areAllChildren(path, eState.SELECTED, true);
		}

		private boolean isNode(TreePath path)
		{
			Object node = path.getLastPathComponent();
			int nbChildren = model.getChildCount(node);
			return (nbChildren == 0);
		}

		private boolean areAllChildren(TreePath path, eState state)
		{
			return areAllChildren(path, state, false);
		}

		private boolean areAllChildren(TreePath path, eState state, boolean ignoreInvisible)
		{
			if (ignoreInvisible)
			{
				if (invisiblesPaths.containsKey(path))
				{
					return true;
				}
			}
			else
			{
				// Test invisibles paths
				Iterator<TreePath> it = invisiblesPaths.keySet().iterator();
				while (it.hasNext())
				{
					TreePath invisiblePath = it.next();
					if ((path != invisiblePath) && (path.isDescendant(invisiblePath)))
					{
						if ( !areAllChildren(invisiblePath, state, ignoreInvisible))
						{
							return false;
						}
					}
				}
			}

			// Test visibles paths
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			for (int i = 0; i < model.getChildCount(node); ++i)
			{
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) model.getChild(node, i);
				if ( !areAllChildren(new TreePath(childNode.getPath()), state, ignoreInvisible))
				{
					return false;
				}
			}

			switch (state)
			{
			case SELECTED:
			{
				if ( !ignoreInvisible && invisiblesPaths.containsKey(path))
				{
					return invisiblesPaths.get(path);
				}

				if (isNode(path))
				{
					return super.isPathSelected(path);
				}
				else
				{
					return true;
				}
			}
			case NOT_SELECTED:
			{
				if ( !ignoreInvisible && invisiblesPaths.containsKey(path))
				{
					return !invisiblesPaths.get(path);
				}

				if (isNode(path))
				{
					return !super.isPathSelected(path);
				}
				else
				{
					return true;
				}
			}
			default:
			{
				throw new UnsupportedOperationException("areAllChildren(" + state.toString() + ") make no sense");
			}
			}
		}

		public void togglePathSelection(TreePath path)
		{
			boolean doSelect;

			if (isNode(path))
			{
				doSelect = !isPathSelected(path);
			}
			else
			{
				// boolean isPartialySelected = isPartiallySelected(path);
				boolean isVisiblySelected = isVisiblySelected(path);

				// doSelect = !isPartialySelected || !isVisiblySelected;
				doSelect = !isVisiblySelected;
			}

			togglePathSelection(path, doSelect);
		}

		private void togglePathSelection(TreePath path, boolean doSelect)
		{
			if (isNode(path))
			{
				if ( !doSelect)
				{
					removeSelectionPath(path);
				}
				else
				{
					addSelectionPath(path);
				}
			}
			else
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				for (int i = 0; i < model.getChildCount(node); ++i)
				{
					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) model.getChild(node, i);
					togglePathSelection(new TreePath(childNode.getPath()), doSelect);
				}
			}
		}

		public void setSelectionPaths(TreePath[] pPaths)
		{
			throw new UnsupportedOperationException("not implemented yet!!!");
		}

	}

	/** Serialization version. */
	private static final long					serialVersionUID	= 1L;

	/** Allowed separator for nodes paths. */
	private static final String[]				NODEPATH_SEPARATORS	= {">", ";", "/", "\"", ":", ","};			//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	/** Root node. */
	private final DefaultMutableTreeNode		root;

	/** TreeModel. */
	private final MyCheckBoxTreeModel			model;

	/** TreeSelectionModel. */
	private final MyCheckBoxTreeSelectionModel	selectionModel;

	int											hotspot				= new JCheckBox().getPreferredSize().width;

	/** Current tree listener. */
	private MyCheckBoxTreeListener				listener			= null;

	/** State flag. */
	private Boolean								isTreeStable		= true;

	/** Stability timer. */
	private Timer								stabilityTimer		= new Timer();

	/**
	 * Constructor. Create the CheckBoxTree with a default tree root.
	 */
	public MyCheckBoxTree(Object rootUserObject)
	{
		super(new MyCheckBoxTreeModel(new DefaultMutableTreeNode(rootUserObject)));

		model = (MyCheckBoxTreeModel) getModel();
		root = (DefaultMutableTreeNode) model.getRoot();
		selectionModel = new MyCheckBoxTreeSelectionModel(model);
		setCellRenderer(new MyCheckBoxTreeCellRenderer(getCellRenderer(), selectionModel));
		
		addMouseListener(new MouseAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent me)
			{
				TreePath path = getPathForLocation(me.getX(), me.getY());
				if (path == null) return;
				if (me.getX() > getPathBounds(path).x + hotspot) return;

				System.out.println("mouseClicked: " + path);

				try
				{
					selectionModel.togglePathSelection(path);
				}
				finally
				{
					selectionModel.addTreeSelectionListener(MyCheckBoxTree.this);
					treeDidChange();
				}
			}
		});

		selectionModel.addTreeSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		MyUtils.trace(Level.INFO, "MyCheckBoxTree.valueChanged: " + e + "\nisStable: " + isTreeStable);
		treeDidChange();

		stabilityTimer.cancel();
		stabilityTimer.purge();
		
		setTreeStable(false);
		processTreeNodesChanged(e.getPath());
		
		stabilityTimer = new Timer();
		stabilityTimer.schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				setTreeStable(true);
				processTreeNodesChanged(null);
			}
		}, 500);
	}

	// //////////////////////////

	public void setTreeNodes(Map<String, Boolean> nodes, boolean visibles)
	{
		clearModel();
		addTreeNodes(nodes, visibles);
	}

	public void addTreeNodes(Map<String, Boolean> nodes, boolean visibles)
	{
		Iterator<String> it = nodes.keySet().iterator();
		while (it.hasNext())
		{
			String node = it.next();
			boolean isSelected = nodes.get(node);

			addNode(node, isSelected, visibles);
		}

		treeDidChange();
		repaint();
	}

	public void hideAllNodes()
	{
		hideAllNodes(root, false);
	}

	private void hideAllNodes(DefaultMutableTreeNode parentNode, boolean includeParentNode)
	{
		Enumeration<DefaultMutableTreeNode> children = parentNode.children();
		while (children.hasMoreElements())
		{
			hideAllNodes(children.nextElement(), true);
		}

		if (includeParentNode)
		{
			hideNode(parentNode, selectionModel.isPathSelected(new TreePath(parentNode.getPath())));
		}
	}

	/**
	 * Add a node given by its path, to the current tree. All non existing parents nodes are created. If the node already exists, this method only update the selection value.
	 * 
	 * @param completeNodePath
	 *            New node given by its full node path.
	 * @param isSelected
	 *            Selection state for the given node.
	 */
	private void addNode(String completeNodePath, boolean isSelected, boolean visible)
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

		addNode(nodesLabels, root, isSelected, visible);
	}

	private void hideNode(DefaultMutableTreeNode node, boolean isSelected)
	{
		TreePath path = new TreePath(node.getPath());
		selectionModel.addInvisiblePath(path, isSelected);
		node.removeFromParent();
		model.reload();
	}

	private void showNode(DefaultMutableTreeNode node, boolean isSelected)
	{
		TreePath path = new TreePath(node.getPath());

		selectionModel.removeInvisiblePath(path);

		if (isSelected)
		{
			selectionModel.addSelectionPath(path);
			model.reload();
		}
		else
		{
			selectionModel.removeSelectionPath(path);
			model.reload();
		}
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
	private void addNode(String[] nodesLabels, DefaultMutableTreeNode parentNode, boolean isSelected, boolean visible)
	{
		if (nodesLabels.length == 0)
		{
			TreePath path = new TreePath(parentNode.getPath());

			if (visible)
			{
				showNode(parentNode, isSelected);
			}
			else
			{
				hideNode(parentNode, isSelected);
			}

			return;
		}

		Enumeration<DefaultMutableTreeNode> children = parentNode.children();

		int newChildIndex = 0;
		while (children.hasMoreElements())
		{
			DefaultMutableTreeNode sub = children.nextElement();

			if (sub.toString().compareToIgnoreCase(nodesLabels[0]) == 0)
			{
				nodesLabels = MyUtils.offsetObjectElements(nodesLabels, 1);
				addNode(nodesLabels, sub, isSelected, visible);
				return;
			}
			
			if (((Comparator<String>) MyUtils.PADDED_NUMBERS_COMPARATOR).compare(nodesLabels[0], sub.getUserObject().toString()) >= 0)
			{
				++newChildIndex;
			}
		}

		DefaultMutableTreeNode sub = new DefaultMutableTreeNode(nodesLabels[0]);
		parentNode.insert(sub, newChildIndex);
		model.reload();
		nodesLabels = MyUtils.offsetObjectElements(nodesLabels, 1);
		addNode(nodesLabels, sub, isSelected, visible);
		return;
	}

	/**
	 * Private method called to fire {@link MyCheckBoxTreeListener#treeNodesChanged(MyCheckBoxTreeEvent)} on listener.
	 * 
	 * @param path
	 *            The path who changed.
	 */

	private void processTreeNodesChanged(TreePath path)
	{
		if (listener != null)
		{
			MyCheckBoxTreeEvent e;

			if (path == null)
			{
				e = new MyCheckBoxTreeEvent(null, null, false, true);
			}
			else
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
				selected = (selectionModel.isPathSelected(path));

				e = new MyCheckBoxTreeEvent(completeNodePath, item, selected, false/* isTreeStable */);
			}
			
			listener.treeNodesChanged(e);
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
	 * Event class used to fire {@link MyCheckBoxTreeListener} methods. This event provide item path which fire the event, its label and selection state.
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
		 *            MyCheckBoxTreeEvent providing useful informations.
		 */
		void treeNodesChanged(MyCheckBoxTreeEvent e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see epsofts.KanjiNoSensei.utils.IMyCheckBoxTree#setSubTreeSelected(javax.swing.tree.TreePath, boolean, int)
	 */
	/*
	 * public synchronized void setSubTreeSelected(TreePath path, boolean selected, int subLevel) { DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
	 * 
	 * checkSubsFrom(node, selected, node.getLevel(), subLevel); treeDidChange(); repaint(); }
	 */

	/**
	 * Private recursive method used to set sub nodes selection state.
	 * 
	 * @param node
	 *            Current visited node.
	 * @param parentState
	 *            Current parent selection state.
	 * @param level
	 *            Current depth level.
	 * @param subLevel
	 *            Maxmimum sub level depth to visit.
	 */
	/*
	 * @SuppressWarnings("unchecked")//$NON-NLS-1$ private void checkSubsFrom(DefaultMutableTreeNode node, boolean parentState, int level, int subLevel) { TreePath path = new TreePath(node.getPath());
	 * 
	 * if (subLevel < 0) subLevel = Integer.MAX_VALUE;
	 * 
	 * // If this is parent call, we mark this tree as UnStable (because we will scan and change all the sub tree). if (node.getLevel() == level) { isTreeStable = false; }
	 * 
	 * if ((node.getLevel() - level) <= subLevel) { Enumeration<DefaultMutableTreeNode> children = node.children(); while (children.hasMoreElements()) { DefaultMutableTreeNode sub = children.nextElement(); checkSubsFrom(sub, parentState, level, subLevel); } }
	 * 
	 * // If this is parent call, then we can mark this tree Stable again just BEFORE to fire the last "nodesChange" event. if (node.getLevel() == level) { isTreeStable = true; }
	 * 
	 * if (parentState) { getCheckingModel().addCheckingPath(path); } else { getCheckingModel().removeCheckingPath(path); } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see epsofts.KanjiNoSensei.utils.IMyCheckBoxTree#clearModel()
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
	 * Force isTreeStable flag. Must be used by external tree scanning methods. Warning not to set tree stable if another method is running concurrently.
	 */
	private synchronized void setTreeStable(boolean isTreeStable)
	{
		this.isTreeStable = isTreeStable;
	}
}
