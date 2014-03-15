package view;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import model.dump.thread.DumpThread;

/**
 * Renderer del arbol que cambia el fondo de color segun el estado del thread
 * @author scormio
 *
 */
public class DumpThreadCellRenderer extends DefaultTreeCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8235579211713562599L;
	public static Color colorGreen = Color.decode("#BDEAB3");
	public static Color colorRed = Color.decode("#E8ABA8");
	public static final Color colorYellow = Color.decode("#FDEEC1");
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		//Por default vuelve la opacidad a false
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree,
				value, selected, expanded, leaf, row, hasFocus);
		label.setOpaque(false);
		
		//Cambia el fondo solo cuando no está seleccionado
		if ((value != null) && (value instanceof DefaultMutableTreeNode) && !selected) {
		      Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
			if (userObject instanceof DumpThread) {
				DumpThread thread = (DumpThread) userObject;
				
				label.setOpaque(true);
				switch (thread.getState()) {
				case BLOCKED:
					label.setBackground(colorRed);
					break;
				case RUNNABLE:
				case RUNNING:
					label.setBackground(colorGreen);
					break;
				case TIMED_WAITING:
				case WAITING:
					label.setBackground(colorYellow);
					break;
				default:
					break;
				}
				return label;
			}
		}
		
		return super.getTreeCellRendererComponent(tree,
				value, selected, expanded, leaf, row, hasFocus);
	}

}
