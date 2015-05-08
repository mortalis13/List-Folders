using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ListFolders.Includes.Tree {
  class DirNode : TreeNode {

    public List<TreeNode> children;

    public DirNode(String text, List<TreeNode> children) : base(text) {
      this.children = children;
    }

  }
}
