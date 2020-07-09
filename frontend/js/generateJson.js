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
          successfullJobSubmition();
          clearFields(); 
    });
  });

  $(document).ready(function(){
    $("#send-experiment").click(function(){
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
  
  }

  function successfullJobSubmition(){
    $("#success-alert").fadeTo(2000,500).slideUp(500, function(){
      $("#success-alert").slideUp(500);
  });
  }
  function failedJobSubmition(){
    $("#failure-alert").fadeTo(2000,500).slideUp(500, function(){
      $("#failure-alert").slideUp(500);
  });
  }