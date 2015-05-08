package listfolders.includes;

import javax.swing.Action;
import javax.swing.KeyStroke;

public class Shortcut {
  
  public KeyStroke stroke;
  public Action action;
  
  public Shortcut(KeyStroke stroke, Action action){
    this.stroke=stroke;
    this.action=action;
  }
  
}
