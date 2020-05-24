package iai.kit.edu.config;


import iai.kit.edu.core.GLEAMPopulation;

/**
 * Contains configuration parameters for one epoch
 */
public class EAEpochConfig {

    private String epochTerminationCriterion;
    private int epochTerminationEvaluation;
    private double epochTerminationFitness;
    private int epochTerminationGeneration;
    private int epochTerminationTime;
    private int populationSize;
    private int delay;
    private int demeSize;
    private String acceptanceRuleForOffspring;
    private double rankingParameter;
    private String initStrategy;
    private int amountFitness;
    private GLEAMPopulation population;


    public GLEAMPopulation getPopulation() {
        return population;
    }

    public void setPopulation(GLEAMPopulation population) {
        this.population = population;
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

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }


    public int getDemeSize() {
        return demeSize;
    }

    public void setDemeSize(int demeSize) {
        this.demeSize = demeSize;
    }

    public String getAcceptanceRuleForOffspring() {
        return acceptanceRuleForOffspring;
    }

    public void setAcceptanceRuleForOffspring(String acceptanceRuleForOffspring) {
        this.acceptanceRuleForOffspring = acceptanceRuleForOffspring;
    }

    public double getRankingParameter() {
        return rankingParameter;
    }

    public void setRankingParameter(double rankingParameter) {
        this.rankingParameter = rankingParameter;
    }

    public String getInitStrategy() {
        return initStrategy;
    }

    public void setInitStrategy(String initStrategy) {
        this.initStrategy = initStrategy;
    }

    public int getAmountFitness() {
        return amountFitness;
    }

    public void setAmountFitness(int amountFitness) {
        this.amountFitness = amountFitness;
    }
}

