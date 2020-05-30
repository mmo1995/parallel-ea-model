package edu.kit.iai.gleam.config;

/**
 * Stores application relevant Strings
 */
public class ConstantStrings {

    public static final String coordinationURL = "http://localhost:8071";
      public static final String starterURL = "http://localhost:8090";

     /*public static final String coordinationURL = "http://coordination-hybrid:8071";
     public static final String starterURL = "http://starter-hybrid:8090";*/


    public static final String islandsWithPopulationCounter = "proof.islands.population.initialized";
    public static final String islandsWithSubscribedNeighborsCounter = "proof.islands.neighbors.subscribed";
    public static final String islandsWithReadySlavesCounter = "proof.islands.slaves.ready";
    public static final String receivedResultsCounter = "proof.results";
    public static final String receivedSlavesResultsCounter = "proof.results.slaves";
    public static final String numberOfGenerationForOneIsland = "proof.island.generation.amount";
    public static final String numberOfIslands = "proof.island.amount";
    public static final String managementConfig = "proof.management.config";
    public static final String managementHeteroConfig = "proof.management.hetero.config";
    public static final String managementStart = "proof.management.start";
    public static final String managementConfigMigration = "proof.management.config.migration";
    public static final String managementConfigAlgorithm = "proof.management.config.algorithm";
    public static final String managementConfigNeighbor = "proof.management.config.neighbors";
    public static final String initialPopulation = "proof.population.initial";
    public static final String resultPopulation = "proof.result";

    public static final String neighborsSubscribed = "Neighbors subscribed";
    public static final String populationReady = "Population ready";
    public static final String slavesReady = "Slaves ready";
    public static final String numberOfSlavesTopic = "proof.slaves.amount";
    public static final String slavePopulation = "proof.population.slave";

    public static final String gleamConfigurationsGeneration = "proof.gleam.generation"; // used to differ between best chromosome and others


}