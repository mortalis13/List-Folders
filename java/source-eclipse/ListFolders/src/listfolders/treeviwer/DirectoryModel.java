package listfolders.treeviwer;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import listfolders.includes.tree.DirNode;
import listfolders.includes.tree.FileNode;
import listfolders.includes.tree.TreeNode;

public class DirectoryModel implements TreeModel {
  
  private TreeNode root;

  public DirectoryModel(TreeNode root) {
    this.root = root;
  }
  
  public Object getRoot() {
    return root;
  }

  public Object getChild(Object parent, int index) {
    DirNode dir = (DirNode) parent;
    return dir.children.get(index);
  }

  public int getChildCount(Object parent) {
    if(!(parent instanceof DirNode)) return 0;
    
    DirNode dir = (DirNode) parent;
    return dir.children.size();
  }

  public boolean isLeaf(Object node) {
    return node instanceof FileNode;
  }

//------------------------ other methods ------------------------
  
 public int getIndexOfChild(Object parent, Object child) { return 0; }
 public void addTreeModelListener(TreeModelListener l) { }
 public void removeTreeModelListener(TreeModelListener l) { }
 public void valueForPathChanged(TreePath path, Object newValue) { }

}
