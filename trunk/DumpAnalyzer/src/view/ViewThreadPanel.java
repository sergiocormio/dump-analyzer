package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import model.dump.thread.DumpThread;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.PatternPredicate;
import org.jdesktop.swingx.search.SearchFactory;

import view.highlightings.Highlighting;

public class ViewThreadPanel extends JPanel {

	public static final String SCROLL_CHANGED_BY_USER = "Scroll changed by user";
	private static final long serialVersionUID = 1L;
	private JXTable table;
	private JScrollPane scrollPane;
	private DumpThread threadToShow;
	private DefaultTableModel tableModel;
	private DumpTreePanel parentFrame;
	
	public ViewThreadPanel(DumpTreePanel parentFrame){
		this.parentFrame = parentFrame;
		createUI();
	}
	
	private void createUI() {
		setLayout(new BorderLayout());
		initTable();
		scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(Color.WHITE);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void initTable() {
		table = new JXTable();
	    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    //hide columns
	  	table.setTableHeader(null);
	  	//hide grid
	  	table.setShowGrid(false);
	  	table.setIntercellSpacing(new Dimension(0, 0));
	    tableModel = new DefaultTableModel(null, new String[]{"text"});
	    table.setModel(tableModel);
	    table.setHorizontalScrollEnabled(true);
	}

	public void processHighlightings(){
		List<Highlighting> highlightings = parentFrame.getHighlightings();
		//removes all the previous highlighters
		for(Highlighter h: table.getHighlighters()){
			table.removeHighlighter(h);
		}
		
		PatternPredicate patternPredicate = null;
		ColorHighlighter highlighter = null;
		List<Highlighter> highlighters = new LinkedList<Highlighter>();
		for(Highlighting highlighting : highlightings){
			//TODO set case sensitive and insensitive (now it's only case insensitive)
		    patternPredicate = new PatternPredicate(Pattern.compile(highlighting.getToken(),Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
		    highlighter = new ColorHighlighter(patternPredicate, highlighting.getBackgroundColor(), highlighting.getForegroundColor());
		    highlighters.add(highlighter);
		}
		//Reverse order to work properly
		Collections.reverse(highlighters);
		table.setHighlighters(highlighters.toArray(new Highlighter[0]));
	}

	public synchronized void setThreadToShow(DumpThread thread) {
		this.threadToShow = thread;
		loadInitialData(threadToShow);
		processHighlightings();
	}

	private void loadInitialData(DumpThread thread) {
		removeAllRows();
		String[] auxStr = new String[1];
		if(thread != null){
			for(String line : thread.getCompleteText().split("\n")){
				auxStr[0] = line;
				tableModel.addRow(auxStr);
			}
		}
		table.packAll();
	}
	
	private void removeAllRows(){
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		    	tableModel.removeRow(i);
		    }
		}
	}
	
	public void showFindDialog(){
		SearchFactory.getInstance().showFindInput(ViewThreadPanel.this, table.getSearchable());
	}
	
}
