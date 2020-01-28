package edu.kit.iai.calculation.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.kit.iai.calculation.config.ConstantStrings;
import edu.kit.iai.calculation.producer.IntermediatePopulationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


import static java.lang.Math.abs;

public class Calculation {
    private int planIntID;
    private int childIntID;
    private JsonArray resourceIntPlan;
    private int resourceIntID;
    private JsonArray resourceIntPlanPart;
    private float [] costResourceTempo;
    private  Gson gson;
    private float planCost;
    private float resourceCost;
    private JsonObject price;
    private JsonObject consumption;
    private static String date = "2013-01-01";
    private float houseConsumption;
    private float sumOfRequestedPowerinEachPlan;
    private RestTemplate restTemplate = new RestTemplate();
    /*public static ResponseEntity<String> getPrice;
    public static ResponseEntity<String> getConsumption;*/
    public static String getPrice;
    public static String getConsumption;

    @Autowired
    IntermediatePopulationPublisher intermediatePopulationPublisher;
    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> intTemplate;

    private long startTime;
    private long endTime;
    private long jobDuration;
    private long generationduration;

    private RedisAtomicInteger amountOfGeneration;
    private int numberOfGenerationOfOneJob;
    private int actualNumberOfGenerationOfOneJob = 0;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    public void calculationPrice  (JsonArray jsonArray, String idNumber) throws FileNotFoundException, UnknownHostException {
        startTime = System.currentTimeMillis();

        logger.info("the date to be scheduled is "+ date);
        houseConsumption = calculationSumConsumption(date);
        //  System.out.println("The ssssdddd JSON  file as chromosome list with relative values" + partOfPopulation + "\n");
        //  String path = "/home/ws/na8117/Schreibtisch/Intermed_SchedulingOnline/calculation/Price.json";

        gson = new Gson();
        //  price = gson.fromJson(new FileReader(path), JsonObject.class);
        // System.out.println("The hAASD ssssssssssss");

        StringBuilder finalResult = new StringBuilder("");
         // System.out.println("The JSON  file as chromosome list with relative values" + partOfPopulation);
          //  JsonArray jsonArray = gson.fromJson(partOfPopulation, JsonArray.class);
        // System.out.println("The jsonArray size " + jsonArray.size());

        for (int i = 0; i < jsonArray.size(); i++)
        { // for each plan in Json
            planCost = 0;
            sumOfRequestedPowerinEachPlan = 0;

            float [] sumOfRequestedPowrinEachHour = new float[24];
            for(int ii=0;ii<sumOfRequestedPowrinEachHour.length;ii++)
                sumOfRequestedPowrinEachHour[ii] = 0;

            JsonObject plan = jsonArray.get(i).getAsJsonObject();
            planIntID = plan.get("planID").getAsInt();
            childIntID = plan.get("childID").getAsInt();
            resourceIntPlan = gson.fromJson(plan.get("resourcePlan").getAsJsonArray(),JsonArray.class); //size is equal to the number of resources
            for (int j = 0; j< resourceIntPlan.size();j++) // for each resource in this plan
            {
                resourceCost = 0;
                float [] costResourceTempo = new float[24];
                JsonObject ResPlan = resourceIntPlan.get(j).getAsJsonObject();
                resourceIntID = ResPlan.get("resourceID").getAsInt();
                resourceIntPlanPart = ResPlan.get("powerGeneration").getAsJsonArray();
                for (int k = 0 ; k<resourceIntPlanPart.size();k++)
                {

                    String composedDate;
                    if(k < 10){
                        composedDate = String.format("%s %d%d", date,0,k);
                    }
                    else
                        composedDate = String.format("%s %d", date,k);

                     //System.out.println("composedDate to send th forecasting "+ composedDate);

                    costResourceTempo[k]  = calculationPlancost(resourceIntPlanPart.get(k).getAsFloat(), composedDate, resourceIntID);
                    sumOfRequestedPowrinEachHour[k] += resourceIntPlanPart.get(k).getAsFloat();
                    //System.out.println("The sum Of Requested Power in Each Plan is "+resourceIntPlanPart.get(k).getAsFloat());
                    sumOfRequestedPowerinEachPlan += resourceIntPlanPart.get(k).getAsFloat();
                }
                for (int l = 0; l < costResourceTempo.length;l++)
                {
                    resourceCost+=costResourceTempo[l];
                }
                planCost+=resourceCost; // the cost of one plan

            } // end of all resouces in this plan
            //form the reslut for this plan in for of planID childID res1 res2 res3 .....
            float numberOfDifferenceEachhour = calculationDifferenceEachhour(sumOfRequestedPowrinEachHour, date);
            finalResult.append(planIntID);
            finalResult.append(" "+childIntID);
            finalResult.append(" " +planCost);
            finalResult.append(" "+abs(houseConsumption-sumOfRequestedPowerinEachPlan));
            finalResult.append(" "+ numberOfDifferenceEachhour+"\n");

           // System.out.println("The cost of this plan is "+planCost);
          //   System.out.println("**********************************************************");

            //finalResult.append(" "+(Math.random() * (1200 - 0) + 100)+"\n");

        }
       //System.out.println("Here is inside the function (first)"+finalResult);
        endTime = System.currentTimeMillis();
        generationduration = TimeUnit.MILLISECONDS.toMinutes(endTime - startTime);
        caculateDuration(generationduration);
        finalResult.append("#" + idNumber);
        intermediatePopulationPublisher.publishIntermediatePopulation(finalResult);


    }


    public  float fetchDataMarket (String date1, int resourceID) throws UnknownHostException {

        /*Document docs = collectionMarket.find(and(regex("localhour" , date), eq("dataid", resourceID))).first();
        String jsonMarket =docs.toJson();
        price = new Gson().fromJson(jsonMarket, JsonObject.class);
        float priceInt = price.get("preis").getAsFloat();*/

        //JsonArray jsonArrayPeice = new Gson().fromJson(getPrice.getBody(), JsonArray.class);
        JsonArray jsonArrayPeice = new Gson().fromJson(getPrice, JsonArray.class);// if you use API use the above line
        float priceInt1 = 0;
        for (int i = 0; i < jsonArrayPeice.size(); i++) {
            JsonObject priceJsonObjects = jsonArrayPeice.get(i).getAsJsonObject();
            String dateInJsonObject = priceJsonObjects.get("localhour").getAsString();
            int resourceIDInJsonObject =  priceJsonObjects.get("dataid").getAsInt();
            if (dateInJsonObject.contains(date1) && resourceIDInJsonObject == resourceID)
            {
                priceInt1 = priceJsonObjects.get("preis").getAsFloat();
                break;
            }
        }

         //System.out.println ("The Price for the date "+ date + " and the resource  "+resourceID + " is "+ priceInt1);
         // System.out.println("the Doc price for this hour and this resource is :"+ docs.toJson());
         //  System.out.println("the price for this hour and this resource is :"+ priceInt);
        return priceInt1;
    }

    private float calculationPlancost (float absoulteValue, String composedDate, int resourceID) throws FileNotFoundException, UnknownHostException {

       // System.out.println("the absoulte value for this hour and this resource is  :"+ absoulteValue);
        float result = absoulteValue*fetchDataMarket (composedDate, resourceID);
       // System.out.println("the cost of this hour for this resource is  :"+ result);
        return result;

    }

   /* public  FindIterable<Document>  fetchDataConsumption (String date) throws UnknownHostException {
        databaseConsumption = mongoClient.getDatabase("consumption");
        collectionConsumption = databaseConsumption.getCollection("consumption");
        FindIterable<Document> docsF = collectionConsumption.find(regex("localhour" , date));


        // System.out.println("the Doc price for this hour and this resource is :"+ docs.toJson());
        //  System.out.println("the price for this hour and this resource is :"+ priceInt);
        return docsF;
    }*/

    private float calculationSumConsumption (String date1) throws FileNotFoundException, UnknownHostException {
        float result = 0;
        /*FindIterable<Document> docs = fetchDataConsumption(date);
        for (Document doc : docs)
        {
            String jsonForecasting =doc.toJson();
            consumption = new Gson().fromJson(jsonForecasting, JsonObject.class);
            float consumptionInt = consumption.get("use").getAsFloat();
            result +=consumptionInt;
         //   System.out.println("The consumption for this day is  " + result);

        }*/

        //JsonArray jsonArrayConsumption = new Gson().fromJson(getConsumption.getBody(), JsonArray.class);
        JsonArray jsonArrayConsumption = new Gson().fromJson(getConsumption, JsonArray.class); // if you use API use the above line
        float consumptionInt = 0;
        for (int i = 0; i < jsonArrayConsumption.size(); i++) {
            JsonObject consumptionJsonObjects = jsonArrayConsumption.get(i).getAsJsonObject();
            String dateInJsonObject = consumptionJsonObjects.get("localhour").getAsString();
            if (dateInJsonObject.contains(date1))
            {
                consumptionInt = consumptionJsonObjects.get("use").getAsFloat();
                result +=consumptionInt;

            }
        }

        //System.out.println("The consumption for this day is Final " + (result));
        return (result);
    }

    private float calculationDifferenceEachhour (float [] sumOfRequestedPowrinEachHour, String date1) throws FileNotFoundException, UnknownHostException {

       /* FindIterable<Document> docs = fetchDataConsumption(date);
        int index = 0;
        for (Document doc : docs)
        {
            String jsonForecasting =doc.toJson();
            consumption = new Gson().fromJson(jsonForecasting, JsonObject.class);
            float consumptionInt = consumption.get("use").getAsFloat();
            if (consumptionInt > sumOfRequestedPowrinEachHour[index])
            {
                numberOfHourWhereUseMoreThanRequestedPower++;

            }
            index++;

        }*/
        float consumptionInt = 0;
        float numberOfHourWhereUseMoreThanRequestedPower = 0;
        int index = 0;
        //JsonArray jsonArrayConsumption = new Gson().fromJson(getConsumption.getBody(), JsonArray.class);
        JsonArray jsonArrayConsumption = new Gson().fromJson(getConsumption, JsonArray.class);// if you use API use the above line
        for (int i = 0; i < jsonArrayConsumption.size(); i++) {
            JsonObject consumptionJsonObjects = jsonArrayConsumption.get(i).getAsJsonObject();
            String dateInJsonObject = consumptionJsonObjects.get("localhour").getAsString();
            if (dateInJsonObject.contains(date1))
            {
                consumptionInt = consumptionJsonObjects.get("use").getAsFloat();
                if (consumptionInt > sumOfRequestedPowrinEachHour[index])
                {
                    numberOfHourWhereUseMoreThanRequestedPower++;

                }
                index++;

            }
        }

        //System.out.println("The number of Of Hour Where Use More Than Requested Power " + numberOfHourWhereUseMoreThanRequestedPower);
        return (numberOfHourWhereUseMoreThanRequestedPower);


    }

    public void caculateDuration(long durationPar)
    {
        actualNumberOfGenerationOfOneJob++;
        logger.info("actualNumberOfGenerationOfOneJob is "+ actualNumberOfGenerationOfOneJob);
        jobDuration = jobDuration + durationPar;

        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, intTemplate.getConnectionFactory());
        numberOfGenerationOfOneJob = amountOfGeneration.get();
        logger.info("numberOfGenerationOfOneJob is "+ numberOfGenerationOfOneJob);

        if (actualNumberOfGenerationOfOneJob != numberOfGenerationOfOneJob) {
            logger.info("the time taken to calculate this sub population for one generation is " + durationPar + " Minutes");
        }
        else
        {
            actualNumberOfGenerationOfOneJob = 0;
            logger.info("the time taken to calculate all parts of one job is " + jobDuration + " Minutes");
            jobDuration = 0;
        }
    }


    public  String getDate() {
        return date;
    }

    public void setDate(String date) {
        Calculation.date = date;
    }
}




