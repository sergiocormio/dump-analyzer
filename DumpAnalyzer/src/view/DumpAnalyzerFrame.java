package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import resources.ResourcesFactory;
import model.dump.Dump;
import model.parser.DumpParser;
import model.parser.DumpStatistics;


public class DumpAnalyzerFrame extends JFrame{

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
		this.setTitle("Dump Analyzer");
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
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		JMenuItem aboutMenuItem = new JMenuItem("About");
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
		
		//Open Button
		final JButton openButton = new JButton("Open Dump File",ResourcesFactory.getOpenIcon());
		
		openButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int retVal = fileChooser.showOpenDialog(DumpAnalyzerFrame.this);
				if(retVal == JFileChooser.APPROVE_OPTION){
					try{
						dump = new Dump();
						parser.parse(dump, fileChooser.getSelectedFile());
						pathTextField.setText(fileChooser.getSelectedFile().getPath());
						treePanel.setDump(dump);
						DumpStatistics dumpStatistics = new DumpStatistics(dump);
						statisticsTextArea.setText(dumpStatistics.getStatistics());
						//envia el scroll arriba de todo
						statisticsTextArea.setCaretPosition(0);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(DumpAnalyzerFrame.this, e.getMessage(), "Error al parsear el archivo de Dump", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
				
			}
		});
		
		topPanel.add(openButton,BorderLayout.WEST);
		//TextField
		pathTextField = new JTextField();
		pathTextField.setEditable(false);
		topPanel.add(pathTextField,BorderLayout.CENTER);
		this.add(topPanel,BorderLayout.NORTH);
		
	}

	public static void main(String[] args) throws IOException {
		new DumpAnalyzerFrame().setVisible(true);
	}

}
