
<?php
  $path="C:/1-Roman/Documents/3-programming/cms";
  $filter=array("pdf","djvu");
  $filterText=join("\n",$filter);
?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="style.css">
    <title>List Folders</title>
  </head>
  
  <body>
    <div id="container">
    
      <div class="form-container">
        <form method="post">
          <div class="form-field">
            <input type="text" name="path" id="path" placeholder="Directory Full Path" value="<?=$path?>">
          </div>
          
          <div class="form-field">
            <label for="filter">Filter Extensions ('png')</label><br>
            <textarea name="filter" id="filter" cols="10" rows="5"><?=$filterText?></textarea>
          </div>
          
          <div class="form-field">
            <input type="submit" name="scan_dir" accesskey="s" value="Scan Directory">
          </div>
          
          <!-- <input type="hidden" name="scan_dir"> -->
        </form>
      </div>

      <?php
        
        class ScanDirectory{
          
          public function __construct(){
            
          }
        
          header("Content-Type: text/html; charset=cp1251");
          
          // define(nl,"<br>\n");
          
          $path=$_POST['path'];
          // $path="C:/1-Roman/Documents/3-programming/cms";
          // $path="C:/1-Roman/Documents/test";
          
          function getList($folders,$files){
            $flags=SORT_FLAG_CASE | SORT_STRING;
            sort($folders,$flags); 
            sort($files,$flags);
            // natcasesort ($folders); natcasesort ($files);
            
            $list=array_merge($folders,$files);
            return $list;
          }
          
          function prepareData($data,$dir){
            $minus = array(".", "..");
            $data = array_diff($data, $minus);
            return $data;
            
            $folders=[]; $files=[];
            
            foreach ($data as $value) {
              $item=$dir.'/'.$value;
              if (is_dir($item))
                $folders[]=$value;
              else if(filterFile($value))
                $files[]=$value;
            }
            
            $list=getList($folders,$files);
            return $list;
          }
          
          function filterFile($value,$filter){
            // $filter=array("pdf","djvu");
            // $filter1=explode("\n",$_POST['filter']);
            
            foreach($filter as $ext){
              $ext=trim($ext);
              $pattern="/\.".$ext."$/";
              if(preg_match($pattern, $value)!==0)
                return true;
            }
            return false;
          }
          
          function getPadding($level){
            $pad="    ";
            $resPad="";
            for($i=0;$i<=$level;$i++)
              $resPad.=$pad;
            return $resPad;
          }
          
          function wrapResult($result){
            return "<pre>".$result."</pre>";
          }
          
          function dirToArray($dir, $level=-1) {
            $result="";
            $data = scandir($dir);
            $list=prepareData($data,$dir);
            $pad=getPadding($level);
            
//            $filter=array("pdf","djvu");
            $filter=explode("\n",$_POST['filter']);
            
            $x=1;
            
            foreach ($list as $key => $value) {
              $item=$dir . DIRECTORY_SEPARATOR . $value;
              
              if (is_dir($item)) {
                $res=dirToArray($item, $level+1);
                if($res)
                  $result.= $pad."[".$value."]".nl . $res;
              }
              else if(filterFile($value, $filter)){
                $result.=$pad.$value.nl;
              }
            }
            return $result;
          } 
          
          $result=dirToArray($path);
          echo wrapResult($result);
        }
        
        if(isset($_POST['scan_dir'])){
          new ScanDirectory();
        }
        
      ?>

    </div>
  </body>
</html>