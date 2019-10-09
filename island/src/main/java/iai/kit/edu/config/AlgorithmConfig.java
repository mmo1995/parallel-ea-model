package iai.kit.edu.config;

/**
 * Interface to interact with a deployed EA configuration
 */
public abstract class AlgorithmConfig {
    private int delay;

    public abstract void readFiles();

    public abstract void writeFiles();

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }
}
