using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ListFolders.Includes.Tree {
  class FileNode : TreeNode{

    public FileNode(String text, String icon) : base(text) {
      this.icon = icon;
    }

  }
}
