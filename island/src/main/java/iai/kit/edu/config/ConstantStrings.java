package iai.kit.edu.config;
public class ConstantStrings {

     public static final String coordinationURL = "http://localhost:8071";
    public static final String splittingJoiningURL = "http://localhost:8074";
    /*public static final String coordinationURL = "http://coordination-hybrid:8071";
    public static final String splittingJoiningURL = "http://splitting-joining-hybrid:8074";*/


    public static final String islandPath = "./gleam/testfeld/island/";
    public static final String initializerPath = "./gleam/testfeld/initializer/";

    public static final String initialPopulationFileName = "starter.mem";

    public static final String fileEndingAks = ".aks";
    public static final String fileEndingMem = ".mem";
    public static final String fileNamePartlog = "log";
    public static final String fileNamePartLog = "Log";

    public static final String islandMigrantsReceivedCounter = "proof.island.migrants.received.counter";
    public static final String islandsWithPopulationCounter = "proof.islands.population.initialized";
    public static final String islandsWithSubscribedNeighborsCounter = "proof.islands.neighbors.subscribed";
    public static final String initializedIslandCounter = "proof.islands.initialized";
    public static final String receivedResultsCounter = "proof.results";
    public static final String readyIslandCounter = "proof.islands.ready";
    public static final String numberOfIslands = "proof.island.amount";
    public static final String managementConfig = "proof.management.config";
    public static final String managementStop = "proof.management.stop";
    public static final String managementStart = "proof.management.start";
    public static final String statusInitialized = "proof.island.status.initialized";
    public static final String statusReady = "proof.island.status.ready";
    public static final String statusMigrationCompleted = "proof.island.status.migration.completed";
    public static final String managementConfigMigration = "proof.management.config.migration";
    public static final String managementConfigAlgorithm = "proof.management.config.algorithm";
    public static final String managementConfigNeighbor = "proof.management.config.neighbors";
    public static final String initializeIslands = "proof.management.initialize.islands";
    public static final String initialPopulation = "proof.population.initial";
    public static final String neighborPopulation = "proof.population.neighbor";
    public static final String intermediatePopulation = "proof.population.intermediate";
    public static final String resultPopulation = "proof.result";
    public static final String eaReady = "proof.ea.status.ready";

    public static final String neighborsSubscribed = "Neighbors subscribed";
    public static final String populationReady = "Population ready";
    public static final String slavesReady = "Slaves ready";

    public static final String topologyRing = "ring";
    public static final String topologyBiRing = "biRing";
    public static final String topologyLadder = "ladder";
    public static final String topologyComplete = "complete";

    public static final String chromosomeEnding = "10000";

    public static final String initStrategyBest = "best";
    public static final String initStrategyNew = "new";
    public static final String initStrategyBestNew = "bestNew";
    public static final String initStrategyMix = "mix";

    public static final String randomSelection = "random";
    public static final String bestSelection = "best";

    public static final String randomReplacement = "random";
    public static final String worstReplacement = "worst";

    public static final String terminationEvaluation = "evaluation";
    public static final String terminationFitness = "fitness";
    public static final String terminationGeneration = "generation";
    public static final String terminationTime = "time";

    public static final String epochTopic = "algorithm.EA.epoch";

    public static final String EAConfig = "algorith.EA.config";
}