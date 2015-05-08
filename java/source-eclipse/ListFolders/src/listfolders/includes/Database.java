package listfolders.includes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Database {
  private Connection conn;
  private String options_table="options";
  private String config_table="config";
  
  Statement st;
  PreparedStatement prep;
  ResultSet rs;
  
  String sql, table;
  
  public Database(){
    if(conn==null)
      conn=createConnection();
  }
  
  private Connection createConnection(){
    Connection conn = null;
    String driver="com.mysql.jdbc.Driver";
    String url="jdbc:mysql://localhost/list_folders_java";
    
    try {
      Class.forName(driver);
      conn = DriverManager.getConnection(url,"root","");
    } catch (Exception e) {
      System.out.println("No Connection: createConnection()");
      return null;
    }

    return conn;
  }
  
  /*
   * Adds or updates option in the 'options' table
   */
  public void updateOption(String name, String value){
    updateOption(name, value, options_table);
  }
  
  /*
   * General update method
   */
  public void updateOption(String name, String value, String dbtable){
    table=options_table;
    if(dbtable.length()!=0) table=dbtable;
    
    sql="select name from "+table+" where name=?";
    
    try {
      if(conn==null) return;
      prep = conn.prepareStatement(sql);
      prep.setString(1, name);
      
      rs=prep.executeQuery();
      
      if(!rs.next()){
        addOption(name, value, table);
        return;
      }
      
      sql="update "+table+" set value=? where name=?";
      prep = conn.prepareStatement(sql);
      prep.setString(1, value);
      prep.setString(2, name);
      prep.execute();
    } catch (Exception e) {
      System.out.println("Error in SQL query (updateOption()): "+e.getMessage());
    }
  }
  
  /*
   * Adds or updates last option in the 'config' table
   * Redirects to the updateOption()
   */
  public void updateConfig(String name, String value){
    updateOption(name,value,config_table);
  }
  
  /*
   * Loads last options from the database to assign them to the form fields
   */
  public String loadLastOptions(){
    return getOption("last", config_table);
  }
  
  public String getOption(String name){
    return getOption(name, options_table);
  }
  
  /*
   * Retrieves option from the database when an item is selected in the dropdown
   * to load options set into the form fields
   */
  public String getOption(String name, String table){
    sql="select value from "+table+" where name=?";
    
    try {
      if(conn==null) return null;
      prep = conn.prepareStatement(sql);
      prep.setString(1, name);
      rs=prep.executeQuery();
      
      if(!rs.next()) return null;
      return rs.getString(1);
    } catch (Exception e) {
      System.out.println("Error in SQL query (getOption()): "+e.getMessage());
      return null;
    }
  }
  
  /*
   * Loads all options from the database to show them in the dropdown
   */
  public ArrayList<String> listOptions(){
    table=options_table;
    String key="name";
    ArrayList<String> list=new ArrayList<String>();
    
    sql="select "+key+" from "+table+" order by "+key+" asc";
    
    try {
      if(conn==null) return null;
      st = conn.createStatement();
      rs=st.executeQuery(sql);
      
      while(rs.next())
        list.add(rs.getString("name"));
      
      if(list.size()==0) return null;
      return list;
      
    } catch (Exception e) {
      System.out.println("Error in SQL query (listOptions()): "+e.getMessage());
      return null;
    }
  }
  
  /*
   * Removes option from the database
   */
  public void removeOption(String name) {
    table=options_table;
    sql="delete from "+table+" where name=?";
    
    try {
      if(conn==null) return;
      prep = conn.prepareStatement(sql);
      prep.setString(1, name);
      prep.execute();
    } catch (Exception e) {
      System.out.println("Error in SQL query (removeOption()): "+e.getMessage());
      return;
    }
  }
  
  /*
   * Insert option into the database
   * helper funtion for updateOption()
   */
  private void addOption(String name, String value, String table) throws SQLException{
    sql="insert into "+table+" (name,value) values(?,?)";
    prep = conn.prepareStatement(sql);
    prep.setString(1, name);
    prep.setString(2, value);
    prep.execute();
  }
  
}
