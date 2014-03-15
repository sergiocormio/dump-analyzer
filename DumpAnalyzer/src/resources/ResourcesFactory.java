package resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ResourcesFactory {
		public static ImageIcon getDumpIcon(){
			return new ImageIcon(ResourcesFactory.class.getResource("rigid_dump_truck.png"));
		}
		
		public static Icon getOpenIcon(){
			return new ImageIcon(ResourcesFactory.class.getResource("open-file.png"));
		}
}
