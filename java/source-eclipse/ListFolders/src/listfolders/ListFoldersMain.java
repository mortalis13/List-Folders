package listfolders;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import listfolders.includes.Database;
import listfolders.includes.Functions;
import listfolders.includes.ScanDirectory;
import listfolders.includes.ScanDirectoryOld;
import listfolders.includes.TopDashedBorder;
import listfolders.treeviwer.TreeViewerWindow;


public class ListFoldersMain {
  
  public JFrame frame;
  
  public JTextArea taFilterExt;
  public JTextArea taExcludeExt;
  public JTextArea taFilterDir;
  public JTextArea taOutput;
  
  public JLabel lPath;
  public JTextField tfPath;
  public JTextField tfExportName;
  
  public JCheckBox chExportText;
  public JCheckBox chExportMarkup;
  public JCheckBox chExportTree;
  
  public JScrollPane pFilterExtScroll;
  public JScrollPane pExcludeExtScroll;
  public JScrollPane pFilterDirScroll;
  public JScrollPane pOutputScroll;
  
  public JPanel pStatusBar;
  public JLabel lStatus;
  
  public JToggleButton bManageOptions;
  public JButton bScanDir;
  public JButton bTreeViewer;
  public JButton bBrowse;
  
  public JButton bClearFilterExt;
  public JButton bClearExcludeExt;
  public JButton bClearFilterDir;
  public JButton bToggleExports;
  
  public JProgressBar progressBar;
  public JFileChooser fc;
  
  public TreeViewerWindow treeViewerWindow;
  public ManageOptionsDialog manOptDialog;
  
  public static ListFoldersMain window;
  public static Database db;
  public static Functions fun;
  public static ScanDirectory scandir;
  public static ScanDirectoryOld scandirOld;
  
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
          window = new ListFoldersMain();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  
  /**
   * Create the application.
   */
  public ListFoldersMain() {
    initialize();
  }
  
  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame("List Folders");
    Functions.setWindowIcon(frame);
    
    frame.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentMoved(ComponentEvent e) {
        if(manOptDialog!=null && manOptDialog.isVisible())                    // put dialog to the right border of the main window
          fun.stickWindow(frame, manOptDialog);
      }
    });
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        String value=fun.encodeJSON(fun.getFieldsMap());                     // save field values to the database on exit to restore them in the next session
        db.updateConfig("last",value);
        if(manOptDialog!=null)
          manOptDialog.dispose();
        if(treeViewerWindow!=null)
          treeViewerWindow.frame.dispose(); 
      }
      @Override
      public void windowOpened(WindowEvent e) {                             // assign static objects for use in other classes
        db=new Database();                                                  // and load last saved fields from the DB
        fun=new Functions();
        fun.loadFields();
        
        fun.addShortcut(frame.getRootPane(), "exit");
        fun.addShortcut(bScanDir, "scan");
        fun.addShortcut(bBrowse, "browseDir");
      }
    });
    frame.setBounds(0, 0, 516, 707);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
// ----------------------------------------------------- Buttons -----------------------------------------------------

    bClearFilterExt = new JButton("Clear");
    bClearExcludeExt = new JButton("Clear");
    bClearFilterDir = new JButton("Clear");
    bToggleExports = new JButton("Toggle All");
    
    bClearFilterExt.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        taFilterExt.setText("");
      }
    });
    bClearExcludeExt.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        taExcludeExt.setText("");
      }
    });
    bClearFilterDir.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        taFilterDir.setText("");
      }
    });
    bToggleExports.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        JCheckBox[] exports={chExportText, chExportMarkup, chExportTree};
        boolean clear=false;
        
        for(JCheckBox item:exports){                                          // if at least one checkbox is selected then clear all
          if(item.isSelected()){
            item.setSelected(false);
            clear=true;
          }
        }
        
        if(!clear){                                                           // if all are clear then select all
          for(JCheckBox item:exports){
            item.setSelected(true);
          }
        }
      }
    });
    
    bScanDir = new JButton("Scan Directory");
    bScanDir.setMnemonic('s');
    bScanDir.setToolTipText("Ctrl+R");
    bScanDir.setActionCommand("scan");
    bScanDir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {                              // main processing directory action
        String command=e.getActionCommand();
        
        switch(command){
        case "scan":
          scandir = new ScanDirectory();
          scandir.startScan();
          break;
        case "stop":
          scandir.stopScan();
          break;
        }
        
      }
    });
    
    bTreeViewer=new JButton("Tree View");
    bTreeViewer.setMnemonic('v');
    bTreeViewer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(treeViewerWindow==null){
          treeViewerWindow = new TreeViewerWindow();
        }
        treeViewerWindow.frame.setVisible(true);
      }
    });
    
    bManageOptions = new JToggleButton("Manage Options");
    bManageOptions.setMnemonic('m');
    bManageOptions.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED){
          if(manOptDialog==null){
            manOptDialog = new ManageOptionsDialog(frame);
          }
          manOptDialog.pack();
          fun.stickWindow(frame, manOptDialog);
          manOptDialog.setVisible(true);
        }else{
          manOptDialog.setVisible(false);
        }
      } 
    });
    
    JButton bClearOutput=new JButton("Clear");
    bClearOutput.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        taOutput.setText("");
      }
    });
    
    bBrowse=new JButton("Browse...");
    bBrowse.setToolTipText("Ctrl+O");
    bBrowse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String path;
        fc = new JFileChooser();
        
        path=tfPath.getText();
        if(path.length()!=0)
          fc.setCurrentDirectory(new File(path));
        
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(frame);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          path=file.getPath();
          path=fun.formatPath(path);
          tfPath.setText(path);
        } 
      }
    });
    
// ----------------------------------------------------- Components -----------------------------------------------------
    
    JPanel pOptions = new JPanel();
    JPanel pExports = new JPanel();
    JPanel pWrapper = new JPanel();
    
    pFilterExtScroll = new JScrollPane();
    pExcludeExtScroll = new JScrollPane();
    pFilterDirScroll = new JScrollPane();
    pOutputScroll = new JScrollPane();
    
    lPath = new JLabel("Path");
    lPath.setDisplayedMnemonic('p');
    tfPath = new JTextField();
    lPath.setLabelFor(tfPath);
    tfPath.setMargin(new Insets(2, 5, 2, 2));
    
  // ===================== Filter Textareas =====================
    
    JLabel lFilterExt = new JLabel("Filter Extensions");
    lFilterExt.setDisplayedMnemonic('f');
    lFilterExt.setLabelFor(pFilterExtScroll);
    taFilterExt = new JTextArea();
    taFilterExt.setMargin(new Insets(5, 5, 5, 5));
    taFilterExt.setLineWrap(true);
    taFilterExt.setFont(new Font("Monospaced", Font.PLAIN, 12));
    
    JLabel lExcludeExt = new JLabel("Exclude Extensions");
    lExcludeExt.setDisplayedMnemonic('e');
    lExcludeExt.setLabelFor(pExcludeExtScroll);
    taExcludeExt = new JTextArea();
    taExcludeExt.setLineWrap(true);
    taExcludeExt.setMargin(new Insets(5, 5, 5, 5));
    taExcludeExt.setFont(new Font("Monospaced", Font.PLAIN, 12));
    
    JLabel lFilterDir = new JLabel("Filter Directories");
    lFilterDir.setDisplayedMnemonic('d');
    lFilterDir.setLabelFor(pFilterDirScroll);
    taFilterDir = new JTextArea();
    taFilterDir.setLineWrap(true);
    taFilterDir.setMargin(new Insets(5, 5, 5, 5));
    taFilterDir.setFont(new Font("Monospaced", Font.PLAIN, 12));
    
  // ===================== Export Options =====================
    
    JLabel lExportOptions = new JLabel("Export Options");
    chExportText = new JCheckBox("Export Text");
    chExportText.setMnemonic('x');
    chExportText.setMargin(new Insets(0, 0, 10, 0));
    chExportText.setIconTextGap(5);
    chExportText.setHorizontalAlignment(SwingConstants.LEFT);
    
    chExportMarkup = new JCheckBox("Export Markup");
    chExportMarkup.setMnemonic('m');
    chExportMarkup.setMargin(new Insets(0, 0, 10, 0));
    chExportMarkup.setIconTextGap(5);
    chExportMarkup.setHorizontalAlignment(SwingConstants.LEFT);
    
    chExportTree = new JCheckBox("Export Tree");
    chExportTree.setMnemonic('t');
    chExportTree.setMargin(new Insets(0, 0, 10, 0));
    chExportTree.setIconTextGap(5);
    chExportTree.setHorizontalAlignment(SwingConstants.LEFT);
    
    JLabel lExportName = new JLabel("Export Name");
    lExportName.setDisplayedMnemonic('n');
    tfExportName = new JTextField();
    lExportName.setLabelFor(tfExportName);
    tfExportName.setMargin(new Insets(2, 5, 2, 2));
    tfExportName.setPreferredSize(new Dimension(150, 20));
    
  // ===================== Output Textarea and Status bar =====================
    
    taOutput = new JTextArea();
    taOutput.setMargin(new Insets(5, 5, 5, 5));
    taOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
    
    progressBar = new JProgressBar();
    
    lStatus = new JLabel(" ");
    lStatus.setOpaque(true);
    
    Border statusBorder=new CompoundBorder(new TopDashedBorder(), new EmptyBorder(5, 5, 5, 5));
    pStatusBar = new JPanel();
    pStatusBar.setBorder(statusBorder);
    
    pStatusBar.setLayout(new BorderLayout());
    pStatusBar.add(lStatus);
    
    
// =======================================================================================================================
// ===================================================== Layouts =========================================================
// =======================================================================================================================
    
    
// -------------------------------------------------- Main layout --------------------------------------------------
    
    GroupLayout gl = new GroupLayout(frame.getContentPane());
    gl.setHorizontalGroup(
      gl.createParallelGroup(Alignment.LEADING)
        .addComponent(pWrapper, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
    );
    gl.setVerticalGroup(
      gl.createParallelGroup(Alignment.LEADING)
        .addComponent(pWrapper, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
    );
    
// -------------------------------------------------- Export options panel layout --------------------------------------------------
    
    GroupLayout gl_pExports = new GroupLayout(pExports);
    gl_pExports.setHorizontalGroup(
      gl_pExports.createParallelGroup(Alignment.LEADING)
        .addComponent(chExportText)
        .addComponent(chExportMarkup)
        .addComponent(chExportTree)
    );
    
    gl_pExports.setVerticalGroup(
      gl_pExports.createSequentialGroup()
        .addComponent(chExportText)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(chExportMarkup)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(chExportTree)
    );
    
    gl_pExports.linkSize(SwingConstants.HORIZONTAL, new Component[] {chExportText, chExportMarkup, chExportTree});
    pExports.setLayout(gl_pExports);
    
// ----------------------------------------------------- Options panel layout -----------------------------------------------------
    
    GroupLayout gl_pOptions = new GroupLayout(pOptions);
    gl_pOptions.setHorizontalGroup(
      gl_pOptions.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_pOptions.createSequentialGroup()
          .addGroup(gl_pOptions.createParallelGroup(Alignment.LEADING, false)
            .addComponent(pFilterExtScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(lFilterExt, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bClearFilterExt, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGap(15)
          .addGroup(gl_pOptions.createParallelGroup(Alignment.LEADING, false)
            .addComponent(pExcludeExtScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(lExcludeExt, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
            .addComponent(bClearExcludeExt, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGap(15)
          .addGroup(gl_pOptions.createParallelGroup(Alignment.LEADING, false)
            .addComponent(pFilterDirScroll)
            .addComponent(lFilterDir, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(bClearFilterDir, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGap(15)
          .addGroup(gl_pOptions.createParallelGroup(Alignment.LEADING, false)
            .addComponent(pExports, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
            .addComponent(lExportOptions, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
            .addComponent(bToggleExports, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)))
    );
    gl_pOptions.setVerticalGroup(
      gl_pOptions.createParallelGroup(Alignment.TRAILING)
        .addGroup(gl_pOptions.createSequentialGroup()
          .addComponent(lFilterExt)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(pFilterExtScroll, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(bClearFilterExt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
        .addGroup(gl_pOptions.createSequentialGroup()
          .addComponent(lExcludeExt)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(pExcludeExtScroll, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(bClearExcludeExt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
        .addGroup(gl_pOptions.createSequentialGroup()
          .addComponent(lFilterDir)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(pFilterDirScroll, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(bClearFilterDir, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
        .addGroup(gl_pOptions.createSequentialGroup()
          .addComponent(lExportOptions)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(pExports, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(bToggleExports, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
    );
    pOptions.setLayout(gl_pOptions);
    
// ----------------------------------------------------- External wrapper layout -----------------------------------------------------
    
    GroupLayout gl_pWrapper = new GroupLayout(pWrapper);
    gl_pWrapper.setHorizontalGroup(
      gl_pWrapper.createParallelGroup(Alignment.LEADING)
        .addComponent(lPath, 0, 480, Short.MAX_VALUE)
        .addGroup(gl_pWrapper.createSequentialGroup()
          .addComponent(tfPath, 0, 480, Short.MAX_VALUE)
          .addComponent(bBrowse))
        .addComponent(pOptions, 0, 480, Short.MAX_VALUE)
        .addComponent(lExportName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        .addGroup(gl_pWrapper.createSequentialGroup()
          .addComponent(bScanDir, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
          .addComponent(bClearOutput, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
          .addComponent(bManageOptions))
        .addGroup(gl_pWrapper.createSequentialGroup()
          .addComponent(tfExportName, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
          .addComponent(bTreeViewer))
        .addComponent(pOutputScroll, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(progressBar, 0, 480, Short.MAX_VALUE)
        .addComponent(pStatusBar, 0, 480, Short.MAX_VALUE)
    );
    gl_pWrapper.setVerticalGroup(
      gl_pWrapper.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_pWrapper.createSequentialGroup()
          .addComponent(lPath)
          .addGroup(gl_pWrapper.createParallelGroup(Alignment.LEADING)
            .addComponent(tfPath, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
            .addComponent(bBrowse, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
          .addGap(18)
          .addComponent(pOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
          .addGap(18)
          .addComponent(lExportName)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addGroup(gl_pWrapper.createParallelGroup(Alignment.LEADING)
            .addComponent(tfExportName, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
            .addComponent(bTreeViewer, Alignment.TRAILING))
          .addGap(18)
          .addGroup(gl_pWrapper.createParallelGroup(Alignment.LEADING)
            .addComponent(bScanDir, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
            .addComponent(bClearOutput, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
            .addComponent(bManageOptions, Alignment.TRAILING))
          .addGap(18)
          .addComponent(pOutputScroll, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
          .addGap(10)
          .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addGap(10)
          .addComponent(pStatusBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addGap(0))
    );
    gl_pWrapper.linkSize(SwingConstants.VERTICAL, new Component[] {bScanDir, bManageOptions, bTreeViewer});
    gl_pWrapper.linkSize(SwingConstants.HORIZONTAL, new Component[] {bScanDir, bManageOptions, bTreeViewer});
    gl_pWrapper.setAutoCreateGaps(true);
    gl_pWrapper.setAutoCreateContainerGaps(true);
    
    pFilterExtScroll.setViewportView(taFilterExt);
    pExcludeExtScroll.setViewportView(taExcludeExt);
    pFilterDirScroll.setViewportView(taFilterDir);
    pOutputScroll.setViewportView(taOutput);
    
    pWrapper.setLayout(gl_pWrapper);
    frame.getContentPane().setLayout(gl);
  }
  
}
