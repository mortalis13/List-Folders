
var fs=require('fs')
var path="C:/1-Roman/Documents/8-test/test"

exports.text=""
var nl='\n'

exports.ScanDirectory=function(post){
  var path, filterExt, excludeExt, filterDir
  
  this.iconsPath="./lib/images/"
  this.dirIcon=this.iconsPath+"directory.png"
  this.pad="    "
  
  this.path=this.formatPath(post.path)
  this.filterExt=this.getFilters(post.filter_ext)
  this.excludeExt=this.getFilters(post.exclude_ext)
  this.filterDir=this.getFilters(post.filter_dir)
  
  this.doExportText=post.export_text
  this.doExportMarkup=post.export_markup
  this.doExportTree=post.export_tree
  
  this.exportName=this.trim(post.export_name)
  
  this.text=[]
  this.markup=[]
  
  this.exts=[
    "chm", "css", "djvu", "dll", "doc",
    "exe", "html", "iso", "js", "msi",
    "pdf", "php", "psd", "rar", "txt",
    "xls", "xml", "xpi", "zip",
  ]

  this.imageExts=[
    "png", "gif", "jpg", "jpeg", "tiff", "bmp",
  ]

  this.musicExts=[
    "mp3", "wav", "ogg", "alac", "flac",
  ]

  this.videoExts=[
    "mkv", "flv", "vob", "avi", "wmv",
    "mov", "mp4", "mpg", "mpeg", "3gp",
  ]
  
  this.codeExts =[
    "c", "cpp", "cs", "java",
  ]
  
  this.processData()                                   // << Start point >>
}

exports.ScanDirectory.prototype={
  
  /*
   * Scans the directory
   * Outputs the result as text
   * Exports result if checkboxes are selected
   */  
  processData: function(){
    var text,json
    
    json=this.fullScan(this.path,-1)
    this.json=JSON.stringify(json)
    
    if(!this.text.length){
      console.log("No Data!")
      return
    } 
    
    text=this.text.join('\n')
    exports.text=this.wrapText(text)
    
    if(this.doExportText) this.exportText(text)
    if(this.doExportMarkup) this.exportMarkup()
    if(this.doExportTree) this.exportTree()
  },
    
  /*
   * Recursive scans all subdirectories
   */
  fullScan: function(dir,level){
    var self=this,
    data,list,pad,count,passed
    var json=[]
    
    data=fs.readdirSync(dir)
    list=this.prepareData(data,dir)                   // clean of filtered dirs and exts, sort by name and put directories first
    pad=this.getPadding(level)                        // spaces from this.pad
    
    count=list.length
    // if(!count) return false                        // if no files/subdirs or files excluded by filters don't show this dir
      
    list.forEach(function(value){
      var item=dir+'/'+value
      
      if(self.isDir(item)){
        passed=true
        if(self.filterDir && level==-1){
          passed=self.filterDirectory(value);          // filter directories
        }
        if(!passed) return
        
        var currentDir='['+value+']'
        self.markup.push(pad+self.wrapDir(currentDir))      // add markup as new line
        self.text.push(pad+currentDir)                      // add text as new line
        var res=self.fullScan(item,level+1)
        
        json.push({
          text: self.fixEncoding(value),                    // special json formatting for jsTree.js                   
          icon: self.dirIcon,
          children: res
        })
      }
      else{
        var currentFile=value
        self.markup.push(pad+self.wrapFile(currentFile))
        self.text.push(pad+currentFile)
        
        json.push({
          text: self.fixEncoding(value),            
          icon: self.getIcon(value)
        })
      }
      
    })

    // if(!count) return false
    return json
  },

  /*
   * Filters files and folders
   * Sorts by name and directories-first order
   */
  prepareData: function(data,dir){
    var self=this,
    folders=[], files=[], list
    
    data.forEach(function(value){
      var item=dir+'/'+value
      if (self.isDir(item))
        folders.push(value)
      else if(self.filterFile(value))
        files.push(value)
    })
    
    list=this.getList(folders,files)
    return list
  },
  
  /*
   * Merge folders and files arrays
   */
  getList: function(folders,files){
    folders.sort()
    files.sort()
    var list=folders.concat(files)
    return list
  },
  
// --------------------------------------------- helpers ---------------------------------------------
  
  /*
   * Formats path, fixes backslashes, trims and removes last slash
   */
  formatPath: function(path){
    path=path.replace(/\\/g,"/")
    path=path.trim()
    
    if(path.substr(-1)=="/")
      path=path.substr(0,path.length-1)
    
    return path
  },
  
  /*
   * Checks if the item is directory
   */
  isDir: function(item){
    var stat=fs.statSync(item)
    var res=stat && stat.isDirectory()
    return res
  },
  
  fixEncoding: function(value){
    return value
  },
  
  /*
   * Replaces strings from the tree template (strings format: '_string_') with the 'replacement' text
   */
  replaceTemplate: function(tmpl, replacement, text){
    var text=text.replace(tmpl, replacement)
    return text
  },
  
  /*
   * Outputs padding spaces for text output depending on nesting level
   */
  getPadding: function(level){
    var resPad=""
    for(var i=0;i<=level;i++)
      resPad+=this.pad
    return resPad
  },  
  
  /*
   * Returns icon path for the tree view
   */
  getIcon: function(file){
    var ext, icon, path, iconExt, useDefault 
    
    ext=""
    icon="jstree-file"
    path=this.iconsPath
    iconExt=".png"
    
    var rx=new RegExp("\\.[\\w]+$")
    ext=rx.exec(file)
    if(!ext) return icon
    ext=ext[0].substr(1)
    
    useDefault=true
    
    if(useDefault){
      for(var i in this.exts){
        var item=this.exts[i]
        if(ext==item){
          icon=path+item+iconExt
          useDefault=false
          break
        }
      }
    }
    
    if(useDefault){
      for(var i in this.imageExts){
        var item=this.imageExts[i]
        if(ext==item){
          icon=path+"image"+iconExt
          useDefault=false
          break
        }
      }
    }
    
    if(useDefault){
      for(var i in this.musicExts){
        var item=this.musicExts[i]
        if(ext==item){
          icon=path+"music"+iconExt
          useDefault=false
          break
        }
      }
    }
    
    if(useDefault){
      for(var i in this.videoExts){
        var item=this.videoExts[i]
        if(ext==item){
          icon=path+"video"+iconExt
          useDefault=false
          break
        }
      }
    }
    
    if(useDefault){
      for(var i in this.codeExts){
        var item=this.codeExts[i]
        if(ext==item){
          icon=path+"code"+iconExt
          useDefault=false
          break
        }
      }
    }
    
    return icon
  },
  
  /*
   * Cleans, trims and checks filters for emptiness
   */
  getFilters: function(filter){
    filter=filter && filter.trim()
    
    if(filter){
      filter=filter.split("\n")
      for(var i in filter){
        filter[i]=filter[i].trim()
      }
    }
    return filter
  },
  
  /*
   * Filters file extensions and returns true if the file will be included in the output
   * If exclude filter is not empty ignores the include filter
   */
  filterFile: function(value){
    if(this.excludeExt){
      for(var i in this.excludeExt){
        var ext=this.excludeExt[i]
        var rx=new RegExp("\\."+ext+"$")
        if(rx.test(value))
          return false
      }
      return true
    }
    
    if(!this.filterExt) return true
    for(var i in this.filterExt){
      var ext=this.filterExt[i]
      var rx=new RegExp("\\."+ext+"$")
      if(rx.test(value))
        return true
    }
    return false
  },
  
  /*
   * Uses form filter to filter directories from the first scanning level
   */
  filterDirectory: function(dir){
    for(var i in this.filterDir) {
      var filter=this.filterDir[i]
      if(filter==dir)
        return true
    }
    return false
  },
  
  
  /*
   * Gets text for the tree template
   */
  getFiltersText: function(){
    var filterExt="", filterDir="", filters
    
    if(this.filterExt){
      filterExt=this.filterExt.join(",")
    }
    if(this.filterDir){
      filterDir=this.filterDir.join(",")
    }
    
    filters='Files ['+filterExt+']'
    filters+=', Directories ['+filterDir+']'
    
    return filters
  },
  
  /*
   * Trims string if it's not null
   */
  trim: function(value){
    return value && value.trim()
  },
  
// --------------------------------------------- wrappers ---------------------------------------------
  
  wrapDir: function(dir){
    return '<span class="directory">'+dir+'</span>'
  },
  
  wrapFile: function(file){
    return '<span class="file">'+file+'</span>'
  },
  
  wrapText: function(text){
    return '<pre>'+text+'</pre>'
  },
  
  wrapMarkup: function(markup){
    markup='<pre>'+nl+markup+nl+'</pre>'
    markup=this.wrapDocument(markup)
    return markup
  },
  
  wrapDocument: function(markup){
    return '<meta charset="utf-8">'+nl+markup
  },
  
// --------------------------------------------- exports ---------------------------------------------
  
  /*
   * Exports text to a .txt file in 'export/text'
   */
  exportText: function(text){
    var exportPath, ext, filename
    exportPath='export/text/'
    ext=".txt"
    name=this.getExportName(ext)
    fs.writeFileSync(exportPath+name,text)
  },
  
  /*
   * Exports HTML markup to a .hmtl file in 'export/markup'
   */
  exportMarkup: function(){
    var exportPath, ext, filename, markup
    markup=this.markup.join('\n')
    markup=this.wrapMarkup(markup)
    exportPath='export/markup/'
    ext=".html"
    name=this.getExportName(ext)
    fs.writeFileSync(exportPath+name,markup)
  },
  
  /*
   * Exports .json and .html files to the 'export/tree'
   * The .html file can be used directly to view the tree
   * The jsTree plugin must be in the 'tree/lib'
   *
   * The method gets the .html template from 'templates/tree.html', 
   * replaces template strings with the current data and create new .html in the 'exports/tree'
   * Then creates .json in the 'exports/tree/json' which is read by the script in the exported .html page
   */
  exportTree: function(){
    var exportPath, treeName, tmpl, doc, filters
    var json, jsonFolder, jsonPath,
    exportDoc, exportJSON
    
    json=this.json
    
    treeName=this.getExportName()
    tmpl='templates/tree.html'
    
    exportPath="export/tree/"
    jsonFolder="json/"
    jsonPath=exportPath+jsonFolder
    
    exportDoc=treeName+".html"
    exportJSON=treeName+".json"
    
    doc=fs.readFileSync(tmpl,'utf8')
    
    doc=this.replaceTemplate("_jsonPath_", jsonFolder+exportJSON, doc)
    doc=this.replaceTemplate("_Title_", 'Directory: '+treeName, doc)
    doc=this.replaceTemplate("_FolderPath_", 'Directory: '+this.path, doc)
    
    filters=this.getFiltersText()
    doc=this.replaceTemplate("_Filters_", "Filters: "+filters, doc)
    
    fs.writeFileSync(exportPath+exportDoc,doc)
    fs.writeFileSync(jsonPath+exportJSON,json)
  },
  
  /*
   * Returns the name that will be used to export 
   * text, markup and tree views of the directory structure
   */
  getExportName: function(ext){
    var useCurrentDir,exportName,name
    
    useCurrentDir=true
    exportName="no-name"
    
    if(this.exportName){
      exportName=this.exportName
      useCurrentDir=false
    }
    
    if(useCurrentDir){
      var rx=new RegExp("/[^/]+$")
      exportName=rx.exec(this.path)
      exportName=exportName[0].substr(1)
    }
    
    name=exportName
    if(ext)
      name=exportName+ext
    
    return name
  },
}

// new ScanDirectory(path)
