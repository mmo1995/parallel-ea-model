package iai.kit.edu.algorithm;

/**
 * Generic interface to interact with a deployed EA
 */
public interface AlgorithmStarter {

    public void setIslandNumber(int islandNumber);
    public void setPopulationSize(int populationSize);
    public void setAmountFitness(int amountFitness);
    public void setTerminationEvaluation(int terminationEvaluation);
    public void setTerminationFitness(double terminationFitness);
    public void setTerminationGeneration(int terminationGeneration);
    public void setTerminationTime(int terminationTime);
    public void setDelay(int delay);
    public void setDemeSize(int DemeSize);
    public void setAcceptanceRuleForOffspring(String acceptanceRuleForOffspring);
    public void setRankingParameter(double rankingParameter);
    public void setInitStrategy(String initStrategy);
    public void start();
    public void reset();
    public void stop();
    public boolean isStopeped();
    public void setStopeped(boolean stopeped);
    public boolean isFirstEpoch();
    public void setFirstEpoch(boolean firstEpoch);
}
