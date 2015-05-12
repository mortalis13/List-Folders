using Tree = ListFolders.Includes.Tree;
using ListFolders.Includes.Tree;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;
using System.Threading;
using System.Web.Script.Serialization;
using System.Windows.Forms;

namespace ListFolders.Includes {
  public class ScanDirectory {
    MainForm form;
    Thread worker;

    public string path;

    public string text="";
    public List<Tree.TreeNode> jsonArray;

    const string nl = "\r\n";
    const string pad="    ";
    const string iconsPath="./lib/images/";
    
    public List<string> filterExt;
    public List<string> excludeExt;
    public List<string> filterDir;
    
    bool doExportText;
    bool doExportTree;
    
    string exportName="";
    
    int dirCount = 0;
    int rootDirCount = 0;
    public static bool scanCanceled = false;
    long prevTime = 0;
    int totalTime = 0;
    
    string[] exts={                                       // sets of extensions for tree view icons (stored in lib/images)
      "chm", "css", "djvu", "dll", "doc", 
      "exe", "html", "iso", "js", "msi", 
      "pdf", "php", "psd", "rar", "txt", 
      "xls", "xml", "xpi", "zip",
    };
    
    string[] imageExts={
      "png", "gif", "jpg", "jpeg", "tiff", "bmp",
    };

    string[] musicExts={
      "mp3", "wav", "ogg", "alac", "flac",
    };

    string[] videoExts={
      "mkv", "flv", "vob", "avi", "wmv",
      "mov", "mp4", "mpg", "mpeg", "3gp",
    };

    string[] codeExts ={
      "c", "cpp", "cs", "java",
    };

// --------------------------------------------- Constructor --------------------------------------------- 

    public ScanDirectory(MainForm form) {
      string filterExtText, excludeExtText, filterDirText;
      this.form = MainForm.form;
      
      IDictionary fields=Functions.getFieldsMap();

      path=(string) fields["path"];
      path=Functions.formatPath(path);
      form.tbPath.Text=path;
      
      filterExtText=(string) fields["filterExt"];
      excludeExtText=(string) fields["excludeExt"];
      filterDirText=(string) fields["filterDir"];
      
      doExportText=(bool) fields["doExportText"];
      doExportTree=(bool) fields["doExportTree"];
      exportName=(string) fields["exportName"];
      
      filterExt=getFilters(filterExtText);
      excludeExt=getFilters(excludeExtText);
      filterDir=getFilters(filterDirText);
    }

// --------------------------------------------- Main processing --------------------------------------------- 

    public void startScan(){                                         // << Start point >>
      scanCanceled=false;
      worker = new Thread(backgroundProcess);
      worker.Start();
    }

    public void stopScan(){
      scanCanceled=true;
      // worker.Abort();
    }

    /*
     * Thread runner
     */
    void backgroundProcess() {
      prepareProcessing();
      jsonArray = fullScan(path, 0);
      done();
    }
    
    /*
     * Actions after main scanning
     * Still runs in the thread but after the whole scanning
     */
    private void done(){
      if(doExportText) exportText();
      if(doExportTree) exportTree();
      
      log(nl+"----------------------------"+nl);
      string time=Functions.formatTime(totalTime, "(time: {0:f2} s)");
      log("Total time: "+time+nl);
      
      Functions.setProgress(100);
      updateStatusBar("finish");
      form.bScanDir.Text="Scan Directory";
      MainForm.startScan=true;
      
      if(scanCanceled){
        Functions.setProgress(0);
        updateStatusBar("cancel");
        log(nl+"Scanning canceled");
      }
    }
    
    /*
     * Sets progress bar, gets initial time, changes button label
     */
    void prepareProcessing(){
      Functions.setProgress(0);
      Functions.clearLog();
      prevTime=Functions.ms();
      form.bScanDir.Text="Stop";
      MainForm.startScan=false;
    }

    /*
     * Recursively scans all subdirectories
     */
    private List<Tree.TreeNode> fullScan(string dir, int level) {
      if(scanCanceled) return null;

      List<Tree.TreeNode> json, res;
      Tree.TreeNode node;
      DirectoryInfo list;
      string pad;

      json = new List<Tree.TreeNode>();
      
      list = new DirectoryInfo(dir);                              // get all dir/files list
      pad = getPadding(level);

      DirectoryInfo[] dirList=list.GetDirectories();              // only dirs
      if (level == 0) {
        rootDirCount = getDirCount(dirList.Length);
      }
      
      foreach (DirectoryInfo nextDir in dirList) {                // ||= loop directories =||
        string name=nextDir.Name;
        string currentDir = "[" + name + "]";

        if(level==0){
          if(!filterDirectory(name)) continue;
          updateStatusBar("scanning", currentDir);
        } 

        if(doExportText)
          text += pad + currentDir + nl;                          // accumulate text structure
        
        res=fullScan(nextDir.FullName, level+1);                  // recursive point
        if(res==null) return null;
        
        node = new DirNode(name, res);
        json.Add(node);                                           // accumulate recursive tree structure

        if (level == 0) {                                         // update progress status for scanned top-level directories
          dirCount++;
          int progress=(int) ((float) dirCount/rootDirCount*100);
          logStats(currentDir, progress);
          Functions.setProgress(progress);
        }
      }

      foreach (FileInfo nextFile in list.GetFiles()) {                // ||= loop files =||
        string name=nextFile.Name;
        if(!filterFile(name)) continue;
        
        string currentFile = name;
        if (doExportText)
          text += pad + currentFile + nl;
        
        node = new FileNode(name, getIcon(name));
        json.Add(node);
      }
      
      return json;
    }
    
// --------------------------------------------------- logging ---------------------------------------------------
    
    /*
     * Calculates and outputs time between folders processing
     */
    private void logStats(string currentDir, int progress){
      long currentTime=Functions.ms();

      int time=(int) (currentTime-prevTime);
      prevTime=currentTime;
      
      string timeString=Functions.formatTime(time, "Time: {0:f2} s ");
      totalTime+=time;
      
      log(currentDir+"\n  "+timeString);
      log("\t Dir: "+dirCount+"/"+rootDirCount+" \t progress: "+progress+"% \n");
    }
    
    /*
     * Sets text in the window status bar
     */
    private void updateStatusBar(string type, string currentDir=""){
      string text="";
      
      switch(type){
        case "scanning":
          text="Scanning: "+currentDir;
          break;
        case "finish":
          string time=Functions.formatTime(totalTime, "(time: {0:f2} s)");
          text="Scanning finished "+time;
          break;
        case "cancel":
          text="Scanning canceled";
          break;
      }
      form.lStatus.Text=text;
    }
    
    /*
     * Uses shorter version of the same function in the Functions class
     */
    private void log(string text){
      Functions.log(text);
    }
    
    /*
     * Gets top-level count of directories to be scanned
     */
    private int getDirCount(int totalCount){
      int filteredCount=filterDir.Count;
      if(filteredCount==0) return totalCount;
      return filteredCount;
    }
    
// --------------------------------------------------- helpers ---------------------------------------------------
    
    /*
     * Replaces strings from the tree template (strings format: '_string_') with the 'replacement' text
     */
    private string replaceTemplate(string tmpl, string replacement, string text){
      text=text.Replace(tmpl, replacement);
      return text;
    }
    
    /*
     * Outputs padding spaces for text output depending on nesting level
     */
    private string getPadding(int level) {
      string resPad = "";
      for (int i = 0; i < level; i++) {
        resPad += pad;
      }
      return resPad;
    }
    
    /*
     * Returns icon path for the tree view
     */
    private string getIcon(string file){
      string ext, icon, path, iconExt;
      bool useDefault=true;
      
      ext="";
      icon="jstree-file";
      path=iconsPath;
      iconExt=".png"; 
      
      ext=Functions.regexFind(@"\.([\w]+)$", file);
      if(ext==null) return icon;
      
      if(useDefault){                                             // extensions for known types
        foreach(string item in exts){
          if(item.Equals(ext)){
            icon=path+item+iconExt;
            useDefault=false;
            break;
          }
        }
      }
      
      if(useDefault){                                             // general extensions for "images"
        foreach(string item in imageExts){
          if(item.Equals(ext)){
            icon=path+"image"+iconExt;
            useDefault=false;
            break;
          }
        }
      }
      
      if(useDefault){                                             // general extensions for "music"
        foreach(string item in musicExts){
          if(item.Equals(ext)){
            icon=path+"music"+iconExt;
            useDefault=false;
            break;
          }
        }
      }
      
      if(useDefault){                                             // general extensions for "video"
        foreach(string item in videoExts){
          if(item.Equals(ext)){
            icon=path+"video"+iconExt;
            useDefault=false;
            break;
          }
        }
      }

      if (useDefault) {                                             // general extensions for "code"
        foreach (string item in codeExts) {
          if (item.Equals(ext)) {
            icon = path + "code" + iconExt;
            useDefault = false;
            break;
          }
        }
      }
      
      return icon;
    }
    
// --------------------------------------------------- filters ---------------------------------------------------
    
    /*
     * Filters file extensions and returns true if the file will be included in the output
     * If exclude filter is not empty ignores the include filter
     */
    private bool filterFile(string file) {
      if(excludeExt.Count!=0){
        foreach(string ext in excludeExt){
          if(Functions.matches(@"\."+ext+"$",file))
            return false;
        }
        return true;
      }
      
      if(filterExt.Count==0) return true;
      foreach(string ext in filterExt){
        if(Functions.matches(@"\."+ext+"$",file))
          return true;
      }
      return false;
    }
    
    /*
     * Uses form filter to filter directories from the first scanning level
     */
    private bool filterDirectory(string dir) {
      if(filterDir.Count==0) return true;
      
      foreach(string filter in filterDir){
        if(filter.Equals(dir))
          return true;
      }
      return false;
    }
    
    /*
     * Cleans, trims and checks filters for emptiness
     */
    private List<string> getFilters(string filter) {
      List<string> list=new List<string>();
      string[] elements;
      filter=filter.Trim();
      
      if(filter.Length!=0){
        elements=filter.Split('\n');
        foreach(string s in elements){
          list.Add(s.Trim());
        }
      }      
      
      return list;
    }    
    
    /*
     * Gets text for the tree template
     */
    private string getFiltersText() {
      string filterExtText="", excludeExtText="", filterDirText="", filters="";
      
      if(filterExt.Count!=0){
        filterExtText = string.Join(",", filterExt);
      }
      if(excludeExt.Count!=0){
        excludeExtText = string.Join(",", excludeExt);
      }
      if(filterDir.Count!=0){
        filterDirText = string.Join(",", filterDir); 
      }
      
      filters="Files include ["+filterExtText+"]";
      filters+=", Files exclude ["+excludeExtText+"]";
      filters+=", Directories ["+filterDirText+"]";
      
      return filters;
    }
    
// --------------------------------------------------- exports ---------------------------------------------------
    
    /*
     * Exports text to a .txt file in 'export/text'
     */
    private void exportText() {
      String exportPath, fileName, ext, text;

      exportPath = Functions.getPath(@"export\text\");
      ext=".txt";
      fileName = getExportName(ext);
      fileName = exportPath + fileName;
      
      text=this.text;
      // text=Functions.fixEncoding(text);
      
      Functions.writeFile(fileName,text);
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
      string tmpl, doc, json, treeName, 
      exportPath, jsonFolder, jsonPath, 
      exportDoc, exportJSON;
      string filters;
      string jsonFile, htmlFile;
      
      json = Functions.encodeJSON(jsonArray);
      if (json == null) return;

      treeName=getExportName(null);                                       // get name
      
      tmpl=Functions.getPath(@"templates\tree.html");
      exportPath=Functions.getPath(@"export\tree\");
      jsonFolder="json/";                                                   // should be "/" because "\" prints as control symbol
      jsonPath=exportPath+jsonFolder;
      
      exportDoc=treeName+".html";
      exportJSON=treeName+".json";

      try {
        doc=Functions.readFile(tmpl);                                        // process template
        if (doc == null) {
          throw new Exception("No \"templates/tree.html\" file");
        }
      }
      catch (Exception e) {
        MessageBox.Show(e.Message, "Error");
        return;
      }

      doc=replaceTemplate("_jsonPath_", jsonFolder+exportJSON, doc);
      doc=replaceTemplate("_Title_", "Directory: "+treeName, doc);
      doc=replaceTemplate("_FolderPath_", "Directory: "+path, doc);
      
      filters=getFiltersText();
      doc=replaceTemplate("_Filters_", "Filters: "+filters, doc);
      
      htmlFile=exportPath+exportDoc;                                        // get paths
      jsonFile=jsonPath+exportJSON;
        
      Functions.writeFile(htmlFile, doc);                                   // write results
      Functions.writeFile(jsonFile, json);
    }
    
    /*
     * Returns the name that will be used to export 
     * text, markup and tree views of the directory structure
     */
    private string getExportName(string ext){
      bool useCurrentDir=true;
      string exportName, name, res;
      
      exportName="no-name";
      
      if(this.exportName.Length!=0){
        exportName=this.exportName;
        useCurrentDir=false;
      }
      
      if(useCurrentDir){
        res = Functions.regexFind(@"\\([^\\]+)\\?$", path);
        if(res!=null)
          exportName=res;
      }
      
      name=exportName;
      if(ext!=null) name+=ext;
      
      return name;
    }
      
  }

}
