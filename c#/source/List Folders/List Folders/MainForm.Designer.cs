namespace ListFolders
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
      System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
      this.tbPath = new System.Windows.Forms.TextBox();
      this.bScanDir = new System.Windows.Forms.Button();
      this.lPath = new System.Windows.Forms.Label();
      this.pOptions = new System.Windows.Forms.Panel();
      this.tbFilterDir = new System.Windows.Forms.RichTextBox();
      this.tbExcludeExt = new System.Windows.Forms.RichTextBox();
      this.tbFilterExt = new System.Windows.Forms.RichTextBox();
      this.chExportTree = new System.Windows.Forms.CheckBox();
      this.chExportText = new System.Windows.Forms.CheckBox();
      this.bClearFilterDir = new System.Windows.Forms.Button();
      this.bClearExcludeExt = new System.Windows.Forms.Button();
      this.bClearFilterExt = new System.Windows.Forms.Button();
      this.lExportOpt = new System.Windows.Forms.Label();
      this.lFilterDir = new System.Windows.Forms.Label();
      this.lExcludeExt = new System.Windows.Forms.Label();
      this.lFilterExt = new System.Windows.Forms.Label();
      this.tbExportName = new System.Windows.Forms.TextBox();
      this.lExportName = new System.Windows.Forms.Label();
      this.tbOut = new System.Windows.Forms.RichTextBox();
      this.bBrowse = new System.Windows.Forms.Button();
      this.bClearOut = new System.Windows.Forms.Button();
      this.bTreeViewer = new System.Windows.Forms.Button();
      this.progressBar = new System.Windows.Forms.ProgressBar();
      this.pStatus = new System.Windows.Forms.Panel();
      this.lStatus = new System.Windows.Forms.Label();
      this.pOptions.SuspendLayout();
      this.pStatus.SuspendLayout();
      this.SuspendLayout();
      // 
      // tbPath
      // 
      this.tbPath.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.tbPath.Location = new System.Drawing.Point(12, 29);
      this.tbPath.Margin = new System.Windows.Forms.Padding(10, 3, 3, 3);
      this.tbPath.Name = "tbPath";
      this.tbPath.Size = new System.Drawing.Size(398, 20);
      this.tbPath.TabIndex = 0;
      // 
      // bScanDir
      // 
      this.bScanDir.Location = new System.Drawing.Point(12, 300);
      this.bScanDir.Name = "bScanDir";
      this.bScanDir.Size = new System.Drawing.Size(105, 35);
      this.bScanDir.TabIndex = 1;
      this.bScanDir.Text = "Scan Directory";
      this.bScanDir.UseVisualStyleBackColor = true;
      this.bScanDir.Click += new System.EventHandler(this.bScanDir_Click);
      // 
      // lPath
      // 
      this.lPath.AutoSize = true;
      this.lPath.Location = new System.Drawing.Point(9, 13);
      this.lPath.Name = "lPath";
      this.lPath.Size = new System.Drawing.Size(29, 13);
      this.lPath.TabIndex = 3;
      this.lPath.Text = "Path";
      // 
      // pOptions
      // 
      this.pOptions.Controls.Add(this.tbFilterDir);
      this.pOptions.Controls.Add(this.tbExcludeExt);
      this.pOptions.Controls.Add(this.tbFilterExt);
      this.pOptions.Controls.Add(this.chExportTree);
      this.pOptions.Controls.Add(this.chExportText);
      this.pOptions.Controls.Add(this.bClearFilterDir);
      this.pOptions.Controls.Add(this.bClearExcludeExt);
      this.pOptions.Controls.Add(this.bClearFilterExt);
      this.pOptions.Controls.Add(this.lExportOpt);
      this.pOptions.Controls.Add(this.lFilterDir);
      this.pOptions.Controls.Add(this.lExcludeExt);
      this.pOptions.Controls.Add(this.lFilterExt);
      this.pOptions.Location = new System.Drawing.Point(12, 61);
      this.pOptions.Name = "pOptions";
      this.pOptions.Size = new System.Drawing.Size(478, 178);
      this.pOptions.TabIndex = 4;
      // 
      // tbFilterDir
      // 
      this.tbFilterDir.Font = new System.Drawing.Font("Consolas", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.tbFilterDir.Location = new System.Drawing.Point(263, 26);
      this.tbFilterDir.Margin = new System.Windows.Forms.Padding(10);
      this.tbFilterDir.Name = "tbFilterDir";
      this.tbFilterDir.Size = new System.Drawing.Size(110, 119);
      this.tbFilterDir.TabIndex = 4;
      this.tbFilterDir.Text = "";
      this.tbFilterDir.WordWrap = false;
      // 
      // tbExcludeExt
      // 
      this.tbExcludeExt.Font = new System.Drawing.Font("Consolas", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.tbExcludeExt.Location = new System.Drawing.Point(131, 26);
      this.tbExcludeExt.Margin = new System.Windows.Forms.Padding(10);
      this.tbExcludeExt.Name = "tbExcludeExt";
      this.tbExcludeExt.Size = new System.Drawing.Size(110, 119);
      this.tbExcludeExt.TabIndex = 4;
      this.tbExcludeExt.Text = "";
      this.tbExcludeExt.WordWrap = false;
      // 
      // tbFilterExt
      // 
      this.tbFilterExt.Font = new System.Drawing.Font("Consolas", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.tbFilterExt.Location = new System.Drawing.Point(0, 26);
      this.tbFilterExt.Name = "tbFilterExt";
      this.tbFilterExt.Size = new System.Drawing.Size(110, 119);
      this.tbFilterExt.TabIndex = 4;
      this.tbFilterExt.Text = "";
      this.tbFilterExt.WordWrap = false;
      // 
      // chExportTree
      // 
      this.chExportTree.AutoSize = true;
      this.chExportTree.Location = new System.Drawing.Point(389, 51);
      this.chExportTree.Name = "chExportTree";
      this.chExportTree.Size = new System.Drawing.Size(81, 17);
      this.chExportTree.TabIndex = 3;
      this.chExportTree.Text = "Export Tree";
      this.chExportTree.UseVisualStyleBackColor = true;
      // 
      // chExportText
      // 
      this.chExportText.AutoSize = true;
      this.chExportText.Location = new System.Drawing.Point(389, 28);
      this.chExportText.Name = "chExportText";
      this.chExportText.Size = new System.Drawing.Size(80, 17);
      this.chExportText.TabIndex = 3;
      this.chExportText.Text = "Export Text";
      this.chExportText.UseVisualStyleBackColor = true;
      // 
      // bClearFilterDir
      // 
      this.bClearFilterDir.Location = new System.Drawing.Point(263, 151);
      this.bClearFilterDir.Name = "bClearFilterDir";
      this.bClearFilterDir.Size = new System.Drawing.Size(110, 23);
      this.bClearFilterDir.TabIndex = 2;
      this.bClearFilterDir.Text = "Clear";
      this.bClearFilterDir.UseVisualStyleBackColor = true;
      this.bClearFilterDir.Click += new System.EventHandler(this.bClearFilterDir_Click);
      // 
      // bClearExcludeExt
      // 
      this.bClearExcludeExt.Location = new System.Drawing.Point(131, 151);
      this.bClearExcludeExt.Name = "bClearExcludeExt";
      this.bClearExcludeExt.Size = new System.Drawing.Size(110, 23);
      this.bClearExcludeExt.TabIndex = 2;
      this.bClearExcludeExt.Text = "Clear";
      this.bClearExcludeExt.UseVisualStyleBackColor = true;
      this.bClearExcludeExt.Click += new System.EventHandler(this.bClearExcludeExt_Click);
      // 
      // bClearFilterExt
      // 
      this.bClearFilterExt.Location = new System.Drawing.Point(0, 151);
      this.bClearFilterExt.Name = "bClearFilterExt";
      this.bClearFilterExt.Size = new System.Drawing.Size(110, 23);
      this.bClearFilterExt.TabIndex = 2;
      this.bClearFilterExt.Text = "Clear";
      this.bClearFilterExt.UseVisualStyleBackColor = true;
      this.bClearFilterExt.Click += new System.EventHandler(this.bClearFilterExt_Click);
      // 
      // lExportOpt
      // 
      this.lExportOpt.AutoSize = true;
      this.lExportOpt.Location = new System.Drawing.Point(386, 10);
      this.lExportOpt.Name = "lExportOpt";
      this.lExportOpt.Size = new System.Drawing.Size(74, 13);
      this.lExportOpt.TabIndex = 0;
      this.lExportOpt.Text = "Export options";
      // 
      // lFilterDir
      // 
      this.lFilterDir.AutoSize = true;
      this.lFilterDir.Location = new System.Drawing.Point(260, 10);
      this.lFilterDir.Name = "lFilterDir";
      this.lFilterDir.Size = new System.Drawing.Size(82, 13);
      this.lFilterDir.TabIndex = 0;
      this.lFilterDir.Text = "Filter Directories";
      // 
      // lExcludeExt
      // 
      this.lExcludeExt.AutoSize = true;
      this.lExcludeExt.Location = new System.Drawing.Point(128, 10);
      this.lExcludeExt.Name = "lExcludeExt";
      this.lExcludeExt.Size = new System.Drawing.Size(99, 13);
      this.lExcludeExt.TabIndex = 0;
      this.lExcludeExt.Text = "Exclude Extensions";
      // 
      // lFilterExt
      // 
      this.lFilterExt.AutoSize = true;
      this.lFilterExt.Location = new System.Drawing.Point(-3, 10);
      this.lFilterExt.Name = "lFilterExt";
      this.lFilterExt.Size = new System.Drawing.Size(83, 13);
      this.lFilterExt.TabIndex = 0;
      this.lFilterExt.Text = "Filter Extensions";
      // 
      // tbExportName
      // 
      this.tbExportName.Location = new System.Drawing.Point(12, 268);
      this.tbExportName.Name = "tbExportName";
      this.tbExportName.Size = new System.Drawing.Size(162, 20);
      this.tbExportName.TabIndex = 5;
      // 
      // lExportName
      // 
      this.lExportName.AutoSize = true;
      this.lExportName.Location = new System.Drawing.Point(9, 252);
      this.lExportName.Name = "lExportName";
      this.lExportName.Size = new System.Drawing.Size(68, 13);
      this.lExportName.TabIndex = 6;
      this.lExportName.Text = "Export Name";
      // 
      // tbOut
      // 
      this.tbOut.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.tbOut.Font = new System.Drawing.Font("Consolas", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.tbOut.Location = new System.Drawing.Point(12, 343);
      this.tbOut.Name = "tbOut";
      this.tbOut.Size = new System.Drawing.Size(479, 218);
      this.tbOut.TabIndex = 7;
      this.tbOut.Text = "";
      this.tbOut.WordWrap = false;
      // 
      // bBrowse
      // 
      this.bBrowse.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
      this.bBrowse.Location = new System.Drawing.Point(416, 26);
      this.bBrowse.Name = "bBrowse";
      this.bBrowse.Size = new System.Drawing.Size(75, 23);
      this.bBrowse.TabIndex = 8;
      this.bBrowse.Text = "Browse...";
      this.bBrowse.UseVisualStyleBackColor = true;
      this.bBrowse.Click += new System.EventHandler(this.bBrowse_Click);
      // 
      // bClearOut
      // 
      this.bClearOut.Location = new System.Drawing.Point(204, 312);
      this.bClearOut.Name = "bClearOut";
      this.bClearOut.Size = new System.Drawing.Size(110, 23);
      this.bClearOut.TabIndex = 1;
      this.bClearOut.Text = "Clear";
      this.bClearOut.UseVisualStyleBackColor = true;
      this.bClearOut.Click += new System.EventHandler(this.bClearOut_Click);
      // 
      // bTreeViewer
      // 
      this.bTreeViewer.Location = new System.Drawing.Point(401, 300);
      this.bTreeViewer.Name = "bTreeViewer";
      this.bTreeViewer.Size = new System.Drawing.Size(90, 35);
      this.bTreeViewer.TabIndex = 9;
      this.bTreeViewer.Text = "Tree Viewer";
      this.bTreeViewer.UseVisualStyleBackColor = true;
      this.bTreeViewer.Click += new System.EventHandler(this.bTreeViewer_Click);
      // 
      // progressBar
      // 
      this.progressBar.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.progressBar.Location = new System.Drawing.Point(12, 572);
      this.progressBar.Name = "progressBar";
      this.progressBar.Size = new System.Drawing.Size(477, 16);
      this.progressBar.TabIndex = 10;
      // 
      // pStatus
      // 
      this.pStatus.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.pStatus.Controls.Add(this.lStatus);
      this.pStatus.Location = new System.Drawing.Point(12, 600);
      this.pStatus.Name = "pStatus";
      this.pStatus.Size = new System.Drawing.Size(478, 19);
      this.pStatus.TabIndex = 11;
      // 
      // lStatus
      // 
      this.lStatus.AutoSize = true;
      this.lStatus.Font = new System.Drawing.Font("Consolas", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.lStatus.Location = new System.Drawing.Point(2, 5);
      this.lStatus.Name = "lStatus";
      this.lStatus.Size = new System.Drawing.Size(0, 13);
      this.lStatus.TabIndex = 0;
      // 
      // MainForm
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.ClientSize = new System.Drawing.Size(503, 624);
      this.Controls.Add(this.pStatus);
      this.Controls.Add(this.progressBar);
      this.Controls.Add(this.bTreeViewer);
      this.Controls.Add(this.bBrowse);
      this.Controls.Add(this.tbOut);
      this.Controls.Add(this.lExportName);
      this.Controls.Add(this.tbExportName);
      this.Controls.Add(this.pOptions);
      this.Controls.Add(this.lPath);
      this.Controls.Add(this.bClearOut);
      this.Controls.Add(this.bScanDir);
      this.Controls.Add(this.tbPath);
      this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
      this.KeyPreview = true;
      this.Name = "MainForm";
      this.Text = "List Folders";
      this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
      this.Load += new System.EventHandler(this.MainForm_Load);
      this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.MainForm_KeyDown);
      this.pOptions.ResumeLayout(false);
      this.pOptions.PerformLayout();
      this.pStatus.ResumeLayout(false);
      this.pStatus.PerformLayout();
      this.ResumeLayout(false);
      this.PerformLayout();

        }

        #endregion

        public System.Windows.Forms.TextBox tbPath;
        public System.Windows.Forms.Button bScanDir;
        public System.Windows.Forms.Label lPath;
        public System.Windows.Forms.Panel pOptions;
        public System.Windows.Forms.Button bClearFilterExt;
        public System.Windows.Forms.Label lFilterExt;
        public System.Windows.Forms.Button bClearFilterDir;
        public System.Windows.Forms.Button bClearExcludeExt;
        public System.Windows.Forms.Label lFilterDir;
        public System.Windows.Forms.Label lExcludeExt;
        public System.Windows.Forms.TextBox tbExportName;
        public System.Windows.Forms.Label lExportName;
        public System.Windows.Forms.CheckBox chExportTree;
        public System.Windows.Forms.CheckBox chExportText;
        public System.Windows.Forms.Label lExportOpt;
        public System.Windows.Forms.RichTextBox tbFilterDir;
        public System.Windows.Forms.RichTextBox tbExcludeExt;
        public System.Windows.Forms.RichTextBox tbFilterExt;
        public System.Windows.Forms.RichTextBox tbOut;
        private System.Windows.Forms.Button bBrowse;
        public System.Windows.Forms.Button bClearOut;
        private System.Windows.Forms.Button bTreeViewer;
        private System.Windows.Forms.Panel pStatus;
        public System.Windows.Forms.ProgressBar progressBar;
        public System.Windows.Forms.Label lStatus;
    }
}

