package edu.kit.iai.gleam.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.kit.iai.gleam.*;
import edu.kit.iai.gleam.config.ConstantStrings;
import edu.kit.iai.gleam.producer.ConfigurationAvailablePublisher;
import edu.kit.iai.gleam.producer.InitialPopulationPublisher;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Contains all MasterSlave related REST controller parts
 */
@CrossOrigin(origins = "*")
@RestController
public class MasterSlave {
    @Autowired
    private Gson gson;
    private String name;


    private static List<ChromosomeList> HistoricalChromosomeList = new ArrayList<ChromosomeList>();
    private static ChromosomeList chromosomeList = new ChromosomeList();
    private static List<Chromosome> listOfChromosomes = new ArrayList<Chromosome>();
    private static int number_of_chromosomes = 0;
    private static int resVAlNumber_err = 0;
    private static Result res = new Result();
    private static String data[];
    private static RestTemplate restTemplate = new RestTemplate();
    private static int l = 0;
    private static List<String> runningContainer = new ArrayList<String>();
    private static String receivedChromosomesListBackup = "";
    private static boolean stateOfSentData = true;
    private static int taskID = 0;
    private static int WFID = 0;
    private static boolean isOnline = true; // status of Jianlei Server
    private static String containerIp = "127.0.0.1:8073"; // Ip of container mangment service


    @Value("${containers}")
    private int number_of_dockers;

    public void sendingDataController(DistibutedList inputList) throws JSONException, IOException, InterruptedException {
        System.out.println("Sending data with length " + inputList.getDistibutedList().size());
        String json = this.gson.toJson(inputList.getDistibutedList());
        String url = "http://" + containerIp + "/opal-simu/tasks/input";

        sendHttpRequest(json, url);
    }


    public DistibutedList distibuteData(String receivedChromosomesList) throws JSONException, InterruptedException {

        DistibutedList returnedResult = new DistibutedList(); //  contains DistibutedChromosomeList DCL
        returnedResult.DistibutedList = buildDistribution(receivedChromosomesList, number_of_dockers);
        System.out.println("Created Distributed List");

        return returnedResult;
    }

    private List<String> buildDistribution(String input, int containers) {
        input = input.trim();
        input += "\n";
        String head = input.substring(0, input.indexOf("\n")).trim();
        int firstNumberOfHead = Integer.parseInt(input.substring(0, head.indexOf(" ")));
        System.out.print("I have recieved a new Chromosomes list with " + firstNumberOfHead + " chromosomes\n");
        String rest = input.substring(input.indexOf("\n") + 1, input.length());
        int thirdNumberOfSecondline = Integer.parseInt(rest.substring(rest.indexOf(" ", rest.indexOf(" ") + 1) + 1, rest.indexOf("\n")));
        return build(rest, firstNumberOfHead, containers, head, thirdNumberOfSecondline);
    }

    private List<String> build(String rest, int firstNumberOfHead, int containers, String head, int thirdNumberOfSecondline) {
        List<String> list = new ArrayList<String>();
        String returnValue = "";

        String[] headArray = head.split(" ");
        String restHead = " " + headArray[1] + " " + headArray[2];

        for (int portion = 1; portion <= containers; portion++) {
            int numberOfEachPortion = (thirdNumberOfSecondline + 1) * (firstNumberOfHead / containers);
            head = Integer.toString(firstNumberOfHead / containers) + restHead;
            if (portion <= firstNumberOfHead % containers) {
                numberOfEachPortion += (thirdNumberOfSecondline + 1);
                head = Integer.toString(firstNumberOfHead / containers + 1) + restHead;
            }
            if (numberOfEachPortion == 0) {
                continue;
            }
            int position = StringUtils.ordinalIndexOf(rest, "\n", numberOfEachPortion);
            list.add(head + "\n" + rest.substring(0, position + 1));
            rest = rest.substring(position + 1, rest.length());
        }
        return list;
    }

/**
 * @throws InterruptedException
 * @throws IOException
 * @throws  *****************************************************************************************/
    /**
     * @throws JSONException
     *****************************************************************************************/
    public DistibutedList mainFunction(String receivedChromosomesList) throws InterruptedException, IOException, JSONException {
        DistibutedList returnedResult = new DistibutedList();
        //SetWatcherOnChild();
//	deleteZookeeperChildren (); // delete existing children nodes under PROOF node in ZooKeeper
        returnedResult = distibuteData(receivedChromosomesList);
        //createContainer(returnedResult.getNrOfContainer());
        //findRunningContainer();
        return returnedResult;
    }
/*******************************************************************************************/
    /**
     * Get an ID for the task
     *
     * @throws IOException
     **/
    @RequestMapping(value = "/opt/taskID", method = RequestMethod.GET)
    public String getIDforTask() throws IOException {

        @SuppressWarnings("resource")
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
            sb.append(taskID);
            String strI = sb.toString();
            return strI;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            String strI = sb.toString();
            return strI;

        }
    }

/** End of the get an ID for the task function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Reset the ID of the task
     *
     * @throws IOException
     **/


    @RequestMapping(value = "/opt/resetTaskID", method = RequestMethod.GET)
    public int resetTaskID() throws IOException {


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
    @RequestMapping(value = "/opt/{id}/{nameOfModel}/active", method = RequestMethod.GET)
    public String checkModel(@PathVariable String id,
                             @PathVariable String nameOfModel) {
        if (!id.equals("0") && Integer.parseInt(id) == taskID)

            return "yes";

        else
            return "no";
    }

/** End of the check if a specific model exists or not  function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Get the available models
     **/
    @RequestMapping(value = "/opt/{id}/models", method = RequestMethod.GET)
    public String getModels(@PathVariable String id) {
        if (!id.equals("0") && Integer.parseInt(id) == taskID && isOnline1()) {
            String models = "OpalDemo\n" + "OpalDemo1\n" + "OpalDemo2\n";
            return models;
        } else
            return "";
    }

/** End of the check if a specific model exists or not  function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Send the Configuration related to the task
     **/
    @RequestMapping(value = "/opt/{id}/start", method = RequestMethod.POST)
    public String startTask(@PathVariable String id, @RequestBody String configuration) {
        WFID = 1;
        if (!id.equals("0") && Integer.parseInt(id) == taskID) {
//            System.out.println("Configurations are: \n" + configuration);
            /** TODO processing the Configuration**/
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(WFID);
            String strI = sb.toString();
            return strI;
        } else
            return "-1";
    }

/** End of the check if a specific model exists or not  function**/
/*******************************************************************************************/
    /**
     * @throws IOException
     * @throws UnknownHostException
     *****************************************************************************************/
    public static boolean isOnline1() {
        String[] bla = containerIp.split(":");
        try (Socket s = new Socket(bla[0], Integer.parseInt(bla[1]))) {
            return isOnline;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;

    }


/*******************************************************************************************/
    /**
     * Send the chromosome list related to the task
     **/

    @RequestMapping(value = "/opt/{id}/{wfid}/input", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String postReq(@RequestBody String receivedChromosomesList,
                          @PathVariable String id,
                          @PathVariable String wfid) throws InterruptedException, IOException, JSONException {
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
	/*else if (!isOnline1())
	{
		status = "-4"; // no connection to PROOF
	}*/
        else {

            status = "1"; // Success
//            System.out.println(isOnline1());
            if (isOnline1()) {
                new Thread(() -> {
                    receivedChromosomesListBackup = receivedChromosomesList;
                    try {
                        sendingDataController(mainFunction(receivedChromosomesList));
                    } catch (JSONException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                String lines[] = receivedChromosomesList.split("\n"); // Put the received chromosomes list in an array where each chromosome is an array element
                lines[0] = lines[0].replaceFirst("^\\s*", "");
                String chromosomeListHead[] = lines[0].split("\\s+"); // split the head of chromosomes list in an array
                number_of_chromosomes = Integer.parseInt(chromosomeListHead[0]);
                System.out.print("I have recieved a new Chromosomes list with " + number_of_chromosomes + " chromosomes \n");
//                System.out.println("---------------------------- Results ... \n");
            }
        }
        return status;
    }

/** End of the send of the chromosome list related to the task function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Stop signal to end the task
     **/
    @RequestMapping(value = "/opt/{id}/stop", method = RequestMethod.GET)
    public String stopTask(@PathVariable String id) {
        if (isOnline1()) {
            System.out.println("here is Stop API");
            if (!id.equals("0") && Integer.parseInt(id) == taskID)

                return "1";

            else
                return "-1";
        } else {
            return "-2";
        }
    }

/** End of the stop signal to end the task function**/
/*******************************************************************************************/

/*******************************************************************************************/
    /**
     * Abort signal to end the task in a forced way
     **/
    @RequestMapping(value = "/opt/{id}/{wfid}/abort", method = RequestMethod.GET)
    public String abortTask(@PathVariable String id, @PathVariable String wfid) {
        String status = "";
        if (isOnline1()) {
            System.out.println("here is Abort API");
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
        }
    }

/** End of Abort signal to end the task in a forced way **/
    /*******************************************************************************************/
    @RequestMapping(value = "/opt/receiveresult", method = RequestMethod.POST)
    public void receiveresult(@RequestBody String receivedResult) {
        System.out.print("receive result: ");
        data = receivedResult.split(",");
        System.out.println(data.length);
//        for (int o = 0; o < data.length; o++)
//            System.out.println("save result..." + data[o]);

    }

/******************************************************************************************/
    /**
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     *****************************************************************************************/
    @RequestMapping(value = "/opt/{id}/{wfid}/result", method = RequestMethod.GET)
    public String getReq(
            @PathVariable String id,
            @PathVariable String wfid
    ) throws InterruptedException, UnsupportedEncodingException {

        if (isOnline1()) {

            String reData = "";
            String reDataNeu = "";
            ArrayList<String> ZeroStatusOfHead = new ArrayList<String>();
            ArrayList<String> PositiveStatusOfHead = new ArrayList<String>();
            ArrayList<String> NegativeStatusOfHead = new ArrayList<String>();
            int lengthOfData = 0;
            int numOfChromosoms = 0;
            int resValNumber = 0;

            //ChildzondeListGet = zk.getChildren("/"+rootName, false);
            if (data != null) {
/*		  System.out.println("I have recieved a GET request to get the result, please wait ");
		  System.out.println("Chrildern number "+ ChildzondeListGet.size());

	   int m = 0;
	   for(String child : ChildzondeListGet)
	   {
		        byte[] bn1 = zk.getData("/"+rootName+"/"+child,
                false, null);
                data[m] = new String(bn1,"UTF-8");
                zkc.getConnectedSignal().countDown();
                zk.delete("/"+rootName+"/"+child, -1);
                m++;
	   }*/

                lengthOfData = data.length;
                for (int u = 0; u < data.length; u++) {
//                    System.out.println(data[u]);

                    String[] result = data[u].split("\\n");
                    numOfChromosoms = numOfChromosoms + result.length - 1;
                    String[] headTempResult = result[0].split("\\s+");

                    resValNumber = Integer.parseInt(headTempResult[2]);
                    if (Integer.parseInt(headTempResult[0]) == 0)
                        ZeroStatusOfHead.add(headTempResult[0]);
                    else if (Integer.parseInt(headTempResult[0]) < 0)
                        PositiveStatusOfHead.add(headTempResult[0]);
                    else
                        NegativeStatusOfHead.add(headTempResult[0]);
                    for (int h = 1; h < result.length; h++) {
                        reData = reData + result[h] + "\n";
                    }
                }
                if (ZeroStatusOfHead.size() == lengthOfData && Integer.parseInt(id) == taskID && Integer.parseInt(wfid) == WFID)
                    reDataNeu = "0  " + numOfChromosoms + " " + resValNumber + "\n" + reData;
                else if (PositiveStatusOfHead.size() != 0 && NegativeStatusOfHead.size() == 0 && Integer.parseInt(id) == taskID && Integer.parseInt(wfid) == WFID)
                    reDataNeu = "1  " + numOfChromosoms + " " + resValNumber + "\n" + reData;
                else if (PositiveStatusOfHead.size() == 0 && NegativeStatusOfHead.size() != 0 || (Integer.parseInt(id) != taskID && Integer.parseInt(wfid) != WFID))
                    reDataNeu = "-1  " + numOfChromosoms + " " + resValNumber + "\n" + reData;
                else if (PositiveStatusOfHead.size() != 0 && NegativeStatusOfHead.size() != 0 || (Integer.parseInt(id) == taskID && Integer.parseInt(wfid) != WFID))
                    reDataNeu = "-2  " + numOfChromosoms + " " + resValNumber + "\n" + reData;
                else
                    reDataNeu = "-3  " + numOfChromosoms + " " + resValNumber + "\n" + reData;
                //System.out.println(reDataNeu);
                data = null;
                return reDataNeu;
            }
            return "."; // no results
        } else
            return "-4  " + number_of_chromosomes + " " + resVAlNumber_err + "\n";
//	return "-3"; // id or wfid is wrong

    }
/*******************************************************************************************/

    /*******************************************************************************************/
    @RequestMapping(value = "/getresult", method = RequestMethod.GET)
    public void getReqRes() {
	/*String hh = "    50 2 0";
	System.out.println(hh);
	hh = hh.replaceFirst("^\\s*","");
	System.out.println(hh);
	try {
		zk.create("/_locknode_",null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

		zk.create("/_locknode_/queue-",null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
	} catch (KeeperException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			//CreateMode=EPHEMERAL_SEQUENTIAL);
	
	*/

    }

    /**
     * @throws IOException
     * @throws UnknownHostException
     *****************************************************************************************/
    @RequestMapping(value = "/gethistory", method = RequestMethod.GET)
    public void getHistoryReq() throws UnknownHostException, IOException {
//System.out.print(HistoricalChromosomeList.size());


    }

    /**
     * @throws JSONException
     *****************************************************************************************/
    @RequestMapping(value = "/removecontainer", method = RequestMethod.GET)
    public void getresult() throws JSONException {


        String json_container = restTemplate.getForObject("http://" + containerIp + "/opal-simu/containers/status", String.class);
        JSONObject json_docker = new JSONObject(json_container);
        Iterator keysinJSON = json_docker.keys();
        List<String> keysList = new ArrayList<String>();
        while (keysinJSON.hasNext()) {
            String key = (String) keysinJSON.next();
            keysList.add(key);
        }
        for (String key : keysList) {
            String id = key.substring(3, key.length());
            restTemplate.delete("http://" + containerIp + "/opal-simu/" + id + "/tasks/close");
        }

    }

    /*******************************************************************************************/
    @RequestMapping(value = "/opt/{ip}/setContainerServiceIP", method = RequestMethod.POST)
    public void setContainerServiceIP(@PathVariable String ip) {
        System.out.println("before:" + containerIp);
        containerIp = ip;
        System.out.print(containerIp);
    }

    /*******************************************************************************************/
    @RequestMapping(value = "/buildresult", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public Result buildResult(@RequestBody String str) {
        String lines[] = str.split("\n");
        ResultList resList = new ResultList();
        List<ResultList> tempResList = new ArrayList<>();

        String resultBody[] = lines[1].split("\\s+");
        resList.parentIndex = Integer.parseInt(resultBody[0]);
        resList.childIndex = Integer.parseInt(resultBody[1]);
        List<Double> Dlist = new ArrayList<>();

        for (int k = 2; k < resultBody.length; k++) {
            Dlist.add(Double.parseDouble(resultBody[k]));

        }
        resList.Dlist = Dlist;
        tempResList.add(resList);

        String resultListHead[] = lines[0].split("\\s+");
        res.retStatus = Integer.parseInt(resultListHead[0]);
        res.chrNumber = Integer.parseInt(resultListHead[1]);
        res.resValNumber = Integer.parseInt(resultListHead[2]);
        res.Reslist = tempResList;

        return res;

    }


    private void sendHttpRequest(String json, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        System.out.println("POST request before");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(url, entity, String.class);
        System.out.println("POST request after");
    }
    /*******************************************************************************************/

}
