package view;
import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.dump.Dump;
import model.dump.thread.DumpThread;
import view.highlightings.Highlighting;
import view.highlightings.HighlightingPersistor;


public class DumpTreePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5636154388264725732L;
	private JTree dumpTree;
	private Dump dump;
	private JTextArea completeText;
	private LinkedList<Highlighting> highlightings;
	public LinkedList<Highlighting> getHighlightings() {
		return highlightings;
	}

	private DumpThread selectedThread;
	private JSplitPane splitPane;
	private JScrollPane viewThreadScrollPane;
	private JScrollPane dumpTreeScrollPane;
	
	public DumpTreePanel(Dump dump){
		this.dump = dump;
		loadHighlightings();
		createUI();
	}

	/**
	 * creates and load the highlightings
	 */
	private void loadHighlightings() {
		highlightings = new HighlightingPersistor().loadHighlightings();
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
			processHighlightings();
		}
		
	}

	public void processHighlightings() {
		Highlighter highlighter = completeText.getHighlighter();
		highlighter.removeAllHighlights();
		//if completeText is empty
		if(completeText.getText() == null || completeText.getText().trim().length() == 0){
			return;
		}
		//Complete text in upper case
		String text = completeText.getText().toUpperCase();
		for(Highlighting highlighting : highlightings){
			int lastIndex = -1;
			try {
				if(highlighting.getToken() != null && highlighting.getToken().trim().length()>0){
					do{
						lastIndex = text.indexOf(highlighting.getToken().trim().toUpperCase(), lastIndex+1);
						if(lastIndex>=0){
							highlighter.addHighlight(getPreviousEnterIndex(text,lastIndex), getNextEnterIndex(text, lastIndex), new DefaultHighlighter.DefaultHighlightPainter(highlighting.getBackgroundColor()));
						}
					}while(lastIndex>=0);
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
	}

	private int getNextEnterIndex(String text, int index) {
		int nextEnterIndex = text.indexOf("\n", index);
		if(nextEnterIndex==-1){
			nextEnterIndex = text.length();
		}
		return nextEnterIndex;
	}

	/**
	 * Returns the index of "\n" previous to index parameter in text
	 * @param text
	 * @param index
	 * @return 
	 */
	private int getPreviousEnterIndex(String text, int index) {
		int previousEnterIndex = -1;
		int currentIndex = -1;
		while(currentIndex < index){
			previousEnterIndex = currentIndex;
			currentIndex = text.indexOf("\n",currentIndex+1);
			if(currentIndex==-1){ //There is no enter previous
				break;
			}
		}
		if(previousEnterIndex==-1){
			return 0;
		}
		return previousEnterIndex;
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
