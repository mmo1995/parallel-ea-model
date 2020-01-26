package iai.kit.edu.controller;

import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.InitializerEAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Called by Coordination Service to choose best chromosome from population
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chromosomes")
public class BestChromosomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AlgorithmStarter algorithmStarter;

    /**
     * Creates best Chromosome
     * @param population The population from which the best chromosome has to be chosen
     * @return best chromosome
     */
    @RequestMapping(value = "/best", method = RequestMethod.POST)
    public String receiveInitializationConfiguration(@RequestBody List<String> population) {
        logger.info("received request to choose best chromosome ");
        try {
            writePopulation(population);
        } catch (IOException e) {
            e.printStackTrace();
        }
        algorithmStarter.setAmountFitness(1);
        algorithmStarter.setInitStrategy(ConstantStrings.initStrategyBestNew);
        algorithmStarter.setIslandNumber(0);
        algorithmStarter.chooseBestChromosome();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String bestChromosome = readChromosome();
        return bestChromosome;
    }

    private void writePopulation(List<String> population) throws IOException {
        BufferedWriter writer = null;
        File starter = new File(ConstantStrings.initializerPath + ConstantStrings.initialPopulationFileName);
        if(starter.exists() && starter.isFile()){
            starter.delete();
        }
        starter.createNewFile();


            writer = new BufferedWriter(new FileWriter(starter.getPath()));
            for(int i = 0; i< population.size(); i++) {
                writer.write(population.get(i));
                writer.newLine();
            }
            writer.close();

    }

    /**
     * Reads best Chromosome
     * @return best Chromosome
     */
    private String readChromosome() {
        String chromosome = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ConstantStrings.initializerPath + ConstantStrings.initialPopulationCalculatedFileName));
            String line = bufferedReader.readLine();

            while (line != "" && line != null) {
                if (line.endsWith(ConstantStrings.chromosomeEnding)) {
                    chromosome = chromosome + line;
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
        return chromosome;
    }
}
