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

        experimentJson = JSON.stringify(experiment);
        var jsonObject = JSON.parse(experimentJson);
        console.log(jsonObject);
    });
  });
