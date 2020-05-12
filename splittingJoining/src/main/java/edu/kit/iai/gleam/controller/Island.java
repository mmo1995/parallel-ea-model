package edu.kit.iai.gleam.controller;

import com.google.gson.Gson;
import edu.kit.iai.gleam.config.ConstantStrings;
import edu.kit.iai.gleam.producer.ConfigurationAvailablePublisher;
import edu.kit.iai.gleam.producer.DynamicConfigurationAvailablePublisher;
import edu.kit.iai.gleam.producer.InitialPopulationPublisher;
import edu.kit.iai.gleam.producer.SlavesPopulationPublisher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Central REST-controller for the Coarse-Grained Model (island model)
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sjs")
public class Island {

    @Autowired
    private Gson gson;
    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    @Autowired
    @Qualifier("integerTemplate")
    private RedisTemplate<String, Integer> integerTemplate;

    @Autowired
    InitialPopulationPublisher initialPopulationPublisher;

    @Autowired
    SlavesPopulationPublisher slavesPopulationPublisher;

    @Autowired
    ConfigurationAvailablePublisher configurationAvailablePublisher;

    @Autowired
    DynamicConfigurationAvailablePublisher dynamicConfigurationAvailablePublisher;



    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public int numberOfChromosomes;
    private String rest;
    private RedisAtomicInteger amountOfGeneration;
    private RedisAtomicInteger amountOfSlaves;

    @Autowired
    ResultController resultController;

    /**
     * Receives the initialPopulation from the Coordination Service, divides it, stores it in Temporary DB and notifies
     * the islands that the initial population is available
     * @param initialPopulation
     */
    @RequestMapping(value = "/population/initial", method = RequestMethod.POST)
    public void splitPopulation(@RequestBody List<String> initialPopulation) {
        logger.info("received initial population"+"\n"+initialPopulation.size());
        RedisAtomicInteger numberOfIslandsCounter = new RedisAtomicInteger(ConstantStrings.numberOfIslands, integerTemplate.getConnectionFactory());
        int numberOfIslands = numberOfIslandsCounter.get();
        int chromosomesPerIsland = initialPopulation.size() / numberOfIslands;
        List<List<String>> dividedPopulation = new ArrayList<>();
        int populationCounter = 0;
        // Remainder is evenly distributed to the first #remainder islands
        int remainder = initialPopulation.size() % numberOfIslands;

        for (int islandCounter = 1; islandCounter <= numberOfIslands; islandCounter++) {
            if (islandCounter <= remainder) {
                dividedPopulation.add(initialPopulation.subList(populationCounter, populationCounter + chromosomesPerIsland + 1));
                populationCounter += (chromosomesPerIsland + 1);
            } else {
                dividedPopulation.add(initialPopulation.subList(populationCounter, populationCounter + chromosomesPerIsland));
                populationCounter += chromosomesPerIsland;
            }
        }

        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
        for (int i = 0; i < dividedPopulation.size(); i++) {
            String json = gson.toJson(dividedPopulation.get(i));
            ops.set(ConstantStrings.initialPopulation + "." + (i + 1), json);
        }
        initialPopulationPublisher.publish("Initial population available");
    }


    /**
     * Receives migration information from Coordination Service, stores it in Temporary DB, notifies islands about
     * availability.
     * @param migrationConfig
     */
    @RequestMapping(value = "/config/migration", method = RequestMethod.POST)
    public void sendMigrationConfig(@RequestBody String migrationConfig) {
        //logger.info("received migration configuration");
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();

        ops.set(ConstantStrings.managementConfigMigration, migrationConfig);
        //logger.info("stored migration configuration");
    }
    /**
     * Receives algorithmConfig from Coordination Service, stores it in Temporary DB, notifies islands about
     * availability.
     * @param algorithmConfig
     */
    @RequestMapping(value = "/config/algorithm", method = RequestMethod.POST)
    public void sendAlgorithmConfig(@RequestBody String algorithmConfig) {
        //logger.info("received algorithm configuration");
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();

        ops.set(ConstantStrings.managementConfigAlgorithm, algorithmConfig);
        //logger.info("stored algorithm configuration");
    }

    /**
     * Receives topology information from Coordination Service, stores it in Temporary DB, notifies islands about
     * availability.
     * @param neighborsConfigJson
     */
    @RequestMapping(value = "/config/neighbors", method = RequestMethod.POST)
    public void sendNeighborsConfig(@RequestBody String neighborsConfigJson) {
        //logger.info("received neighbors configuration");
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
        List<List<String>> neighborsConfig = gson.fromJson(neighborsConfigJson, List.class);
        for (int i = 0; i < neighborsConfig.size(); i++) {
            ops.set(ConstantStrings.managementConfigNeighbor + "." + (i + 1), gson.toJson(neighborsConfig.get(i)));
        }
        //logger.info("stored neighbors configuration");
        configurationAvailablePublisher.publish("Configuration available");
    }

    /**
     * Receives topology information from Coordination Service, stores it in Temporary DB, notifies islands about
     * availability.
     * @param neighborsConfigJson
     */
    @RequestMapping(value = "/config/dynamic/neighbors", method = RequestMethod.POST)
    public void sendNeighborsConfigDynamic(@RequestBody String neighborsConfigJson) {
        //logger.info("received neighbors configuration");
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
        List<List<String>> neighborsConfig = gson.fromJson(neighborsConfigJson, List.class);
        for (int i = 0; i < neighborsConfig.size(); i++) {
            ops.set(ConstantStrings.managementConfigNeighbor + "." + (i + 1), gson.toJson(neighborsConfig.get(i)));
        }
        //logger.info("stored neighbors configuration");
        dynamicConfigurationAvailablePublisher.publish("Configuration available");
    }



    /**
     * receives population from starter/coordination service, divides it and notifies slaves
     * that population is available
     * @param initialPopulationWithId
     */

    @RequestMapping(value = "/population/{islandnumber}/slaves", method = RequestMethod.POST)
    public void splitPopulationForSlaves(@PathVariable String islandnumber, @RequestBody String initialPopulationWithId) {
        synchronized (this){
            //logger.info("received a population of one  generation");
            String initialPopulation;
            String idNumber;
            if(initialPopulationWithId.contains("#"))
            {
                initialPopulation = initialPopulationWithId.substring(0, initialPopulationWithId.indexOf("#"));
                idNumber = initialPopulationWithId.substring(initialPopulationWithId.indexOf("#") + 1);
            } else{
                initialPopulation = initialPopulationWithId;
                idNumber = "1";
            }
            ValueOperations<String, Integer> ops = this.integerTemplate.opsForValue();
            RedisAtomicInteger numberOfIslandsCounter = new RedisAtomicInteger(ConstantStrings.numberOfIslands, integerTemplate.getConnectionFactory());
            int numberOfIslands = numberOfIslandsCounter.get();
            int numberOfSlaves = ops.get(ConstantStrings.numberOfSlavesTopic);
            RedisAtomicInteger receivedResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedResultsCounter, integerTemplate.getConnectionFactory());
            receivedResultsCounter.set(0);
            int currentIslandNumber = Integer.parseInt(islandnumber);
            buildDistribution(initialPopulation,numberOfSlaves, idNumber, currentIslandNumber);

        }

    }




    private void buildDistribution(String chromosmeList, int containers, String idNumber,  int currentIslandNumber) {
        numberOfChromosomes = 0;
        rest = "";
        chromosmeList = chromosmeList.trim();
        String head = chromosmeList.substring(0, chromosmeList.indexOf("\n")).trim();
        numberOfChromosomes = Integer.parseInt(chromosmeList.substring(0, head.indexOf(" ")));
        if (numberOfChromosomes > 1)
        {
            rest = chromosmeList.substring(chromosmeList.indexOf("\n") + 1, chromosmeList.length());
            build(rest, numberOfChromosomes,idNumber, containers, head, currentIslandNumber);
        }
        else
        {
            amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, integerTemplate.getConnectionFactory());
            amountOfSlaves = new RedisAtomicInteger(ConstantStrings.numberOfIslands, integerTemplate.getConnectionFactory());
            ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
           /* ops.set(ConstantStrings.stopSubscribing + ".1", "continue");
            for (int i =2;i<=amountOfIslands.get();i++) {
                logger.info("stop signal is sent to slave Nr. " + i);
                ops.set(ConstantStrings.stopSubscribing + "." + i, "stop");
                stopingPublisher.publish("stop", i);
            }*/
            amountOfSlaves.set(1);
            amountOfGeneration.set(1);
            resultController.actualNumberOfGenerationOfOneJob =0 ;
            //System.out.print("Following Chromosome List is sent to chromosome Interpreter" + partChrList);
            chromosmeList = chromosmeList.concat("#" + idNumber);
            ops.set(ConstantStrings.slavePopulation + ".1.1" , chromosmeList);
            slavesPopulationPublisher.publish("Initial population available",1,1);
            //logger.info("population is available to one slave");
        }

    }

    private void build(String rest, int numberOfChromosomes,String idNumber, int containers, String head, int currentIslandNumber) {
        int chromosomesPerSlave = numberOfChromosomes / containers;
        List<List<String>> dividedPopulation = new ArrayList<>();
        int populationCounter = 0;
        int remainder = numberOfChromosomes % containers;
        List<String> listOfChromosoms = new ArrayList<String>();
        String[] headArray = head.split(" ");
        String restHead = " " + headArray[1] + " " + headArray[2];

        int thirdNumberOfSecondline = 0;
        int indexOfN = 0;
        int indexOfStart;
        for (int i = 0; i < numberOfChromosomes; i++) {

            indexOfStart = 0;
            thirdNumberOfSecondline = Integer.parseInt(rest.substring(rest.indexOf(" ", rest.indexOf(" ") + 1) + 1, rest.indexOf("\n")));
            if (i < numberOfChromosomes - 1) {
                indexOfN = StringUtils.ordinalIndexOf(rest, "\n", thirdNumberOfSecondline + 1);
                listOfChromosoms.add(i, rest.substring(indexOfStart, indexOfN));
                indexOfStart = indexOfN + 1;
                indexOfN = rest.length();
                rest = rest.substring(indexOfStart, indexOfN);
            } else {
                //indexOfN = StringUtils.ordinalIndexOf(rest, "\n", thirdNumberOfSecondline);
                indexOfN = rest.length();
                listOfChromosoms.add(i, rest.substring(indexOfStart, indexOfN));
                indexOfStart = indexOfN;
                rest = rest.substring(indexOfStart, indexOfN);
            }
        }


        for (int portion = 1; portion <= containers; portion++) {
            if (portion <= remainder) {

                dividedPopulation.add(listOfChromosoms.subList(populationCounter, populationCounter + chromosomesPerSlave + 1));
                populationCounter += (chromosomesPerSlave + 1);

            } else {
                //System.out.println("the populationCounter is " + populationCounter);
                //System.out.println("the chromosomesPerIsland is " + chromosomesPerIsland);
                //System.out.println("the size of sub is " + (listOfChromosoms.subList(populationCounter, populationCounter + chromosomesPerIsland).size()));

                dividedPopulation.add(listOfChromosoms.subList(populationCounter, populationCounter + chromosomesPerSlave));
                populationCounter += chromosomesPerSlave;
            }
        }
        //System.out.println("length of  dividedPopulation is " + dividedPopulation.size());

        for (int numberOfChrinList = 0; numberOfChrinList < dividedPopulation.size(); numberOfChrinList++) {
            String partChrHead = dividedPopulation.get(numberOfChrinList).size() + restHead;
            String partChrList = "";
            for (int numberOfChrinListInsid = 0; numberOfChrinListInsid < dividedPopulation.get(numberOfChrinList).size(); numberOfChrinListInsid++) {
                partChrList = partChrList + dividedPopulation.get(numberOfChrinList).get(numberOfChrinListInsid) + "\n";
            }
            partChrList = partChrHead + "\n" + partChrList;
            partChrList = partChrList.concat("#" + idNumber);
            ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
            //System.out.print("Following Chromosome List is sent to chromosome Interpreter" + partChrList);
            int numberOfChannel = numberOfChrinList + 1;
            ops.set(ConstantStrings.slavePopulation + "." + currentIslandNumber + "." + numberOfChannel, partChrList);
            slavesPopulationPublisher.publish("Initial population available", currentIslandNumber,numberOfChannel);
            //logger.info ("population is available to slave Nr. " + numberOfChannel);


        }

    }

}
