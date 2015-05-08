package listfolders.treeviwer;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import listfolders.includes.tree.FileNode;

public class DirectoryRenderer extends DefaultTreeCellRenderer {

  String imagePath="lib/images/";
  HashMap<String, Icon> icons;
  ArrayList<String> iconNames;

  /*
   * Gets all icons and loads them before they appear in the tree
   */
  public DirectoryRenderer(ArrayList<String> iconNames) {
    this.iconNames=iconNames;
    icons=new HashMap<String, Icon>();
    
    for(String icon:iconNames){
      if(icon!=null){
        Icon iconImage=new ImageIcon(imagePath+icon);
        icons.put(icon, iconImage);
      }
    }
  }

  /*
   * Gets icon path from the source FileNode object
   */
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

    if (leaf) {
      String path=((FileNode) value).icon;
      String iconName=TreeViewer.getIconFromNode(path);
      Icon icon=icons.get(iconName);
      setIcon(icon);
    }

    return this;
  }

}
