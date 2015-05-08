
<?php

  include_once(dirname(__FILE__) . '/database-class.php');
  include_once(dirname(__FILE__) . '/functions.php');

  /*
   * Adds option with entered name to the database
   */
  function add_option(){
    $name=$_POST['name'];
    $value=json_encode($_POST['value']);
    $db=new Database();
    $db->updateOption($name, $value);
  }
  
  /*
   * Removes option selected in the dropdown from the database
   */
  function remove_option(){
    $name=$_POST['name'];
    $db=new Database();
    $db->removeOption($name);
  }
  
  /*
   * Loads an option selected in the dropdown
   */
  function load_option(){
    $name=$_POST['name'];
    $db=new Database();
    $options=$db->getOption($name);
    echo $options;
  }
  
  $action=$_POST['action'];
  switch($action){
    case 'add':
      add_option();
      break;
    case 'remove':
      remove_option();
      break;
    case 'load':
      load_option();
      break;
  }
  
  // $opt=getOption('last');
  // return json_encode($opt);

?>


