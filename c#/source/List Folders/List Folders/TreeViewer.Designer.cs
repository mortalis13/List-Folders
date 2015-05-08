namespace ListFolders {
  partial class TreeViewer {
    /// <summary>
    /// Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    /// Clean up any resources being used.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
    protected override void Dispose(bool disposing) {
      if (disposing && (components != null)) {
        components.Dispose();
      }
      base.Dispose(disposing);
    }

    #region Windows Form Designer generated code

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent() {
      System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(TreeViewer));
      this.treeView = new System.Windows.Forms.TreeView();
      this.bLoadTree = new System.Windows.Forms.Button();
      this.tbPath = new System.Windows.Forms.TextBox();
      this.bBrowse = new System.Windows.Forms.Button();
      this.label1 = new System.Windows.Forms.Label();
      this.label2 = new System.Windows.Forms.Label();
      this.worker = new System.ComponentModel.BackgroundWorker();
      this.SuspendLayout();
      // 
      // treeView
      // 
      this.treeView.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.treeView.Location = new System.Drawing.Point(12, 75);
      this.treeView.Name = "treeView";
      this.treeView.Size = new System.Drawing.Size(529, 380);
      this.treeView.TabIndex = 0;
      this.treeView.AfterCollapse += new System.Windows.Forms.TreeViewEventHandler(this.treeView_AfterCollapse);
      this.treeView.AfterExpand += new System.Windows.Forms.TreeViewEventHandler(this.treeView_AfterExpand);
      this.treeView.NodeMouseClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.treeView_NodeMouseClick);
      // 
      // bLoadTree
      // 
      this.bLoadTree.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.bLoadTree.Location = new System.Drawing.Point(12, 468);
      this.bLoadTree.Name = "bLoadTree";
      this.bLoadTree.Size = new System.Drawing.Size(529, 44);
      this.bLoadTree.TabIndex = 1;
      this.bLoadTree.Text = "Load &Tree";
      this.bLoadTree.UseVisualStyleBackColor = true;
      this.bLoadTree.Click += new System.EventHandler(this.bLoadTree_Click);
      // 
      // tbPath
      // 
      this.tbPath.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.tbPath.Location = new System.Drawing.Point(12, 27);
      this.tbPath.Name = "tbPath";
      this.tbPath.Size = new System.Drawing.Size(448, 20);
      this.tbPath.TabIndex = 2;
      // 
      // bBrowse
      // 
      this.bBrowse.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
      this.bBrowse.Location = new System.Drawing.Point(466, 25);
      this.bBrowse.Name = "bBrowse";
      this.bBrowse.Size = new System.Drawing.Size(75, 23);
      this.bBrowse.TabIndex = 3;
      this.bBrowse.Text = "Browse...";
      this.bBrowse.UseVisualStyleBackColor = true;
      this.bBrowse.Click += new System.EventHandler(this.bBrowse_Click);
      // 
      // label1
      // 
      this.label1.AutoSize = true;
      this.label1.Location = new System.Drawing.Point(12, 9);
      this.label1.Name = "label1";
      this.label1.Size = new System.Drawing.Size(29, 13);
      this.label1.TabIndex = 4;
      this.label1.Text = "Path";
      // 
      // label2
      // 
      this.label2.AutoSize = true;
      this.label2.Location = new System.Drawing.Point(12, 58);
      this.label2.Name = "label2";
      this.label2.Size = new System.Drawing.Size(29, 13);
      this.label2.TabIndex = 5;
      this.label2.Text = "Tree";
      // 
      // worker
      // 
      this.worker.DoWork += new System.ComponentModel.DoWorkEventHandler(this.worker_DoWork);
      // 
      // TreeViewer
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.ClientSize = new System.Drawing.Size(553, 524);
      this.Controls.Add(this.label2);
      this.Controls.Add(this.label1);
      this.Controls.Add(this.bBrowse);
      this.Controls.Add(this.tbPath);
      this.Controls.Add(this.bLoadTree);
      this.Controls.Add(this.treeView);
      this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
      this.KeyPreview = true;
      this.Name = "TreeViewer";
      this.Text = "Tree Viewer";
      this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TreeViewer_KeyDown);
      this.ResumeLayout(false);
      this.PerformLayout();

    }

    #endregion

    private System.Windows.Forms.TreeView treeView;
    private System.Windows.Forms.Button bLoadTree;
    private System.Windows.Forms.TextBox tbPath;
    private System.Windows.Forms.Button bBrowse;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;
    private System.ComponentModel.BackgroundWorker worker;

  }
}