package listfolders.includes;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import listfolders.ListFoldersMain;

import com.google.gson.Gson;

public class Functions {
  
  public static ListFoldersMain window;
  public Database db;
  
  static final String nl="\n";
  
  private HashMap<String, Shortcut> shortcuts;
  
// ----------------------------------------------- Shortcut actions -----------------------------------------------
  
  Action exitAction = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      window.frame.dispose();
    }
  };
  
  Action exitTreeViewer = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      window.treeViewerWindow.frame.dispose();
    }
  };
  
  Action closeManOpt = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      window.manOptDialog.dispose();
    }
  };
  
  Action scanAction = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      window.bScanDir.doClick();
    }
  };
  
  Action browseDir = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      window.bBrowse.doClick();
    }
  };
  
  Action browseTreeFile = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      window.treeViewerWindow.bBrowse.doClick();
    }
  };
  
  Action addOption = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      window.manOptDialog.bAdd.doClick();
    }
  };
  
// ----------------------------------------------- Constructor -----------------------------------------------
  
  public Functions(){
    window=ListFoldersMain.window;
    db=ListFoldersMain.db;
    
    shortcuts=new HashMap<String, Shortcut>();
    setShortcuts();
  }
  
// ----------------------------------------------- Functions -----------------------------------------------
  
  /*
   * Adds predefined shortcuts to the HashMap
   * which then used in the addShortcut() to assign shortcuts to window components
   */
  private void setShortcuts(){
    KeyStroke stroke;
    
    stroke=KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    shortcuts.put("exit", new Shortcut(stroke, exitAction));
    shortcuts.put("exitTreeViewer", new Shortcut(stroke, exitTreeViewer));
    shortcuts.put("closeManOpt", new Shortcut(stroke, closeManOpt));
    
    stroke=KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
    shortcuts.put("scan", new Shortcut(stroke, scanAction));
    
    stroke=KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
    shortcuts.put("browseDir", new Shortcut(stroke, browseDir));
    shortcuts.put("browseTreeFile", new Shortcut(stroke, browseTreeFile));
    
    stroke=KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    shortcuts.put("addOption", new Shortcut(stroke, addOption));
  }
  
  /*
   * Assigns shortcut to the component globally within the parent window
   * The command is taken from the setShortcuts() .put() calls
   */
  public void addShortcut(JComponent comp, String comm){
    KeyStroke stroke;
    Action action;
    InputMap inputMap;
    
    stroke=shortcuts.get(comm).stroke;
    action=shortcuts.get(comm).action;
    
    inputMap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(stroke, comm);
    comp.getActionMap().put(comm, action);
  }
  
  /*
   * Loads field values from the 'options' table
   * and assigns each value to appropriate field on the form
   */
  public void loadFields(String fieldsList){
    HashMap<String,Object> fields;
    if(fieldsList.length()==0) return;
    fields=decodeJSON(fieldsList);
    assignFields(fields);
  }
  
  /*
   * Loads and assign the last options set
   * saved after previous application session
   * Redirects to more general method loadFields(String)
   */
  public void loadFields(){
    String last=db.loadLastOptions();
    if(last==null) return;
    loadFields(last);
  }
  
  /*
   * Gets HashMap of all form fields
   * which is used to serialize them to JSON string
   */
  public HashMap getFieldsMap(){
    HashMap<String,Object> map=new HashMap<String,Object>();
      
    String path, filterExt, excludeExt, filterDir, exportName;
    boolean doExportText, doExportMarkup, doExportTree;
    
    path=window.tfPath.getText();
    filterExt=window.taFilterExt.getText();
    excludeExt=window.taExcludeExt.getText();
    filterDir=window.taFilterDir.getText();
    
    doExportText=window.chExportText.isSelected();
    doExportMarkup=window.chExportMarkup.isSelected();
    doExportTree=window.chExportTree.isSelected();
    exportName=window.tfExportName.getText();
    
    map.put("path",path);
    map.put("filterExt",filterExt);
    map.put("excludeExt",excludeExt);
    map.put("filterDir",filterDir);
    map.put("doExportText",doExportText);
    map.put("doExportMarkup",doExportMarkup);
    map.put("doExportTree",doExportTree);
    map.put("exportName",exportName);
    
    return map;
  }
  
  /*
   * Assigns values from the HashMap to form fields
   */
  private void assignFields(HashMap fields){
    window.tfPath.setText((String) fields.get("path"));
    window.taFilterExt.setText((String) fields.get("filterExt"));
    window.taExcludeExt.setText((String) fields.get("excludeExt"));
    window.taFilterDir.setText((String) fields.get("filterDir"));
    
    window.chExportText.setSelected((boolean) fields.get("doExportText"));
    window.chExportMarkup.setSelected((boolean) fields.get("doExportMarkup"));
    window.chExportTree.setSelected((boolean) fields.get("doExportTree"));
    window.tfExportName.setText((String) fields.get("exportName"));
  }
  
// ----------------------------------------------------- read/write -----------------------------------------------------
  
  /*
   * Reads JSON file and returns the string
   */
  public static String readWholeFile(String path){
    String res = "";
    
    try {
      Path filePath = Paths.get(path);
      Charset charset = Charset.forName("UTF-8");
      
      byte[] data = Files.readAllBytes(filePath);
      res=new String(data, charset);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return res;
  }
  
  /*
   * Writes the text to the file
   * filename contains extension
   */
  public static void writeFile(String filename, String text) {
    File file;
    PrintWriter writer;
    
    try {
      file = new File(filename);
      file.createNewFile();

      writer = new PrintWriter(filename);
      writer.print(text);
      writer.close();
    } catch (Exception e) {
      System.out.println("error-writing-file: " + e.getMessage());
    }
  }
  
// ----------------------------------------------------- strings -----------------------------------------------------
  
  /*
   * Formats path, fixes backslashes, trims and removes last slash
   */
  public static String formatPath(String path) {
    path=path.replace('\\', '/');
    path=path.trim();
    
    int last=path.length()-1;
    if(path.substring(last).equals("/"))
      path=path.substring(0,last);
    
    return path;
  }
  
  /*
   * Joins array items into a string with separators
   */
  public static String join(ArrayList<String> array, String separator) {
    String res = "";
    int size=array.size();
    
    for(int i=0;i<size;i++){
      if(i==size-1)
        separator="";
      res += array.get(i) + separator;
    }
    return res;
  }
  
  /*
   * Checks if text matches partially to regex
   */
  public static boolean matches(String regex, String text) {
    Pattern pat=Pattern.compile(regex);
    return pat.matcher(text).find();
  }
  
  /*
   * Returns the result of the string search using regex
   * The 'group' parameter corresponds to the regex group in parenthesis
   * If the whole result is needed group=0 should be passed
   */
  public static String regexFind(String pattern, String text, int group){
    String result="";
    
    Pattern pat=Pattern.compile(pattern);
    Matcher mat=pat.matcher(text);
    
    if(mat.find())
      result=mat.group(group);
    
    return result;
  }
  
  /*
   * Converts string to UTF-8 encoding
   */
  public static String fixEncoding(String value){
    String fix=value;
    try {
      fix = new String(value.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return fix;
  }
  
  /*
   * Returns file name from the full path of the JSON file
   * the name is assigned to the root directory name
   */
  public static String getNameFromPath(String path){
    String name="";
    name=regexFind("/([^/]+)\\.[^/.]+$", path, 1);
    return name;
  }
  
  /*
   * Returns extension from the filename
   */
  public static String getExt(String file){
    String ext;
    ext=regexFind("\\.([^.]+)$", file, 1);
    ext=ext.toLowerCase();
    return ext;
  }
  
// ----------------------------------------------------- logging -----------------------------------------------------
  
  /*
   * Outputs additional information to the Output textarea
   */
  public static void log(String text){
    JTextArea out=window.taOutput;
    String prev=out.getText();
    out.setText(prev+text);
  }
  
  /*
   * Clears the log textarea
   */
  public static void clearLog(){
    JTextArea out=window.taOutput;
    out.setText("");
  }
  
  /*
   * Formats time value according to the format
   */
  public static String formatTime(int time, String format){
    Formatter timeFormat=new Formatter();
    timeFormat.format(format, (float) time/1000);
    return timeFormat.toString();
  }
  
// ----------------------------------------------------- JSON serialization -----------------------------------------------------
  
  /*
   * Gets JSON string from an object (array, array list, hash map)
   */
  public String encodeJSON(Object collection){
    Gson gson=new Gson();
    return gson.toJson(collection);
  }
  
  /*
   * Gets HashMap from JSON string
   */
  public HashMap decodeJSON(String json){
    HashMap fields;
    fields=new Gson().fromJson(json, HashMap.class);
    return fields;
  }
  
// ----------------------------------------------------- other -----------------------------------------------------
  
  /*
   * Moves dialog window to the right boundary of the main window
   */
  public void stickWindow(JFrame to, Window what){
    int x=to.getX(), 
        y=to.getY(),
        w=to.getWidth();
    
    if(what!=null)
      what.setLocation(x+w,y);
  }
  
  /*
   * Sets small and large icons for the window
   */
  public static void setWindowIcon(JFrame frame){
    List<Image> icons = new ArrayList<Image>();
    icons.add(new ImageIcon("icons/icon16.png").getImage());
    icons.add(new ImageIcon("icons/icon32.png").getImage());
    frame.setIconImages(icons);
  }
  
// ------------------------------------ old ------------------------------------
  
  /*
   * for small files use readWholeFile() instead 
   * Gets the template for the tree view (jsTree plugin)
   */
  public static String readFile(String file) {
    String doc = "", line = null;
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(file));               // read by lines
      while ((line = br.readLine()) != null) {
        doc += line+nl;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return doc;
  }
  
}
