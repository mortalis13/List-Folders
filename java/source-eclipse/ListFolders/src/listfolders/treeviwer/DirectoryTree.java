package listfolders.treeviwer;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import listfolders.includes.tree.TreeNode;


public class DirectoryTree extends JTree implements TreeWillExpandListener{
  ArrayList<String> iconNames;
  
  public DirectoryTree(ArrayList<String> iconNames){
    this.iconNames=iconNames;
  }
  
  public void setData(TreeNode data){
    DirectoryModel model=new DirectoryModel(data);
    super.setModel(model);
  }
  
  public void setTreeOptions(JTree tree){
    DefaultTreeCellRenderer renderer;
    Icon nodeIcon = null;
    
    getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    setToggleClickCount(1);
    setShowsRootHandles(true);
    addTreeWillExpandListener(this);
    
    renderer=new DirectoryRenderer(iconNames);
    setCellRenderer(renderer);
  }
  
  public void treeWillExpand(TreeExpansionEvent e){ }
  public void treeWillCollapse(TreeExpansionEvent e) { }
  
} 
