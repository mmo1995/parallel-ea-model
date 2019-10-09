package iai.kit.edu.controller;

import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.InitializerEAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Called by Coordination Service to create initial population
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ine")
public class InitialPopulationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AlgorithmStarter algorithmStarter;

    /**
     * Creates initial population
     * @param initializerEAConfig parameters to initialize population
     * @return initial population
     */
    @RequestMapping(value = "/population/initial", method = RequestMethod.POST)
    public List<String> receiveInitializationConfiguration(@RequestBody InitializerEAConfig initializerEAConfig) {
        logger.info("received request: "+initializerEAConfig.toString());
        algorithmStarter.setPopulationSize(initializerEAConfig.getPopulationSize());
        algorithmStarter.setAmountFitness(initializerEAConfig.getAmountFitness());
        algorithmStarter.setInitStrategy(initializerEAConfig.getInitStrategy());
        algorithmStarter.setIslandNumber(0);
        algorithmStarter.init();
        List<String>initialPopulation = readPopulation();
        return initialPopulation;
    }

    /**
     * Reads created population
     * @return initial population
     */
    private List<String> readPopulation() {
        List<String> population = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ConstantStrings.initializerPath + ConstantStrings.initialPopulationCalculatedFileName));
            String line = bufferedReader.readLine();
            String chromosome = "";
            while (line != "" && line != null) {
                if (line.endsWith(ConstantStrings.chromosomeEnding)) {
                    chromosome = chromosome + line;
                    population.add(chromosome);
                    chromosome = "";
                } else {
                    chromosome = chromosome + line + "\n";
                }
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return population;
    }

}
