
var openOnStart=false                              // open main page on server start

var http = require('http')
var parse = require('url').parse
var join = require('path').join
var fs = require('fs')

var jQuery = require('jquery')
var jsdom = require("jsdom")
var qs = require('querystring')
var open = require('open')

var scandir = require("./includes/scandir.js")
var ScanDirectory = scandir.ScanDirectory

var db = require('./includes/database.js')
var man_opt = require('./includes/manage_options.js')

var root = __dirname
var post = {}

var port = 3000, host = 'localhost'

// --------------------------------------------- Start server ---------------------------------------------

var server = http.createServer(function(req, res){
  var url = parse(req.url).pathname
  if(url=='/') url='index.html'
  scandir.text=""

  switch(req.method){
    case 'GET':
      show(res, url)
      break
    case 'POST':
      doPost(req, res, url)
      break
  }
})

server.listen(port)
console.log("Listening to port 3000 ...")
if(openOnStart)
  open("http://localhost:"+port, "firefox")

// --------------------------------------------- Functions ---------------------------------------------

/*
 * Processes POST requests
 */
function doPost(req, res, url){
  var body = ''
  req.on('data', function(chunk){ body += chunk })
  
  if(url=='index.html'){                                  // main page, get post values, scan directory, save config and show the results with the initial form
    req.setEncoding('utf8')
    req.on('end', function(){
      post = qs.parse(body)

      new ScanDirectory(post)
      var value=JSON.stringify(post)
      db.updateConfig('last', value)
      
      show(res, url)
    })
  }else if(url=='/manage-options'){                       // add, remove, list all options in the Manage Options control block
    req.on('end', function(){
      post = qs.parse(body)
      var action=post.action
      var result
      
      switch(action){
        case 'add':
          addOption(res,post)
          break
        case 'remove':
          removeOption(res,post)
          break
        case 'load':
          loadOption(res,post)
          break
      }
    })
  }
}

/*
 * Shows the main page on GET request and after processing POST request
 */
function show(res, url, text) {
  var path = join(root, url)
  var body=""
  
  var stream=fs.createReadStream(path)                          // collect all portions of currently loaded file
  stream.on('data',function(chunk){
    body+=chunk
  })
  
  stream.on('end',function(chunk){                              // on finish collecting change the file and output it (for .html only)
      
    jsdom.env(body, function(err, window){
      if(url=='index.html'){
        var last,path,filterExt,excludeExt,
        filterDir,optionsList
        var $=jQuery(window)                                    // use jQuery
        
        db.loadLastOptions(function(last){                          // the set of nested anonymous functions
          if(last){                                             // needed because of 'mysql' package async nature
            last=JSON.parse(last)
            path=last.path
            filterExt=last.filter_ext
            excludeExt=last.exclude_ext
            filterDir=last.filter_dir
          }                                                     // get all last saved options
          
          setVal('path',path)
          setVal('filter-ext',filterExt)
          setVal('exclude-ext',excludeExt)
          setVal('filter-dir',filterDir)                        // and assign them to document elements
          
          db.listOptions(function(data){                        // next nesting level: load options list to show in the dropdown
            if(!data)
              data="-No options-"
            optionsList=wrapOptions(data)
            
            $('#options-list').html(optionsList)
            
            var html=$('html').html()                           // final wrap and add processed text
            if(scandir.text) html+=scandir.text
            body='<!DOCTYPE html>'+html+'</html>'
            
            res.end(body)                                       // output
          })
        })
        
        function setVal(id,value){                              // helper function
          var elem=$('#'+id)
          var tag=elem[0].tagName.toLowerCase()
          switch(tag){
            case 'input':
              elem.attr('value',value)
              break
            case 'textarea':
              elem.html(value)
              break
          }
        }
      }
      else
        res.end(body)                                           // if not 'index.html' just send the file content without processing
    })
  })
}

// --------------------------------------------- Helpers ---------------------------------------------

/*
 * Adds options set to the database
 */
function addOption(res, post) {
  var result=man_opt.addOption(post)
  res.setHeader("Content-Type", "text/plain")             // needed to prevent js console "Syntax error" on returning from the script
  result && res.end('option-add')
}

/*
 * Removes options set from the database
 */
function removeOption(res, post) {
  var result=man_opt.removeOption(post)
  res.setHeader("Content-Type", "text/plain")
  result && res.end('option-remove')
}

/*
 * Loads all options sets from the database and returns them to the script 
 * so they be shown in the dropdown
 */
function loadOption(res, post) {
  man_opt.loadOption(post,function(result){
    res.setHeader("Content-Type", "text/plain")
    result && res.end(result)
  })
}

/*
 * Prepares options list to be inserted into the dropdown list
 */
function wrapOptions(list){
  if(typeof list=='string')
    return '<option value="-1">'+list+"</option>"                 // if no options then wrap the '-No Options-' string
  
  for(var i in list){
    var option=list[i]
    list[i]="<option value="+option+">"+option+"</option>"
  }
  list=list.join("\n")
  return list
}
