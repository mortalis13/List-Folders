
var db=require('./database.js')

/*
 * Adds option with entered name to the database
 */
exports.addOption=function(post){
  var name, value
  name=post.name
  value=post.value
  db.updateOption(name,value)
  return true
}

/*
 * Removes option selected in the dropdown from the database
 */
exports.removeOption=function(post){
  var name, value
  name=post.name
  db.removeOption(name)
  return true
}

/*
 * Loads an option selected in the dropdown
 */
exports.loadOption=function(post,cb){
  var name, res
  name=post.name
  res=db.getOption(name,cb)
  return res
}
