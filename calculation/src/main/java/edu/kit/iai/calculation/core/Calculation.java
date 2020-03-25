package edu.kit.iai.calculation.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.kit.iai.calculation.config.ConfigResetter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static java.lang.Math.abs;

public class Calculation {
    private int planIntID;
    private int childIntID;
    private int NrOfGenes;
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
    @Autowired
    ConfigResetter configResetter;

    private long startTime;
    private long endTime;
    private long jobDuration;
    private long generationduration;

    private RedisAtomicInteger amountOfGeneration;
    private int numberOfGenerationOfOneJob;
    private int actualNumberOfGenerationOfOneJob = 0;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    StringBuilder finalResult;
    private  List<JsonObject> JsonObjectPriceList;

    public void calculationPrice  (JsonArray jsonArray, String idNumber)  {
        startTime = System.currentTimeMillis();

        logger.info("the date to be scheduled is "+ date);
        houseConsumption = calculationSumConsumption(date);
        finalResult = new StringBuilder("");
        gson = new Gson();
        List<JsonObject> JsonObjectList = new ArrayList<JsonObject>();

        for (int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject plan = jsonArray.get(i).getAsJsonObject();
            JsonObjectList.add(plan);
        }


        JsonArray jsonArrayPrice = new Gson().fromJson(getPrice, JsonArray.class);// if you use API use the above line
        JsonObjectPriceList = new ArrayList<JsonObject>();
        for (int i = 0; i < jsonArrayPrice.size(); i++)
        {
            JsonObject planJson = jsonArrayPrice.get(i).getAsJsonObject();
            JsonObjectPriceList.add(planJson);
        }

        JsonObjectList.stream().parallel().forEach(
                plan ->  planCalculationParallel(plan)
        );


        // System.out.println("Here is inside the function (first)"+finalResult);
        endTime = System.currentTimeMillis();
        generationduration = TimeUnit.MILLISECONDS.toMinutes(endTime - startTime);
        caculateDuration(generationduration);
        finalResult.append("#" + idNumber);
        intermediatePopulationPublisher.publishIntermediatePopulation(finalResult);
    }


    public void planCalculationParallel(JsonObject plan) {
        synchronized(finalResult){
            planCost = 0;
            sumOfRequestedPowerinEachPlan = 0;

            float [] sumOfRequestedPowrinEachHour = new float[24];
            for(int ii=0;ii<sumOfRequestedPowrinEachHour.length;ii++)
                sumOfRequestedPowrinEachHour[ii] = 0;

            planIntID = plan.get("planID").getAsInt();
            childIntID = plan.get("childID").getAsInt();
            NrOfGenes = plan.get("NrOfGenes").getAsInt();


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
            finalResult.append(" "+ numberOfDifferenceEachhour);
            finalResult.append(" " +NrOfGenes+"\n");

        }
        // System.out.println("The cost of this plan is "+planCost);
        //   System.out.println("**********************************************************");

        //finalResult.append(" "+(Math.random() * (1200 - 0) + 100)+"\n");

    }


    //* public  float fetchDataMarket (String date1, int resourceID)  {

        /*Document docs = collectionMarket.find(and(regex("localhour" , date), eq("dataid", resourceID))).first();
        String jsonMarket =docs.toJson();
        price = new Gson().fromJson(jsonMarket, JsonObject.class);
        float priceInt = price.get("preis").getAsFloat();///////////
        //JsonArray jsonArrayPeice = new Gson().fromJson(getPrice.getBody(), JsonArray.class);
        String wrongValue = "99999999";
        List<Float> priceToReturn = Collections.synchronizedList(new ArrayList<Float>());
        JsonArray jsonArrayPeice = new Gson().fromJson(getPrice, JsonArray.class);// if you use API use the above line
        List<JsonObject> JsonObjectPriceList = new ArrayList<JsonObject>();
        for (int i = 0; i < jsonArrayPeice.size(); i++)
        {
            JsonObject plan = jsonArrayPeice.get(i).getAsJsonObject();
            JsonObjectPriceList.add(plan);
        }
        JsonObjectPriceList.stream().parallel().forEach(
                priceJson -> {
                    String dateInJsonObject = priceJson.get("localhour").getAsString();
                    int resourceIDInJsonObject =  priceJson.get("dataid").getAsInt();
                    if (dateInJsonObject.contains(date1) && resourceIDInJsonObject == resourceID)
                    {
                        float priceInt1 = priceJson.get("preis").getAsFloat();
                        priceToReturn.add(priceInt1);
                    }
                    else
                        priceToReturn.add(Float.parseFloat(wrongValue));
                }
        );
        float retuernedprice = priceToReturn.get(0);
        priceToReturn.remove(0);
        System.out.println("retuernedprice" + retuernedprice);
        return retuernedprice;
    }*/

    private float calculationPlancost (float absoulteValue, String composedDate, int resourceID)  {

        // System.out.println("the absoulte value for this hour and this resource is  :"+ absoulteValue);
        final float [] priceInThishour = {0};

        JsonObjectPriceList.stream().parallel().forEach(
                priceJson -> {
                    String dateInJsonObject = priceJson.get("localhour").getAsString();
                    int resourceIDInJsonObject =  priceJson.get("dataid").getAsInt();
                    if (dateInJsonObject.contains(composedDate) && resourceIDInJsonObject == resourceID)
                    {
                        priceInThishour[0] = priceJson.get("preis").getAsFloat();
                    }

                }
        );

        float result = absoulteValue*priceInThishour[0] ;
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

    private float calculationSumConsumption (String date1)  {
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

        return (result);
    }

    private float calculationDifferenceEachhour (float [] sumOfRequestedPowrinEachHour, String date1) {

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
        /*TODO make instead of date  ComposeDate to contain Hour */
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

        jobDuration = jobDuration + durationPar;

        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, intTemplate.getConnectionFactory());
        numberOfGenerationOfOneJob = amountOfGeneration.get();

        if (actualNumberOfGenerationOfOneJob != numberOfGenerationOfOneJob) {
            logger.info("the time taken to calculate this sub population for one generation is " + durationPar + " Minutes");
        }
        else
        {
            actualNumberOfGenerationOfOneJob = 0;
            //configResetter.reset();
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




