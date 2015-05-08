
<html lang="en">
<head>
  <!-- <meta charset="utf-8"> -->
</head>
  
<body>

<?php
  // header("Content-Type: text/html; charset=utf-8");
  
  $path="C:/Documents/test";
  $list=scandir($path);
  $list=join("\n",$list);
  
  $list=mb_convert_encoding($list, "utf-8", "cp1251");
  echo "<pre>".$list."</pre>";
  
  $text=$list;
  foreach(mb_list_encodings() as $chr){
    echo "<u><b>".$chr."</b></u>"." : ".mb_convert_encoding($text, "UTF-8", $chr)."<br>";   
  } 
  
  
  // $result="йцу";

  // $filename="test.txt";
  // $path="C:/1-Roman/Documents/3-programming/cms";
  
  // $file = fopen($path."/".$filename, "w");
  // fwrite($file,$result);
  
  // $text = "A strange string to pass, maybe with some ø, æ, å characters.";

  // foreach(mb_list_encodings() as $chr){
  //   echo "<u><b>".$chr."</b></u>"." : ".mb_convert_encoding($text, "UTF-8", $chr)."<br>";   
  // } 
  
  // $text = "A strange string to pass, maybe with some ø, æ, å characters.";

  // foreach(mb_list_encodings() as $chr){
  //   echo "<u><b>".$chr."</b></u>"." : ".mb_convert_encoding($text, "UTF-8", $chr)."<br>";   
  // } 
  
?>

</body>
</html>