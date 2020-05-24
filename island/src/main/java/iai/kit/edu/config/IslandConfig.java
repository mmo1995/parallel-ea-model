package iai.kit.edu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about the island and its execution
 */
public class IslandConfig {
    //Island number, should be set as command line parameter --island.number=x
    @Value("${island.number}")
    private int islandNumber;

    private boolean receivedMigrantsCompleted;
    private boolean receivedIntermediatePopulation;

    private AlgorithmConfig algorithmConfig;

    private MigrationConfig migrationConfig;

    private List<Integer> neighbors;

    private RedisTemplate<String, Integer> template;

    private boolean stopped = false;

    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    public IslandConfig(RedisTemplate<String, Integer> template) {
        this.template = template;
    }

    public int getIslandNumber() {
        return islandNumber;
    }

    public void setIslandNumber(int islandNumber) {
        this.islandNumber = islandNumber;
    }

    public void setNeighbors(List<String> neighbors) {
        List<Integer> neighborsConverted = new ArrayList<>();
        for (String neighborID:neighbors){
            neighborsConverted.add(Integer.parseInt(neighborID));
        }
        this.neighbors = neighborsConverted;
    }

    public void setAlgorithmConfig(AlgorithmConfig algorithmConfig) {
        this.algorithmConfig = algorithmConfig;
    }

    public AlgorithmConfig getAlgorithmConfig(){return this.algorithmConfig;}

    public void setMigrationConfig(MigrationConfig migrationConfig) {
        this.migrationConfig = migrationConfig;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public MigrationConfig getMigrationConfig(){return migrationConfig;}

    public List<Integer> getNeighbors() {
        return this.neighbors;
    }

    public void reset(){
        this.stopped=false;
        this.receivedIntermediatePopulation=false;
        this.receivedMigrantsCompleted=false;
    }

    public boolean isReceivedMigrantsCompleted() {
        return receivedMigrantsCompleted;
    }

    public void setReceivedMigrantsCompleted(boolean receivedMigrantsCompleted) {
        this.receivedMigrantsCompleted = receivedMigrantsCompleted;
    }

    public boolean isReceivedIntermediatePopulation() {
        return receivedIntermediatePopulation;
    }

    public void setReceivedIntermediatePopulation(boolean receivedIntermediatePopulation) {
        this.receivedIntermediatePopulation = receivedIntermediatePopulation;
    }
}
