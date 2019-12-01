package iai.kit.edu.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;



@CrossOrigin(origins = "*")
@RestController
public class TaskController {
	
	private Gson gson = new Gson();
	private static RestTemplate restTemplate = new RestTemplate();
	private static String containerIp = "localhost:8081";
	private static String data[];
	private static String receivedChromosomesListBackup = "";
	private int containers = 1;

    /**
     * Get an ID for the task
     *
     * @throws IOException
     **/
    @RequestMapping(value = "/opt/taskID", method = RequestMethod.GET)
    public String getIDforTask() throws IOException {

       return "1";
    }
    
    @RequestMapping(value = "/opt/{id}/start", method = RequestMethod.POST)
    public String startTask(@PathVariable String id, @RequestBody String configuration) {
        if (!id.equals("0") && Integer.parseInt(id) == 1) {
            return "1";
        } else
            return "-1";
    }
    
    @RequestMapping(value = "/opt/{id}/{nameOfModel}/active", method = RequestMethod.GET)
    public String checkModel(@PathVariable String id,
                             @PathVariable String nameOfModel) {
        if (!id.equals("0") && Integer.parseInt(id) == 1)
            return "yes";
        else
            return "no";
    }
    @RequestMapping(value = "/opt/{id}/models", method = RequestMethod.GET)
    public String getModels(@PathVariable String id) {
        if (!id.equals("0") && Integer.parseInt(id) == 1) {
            String models = "OpalDemo\n" + "OpalDemo1\n" + "OpalDemo2\n";
            return models;
        } else
            return "";
    }
    
    @RequestMapping(value = "/opt/{id}/{wfid}/input", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String recieveInitialChromosomeList(@RequestBody String receivedChromosomesList,
                          @PathVariable String id,
                          @PathVariable String wfid) throws InterruptedException, IOException, JSONException{
    	
    	if(!id.equals("1") && !wfid.equals("1")) {
    		return "-3"; //both id and wfid are wrong
    	}
    	if(!id.equals("1") && wfid.equals("1")) {
    		return "-1"; //id is wrong
    	}
    	if(id.equals("1") && !wfid.equals("1")) {
    		return "-2"; //wfid is wrong
    	}
    	new Thread(() -> {
            receivedChromosomesListBackup = receivedChromosomesList;
            try {
                sendingDataController(convertToDistributedList(receivedChromosomesList));
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    	
    	return "1";
    }
    
    @RequestMapping(value = "/opt/receiveresult", method = RequestMethod.POST)
    public void receiveresult(@RequestBody String receivedResult) {
        System.out.print("receive result: ");
        data = receivedResult.split(",");
        System.out.println(data.length);
    }
    
    @RequestMapping(value = "/opt/{id}/stop", method = RequestMethod.GET)
    public String stopTask(@PathVariable String id) {
            System.out.println("here is Stop API");
            if (id.equals("1"))
                return "1";
            else
                return "-1";
    }
    
    @RequestMapping(value = "/opt/{id}/{wfid}/result", method = RequestMethod.GET)
    public String sendResultToGleam(
            @PathVariable String id,
            @PathVariable String wfid
    ) {
    	System.out.println("I have recieved a GET request to take the result");
    	return "0 5 1 \n 1 0 2.22 \n 2 0 7.15 \n 3 0 24.14 \n 4 0 15.4 \n 5 0 12.4";
    	
    }
    
    public DistibutedList convertToDistributedList(String receivedChromosomesList) throws InterruptedException, IOException, JSONException {
        DistibutedList returnedResult = new DistibutedList();
        receivedChromosomesList = receivedChromosomesList.trim();
        receivedChromosomesList += "\n";
        String head = receivedChromosomesList.substring(0, receivedChromosomesList.indexOf("\n")).trim();
        int firstNumberOfHead = Integer.parseInt(receivedChromosomesList.substring(0, head.indexOf(" ")));
        System.out.print("I have recieved a new Chromosomes list with " + firstNumberOfHead + " chromosomes\n");
        String rest = receivedChromosomesList.substring(receivedChromosomesList.indexOf("\n") + 1, receivedChromosomesList.length());
        int thirdNumberOfSecondline = Integer.parseInt(rest.substring(rest.indexOf(" ", rest.indexOf(" ") + 1) + 1, rest.indexOf("\n")));
        List<String> list = new ArrayList<String>();
        String returnValue = "";

        String[] headArray = head.split(" ");
        String restHead = " " + headArray[1] + " " +headArray[2];

        for (int portion = 1; portion <= containers; portion++) {
            int numberOfEachPortion = (thirdNumberOfSecondline + 1) * (firstNumberOfHead / containers);
            head = Integer.toString(firstNumberOfHead / containers) + restHead;
            if(portion <= firstNumberOfHead % containers) {
                numberOfEachPortion += (thirdNumberOfSecondline + 1);
                head = Integer.toString(firstNumberOfHead / containers + 1) + restHead;
            }
            if(numberOfEachPortion == 0) {
                continue;
            }
            int position = StringUtils.ordinalIndexOf(rest, "\n", numberOfEachPortion);
            list.add(head + "\n" + rest.substring(0, position + 1));
            rest = rest.substring(position + 1, rest.length());
        }
        returnedResult.DistibutedList = list;
        System.out.println("Created Distributed List");

        return returnedResult;
    }
    
    public void sendingDataController(DistibutedList inputList) throws JSONException, IOException, InterruptedException {
        System.out.println("Sending data with length " + inputList.getDistibutedList().size());
       
        String json = this.gson.toJson(inputList.getDistibutedList());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        System.out.println("POST request before");
        ResponseEntity<String> answer1 = restTemplate.postForEntity("http://" + containerIp + "/opal-simu/tasks/input", entity, String.class);
        System.out.println("POST request after");
    }
    
}
