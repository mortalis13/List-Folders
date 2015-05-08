using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;

namespace ListFolders.Includes {
  public class Database {
    
    string options_table="options";
    string config_table="config";
    string table;
    string sql;
    string res;

    bool connected;

    private MySqlConnection conn;
    private MySqlCommand cmd;
    
    private string server;
    private string database;
    private string uid;
    private string password;

    public Database() {
      if (conn == null)
        conn = createConnection();
      connected=OpenConnection();

      string text;
      if (!connected) text= "No MySQL Connection";
      else text = "MySQL connected";
      MainForm.form.lStatus.Text = text;
    }

    /*
     * Creates new connection object and returns it
     */
    private MySqlConnection createConnection() {
      server = "localhost";
      database = "list_folders";
      uid = "root";
      password = "";
      string connectionString;
      connectionString = "SERVER=" + server + ";" + "DATABASE=" + database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";

      conn = new MySqlConnection(connectionString);

      return conn;
    }
    
    /*
     * Opens connection if server is running and database exists
     * returns false if error occurs
     */
    private bool OpenConnection() {
      try {
        conn.Open();
        return true;
      }
      catch (MySqlException ex) {
        return false;
      }
    }

    /*
     * Closes connection after all operations are done
     */
    public bool CloseConnection() {
      try {
        conn.Close();
        return true;
      }
      catch (MySqlException ex) {
        return false;
      }
    }
    
    /*
     * Checks if "name" exists in the table
     */
    public bool Exists(string table, string name) {
      string sql;

      sql = "select count(*) from " + table + " where name=@name";
      int count=0;
      if (!connected) return false;

      cmd = new MySqlCommand(sql, conn);
      cmd.Prepare();
      cmd.Parameters.AddWithValue("@name", name);
      count = Convert.ToInt32(cmd.ExecuteScalar());
      
      if(count==0) return false;
      return true;
    }
    
    /*
     * General update method for 'config' and 'options' tables
     */
    public void updateOption(string name, string value, string dbtable){
      if (!connected) return;

      table=options_table;
      if(dbtable.Length!=0) table=dbtable;
      
      sql="update "+table+" set value=@value where name=@name";
      if(!Exists(table, name)){
        sql="insert into "+table+" (name,value) values(@name, @value)";
      }
      
      cmd = new MySqlCommand(sql, conn);
      cmd.Prepare();
      cmd.Parameters.AddWithValue("@name", name);
      cmd.Parameters.AddWithValue("@value", value);
      
      cmd.ExecuteNonQuery();
    }
    
    /*
     * Adds or updates option in the 'options' table
     */
    public void updateOption(string name, string value){
      updateOption(name, value, options_table);
    }
    
    /*
     * Adds or updates last option in the 'config' table
     * Redirects to the updateOption()
     */
    public void updateConfig(string name, string value){
      updateOption(name,value,config_table);
    }
    
    /*
     * Loads last options from the database to assign them to the form fields
     */
    public string loadLastOptions(){
      return getOption("last", config_table);
    }
    
    /*
     * Gets option from 'options' table
     */
    public string getOption(string name){
      return getOption(name, options_table);
    }
    
    /*
     * Retrieves option from the database when an item is selected in the dropdown
     * to load options set into the form fields
     */
    public string getOption(string name, string table){
      sql="select value from "+table+" where name=@name";

      if (!connected) return null;
      
      cmd = new MySqlCommand(sql, conn);
      
      cmd.Prepare();
      cmd.Parameters.AddWithValue("@name", name);
      
      MySqlDataReader dataReader = cmd.ExecuteReader();
      if (!dataReader.Read()) {
        dataReader.Close();
        return null;
      }

      res=(string) dataReader["value"];
      dataReader.Close();

      return res;
    }

  }
}
