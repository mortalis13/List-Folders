package listfolders.includes.tree;
import java.util.ArrayList;

/*
 * Directory node
 */
public class DirNode extends TreeNode{
  
  public ArrayList<TreeNode> children;
  
  public DirNode(String text, ArrayList<TreeNode> children) {
    super(text);
    this.children = children;
  }

}
