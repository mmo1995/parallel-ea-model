package edu.kit.iai.gleam;


import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@CrossOrigin(origins = "*")
@RestController
public class StarterController {
    @Autowired

    private static int l = 0;
    private static int taskID = 0;
    private static int WFID = 0;
    private static RestTemplate restTemplate = new RestTemplate();
    private static String splittingJoining = "localhost:8074";
    private static String coordination = "localhost:8071";
    // private static String splittingJoining = "splitting-joining-hybrid:8074";
    // private static String coordination = "coordination-hybrid:8071";
    private Map<String, String> finalResultCol = new HashMap<>();
    private String finalplan;
    private int numberOfChromosomes;
    private int numberOfGeneration = 0;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //  @Value("${containers}")


/*******************************************************************************************/
    /**
     * Get an ID for the task
     *
     * @throws IOException
     **/
    @RequestMapping(value = "/opt/{islandNumber}/taskID", method = RequestMethod.GET)
    public String getIDforTask(@PathVariable int islandNumber){
        synchronized (this){
            System.out.println(islandNumber);
            String returnedTaskID = String.valueOf(taskID);
            taskID++;
            return  returnedTaskID;
        }


       /* @SuppressWarnings("resource")
        BufferedReader input = new BufferedReader(new FileReader("taskID.txt"));
        String last = "0", line;
        while ((line = input.readLine()) != null) {
            last = line;
        }
        taskID = Integer.parseInt(last);
        taskID++;
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("taskID.txt"), "utf-8"))) {
            writer.write(taskID + "\n");
        }

        if (taskID > Integer.parseInt(last) && taskID > 0) {
//            System.out.println("----------------------------------------------------------------------------------- \n");
//            System.out.println("----------------------------------------------------------------------------------- \n");
//            System.out.println("TaskID is " + taskID);
//            System.out.println(new java.util.Date());
            StringBuilder sb = new StringBuilder();
            sb.append("");
            taskID = 1;
            sb.append(taskID);
           String strI = sb.toString();
            return strI;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            String strI = sb.toString();
            return strI;

        }*/
    }

/** End of the get an ID for the task function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Reset the ID of the task
     *
     * @throws IOException
     **/


    @RequestMapping(value = "/opt/{islandNumber}/resetTaskID", method = RequestMethod.GET)
    public int resttaskID(@PathVariable int islandNumber) throws IOException {


        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("taskID.txt"), "utf-8"))) {
            writer.write(taskID + "\n");
        }

        return taskID;
    }

/** End of the reset the ID of the task  function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Check if a specific model exists or not
     **/
    @RequestMapping(value = "/opt/{islandNumber}/{id}/{nameOfModel}/active", method = RequestMethod.GET)
    public String checkModel(@PathVariable String id,
                             @PathVariable String nameOfModel,
                             @PathVariable int islandNumber) {

        synchronized (this){return "yes";}
/*        if (!id.equals("0") && Integer.parseInt(id) == taskID)

            return "yes";

        else
            return "no";*/
    }

/** End of the check if a specific model exists or not  function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Get the available models
     **/
    @RequestMapping(value = "/opt/{islandNumber}/{id}/models", method = RequestMethod.GET)
    public String getModels(@PathVariable String id, @PathVariable int islandNumber) {
        synchronized (this){String models = "OpalDemo\n" + "OpalDemo1\n" + "OpalDemo2\n";
        return models;}
/*        if (!id.equals("0") && Integer.parseInt(id) == taskID) {
            String models = "OpalDemo\n" + "OpalDemo1\n" + "OpalDemo2\n";
            return models;
        } else
            return "";*/
    }

/** End of the check if a specific model exists or not  function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Send the Configuration related to the task
     **/
    @RequestMapping(value = "/opt/{islandNumber}/{id}/start", method = RequestMethod.POST)
    public String startTask(@PathVariable String id,@PathVariable int islandNumber, @RequestBody String configuration) {
        synchronized (this) {
            WFID = 1;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(WFID);
            String strI = sb.toString();
            logger.info("one optimization job is started with  id " + id);
            return strI;
/*        if (!id.equals("0") && Integer.parseInt(id) == taskID) {
//            System.out.println("Configurations are: \n" + configuration);
            *//** TODO processing the Configuration**//*
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(WFID);
            String strI = sb.toString();
            logger.info("one optimization job is started");
            return strI;
        } else
            return "-1";*/
        }
    }

/** End of the check if a specific model exists or not  function**/


/*******************************************************************************************/
    /**
     * Stop signal to end the task
     **/
    @RequestMapping(value = "/opt/{islandNumber}/{id}/stop", method = RequestMethod.GET)
    public String stopTask(@PathVariable String id, @PathVariable int islandNumber) {
        synchronized (this) {
            if (true) {
                numberOfGeneration = 0;
                finalResultCol.put(id, ".");
                logger.info("one optimization job is finishedwith id "+ id);
                logger.info("********************************");
                return "1";

/*            if (!id.equals("0") && Integer.parseInt(id) == taskID)
            {
                //ResponseEntity<String> answer1 = restTemplate.postForEntity("http://" + coordination +"/ojm/result", finalResultCol+"#"+finalplan, String.class);
                numberOfGeneration = 0;
                logger.info("one optimization job is finished");
                logger.info("********************************");
                return "1";
            }
            else
                return "-1";*/
            } else {
                return "-2";
            }
        }
    }

/** End of the stop signal to end the task function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Abort signal to end the task in a forced way
     **/
    @RequestMapping(value = "/opt/{islandNumber}/{id}/{wfid}/abort", method = RequestMethod.GET)
    public String abortTask(@PathVariable String id, @PathVariable String wfid, @PathVariable int islandNumber) {
        synchronized (this){return "1";}
/*        String status = "";
        if (true) {
            if (Integer.parseInt(id) == taskID && Integer.parseInt(wfid) == WFID) {

                status = "1";// id and wfid are right
            } else if (Integer.parseInt(id) != taskID && Integer.parseInt(wfid) != WFID) {

                status = "-1";// id and wfid are right
            } else if (Integer.parseInt(id) != taskID && Integer.parseInt(wfid) == WFID)

            {

                status = "-2"; // id is wrong
            } else

            {

                status = "-3";// wfid is wrong
            }
            return status;
        } else {
            return "-4";
        }*/
    }

/** End of Abort signal to end the task in a forced way **/


/*******************************************************************************************/
    /**
     * Send the chromosome list related to the task
     **/

    @RequestMapping(value = "/opt/{islandNumber}/{id}/{wfid}/input", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String postReq(@RequestBody String receivedChromosomesList,
                          @PathVariable String id,
                          @PathVariable String wfid,
                          @PathVariable int islandNumber) throws InterruptedException, IOException, JSONException {
        synchronized (this) {
            logger.info("received the population for task "+ id);
            finalResultCol.put(id, ".");
            finalplan = "";
            numberOfGeneration++;
            String island = String.valueOf(islandNumber);
            receivedChromosomesList = receivedChromosomesList.concat("#" + island);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(receivedChromosomesList, headers);
            String status = "1"; // Success

            String ChromosomesListForNumberOfChr = receivedChromosomesList.trim();
            String head = ChromosomesListForNumberOfChr.substring(0, ChromosomesListForNumberOfChr.indexOf("\n")).trim();
            numberOfChromosomes = Integer.parseInt(ChromosomesListForNumberOfChr.substring(0, head.indexOf(" ")));
            ResponseEntity<String> answer1 = restTemplate.postForEntity("http://" + splittingJoining + "/sjs/population/" + islandNumber + "/slaves", entity, String.class);
            if (numberOfChromosomes > 1)
                logger.info("received the " + numberOfGeneration + " generation with " + numberOfChromosomes + " chromosomes");
            else {
                logger.info("#####");
                logger.info("received the best chromosome from EA");
            }
            //System.out.println("Complete Sending to splitting and Joining Service");

            return status;

        /*WFID =1 ;
        taskID =1;
        finalResultCol = ".";
        finalplan = "";
        numberOfGeneration++;
        //System.out.println("Recieved a Chromosme list \n" + receivedChromosomesList);
          *//*System.out.println(id);
        System.out.println(wfid);
        System.out.println(taskID);
        System.out.println(WFID);*//*
        String status = "";
        if (Integer.parseInt(id) != taskID && Integer.parseInt(wfid) != WFID) {

            status = "-3";// id and wfid are right
        } else if (Integer.parseInt(id) != taskID && Integer.parseInt(wfid) == WFID)

        {

            status = "-1"; // id is wrong
        } else if (Integer.parseInt(id) == taskID && Integer.parseInt(wfid) != WFID)

        {

            status = "-2";// wfid is wrong
        }

            else {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(receivedChromosomesList, headers);
            status = "1"; // Success

            String ChromosomesListForNumberOfChr = receivedChromosomesList.trim();
            String head = ChromosomesListForNumberOfChr.substring(0, ChromosomesListForNumberOfChr.indexOf("\n")).trim();
            numberOfChromosomes = Integer.parseInt(ChromosomesListForNumberOfChr.substring(0, head.indexOf(" ")));
            ResponseEntity<String> answer1 = restTemplate.postForEntity("http://" + splittingJoining + "/sjs/population/slaves", entity,String.class);
            if (numberOfChromosomes> 1)
                logger.info("received the "+numberOfGeneration+ " generation with "+ numberOfChromosomes + " chromosomes");
            else {
                logger.info("#####");
                logger.info("received the best chromosome from EA");
            }
            //System.out.println("Complete Sending to splitting and Joining Service");
        }
            return status;*/
        }
    }
/** End of the send of the chromosome list related to the task function**/
/*******************************************************************************************/

@RequestMapping(value = "/opt/{islandNumber}/result", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
public void receiveresult(@RequestBody String receivedResult, @PathVariable int islandNumber) throws IOException {
    synchronized (this) {
        String island = receivedResult.substring(receivedResult.indexOf("#") + 1);
        if (numberOfChromosomes > 1) {

        /*File file = new File("../../../store/result_"+JobID+".txt");
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(receivedResult);
        writer.close();*/
            logger.info("received the results of the " + numberOfGeneration + " generation of the island " + island);
        } else {
            String resultsOfBestSChedulingPlan = receivedResult.substring(receivedResult.indexOf("\n"), receivedResult.length());
            resultsOfBestSChedulingPlan = resultsOfBestSChedulingPlan.replace("\n", "").replace("\r", "");
            logger.info("received the results of the optimal scheduling plan #" + resultsOfBestSChedulingPlan + "#");
            logger.info("#####");
        }
        receivedResult = receivedResult.substring(0, receivedResult.indexOf("#"));
        finalResultCol.put(island, receivedResult);
    }
}
/*********************************************************************************/
@RequestMapping(value = "/opt/{islandNumber}/finalplan", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
public void finalplan(@RequestBody String receivedFinalPlan, @PathVariable int islandNumber) {
    synchronized (this) {
        if (numberOfChromosomes == 1) {
            logger.info("received the optimal scheduling plan with real values");
            saveFinalPlan(receivedFinalPlan);
        }
    }
}
public synchronized void saveFinalPlan (String saveFinalPlane)
{
    finalplan = saveFinalPlane;
}
/*********************************************************************************/
    @RequestMapping(value = "/opt/{islandNumber}/{id}/{wfid}/result", method = RequestMethod.GET)
    public String getReq(
            @PathVariable String id,
            @PathVariable String wfid,
            @PathVariable int islandNumber
    ) throws InterruptedException {
        synchronized (this) {
            if (finalResultCol.get(String.valueOf(islandNumber)).equals("."))
                return ".";
            else
                return finalResultCol.get(String.valueOf(islandNumber));

        }
    }

}
