package iai.kit.edu.config;

/**
 * Reads and writes general config related to a deployed EA
 */
public abstract class AlgorithmConfig {
    private int delay;

    private int demeSize;

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
}
