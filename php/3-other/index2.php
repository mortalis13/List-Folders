
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
        header("Content-Type: text/html; charset=cp1251");
        
        class ScanDirectory{
          
          private $path;
          private $filter;
          
          public function __construct(){
            define(nl,"\n");
            $this->path=$_POST['path'];
            $this->filter=explode("\n",$_POST['filter']);
            
            $result=$this->fullScan($this->path);
            echo $this->wrapResult($result);
          }
          
          public function fullScan($dir, $level=-1) {
            $result="";
            $data = scandir($dir);
            $list=$this->prepareData($data,$dir);
            $pad=$this->getPadding($level);
            
            foreach ($list as $key => $value) {
              $item=$dir . DIRECTORY_SEPARATOR . $value;
              
              if (is_dir($item)) {
                $res=$this->fullScan($item, $level+1);
                if($res)
                  $result.= $pad."[".$value."]".nl . $res;
              }
              else{
                $result.=$pad.$value.nl;
              }
            }
            return $result;
          } 
        
          public function getList($folders,$files){
            $flags=SORT_FLAG_CASE | SORT_STRING;
            sort($folders,$flags); 
            sort($files,$flags);
            
            $list=array_merge($folders,$files);
            return $list;
          }
          
          public function prepareData($data,$dir){
            $minus = array(".", "..");
            $data = array_diff($data, $minus);
            
            $folders=[]; $files=[];
            
            foreach ($data as $value) {
              $item=$dir.'/'.$value;
              if (is_dir($item))
                $folders[]=$value;
              else if($this->filterFile($value))
                $files[]=$value;
            }
            
            $list=$this->getList($folders,$files);
            return $list;
          }
          
          public function filterFile($value){
            foreach($this->filter as $ext){
              $ext=trim($ext);
              $pattern="/\.".$ext."$/";
              if(preg_match($pattern, $value)!==0)
                return true;
            }
            return false;
          }
          
          public function getPadding($level){
            $pad="    ";
            $resPad="";
            for($i=0;$i<=$level;$i++)
              $resPad.=$pad;
            return $resPad;
          }
          
          public function wrapResult($result){
            return "<pre>".$result."</pre>";
          }
          
        }
        
        if(isset($_POST['scan_dir'])){
          new ScanDirectory();
        }
        
      ?>

    </div>
  </body>
</html>