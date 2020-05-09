package iai.kit.edu.config;

/**
 * Stores application relevant Strings
 */
public class ConstantStrings {

    public static final String initializerPath = "./gleam/testfeld/initializer/";

     /*public static final String containerManagementURL = "http://localhost:8073";
    public static final String splittingJoiningURL = "http://localhost:8074";
    public static final String initializerEAURL = "http://localhost:8075";*/

     public static final String containerManagementURL = "http://container-management-hybrid:8073";
    public static final String splittingJoiningURL = "http://splitting-joining-hybrid:8074";
    public static final String initializerEAURL = "http://initializer-hybrid:8075";


    public static final String fileEndingAks = ".aks";
    public static final String fileEndingMem = ".mem";
    public static final String fileNamePartlog = "log";
    public static final String fileNamePartLog = "Log";

    public static final String gleamConfigurationsGeneration = "proof.gleam.generation";

    public static final String islandMigrantsReceivedCounter = "proof.island.migrants.received.counter";
    public static final String islandsWithPopulationCounter = "proof.islands.population.initialized";
    public static final String islandsWithSubscribedNeighborsCounter = "proof.islands.neighbors.subscribed";
    public static final String initializedIslandCounter = "proof.islands.initialized";
    public static final String receivedResultsCounter = "proof.results";

    public static final String intermediatePopulation = "proof.population.intermediate";

    public static final String numberOfSlavesTopic = "proof.slaves.amount";
    public static final String numberOfIslands = "proof.island.amount";
    public static final String numberOfGenerationForOneIsland = "proof.island.generation.amount";
    public static final String receivedSlavesResultsCounter = "proof.results.slaves";
    public static final String islandsWithReadySlavesCounter = "proof.islands.slaves.ready";


    public static final String topologyRing = "ring";
    public static final String topologyBiRing = "biRing";
    public static final String topologyLadder = "ladder";
    public static final String topologyComplete = "complete";

}