
<?php
  
  /*
   * Prepares options list to be inserted into the dropdown list
   */
  function wrapOptions($list){
    if(gettype($list)=='string')
      return '<option value="-1">'.$list."</option>";                 // if no options then wrap the '-No Options-' string
    
    foreach($list as &$option){
      $option="<option value=".$option.">".$option."</option>";
    }
    $list=join("\n",$list);
    return $list;
  }
  
?>
