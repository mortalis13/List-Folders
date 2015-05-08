package listfolders.includes;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

  @Override
  public int compare(File o1, File o2) {
    String name1, name2;
    name1=o1.getName();
    name2=o2.getName();
    return name1.compareTo(name2);
  }

}
