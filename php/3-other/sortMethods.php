<?php
  define(nl,"<br>");

  $fruits = array("d" => "lemon", "a" => "orange", "b" => "banana", "c" => "[apple]");
  
  sort($fruits);
  // natcasesort($fruits);
  // asort($fruits);
  
  foreach ($fruits as $key => $val) {
      echo "$key = $val".nl;
  }
?>