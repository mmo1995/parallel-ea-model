var numberOfJobs = 0;
var experiment = {
            "heteroConfiguration": [],
            "globalPopulationSize": [],
            "numberOfIslands": [],
            "numberOfSlaves": [],
            "numberOfGenerations": [],
            "migrationRate" : [],
            "topology" : [],
            "initialSelectionPolicy": [],
            "amountFitness" : [],
            "initialSelectionPolicyInitializer": [],
            "amountFitnessInitializer": [],
            "selectionPolicy": [],
            "replacementPolicy": [],
            "demeSize": [],
            "asyncMigration": [],
            "acceptRuleForOffspring": [],
            "rankingParameter": [],
            "minimalHammingDistance": [],
            "delay":[],
            "globalTerminationCriterion": [],
            "globalTerminationEpoch": [],
            "globalTerminationEvaluation": [],
            "globalTerminationFitness": [],
            "globalTerminationGeneration": [],
            "globalTerminationTime": [],
            "globalTerminationGDV": [],
            "globalTerminationGAK": [],
            "epochTerminationCriterion": [],
            "epochTerminationEvaluation": [],
            "epochTerminationFitness": [],
            "epochTerminationGeneration": [],
            "epochTerminationTime": [],
            "epochTerminationGDV": [],
            "epochTerminationGAK": [],
            "evoFileName": []
            }
var globalPopulationSizeArray = [];
var numberOfIslandsArray = [];
var numberOfSlavesArray = [];
var numberOfGenerationsArray = [];
var migrationRatesArray = [];
var topologyArray = [];
var initialSelectionPolicyArray = [];
var amountFitnessArray = [];
var initialSelectionPolicyInitializerArray = [];
var amountFitnessInitializerArray = [];
var selectionPolicyArray = [];
var replacementPolicyArray = [];
var demeSizeArray = [];
var asyncMigrationArray = [];
var acceptRuleForOffspringArray = [];
var rankingParameterArray = [];
var minimalHammingDistanceArray = [];
var delayArray = [];
var globalTerminationCriterionArray = [];
var globalTerminationEpochArray = [];
var globalTerminationFitnessArray = [];
var globalTerminationEvaluationArray = [];
var globalTerminationGenerationArray = [];
var globalTerminationTimeArray = [];
var globalTerminationGDVArray = [];
var globalTerminationGAKArray = [];
var epochTerminationCriterionArray = [];
var epochTerminationEvaluationArray = [];
var epochTerminationFitnessArray = [];
var epochTerminationGenerationArray = [];
var epochTerminationTimeArray = [];
var epochTerminationGDVArray = [];
var epochTerminationGAKArray = [];
var heteroArray = [];
var evoFileNameArray = [];
$(document).ready(function(){
    $("#submit").click(function(){
        if(!checkValidation()){
          failedJobSubmition();
        } else{
          globalPopulationSizeArray.push(parseInt($("#population").val()));
          if($("select#models").val()=="master-slave"){
            numberOfIslandsArray.push(1);
            heteroArray.push(false);
          } else {
            if($("#hetero").is(":checked")){
              numberOfIslandsArray.push(parseInt($("#hetero-islands-number").val()));
            }else{
              numberOfIslandsArray.push(parseInt($("#islands-number").val()));
            }
          }
          if($("select#models").val()=="island"){
            numberOfSlavesArray.push(1);
          }else{
            numberOfSlavesArray.push(parseInt($("#slaves-number").val()));
          }
          if($("#hetero").is(":checked")){
            pushHeteroNumberOfGenerations();
          } else{
            numberOfGenerationsArray.push(3);
          }
          
          if($("select#models").val()=="master-slave"){
            migrationRatesArray.push(1);
          } else{
            if($("#hetero").is(":checked")){
              heteroArray.push(true);
              pushHeteroMigrationRate();
            } else{
              heteroArray.push(false);
              migrationRatesArray.push(parseInt($("#migration-rate").val()));
            }
          }
          if($("select#models").val()=="master-slave"){
            topologyArray.push("ring");
          } else{
            topologyArray.push(($("#topology").val()));
          }
          if($("#hetero").is(":checked")){
            pushHeteroInitialSelectionPolicy();
          } else{
            initialSelectionPolicyArray.push($("#initial-selection-policy").val());
            amountFitnessArray.push(parseInt($("#strategy-parameter").val()));
          }
          initialSelectionPolicyInitializerArray.push($("#initial-selection-policy-initializer").val());
          amountFitnessInitializerArray.push(parseInt($("#strategy-parameter-initializer").val()));
          if($("select#models").val()=="master-slave"){
            selectionPolicyArray.push("best");
          } else{
            if($("#hetero").is(":checked")){
              pushHeteroSelectionPolicy();
            } else{
              selectionPolicyArray.push(($("#selection-policy").val()));
            }
            
          }
          if($("select#models").val()=="master-slave"){
            replacementPolicyArray.push("worst");
            asyncMigrationArray.push("false");
          } else{
            if($("#hetero").is(":checked")){
              pushHeteroReplacementPolicy();
            } else{
              replacementPolicyArray.push($("#replacement-policy").val());
            }
            asyncMigrationArray.push($("#async-migration").val());
          }
          if($("#hetero").is(":checked")){
            pushHeteroDemeSize();
          } else{
            demeSizeArray.push(parseInt($("#deme-size").val()));
          }
            if($("#hetero").is(":checked")){
              pushHeteroAcceptRuleOffspring();
              pushHeteroRankingParameter();
              pushHeteroMinimalHammingDistance();
              pushHeteroEvoFile();
            }else{
              acceptRuleForOffspringArray.push($("#accept-offspring").val());
              evoFileNameArray.push($("#evo-file").val());
              rankingParameterArray.push(parseFloat($("#ranking-parameter").val()));
              minimalHammingDistanceArray.push(parseFloat($("#min-hamming").val()));
            }
            delayArray.push(0);
            globalTerminationCriterionArray.push($("#global-criterion").val());
            globalTerminationGenerationArray.push(1000);
            globalTerminationGAKArray.push(0);
            globalTerminationGDVArray.push(0);
            globalTerminationTimeArray.push(0);
            if($("#hetero").is(":not(:checked)")){
              epochTerminationGAKArray.push(0);
              epochTerminationGDVArray.push(0);
              epochTerminationTimeArray.push(0);
            }            
            switch($("#global-criterion").val()){
              case "fitness":
                globalTerminationEpochArray.push(0);
                globalTerminationFitnessArray.push(parseInt($("#global-criterion-limit").val()));
                globalTerminationEvaluationArray.push(0);
                break;
              case "evaluation":
                globalTerminationEpochArray.push(0);
                globalTerminationFitnessArray.push(0);
                globalTerminationEvaluationArray.push(parseInt($("#global-criterion-limit").val()));
                break;
              case "generation":
                globalTerminationEpochArray.push(parseInt($("#global-criterion-limit").val()));
                globalTerminationFitnessArray.push(0);
                globalTerminationEvaluationArray.push(0);
                break;
              default:
                break;
            }
            if($("select#models").val()=="master-slave"){
              epochTerminationCriterionArray.push(globalTerminationCriterionArray[globalTerminationCriterionArray.length-1]);
              epochTerminationEvaluationArray.push(globalTerminationEvaluationArray[globalTerminationEvaluationArray.length-1]);
              epochTerminationFitnessArray.push(globalTerminationFitnessArray[globalTerminationFitnessArray.length-1]);
              epochTerminationGenerationArray.push(globalTerminationEpochArray[globalTerminationEpochArray.length-1]);
            } else{
              if($("#hetero").is(":checked")){
                  pushHeteroEpochTermination();
              } else{
                epochTerminationCriterionArray.push($("#epoch-criterion").val());
                switch($("#epoch-criterion").val()){
                  case "fitness":
                    epochTerminationGenerationArray.push(0);
                    epochTerminationFitnessArray.push(parseInt($("#epoch-criterion-limit").val()));
                    epochTerminationEvaluationArray.push(0);
                    break;
                  case "evaluation":
                    epochTerminationGenerationArray.push(0);
                    epochTerminationFitnessArray.push(0);
                    epochTerminationEvaluationArray.push(parseInt($("#epoch-criterion-limit").val()));
                    break;
                  case "generation":
                    epochTerminationGenerationArray.push(parseInt($("#epoch-criterion-limit").val()));
                    epochTerminationFitnessArray.push(0);
                    epochTerminationEvaluationArray.push(0);
                    break;
                  default:
                    break;
                }
              }
            }
            numberOfJobs++;
            successfullJobSubmition();
            clearFields(); 
            $("#number-submitted-jobs").text("Number Of Submitted Jobs: " + numberOfJobs);
            $("#hetero-islands-config").hide();
        }

    });
  });

  $(document).ready(function(){
    $("#send-experiment").click(function(){
        if(numberOfJobs>0){
          experiment.globalPopulationSize = globalPopulationSizeArray;
          experiment.numberOfIslands = numberOfIslandsArray;
          experiment.numberOfSlaves = numberOfSlavesArray;
          experiment.numberOfGenerations = numberOfGenerationsArray;
          experiment.migrationRate = migrationRatesArray;
          experiment.topology = topologyArray;
          experiment.initialSelectionPolicy = initialSelectionPolicyArray;
          experiment.amountFitness = amountFitnessArray;
          experiment.selectionPolicy = selectionPolicyArray;
          experiment.replacementPolicy = replacementPolicyArray;
          experiment.initialSelectionPolicyInitializer = initialSelectionPolicyInitializerArray;
          experiment.amountFitnessInitializer = amountFitnessInitializerArray;
          experiment.demeSize = demeSizeArray;
          experiment.asyncMigration = asyncMigrationArray;
          experiment.acceptRuleForOffspring = acceptRuleForOffspringArray;
          experiment.rankingParameter = rankingParameterArray;
          experiment.minimalHammingDistance = minimalHammingDistanceArray;
          experiment.globalTerminationCriterion = globalTerminationCriterionArray;
          experiment.globalTerminationEpoch = globalTerminationEpochArray;
          experiment.globalTerminationFitness = globalTerminationFitnessArray;
          experiment.globalTerminationEvaluation = globalTerminationEvaluationArray;
          experiment.globalTerminationGeneration = globalTerminationGenerationArray;
          experiment.globalTerminationTime = globalTerminationTimeArray;
          experiment.globalTerminationGDV = globalTerminationGDVArray;
          experiment.globalTerminationGAK = globalTerminationGAKArray;
          experiment.epochTerminationCriterion = epochTerminationCriterionArray;
          experiment.epochTerminationEvaluation = epochTerminationEvaluationArray;
          experiment.epochTerminationFitness = epochTerminationFitnessArray;
          experiment.epochTerminationGeneration = epochTerminationGenerationArray;
          experiment.epochTerminationTime = epochTerminationTimeArray;
          experiment.epochTerminationGDV = epochTerminationGDVArray;
          experiment.epochTerminationGAK = epochTerminationGAKArray;
          experiment.heteroConfiguration = heteroArray; 
          experiment.delay = delayArray;
          experiment.evoFileName = evoFileNameArray;
          experimentJson = JSON.stringify(experiment);
          var jsonObject = JSON.parse(experimentJson);
          console.log(jsonObject);
          sendJsonExperiment(experimentJson);
          successExperimentSending();
          numberOfJobs = 0;
          clearArrays();
          $("#number-submitted-jobs").text("Number Of Submitted Jobs: " + numberOfJobs);
        } else{
          failedExperimentSending();
        }
    });
  });

function clearFields(){
  $('#islands-number').val('');
  $('#slaves-number').val('');
  $('#population').val('');
  $('#migration-rate').val('');
  $("#initial-selection-policy-form")[0].reset();
  $("#strategy-parameter").val('');
  $("#initial-selection-policy-initializer-form")[0].reset();
  $("#strategy-parameter-initializer").val('');
  $("#topology-form")[0].reset();
  $("#selection-policy-form")[0].reset();
  $("#replacement-policy-form")[0].reset();
  $("#deme-size").val('');
  $("#async-migration-form")[0].reset();
  $("#accept-offspring-form")[0].reset();
  $("#ranking-parameter").val('');
  $("#min-hamming").val('');
  $("#global-criterion-form")[0].reset();
  $("#global-criterion-limit").val('');
  $("#epoch-criterion-form")[0].reset();
  $("#epoch-criterion-limit").val('');
  $("#evo-file-form")[0].reset();

  $("#hetero-islands-number").val('');
  $("#migration-rate-1").val('');
  $("#initial-selection-policy-form-1")[0].reset();
  $("#strategy-parameter-1").val('');
  $("#selection-policy-form-1")[0].reset();
  $("#replacement-policy-form-1")[0].reset();
  $("#epoch-criterion-form-1")[0].reset();
  $("#epoch-criterion-limit-1").val('');
  $("#deme-size-1").val('');
  $("#accept-offspring-form-1")[0].reset();
  $("#ranking-parameter-1").val('');
  $("#min-hamming-1").val('');
  $("#evo-file-form-1")[0].reset();

  $("#migration-rate-2").val('');
  $("#initial-selection-policy-form-2")[0].reset();
  $("#strategy-parameter-2").val('');
  $("#selection-policy-form-2")[0].reset();
  $("#replacement-policy-form-2")[0].reset();
  $("#epoch-criterion-form-2")[0].reset();
  $("#epoch-criterion-limit-2").val('');
  $("#deme-size-2").val('');
  $("#accept-offspring-form-2")[0].reset();
  $("#ranking-parameter-2").val('');
  $("#min-hamming-2").val('');
  $("#evo-file-form-2")[0].reset();

  $("#migration-rate-3").val('');
  $("#initial-selection-policy-form-3")[0].reset();
  $("#strategy-parameter-3").val('');
  $("#selection-policy-form-3")[0].reset();
  $("#replacement-policy-form-3")[0].reset();
  $("#epoch-criterion-form-3")[0].reset();
  $("#epoch-criterion-limit-3").val('');
  $("#deme-size-3").val('');
  $("#accept-offspring-form-3")[0].reset();
  $("#ranking-parameter-3").val('');
  $("#min-hamming-3").val('');
  $("#evo-file-form-3")[0].reset();

  $("#migration-rate-4").val('');
  $("#initial-selection-policy-form-4")[0].reset();
  $("#strategy-parameter-4").val('');
  $("#selection-policy-form-4")[0].reset();
  $("#replacement-policy-form-4")[0].reset();
  $("#epoch-criterion-form-4")[0].reset();
  $("#epoch-criterion-limit-4").val('');
  $("#deme-size-4").val('');
  $("#accept-offspring-form-4")[0].reset();
  $("#ranking-parameter-4").val('');
  $("#min-hamming-4").val('');
  $("#evo-file-form-4")[0].reset();

  $("#migration-rate-5").val('');
  $("#initial-selection-policy-form-5")[0].reset();
  $("#strategy-parameter-5").val('');
  $("#selection-policy-form-5")[0].reset();
  $("#replacement-policy-form-5")[0].reset();
  $("#epoch-criterion-form-5")[0].reset();
  $("#epoch-criterion-limit-5").val('');
  $("#deme-size-5").val('');
  $("#accept-offspring-form-5")[0].reset();
  $("#ranking-parameter-5").val('');
  $("#min-hamming-5").val('');
  $("#evo-file-form-5")[0].reset();
  
  }

  function clearArrays(){
      globalPopulationSizeArray = [];
      numberOfIslandsArray = [];
      numberOfSlavesArray = [];
      numberOfGenerationsArray = [];
      migrationRatesArray = [];
      topologyArray = [];
      initialSelectionPolicyArray = [];
      amountFitnessArray = [];
      initialSelectionPolicyInitializerArray = [];
      amountFitnessInitializerArray = [];
      selectionPolicyArray = [];
      replacementPolicyArray = [];
      demeSizeArray = [];
      asyncMigrationArray = [];
      acceptRuleForOffspringArray = [];
      rankingParameterArray = [];
      minimalHammingDistanceArray = [];
      delayArray = [];
      globalTerminationCriterionArray = [];
      globalTerminationEpochArray = [];
      globalTerminationFitnessArray = [];
      globalTerminationEvaluationArray = [];
      globalTerminationGenerationArray = [];
      globalTerminationTimeArray = [];
      globalTerminationGDVArray = [];
      globalTerminationGAKArray = [];
      epochTerminationCriterionArray = [];
      epochTerminationEvaluationArray = [];
      epochTerminationFitnessArray = [];
      epochTerminationGenerationArray = [];
      epochTerminationTimeArray = [];
      epochTerminationGDVArray = [];
      epochTerminationGAKArray = [];
      heteroArray = [];
      evoFileNameArray = [];
  }

  function successfullJobSubmition(){
    $("#success-alert-job").fadeTo(2000,500).slideUp(500, function(){
      $("#success-alert-job").slideUp(500);
  });
  }
  function failedJobSubmition(){
    $("#failure-alert-job").fadeTo(2000,500).slideUp(500, function(){
      $("#failure-alert-job").slideUp(500);
  });
}
  function successExperimentSending(){
    $("#success-alert-experiment").fadeTo(2000,500).slideUp(500, function(){
      $("#success-alert-experiment").slideUp(500);
  });
  }
  function failedExperimentSending(){
    $("#failure-alert-experiment").fadeTo(2000,500).slideUp(500, function(){
      $("#failure-alert-experiment").slideUp(500);
  });
  }


  function pushHeteroMigrationRate(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroMigrationArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroMigrationArray.push(parseInt($(`#migration-rate-${i}`).val()));
    }
    migrationRatesArray.push(heteroMigrationArray);
  }
  function pushHeteroNumberOfGenerations(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroGenerationAmountArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroGenerationAmountArray.push(3);    
    }
    numberOfGenerationsArray.push(heteroGenerationAmountArray);
  }
  function pushHeteroSelectionPolicy(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroSelectionPolicyArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroSelectionPolicyArray.push($(`#selection-policy-${i}`).val());
    }
    selectionPolicyArray.push(heteroSelectionPolicyArray);
  }
  function pushHeteroReplacementPolicy(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroReplacementPolicyArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroReplacementPolicyArray.push($(`#replacement-policy-${i}`).val());
    }
    replacementPolicyArray.push(heteroReplacementPolicyArray);
  }

  function pushHeteroInitialSelectionPolicy(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroInitialSelectionPolicyArray = [];
    var heteroAmountFitness = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroInitialSelectionPolicyArray.push($(`#initial-selection-policy-${i}`).val());
      heteroAmountFitness.push(parseInt($(`#strategy-parameter-${i}`).val()));
    }
    initialSelectionPolicyArray.push(heteroInitialSelectionPolicyArray);
    amountFitnessArray.push(heteroAmountFitness);
  }

  function pushHeteroEpochTermination(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroEpochTerminationCriterionArray = [];
    var heteroEpochTerminationGinerationArray = [];
    var heteroEpochTerminationFitnessArray = [];
    var heteroEpochTerminationEvaluationArray = [];
    var heteroEpochTerminationGAKArray = [];
    var heteroEpochTerminationGDVArray = [];
    var heteroEpochTerminationTimeArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroEpochTerminationGDVArray.push(0);
      heteroEpochTerminationGAKArray.push(0);
      heteroEpochTerminationTimeArray.push(0);
      heteroEpochTerminationCriterionArray.push($(`#epoch-criterion-${i}`).val());
      switch($(`#epoch-criterion-${i}`).val()){
        case "fitness":
          heteroEpochTerminationGinerationArray.push(0);
          heteroEpochTerminationFitnessArray.push(parseInt($(`#epoch-criterion-limit-${i}`).val()));
          heteroEpochTerminationEvaluationArray.push(0);
          break;
        case "evaluation":
          heteroEpochTerminationGinerationArray.push(0);
          heteroEpochTerminationFitnessArray.push(0);
          heteroEpochTerminationEvaluationArray.push(parseInt($(`#epoch-criterion-limit-${i}`).val()));
          break;
        case "generation":
          heteroEpochTerminationGinerationArray.push(parseInt($(`#epoch-criterion-limit-${i}`).val()));
          heteroEpochTerminationFitnessArray.push(0);
          heteroEpochTerminationEvaluationArray.push(0);
          break;
        default:
          break;
      }
    }
    epochTerminationCriterionArray.push(heteroEpochTerminationCriterionArray);
    epochTerminationGenerationArray.push(heteroEpochTerminationGinerationArray);
    epochTerminationFitnessArray.push(heteroEpochTerminationFitnessArray);
    epochTerminationEvaluationArray.push(heteroEpochTerminationEvaluationArray);
    epochTerminationGAKArray.push(heteroEpochTerminationGAKArray);
    epochTerminationGDVArray.push(heteroEpochTerminationGDVArray);
    epochTerminationTimeArray.push(heteroEpochTerminationTimeArray);
  }
  function pushHeteroDemeSize(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroDemeSizeArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroDemeSizeArray.push(parseInt($(`#deme-size-${i}`).val()));
    }
    demeSizeArray.push(heteroDemeSizeArray);
  }
  function pushHeteroAcceptRuleOffspring(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroAcceptRuleArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroAcceptRuleArray.push($(`#accept-offspring-${i}`).val());
    }
    acceptRuleForOffspringArray.push(heteroAcceptRuleArray);
  }
  function pushHeteroEvoFile(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroEvoFileArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroEvoFileArray.push($(`#evo-file-${i}`).val());
    }
    evoFileNameArray.push(heteroEvoFileArray);
  }
  function pushHeteroRankingParameter(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroRankingParameterArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroRankingParameterArray.push(parseFloat($(`#ranking-parameter-${i}`).val()));
    }
    rankingParameterArray.push(heteroRankingParameterArray);
  }
  function pushHeteroMinimalHammingDistance(){
    var numberOfIslands = parseInt($("#hetero-islands-number").val());
    var heteroMinHammingArray = [];
    for(i = 1; i<=numberOfIslands; i++){
      heteroMinHammingArray.push(parseFloat($(`#min-hamming-${i}`).val()));
    }
    minimalHammingDistanceArray.push(heteroMinHammingArray);
  }

  function sendJsonExperiment(jsonObject){
    $.ajax(
      { 
        url:"http://ea-hybrid.cloud.iai.kit.edu/ojm/start/jobs/frontend",
        type: "POST",
        data: jsonObject,
        contentType: "application/json; charset=utf-8",
        dataType: "json",

      }
    )
  }