
$(function(){
  
  $('#export-tree').change(function(){
    if(this.checked){
      var treeName=$('<div class="tree-name-wrap">')
      treeName.append('<label id="tree-name-label" for="tree-name">Export Tree name</label>')
      treeName.append('<input type="text" name="tree_name" id="tree-name">')
      treeName.hide()
      $('.options-fields').after(treeName)
      treeName.slideDown(300,function(){
        treeName.find('#tree-name').focus()
      })
    }
    else{
      var treeName=$('.tree-name-wrap')
      treeName.slideUp(300,function(){
        treeName.remove()
      })
    }
  })
  
  $('#clear-ext-filter').click(function(e){
    $('#filter-ext').empty()
    e.preventDefault()
  })
  
  $('#clear-exclude-ext').click(function(e){
    $('#exclude-ext').empty()
    e.preventDefault()
  })
  
  $('#clear-dir-filter').click(function(e){
    $('#filter-dir').empty()
    e.preventDefault()
  })
  
})
