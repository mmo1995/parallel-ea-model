var job = {"globalPopulationSize": 0,
            "numberOfIslands": 1,
            "numberOfSlaves": 0,
            "numberOfGenerations": 3,
            "migrationRate" : 1,
            "topology" : "ring",
            "initialSelectionPolicy": "",
            "amountFitness" : 0,
            }
$(document).ready(function(){
    $("#submit-master").click(function(){
        job.globalPopulationSize = $("#population").val();
        job.numberOfIslands = 1;
        job.numberOfSlaves = $("#slaves-number").val();
        job.initialSelectionPolicy = $("#initial-selection-policy").val();
        job.amountFitness = $();
        jobJson = JSON.stringify(job);
        var jsonObject = JSON.parse(jobJson);
        console.log(jsonObject);
    });
  });
