
<?php

  define("DB_DSN","mysql:host=localhost;dbname=list_folders;charset=utf8");
  define("DB_USERNAME","root");
  define("DB_PASSWORD","");
  
  class Database{
    
    private $conn;
    private $option_table;
    private $config_table;
    
    public function __construct(){
      $this->conn=$this->createConnection();
      $this->option_table='options';
      $this->config_table='config';
    }
    
    /*
     * Creates new DB connection and sets it as persistent to save the connection in cache
     * which allows increase database queries performance
     */
    public function createConnection(){
      try {
        $config=array();
        $config=array(PDO::ATTR_PERSISTENT => true);
        
        $conn=new PDO(DB_DSN,DB_USERNAME,DB_PASSWORD,$config);
        return $conn;
      } catch (PDOException $e) {
        print "Error!: " . $e->getMessage() . "<br/>";
        die();
      }
    }
    
    /*
     * Adds or updates option in the options table
     * First checks if the option with $name exists
     */
    public function updateOption($name, $value){
      $table=$this->option_table;
          
      $sql="select name from ".$table." where name=:name";
      $st=$this->conn->prepare($sql);
      $st->bindValue("name", $name);
      $st->execute();
      
      $res=$st->fetch();
      
      $sql="update ".$table." set value=:value where name=:name";                       // insert new row or update existing
      if(!$res){
        $sql="insert into ".$table." (name,value) values(:name,:value)";
      }
      
      $st=$this->conn->prepare($sql);
      $st->bindValue("name", $name);
      $st->bindValue("value", $value);
      $res=$st->execute();
    }
    
    /*
     * Same as updateOption() but for the config table
     */
    public function updateConfig($name, $value){
      $table=$this->config_table;
          
      $sql="select name from ".$table." where name=:name";
      $st=$this->conn->prepare($sql);
      $st->bindValue("name", $name);
      $st->execute();
      
      $res=$st->fetch();
      
      $sql="update ".$table." set value=:value where name=:name";
      if(!$res){
        $sql="insert into ".$table." (name,value) values(:name,:value)";
      }
      
      $st=$this->conn->prepare($sql);
      $st->bindValue("name", $name);
      $st->bindValue("value", $value);
      $res=$st->execute();
    }
    
    /*
     * Gets option from the table by its name
     */
    public function getOption($name){                                             
      $table=$this->option_table;
      
      $sql="select value from ".$table." where name=:name";
      $st=$this->conn->prepare($sql);
      $st->bindValue("name", $name);
      $st->execute();
      
      $res=$st->fetch();
      if(!$res) return false;
      
      $res=$res[0];
      // $res=json_decode($res);
      return $res;
    }
    
    /*
     * Removes selected option from the database
     */
    public function removeOption($name){
      $table=$this->option_table;
      
      $sql="delete from ".$table." where name=:name";
      $st=$this->conn->prepare($sql);
      $st->bindValue("name", $name);
      $res=$st->execute();
      
      return $res;
    }
    
    /*
     * Gets all options from the 'options' table
     */
    public function listOptions(){
      $table=$this->option_table;
      $key='name';
      
      $sql="select ".$key." from ".$table." order by ".$key." asc";
      $st=$this->conn->query($sql);
      
      $res=$st->fetchAll();
      if(!$res) return false;
      
      $list=[];
      foreach($res as $row){
        $list[]=$row[$key];
      }
      
      return $list;
    }
    
    /*
     * Gets the 'last' option from the 'config' table
     */
    public function getLastOptions(){
      $table=$this->config_table;
      
      $sql="select value from ".$table." where name='last'";
      $st=$this->conn->query($sql);
      
      $res=$st->fetch()[0];
      if(!$res) return false;
      
      return $res;
    }
    
  }
  
?>
