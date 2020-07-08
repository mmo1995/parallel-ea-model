var job = {"globalPopulationSize": 0, "numberOfIslands": 0, "numberOfSlaves": 0}
$(document).ready(function(){
    $("#submit").click(function(){
        job.globalPopulationSize = $("#population").val();
        job.numberOfIslands = 1;
        job.numberOfSlaves = $("#slaves-number").val();
        jobJson = JSON.stringify(job);
        var jsonObject = JSON.parse(jobJson);
        console.log(jsonObject);
    });
  });
