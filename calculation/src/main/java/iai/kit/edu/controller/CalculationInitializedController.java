package iai.kit.edu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
public class CalculationInitializedController implements ApplicationRunner {
	
	@Value("${island.number}")
	int islandNumber;
	
	@Value("${calculation.number}")
	int calculationNumber;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
    @Override
    public void run(ApplicationArguments applicationArguments) {
    	logger.info("Calculation initialized with island number " + islandNumber + 
    			" and calculation number " + calculationNumber + ".");
    }

}
