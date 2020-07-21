package iai.kit.edu;

public class ExperimentConfig {

    private int[] populationSize;
    private int[] numberOfIslands;
    private int[] numberOfSlaves;
    private int [] numberOfGeneration;
    private int[] migrationRate;
    private int[] delay;
    private String [] topology;
    private  String date;



    private int[] demeSize;

    private String[] initialSelectionPolicy;
    private int[] amountFitness;

    private String[] initialSelectionPolicyInitializer;
    private int[] amountFitnessInitializer;

    private boolean[] asyncMigration;


    String[] acceptRuleForOffspring;
    double[] rankingParameter;
    double[] minimalHammingDistance = {0.1};
    String[] evoFileName = {"lsk_stnd.evo"};


    public String epochTerminationCriterion;
    public int epochTerminationEvaluation;
    public double epochTerminationFitness;
    private int epochTerminationGeneration;

    private String globalTerminationCriterion;
    private int globalTerminationEpoch;
    private int globalTerminationEvaluation;
    private double globalTerminationFitness;
    private int globalTerminationGeneration;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    public int[] getNumberOfGeneration() {
        return numberOfGeneration;
    }

    public void setNumberOfGeneration(int[] numberOfGeneration) {
        this.numberOfGeneration = numberOfGeneration;
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

    public void setEpochTerminationGeneration(int epochTerminationTime) {
        this.epochTerminationGeneration = epochTerminationTime;
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

    public int getEpochTerminationGeneration() {
        return epochTerminationGeneration;
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

    public double[] getMinimalHammingDistance() {
        return minimalHammingDistance;
    }

    public void setMinimalHammingDistance(double[] minimalHammingDistance) {
        this.minimalHammingDistance = minimalHammingDistance;
    }

    public String[] getInitialSelectionPolicy() {
        return initialSelectionPolicy;
    }

    public void setInitialSelectionPolicy(String[] initialSelectionPolicy) {
        this.initialSelectionPolicy = initialSelectionPolicy;
    }

    public int[] getAmountFitness() {
        return amountFitness;
    }

    public void setAmountFitness(int[] amountFitness) {
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


    public boolean[] getAsyncMigration() {
        return asyncMigration;
    }

    public void setAsyncMigration(boolean[] asyncMigration) {
        this.asyncMigration = asyncMigration;
    }

    public String[] getEvoFileName() {
        return evoFileName;
    }

    public void setEvoFileName(String[] evoFileName) {
        this.evoFileName = evoFileName;
    }
}
