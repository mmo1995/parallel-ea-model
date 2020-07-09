var initialSelectionPolicyValidValues = ["new","best","mix","bestNew"];
var topologyValidValues = ["ring", "biRing", "ladder", "complete"];
var selectionPolicyValidValues = ["best", "random"];
var replcaementPolicyValidValues = ["worst","random"];
var asyncMigrationValidValues = ["true","false"];

function checkValidation(){
    var inputIslandsNumber = $("#islands-number").val();
    var inputSlavesNumber = $("#slaves-number").val();
    var inputPopulationSize = $("#population").val();
    var inputMigrationRate = $("#migration-rate").val();
    var inputInitialSelectionPolicy = $("#initial-selection-policy").val();
    var inputStrategyParameter = $("#strategy-parameter").val();
    var inputInitialSelectionPolicyInitializer = $("#initial-selection-policy-initializer").val();
    var inputStrategyParameterInitializer = $("#strategy-parameter-initializer").val();
    var inputTopology = $("#topology").val();
    var inputSelectionPolicy = $("#selection-policy").val();
    var inputReplacementPolicy = $("#replacement-policy").val();
    var inputDemeSize = $("#deme-size").val();
    var inputAsyncMigration = $("#async-migration").val();

    if($("select#models").val()=="island" || $("select#models").val()=="hybrid"){
        if(parseInt(inputIslandsNumber)>5 || parseInt(inputIslandsNumber)<1 || inputIslandsNumber.length == 0){
            return false;
        }
        if(parseInt(inputMigrationRate)>50 || parseInt(inputMigrationRate)<1 || inputMigrationRate.length == 0){
            return false;
        }
        if(topologyValidValues.indexOf(inputTopology)== -1){
            return false;
        }
        if(selectionPolicyValidValues.indexOf(inputSelectionPolicy)== -1){
            return false;
        }
        if(replcaementPolicyValidValues.indexOf(inputReplacementPolicy)== -1){
            return false;
        }
        if(asyncMigrationValidValues.indexOf(inputAsyncMigration)==-1){
            return false;
        }


    }
    if($("select#models").val()=="master-slave" || $("select#models").val()=="hybrid"){
        if(parseInt(inputSlavesNumber)>5 || parseInt(inputSlavesNumber)<1 || inputSlavesNumber.length == 0){
            return false;
        }
    }
    if(parseInt(inputPopulationSize)>500 || parseInt(inputPopulationSize)<50 || inputPopulationSize.length == 0){
        return false;
    }
    if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicy)== -1){
        return false;
    }
    if(parseInt(inputStrategyParameter)>10 || parseInt(inputStrategyParameter)<1 || inputStrategyParameter.length == 0){
        return false;
    }
    if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicyInitializer)==-1){
        return false;
    }
    if(parseInt(inputStrategyParameterInitializer)>10 || parseInt(inputStrategyParameterInitializer)<1 || inputStrategyParameterInitializer.length == 0){
        return false;
    }
    if(parseInt(inputDemeSize)>8 || parseInt(inputDemeSize)<1 || inputDemeSize.length == 0){
        return false;
    }

    

    return true;
}