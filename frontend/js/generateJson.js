var numberOfJobs = 0;
var experiment = {"globalPopulationSize": [],
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
            "epochTerminationGAK": []
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
$(document).ready(function(){
    $("#submit").click(function(){
        if(!checkValidation()){
          failedJobSubmition();
        } else{
          globalPopulationSizeArray.push(parseInt($("#population").val()));
          if($("select#models").val()=="master-slave"){
            numberOfIslandsArray.push(1);
          } else {
            numberOfIslandsArray.push(parseInt($("#islands-number").val()));
          }
          if($("select#models").val()=="island"){
            numberOfSlavesArray.push(1);
          }else{
            numberOfSlavesArray.push(parseInt($("#slaves-number").val()));
          }
          numberOfGenerationsArray.push(3);
          if($("select#models").val()=="master-slave"){
            migrationRatesArray.push(1);
          } else{
            migrationRatesArray.push(parseInt($("#migration-rate").val()));
          }
          if($("select#models").val()=="master-slave"){
            topologyArray.push("ring");
          } else{
            topologyArray.push(($("#topology").val()));
          }
          initialSelectionPolicyArray.push($("#initial-selection-policy").val());
          amountFitnessArray.push(parseInt($("#strategy-parameter").val()));
          initialSelectionPolicyInitializerArray.push($("#initial-selection-policy-initializer").val());
          amountFitnessInitializerArray.push(parseInt($("#strategy-parameter-initializer").val()));
          if($("select#models").val()=="master-slave"){
            selectionPolicyArray.push("best");
          } else{
            selectionPolicyArray.push(($("#selection-policy").val()));
          }
          if($("select#models").val()=="master-slave"){
            replacementPolicyArray.push("worst");
            asyncMigrationArray.push("false");
          } else{
            replacementPolicyArray.push($("#replacement-policy").val());
            asyncMigrationArray.push($("#async-migration").val());
          }
            demeSizeArray.push(parseInt($("#deme-size").val()));
            acceptRuleForOffspringArray.push($("#accept-offspring").val());
            rankingParameterArray.push(parseFloat($("#ranking-parameter").val()));
            minimalHammingDistanceArray.push(parseFloat($("#min-hamming").val()));
            delayArray.push(0);
            globalTerminationCriterionArray.push($("#global-criterion").val());
            globalTerminationGenerationArray.push(1000);
            globalTerminationGAKArray.push(0);
            globalTerminationGDVArray.push(0);
            globalTerminationTimeArray.push(0);
            epochTerminationGAKArray.push(0);
            epochTerminationGDVArray.push(0);
            epochTerminationTimeArray.push(0);
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
            numberOfJobs++;
            successfullJobSubmition();
            clearFields(); 
            $("#number-submitted-jobs").text("Number Of Submitted Jobs: " + numberOfJobs);
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
          experimentJson = JSON.stringify(experiment);
          var jsonObject = JSON.parse(experimentJson);
          console.log(jsonObject);
          console.log("Number Of Jobs: " + numberOfJobs);
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

  $("#hetero-islands-number").val('');
  $("#migration-rate-1").val('');
  $("#initial-selection-policy-1").val();
  $("#strategy-parameter-1").val('');
  $("#selection-policy-1").val();
  $("#replacement-policy-1").val();
  $("#epoch-criterion-1").val();
  $("#epoch-criterion-limit-1").val('');
  $("#deme-size-1").val('');
  $("#accept-offspring-1").val();
  $("#ranking-parameter-1").val('');
  $("#min-hamming-1").val('');
  $("#migration-rate-2").val('');
  $("#initial-selection-policy-2").val();
  $("#strategy-parameter-2").val('');
  $("#selection-policy-2").val();
  $("#replacement-policy-2").val();
  $("#epoch-criterion-2").val();
  $("#epoch-criterion-limit-2").val('');
  $("#deme-size-2").val('');
  $("#accept-offspring-2").val();
  $("#ranking-parameter-2").val('');
  $("#min-hamming-2").val('');
  $("#migration-rate-3").val('');
  $("#initial-selection-policy-3").val();
  $("#strategy-parameter-3").val('');
  $("#selection-policy-3").val();
  $("#replacement-policy-3").val();
  $("#epoch-criterion-3").val();
  $("#epoch-criterion-limit-3").val('');
  $("#deme-size-3").val('');
  $("#accept-offspring-3").val();
  $("#ranking-parameter-3").val('');
  $("#min-hamming-3").val('');
  $("#migration-rate-4").val('');
  $("#initial-selection-policy-4").val();
  $("#strategy-parameter-4").val('');
  $("#selection-policy-4").val();
  $("#replacement-policy-4").val();
  $("#epoch-criterion-4").val();
  $("#epoch-criterion-limit-4").val('');
  $("#deme-size-4").val('');
  $("#accept-offspring-4").val();
  $("#ranking-parameter-4").val('');
  $("#min-hamming-4").val('');
  $("#migration-rate-5").val('');
  $("#initial-selection-policy-5").val();
  $("#strategy-parameter-5").val('');
  $("#selection-policy-5").val();
  $("#replacement-policy-5").val();
  $("#epoch-criterion-5").val();
  $("#epoch-criterion-limit-5").val('');
  $("#deme-size-5").val('');
  $("#accept-offspring-5").val();
  $("#ranking-parameter-5").val('');
  $("#min-hamming-5").val('');
  
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