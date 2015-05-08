
<?php

  include_once(dirname(__FILE__) . '/database-class.php');
  include_once(dirname(__FILE__) . '/functions.php');

  function add_option(){
    $name=$_POST['name'];
    $value=json_encode($_POST['value']);
    $db=new Database();
    $db->updateOption($name, $value);
  }
  
  function load_option(){
    $name=$_POST['name'];
    $db=new Database();
    $options=$db->getOption($name);
    echo $options;
  }
  
  function remove_option(){
    $name=$_POST['name'];
    $db=new Database();
    $db->removeOption($name);
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


