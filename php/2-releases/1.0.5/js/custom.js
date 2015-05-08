
$(function(){
  
  $('#options-form').submit(function(){
    var path=$('#path').val()
    x=1
  })
  
  $('.export-option').change(function(){
    var wrapClass=".export-name-wrap"
    
    filedExists=$(wrapClass).length
    
    if(this.checked && !filedExists){
      var exportName=$('<div class="export-name-wrap field">')
      exportName.append('<label id="export-name-label" for="export-name">Export name</label>')
      exportName.append('<input type="text" name="export_name" id="export-name">')
      exportName.hide()
      $('.options-fields').after(exportName)
      exportName.slideDown(300,function(){
        exportName.find('#export-name').focus()
      })
    }
    else{
      var hide=true
      
      $('.export-option').each(function(id,item){
        var checked=item.checked
        if(checked){
          hide=false
          return false
        }
      })
      
      if(hide){
        var exportName=$('.export-name-wrap')
        exportName.slideUp(300,function(){
          exportName.remove()
        })
      }
    }
  })
  
  $('#clear-ext-filter').click(function(e){
    $('#filter-ext').val('')
    e.preventDefault()
  })
  
  $('#clear-exclude-ext').click(function(e){
    $('#exclude-ext').val('')
    e.preventDefault()
  })
  
  $('#clear-dir-filter').click(function(e){
    $('#filter-dir').val('')
    e.preventDefault()
  })
  
  $('#toggle-exports').click(function(e){
    var exports=['export-text','export-markup','export-tree']
    
    exports.forEach(function(item){
      var $item=$("#"+item),
      item=$("#"+item)[0]
      item.checked=!item.checked
      
      $item.trigger('change')
    })
    
    e.preventDefault()
  })
  
  $('#add-option').click(function(e){
    var name=$('#option-name').val()
    if(!name) return false
      
    var value=$('#options-form').serializeArray()
    
    $.post(
      'includes/manage-options.php',
      {action:'add',name:name,value:value},
      function(data){
        var sel=$("#options-list")
        sel.append('<option value="'+name+'">'+name+"</option>")
        
        var noValue=sel.find('option[value=-1]')
        if(noValue.length)
          noValue.remove()
        
        var list=sel.find("option")
        var selected=sel.val()
        
        list.sort(function(a,b) {
          if (a.text > b.text) return 1;
          else if (a.text < b.text) return -1;
          else return 0
        })
        
        sel.empty().append(list);
        sel.val(selected)
      }
    )
    
    e.preventDefault()
  })
  
  $('#remove-option').click(function(e){
    var sel,name
    
    sel=$("#options-list")
    name=sel.val()
    
    $.post(
      'includes/manage-options.php',
      {action:'remove',name:name},
      function(data){
        var opt=sel.find("option[value="+name+"]")
        opt && opt.remove()
      }
    )
    
    e.preventDefault()
  })
  
  $("#options-list").change(function(){
    var name=$(this).val()
    
    $.post(
      'includes/manage-options.php',
      {action:'load',name:name},
      
      function(data){
        for(var i in data){
          var name,value,elem,tag
          
          name=data[i].name
          value=data[i].value
          elem=$("[name="+name+"]")[0]
          tag=elem.tagName.toLowerCase()
          
          switch(tag){
            case 'input':
            case 'textarea':
              elem.value=value
              break
          }
        }
      },
      'json'
    ).fail(function(error) {
      alert( "Error: "+error.status+", "+error.statusText );
    })
    
  })
  
  function handleClick(id, callback){
    $('#'+id).click(callback)
  }
  
})
