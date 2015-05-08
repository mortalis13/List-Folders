
var mysql=require('mysql')

var conn = mysql.createConnection({
  host: '127.0.0.1',
  user: 'root',
  password: '',
  database: 'list_folders_node'
})

var config_table='config'
var options_table='options'

/*
 * Adds or updates option in the 'options' table
 */
exports.updateOption=function(name, value, dbtable){
  var sql, table
  
  table=options_table
  if(dbtable) table=dbtable
  
  sql="select name from "+table+" where name=?"
  conn.query(
    sql,
    [name],
    function(err, rows){
      if(err) throw err
        
      if(!rows.length){
        addOption(name,value,table)
        return
      }
      
      sql="update "+table+" set value=? where name=?"
      conn.query(
        sql,
        [value,name]
      )
    }
  )
}

/*
 * Adds or updates last option in the 'config' table
 * Redirects to the updateOption()
 */
exports.updateConfig=function(name, value){
  exports.updateOption(name,value,config_table)
}

/*
 * Insert option into the database
 * helper funtion for updateOption()
 */
addOption=function(name,value,table){
  var sql="insert into "+table+" (name,value) values(?,?)"
  conn.query(
    sql,
    [name,value]
  )
}

/*
 * Removes option from the database
 */
exports.removeOption=function(name){
  var table=options_table
  var sql="delete from "+table+" where name=?"
  conn.query(
    sql,
    [name]
  )
}

/*
 * Loads last options from the database to assign them to the form fields
 */
exports.loadLastOptions=function(cb){
  var table=config_table
  sql="select value from "+table+" where name='last'"
  conn.query(
    sql,
    function(err, rows){
      if(err) throw err
        
      res=false
      if(rows.length) 
        res=rows[0].value
      cb(res)
    }
  )
}

/*
 * Loads all options from the database to show them in the dropdown
 */
exports.listOptions=function(cb){
  var key,sql
  var table=options_table
  
  key='name'
  sql="select "+key+" from "+table+" order by "+key+" asc"
  
  conn.query(
    sql,
    function(err, rows){
      if(err) throw err
        
      res=false
      if(rows.length){
        res=[]
        for(var i in rows)
          res.push(rows[i].name)
      }
      cb(res)
    }
  )
}

/*
 * Retrieves option from the database when an item is selected in the dropdown
 * to load options set into the form fields
 */
exports.getOption=function(name,cb){
  var key,sql
  var table=options_table
  
  key='name'
  sql="select value from "+table+" where name=?"

  conn.query(
    sql,
    [name],
    function(err, rows){
      if(err) throw err
        
      res=false
      if(rows.length){
        res=rows[0].value
      }
      cb(res)
    }
  )
}
