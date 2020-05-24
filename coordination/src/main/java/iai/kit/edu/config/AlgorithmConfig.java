package iai.kit.edu.config;

/**
 * Reads and writes general config related to a deployed EA
 */
public abstract class AlgorithmConfig {
    private int delay;

    private int demeSize;

    private String acceptanceRuleForOffspring;

    private double rankingParameter;

    public abstract void readFiles();

    public abstract void writeFiles();

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
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
}
