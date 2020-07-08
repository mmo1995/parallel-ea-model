$(document).ready(function(){
    $("select#models").change(function(){
    switch($(this).val()) {
        case 'master-slave':
            $("#master-config").show();
            $("#island-config").hide();
            $("#hybrid-config").hide();
            break;
        case 'island':
            $("#master-config").hide();
            $("#island-config").show();
            $("#hybrid-config").hide();
            break;
        case 'hybrid':
            $("#master-config").hide();
            $("#island-config").hide();
            $("#hybrid-config").show();
            break;
        default:
            $("#master-config").hide();
            $("#island-config").hide();
            $("#hybrid-config").hide();
            break;
            
    }
      
    });
  });