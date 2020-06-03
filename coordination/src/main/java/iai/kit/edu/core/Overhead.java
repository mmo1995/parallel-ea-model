package iai.kit.edu.core;

public class Overhead {
    private static long startIslandCreation = 0;
    private static long endIslandCreation= 0;
    private static long startPopulationCreation= 0;
    private static long endPopulationCreation= 0;
    private static long startSlaveCreation= 0;
    private static long endSlaveCreation= 0;
    private static long startEvolution = 0 ;
    private static long endEvolution = 0 ;
    private static long startInitializationOverhead = 0;
    private static long endInitializationOverhead = 0;

    public static long getStartEvolution() {
        return startEvolution;
    }

    public static void setStartEvolution(long startEvolution) {
        Overhead.startEvolution = startEvolution;
    }

    public static long getEndEvolution() {
        return endEvolution;
    }

    public static void setEndEvolution(long endEvolution) {
        Overhead.endEvolution = endEvolution;
    }

    public static long getStartSlaveCreation() {
        return startSlaveCreation;
    }

    public static void setStartSlaveCreation(long startSlaveCreation) {
        Overhead.startSlaveCreation = startSlaveCreation;
    }

    public static long getEndSlaveCreation() {
        return endSlaveCreation;
    }

    public static void setEndSlaveCreation(long endSlaveCreation) {
        Overhead.endSlaveCreation = endSlaveCreation;
    }

    public long getStartIslandCreation() {
        return startIslandCreation;
    }

    public void setStartIslandCreation(long startIslandCreation) {
        this.startIslandCreation = startIslandCreation;
    }

    public long getEndIslandCreation() {
        return endIslandCreation;
    }

    public void setEndIslandCreation(long endIslandCreation) {
        this.endIslandCreation = endIslandCreation;
    }

    public long getStartPopulationCreation() {
        return startPopulationCreation;
    }

    public void setStartPopulationCreation(long startPopulationCreation) {
        this.startPopulationCreation = startPopulationCreation;
    }

    public long getEndPopulationCreation() {
        return endPopulationCreation;
    }

    public void setEndPopulationCreation(long endPopulationCreation) {
        this.endPopulationCreation = endPopulationCreation;
    }
}
