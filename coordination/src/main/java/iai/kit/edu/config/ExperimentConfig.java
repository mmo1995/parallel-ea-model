package iai.kit.edu.config;


/**
 * Stores configuration for several optimization tasks
 */

public class ExperimentConfig {

    private int[] populationSize;
    private int[] numberOfIslands;
    private int[] numberOfSlaves;
    private int [] numberOfGeneration;
    private int[] migrationRate;
    private int[] delay;
    private int[] demeSize;
    private String [] topology;


    String[] acceptRuleForOffspring = {"localLeast-ES"};
    double[] rankingParameter = {1.45};

    private String epochTerminationCriterion;
    private int epochTerminationEvaluation;
    private double epochTerminationFitness;
    private int epochTerminationGeneration;

    private String globalTerminationCriterion;
    private int globalTerminationEpoch;
    private int globalTerminationEvaluation;
    private double globalTerminationFitness;
    private int globalTerminationGeneration;



    public void setEpochTerminationGeneration(int epochTerminationTime) {
        this.epochTerminationGeneration = epochTerminationTime;
    }

    public int getEpochTerminationGeneration() {
        return epochTerminationGeneration;
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

    public String[] getTopology() {
        return topology;
    }

    public void setTopology(String[] topology) {
        this.topology = topology;
    }

    public int[] getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int[] populationSize) {
        this.populationSize = populationSize;
    }

    public int[] getNumberOfGeneration() {
        return numberOfGeneration;
    }

    public void setNumberOfGeneration(int[] numberOfGeneration) {
        this.numberOfGeneration = numberOfGeneration;
    }

    public int[] getNumberOfIslands() {
        return numberOfIslands;
    }

    public void setNumberOfIslands(int[] numberOfIslands) {
        this.numberOfIslands = numberOfIslands;
    }

    public int[] getNumberOfSlaves() {
        return numberOfSlaves;
    }

    public void setNumberOfSlaves(int[] numberOfSlaves) {
        this.numberOfSlaves = numberOfSlaves;
    }

    public int[] getMigrationRate() {
        return migrationRate;
    }

    public void setMigrationRate(int[] migrationRate) {
        this.migrationRate = migrationRate;
    }

    public int[] getDelay() {
        return delay;
    }

    public void setDelay(int[] delay) {
        this.delay = delay;
    }


    public int[] getDemeSize() {
        return demeSize;
    }

    public void setDemeSize(int[] demeSize) {
        this.demeSize = demeSize;
    }


    public String[] getAcceptRuleForOffspring() {
        return acceptRuleForOffspring;
    }

    public void setAcceptRuleForOffspring(String[] acceptRuleForOffspring) {
        this.acceptRuleForOffspring = acceptRuleForOffspring;
    }

    public double[] getRankingParameter() {
        return rankingParameter;
    }

    public void setRankingParameter(double[] rankingParameter) {
        this.rankingParameter = rankingParameter;
    }
}