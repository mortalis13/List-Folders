# List Folders

Main features:

- List files and subfolders of a selected directory.
- Filter files by extension (include/exclude).
- Filter root directories by name (include).
- Export to text, tree.
- Select export name.
- View tree structure of exported directory using internal Tree Viewer.

## Description

The program is used to list files and subfolders inside a selected local file system path. 
It is possible to customize filters to exclude/include files and include directories. 
These export options are available:

 - **Export Text** - get text file with the folder structure
 - **Export Tree** - get JSON and HTML files for [jsTree](http://jstree.com/) jQuery plugin to view folder structure in tree mode
 
Additional **Export name** field allows set different name for exported files. By default the root folder name is used.


## Requirements

1. [.NET Framework 4.5](https://www.microsoft.com/en-us/download/details.aspx?id=42642).
2. <u>*\[optional\]*</u> **MySQL** server running and database `list_folders` created (use `db.sql` file to create database with needed structure) if you need to save last program state before closure and use Manage Options dialog. You can use [XAMPP](https://sourceforge.net/projects/xampp/files/) installation and select only MySQL.


# Install & Run

1. Run the **List Folders.exe** or **List Folders.msi** installer and pass all steps.
2. The **Desktop** and **Start Menu** *shortcuts* should be created.
3. Start the **MySQL** server if needed.
4. Start the program with the shortcut or using the **List Folders.exe** file from the installed directory.


## Usage

1. Paste a directory path in the **Path** field.
2. Add extensions to **Filter Extensions** to include only files with specified extensions.
3. Add extensions to **Exclude Extensions** to exclude files with these extensions (the **Filter Extensions** text will be ignored).
4. Add folder names (that are subfolders or current directory from the path) to **Filter Directories** to include only these directories.
5. Select **Export Options**

      - **Export Text** - creates `.txt` file with the directory structure in the `export/text` folder.
      - **Export Tree** - creates two files: `.html` in the `export/tree` subfolder of the project main folder and `.json` in the `export/tree/json` subfolder.
      The `.html` file can be run directly to view the scanned directory content in the tree format. It uses jsTree plugin for jQuery to give representation and interaction of a file tree on the HTML page.  

6. Using the field **Export Name** you may set your name which will be given to `.html` and `.json` files in the `export/tree` folder when the tree view is exported. Or leave it blank to use the selected directory as the Tree name.
7. Press **Scan Directory**.
8. During the scanning the statistic information should appear in the bottom textarea. 
9. After the scan the `export` subfolders should contain exported files with scanned structure.
10. To check tree view go to `export/tree` and open `.html` file in browser. It should show the directory structure with expandable tree branches.


## Tree Viewer

1. Press **Tree Viewer** button.
2. Paste the path to the JSON file with tree structure to the **Path** field or use **Browse** button to select the file. The default browse location should point to the `export/tree/json` folder with exported JSON files.
3. Press **Load Tree** to show the file structure in the main area.


## Notes

- This version is reduced in features in comparison with the **Java program**.
- It doesn't have **Manage Options** feature.
- No shortcuts and accesskeys.
- But its support for *multilanguage* file names seems to be better.
- And it uses Windows native paths and `exe` format for executables.
- If **MySQL** server is not running the status message *"No MySQL Connection"* should appear. And the program is fully functional without the connection. It just doesn't remember the state on exit.


## Changelog

### 1.0.1

- Add: Compatibility with Windows XP
- Add: MySQL dll to the program to load it locally indepenently of existence in the Framework

- Change: .NET Framework required version to 4.0

- Fix: Path to JSON file inside of HTML file for jsTree view (it uses / instead of \)
