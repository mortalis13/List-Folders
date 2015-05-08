
<?php
  header("Content-Type: text/html; charset=utf-8");
  ini_set('error_reporting',E_ALL);
  ini_set('xdebug.var_display_max_depth', -1);
  ini_set('xdebug.var_display_max_children', -1);
  ini_set('xdebug.var_display_max_data', -1);
  
  include_once('includes/database-class.php');
  include_once('includes/functions.php');
  include_once('includes/scan-directory-class.php');
  
  $db=new Database();
  
?>

<?php
  if(isset($_POST['scan_dir'])){
    $options=json_encode($_POST);
    $db->updateConfig('last', $options);
  }
?>

<?php  
  
  $path="C:/1-Roman/Documents/3-programming/cms";

  $filterExt=array("pdf","djvu");
  $filterExt=join("\n",$filterExt);
  
  $excludeExt="";
  
  $filterDir=array("magento","wordpress");
  $filterDir=join("\n",$filterDir);
  
  // $path="C:/1-Roman/Documents/test";
  // $filterExt="";
  // $filterDir="";
  
  $last=$db->getLastOptions();
  if($last){
    $last=json_decode($last);
    $path=$last->path;
    $filterExt=$last->filter_ext;
    $excludeExt=$last->exclude_ext;
    $filterDir=$last->filter_dir;
  }
  else if(isset($_POST['scan_dir'])){
    $path=$_POST['path'];
    $filterExt=$_POST['filter_ext'];
    $excludeExt=$_POST['exclude_ext'];
    $filterDir=$_POST['filter_dir'];
  }
  
  $optionsList=$db->listOptions();
  if(!$optionsList)
    $optionsList="-No options-";
  $optionsList=wrapOptions($optionsList);
?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="style.css">
    
    <script src="js/jquery.min.js"></script>
    <script src="js/custom.js"></script>
    
    <title>List Folder 1.0.5</title>
  </head>
  
  <body>
    <div id="container">
    
      <div class="page-title">
        <h2>List Folder</h2>
        <div class="desc">Allows list folders, subfolders and files of a selected directory</div>
      </div>
    
      <div class="form-container">
        <form method="post" id="options-form">
          <div class="field">
            <input type="text" name="path" id="path" placeholder="Directory Full Path" value="<?=$path?>">
          </div>
          
          <div class="options-fields field">
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
              <div class="list">
                <label for="export-text">Export Text</label><input type="checkbox" class="export-option" name="export_text" id="export-text">
                <label for="export-markup">Export Markup</label><input type="checkbox" class="export-option" name="export_markup" id="export-markup">
                <label for="export-tree">Export Tree</label><input type="checkbox" class="export-option" name="export_tree" id="export-tree">
              </div>
              <button class="clear-filter" id="toggle-exports">Toggle All</button>
            </div>
          </div>
          
          <div class="field footer">
            <input type="submit" name="scan_dir" accesskey="s" value="Scan Directory">
          </div>
          
        </form>
      </div>
      
      <div class="manage-options-wrap">
        <div class="manage-options control-block">
          <div class="title">Manage Options</div>
          <div class="add-wrap field">
            <input type="text" name="option_name" id="option-name">
            <button id="add-option">Add</button>
          </div>
          
          <div class="remove-wrap field">
            <select name="options_list" id="options-list"><?=$optionsList?></select>
            <button id="remove-option">Remove</button>
          </div>
        </div>
      </div>
      
      <?php
        if(isset($_POST['scan_dir'])){
          new ScanDirectory();
        }
      ?>

    </div>
  </body>
</html>
