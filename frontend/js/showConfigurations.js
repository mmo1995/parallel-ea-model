$(document).ready(function(){
    $("select#models").change(function(){
    switch($(this).val()) {
        case 'master-slave':
            $("#master-config").show();
            $("#island-config").hide();
            $("#hybrid-config").hide();
            $("#buttons").show();
            break;
        case 'island':
            $("#master-config").hide();
            $("#island-config").show();
            $("#hybrid-config").hide();
            $("#buttons").show();
            break;
        case 'hybrid':
            $("#master-config").hide();
            $("#island-config").hide();
            $("#hybrid-config").show();            
            $("#buttons").show();
            break;
        default:
            $("#master-config").hide();
            $("#island-config").hide();
            $("#hybrid-config").hide();
            $("#buttons").hide();
            break;
            
    }
      
    });
  });