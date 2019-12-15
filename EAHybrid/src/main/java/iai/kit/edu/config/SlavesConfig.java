package iai.kit.edu.config;

public class SlavesConfig {


    private boolean allSlavesInitialized = false;

    private boolean allSlavesReady = false;

    private int numberOfSlaves = 0;

    public boolean isAllSlavesInitialized() {
        return allSlavesInitialized;
    }

    public void setAllSlavesInitialized(boolean allSlavesInitialized) {
        this.allSlavesInitialized = allSlavesInitialized;
    }

    public boolean isAllSlavesReady() {
        return allSlavesReady;
    }

    public void setAllSlavesReady(boolean allSlavesReady) {
        this.allSlavesReady = allSlavesReady;
    }


    public int getNumberOfSlaves() {
        return numberOfSlaves;
    }

    public void setNumberOfSlaves(int numberOfSlaves) {
        this.numberOfSlaves = numberOfSlaves;
    }


}
