package iai.kit.edu.consumer;

import com.google.gson.Gson;
import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.EAEpochConfig;
import iai.kit.edu.producer.EAExecutiontimePublisher;
import iai.kit.edu.producer.IntermediatePopulationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * Receives configuration for one epoch
 */
public class EAEpochSubscriber implements MessageListener {
	@Autowired
	AlgorithmStarter algorithmStarter;

	@Autowired
	EAExecutiontimePublisher eaExecutiontimePublisher;

	@Value("${island.number}")
	private int islandNumber;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private EAEpochConfig eaEpochConfig;

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	File populationFile;

	@Autowired
	IntermediatePopulationPublisher intermediatePopulationPublisher;

	@Autowired
	File populationIntialFile;


	/**
	 * After receiving the configuration for one epoch, the algorithmStarter is
	 * configured and afterwards one epoch executed and the intermediate population
	 * is sent back to Migration & Synchronization Service
	 * 
	 * @param message
	 * @param pattern
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		logger.info("received epoch configuration");
		Gson gson = new Gson();
		algorithmStarter.reset();
		this.eaEpochConfig = gson.fromJson(message.toString(), EAEpochConfig.class);
		algorithmStarter.setPopulationSize(eaEpochConfig.getPopulationSize());
		algorithmStarter.setDelay(eaEpochConfig.getDelay());
		algorithmStarter.setDemeSize(eaEpochConfig.getDemeSize());
		algorithmStarter.setAcceptanceRuleForOffspring(eaEpochConfig.getAcceptanceRuleForOffspring());
		algorithmStarter.setRankingParameter(eaEpochConfig.getRankingParameter());
		algorithmStarter.setAmountFitness(eaEpochConfig.getAmountFitness());
		algorithmStarter.setInitStrategy(eaEpochConfig.getInitStrategy());
		setTerminationCriterion();
		this.eaEpochConfig.getPopulation().writeInitialPopulation(populationFile);
		algorithmStarter.start();
		eaExecutiontimePublisher.publishEAExecutiontime();
		this.eaEpochConfig.getPopulation().read(populationFile);
		intermediatePopulationPublisher.publishIntermediatePopulation(this.eaEpochConfig.getPopulation());
	}
	
	

	private void setTerminationCriterion() {
		switch (this.eaEpochConfig.getEpochTerminationCriterion()) {
		case (ConstantStrings.terminationEvaluation):
			algorithmStarter.setTerminationEvaluation(eaEpochConfig.getEpochTerminationEvaluation());
			break;
		case (ConstantStrings.terminationFitness):
			algorithmStarter.setTerminationFitness(eaEpochConfig.getEpochTerminationFitness());
			break;
		case (ConstantStrings.terminationGeneration):
			algorithmStarter.setTerminationGeneration(eaEpochConfig.getEpochTerminationGeneration());
			break;
		case (ConstantStrings.terminationTime):
			algorithmStarter.setTerminationTime(eaEpochConfig.getEpochTerminationTime());
			break;
		}
	}
}
