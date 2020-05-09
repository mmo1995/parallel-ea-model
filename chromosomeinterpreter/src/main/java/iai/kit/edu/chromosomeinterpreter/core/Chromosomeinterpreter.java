package iai.kit.edu.chromosomeinterpreter.core;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.*;


import iai.kit.edu.chromosomeinterpreter.config.ConfigResetter;
import iai.kit.edu.chromosomeinterpreter.config.ConstantStrings;
import iai.kit.edu.chromosomeinterpreter.producer.CalculationConfigPublisher;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Chromosomeinterpreter {
    @Autowired
    private Gson gson;
    private String rest;
    private int numberOfChromosomes;
    private  List<SchedulingPlan> listSchedulingPlan;
    private JsonObject forecasting;
    private static RestTemplate restTemplate = new RestTemplate();
    private static String date = "2011-01-10";
    /*public static ResponseEntity<String> getGeneration;
    public static ResponseEntity<String> getConsumption;*/
    public static String getGeneration;
    public static String getConsumption;
    private float genValueInObjectJson;
    private String [] jsonObjectGen;
    private  List<JsonObject> JsonObjectGenList;
    @Autowired
    ConfigResetter configResetter;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CalculationConfigPublisher calculationConfigPublisher;
    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> intTemplate;

    long startTime;
    long endTime;
    double jobDuration;
    double generationduration;

    private RedisAtomicInteger amountOfGeneration;
    private int numberOfGenerationOfOneJob;
    private int actualNumberOfGenerationOfOneJob = 0;

    public void mainChromosomeInterpreter(String chromosmeListWithId) throws IOException {
        String chromosomeList = chromosmeListWithId.substring(0, chromosmeListWithId.indexOf("#"));
        String idNumber = chromosmeListWithId.substring(chromosmeListWithId.indexOf("#")+1);

        startTime = System.currentTimeMillis();

        JsonArray jsonArrayConsumption = new Gson().fromJson(getGeneration, JsonArray.class); // if you use API use the above line
        JsonObjectGenList = new ArrayList<JsonObject>();

        for (int i = 0; i < jsonArrayConsumption.size(); i++)
        {
            JsonObject plan = jsonArrayConsumption.get(i).getAsJsonObject();
            JsonObjectGenList.add(plan);
        }

        chromosmeListAnalysis(chromosomeList);
        buildingAllocationMatrix(rest, numberOfChromosomes, idNumber);
        // readForcastinData ();
    }

    public void chromosmeListAnalysis(String chromosmeList) {
        numberOfChromosomes = 0;
        rest = "";
        chromosmeList = chromosmeList.trim();
        String head = chromosmeList.substring(0, chromosmeList.indexOf("\n")).trim();
        numberOfChromosomes = Integer.parseInt(chromosmeList.substring(0, head.indexOf(" ")));
        logger.info("received a sub population with  " + numberOfChromosomes+ " chromosomes");
        logger.info("the date to be scheduled is "+ date);
        jsonObjectGen = getGeneration.split("},");
        rest = chromosmeList.substring(chromosmeList.indexOf("\n") + 1, chromosmeList.length());
    }


    public void buildingAllocationMatrix(String rest, int numberOfChromosomes, String idNumber) throws IOException {
        List<String> listOfChromosoms = new ArrayList<String>();

        /********************** Start isolation of each chromosome in one list slot*/
        if (numberOfChromosomes == 1) {
            listOfChromosoms.add(0, rest);
            logger.info("Best Chromosome is received");

        } else {
            long startTime1 = System.currentTimeMillis();

            int thirdNumberOfSecondline = 0;
            int indexOfN = 0;
            int indexOfStart;
            for (int i = 0; i < numberOfChromosomes; i++) {

                indexOfStart = 0;
                thirdNumberOfSecondline = Integer.parseInt(rest.substring(rest.indexOf(" ", rest.indexOf(" ") + 1) + 1, rest.indexOf("\n")));
                if (i < numberOfChromosomes - 1) {
                    indexOfN = StringUtils.ordinalIndexOf(rest, "\n", thirdNumberOfSecondline + 1);
                } else {
                    indexOfN = StringUtils.ordinalIndexOf(rest, "\n", thirdNumberOfSecondline);
                }
                listOfChromosoms.add(i, rest.substring(indexOfStart, indexOfN));
                indexOfStart = indexOfN + 1;
                indexOfN = rest.length();
                rest = rest.substring(indexOfStart, indexOfN);
            }
            long endTime1 = System.currentTimeMillis();
            long generationduration1 = (endTime1 - startTime1);
            // logger.info("the time taken to isolation each chromosome in one list is " + generationduration1 + " Milli Second");

        }
        /********************** End of isolation each chromosome in one list slot*/
        listSchedulingPlan = Collections.synchronizedList(new ArrayList<>(listOfChromosoms.size()));
        long startTime3 = System.currentTimeMillis();
        listOfChromosoms.stream().parallel().forEach(chromosome -> {
            try {
                interpreterParallelChromosome(chromosome);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        long endTime3 = System.currentTimeMillis();
        long generationduration1 = (endTime3 - startTime3);
        //logger.info("the time taken to interpretation all chromosomes  is " + generationduration1 + " Milli Second");

        gson = new Gson();
        String jsonInString = gson.toJson(listSchedulingPlan);
        jsonInString = jsonInString.concat("#" + idNumber);
        calculationConfigPublisher.publishCalculationConfig(jsonInString);

        if  (listSchedulingPlan.size() == 1)
        {
            buildingFinalPlan (listSchedulingPlan);
            // FileUtils.writeStringToFile(new File("/home/ws/na8117/Schreibtisch/Intermed_SchedulingOnline_V3_Redis/chromosomeinterpreter/A1.json"), jsonInString);

        }
        listSchedulingPlan = new ArrayList<>();
        endTime = System.currentTimeMillis();
        generationduration = TimeUnit.MILLISECONDS.toMinutes(endTime - startTime);
        caculateDuration(generationduration);
    }

    public  void  interpreterParallelChromosome (String chromosome) throws IOException {

        List<SchedulingPlan> listSchedulingPlanTemp = new ArrayList<>();
        String[] genes;
        List<Integer> resources;
        int indexOfFirstWhiteSpaceFirst = 0;
        int indexOfFirstWhiteSpace = 0;
        int indexOfSecondWhiteSpace = 0;
        int indexOfThirdWhiteSpace = 0;
        int indexOfResourceID = 0;
        int indexOfEndPowerFraction = 0;
        int chromosomeID = 0;
        int childID = 0;
        int resourceID = 0;
        int startHour = 0;
        int endHour = 0;
        int duration = 0;
        float powerFraction = 0;
        ResourcePlan resourcePlan;
        List<ResourcePlan> listResourcePlan;
        SchedulingPlan schedulingPlan;
        schedulingPlan = new SchedulingPlan();
        resources = new ArrayList<>();

        genes = chromosome.split("\n"); // genes of the k-th chromosome
        indexOfFirstWhiteSpace = StringUtils.ordinalIndexOf(genes[0], " ", 1);
        indexOfSecondWhiteSpace = StringUtils.ordinalIndexOf(genes[0], " ", 2);
        chromosomeID = Integer.parseInt((genes[0].substring(indexOfResourceID, indexOfFirstWhiteSpace)).replaceAll("\\s+", ""));
        childID = Integer.parseInt((genes[0].substring(indexOfFirstWhiteSpace, indexOfSecondWhiteSpace)).replaceAll("\\s+", ""));
        schedulingPlan.setplanID(chromosomeID);
        schedulingPlan.setChildID(childID);
        schedulingPlan.setNrOfGenes(genes.length-1);

        for (int r = 1; r < genes.length; r++) //for each gene in the chromosome to find the unique of resources
        {
            indexOfFirstWhiteSpaceFirst = StringUtils.ordinalIndexOf(genes[r], " ", 1);
            resourceID = Integer.parseInt(genes[r].substring(indexOfResourceID, indexOfFirstWhiteSpaceFirst));
            if (!resources.contains(resourceID)) {
                resources.add(resourceID);
            }
        }
        Collections.sort(resources);
        listResourcePlan = new ArrayList<>();
        for (int t = 0; t < resources.size(); t++) {
            resourcePlan = new ResourcePlan();
            resourcePlan.setresourceID(resources.get(t));
            resourcePlan.setPowerFraction(setPowerFractionInside(new float[24], 0, 23, 0, 0));
            listResourcePlan.add(resourcePlan);
        }

        //logger.info("the number of genes in this chromosome " + (genes.length-1));

        List<String> genesStream = new LinkedList<String>(Arrays.asList(genes));
        genesStream.remove(0);
        for (String gen : genesStream)
        {
            indexOfFirstWhiteSpaceFirst = StringUtils.ordinalIndexOf(gen, " ", 1);
            indexOfFirstWhiteSpace = StringUtils.ordinalIndexOf(gen, " ", 2);
            indexOfSecondWhiteSpace = StringUtils.ordinalIndexOf(gen, " ", 3);
            indexOfThirdWhiteSpace = StringUtils.ordinalIndexOf(gen, " ", 4);
            indexOfEndPowerFraction = gen.length();

            resourceID = Integer.parseInt(gen.substring(indexOfResourceID, indexOfFirstWhiteSpaceFirst));
            startHour = Integer.parseInt(gen.substring(indexOfFirstWhiteSpace + 1, indexOfSecondWhiteSpace));
            duration = Integer.parseInt(gen.substring(indexOfSecondWhiteSpace + 1, indexOfThirdWhiteSpace));
            powerFraction = Float.parseFloat(gen.substring(indexOfThirdWhiteSpace + 1, indexOfEndPowerFraction));
            endHour = startHour + duration;
            // Usecase 1
            if (endHour > 24) {
                endHour = 24;
                //System.out.println("\n *******Start + endHour > 23**********" + "  " + endHour);
            }
            //Usecase 2
           /* if (resourceID > 7 && resourceID < 18 && endHour > 17) {
                endHour = 17;
            }
            if (resourceID > 25 && resourceID < 51 && endHour > 24) {
                endHour = 24;
            }*/


            for (int ll = 0; ll < listResourcePlan.size(); ll++) {
                if (resourceID == listResourcePlan.get(ll).getresourceID()) {
                    genValueInObjectJson = 0;
                    listResourcePlan.get(ll).setPowerFraction(setPowerFractionInside(listResourcePlan.get(ll).getPowerFraction(), startHour, endHour, powerFraction, resourceID));
                    break;
                }
            }

        }
        schedulingPlan.setResourcePlan(listResourcePlan);
        listSchedulingPlan.add(schedulingPlan);


      /*  for (int k = 0; k < listOfChromosoms.size();k++) {

            schedulingPlan = new SchedulingPlan();
            resources = new ArrayList<>();

            genes = listOfChromosoms.get(k).split("\n"); // genes of the k-th chromosome
            indexOfFirstWhiteSpace = StringUtils.ordinalIndexOf(genes[0], " ", 1);
            indexOfSecondWhiteSpace = StringUtils.ordinalIndexOf(genes[0], " ", 2);
            chromosomeID = Integer.parseInt((genes[0].substring(indexOfResourceID, indexOfFirstWhiteSpace)).replaceAll("\\s+", ""));
            childID = Integer.parseInt((genes[0].substring(indexOfFirstWhiteSpace, indexOfSecondWhiteSpace)).replaceAll("\\s+", ""));
            schedulingPlan.setplanID(chromosomeID);
            schedulingPlan.setChildID(childID);
            for (int r = 1; r < genes.length; r++) //for each gene in the chromosome to find the unique of resources
            {
                indexOfFirstWhiteSpaceFirst = StringUtils.ordinalIndexOf(genes[r], " ", 1);
                resourceID = Integer.parseInt(genes[r].substring(indexOfResourceID, indexOfFirstWhiteSpaceFirst));
                if (!resources.contains(resourceID)) {
                    resources.add(resourceID);
                }
            }

            listResourcePlan = new ArrayList<>();
            for (int t = 0; t < resources.size(); t++) {
                resourcePlan = new ResourcePlan();
                resourcePlan.setresourceID(resources.get(t));
                resourcePlan.setPowerFraction(setPowerFractionInside(new float[24], 0, 23, 0, 0));
                listResourcePlan.add(resourcePlan);
            }

            for (int r = 1; r < genes.length; r++) {
                indexOfFirstWhiteSpaceFirst = StringUtils.ordinalIndexOf(genes[r], " ", 1);
                indexOfFirstWhiteSpace = StringUtils.ordinalIndexOf(genes[r], " ", 2);
                indexOfSecondWhiteSpace = StringUtils.ordinalIndexOf(genes[r], " ", 3);
                indexOfThirdWhiteSpace = StringUtils.ordinalIndexOf(genes[r], " ", 4);
                indexOfEndPowerFraction = genes[r].length();

                resourceID = Integer.parseInt(genes[r].substring(indexOfResourceID, indexOfFirstWhiteSpaceFirst));
                startHour = Integer.parseInt(genes[r].substring(indexOfFirstWhiteSpace + 1, indexOfSecondWhiteSpace));
                duration = Integer.parseInt(genes[r].substring(indexOfSecondWhiteSpace + 1, indexOfThirdWhiteSpace));
                powerFraction = Float.parseFloat(genes[r].substring(indexOfThirdWhiteSpace + 1, indexOfEndPowerFraction));
                endHour = startHour + duration;
                if (endHour > 24) {
                    endHour = 24;
                    //System.out.println("\n *******Start + endHour > 23**********" + "  " + endHour);
                }

                    for (int ll = 0; ll < listResourcePlan.size(); ll++) {
                        if (resourceID == listResourcePlan.get(ll).getresourceID()) {

                            listResourcePlan.get(ll).setPowerFraction(setPowerFractionInside(listResourcePlan.get(ll).getPowerFraction(), startHour, endHour, powerFraction, resourceID));
                        }
                    }

            }
            schedulingPlan.setResourcePlan(listResourcePlan);
            listSchedulingPlan.add(schedulingPlan);
        }*/

    }


    public  float [] setPowerFractionInside (float [] powerFr,int start, int end, float pf, int resID) throws IOException {
        // resID is the resource ID

        if (resID == 0) {
            for (int s = start; s < end; s++) {
                powerFr[s] = pf;
            }
            return powerFr;
        }
        else
        {
            for (int s = start; s < end; s++) {
                final float [] GenInThishour = {0};
                String composedDate;
                if(s < 10){
                    composedDate = String.format("%s %d%d", date,0,s);
                }
                else
                    composedDate = String.format("%s %d", date,s);
                JsonObjectGenList.stream().parallel().forEach(
                        generationsonObjects -> {
                            String dateInJsonObject = generationsonObjects.get("localhour").getAsString();
                            int resourceIDInJsonObject =  generationsonObjects.get("dataid").getAsInt();
                            String hour = composedDate.substring(composedDate.length() - 2);

                            if (dateInJsonObject.contains(composedDate) && resourceIDInJsonObject == resID) {
                                if (generationsonObjects.get("gen").getAsFloat() > 0.1 && (Integer.parseInt(hour)>7 && Integer.parseInt(hour)<18))
                                    GenInThishour[0] = generationsonObjects.get("gen").getAsFloat();
                                else
                                    GenInThishour[0] = (float) 0.5;
                            }

                        }
                );
                powerFr[s] = pf*GenInThishour[0];

            }
            return powerFr;
        }
    }

    /*public float restEnergy (String composedDate, int resID) throws IOException // calculate the rest for energy still by each house by sub gen-use
    {

        JsonArray jsonArrayConsumption = new Gson().fromJson(getGeneration, JsonArray.class); // if you use API use the above line
        String hour = "";
        float gen = 0;

        for (int i = 0; i < jsonArrayConsumption.size(); i++) {
            JsonObject generationsonObjects = jsonArrayConsumption.get(i).getAsJsonObject();
            String dateInJsonObject = generationsonObjects.get("localhour").getAsString();
            int resourceIDInJsonObject =  generationsonObjects.get("dataid").getAsInt();
            hour = composedDate.substring(composedDate.length() - 2);

            if (dateInJsonObject.contains(composedDate) && resourceIDInJsonObject == resID) {
                if (generationsonObjects.get("gen").getAsFloat() > 0.1 && (Integer.parseInt(hour)>8 && Integer.parseInt(hour)<17))
                    gen = generationsonObjects.get("gen").getAsFloat();
                else
                    gen = (float) 0.5;
                break;
            }
        }
        return gen;
    }
    public void getGenParallel (String jsonObjGen, String composedDate1, int resID)
    {
        String hour = "";
        float gen1 = 0;
        JsonObject generationsonObjects = new Gson().fromJson(jsonObjGen, JsonObject.class);
        String dateInJsonObject = generationsonObjects.get("localhour").getAsString();
        int resourceIDInJsonObject =  generationsonObjects.get("dataid").getAsInt();
        hour = composedDate1.substring(composedDate1.length() - 2);

        if (dateInJsonObject.contains(composedDate1) && resourceIDInJsonObject == resID) {
            if (generationsonObjects.get("gen").getAsFloat() > 0.1 && (Integer.parseInt(hour)>8 && Integer.parseInt(hour)<17))
                gen1 = generationsonObjects.get("gen").getAsFloat();
            else
                gen1 = (float) 0.5;
        }
        genValueInObjectJson =   gen1;


    }*/

    public void buildingFinalPlan (List<SchedulingPlan> finalPlan) throws IOException {

        List<ResourcePlan> tempResourcePlan = new ArrayList<>();

        List<FinalSchedulingPlan> data = new ArrayList<>();
        FinalSchedulingPlan tempFinalScheduling = new  FinalSchedulingPlan();
        //JsonArray jsonArrayConsumption = new Gson().fromJson(getConsumption.getBody(), JsonArray.class);
        JsonArray jsonArrayConsumption = new Gson().fromJson(getConsumption, JsonArray.class); // if you use API use the above line
        ResourcePlan consumptionAsResource = new ResourcePlan();
        JsonObject dataToVisualizeObject = new JsonObject();
        JsonArray dataToVisualizeArray = new JsonArray();
        consumptionAsResource.setresourceID(0);
        float [] consumption = new float[24];


        for (int i = 0; i < jsonArrayConsumption.size(); i++) {
            float consumptionInt = 0;
            JsonObject consumptionJsonObjects = jsonArrayConsumption.get(i).getAsJsonObject();
            String dateInJsonObject = consumptionJsonObjects.get("localhour").getAsString();
            if (dateInJsonObject.contains(date))
            {
                consumptionInt = consumptionJsonObjects.get("use").getAsFloat();
                consumption[i] = consumptionInt;
            }
        }

        consumptionAsResource.setPowerFraction(consumption);
        tempResourcePlan = finalPlan.get(0).getResourcePlan();
        tempResourcePlan.add(0, consumptionAsResource);

        finalPlan.get(0).setResourcePlan(tempResourcePlan);

        String day = date.substring(date.length() - 2);
        String month = date.substring(date.length() - 5, date.length() - 3);
        String year = date.substring(0, date.length() - 6);
        tempFinalScheduling.setDay(Integer.parseInt(day));
        tempFinalScheduling.setMonth(Integer.parseInt(month));
        tempFinalScheduling.setYear(Integer.parseInt(year));
        tempFinalScheduling.setPlanID(finalPlan.get(0).getplanID());
        tempFinalScheduling.setResourcePlan(finalPlan.get(0).getResourcePlan());
        gson = new Gson();
        String jsonInString = gson.toJson(tempFinalScheduling);
        logger.info("sending the final plan");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.splittingJoiningURL +"/opt/finalplan", jsonInString, String.class);
    }
    public void caculateDuration(double durationPar)
    {
        actualNumberOfGenerationOfOneJob++;
        jobDuration = jobDuration + durationPar;

        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, intTemplate.getConnectionFactory());
        numberOfGenerationOfOneJob = amountOfGeneration.get();
        if (actualNumberOfGenerationOfOneJob != numberOfGenerationOfOneJob) {
            logger.info("the time taken to interpret this sub population for one generation is " + durationPar + " Minutes");
        }
        else
        {
            actualNumberOfGenerationOfOneJob = 0;
            //configResetter.reset();
            logger.info("the time taken to interpret all parts of one job is " + jobDuration + " Minutes");
            logger.info("**************************************************************************************");
            jobDuration = 0;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        Chromosomeinterpreter.date = date;
    }
}
