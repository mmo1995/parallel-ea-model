package iai.kit.edu.config;

/**
 * Stores migration-related information
 */
public class MigrationConfig {

    private int numberOfIslands;
    private int globalPopulationSize;
    private String selectionPolicy;
    private String replacementPolicy;
    private int migrationRate;
    private String epochTerminationCriterion;
    private int epochTerminationEvaluation;
    private double epochTerminationFitness;
    private int epochTerminationGeneration;
    private int epochTerminationTime;
    private int epochTerminationGDV;
    private int epochTerminationGAK;
    private String globalTerminationCriterion;
    private int globalTerminationEpoch;
    private int globalTerminationEvaluation;
    private double globalTerminationFitness;
    private int globalTerminationGeneration;
    private int globalTerminationTime;
    private int globalTerminationGDV;
    private int globalTerminationGAK;
    public MigrationConfig(int numberOfIslands, int globalPopulationSize, String selectionPolicy, String replacementPolicy, int migrationRate, String epochTerminationCriterion, int epochTerminationEvaluation, double epochTerminationFitness, int epochTerminationGeneration, int epochTerminationTime, int epochTerminationGDV, int epochTerminationGAK, String globalTerminationCriterion, int globalTerminationEpoch, int globalTerminationEvaluation, double globalTerminationFitness, int globalTerminationGeneration, int globalTerminationTime, int globalTerminationGDV, int globalTerminationGAK) {
        this.numberOfIslands = numberOfIslands;
        this.globalPopulationSize=globalPopulationSize;
        this.selectionPolicy = selectionPolicy;
        this.replacementPolicy = replacementPolicy;
        this.migrationRate = migrationRate;
        this.epochTerminationCriterion = epochTerminationCriterion;
        this.epochTerminationEvaluation = epochTerminationEvaluation;
        this.epochTerminationFitness = epochTerminationFitness;
        this.epochTerminationGeneration = epochTerminationGeneration;
        this.epochTerminationTime = epochTerminationTime;
        this.epochTerminationGDV = epochTerminationGDV;
        this.epochTerminationGAK = epochTerminationGAK;
        this.globalTerminationCriterion = globalTerminationCriterion;
        this.globalTerminationEpoch = globalTerminationEpoch;
        this.globalTerminationEvaluation = globalTerminationEvaluation;
        this.globalTerminationFitness = globalTerminationFitness;
        this.globalTerminationGeneration = globalTerminationGeneration;
        this.globalTerminationTime = globalTerminationTime;
        this.globalTerminationGDV = globalTerminationGDV;
        this.globalTerminationGAK = globalTerminationGAK;
    }

    public int getNumberOfIslands() {
        return numberOfIslands;
    }

    public int getGlobalPopulationSize() {
        return globalPopulationSize;
    }

    public String getSelectionPolicy() {
        return selectionPolicy;
    }

    public String getReplacementPolicy() {
        return replacementPolicy;
    }

    public int getMigrationRate() {
        return migrationRate;
    }

    public int getEpochTerminationEvaluation() {
        return epochTerminationEvaluation;
    }

    public double getEpochTerminationFitness() {
        return epochTerminationFitness;
    }

    public int getEpochTerminationGeneration() {
        return epochTerminationGeneration;
    }

    public int getEpochTerminationTime() {
        return epochTerminationTime;
    }

    public int getEpochTerminationGDV() {
        return epochTerminationGDV;
    }

    public int getEpochTerminationGAK() {
        return epochTerminationGAK;
    }

    public int getGlobalTerminationEpoch(){return globalTerminationEpoch;}

    public int getGlobalTerminationEvaluation() {
        return globalTerminationEvaluation;
    }

    public double getGlobalTerminationFitness() {
        return globalTerminationFitness;
    }

    public int getGlobalTerminationGeneration() {
        return globalTerminationGeneration;
    }

    public int getGlobalTerminationTime() {
        return globalTerminationTime;
    }

    public int getGlobalTerminationGDV() {
        return globalTerminationGDV;
    }

    public int getGlobalTerminationGAK() {
        return globalTerminationGAK;
    }

    public String getEpochTerminationCriterion() {
        return epochTerminationCriterion;
    }

    public String getGlobalTerminationCriterion() {
        return globalTerminationCriterion;
    }
}


