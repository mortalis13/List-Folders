package listfolders.includes;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import listfolders.ListFoldersMain;
import listfolders.includes.tree.DirNode;
import listfolders.includes.tree.FileNode;
import listfolders.includes.tree.TreeNode;

import com.google.gson.Gson;

public class ScanDirectory {
  public Functions fun;
  public ListFoldersMain window;
  public ScanWorker worker;
  
  public String path;

  public String text="";
  public String markup="";

  public ArrayList<String> textArray;
  public ArrayList<String> markupArray;
  public ArrayList<TreeNode> jsonArray;
  
  public ArrayList<String> filterExt;
  public ArrayList<String> excludeExt;
  public ArrayList<String> filterDir;
  
  boolean doExportText;
  boolean doExportMarkup;
  boolean doExportTree;
  
  String exportName;
  
  int dirCount=0;
  int rootDirCount=0;
  int longestDirName=0;
  boolean scanCanceled=false;
  long prevTime=0;
  int totalTime=0;

  String nl = "\n";
  String pad = "  ";
  String iconsPath="./lib/images/";
  
  String[] exts={                                       // sets of extensions for tree view icons (stored in lib/images)
    "chm", "css", "djvu", "dll", "doc", 
    "exe", "html", "iso", "js", "msi", 
    "pdf", "php", "psd", "rar", "txt", 
    "xls", "xml", "xpi", "zip",
  };
  
  String[] imageExts={
    "png", "gif", "jpg", "jpeg", "tiff", "bmp",
  };

  String[] musicExts={
    "mp3", "wav", "ogg", "alac", "flac",
  };

  String[] videoExts={
    "mkv", "flv", "vob", "avi", "wmv",
    "mov", "mp4", "mpg", "mpeg", "3gp",
  };
  
  String[] codeExts ={
    "c", "cpp", "cs", "java",
  };
  
// ----------------------------------- Scan worker class ----------------------------------- 
  
  class ScanWorker extends SwingWorker<Void, Integer> implements PropertyChangeListener{

    /*
     * Runs directory processing in a background thread
     */
    public Void doInBackground() {
      setProgress(0);
      Functions.clearLog();
      prevTime=System.currentTimeMillis();
      window.bScanDir.setText("Stop");
      window.bScanDir.setActionCommand("stop");
      window.taOutput.setText("");
      
      jsonArray = fullScan(new File(path), 0);
      
      return null;
    }
    
    /*
     * Exports and outputs results of the scanning
     */
    public void done(){
      if(doExportText) exportText();
      if(doExportMarkup) exportMarkup();
      if(doExportTree) exportTree();
      
      window.bScanDir.setText("Scan Directory");
      window.bScanDir.setActionCommand("scan");
      updateStatusBar("finish", null);
      
      window.progressBar.setValue(100);
      
      if(text.length()==0 && doExportText) 
        log("No Data!");
      
      log(nl+"----------------------------"+nl);
      String time=Functions.formatTime(totalTime, "(time: %.2f s)");
      log("Total time: "+time+nl);
      
      if(scanCanceled){
        window.progressBar.setValue(0);
      }
    }
    
    /*
     * Processes intemediate results and send the progress value to the propertyChange() listener
     */
    protected void process(List<Integer> list){
      int processedCount=list.get(0);
      setProgress(Math.min(processedCount, 100));
    }
    
    /*
     * Processes property change event and sets the progress bar value
     */
    public void propertyChange(PropertyChangeEvent evt) {
      if ("progress" == evt.getPropertyName() && !isDone()) {
        int progress = (Integer) evt.getNewValue();
        window.progressBar.setValue(progress);
      } 
    }
    
// ----------------------------------- additional ScanWorker functions ----------------------------------- 
    
    /*
     * Recursively scans all subdirectories
     */
    public ArrayList<TreeNode> fullScan(File dir, int level) {
      if(scanCanceled) return null;
      
      ArrayList<TreeNode> json, res;
      ArrayList<File> list;
      File[] data;
      String pad;

      json = new ArrayList<TreeNode>();                               // json is recursive tree structure needed for the jsTree plugin
      
      data = dir.listFiles();                                             // get string list of files in the current level directory
      
      if(data==null){
        data=readFromDirStream(dir);
      }
      
      if(level==0)
        list = prepareInitialData(data);
      else
        list = prepareData(data, dir);
      pad = getPadding(level);
      
      for (File file : list) {
        TreeNode node;
        String name=file.getName();
        
        if (file.isDirectory()) {                       // directories
          String currentDir = "[" + name + "]";
          
          if(level==0){
            updateStatusBar("scanning", currentDir);
          }
          
          if(doExportText)
            text+=pad+currentDir+nl;
          if(doExportMarkup)
            markup+=pad+wrapDir(currentDir)+nl;

          res = fullScan(file, level + 1);                      // recursive scan
          if(res==null) return null;
          
          if(doExportTree){
            node = new DirNode(name, res);
            json.add(node);
          }
          
          if(level==0){
            dirCount++;
            int progress=(int) ((float) dirCount/rootDirCount*100);                // send stats (progress)
            publish(progress);
            
            logStats(currentDir, progress);
          }
        } else {                                                // files
          String currentFile = name;
          
          if(doExportText)
            text+=pad+currentFile+nl; 
          if(doExportMarkup)
            markup+=pad+wrapFile(currentFile)+nl;
          
          if(doExportTree){
            node = new FileNode(name, getIcon(name));
            json.add(node);
          }
        }
      }

      return json;
    }
    
    /*
     * Calculates and outputs time between folders processing
     */
    private void logStats(String currentDir, int progress){
      long currentTime=System.currentTimeMillis();
      int time=(int) (currentTime-prevTime);
      prevTime=currentTime;
      
      String timeString=Functions.formatTime(time, "Time: %.2f s");
      
      int len=currentDir.length();
      int dif=longestDirName-(len-2);
      
      String spaces="";
      for(int i=0;i<dif;i++)                                          // align next text columns in the log with the longest folder name
        spaces+=" ";
      
      totalTime+=time;
      
      log(currentDir+spaces+"\t "+timeString);
      log("\t Dir: "+dirCount+"/"+rootDirCount+" \t progress: "+progress+"% \n");
      
      System.out.print(currentDir+spaces+"\t "+timeString);
      System.out.println("\t Dir: "+dirCount+"/"+rootDirCount+" \t progress: "+progress+"%");
    }
    
    /*
     * Sets text in the window status bar
     */
    private void updateStatusBar(String type, String currentDir){
      String text="";
      
      switch(type){
        case "scanning":
          text="Scanning: "+currentDir;
          break;
        case "finish":
          String time=Functions.formatTime(totalTime, "(time: %.2f s)");
          text="Scanning finished "+time;
          break;
      }
      window.lStatus.setText(text);
    }
    
  }
  
// --------------------------------------------- Constructor --------------------------------------------- 

  public ScanDirectory() {
    String filterExtText, excludeExtText, filterDirText;
    fun=new Functions();
    window=ListFoldersMain.window;
    
    textArray = new ArrayList<String>();
    markupArray = new ArrayList<String>();
    
    HashMap fields=fun.getFieldsMap();
    
    path=(String)fields.get("path");
    path=Functions.formatPath(path);
    window.tfPath.setText(path);
    
    filterExtText=(String)fields.get("filterExt");
    excludeExtText=(String)fields.get("excludeExt");
    filterDirText=(String)fields.get("filterDir");
    
    doExportText=(boolean)fields.get("doExportText");
    doExportMarkup=(boolean)fields.get("doExportMarkup");
    doExportTree=(boolean)fields.get("doExportTree");
    
    exportName=(String)fields.get("exportName");
    
    filterExt=getFilters(filterExtText);
    excludeExt=getFilters(excludeExtText);
    filterDir=getFilters(filterDirText);
  }

  public void startScan(){                                  // << Start point >>
    scanCanceled=false;
    worker=new ScanWorker();
    worker.addPropertyChangeListener(worker);
    worker.execute();
  }
  
  public void stopScan(){
    scanCanceled=true;
    worker.cancel(true);
  }
  
  // --------------------------------------------------- helpers ---------------------------------------------------
  
  /*
   * Gets filelist using nio.DirectoryStream
   * Used in case File.listFiles returns null
   */
  private File[] readFromDirStream(File path){
    log("  Using DirectoryStream for: \""+path+"\"\n");
    
    Path dir = path.toPath();
    ArrayList<File> list=new ArrayList<File>();
    File[] fileList;

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (Path file: stream) {
        list.add(new File(file.toString()));
      }
    } catch (IOException | DirectoryIteratorException e) {
      log("Exception::"+e.getCause().toString()+"\n");
    }
    
    fileList=new File[list.size()];
    fileList=list.toArray(fileList);
    return fileList;
  }
  
  /*
   * Filters files and folders
   * Sorts by name and directories-first order
   */
  private ArrayList<File> prepareData(File[] data, File dir) {
    ArrayList<File> folders = new ArrayList<File>(), 
    files = new ArrayList<File>(), list;

    for (File file : data) {
      String name=file.getName();
      
      if (file.isDirectory()) {                        // add directories
        folders.add(file);
      } else if (filterFile(name)) {                   // filter files and add
        files.add(file);
      }
    }
    
    list = getList(folders, files);
    return list;
  }
  
  /*
   * Gets files list for the root directory
   * Filters top-level directories, 
   * gets the top folders count and calculates the longest directory name for the thread stats
   */
  private ArrayList<File> prepareInitialData(File[] data) {
    ArrayList<File> folders = new ArrayList<File>(), 
    files = new ArrayList<File>(), list;
    int longest=0;
    
    for (File file : data) {
      String name=file.getName();
      
      if (file.isDirectory() == true) {                // add directories
        if(filterDir.size()!=0 && !filterDirectory(name)) continue;                 // filter root directories
        folders.add(file);
        if(name.length()>longest)                                                   // get the longest dir name
          longest=name.length();
      } else if (filterFile(name)) {                   // filter files and add
        files.add(file);
      }
    }
    
    rootDirCount=folders.size();
    longestDirName=longest;

    list = getList(folders, files);
    return list;
  }

  /*
   * Merge folders and files arrays
   */
  private ArrayList<File> getList(ArrayList<File> folders, ArrayList<File> files) {
    ArrayList<File> list = new ArrayList<File>();
    Collections.sort(folders);
    Collections.sort(files);
    list.addAll(folders);
    list.addAll(files);
    return list;
  }
  
  /*
   * Replaces strings from the tree template (strings format: '_string_') with the 'replacement' text
   */
  private String replaceTemplate(String tmpl, String replacement, String text){
    text=text.replace(tmpl, replacement);
    return text;
  }
  
  /*
   * Outputs padding spaces for text output depending on nesting level
   */
  private String getPadding(int level) {
    String resPad = "";
    for (int i = 0; i < level; i++) {
      resPad += pad;
    }
    return resPad;
  }
  
  /*
   * Returns icon path for the tree view
   */
  private String getIcon(String file){
    String ext, icon, path, iconExt;
    boolean useDefault=true;
    
    ext="";
    icon="jstree-file";
    path=iconsPath;
    iconExt=".png";
    
    Pattern pat=Pattern.compile("\\.([\\w]+)$");                  // find ext without dot
    Matcher mat=pat.matcher(file);
    
    if(!mat.find()) return icon;                                // first run find() then get results
    
    ext=mat.group(1);                                            // string result
    
    if(useDefault){                                             // process different types of extensions
      for(String item : exts){
        if(item.equals(ext)){
          icon=path+item+iconExt;
          useDefault=false;
          break;
        }
      }
    }
    
    if(useDefault){
      for(String item : imageExts){
        if(item.equals(ext)){
          icon=path+"image"+iconExt;
          useDefault=false;
          break;
        }
      }
    }
    
    if(useDefault){
      for(String item : musicExts){
        if(item.equals(ext)){
          icon=path+"music"+iconExt;
          useDefault=false;
          break;
        }
      }
    }
    
    if(useDefault){
      for(String item : videoExts){
        if(item.equals(ext)){
          icon=path+"video"+iconExt;
          useDefault=false;
          break;
        }
      }
    }
    
    if(useDefault){
      for(String item : codeExts){
        if(item.equals(ext)){
          icon=path+"code"+iconExt;
          useDefault=false;
          break;
        }
      }
    }
    
    return icon;
  }
  
  private void log(String text){
    Functions.log(text);
  }
  
// --------------------------------------------------- filters ---------------------------------------------------
  
  /*
   * Cleans, trims and checks filters for emptiness
   */
  private ArrayList<String> getFilters(String filter) {
    ArrayList<String> list=new ArrayList<String>();
    String[] elements;
    filter=filter.trim();
    
    if(filter.length()!=0){
      elements=filter.split("\n");
      Collections.addAll(list, elements);
      
      for(int i=0;i<list.size();i++){
        String item=list.get(i);
        list.set(i,item.trim());
      }
    }
    
    return list;
  }
  
  /*
   * Filters file extensions and returns true if the file will be included in the output
   * If exclude filter is not empty ignores the include filter
   */
  private boolean filterFile(String file) {
    if(excludeExt.size()!=0){
      for(String ext:excludeExt){
        if(Functions.matches("\\."+ext+"$",file))
          return false;
      }
      return true;
    }
    
    if(filterExt.size()==0) return true;
    for(String ext:filterExt){
      if(Functions.matches("\\."+ext+"$",file))
        return true;
    }
    return false;
  }
  
  /*
   * Uses form filter to filter directories from the first scanning level
   */
  private boolean filterDirectory(String dir) {
    for(String filter:filterDir){
      if(filter.equals(dir))
        return true;
    }
    return false;
  }
  
  /*
   * Gets text for the tree template
   */
  private String getFiltersText() {
    String filterExtText="", excludeExtText="", filterDirText="", filters="";
    
    if(filterExt.size()!=0){
      filterExtText=Functions.join(filterExt, ",");
    }
    if(excludeExt.size()!=0){
      excludeExtText=Functions.join(excludeExt, ",");
    }
    if(filterDir.size()!=0){
      filterDirText=Functions.join(filterDir, ",");
    }
    
    filters="Files include ["+filterExtText+"]";
    filters+=", Files exclude ["+excludeExtText+"]";
    filters+=", Directories ["+filterDirText+"]";
    
    return filters;
  }
  
// --------------------------------------------------- wrappers ---------------------------------------------------

  private String wrapDir(String dir) {
    return "<span class=\"directory\">" + dir + "</span>";
  }

  private String wrapFile(String file) {
    return "<span class=\"file\">" + file + "</span>";
  }

  private String wrapMarkup(String markup) {
    String res = "<pre>" + nl + markup + "</pre>";
    res = wrapDocument(res);
    return res;
  }

  private String wrapDocument(String markup) {
    return "<meta charset=\"utf-8\">" + nl + markup;
  }

// --------------------------------------------------- exports ---------------------------------------------------

  /*
   * Exports text to a .txt file in 'export/text'
   */
  private void exportText() {
    File file;
    String exportPath, fileName, ext, text;

    exportPath = "export/text/";
    ext=".txt";
    fileName = getExportName(ext);
    fileName = exportPath + fileName;
    
    text=this.text;
    text=Functions.fixEncoding(text);
    
    Functions.writeFile(fileName,text);
  }
  
  /*
   * Exports HTML markup to a .hmtl file in 'export/markup'
   */
  private void exportMarkup() {
    File file;
    String exportPath, fileName, ext, markup;

    exportPath = "export/markup/";
    ext=".html";
    fileName = getExportName(ext);
    fileName = exportPath + fileName;
    markup = wrapMarkup(this.markup);
    markup=Functions.fixEncoding(markup);

    Functions.writeFile(fileName, markup);
  }
  
  /*
   * Exports .json and .html files to the 'export/tree'
   * The .html file can be used directly to view the tree
   * The jsTree plugin must be in the 'tree/lib'
   *
   * The method gets the .html template from 'templates/tree.html', 
   * replaces template strings with the current data and create new .html in the 'exports/tree'
   * Then creates .json in the 'exports/tree/json' which is read by the script in the exported .html page
   */
  private void exportTree() {
    String tmpl, doc, treeName, exportPath, jsonFolder, 
    jsonPath, exportDoc, exportJSON;
    String filters;
    String jsonFile, htmlFile;
    
    Gson gson = new Gson();
    String json = gson.toJson(jsonArray);
    json=Functions.fixEncoding(json);

    treeName=getExportName(null);                                         // get name
    
    tmpl="templates/tree.html";
    exportPath="export/tree/";
    jsonFolder="json/";
    jsonPath=exportPath+jsonFolder;
    
    exportDoc=treeName+".html";
    exportJSON=treeName+".json";
    
    doc=Functions.readWholeFile(tmpl);                                               // process template
    doc=replaceTemplate("_jsonPath_", jsonFolder+exportJSON, doc);
    doc=replaceTemplate("_Title_", "Directory: "+treeName, doc);
    doc=replaceTemplate("_FolderPath_", "Directory: "+path, doc);
    
    filters=getFiltersText();
    doc=replaceTemplate("_Filters_", "Filters: "+filters, doc);
    
    htmlFile=exportPath+exportDoc;                                        // get paths
    jsonFile=jsonPath+exportJSON;
      
    Functions.writeFile(htmlFile, doc);                                             // write results
    Functions.writeFile(jsonFile, json);
  }
  
  /*
   * Returns the name that will be used to export 
   * text, markup and tree views of the directory structure
   */
  private String getExportName(String ext){
    boolean useCurrentDir=true;
    String exportName, name;
    
    exportName="no-name";
    
    if(this.exportName.length()!=0){
      exportName=this.exportName;
      useCurrentDir=false;
    }
    
    if(useCurrentDir){
      Pattern pat=Pattern.compile("/([^/]+)$");
      Matcher mat=pat.matcher(path);
      
      if(mat.find()){
        exportName=mat.group(1);
      }
    }
    
    name=exportName;
    if(ext!=null) name+=ext;
    
    return name;
  }

}
