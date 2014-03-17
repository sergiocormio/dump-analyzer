package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.ResourcesFactory;

public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2566945927276159128L;
	
	public AboutDialog(JFrame owner){
		super(owner);
		createUI();
	}

	private void createUI() {
		setTitle("About Dump Analyzer");
		createMainPanel();
		this.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseClicked(MouseEvent e) {
				//Close the app
				AboutDialog.this.setVisible(false);
			}
		});
		setUndecorated(true);
		setModal(true);
		setResizable(false);
		pack();
		setLocationRelativeTo(this.getOwner());
	}

	private void createMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Font boldFont = new Font("Arial", Font.BOLD,14);
		Font normalFont = new Font("Courier", Font.PLAIN,12);
		//Image
		JLabel appImageLabel = new JLabel(ResourcesFactory.getAppImage());
		appImageLabel.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(appImageLabel);
		//Name and version
		JLabel nameAndVersionLabel = new JLabel("Dump Analyzer Version 1.0");
		nameAndVersionLabel.setAlignmentX(CENTER_ALIGNMENT);
		nameAndVersionLabel.setFont(boldFont);
		panel.add(nameAndVersionLabel);
		//space filler
		panel.add(Box.createRigidArea(new Dimension(0,12)));
		//year and creator
		JLabel yearAndCreatorLabel = new JLabel("2014 - Sergio A. Cormio");
		yearAndCreatorLabel.setAlignmentX(CENTER_ALIGNMENT);
		yearAndCreatorLabel.setFont(normalFont);
		panel.add(yearAndCreatorLabel);
		//email
		JLabel emailLabel = new JLabel("sergiocormio@gmail.com");
		emailLabel.setAlignmentX(CENTER_ALIGNMENT);
		emailLabel.setFont(normalFont);
		panel.add(emailLabel);
		//space filler
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(panel);
	}

}
