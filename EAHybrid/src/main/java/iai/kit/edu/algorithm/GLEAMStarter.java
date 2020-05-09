package iai.kit.edu.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import iai.kit.edu.config.ConstantStrings;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains GLEAM specific parts to run GLEAM
 */
public class GLEAMStarter implements AlgorithmStarter {
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
    private String islandNumber;
    private String populationSize;
    private String logging;
    private List<String> initStrategy;
    private String terminationCriterion = "";
    private double amountFitness; //contains initialization parameter, casted to int, if amount is needed
    private String delay;

    private List<String> commands;



    private boolean firstEpoch = true;
    private boolean isStopeped = false;

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

    public void setAmountFitness(double amountFitness) {
        this.amountFitness = amountFitness;
    }

    public void setTerminationEvaluation(int terminationEvaluation) {
        this.terminationCriterion += ConstantStrings.gleamCLVTerminationEvaluation + terminationEvaluation;
    }

    public void setTerminationFitness(double terminationFitness) {
        this.terminationCriterion += ConstantStrings.gleamCLVTerminationFitness + terminationFitness;
    }

    public void setTerminationGeneration(int terminationGeneration) {
        this.terminationCriterion += ConstantStrings.gleamCLVTerminationGeneration + terminationGeneration;
    }

    public void setTerminationTime(int terminationTime) {
        this.terminationCriterion += ConstantStrings.gleamCLVTerminationTime + terminationTime;
    }

    public void setDelay(int delay) {
        this.delay = ConstantStrings.gleamDelay + delay;
    }



    public void start() {
        if (firstEpoch) {
            prepareFirstEpoch();
            firstEpoch = false;
        } else {
            prepareNthEpoch();
        }
        if(!isStopeped) {
            runGLEAM();
        }
    }


    private void prepareFirstEpoch() {
        logging = ConstantStrings.gleamCLVLoggingPlus;
        this.initStrategy = new ArrayList<>();
        this.initStrategy.add(ConstantStrings.gleamCLVInitStrategyBest);
        addCommandsForEpochRun();
    }

    private void prepareNthEpoch() {
        logging = ConstantStrings.gleamCLVLoggingPlus;
        this.initStrategy = new ArrayList<>();
        this.initStrategy.add(ConstantStrings.gleamCLVInitiStrategyFile);
        addCommandsForEpochRun();
    }

    private void addCommandsForEpochRun() {
        commands = new ArrayList<>();
        commands.add(gleamExe);
        commands.add(expFile);
        commands.add(logFile);
        commands.add(islandNumber);
        commands.add(populationSize);
        commands.addAll(initStrategy);
        commands.add(logging);
        commands.add(terminationCriterion);
        commands.add(delay);
    }


    private void runGLEAM() {
        ProcessBuilder pb;
        pb = new ProcessBuilder(commands);

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

    public void reset() {
       // this.firstEpoch = true;
        this.deleteStopFile();
        this.terminationCriterion="";
    }

    private void deleteStopFile() {
        File file = new File(ConstantStrings.islandPath + ConstantStrings.stopFileName);
        logger.debug("delete stop file for next epoch");
        file.delete();
    }

    public void stop() {
        File file = new File(ConstantStrings.islandPath + ConstantStrings.stopFileName);
        if (file.exists()) {
            logger.info("island already stopped");
        } else {
            try {
                file.createNewFile();
                logger.info("island is stopped by file");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public boolean isStopeped() {
        return isStopeped;
    }

    public void setStopeped(boolean stopeped) {
        isStopeped = stopeped;
    }
}
