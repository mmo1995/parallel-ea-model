package edu.kit.iai.gleam.controller;

import com.google.gson.Gson;
import edu.kit.iai.gleam.config.ConstantStrings;
import edu.kit.iai.gleam.producer.ConfigurationAvailablePublisher;
import edu.kit.iai.gleam.producer.InitialPopulationPublisher;
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
    ConfigurationAvailablePublisher configurationAvailablePublisher;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Receives the initialPopulation from the Coordination Service, divides it, stores it in Temporary DB and notifies
     * the islands that the initial population is available
     * @param initialPopulation
     */
    @RequestMapping(value = "/population/initial", method = RequestMethod.POST)
    public void splitPopulation(@RequestBody List<String> initialPopulation) {
        logger.info("received initial population"+"\n"+initialPopulation);
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
        logger.info("received migration configuration");
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();

        ops.set(ConstantStrings.managementConfigMigration, migrationConfig);
        logger.info("stored migration configuration");
    }

    /**
     * Receives algorithmConfig from Coordination Service, stores it in Temporary DB, notifies islands about
     * availability.
     * @param algorithmConfig
     */
    @RequestMapping(value = "/config/algorithm", method = RequestMethod.POST)
    public void sendAlgorithmConfig(@RequestBody String algorithmConfig) {
        logger.info("received algorithm configuration");
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();

        ops.set(ConstantStrings.managementConfigAlgorithm, algorithmConfig);
        logger.info("stored algorithm configuration");
    }

    /**
     * Receives topology information from Coordination Service, stores it in Temporary DB, notifies islands about
     * availability.
     * @param neighborsConfigJson
     */
    @RequestMapping(value = "/config/neighbors", method = RequestMethod.POST)
    public void sendNeighborsConfig(@RequestBody String neighborsConfigJson) {
        logger.info("received neighbors configuration");
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
        List<List<String>> neighborsConfig = gson.fromJson(neighborsConfigJson, List.class);
        for (int i = 0; i < neighborsConfig.size(); i++) {
            ops.set(ConstantStrings.managementConfigNeighbor + "." + (i + 1), gson.toJson(neighborsConfig.get(i)));
        }
        logger.info("stored neighbors configuration");
        configurationAvailablePublisher.publish("Configuration available");
    }
}
