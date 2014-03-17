package view;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.dump.Dump;
import model.dump.thread.DumpThread;


public class DumpTreePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5636154388264725732L;
	private JTree dumpTree;
	private Dump dump;
	private JTextArea completeText;
	private DumpThread selectedThread;
	private JSplitPane splitPane;
	private JScrollPane viewThreadScrollPane;
	private JScrollPane dumpTreeScrollPane;
	
	public DumpTreePanel(Dump dump){
		this.dump = dump;
		createUI();
	}

	private void createUI() {
		this.setLayout(new BorderLayout());
		createTreePanel();
		createViewThreadPanel();
		//Create a split pane with the two scroll panes in it.
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				dumpTreeScrollPane, viewThreadScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerLocation(600);
		this.add(splitPane,BorderLayout.CENTER);
	}

	private void createViewThreadPanel() {
		completeText = new JTextArea();
		completeText.setEditable(false);
		viewThreadScrollPane = new JScrollPane(completeText);
	}

	private void createTreePanel() {
		//creates jtree "empty"
		dumpTree = new JTree();
		//Le coloca un renderer exclusivo para poder mostrar distintos colores
		dumpTree.setCellRenderer(new DumpThreadCellRenderer());
		loadTree();
		dumpTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			public void valueChanged(TreeSelectionEvent arg0) {
						updateSelectedThread();
						updateViewThreadPanel();
			}

		});
		expandJTree(dumpTree);
		dumpTreeScrollPane = new JScrollPane(dumpTree);
	}
	
	private void updateSelectedThread(){
		selectedThread = null;
		if(dumpTree.getSelectionPath()!=null){
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)dumpTree.getSelectionPath().getLastPathComponent();
			if(treeNode.getUserObject() instanceof DumpThread){
				selectedThread = (DumpThread)treeNode.getUserObject();
			}
		}
	}
	
	private void updateViewThreadPanel() {
		completeText.setText("");
		if(selectedThread!=null){
			completeText.setText(selectedThread.getCompleteText());
			//envia el scroll arriba de todo
			completeText.setCaretPosition(0);
		}
		
	}

	private void loadTree() {
		//load tree
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Dump");
		DefaultMutableTreeNode jTreeNode = null;
		for(DumpThread thread : dump.getAllThreadsSorted()){
			if(thread.getParent()==null){
				//Lo agrega al root (o al top)
				jTreeNode = new DefaultMutableTreeNode(thread);
				top.add(jTreeNode);
				//Agrega a sus hijos si los tiene...
				addChildrenToJTree(thread,jTreeNode);
			}
		}
		dumpTree.setModel(new DefaultTreeModel(top));
	}
	
	private void addChildrenToJTree(DumpThread fatherNode, DefaultMutableTreeNode fatherJTreeNode) {
		DefaultMutableTreeNode childJTreeNode = null;
		for(DumpThread childNode : fatherNode.getSortedChildren()){
			childJTreeNode = new DefaultMutableTreeNode(childNode);
			fatherJTreeNode.add(childJTreeNode);
			if(childNode.getChildrenQuantity()>0){
				//recursive call
				addChildrenToJTree(childNode, childJTreeNode);
			}
		}
		
	}
	
	private void expandJTree(JTree tree) {
		// Loop over all tree rows and make all visible.  
		for( int i = 0; i < tree.getRowCount(); ++i ){  
		  tree.expandRow(i);
		}  
	}

	public void setDump(Dump dump) {
		this.dump = dump;
		loadTree();
		expandJTree(dumpTree);
		dumpTree.updateUI();
	}

}
