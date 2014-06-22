package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import resources.ResourcesFactory;
import view.highlightings.HighlightingsDialog;
import model.dump.Dump;
import model.parser.DumpParser;
import model.parser.DumpStatistics;


public class DumpAnalyzerFrame extends JFrame{

	private static final String APP_TITLE = "Dump Analyzer";
	/**
	 * 
	 */
	private static final long serialVersionUID = -5891671256906504322L;
	private DumpTreePanel treePanel;
	private Dump dump;
	private DumpParser parser;
	private JTextField pathTextField;
	private JFileChooser fileChooser;
	private JScrollPane statisticsScrollPane;
	private JTextArea statisticsTextArea;
	
	public DumpAnalyzerFrame() throws IOException{
		parser = new DumpParser();
		dump = new Dump();
		createUI();
		pack();
		this.setLocationRelativeTo(null);
	}

	private void createUI() {
		this.setTitle(APP_TITLE);
		setIconImage(ResourcesFactory.getDumpIcon().getImage());
		setLayout(new BorderLayout(0,0));
		setPreferredSize(new Dimension(800, 600));
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		
		createTopPanel();
		treePanel = new DumpTreePanel(dump);
		createStatisticsPanel();
		//Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePanel, statisticsScrollPane);
		this.add(splitPane,BorderLayout.CENTER);
		createMenu();
		//prepare to open files via drag and drop
		setDragAndDrop();
	}
	
	private void setDragAndDrop() {
		this.setTransferHandler(new TransferHandler(){

			/**
			 * 
			 */
			private static final long serialVersionUID = -6410994042858912735L;
			@Override
	        public boolean canImport(TransferHandler.TransferSupport info) {
	            return true;
	        }

	        @Override
	        public boolean importData(TransferHandler.TransferSupport info) {
	            if (!info.isDrop()) {
	                return false;
	            }

	            // Get the fileList that is being dropped.
	            Transferable t = info.getTransferable();
	            List<File> data;
	            try {
	                data = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
	            } 
	            catch (Exception e) { 
	            	return false; 
	            }
	           
	            for (File file : data) {
	                openDumpFile(file);
	            }
	            
	            return true;
	        }

		});
		
	}

	private void chooseDumpFile() {
		int retVal = fileChooser.showOpenDialog(DumpAnalyzerFrame.this);
		if(retVal == JFileChooser.APPROVE_OPTION){
			openDumpFile(fileChooser.getSelectedFile());
		}
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		//FILE
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		//Open
		JMenuItem openMenuItem = new JMenuItem("Open...");
		openMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseDumpFile();
			}

		});
		fileMenu.add(openMenuItem);
		
		//SEPARATOR
		fileMenu.add(new JSeparator());
		
		//Exit
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});
		fileMenu.add(exitMenuItem);
		
		//HELP
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		//About
		JMenuItem aboutMenuItem = new JMenuItem("About Dump Analyzer...");
		aboutMenuItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				new AboutDialog(DumpAnalyzerFrame.this).setVisible(true);
			}
		});
		helpMenu.add(aboutMenuItem);
		this.setJMenuBar(menuBar);
		
	}

	private void createStatisticsPanel() {
		JPanel statisticsPanel = new JPanel(new BorderLayout());
		statisticsPanel.setBorder(new TitledBorder("Statistics"));
		statisticsTextArea = new JTextArea();
		statisticsTextArea.setEditable(false);
		statisticsPanel.add(statisticsTextArea,BorderLayout.CENTER);
		statisticsScrollPane = new JScrollPane(statisticsPanel);
	}

	private void createTopPanel() {
		fileChooser = new JFileChooser();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setBorder(new EmptyBorder(2, 0, 2, 2));
		
		JPanel topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
		
		//Open Button
		final JButton openButton = new JButton("Open Dump File",ResourcesFactory.getOpenIcon());
		
		openButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				chooseDumpFile();
			}
		});
		
		topLeftPanel.add(openButton);
		//Highlighting Button
		final JButton adminHighlightingButton = new JButton("Highlighting",ResourcesFactory.getHighlightingIcon());
		
		adminHighlightingButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HighlightingsDialog highlightingsDialog = new HighlightingsDialog(DumpAnalyzerFrame.this, DumpAnalyzerFrame.this.treePanel.getHighlightings());
				highlightingsDialog.addPropertyChangeListener(HighlightingsDialog.LIST_CHANGED_EVENT, new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if(evt.getPropertyName().equals(HighlightingsDialog.LIST_CHANGED_EVENT)){
							DumpAnalyzerFrame.this.treePanel.processHighlightings();
						}
					}
				});
				highlightingsDialog.setVisible(true);
				DumpAnalyzerFrame.this.treePanel.processHighlightings();
			}
		});
		
		topLeftPanel.add(adminHighlightingButton);
		
		//Find or search button
		JButton findButton = new JButton("Find",ResourcesFactory.getFindIcon());
		findButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showFindDialog();
			}
		});
		topLeftPanel.add(findButton);
		
		topPanel.add(topLeftPanel,BorderLayout.WEST);
		//TextField
		pathTextField = new JTextField();
		pathTextField.setEditable(false);
		topPanel.add(pathTextField,BorderLayout.CENTER);
		this.add(topPanel,BorderLayout.NORTH);
		
	}
	
	protected void showFindDialog() {
		treePanel.showFindDialog();
	}

	private void openDumpFile(File dumpFile){
		try{
			dump = new Dump();
			parser.parse(dump, dumpFile);
			pathTextField.setText(dumpFile.getPath());
			treePanel.setDump(dump);
			DumpStatistics dumpStatistics = new DumpStatistics(dump);
			statisticsTextArea.setText(dumpStatistics.getStatistics());
			//envia el scroll arriba de todo
			statisticsTextArea.setCaretPosition(0);
			this.setTitle(dumpFile.getName() + " - " + APP_TITLE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(DumpAnalyzerFrame.this, "Error parsing dump file: " + (dumpFile!=null?dumpFile.getName():""), "Error in Dump File", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		new DumpAnalyzerFrame().setVisible(true);
	}

}
