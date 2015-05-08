namespace Test {
  partial class Test {
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
      this.tbRes = new System.Windows.Forms.RichTextBox();
      this.bTest = new System.Windows.Forms.Button();
      this.panel1 = new System.Windows.Forms.Panel();
      this.SuspendLayout();
      // 
      // tbRes
      // 
      this.tbRes.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
      this.tbRes.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(80)))), ((int)(((byte)(80)))), ((int)(((byte)(80)))));
      this.tbRes.Font = new System.Drawing.Font("Consolas", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.tbRes.ForeColor = System.Drawing.Color.White;
      this.tbRes.Location = new System.Drawing.Point(12, 12);
      this.tbRes.Margin = new System.Windows.Forms.Padding(10);
      this.tbRes.Name = "tbRes";
      this.tbRes.Size = new System.Drawing.Size(829, 477);
      this.tbRes.TabIndex = 1;
      this.tbRes.Text = "";
      this.tbRes.WordWrap = false;
      this.tbRes.KeyDown += new System.Windows.Forms.KeyEventHandler(this.tbRes_KeyDown);
      // 
      // bTest
      // 
      this.bTest.Location = new System.Drawing.Point(12, 502);
      this.bTest.Name = "bTest";
      this.bTest.Size = new System.Drawing.Size(140, 38);
      this.bTest.TabIndex = 2;
      this.bTest.Text = "Test";
      this.bTest.UseVisualStyleBackColor = true;
      this.bTest.Click += new System.EventHandler(this.bTest_Click);
      // 
      // panel1
      // 
      this.panel1.Location = new System.Drawing.Point(170, 503);
      this.panel1.Name = "panel1";
      this.panel1.Size = new System.Drawing.Size(670, 36);
      this.panel1.TabIndex = 3;
      // 
      // Test
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.ClientSize = new System.Drawing.Size(852, 552);
      this.Controls.Add(this.panel1);
      this.Controls.Add(this.bTest);
      this.Controls.Add(this.tbRes);
      this.Name = "Test";
      this.Text = "Test";
      this.Load += new System.EventHandler(this.Test_Load);
      this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.RichTextBox tbRes;
    private System.Windows.Forms.Button bTest;
    private System.Windows.Forms.Panel panel1;
  }
}

