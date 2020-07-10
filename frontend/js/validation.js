var initialSelectionPolicyValidValues = ["new","best","mix","bestNew"];
var topologyValidValues = ["ring", "biRing", "ladder", "complete"];
var selectionPolicyValidValues = ["best", "random"];
var replcaementPolicyValidValues = ["worst","random"];
var asyncMigrationValidValues = ["true","false"];
var acceptOffspringValidValues = ["always","localLeast","localLeast-ES", "betterParent"];
var globalCriterionValidValues = ["fitness","evaluation","generation"];
var epochCriterionValidValues = ["fitness", "evaluation", "generation"];

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
    var inputAcceptOffspring = $("#accept-offspring").val();
    var inputRankingParameter = $("#ranking-parameter").val();
    var inputMinHamming = $("#min-hamming").val();
    var inputGlobalCriterion = $("#global-criterion").val();
    var inputGlobalLimit = $("#global-criterion-limit").val();
    var inputEpochCriterion = $("#epoch-criterion").val();
    var inputEpochLimit = $("#epoch-criterion-limit").val();
    

    if($("select#models").val()=="island" && $("#hetero").is(":not(:checked)")){
        return validateIslandModel();
    }
    if($("select#models").val()=="hybrid" && $("#hetero").is(":not(:checked)")){
        return validateHybridModel();
    }
    if($("select#models").val()=="master-slave"){
        return validateMasterSlaveModel();
    }
    if($("select#models").val()=="island" && $("#hetero").is(":checked")){
        return validateIslandHeteroModel();
    }
    if($("select#models").val()=="hybrid" && $("#hetero").is(":checked")){
        return validateHybridHeteroModel();
    }
    function validateIslandModel(){
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
        if(epochCriterionValidValues.indexOf(inputEpochCriterion)==-1){
            return false;
        }
        if(inputEpochLimit.length == 0){
            return false;
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
        if(acceptOffspringValidValues.indexOf(inputAcceptOffspring)==-1){
            return false;
        }
        if(parseFloat(inputRankingParameter)>2 || parseFloat(inputRankingParameter)<1 || inputRankingParameter.length == 0){
            return false;
        }
        if(parseFloat(inputMinHamming)>1 || parseFloat(inputMinHamming)<0 || inputMinHamming.length == 0){
            return false;
        }
        if(globalCriterionValidValues.indexOf(inputGlobalCriterion)==-1){
            return false;
        }
        if(inputGlobalLimit.length == 0){
            return false;
        }
        return true;
    }
    function validateMasterSlaveModel(){
        if(parseInt(inputSlavesNumber)>5 || parseInt(inputSlavesNumber)<1 || inputSlavesNumber.length == 0){
            return false;
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
        if(acceptOffspringValidValues.indexOf(inputAcceptOffspring)==-1){
            return false;
        }
        if(parseFloat(inputRankingParameter)>2 || parseFloat(inputRankingParameter)<1 || inputRankingParameter.length == 0){
            return false;
        }
        if(parseFloat(inputMinHamming)>1 || parseFloat(inputMinHamming)<0 || inputMinHamming.length == 0){
            return false;
        }
        if(globalCriterionValidValues.indexOf(inputGlobalCriterion)==-1){
            return false;
        }
        if(inputGlobalLimit.length == 0){
            return false;
        }
        return true;
    }
    function validateHybridModel(){
        if(parseInt(inputIslandsNumber)>5 || parseInt(inputIslandsNumber)<1 || inputIslandsNumber.length == 0){
            return false;
        }
        if(parseInt(inputSlavesNumber)>5 || parseInt(inputSlavesNumber)<1 || inputSlavesNumber.length == 0){
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
        if(epochCriterionValidValues.indexOf(inputEpochCriterion)==-1){
            return false;
        }
        if(inputEpochLimit.length == 0){
            return false;
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
        if(acceptOffspringValidValues.indexOf(inputAcceptOffspring)==-1){
            return false;
        }
        if(parseFloat(inputRankingParameter)>2 || parseFloat(inputRankingParameter)<1 || inputRankingParameter.length == 0){
            return false;
        }
        if(parseFloat(inputMinHamming)>1 || parseFloat(inputMinHamming)<0 || inputMinHamming.length == 0){
            return false;
        }
        if(globalCriterionValidValues.indexOf(inputGlobalCriterion)==-1){
            return false;
        }
        if(inputGlobalLimit.length == 0){
            return false;
        }
        return true;
    }
    function validateIslandHeteroModel(){
        if(!validateHeteroIslandsNumber()){
            return false;
        }
        if(parseInt(inputPopulationSize)>500 || parseInt(inputPopulationSize)<50 || inputPopulationSize.length == 0){
            return false;
        }
        if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicyInitializer)==-1){
            return false;
        }
        if(parseInt(inputStrategyParameterInitializer)>10 || parseInt(inputStrategyParameterInitializer)<1 || inputStrategyParameterInitializer.length == 0){
            return false;
        }
        if(topologyValidValues.indexOf(inputTopology)== -1){
            return false;
        }
        if(asyncMigrationValidValues.indexOf(inputAsyncMigration)==-1){
            return false;
        }
        if(globalCriterionValidValues.indexOf(inputGlobalCriterion)==-1){
            return false;
        }
        if(inputGlobalLimit.length == 0){
            return false;
        }
        switch(parseInt($("#hetero-islands-number").val())){
            case 1:
                return validateIslandOneHetero();
            case 2:
                return validateIslandOneHetero() && validateIslandTwoHetero();
            case 3:
                return validateIslandOneHetero() && validateIslandTwoHetero()
                && validateIslandThreeHetero();
            case 4:
                return validateIslandOneHetero() && validateIslandTwoHetero()
                && validateIslandThreeHetero() && validateIslandFourHetero();
            case 5:
                return validateIslandOneHetero() && validateIslandTwoHetero()
                && validateIslandThreeHetero() && validateIslandFourHetero()
                && validateIslandFiveHetero();
            default:
                return false;
        }
    }
    function validateHybridHeteroModel(){
        if(!validateHeteroIslandsNumber()){
            return false;
        }
        if(parseInt(inputPopulationSize)>500 || parseInt(inputPopulationSize)<50 || inputPopulationSize.length == 0){
            return false;
        }
        if(parseInt(inputSlavesNumber)>5 || parseInt(inputSlavesNumber)<1 || inputSlavesNumber.length == 0){
            return false;
        }
        if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicyInitializer)==-1){
            return false;
        }
        if(parseInt(inputStrategyParameterInitializer)>10 || parseInt(inputStrategyParameterInitializer)<1 || inputStrategyParameterInitializer.length == 0){
            return false;
        }
        if(topologyValidValues.indexOf(inputTopology)== -1){
            return false;
        }
        if(asyncMigrationValidValues.indexOf(inputAsyncMigration)==-1){
            return false;
        }
        if(globalCriterionValidValues.indexOf(inputGlobalCriterion)==-1){
            return false;
        }
        if(inputGlobalLimit.length == 0){
            return false;
        }
        switch(parseInt($("#hetero-islands-number").val())){
            case 1:
                return validateIslandOneHetero();
            case 2:
                return validateIslandOneHetero() && validateIslandTwoHetero();
            case 3:
                return validateIslandOneHetero() && validateIslandTwoHetero()
                && validateIslandThreeHetero();
            case 4:
                return validateIslandOneHetero() && validateIslandTwoHetero()
                && validateIslandThreeHetero() && validateIslandFourHetero();
            case 5:
                return validateIslandOneHetero() && validateIslandTwoHetero()
                && validateIslandThreeHetero() && validateIslandFourHetero()
                && validateIslandFiveHetero();
            default:
                return false;
        }
    }
}


function validateHeteroIslandsNumber(){
    var inputHeteroIslandsNumber = $("#hetero-islands-number").val();
    if(parseInt(inputHeteroIslandsNumber)>5 || parseInt(inputHeteroIslandsNumber)<1 || inputHeteroIslandsNumber.length== 0){
        return false;
    }
    return true;
}

function validateIslandOneHetero() {
    var inputMigrationRate1 = $("#migration-rate-1").val();
    var inputInitialSelectionPolicy1 = $("#initial-selection-policy-1").val();
    var inputStrategyParameter1 = $("#strategy-parameter-1").val();
    var inputSelectionPolicy1 = $("#selection-policy-1").val();
    var inputReplacementPolicy1 = $("#replacement-policy-1").val();
    var inputEpochCriterion1 = $("#epoch-criterion-1").val();
    var inputEpochLimit1 = $("#epoch-criterion-limit-1").val();
    var inputDemeSize1 = $("#deme-size-1").val();
    var inputAcceptOffspring1 = $("#accept-offspring-1").val();
    var inputRankingParameter1 = $("#ranking-parameter-1").val();
    var inputMinHamming1 = $("#min-hamming-1").val();

    if(parseInt(inputMigrationRate1)>50 || parseInt(inputMigrationRate1)<1 || inputMigrationRate1.length == 0){
        return false;
    }
    if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicy1)== -1){
        return false;
    }
    if(parseInt(inputStrategyParameter1)>10 || parseInt(inputStrategyParameter1)<1 || inputStrategyParameter1.length == 0){
        return false;
    }
    if(selectionPolicyValidValues.indexOf(inputSelectionPolicy1)== -1){
        return false;
    }
    if(replcaementPolicyValidValues.indexOf(inputReplacementPolicy1)== -1){
        return false;
    }
    if(epochCriterionValidValues.indexOf(inputEpochCriterion1)==-1){
        return false;
    }
    if(inputEpochLimit1.length == 0){
        return false;
    }
    if(parseInt(inputDemeSize1)>8 || parseInt(inputDemeSize1)<1 || inputDemeSize1.length == 0){
        return false;
    }
    if(acceptOffspringValidValues.indexOf(inputAcceptOffspring1)==-1){
        return false;
    }
    if(parseFloat(inputRankingParameter1)>2 || parseFloat(inputRankingParameter1)<1 || inputRankingParameter1.length == 0){
        return false;
    }
    if(parseFloat(inputMinHamming1)>1 || parseFloat(inputMinHamming1)<0 || inputMinHamming1.length == 0){
        return false;
    }
    return true;
}
function validateIslandTwoHetero() {
    var inputMigrationRate2 = $("#migration-rate-2").val();
    var inputInitialSelectionPolicy2 = $("#initial-selection-policy-2").val();
    var inputStrategyParameter2 = $("#strategy-parameter-2").val();
    var inputSelectionPolicy2 = $("#selection-policy-2").val();
    var inputReplacementPolicy2 = $("#replacement-policy-2").val();
    var inputEpochCriterion2 = $("#epoch-criterion-2").val();
    var inputEpochLimit2 = $("#epoch-criterion-limit-2").val();
    var inputDemeSize2 = $("#deme-size-2").val();
    var inputAcceptOffspring2 = $("#accept-offspring-2").val();
    var inputRankingParameter2 = $("#ranking-parameter-2").val();
    var inputMinHamming2 = $("#min-hamming-2").val();

    if(parseInt(inputMigrationRate2)>50 || parseInt(inputMigrationRate2)<1 || inputMigrationRate2.length == 0){
        return false;
    }
    if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicy2)== -1){
        return false;
    }
    if(parseInt(inputStrategyParameter2)>10 || parseInt(inputStrategyParameter2)<1 || inputStrategyParameter2.length == 0){
        return false;
    }
    if(selectionPolicyValidValues.indexOf(inputSelectionPolicy2)== -1){
        return false;
    }
    if(replcaementPolicyValidValues.indexOf(inputReplacementPolicy2)== -1){
        return false;
    }
    if(epochCriterionValidValues.indexOf(inputEpochCriterion2)==-1){
        return false;
    }
    if(inputEpochLimit2.length == 0){
        return false;
    }
    if(parseInt(inputDemeSize2)>8 || parseInt(inputDemeSize2)<1 || inputDemeSize2.length == 0){
        return false;
    }
    if(acceptOffspringValidValues.indexOf(inputAcceptOffspring2)==-1){
        return false;
    }
    if(parseFloat(inputRankingParameter2)>2 || parseFloat(inputRankingParameter2)<1 || inputRankingParameter2.length == 0){
        return false;
    }
    if(parseFloat(inputMinHamming2)>1 || parseFloat(inputMinHamming2)<0 || inputMinHamming2.length == 0){
        return false;
    }
    return true;
}
function validateIslandThreeHetero() {
    var inputMigrationRate3 = $("#migration-rate-3").val();
    var inputInitialSelectionPolicy3 = $("#initial-selection-policy-3").val();
    var inputStrategyParameter3 = $("#strategy-parameter-3").val();
    var inputSelectionPolicy3 = $("#selection-policy-3").val();
    var inputReplacementPolicy3 = $("#replacement-policy-3").val();
    var inputEpochCriterion3 = $("#epoch-criterion-3").val();
    var inputEpochLimit3 = $("#epoch-criterion-limit-3").val();
    var inputDemeSize3 = $("#deme-size-3").val();
    var inputAcceptOffspring3 = $("#accept-offspring-3").val();
    var inputRankingParameter3 = $("#ranking-parameter-3").val();
    var inputMinHamming3 = $("#min-hamming-3").val();

    if(parseInt(inputMigrationRate3)>50 || parseInt(inputMigrationRate3)<1 || inputMigrationRate3.length == 0){
        return false;
    }
    if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicy3)== -1){
        return false;
    }
    if(parseInt(inputStrategyParameter3)>10 || parseInt(inputStrategyParameter3)<1 || inputStrategyParameter3.length == 0){
        return false;
    }
    if(selectionPolicyValidValues.indexOf(inputSelectionPolicy3)== -1){
        return false;
    }
    if(replcaementPolicyValidValues.indexOf(inputReplacementPolicy3)== -1){
        return false;
    }
    if(epochCriterionValidValues.indexOf(inputEpochCriterion3)==-1){
        return false;
    }
    if(inputEpochLimit3.length == 0){
        return false;
    }
    if(parseInt(inputDemeSize3)>8 || parseInt(inputDemeSize3)<1 || inputDemeSize3.length == 0){
        return false;
    }
    if(acceptOffspringValidValues.indexOf(inputAcceptOffspring3)==-1){
        return false;
    }
    if(parseFloat(inputRankingParameter3)>2 || parseFloat(inputRankingParameter3)<1 || inputRankingParameter3.length == 0){
        return false;
    }
    if(parseFloat(inputMinHamming3)>1 || parseFloat(inputMinHamming3)<0 || inputMinHamming3.length == 0){
        return false;
    }
    return true;
}
function validateIslandFourHetero() {
    var inputMigrationRate4 = $("#migration-rate-4").val();
    var inputInitialSelectionPolicy4 = $("#initial-selection-policy-4").val();
    var inputStrategyParameter4 = $("#strategy-parameter-4").val();
    var inputSelectionPolicy4 = $("#selection-policy-4").val();
    var inputReplacementPolicy4 = $("#replacement-policy-4").val();
    var inputEpochCriterion4 = $("#epoch-criterion-4").val();
    var inputEpochLimit4 = $("#epoch-criterion-limit-4").val();
    var inputDemeSize4 = $("#deme-size-4").val();
    var inputAcceptOffspring4 = $("#accept-offspring-4").val();
    var inputRankingParameter4 = $("#ranking-parameter-4").val();
    var inputMinHamming4 = $("#min-hamming-4").val();

    if(parseInt(inputMigrationRate4)>50 || parseInt(inputMigrationRate4)<1 || inputMigrationRate4.length == 0){
        return false;
    }
    if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicy4)== -1){
        return false;
    }
    if(parseInt(inputStrategyParameter4)>10 || parseInt(inputStrategyParameter4)<1 || inputStrategyParameter4.length == 0){
        return false;
    }
    if(selectionPolicyValidValues.indexOf(inputSelectionPolicy4)== -1){
        return false;
    }
    if(replcaementPolicyValidValues.indexOf(inputReplacementPolicy4)== -1){
        return false;
    }
    if(epochCriterionValidValues.indexOf(inputEpochCriterion4)==-1){
        return false;
    }
    if(inputEpochLimit4.length == 0){
        return false;
    }
    if(parseInt(inputDemeSize4)>8 || parseInt(inputDemeSize4)<1 || inputDemeSize4.length == 0){
        return false;
    }
    if(acceptOffspringValidValues.indexOf(inputAcceptOffspring4)==-1){
        return false;
    }
    if(parseFloat(inputRankingParameter4)>2 || parseFloat(inputRankingParameter4)<1 || inputRankingParameter4.length == 0){
        return false;
    }
    if(parseFloat(inputMinHamming4)>1 || parseFloat(inputMinHamming4)<0 || inputMinHamming4.length == 0){
        return false;
    }
    return true;
}
function validateIslandFiveHetero() {
    var inputMigrationRate5 = $("#migration-rate-5").val();
    var inputInitialSelectionPolicy5 = $("#initial-selection-policy-5").val();
    var inputStrategyParameter5 = $("#strategy-parameter-5").val();
    var inputSelectionPolicy5 = $("#selection-policy-5").val();
    var inputReplacementPolicy5 = $("#replacement-policy-5").val();
    var inputEpochCriterion5 = $("#epoch-criterion-5").val();
    var inputEpochLimit5 = $("#epoch-criterion-limit-5").val();
    var inputDemeSize5 = $("#deme-size-5").val();
    var inputAcceptOffspring5 = $("#accept-offspring-5").val();
    var inputRankingParameter5 = $("#ranking-parameter-5").val();
    var inputMinHamming5 = $("#min-hamming-5").val();

    if(parseInt(inputMigrationRate5)>50 || parseInt(inputMigrationRate5)<1 || inputMigrationRate5.length == 0){
        return false;
    }
    if(initialSelectionPolicyValidValues.indexOf(inputInitialSelectionPolicy5)== -1){
        return false;
    }
    if(parseInt(inputStrategyParameter5)>10 || parseInt(inputStrategyParameter5)<1 || inputStrategyParameter5.length == 0){
        return false;
    }
    if(selectionPolicyValidValues.indexOf(inputSelectionPolicy5)== -1){
        return false;
    }
    if(replcaementPolicyValidValues.indexOf(inputReplacementPolicy5)== -1){
        return false;
    }
    if(epochCriterionValidValues.indexOf(inputEpochCriterion5)==-1){
        return false;
    }
    if(inputEpochLimit5.length == 0){
        return false;
    }
    if(parseInt(inputDemeSize5)>8 || parseInt(inputDemeSize5)<1 || inputDemeSize5.length == 0){
        return false;
    }
    if(acceptOffspringValidValues.indexOf(inputAcceptOffspring5)==-1){
        return false;
    }
    if(parseFloat(inputRankingParameter5)>2 || parseFloat(inputRankingParameter5)<1 || inputRankingParameter5.length == 0){
        return false;
    }
    if(parseFloat(inputMinHamming5)>1 || parseFloat(inputMinHamming5)<0 || inputMinHamming5.length == 0){
        return false;
    }
    return true;
}