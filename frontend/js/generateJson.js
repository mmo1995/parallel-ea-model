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
            "replacementPolicy": []
            }
var globalPopulationSizeArray = [];
var numberOfIslandsArray = [];
var numberOfSlavesArray = [];
var numberOfGenerationsArray = [];
var migrationRatesArray = [];
var topologyArray = [];
var initialSelectionPolicyArray = [];
var amountFitnessArray = [];
var selectionPolicyArray = [];
var replacementPolicyArray = [];
$(document).ready(function(){
    $("#submit-master").click(function(){
        globalPopulationSizeArray.push($("#population").val());
        numberOfIslandsArray.push(1);
        numberOfSlavesArray.push($("#slaves-number").val());
        numberOfGenerationsArray.push(3);
        migrationRatesArray.push(1);
        topologyArray.push(1);
        initialSelectionPolicyArray.push($("#initial-selection-policy").val());
        amountFitnessArray.push($("#strategy-parameter").val());
        selectionPolicyArray.push("best");
        replacementPolicyArray.push("worst");
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

        experimentJson = JSON.stringify(experiment);
        var jsonObject = JSON.parse(experimentJson);
        console.log(jsonObject);
    });
  });
