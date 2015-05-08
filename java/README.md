# List Folders

Main features:

- List files and subfolders of a selected directory
- Filter files by extension (include/exclude)
- Filter root directories by name (include)
- Export to text, markup, tree
- Select export name
- Manage options (save/restore form fields using MySQL database)
- View tree structure of exported directory using internal Tree Viewer

## Description

The program is used to list files and subfolders inside a selected local file system path. 
It is possible to customize filters to exclude/include files and include directories. 
Three export options are available:

 - **Export Text** - get text file with the folder structure
 - **Export Markup** - get HTML markup
 - **Export Tree** - get JSON and HTML files for [jsTree](http://jstree.com/) jQuery plugin to view folder structure in tree mode
 
Additional **Export name** field allows set different name for exported files. By default the root folder name is used.
**Manage Options** button toggles side dialog window which allows save current options (path and filters) to the database and then retrieve them using the dropdown list. After selecting a list item it's possible to remove it with the near Remove button.


## Requirements

1. [JRE 1.7.0](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html) installed with the path to the **`jre7/bin/`** folder included in the system **PATH** environment variable.
2. **MySQL** server running and database `list_folders` created (use `db.sql` file to create database with needed structure) if you need to save last program state before closure and use Manage Options dialog. You can use [XAMPP](https://sourceforge.net/projects/xampp/files/) installation and select only MySQL.
3. To export folder structure `export` folder and all its subfolders should exist.


# Install & Run

1. Run the **List Folders.exe** installer and pass all steps.
2. The *Desktop shortcut* should be created.
3. Start the **MySQL** server if needed.
4. Start the program with the shortcut of from the installed directory **`List Folders.exe`** file or using the source **`List Folders.jar`** file.
5. If **JRE** is installed and the **PATH** is configured correctly the main program window should appear.


## Usage

1. Paste a directory path in the **Path** field.
2. Add extensions to **Filter Extensions** to include only files with specified extensions.
3. Add extensions to **Exclude Extensions** to exclude files with these extensions (the **Filter Extensions** text will be ignored).
4. Add folder names (that are subfolders or current directory from the path) to **Filter Directories** to include only these directories.
5. Select **Export Options**

      - **Export Text** - creates `.txt` file with the directory structure in the `export/text` folder.
      - **Export Markup** - creates `.html` file with the same text as for the **Export Text** but with wrapped directory/file names in appropriate <span> elements. The file is created in the `export/markup` folder.
      - **Export Tree** - creates two files: `.html` in the `export/tree` subfolder of the project main folder and `.json` in the `export/tree/json` subfolder.
      The `.html` file can be run directly to view the scanned directory content in the tree format. It uses jsTree plugin for jQuery to give representation and interaction of a file tree on the HTML page.  

6. Using the field **Export Name** you may set your name which will be given to `.html` and `.json` files in the `export/tree` folder when the tree view is exported. Or leave it blank to use the selected directory as the Tree name.
7. Press **Scan Directory**.
8. During the scanning the statistic information should appear in the bottom textarea. 
9. After the scan the `export` subfolders should contain exported files with scanned structure.
9. To check tree view go to `export/tree` and open `.html` file in browser. It should show the directory structure with expandable tree branches.


## Manage Options

1. To **save** current path and filters enter name in the field and press **Add**.
2. To **load** a saved option select a name from the list.
3. To **remove** a saved option select a name and press **Remove**.


## Keyboard shortcuts

- *Esc* - Exit **Main window** / **Manage Options** dialog / **Tree Viewer**
- *Ctrl+R* - Scan directory
- *Ctrl+O* - Browse for directory in the **Main window** / browse for JSON file in the **Tree Viewer**

## Changelog

### [Here](https://sourceforge.net/p/listfolders/wiki/Changelog/)
