package iai.kit.edu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import iai.kit.edu.producer.CalculationInitializedPublisher;

@Component
public class CalculationInitializedController implements ApplicationRunner {
	
	@Autowired
	CalculationInitializedPublisher publisher;
	
	@Value("${island.number}")
	int islandNumber;
	
	@Value("${calculation.number}")
	int calculationNumber;
	
	
    @Override
    public void run(ApplicationArguments applicationArguments) {
    	publisher.publishInitialized(islandNumber, calculationNumber);
    }

}
