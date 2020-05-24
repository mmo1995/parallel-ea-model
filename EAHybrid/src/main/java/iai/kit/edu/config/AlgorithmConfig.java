package iai.kit.edu.config;

public abstract class AlgorithmConfig {
    private int delay;


    private int deamSize;

    private String acceptanceRuleForOffspring;

    private double rankingParameter;

    private String initStrategy;

    private int amountFitness;

    public abstract void readFiles();

    public abstract void writeFiles();

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }


    public int getDeamSize() {
        return deamSize;
    }

    public void setDeamSize(int deamSize) {
        this.deamSize = deamSize;
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
