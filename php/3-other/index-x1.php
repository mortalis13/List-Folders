
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>List Folders</title>
  </head>
  
  <body>
    <div id="container">

<?php
  header('Content-Type: text/html; charset=cp1251');
  
  // define(nl,'<br>');
  // $nl='\n';
  $path='C:/1-Roman/Documents/3-programming/cms';
  // $path='C:/1-Roman/Documents/test';
  
  // echo $path;
  
  function dirToArray($dir) {
    $result='';
    $list = scandir($dir);
    
    // $minus = array('.', '..');
    // $list = array_diff($list, $minus);
    
    foreach ($list as $key => $value) {
      if (is_dir($dir . DIRECTORY_SEPARATOR . $value)) {
        // $res=dirToArray($dir . DIRECTORY_SEPARATOR . $value);
        // $result. = $value . $nl . $res;
        
        // $result. = $value . dirToArray($dir . DIRECTORY_SEPARATOR . $value);
        $result. = '-';
      }
      else { 
        $result.='=';
        // $result.=$value;
      }
    }
    return $result;
  } 
  
  $result=dirToArray($path);
  echo $result;
  
  // var_dump($list);
  
  // $list=scandir($path);
  // $minus = array('.', '..');
  // $list = array_diff($list, $minus);
  
  // foreach($list as $item){
  //   echo $item.'<br>';
  // }
?>

    </div>
  </body>
</html>