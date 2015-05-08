
<?php
  ini_set('error_reporting',E_ALL);
  // error_reporting(E_ALL);
  
  $path="C:/1-Roman/Documents/3-programming/cms";

  $filterExt=array("pdf","djvu");
  $filterExt=join("\n",$filterExt);
  
  $filterDir=array("magento","wordpress");
  $filterDir=join("\n",$filterDir);
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
          
          <div class="options-fields form-field">
            <div class="filter-fields filter-ext">
              <label for="filter-ext">Filter Extensions</label><br>
              <textarea name="filter_ext" id="filter-ext" cols="10" rows="5"><?=$filterExt?></textarea>
            </div>
            
            <div class="filter-fields filter-dir">
              <label for="filter-dir">Filter Directories</label><br>
              <textarea name="filter_dir" id="filter-dir" cols="10" rows="5"><?=$filterDir?></textarea>
            </div>
            
            <div class="export-options">
              <div class="title">Export Options</div>
              <label for="export-text">Export Text</label> <input type="checkbox" name="export-text" id="export-text">
              <label for="export-markup">Export Markup</label> <input type="checkbox" name="export-markup" id="export-markup" checked>
              <label for="export-tree">Export Tree</label> <input type="checkbox" name="export-tr ee" id="export-tree">
            </div>
          </div>
          
          <div class="form-field">
            <input type="submit" name="scan_dir" accesskey="s" value="Scan Directory">
          </div>
          
          <!-- <input type="hidden" name="scan_dir"> -->
        </form>
      </div>

      <?php
        header("Content-Type: text/html; charset=utf-8");
        
        class ScanDirectory{
          
          private $path;
          private $filterExt;
          private $filterDir;
          
          private $text;
          private $markup;
          
          public function __construct(){
            define("nl","\n");
            
            $this->path=$_POST['path'];
            $this->filterExt=explode("\n",$_POST['filter_ext']);
            
            $filterDir=trim($_POST['filter_dir']);
            if($filterDir){
              $this->filterDir=explode("\n",$filterDir);
            }
            
            $this->text=[];
            $this->markup=[];
            
            $this->processData();
          }
          
          public function processData() {
            $this->fullScan($this->path);
            $text=$this->wrapText();
            if(!$text) echo "No Data!";
            echo $text;
            
            if(isset($_POST['export-text'])){
              $text=join("\n",$this->text);
              $filename='result-text.txt';
              $file = fopen($this->path.'/'.$filename, "w");              
              fwrite($file,$text);
            }
            
            if(isset($_POST['export-markup'])){
              $markup=$this->wrapMarkup();
              $filename='result-markup.html';
              $file = fopen($this->path.'/'.$filename, "w");   
              fwrite($file, $markup);
            }
          }
          
          public function fullScan($dir, $level=-1){
            $data = scandir($dir);
            $list=$this->prepareData($data,$dir);
            $pad=$this->getPadding($level);
            
            $count=count($list);
            if(!$count) return false;
            
            foreach ($list as $key => $value) {
              $item=$dir . DIRECTORY_SEPARATOR . $value;
              
              if (is_dir($item)) {
                $passed=true;
                if($this->filterDir && $level==-1){
                  $this->filterDir=explode("\n",$_POST['filter_dir']);
                  $passed=$this->filterDirectories($value);
                }
                if(!$passed) continue;
                
                $currentDir="[".$value."]";
                $this->markup[]=$pad.$this->wrapDir($currentDir);
                $this->text[]=$pad.$currentDir;
                
                $res=$this->fullScan($item, $level+1);
                
                if(!$res){
                  array_pop($this->text);
                  array_pop($this->markup);
                  $count--;
                }
              }
              else{
                $currentFile=$value;
                $this->markup[]=$pad.$this->wrapFile($currentFile);
                $this->text[]=$pad.$currentFile;
              }
            }
            
            if(!$count) return false;
            return true;
          }
          
          public function wrapDocument($markup){
            $markup='<meta charset="utf-8">'.nl.$markup;
            return $markup;
          }
          
          public function wrapDir($dir){
            return '<span class="directory">'.$dir.'</span>';
          }
          
          public function wrapFile($file){
            return '<span class="file">'.$file.'</span>';
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
            foreach($this->filterExt as $ext){
              $ext=trim($ext);
              $pattern="/\.".$ext."$/";
              if(preg_match($pattern, $value)!==0)
                return true;
            }
            return false;
          }
          
          public function filterDirectories($dir){
            $result=[];
            
            foreach($this->filterDir as $filter) {
              $filter=trim($filter);
              if($filter==$dir)
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
          
          public function wrapText(){
            $text=$this->text;
            $text=join("\n",$text);
            $text=mb_convert_encoding($text, 'utf-8', 'cp1251');
            $text='<pre>'.nl.$text.nl.'</pre>';
            
            return $text;
          }
          
          public function wrapMarkup(){
            $markup=$this->markup;
            $markup=join("\n",$markup);
            $markup=mb_convert_encoding($markup, 'utf-8', 'cp1251');
            $markup='<pre>'.nl.$markup.nl.'</pre>';
            $markup=$this->wrapDocument($markup);
            
            return $markup;
          }
          
        }
        
        if(isset($_POST['scan_dir'])){
          new ScanDirectory();
        }
        
      ?>

    </div>
  </body>
</html>
