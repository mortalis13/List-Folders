
<?php
  ini_set('error_reporting',E_ALL);
  ini_set('xdebug.var_display_max_depth', -1);
  ini_set('xdebug.var_display_max_children', -1);
  ini_set('xdebug.var_display_max_data', -1);
  
  $path="C:/1-Roman/Documents/3-programming/cms";

  $filterExt=array("pdf","djvu");
  $filterExt=join("\n",$filterExt);
  
  $excludeExt="";
  
  $filterDir=array("magento","wordpress");
  $filterDir=join("\n",$filterDir);
  
  // $path="C:/1-Roman/Documents/test";
  // $filterExt="";
  // $filterDir="";
?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="style.css">
    
    <script src="js/jquery.min.js"></script>
    <script src="js/custom.js"></script>
    
    <title>List Folder 1.0.4</title>
  </head>
  
  <body>
    <div id="container">
    
      <div class="title">
        <h2>List Folder</h2>
        <div class="desc">Allows list folders, subfolders and files of a selected directory</div>
      </div>
    
      <div class="form-container">
        <form method="post">
          <div class="form-field">
            <input type="text" name="path" id="path" placeholder="Directory Full Path" value="<?=$path?>">
          </div>
          
          <div class="options-fields form-field">
            <div class="filter-fields filter-ext-wrap">
              <label for="filter-ext">Filter Extensions</label><br>
              <textarea name="filter_ext" id="filter-ext" cols="10" rows="5"><?=$filterExt?></textarea>
              <button class="clear-filter" id="clear-ext-filter">Clear</button>
            </div>
            
            <div class="filter-fields exclude-ext-wrap">
              <label for="exclude-ext">Exclude Extensions</label><br>
              <textarea name="exclude_ext" id="exclude-ext" cols="10" rows="5"><?=$excludeExt?></textarea>
              <button class="clear-filter" id="clear-exclude-ext">Clear</button>
            </div>
            
            <div class="filter-fields filter-dir-wrap">
              <label for="filter-dir">Filter Directories</label><br>
              <textarea name="filter_dir" id="filter-dir" cols="10" rows="5"><?=$filterDir?></textarea>
              <button class="clear-filter" id="clear-dir-filter">Clear</button>
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
          
        </form>
      </div>

      <?php
        header("Content-Type: text/html; charset=utf-8");
        
        include_once('scan-directory-class.php');
        
        if(isset($_POST['scan_dir'])){
          new ScanDirectory();
        }
      ?>

    </div>
  </body>
</html>
