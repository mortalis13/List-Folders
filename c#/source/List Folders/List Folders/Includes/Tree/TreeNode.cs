using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ListFolders.Includes.Tree {
  public class TreeNode {

    public string text;
    public string icon;

    public TreeNode(string text) {
      this.text = text;
      this.icon = "./lib/images/directory.png";                     // default folder icon
    }

    public string toString() {
      return text;
    }

  }
}
