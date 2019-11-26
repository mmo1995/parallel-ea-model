package iai.kit.edu.algorithm;

/**
 * Generic interface to interact with a deployed EA
 */
public interface AlgorithmStarter {

    public void setIslandNumber(int islandNumber);
    public void setPopulationSize(int populationSize);
    public void setAmountFitness(double amountFitness);
    public void setTerminationEvaluation(int terminationEvaluation);
    public void setTerminationFitness(double terminationFitness);
    public void setTerminationGeneration(int terminationGeneration);
    public void setTerminationTime(int terminationTime);
    public void setDelay(int delay);
    public void start();
    public void reset();
    public void stop();
}