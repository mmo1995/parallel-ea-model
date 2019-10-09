package iai.kit.edu.config;

/**
 * Stores configuration to initialize a population, sent to InitializerEAService
 */
public class InitializerEAConfig {
    private int populationSize;
    private double amountFitness;
    private String initStrategy;

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public double getAmountFitness() {
        return amountFitness;
    }

    public void setAmountFitness(double amountFitness) {
        this.amountFitness = amountFitness;
    }

    public String getInitStrategy() {
        return initStrategy;
    }

    public void setInitStrategy(String initStrategy) {
        this.initStrategy = initStrategy;
    }

}
