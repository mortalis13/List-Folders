package listfolders;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import listfolders.includes.Database;
import listfolders.includes.Functions;
import listfolders.includes.ManageOptions;
import listfolders.includes.TopDashedBorder;

public class ManageOptionsDialog extends JDialog {

  private JPanel contentPane;
  public JPanel panel;
  public JTextField tfName;
  public JComboBox cbList;
  public JButton bAdd;
  public JButton bRemove;
  public JPanel pStatusBar;
  public JLabel lStatus;
  
  private boolean switchAfterAdd;
  
  ListFoldersMain window;
  Functions fun;
  Database db;
  ManageOptions manOpt;

  public ManageOptionsDialog(Window owner) {
    super(owner);
    setResizable(false);
    
    window=ListFoldersMain.window;
    fun=ListFoldersMain.fun;
    db=ListFoldersMain.db;
    
    initialize();
  }
  
  private void initialize(){
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    contentPane = new JPanel();
    contentPane.setBorder(null);
    setContentPane(contentPane);
    contentPane.setLayout(new GridLayout(1, 0, 0, 0));
    
    panel = new JPanel();
    contentPane.add(panel);
    
    tfName = new JTextField();
    tfName.setFont(new Font("Tahoma", Font.PLAIN, 12));
    tfName.setMargin(new Insets(2, 5, 2, 2));
    tfName.setColumns(10);
    
    addWindowListener(new WindowAdapter() {                                     // Load options list
      @Override
      public void windowOpened(WindowEvent e) {
        manOpt=new ManageOptions();
        manOpt.listOptions();
        fun.addShortcut(getRootPane(), "closeManOpt");
        fun.addShortcut(tfName, "addOption");
      }
      
      @Override
      public void windowClosed(WindowEvent e) {
        window.bManageOptions.setSelected(false);
      }
    });
    
    bAdd = new JButton("Add");
    bAdd.addActionListener(new ActionListener() {                                // Add option
      public void actionPerformed(ActionEvent e) { 
        String name=tfName.getText();
        manOpt.addOption(name);
        switchAfterAdd=true;
        cbList.setSelectedItem(name);
      }
    });
    
    cbList = new JComboBox();
    cbList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {                                // Switch options
        String name=(String) cbList.getSelectedItem();
        manOpt.loadOption(name);
        
        if(!switchAfterAdd){
          int count=cbList.getItemCount();
          tfName.setText(name);
          lStatus.setText(count+" options loaded");
        }else
          switchAfterAdd=false;
      }
    });
    
    bRemove = new JButton("Remove");
    bRemove.addActionListener(new ActionListener(){                                // Remove option
      public void actionPerformed(ActionEvent e){
        String name=(String) cbList.getSelectedItem();
        manOpt.removeOption(name);
      }
    });
    
    Border statusBorder=new CompoundBorder(new TopDashedBorder(), new EmptyBorder(5, 5, 5, 5));
    pStatusBar = new JPanel();
    pStatusBar.setBorder(statusBorder);
    
    lStatus = new JLabel("");
    lStatus.setOpaque(true);
    pStatusBar.add(lStatus);
    
    GroupLayout gl_panel = new GroupLayout(panel);
    gl_panel.setHorizontalGroup(
      gl_panel.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel.createSequentialGroup()
          .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_panel.createSequentialGroup()
              .addGap(20)
              .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                .addComponent(tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(cbList, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                .addComponent(bAdd)
                .addComponent(bRemove, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)))
            .addGroup(gl_panel.createSequentialGroup()
              .addContainerGap()
              .addComponent(pStatusBar, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)))
          .addContainerGap())
    );
    gl_panel.setVerticalGroup(
      gl_panel.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel.createSequentialGroup()
          .addGap(20)
          .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
            .addComponent(tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(bAdd))
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
            .addComponent(cbList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(bRemove, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
          .addPreferredGap(ComponentPlacement.UNRELATED, 30, Short.MAX_VALUE)
          .addComponent(pStatusBar, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
    );
    gl_panel.linkSize(SwingConstants.VERTICAL, new Component[] {tfName, bAdd, cbList, bRemove});
    gl_panel.linkSize(SwingConstants.HORIZONTAL, new Component[] {tfName, cbList});
    gl_panel.linkSize(SwingConstants.HORIZONTAL, new Component[] {bAdd, bRemove});
    pStatusBar.setLayout(new BorderLayout(5, 0));
    
    panel.setLayout(gl_panel);
  }
}
