using ListFolders.Includes;
using Tree = ListFolders.Includes.Tree;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;
using System.Windows.Forms;
using System.Collections;
using System.IO;
using System.Threading;
using System.Diagnostics;

namespace ListFolders {
  public partial class TreeViewer : Form {
    object[] rootTree;
    delegate void Set(TreeNode root);
    ImageList icons;

    string path;
    string rootName="no-name";

    string iconsPath = Functions.getPath(@"lib\icons\");
    string closeFolder = "dir.png";
    string openFolder = "dir-open.png";

    public TreeViewer() {
      InitializeComponent();
    }

    private void TreeViewer_KeyDown(object sender, KeyEventArgs e) {
      if (e.KeyCode == Keys.Escape) {
        Close();
      }
    }
    
    /*
     * Browse for a JSON file with the directory structure
     */
    private void bBrowse_Click(object sender, EventArgs e) {
      OpenFileDialog browseFile;

      browseFile = new OpenFileDialog();

      browseFile.InitialDirectory = Functions.getPath(@"export\tree\json");
      //browseFile.InitialDirectory = Directory.GetCurrentDirectory() + @"\export\tree\json";
      browseFile.Filter = "JSON files (*.json)|*.json";
      browseFile.RestoreDirectory = true;

      if (browseFile.ShowDialog() == DialogResult.OK) {
        tbPath.Text = browseFile.FileName;
      }
    }
    
    /*
     * Load tree from the JSON file to the TreeView component
     */
    private void bLoadTree_Click(object sender, EventArgs e) {
      string json;

      treeView.Nodes.Clear();

      path = Functions.formatPath(tbPath.Text);
      if (path.Length == 0) {
        bBrowse.PerformClick();
        return;
      }
      rootName = Functions.getNameFromPath(path);

      prepareIcons();
      treeView.ImageList = icons;
      treeView.ImageKey = treeView.SelectedImageKey = closeFolder;            // folder icons

      json = Functions.readFile(path);
      rootTree = getTreeObject(json);
      if (rootTree == null) return;
      
      worker.RunWorkerAsync();                                                // run background worker
    }

    /*
     * Returns object array after deserialization from the JSON string
     */
    private object[] getTreeObject(string json) {
      object tree=null;
      JavaScriptSerializer serializer;

      serializer = new JavaScriptSerializer();
      serializer.MaxJsonLength = int.MaxValue;            // for large files

      try {
        tree = serializer.DeserializeObject(json);
      }
      catch (Exception e) {
        MessageBox.Show(e.Message, "Error");
        return null;
      }

      rootTree = (object[])tree;
      return rootTree;
    }

// ---------------------------------------------- TreeView events ----------------------------------------------
    
    private void treeView_AfterExpand(object sender, TreeViewEventArgs e) {         // change folder icons for expand/collapse state
      e.Node.ImageKey = openFolder;
      e.Node.SelectedImageKey = openFolder;
    }

    private void treeView_AfterCollapse(object sender, TreeViewEventArgs e) {
      e.Node.ImageKey = closeFolder;
      e.Node.SelectedImageKey = closeFolder;
    }

    private void treeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e) {    // single click to expand/collapse folders
      var hitTest = e.Node.TreeView.HitTest(e.Location);
      if (hitTest.Location == TreeViewHitTestLocations.PlusMinus)                           // prevent double run when clicked on plus sign
        return;

      if (e.Node.IsExpanded)
        e.Node.Collapse();
      else
        e.Node.Expand();
    }
    
    /*
     * Background worker builds tree and outputs it to the TreeView component
     */
    private void worker_DoWork(object sender, DoWorkEventArgs e) {
      long time1, time2;                                                            // calculate time of the processing 
      time1 = Functions.ms();

        TreeView view = new TreeView();
        TreeNode root = view.Nodes.Add(rootName);
        buildTree(root, rootTree);                                                  // call recursive walk the structure (I. first time consuming step)

        e.Result = root;

        TreeNode node = (TreeNode)root.Clone();                                     // copy root node with all subnodes and assign it to the TreeView component
        treeView.Invoke(new Set(setTree), new object[]{node});

      time2 = Functions.ms();
      Debug.WriteLine("thread-stop: " + Functions.formatTime((int)(time2 - time1), "{0:f2} s"));                // run in Visual Studio Debug mode to see the log in the Output window
    }
    
    /*
     * Assigns processed root node to the form TreeView component which shows the structure in the window
     */
    private void setTree(TreeNode root) {
      long time1, time2;
      time1 = Functions.ms();

        treeView.BeginUpdate();
        treeView.Nodes.Add(root);                                                       // add root to the TreeView (II. second time consuming step)
        treeView.EndUpdate();

      time2 = Functions.ms();
      Debug.WriteLine("tree-set: " + Functions.formatTime((int)(time2 - time1), "{0:f2} s"));
    }
    
    /*
     * Recursively walk the 'tree' structure differentiating 'dir' and 'file' nodes as Dictionaries (with key/values)
     * ('dirs' are with children)
     */
    private void buildTree(TreeNode root, object[] tree) {
      foreach (object obNode in tree) {
        IDictionary node = (IDictionary)obNode;

        if (node.Contains("children")) {
          TreeNode dirNode = root.Nodes.Add((string)node["text"]);
          object[] dirTree = (object[])node["children"];
          buildTree(dirNode, dirTree);                                        // recursive point for subdirectories
        }
        else {
          TreeNode fileNode = root.Nodes.Add((string)node["text"]);
          string icon = (string)node["icon"];
          icon = Functions.extractIconName(icon);                             // assign right icon
          fileNode.ImageKey = fileNode.SelectedImageKey = icon;
        }

      }
    }
    
    /*
     * load all icons from the directory to the global 'icons' list
     */
    private void prepareIcons() {
      icons = new ImageList();
      icons.ColorDepth = ColorDepth.Depth32Bit;

      DirectoryInfo list = new DirectoryInfo(iconsPath);                        // read all icons from the specified directory
      foreach (FileInfo nextFile in list.GetFiles()) {
        string name = nextFile.Name;
        setIcon(icons, name);
      }
    }
    
    /*
     * add icon with name as key to the global 'icons' list
     */
    private void setIcon(ImageList icons, string name) {
      if (icons.Images.ContainsKey(name)) return;
      Image img=Image.FromFile(iconsPath + name);
      icons.Images.Add(name, img);
    }

  }
}
