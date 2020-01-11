package iai.kit.edu.chromosomeinterpreter.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import iai.kit.edu.chromosomeinterpreter.consumer.DateSubscriber;
import iai.kit.edu.chromosomeinterpreter.core.Chromosomeinterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@CrossOrigin(origins = "*")
@RestController

public class AnalysisGenCon {
    @Autowired
    static Chromosomeinterpreter dataGen;
    private static String date = "2013-01-01";
    HashMap<String, Float> listGen = new HashMap<String, Float>();
    String composedDate = "";
    String [] hours = new String []{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

    @RequestMapping(value = "/chr/genanalyse/{Date}", method = RequestMethod.GET)
    public String receiveresult1(@PathVariable String Date) throws IOException {
        String gen = dataGen.getGeneration;
        JsonArray jsonArrayConsumption = new Gson().fromJson(gen, JsonArray.class);
        System.out.println("jsonArrayConsumption is " + jsonArrayConsumption);
        System.out.println("Date is " + Date);

        for (int k = 0; k < hours.length; k++){
            composedDate = Date + " " + hours[k];
            float gesamteGen = 0;
            for (int i = 0; i < jsonArrayConsumption.size(); i++) {
                JsonObject generationsonObjects = jsonArrayConsumption.get(i).getAsJsonObject();
                String dateInJsonObject = generationsonObjects.get("localhour").getAsString();
                if (dateInJsonObject.contains(composedDate)) {
                    gesamteGen += generationsonObjects.get("gen").getAsFloat();
                }
            }
            listGen.put(composedDate,gesamteGen);
        }
        String json = new Gson().toJson(listGen);
        return json;
    }

    @RequestMapping(value = "/chr/genanalyse", method = RequestMethod.GET)
    public String receiveresult() throws IOException {
        String  Date = dataGen.getDate();
        String gen = dataGen.getGeneration;
        JsonArray jsonArrayConsumption = new Gson().fromJson(gen, JsonArray.class);

        for (int k = 0; k < hours.length; k++){

            composedDate = Date + " " + hours[k];
            float gesamteGen = 0;
            for (int i = 0; i < jsonArrayConsumption.size(); i++) {
                JsonObject generationsonObjects = jsonArrayConsumption.get(i).getAsJsonObject();
                String dateInJsonObject = generationsonObjects.get("localhour").getAsString();
                if (dateInJsonObject.contains(composedDate)) {
                    gesamteGen += generationsonObjects.get("gen").getAsFloat();
                }
            }
            listGen.put(composedDate,gesamteGen);
        }
        String json = new Gson().toJson(listGen);
        return json;    }
}
