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
            $("#epoch-criterion-div").hide();
            $("#epoch-criterion-limit-div").hide();
            $("#buttons").show();
            $("#master-h5").show();
            $("#island-h5").hide();
            $("#hybrid-h5").hide();
            $("#hetero-homo-div").hide();
            $("#hetero-container").hide();
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
            $("#epoch-criterion-div").show();
            $("#epoch-criterion-limit-div").show();
            $("#buttons").show();
            $("#master-h5").hide();
            $("#island-h5").show();
            $("#hybrid-h5").hide();
            $("#hetero-homo-div").show();
            if($("#hetero").is(":checked")){
                $("#hetero-container").show();
            }
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
            $("#epoch-criterion-div").show();
            $("#epoch-criterion-limit-div").show();
            $("#buttons").show();
            $("#master-h5").hide();
            $("#island-h5").hide();
            $("#hybrid-h5").show();
            $("#hetero-homo-div").show();
            if($("#hetero").is(":checked")){
                $("#hetero-container").show();
            }
            break;
        default:
            $("#config").hide();
            $("#buttons").hide();
            break;      
    }   
    });
  });



  $(document).ready(function(){
    $("#hetero").click(function(){
        if($(this).is(":checked")){
            $("#hetero-container").show();
        }
        else if($(this).is(":not(:checked)")){
            $("#hetero-container").hide();
        }
    });
});
$(document).ready(function(){
    $("#start-hetero-config").click(function(){
        if(validateHeteroIslandsNumber()){
            $("#hetero-islands-config").show();
            createHeteroConfigElements();
        } else {
            failedHeteroConfig();
        }
    });
});

function failedHeteroConfig(){
    $("#failure-alert-hetero").fadeTo(2000,500).slideUp(500, function(){
      $("#failure-alert-hetero").slideUp(500);
  });
}

function createHeteroConfigElements() {
    var islandNumber = parseInt($("#hetero-islands-number").val());
    switch (islandNumber) {
        case 1: 
        showOneIsland();
        break;
        case 2:
        showTwoIslands();
        break;
        case 3:
        showThreeIslands();
        break;
        case 4:
        showFourIslands();
        break;
        case 5:
        showFiveIslands();
        break;
        default:
        break;  

    }
    
}
function showOneIsland(){
    $("#island1-li").show();
    $("#island2-li").hide();
    $("#island3-li").hide();
    $("#island4-li").hide();
    $("#island5-li").hide();
}
function showTwoIslands(){
    $("#island1-li").show();
    $("#island2-li").show();
    $("#island3-li").hide();
    $("#island4-li").hide();
    $("#island5-li").hide();
}
function showThreeIslands(){
    $("#island1-li").show();
    $("#island2-li").show();
    $("#island3-li").show();
    $("#island4-li").hide();
    $("#island5-li").hide();
}
function showFourIslands(){
    $("#island1-li").show();
    $("#island2-li").show();
    $("#island3-li").show();
    $("#island4-li").show();
    $("#island5-li").hide();
}
function showFiveIslands(){
    $("#island1-li").show();
    $("#island2-li").show();
    $("#island3-li").show();
    $("#island4-li").show();
    $("#island5-li").show();
}