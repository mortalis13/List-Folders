
<?php

  function wrapOptions($list){
    if(gettype($list)=='string')
      return '<option value="-1">'.$list."</option>";
    
    foreach($list as &$option){
      $option="<option value=".$option.">".$option."</option>";
    }
    $list=join("\n",$list);
    return $list;
  }
  
?>
