package view.highlightings;

import javax.swing.JDialog;
import javax.swing.JFrame;

import resources.ResourcesFactory;

public class HighlightingsDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3825981838950942111L;

	public HighlightingsDialog(JFrame owner) {
		super(owner);
		setLocationRelativeTo(owner);
		setModal(true);
		setTitle("Highlighting");
		setIconImage(ResourcesFactory.getHighlightingIcon().getImage());
	}


}