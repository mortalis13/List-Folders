
<?php
  ini_set('error_reporting',E_ALL);
  ini_set('xdebug.var_display_max_depth', -1);
  ini_set('xdebug.var_display_max_children', -1);
  ini_set('xdebug.var_display_max_data', -1);
  
  $path="C:/1-Roman/Documents/3-programming/cms";
  // $path="C:/1-Roman/Documents/test";

  $filterExt=array("pdf","djvu");
  // $filterExt=array("");
  $filterExt=join("\n",$filterExt);
  
  $filterDir=array("magento","wordpress");
  // $filterDir=array("");
  $filterDir=join("\n",$filterDir);
?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="style.css">
    
    <script src="js/jquery.min.js"></script>
    <script src="js/custom.js"></script>
    
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
              <label for="export-text">Export Text</label> <input type="checkbox" name="export_text" id="export-text">
              <label for="export-markup">Export Markup</label> <input type="checkbox" name="export_markup" id="export-markup">
              <label for="export-tree">Export Tree</label> <input type="checkbox" name="export_tree" id="export-tree">
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
          private $json;
          
          private $iconsPath;
          
          public function __construct(){
            define("nl","\n");
            $this->iconsPath="./lib/images/";
            
            $this->path=$_POST['path'];
            
            $this->filterExt=$this->getFilters('filter_ext');
            $this->filterDir=$this->getFilters('filter_dir');
            
            $this->text=[];
            $this->markup=[];
            
            $this->processData();
          }
          
          public function processData() {
            $json=$this->fullScan($this->path);
            $text=$this->wrapText();
            if(!$text) echo "No Data!";
            echo $text;
            
            if(isset($_POST['export_tree'])){
              $this->json=json_encode($json);
              $this->exportTree();
            }
            
            if(isset($_POST['export_text'])){
              $text=join("\n",$this->text);
              $filename='result-text.txt';
              $file = fopen($this->path.'/'.$filename, "w");              
              fwrite($file,$text);
              fclose($file);
            }
            
            if(isset($_POST['export_markup'])){
              $markup=$this->wrapMarkup();
              $filename='result-markup.html';
              $file = fopen($this->path.'/'.$filename, "w");   
              fwrite($file, $markup);
              fclose($file);
            }
          }
          
          public function exportTree(){
            if(isset($_POST['tree_name']))
              $treeName=$_POST['tree_name'];
            else{
              preg_match("/\/[\w]+$/",$this->path,$treeName);
              $treeName=substr($treeName[0],1);
            }
            
            $tmpl='templates/tree.html';
            
            $exportPath="export/tree/";
            $jsonFolder="json/";
            $jsonPath=$exportPath.$jsonFolder;
            
            $exportDoc=$treeName.".html";
            $exportJSON=$treeName.".json";
            
            $file=fopen($tmpl,"r");
            $text = fread($file, filesize($tmpl));
            fclose($file);
            
            $this->replaceTemplate("_jsonPath_", $jsonFolder.$exportJSON, $text);
            $this->replaceTemplate("_Title_", 'Directory: '.$treeName, $text);
            $this->replaceTemplate("_FolderPath_", 'Directory: '.$this->path, $text);
            
            $filters='Files ['.join(",",$this->filterExt).']';
            $filters.=', Directories ['.join(",",$this->filterDir).']';
            $this->replaceTemplate("_Filters_", "Filters: ".$filters, $text);
            
            $file=fopen($exportPath.$exportDoc,"w");
            fwrite($file,$text);
            fclose($file);
            
            $file=fopen($jsonPath.$exportJSON, "w");
            fwrite($file,$this->json);
            fclose($file);
          }
          
          public function replaceTemplate($tmpl, $replacement, &$text){
            $text=str_replace($tmpl, $replacement, $text);
          }
          
          public function getFilters($filter){
            $filter=trim($_POST[$filter]);
            
            if($filter){
              $filter=explode("\n",$filter);
              foreach($filter as &$item){
                $item=trim($item);
              }
            }
            return $filter;
          }
          
          public function fixEncoding($text){
            $text=mb_convert_encoding($text, 'utf-8', 'cp1251');
            return $text;
          }
          
          public function fullScan($dir, $level=-1){
            $json=[];
            
            $data = scandir($dir);
            $list=$this->prepareData($data,$dir);
            $pad=$this->getPadding($level);
            
            $count=count($list);
            if(!$count) return false;
            
            foreach ($list as $value) {
              $item=$dir . DIRECTORY_SEPARATOR . $value;
              
              if (is_dir($item)) {
                $passed=true;
                if($this->filterDir && $level==-1){
                  // $this->filterDir=explode("\n",$_POST['filter_dir']);
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
                
                if($res){
                  $json[]=array(
                    "text"=>$this->fixEncoding($value),
                    "children"=>$res
                  );
                }
              }
              else{
                $currentFile=$value;
                $this->markup[]=$pad.$this->wrapFile($currentFile);
                $this->text[]=$pad.$currentFile;
                
                $json[]=array(
                  "text"=>$this->fixEncoding($value),
                  "icon"=>$this->getIcon($value)
                );
              }
            }
            
            if(!$count) return false;
            return $json;
          }
          
          public function getIcon($file){
            preg_match("/\.[\w]+$/",$file,$ext);
            $ext=substr($ext[0],1);
            
            switch($ext){
              case "pdf":
                $icon=$this->iconsPath."pdf.png";
                break;
              case "djvu":
                $icon=$this->iconsPath."djvu.gif";
                break;
              default:
                $icon="jstree-file";
                break;
            }
            return $icon;
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
            $isFilter=trim(join('',$this->filterExt));
            if(!$isFilter) return true;
            
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
