package listfolders.includes;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import listfolders.ListFoldersMain;
import listfolders.ManageOptionsDialog;

public class ManageOptions {
  
  ManageOptionsDialog dialog;
  Functions fun;
  Database db;
  ListFoldersMain window;
  
  public ManageOptions(){
    window=ListFoldersMain.window;
    dialog=window.manOptDialog;
    fun=ListFoldersMain.fun;
    db=ListFoldersMain.db;
  }
  
  /*
   * Adds option with entered name to the database
   */
  public void addOption(String name){
    String value;
    value=fun.encodeJSON(fun.getFieldsMap());               // get key/value Array of all field values the serialize it
    db.updateOption(name, value);
    listOptions("(+1 new)");
  }
  
  /*
   * Removes option selected in the dropdown from the database
   */
  public void removeOption(String name){
    String value;
    db.removeOption(name);
    listOptions("(1 removed)");
  }
  
  /*
   * Loads an option selected in the dropdown
   */
  public void loadOption(String name){
    String value;
    value=db.getOption(name);
    if(value==null) return;
    fun.loadFields(value);
  }
  
  /*
   * Gets all option names from the 'options' table
   * and assigns the retrieved list to the ComboBox
   * Then print the status message about total count of items
   */
  public void listOptions(String msg){
    ArrayList<String> list;
    String[] array;
    int count=0;
    
    list=db.listOptions();
    if(list==null) return;
    
    array=list.toArray(new String[0]);                                // convert ArrayList to Array for ComboBox model
    count=list.size();
    
    DefaultComboBoxModel model=new DefaultComboBoxModel(array);
    dialog.cbList.setModel(model);
    
    if(msg==null) msg="";
    else msg=" "+msg;
    
    dialog.lStatus.setText(count+" options loaded"+msg);
  }
  
  /*
   * Method with default status message
   */
  public void listOptions(){
    listOptions(null);
  }
  
}
