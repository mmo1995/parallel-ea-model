package iai.kit.edu.algorithm;

import iai.kit.edu.config.ConstantStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deployed EA specific functions to start GLEAM and initialize a population
 */
public class GLEAMStarter  implements AlgorithmStarter {
    @Autowired
    @Qualifier("populationFile")
    File populationFile;

    String gleamWorkspaceString;

    private File gleamWorkspace;
    private File gleamLog;
    private String workingDirectory = System.getProperty(ConstantStrings.workingDirectoryString);
    private String gleamExe = workingDirectory + ConstantStrings.gleamCLV;
    private String expFile = ConstantStrings.expFile;
    private String logFile = ConstantStrings.logFile;
    private String bestChromosomeLogFile = ConstantStrings.bestChromosomeLogFile;
    private String islandNumber;
    private String populationSize;
    private List<String> initStrategy;
    private double amountFitness; //contains initialization parameter, casted to int, if amount is needed

    private List<String> commands;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GLEAMStarter(String gleamWorkspaceString) {
        this.gleamWorkspaceString = gleamWorkspaceString;
        this.gleamWorkspace = new File(gleamWorkspaceString);
        this.gleamLog = new File(gleamWorkspaceString + ConstantStrings.gleamTerminalLogFileName);
    }

    public void setIslandNumber(int islandNumber) {
        this.islandNumber = ConstantStrings.gleamCLVIslandNumber + islandNumber;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = ConstantStrings.gleamCLVPopulationSize + populationSize;
    }


    public void setInitStrategy(String initStrategyString) {
        this.initStrategy = new ArrayList<>();
        if (initStrategyString.equals(ConstantStrings.initStrategyBest)) {
            this.initStrategy.add(ConstantStrings.gleamCLVInitStrategyBest);
        } else if (initStrategyString.equals(ConstantStrings.initStrategyMix)) {
            this.initStrategy.add(ConstantStrings.gleamCLVInitStrategyMix);
            this.initStrategy.add(ConstantStrings.gleamCLVInitializationParameter + amountFitness);
        } else if (initStrategyString.equals(ConstantStrings.initStrategyBestNew)) {
            this.initStrategy.add(ConstantStrings.gleamCLVInitStrategyBestNew);
            this.initStrategy.add(ConstantStrings.gleamCLVInitializationParameter + (int) amountFitness);
        } else {
            this.initStrategy.add(ConstantStrings.gleamCLVInitStrategyNew);
        }
    }

    public void setAmountFitness(double amountFitness) {
        this.amountFitness = amountFitness;
    }

    public void init() {
        addCommandsForInitializationRun();
        runGLEAM();

    }
    public void chooseBestChromosome() {
        addCommandsForChoosingBestChromosome();
        runGLEAM();

    }

    private void addCommandsForInitializationRun() {
        commands = new ArrayList<>();
        commands.add(gleamExe);
        commands.add(expFile);
        commands.add(logFile);
        commands.add(islandNumber);
        commands.add(populationSize);
        commands.addAll(initStrategy);
    }
    private void addCommandsForChoosingBestChromosome() {
        commands = new ArrayList<>();
        commands.add(gleamExe);
        commands.add(expFile);
        commands.add(bestChromosomeLogFile);
        commands.add(islandNumber);
        commands.addAll(initStrategy);
    }

    private void runGLEAM() {
        ProcessBuilder pb;
        System.out.println(commands.toString());
        pb = new ProcessBuilder(commands);
        System.out.println(gleamWorkspace.toString());
        pb = pb.directory(gleamWorkspace);
        if (!gleamLog.exists()) {
            try {
                gleamLog.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        pb.redirectOutput(gleamLog);
        Process process;

        try {
            logger.trace("command for GLEAM: " + commands);
            logger.info("starting GLEAM");
            process = pb.start();
            process.waitFor();
            logger.info("completing GLEAM");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}