package iai.kit.edu.config;

import org.springframework.beans.factory.annotation.Value;

public class ConstantStrings {

    /**
     * Stores application relevant Strings
     */
    public static final String islandPath = "./gleam-esso/testfeld/island/";

    public static final String initialPopulationCalculatedFileName = "initialChromosomeSet.aks";
    public static final String initialPopulationFileName = "starter.mem";
    public static final String intermediatePopulationFileName = "evo_tmp.mem";
    public static final String stopFileName = "evo_stop.tmp";
    public static final String gleamTerminalLogFileName = "gleamCommandLineLog";
    public static final String expFile = "experiment.exp";
    public static final String logFile = "logfile";

    public static final String fileEndingAks = ".aks";
    public static final String fileEndingMem = ".mem";
    public static final String fileNamePartlog = "log";
    public static final String fileNamePartLog = "Log";

    public static final String workingDirectoryString = "user.dir";
    public static final String gleamCLV = "/gleam-esso/gleamParPopEssO_CLV";

    public static final String gleamCLVIslandNumber = "-n";
    public static final String gleamCLVPopulationSize = "-P";
    public static final String gleamCLVLoggingPlus = "-p+";
    public static final String gleamCLVLoggingMinus = "-p-";
    public static final String gleamCLVInitStrategyBest = "-Ibest";
    public static final String gleamCLVInitiStrategyFile = "-Ifile";
    public static final String gleamCLVInitStrategyMix = "-Imix";
    public static final String gleamCLVInitStrategyBestNew = "-IbestNeu";
    public static final String gleamCLVInitStrategyNew = "-Inew";
    public static final String gleamCLVInitializationParameter = "-i";
    public static final String gleamCLVTerminationEvaluation = "-E";
    public static final String gleamCLVTerminationFitness = "-F";
    public static final String gleamCLVTerminationGeneration = "-G";
    public static final String gleamCLVTerminationTime = "-T";
    public static final String gleamDelay = "-x";

    public static final String intermediatePopulation = "proof.population.intermediate";
    public static final String numberOfSlaves = "proof.slaves.amount";
    public static final String numberOfSlavesInitialized = "proof.slaves.initialized";
    public static final String numberOfSlavesReady = "proof.slaves.ready";
    public static final String eaReady = "proof.ea.status.ready";
    public static final String slaveInitialized = "proof.slave.status.initialized";
    public static final String slaveReady = "proof.slave.status.ready";
    
    public static final String chromosomeEnding = "10000";

    public static final String terminationEvaluation = "evaluation";
    public static final String terminationFitness = "fitness";
    public static final String terminationGeneration = "generation";
    public static final String terminationTime = "time";

    public static final String epochTopic = "algorithm.EA.epoch";
    

    public static final String EAConfig = "algorith.EA.config";
}