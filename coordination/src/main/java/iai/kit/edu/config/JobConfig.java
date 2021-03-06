package iai.kit.edu.config;

import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;

/**
 * Stores configuration for one optimization task
 */
public class JobConfig {
    private int globalPopulationSize;
    private int numberOfIslands;
    private int numberOfSlaves;
    private int numberOfGeneration;
    private int migrationRate;
    private String topology = "ring";
    private String initialSelectionPolicy = "new";
    private int amountFitness = 0;
    private String selectionPolicy = "best";
    private String replacementPolicy = "worst";

    boolean asyncMigration = false;

    public String initialSelectionPolicyInitializer = "new";
    public int amountFitnessInitializer = 0;

    private String epochTerminationCriterion = "generation";
    private int epochTerminationEvaluation = 1000000;
    private double epochTerminationFitness = 100000.0;
    private int epochTerminationGeneration = 1;
    private int epochTerminationTime = 100000;
    private int epochTerminationGDV = 500;
    private int epochTerminationGAK = 100;

    private String globalTerminationCriterion = "generation";
    private int globalTerminationEpoch = 1;
    private int globalTerminationEvaluation = 1000000;
    private double globalTerminationFitness = 100000.0;
    private int globalTerminationGeneration = 1000;
    private int globalTerminationTime = 90;
    private int globalTerminationGDV = 500;
    private int globalTerminationGAK = 100;

    private int delay = 0;
    private int demeSize;


    private String acceptRuleForOffspring = "localLeast-ES";
    private double rankingParameter = 1.45;



    private double minimalHammingDistance = 0.1;
    private String evoFileName = "lsk_stnd.evo";

    public int getDelay() {
        return delay;
    }

    public int getDemeSize() {
        return demeSize;
    }

    public void setDemeSize(int demeSize) {
        this.demeSize = demeSize;
    }

    public int getGlobalPopulationSize() {
        return globalPopulationSize;
    }

    public void setGlobalPopulationSize(int globalPopulationSize) {
        this.globalPopulationSize = globalPopulationSize;
    }

    public int getNumberOfIslands() {
        return numberOfIslands;
    }

    public void setNumberOfIslands(int numberOfIslands) {
        this.numberOfIslands = numberOfIslands;
    }


    public int getNumberOfSlaves() {
        return numberOfSlaves;
    }

    public void setNumberOfSlaves(int numberOfSlaves) {
        this.numberOfSlaves = numberOfSlaves;
    }


    public int getNumberOfGeneration() {
        return numberOfGeneration;
    }

    public void setNumberOfGeneration(int numberOfGeneration) {
        this.numberOfGeneration = numberOfGeneration;
    }

    public int getMigrationRate() {
        return migrationRate;
    }

    public void setMigrationRate(int migrationRate) {
        this.migrationRate = migrationRate;
    }

    public String getTopology() {
        return topology;
    }

    public void setTopology(String topology) {
        this.topology = topology;
    }

    public String getInitialSelectionPolicy() {
        return initialSelectionPolicy;
    }

    public void setInitialSelectionPolicy(String initialSelectionPolicy) {
        this.initialSelectionPolicy = initialSelectionPolicy;
    }

    public int getAmountFitness() {
        return amountFitness;
    }

    public void setAmountFitness(int amountFitness) {
        this.amountFitness = amountFitness;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(String selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public String getReplacementPolicy() {
        return replacementPolicy;
    }

    public void setReplacementPolicy(String replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }

    public String getEpochTerminationCriterion() {
        return epochTerminationCriterion;
    }

    public void setEpochTerminationCriterion(String epochTerminationCriterion) {
        this.epochTerminationCriterion = epochTerminationCriterion;
    }

    public int getEpochTerminationEvaluation() {
        return epochTerminationEvaluation;
    }

    public void setEpochTerminationEvaluation(int epochTerminationEvaluation) {
        this.epochTerminationEvaluation = epochTerminationEvaluation;
    }

    public double getEpochTerminationFitness() {
        return epochTerminationFitness;
    }

    public void setEpochTerminationFitness(double epochTerminationFitness) {
        this.epochTerminationFitness = epochTerminationFitness;
    }

    public int getEpochTerminationGeneration() {
        return epochTerminationGeneration;
    }

    public void setEpochTerminationGeneration(int epochTerminationGeneration) {
        this.epochTerminationGeneration = epochTerminationGeneration;
    }

    public int getEpochTerminationTime() {
        return epochTerminationTime;
    }

    public void setEpochTerminationTime(int epochTerminationTime) {
        this.epochTerminationTime = epochTerminationTime;
    }

    public int getEpochTerminationGDV() {
        return epochTerminationGDV;
    }

    public void setEpochTerminationGDV(int epochTerminationGDV) {
        this.epochTerminationGDV = epochTerminationGDV;
    }

    public int getEpochTerminationGAK() {
        return epochTerminationGAK;
    }

    public void setEpochTerminationGAK(int epochTerminationGAK) {
        this.epochTerminationGAK = epochTerminationGAK;
    }

    public String getGlobalTerminationCriterion() {
        return globalTerminationCriterion;
    }

    public void setGlobalTerminationCriterion(String globalTerminationCriterion) {
        this.globalTerminationCriterion = globalTerminationCriterion;
    }

    public int getGlobalTerminationEpoch() {
        return globalTerminationEpoch;
    }

    public void setGlobalTerminationEpoch(int globalTerminationEpoch) {
        this.globalTerminationEpoch = globalTerminationEpoch;
    }

    public int getGlobalTerminationEvaluation() {
        return globalTerminationEvaluation;
    }

    public void setGlobalTerminationEvaluation(int globalTerminationEvaluation) {
        this.globalTerminationEvaluation = globalTerminationEvaluation;
    }

    public double getGlobalTerminationFitness() {
        return globalTerminationFitness;
    }

    public void setGlobalTerminationFitness(double globalTerminationFitness) {
        this.globalTerminationFitness = globalTerminationFitness;
    }

    public int getGlobalTerminationGeneration() {
        return globalTerminationGeneration;
    }

    public void setGlobalTerminationGeneration(int globalTerminationGeneration) {
        this.globalTerminationGeneration = globalTerminationGeneration;
    }

    public int getGlobalTerminationTime() {
        return globalTerminationTime;
    }

    public void setGlobalTerminationTime(int globalTerminationTime) {
        this.globalTerminationTime = globalTerminationTime;
    }

    public int getGlobalTerminationGDV() {
        return globalTerminationGDV;
    }

    public void setGlobalTerminationGDV(int globalTerminationGDV) {
        this.globalTerminationGDV = globalTerminationGDV;
    }

    public int getGlobalTerminationGAK() {
        return globalTerminationGAK;
    }

    public void setGlobalTerminationGAK(int globalTerminationGAK) {
        this.globalTerminationGAK = globalTerminationGAK;
    }


    public String getAcceptRuleForOffspring() {
        return acceptRuleForOffspring;
    }

    public void setAcceptRuleForOffspring(String acceptRuleForOffspring) {
        this.acceptRuleForOffspring = acceptRuleForOffspring;
    }

    public double getRankingParameter() {
        return rankingParameter;
    }

    public void setRankingParameter(double rankingParameter) {
        this.rankingParameter = rankingParameter;
    }

    public double getMinimalHammingDistance() {
        return minimalHammingDistance;
    }

    public void setMinimalHammingDistance(double minimalHammingDistance) {
        this.minimalHammingDistance = minimalHammingDistance;
    }

    public String getInitialSelectionPolicyInitializer() {
        return initialSelectionPolicyInitializer;
    }

    public void setInitialSelectionPolicyInitializer(String initialSelectionPolicyInitializer) {
        this.initialSelectionPolicyInitializer = initialSelectionPolicyInitializer;
    }

    public int getAmountFitnessInitializer() {
        return amountFitnessInitializer;
    }

    public void setAmountFitnessInitializer(int amountFitnessInitializer) {
        this.amountFitnessInitializer = amountFitnessInitializer;
    }

    public String getEvoFileName() {
        return evoFileName;
    }

    public void setEvoFileName(String evoFileName) {
        this.evoFileName = evoFileName;
    }

    public boolean isAsyncMigration() {
        return asyncMigration;
    }

    public void setAsyncMigration(boolean asyncMigration) {
        this.asyncMigration = asyncMigration;
    }

    public MigrationConfig generateMigrationConfig() {
        MigrationConfig migrationConfig = new MigrationConfig(this.numberOfIslands, this.globalPopulationSize,
                this.selectionPolicy, this.replacementPolicy, this.migrationRate, this.epochTerminationCriterion, this.epochTerminationEvaluation,
                this.epochTerminationFitness, this.epochTerminationGeneration, this.epochTerminationTime,
                this.epochTerminationGDV, this.epochTerminationGAK, this.globalTerminationCriterion, this.globalTerminationEpoch, this.globalTerminationEvaluation,
                this.globalTerminationFitness, this.globalTerminationGeneration, this.globalTerminationTime,
                this.globalTerminationGDV, this.globalTerminationGAK, this.asyncMigration);
        return migrationConfig;
    }

    public void readFromJson(String json) {
        Gson gson = new Gson();
        JobConfig jobConfig = gson.fromJson(json, JobConfig.class);
        BeanUtils.copyProperties(jobConfig, this);
        return;
    }

    public void readFromExistingJobConfig(JobConfig jobConfig) {
        BeanUtils.copyProperties(jobConfig, this);
        return;
    }

    @Override
    public String toString() {
        String jobConfigStr = "";
        jobConfigStr = jobConfigStr + "NumberOfIslands: " + numberOfIslands + ", ";
        jobConfigStr = jobConfigStr + "PopulationSize: " + globalPopulationSize + ", ";
        jobConfigStr = jobConfigStr + "Delay: " + delay + ", ";
        jobConfigStr = jobConfigStr + "MigrationRate: " + migrationRate +", ";
        jobConfigStr = jobConfigStr + "GlobalTerminationEpoch: "+globalTerminationEpoch+", ";
        jobConfigStr = jobConfigStr + "Topology: "+topology;
        return jobConfigStr;
    }
}