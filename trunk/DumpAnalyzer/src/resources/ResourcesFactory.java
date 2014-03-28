package resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ResourcesFactory {
	
		//APP icon
		public static ImageIcon getDumpIcon(){
			return new ImageIcon(ResourcesFactory.class.getResource("rigid_dump_truck.png"));
		}
		
		//Open icon
		public static Icon getOpenIcon(){
			return new ImageIcon(ResourcesFactory.class.getResource("open-file.png"));
		}
		
		//APP Image
		public static Icon getAppImage() {
			return new ImageIcon(ResourcesFactory.class.getResource("rigid_dump_truck 256.png"));
		}
		
		//Highlighting
		public static ImageIcon getHighlightingIcon() {
			return new ImageIcon(ResourcesFactory.class.getResource("highlighter-text.png"));
		}
}
