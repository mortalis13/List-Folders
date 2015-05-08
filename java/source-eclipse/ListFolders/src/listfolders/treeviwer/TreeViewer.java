package listfolders.treeviwer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import listfolders.includes.Functions;
import listfolders.includes.tree.DirNode;
import listfolders.includes.tree.FileNode;
import listfolders.includes.tree.TreeNode;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TreeViewer{
  
  private ArrayList<TreeNode> treeData;
  private TreeViewerWindow window;
  
  private long prevTime=0;
  private int totalTime=0;
  
  String rootName;
  String path;
  
  public ArrayList<String> iconNames;
  
// ------------------------------------------- Tree worker class -------------------------------------------
  
  class TreeWorker extends SwingWorker<Void, Integer> implements PropertyChangeListener{

  // ========== main functions ==========

    public Void doInBackground() {
      treeData=getTree();
      return null;
    }
    
    public void done(){
      loadTreeIntoWindow();
    }
    
  // ========== additional functions ==========
    
    /*
     * Gets JSON string from file and then recursive tree structure from this string
     */
    private ArrayList<TreeNode> getTree(){
      ArrayList<TreeNode> tree;
      String json;
      
      json=Functions.readWholeFile(path);
      tree=decodeJSONTree(json);
      
      return tree;
    }
    
    /*
     * Parses JSON string and returns tree structure
     */
    private ArrayList<TreeNode> decodeJSONTree(String json){
      ArrayList<TreeNode> tree;
      JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
      tree=convertJsonToTree(jsonArray);
      return tree;
    }
    
    /*
     * Recursively walks the JSON array obtained from the JSON string
     * and converts its nodes to correct DirNode and FileNode objects
     */
    private ArrayList<TreeNode> convertJsonToTree(JsonArray jsonArray){
      ArrayList<TreeNode> tree=new ArrayList<TreeNode>();
      Gson gson=new Gson();
      
      for(JsonElement jsonElem:jsonArray){
        JsonObject json=(JsonObject) jsonElem;
        boolean isDir=json.has("children");
        
        if(isDir){
          DirNode dir=gson.fromJson(json, DirNode.class);
          tree.add(dir);
          
          JsonArray children=(JsonArray) json.get("children");
          dir.children=convertJsonToTree(children);
        }
        else{
          FileNode file=gson.fromJson(json, FileNode.class);
          tree.add(file);
          iconNames.add(getIconFromNode(file.icon));
        }
      }
      
      return tree;
    }
    
  // ========== not-used stats functions ==========
    
    protected void process(List<Integer> list){
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
    }
    
    private void logStats(String currentDir, int progress){
      long currentTime=System.currentTimeMillis();
      int time=(int) (currentTime-prevTime);
      prevTime=currentTime;
      
      String timeString=formatTime(time, "Time: %.2f s");
      totalTime+=time;
    }
    
    private void updateStatusBar(String type, String currentDir){
    }
    
    private String formatTime(int time, String format){
      Formatter timeFormat=new Formatter();
      timeFormat.format(format, (float) time/1000);
      return timeFormat.toString();
    }
    
    // time measure template
    private void showProcessTime(){
      long time1,time2;
      time1=System.currentTimeMillis();
      // json=readJSON(file);
      time2=System.currentTimeMillis();
      System.out.println("Read time: "+formatTime((int) (time2-time1), "%.2f s") );
    }
  }
  
// ----------------------------------------- TreeViewer class methods -----------------------------------------  
  
  public TreeViewer(){
    window=TreeViewerWindow.window;
    iconNames=new ArrayList<String>();
  }
  
  /*
   * Runs the background thread to load the tree data 
   * which then passes the control to the loadTreeIntoWindow() function to show the tree
   */
  public void showTree(){
    path=window.tfPath.getText();
    TreeWorker worker=new TreeWorker();
    worker.addPropertyChangeListener(worker);
    worker.execute();
  }
  
  /*
   * Creates the root node, creates new tree and includes it into the scroll pane
   */
  private void loadTreeIntoWindow(){
    if(treeData!=null){
      DirectoryTree tree=window.tree;
      
      rootName=Functions.getNameFromPath(path);
      DirNode root=new DirNode(rootName, treeData);
      
      tree=new DirectoryTree(iconNames);
      tree.setData(root);
      tree.setTreeOptions(tree);
      
      window.scrollPane.setViewportView(tree);
    }
  }
  
  /*
   * Gets icon name and extension from the full icon path
   */
  public static String getIconFromNode(String iconPath){
    String icon=null;
    
    Pattern pat=Pattern.compile("/([^/]+\\.[^/.]+)$");
    Matcher mat=pat.matcher(iconPath);
    
    if(mat.find()){
      icon=mat.group(1);                                      
    }
    else{
      icon="file.png";
    }
    
    return icon;
  }
  
}
