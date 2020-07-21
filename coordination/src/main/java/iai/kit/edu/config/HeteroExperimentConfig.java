package iai.kit.edu.config;

public class HeteroExperimentConfig {

    private int[] globalPopulationSize;
    private int[] numberOfIslands;
    private int[] numberOfSlaves;
    private int[][] numberOfGeneration;
    private int[][] migrationRate;
    private String[] topology;
    private String[][] initialSelectionPolicy;
    private int[][] amountFitness;
    private String[] initialSelectionPolicyInitializer;
    private int[] amountFitnessInitializer;
    private String[][] selectionPolicy;
    private String[][] replacementPolicy;
    private int[][] demeSize;

    private boolean[] asyncMigration;

    String[][] acceptRuleForOffspring;
    double[][] rankingParameter;
    double[][] minimalHammingDistance;
    String[][] evoFileName;

    private String[][] epochTerminationCriterion;
    private int[][] epochTerminationEvaluation;
    private double[][] epochTerminationFitness;
    private int[][] epochTerminationGeneration;
    private int[][] epochTerminationTime;
    private int[][] epochTerminationGDV;
    private int[][] epochTerminationGAK;

    private String globalTerminationCriterion;
    private int globalTerminationEpoch;
    private int globalTerminationEvaluation;
    private double globalTerminationFitness;
    private int globalTerminationGeneration;
    private int globalTerminationTime;
    private int globalTerminationGDV;
    private int globalTerminationGAK;

    private int[] delay;

    public int[] getGlobalPopulationSize() {
        return globalPopulationSize;
    }

    public void setGlobalPopulationSize(int[] globalPopulationSize) {
        this.globalPopulationSize = globalPopulationSize;
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

    public int[][] getNumberOfGeneration() {
        return numberOfGeneration;
    }

    public void setNumberOfGeneration(int[][] numberOfGeneration) {
        this.numberOfGeneration = numberOfGeneration;
    }

    public int[][] getMigrationRate() {
        return migrationRate;
    }

    public void setMigrationRate(int[][] migrationRate) {
        this.migrationRate = migrationRate;
    }

    public String[] getTopology() {
        return topology;
    }

    public void setTopology(String[] topology) {
        this.topology = topology;
    }

    public String[][] getInitialSelectionPolicy() {
        return initialSelectionPolicy;
    }

    public void setInitialSelectionPolicy(String[][] initialSelectionPolicy) {
        this.initialSelectionPolicy = initialSelectionPolicy;
    }

    public int[][] getAmountFitness() {
        return amountFitness;
    }

    public void setAmountFitness(int[][] amountFitness) {
        this.amountFitness = amountFitness;
    }

    public String[] getInitialSelectionPolicyInitializer() {
        return initialSelectionPolicyInitializer;
    }

    public void setInitialSelectionPolicyInitializer(String[] initialSelectionPolicyInitializer) {
        this.initialSelectionPolicyInitializer = initialSelectionPolicyInitializer;
    }

    public int[] getAmountFitnessInitializer() {
        return amountFitnessInitializer;
    }

    public void setAmountFitnessInitializer(int[] amountFitnessInitializer) {
        this.amountFitnessInitializer = amountFitnessInitializer;
    }

    public String[][] getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(String[][] selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public String[][] getReplacementPolicy() {
        return replacementPolicy;
    }

    public void setReplacementPolicy(String[][] replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }

    public int[][] getDemeSize() {
        return demeSize;
    }

    public void setDemeSize(int[][] demeSize) {
        this.demeSize = demeSize;
    }

    public boolean[] getAsyncMigration() {
        return asyncMigration;
    }

    public void setAsyncMigration(boolean[] asyncMigration) {
        this.asyncMigration = asyncMigration;
    }

    public String[][] getAcceptRuleForOffspring() {
        return acceptRuleForOffspring;
    }

    public void setAcceptRuleForOffspring(String[][] acceptRuleForOffspring) {
        this.acceptRuleForOffspring = acceptRuleForOffspring;
    }

    public double[][] getRankingParameter() {
        return rankingParameter;
    }

    public void setRankingParameter(double[][] rankingParameter) {
        this.rankingParameter = rankingParameter;
    }

    public double[][] getMinimalHammingDistance() {
        return minimalHammingDistance;
    }

    public void setMinimalHammingDistance(double[][] minimalHammingDistance) {
        this.minimalHammingDistance = minimalHammingDistance;
    }

    public String[][] getEpochTerminationCriterion() {
        return epochTerminationCriterion;
    }

    public void setEpochTerminationCriterion(String[][] epochTerminationCriterion) {
        this.epochTerminationCriterion = epochTerminationCriterion;
    }

    public int[][] getEpochTerminationEvaluation() {
        return epochTerminationEvaluation;
    }

    public void setEpochTerminationEvaluation(int[][] epochTerminationEvaluation) {
        this.epochTerminationEvaluation = epochTerminationEvaluation;
    }

    public double[][] getEpochTerminationFitness() {
        return epochTerminationFitness;
    }

    public void setEpochTerminationFitness(double[][] epochTerminationFitness) {
        this.epochTerminationFitness = epochTerminationFitness;
    }

    public int[][] getEpochTerminationGeneration() {
        return epochTerminationGeneration;
    }

    public void setEpochTerminationGeneration(int[][] epochTerminationGeneration) {
        this.epochTerminationGeneration = epochTerminationGeneration;
    }

    public int[][] getEpochTerminationTime() {
        return epochTerminationTime;
    }

    public void setEpochTerminationTime(int[][] epochTerminationTime) {
        this.epochTerminationTime = epochTerminationTime;
    }

    public int[][] getEpochTerminationGDV() {
        return epochTerminationGDV;
    }

    public void setEpochTerminationGDV(int[][] epochTerminationGDV) {
        this.epochTerminationGDV = epochTerminationGDV;
    }

    public int[][] getEpochTerminationGAK() {
        return epochTerminationGAK;
    }

    public void setEpochTerminationGAK(int[][] epochTerminationGAK) {
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

    public int[] getDelay() {
        return delay;
    }

    public void setDelay(int[] delay) {
        this.delay = delay;
    }

    public String[][] getEvoFileName() {
        return evoFileName;
    }

    public void setEvoFileName(String[][] evoFileName) {
        this.evoFileName = evoFileName;
    }
}
