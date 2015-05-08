package listfolders.treeviwer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import listfolders.includes.Functions;


public class TreeViewerWindow {
  
  public static TreeViewerWindow window;
  Functions fun;
  
  public JFrame frame;
  public JButton bLoadTree;
  public JTextField tfPath;
  public TreeViewer treeViewer;
  
  public DirectoryTree tree;
  public JScrollPane scrollPane;
  
  public JLabel lPath;
  public JButton bBrowse;
  public JFileChooser fc;
  
  String filterStartDir="export/tree/json";
  
  /**
   * Create the application.
   */
  public TreeViewerWindow() {
    initialize();
    window=this;
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame("Tree Viewer");
    Functions.setWindowIcon(frame);

    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        treeViewer=new TreeViewer();
        fun=new Functions();
        fun.addShortcut(frame.getRootPane(), "exitTreeViewer");
        fun.addShortcut(bBrowse, "browseTreeFile");
      }
    });
    frame.setBounds(100, 100, 577, 535);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    GridBagLayout gbl = new GridBagLayout();
    gbl.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0};
    gbl.columnWeights = new double[]{1.0};
    frame.getContentPane().setLayout(gbl);
    
    lPath = new JLabel();
    lPath.setBorder(new EmptyBorder(0, 5, 0, 0));
    lPath.setText("Path");
    GridBagConstraints gbc_lPath = new GridBagConstraints();
    gbc_lPath.insets = new Insets(5, 5, 0, 5);
    gbc_lPath.fill = GridBagConstraints.HORIZONTAL;
    gbc_lPath.gridx = 0;
    gbc_lPath.gridy = 0;
    frame.getContentPane().add(lPath, gbc_lPath);
    
    tfPath = new JTextField();
    tfPath.setMargin(new Insets(5, 5, 2, 5));
    GridBagConstraints gbc_tfPath = new GridBagConstraints();
    gbc_tfPath.ipady = 5;
    gbc_tfPath.insets = new Insets(5, 5, 5, 5);
    gbc_tfPath.ipadx = 5;
    gbc_tfPath.fill = GridBagConstraints.HORIZONTAL;
    gbc_tfPath.gridx = 0;
    gbc_tfPath.gridy = 1;
    frame.getContentPane().add(tfPath, gbc_tfPath);
    
    bBrowse = new JButton();
    bBrowse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fc = new JFileChooser();
        fc.setCurrentDirectory(new File(filterStartDir));
        fc.setFileFilter(new CustomFilter());
        int returnVal = fc.showOpenDialog(frame);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          String path=file.getPath();
          path=Functions.formatPath(path);
          tfPath.setText(path);
        } 
      }
    });
    bBrowse.setText("Browse...");
    GridBagConstraints gbc_bBrowse = new GridBagConstraints();
    gbc_bBrowse.insets = new Insets(5, 0, 5, 5);
    gbc_bBrowse.fill = GridBagConstraints.HORIZONTAL;
    gbc_bBrowse.gridx = 1;
    gbc_bBrowse.gridy = 1;
    gbc_bBrowse.ipady = 5;
    frame.getContentPane().add(bBrowse, gbc_bBrowse);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 2;
    gbc_scrollPane.gridwidth = 2;
    frame.getContentPane().add(scrollPane, gbc_scrollPane);
    
    bLoadTree = new JButton("Load Tree");
    bLoadTree.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(tfPath.getText().length()==0){
          bBrowse.doClick();
          return;
        }
        treeViewer.showTree();
      }
    });
    
    GridBagConstraints gbc_bLoadTree = new GridBagConstraints();
    gbc_bLoadTree.ipady = 20;
    gbc_bLoadTree.ipadx = 5;
    gbc_bLoadTree.insets = new Insets(5, 5, 5, 5);
    gbc_bLoadTree.fill = GridBagConstraints.HORIZONTAL;
    gbc_bLoadTree.gridx = 0;
    gbc_bLoadTree.gridy = 3;
    gbc_bLoadTree.gridwidth = 2;
    frame.getContentPane().add(bLoadTree, gbc_bLoadTree);
  }

}
