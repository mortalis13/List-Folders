using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Web.Script.Serialization;
using System.Text.RegularExpressions;
using System.IO;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Drawing.Drawing2D;

namespace Test {
  public partial class Test : Form {

    [DllImport("user32.dll")]
    public static extern bool RegisterHotKey(IntPtr hWnd, int id, int fsModifiers, int vlc);
    [DllImport("user32.dll")]
    public static extern bool UnregisterHotKey(IntPtr hWnd, int id);

    const string nl = "\r\n";

    public Test() {
      InitializeComponent();
    }

    private void tbRes_KeyDown(object sender, KeyEventArgs e) {
      if (e.KeyCode == Keys.Escape) {
        Close();
      }
    }

    private void test1() {
      int[] data = { 4, 5, 6, 7, 2, 4, 634, 123, 56, 45 };
      var json = new JavaScriptSerializer().Serialize(data);
      tbRes.Text = json;
    }

    private void test2() {
      string path = "C:/1-Roman/Documents/8-test/list-test/en";
      string exportName="no";

      Regex regex = new Regex(@"/([^/]+)$");
      Match match = regex.Match(path);
      if (match.Success) {
        exportName = match.Groups[1].ToString();
      }

      tbRes.Text = exportName;
    }

    private void test3() {
      string path = "templates/tree.html";
      string startupPath = Application.StartupPath;
      string res = "no";

      try {
        res = File.ReadAllText(path);
      }
      catch (Exception e) {
        res = "Not Found"+nl;

        res += e.Message+nl;
        res += e.StackTrace + nl;
      }

      tbRes.Text = res;
    }

    private void test4() {
      string path = "out.html";
      string text = "123";
      string res = "Done";

      try {
        File.WriteAllText(path, text);
      }
      catch (Exception e) {
        res = "Not Found" + nl;

        res += e.Message + nl;
        res += e.StackTrace + nl;
      }

      tbRes.Text = res;
    }

    private void test5() {
      Test.RegisterHotKey(this.Handle, this.GetType().GetHashCode(), 2, (int)'E');
    }

    protected override void WndProc(ref Message m) {
      if (m.Msg == 0x0312)
        MessageBox.Show("");
      base.WndProc(ref m);
    }

    private void test6() {
      //long res = (long) (DateTime.Now - DateTime.Today).TotalMilliseconds;

      long res = (long) DateTime.Now.TimeOfDay.TotalMilliseconds;
      tbRes.Text += res.ToString() + nl;

      DateTime date = DateTime.Today;
      //tbRes.Text += date + nl;
    }

    private void test7() {
      double d = 362.365895;
      tbRes.Text = String.Format("{0:f2}",d);
    }

    private void test() {
      panel1.Paint += drawBorder;
    }

    void drawBorder(object sender, PaintEventArgs e) {
      var control = (Control)sender;

      var graphics = e.Graphics;
      var bounds = e.Graphics.ClipBounds;

      Pen pen=new Pen(Color.FromArgb(120,120,120));
      pen.DashStyle = DashStyle.Dot;
      graphics.DrawLine(pen, new PointF(0, 0), new PointF(control.Width, 0));
    }

    private void Test_Load(object sender, EventArgs e) {
      test();
    }

    private void bTest_Click(object sender, EventArgs e) {
      test();
    }

  }
}
