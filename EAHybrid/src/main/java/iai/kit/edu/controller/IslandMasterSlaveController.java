package iai.kit.edu.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iai.kit.edu.config.ChromosomeListConverter;
import iai.kit.edu.core.GLEAMPopulation;
import iai.kit.edu.producer.IntermediatePopulationPublisher;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ims")
public class IslandMasterSlaveController {
	
	@Autowired
	ChromosomeListConverter chromosomeConverter;
	
	private GLEAMPopulation intermediatePopulation = new GLEAMPopulation();
	
	@Autowired
	IntermediatePopulationPublisher intermediatePopulationPublisher;
	
	
    /**
     * Receive result from master slave and convert it to island population
     * file
     * @param result list of master slave calculation
     */
    @RequestMapping(value = "/receiveResult", method = RequestMethod.POST)
    public void receiveIntermediateResult(@RequestBody String resultList) {
    	File populationFile = chromosomeConverter.convertChromosomeListToIsland(resultList);
        this.intermediatePopulation.read(populationFile);
        intermediatePopulationPublisher.publishIntermediatePopulation(this.intermediatePopulation);
        
    }
	

}
