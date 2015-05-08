package listfolders.includes.tree;

/*
 * Parent for DirNode and FileNode
 */
public class TreeNode {
  public String text;
  public String icon;
  
  public TreeNode(String text){
    this.text=text;
    this.icon = "./lib/images/directory.png";
  }
  
  public String toString(){
    return text;
  }
  
}
