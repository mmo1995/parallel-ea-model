package iai.kit.edu.algorithm;

/**
 * Interface to deployed EAs
 */
public interface AlgorithmStarter {

    public void setIslandNumber(int islandNumber);
    public void setPopulationSize(int populationSize);
    public void setInitStrategy(String initStrategy);
    public void setAmountFitness(double amountFitness);
    public void init();
}
