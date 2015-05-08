using ListFolders.Includes;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ListFolders {
  public partial class MainForm : Form {
    public static MainForm form;
    public static Database db;
    
    ScanDirectory scandir;
    TreeViewer treeViewer;
    
    public static bool startScan=true;

    public MainForm() {
      InitializeComponent();
      pStatus.Paint += Functions.drawBorder;
    }

    private void MainForm_Load(object sender, EventArgs e) {
      form = this;
      Form.CheckForIllegalCrossThreadCalls = false;                     // disable check for calls to UI elements (like tbOut.Text=...) from other threads

      db = new Database();
      new Functions();
      Functions.loadFields();                                           // load last saved fields from the database ('config' table, 'last' row)
    }

    private void MainForm_FormClosing(object sender, FormClosingEventArgs e) {
      string value = Functions.encodeJSON(Functions.getFieldsMap());
      db.updateConfig("last", value);                                   // save current fields to the database ('config' table) to restore them in the next session
      db.CloseConnection();
    }
    
    private void bScanDir_Click(object sender, EventArgs e) {
      if (MainForm.startScan) {                                         // start or stop scanning
        scandir = new ScanDirectory(form);
        scandir.startScan();
      }
      else {
        scandir.stopScan();
      }
    }

    private void bBrowse_Click(object sender, EventArgs e) {          // open folder chooser starting from the directory in the Path field
      FolderBrowserDialog browseDir;
      DialogResult result;
      string folderName, path;
      
      browseDir = new FolderBrowserDialog();
      path=tbPath.Text;
      if (path.Length != 0)
        browseDir.SelectedPath = Functions.formatPath(path);
      result = browseDir.ShowDialog();
      
      if (result == DialogResult.OK) {
        folderName = browseDir.SelectedPath;
        tbPath.Text = folderName;
      }
    }

    private void bClearFilterExt_Click(object sender, EventArgs e) {
      tbFilterExt.Clear();
    }

    private void bClearExcludeExt_Click(object sender, EventArgs e) {
      tbExcludeExt.Clear();
    }

    private void bClearFilterDir_Click(object sender, EventArgs e) {
      tbFilterDir.Clear();
    }

    private void bClearOut_Click(object sender, EventArgs e) {
      tbOut.Clear();
    }

    private void MainForm_KeyDown(object sender, KeyEventArgs e) {
      if (e.KeyCode == Keys.Escape) {
        Close();
      }
    }

    private void bTreeViewer_Click(object sender, EventArgs e) {
      treeViewer = new TreeViewer();
      treeViewer.Show();
    }

  }
}
