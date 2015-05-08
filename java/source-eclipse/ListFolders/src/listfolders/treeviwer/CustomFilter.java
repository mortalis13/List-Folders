package listfolders.treeviwer;
import java.io.File;

import javax.swing.filechooser.FileFilter;

import listfolders.includes.Functions;


public class CustomFilter extends FileFilter {
  
  private String[] allowedExt={"json"};
  private String description="JSON";

  @Override
  public String getDescription() {
    return description;
  }
  
  /*
   * Accepts files with specified extensions
   */
  @Override
  public boolean accept(File f) {
    if (f.isDirectory())
      return true;
    
    String name=f.getName();
    String ext=Functions.getExt(name);
    if(ext.equals(allowedExt[0]))
      return true;
    
    return false;
  }

}
