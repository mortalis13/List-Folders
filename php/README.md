
# List Folders (PHP version)


## Requirements

1. **MySQL** server running and database `list_folders` created (use `add/db.sql` file to create database with needed structure).
2. **Apache** server running.


## Run

1. Copy last folder from `2-releases` (the last release) to `htdocs` and rename it
6. Open URL in the browser `localhost/list-folders`.
7. The page with form should appear.


## Problems

Detected problems with russian and spanish folder names.

1. If root folder has russian symbols the scanning fails (scandir()). 
2. If root folder is in english and has russian subfolders all should be well (russian subfolder scans correctly). 
3. For spanish folders (root/subfolder) this isn't true. 
4. Spanish files inside folders render correctly but the inner substitution occurs (ñ->n, á->a, ...) 


## Changelog

### 1.0.6

- add some style
- refactored some code in `custom.js` and `includes/scan-directory-class.php`


### 1.0.5

- add trim path
- add Toggle All button for export options
- add database interaction to load last options after submit and save/load options
- add Manage Options panel to add/load/remove options to a database
- add ability to type export name for all export options
- add more file extensions icons
- change export destination is `export` subfolder for all export options
- fix path with last slash
- fix scanning files without extensions


### 1.0.4

- add `Exclude extensions` filter
- add clear buttons for all filters
- add style and description
- fix backslash paths


### 1.0.3

- add html template for the folder, it edits within the code and exports to the `export/tree` directory
- add style, title, exported folder info to the template
- add **Expand/Collapse All** buttons for the tree
- add toggle folder on click event
- add **Tree name** field when the **Export tree** is checked, allows select other name for the target folder (default is the last folder name in the path)


### 1.0.2

- exports JSON file valid for the [jsTree](http://jstree.com/) jQuery plugin which shows navigatable tree
- adds PDF, DJVU icons to the tree


### 1.0.1

- exports the structure text to HTML file with markup for directories and files which allows to style these elements


### 1.0.0

- shows directory structure with file/directory filters
- exports the structure text to a text file if **Export text** checked
- skips empty directories and directories that doesn't contain files required by the filter
