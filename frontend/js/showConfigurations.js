$(document).ready(function(){
    $("select#models").change(function(){
    switch($(this).val()) {
        case 'master-slave':
            $("#config").show();
            $("#island-number-div").hide();
            $("#slave-number-div").show();
            $("#migration-rate-div").hide();
            $("#selection-policy-div").hide();
            $("#replacement-policy-div").hide();
            $("#topology-div").hide();
            $("#async-migration-div").hide();
            $("#buttons").show();
            $("#master-h5").show();
            $("#island-h5").hide();
            $("#hybrid-h5").hide();
            break;
        case 'island':
            $("#config").show();
            $("#island-number-div").show();
            $("#slave-number-div").hide();
            $("#migration-rate-div").show();
            $("#selection-policy-div").show();
            $("#replacement-policy-div").show();
            $("#topology-div").show();
            $("#async-migration-div").show();
            $("#buttons").show();
            $("#master-h5").hide();
            $("#island-h5").show();
            $("#hybrid-h5").hide();
            break;
        case 'hybrid':           
            $("#config").show();
            $("#island-number-div").show();
            $("#slave-number-div").show();
            $("#migration-rate-div").show();
            $("#selection-policy-div").show();
            $("#replacement-policy-div").show();
            $("#topology-div").show();
            $("#async-migration-div").show();
            $("#buttons").show();
            $("#master-h5").hide();
            $("#island-h5").hide();
            $("#hybrid-h5").show();
            break;
        default:
            $("#config").hide();
            break;
            
    }
      
    });
  });