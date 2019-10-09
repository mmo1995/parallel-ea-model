package iai.kit.edu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Stores configuration for one optimization task for GLEAM
 */
public class GLEAMConfig extends AlgorithmConfig {

    private List<String> fileNames;
    private List<String> files;

    private String workspacePath;

    private Logger logger;

    public GLEAMConfig(String workspacePath, int delay) {
        this.workspacePath = workspacePath;
        this.setDelay(delay);
    }

    private static class GleamFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File directory, String fileName) {
            if (fileName.contains(ConstantStrings.fileEndingAks) || fileName.contains(ConstantStrings.fileEndingMem) || fileName.contains(ConstantStrings.fileNamePartlog) || fileName.contains(ConstantStrings.fileNamePartLog)) {
                return false;
            }
            return true;
        }
    }

    private void storeFileNames(File[] fileArray) {
        this.fileNames = new ArrayList<>();
        for (File file : fileArray) {
            fileNames.add(file.getName());
        }
    }

    public void readFiles() {
        File gleamWorkspace = new File(workspacePath);
        File[] fileArray = gleamWorkspace.listFiles(new GleamFileFilter());
        this.storeFileNames(fileArray);
        files = new ArrayList<>();
        for (String fileName : this.fileNames) {
            StringBuilder contentBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines(Paths.get(workspacePath + fileName), StandardCharsets.ISO_8859_1)) {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            } catch (IOException e) {
                logger = LoggerFactory.getLogger(this.getClass());
                logger.error(e.getMessage());
            }
            files.add(contentBuilder.toString());
        }
    }


    public void writeFiles() {
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            File path = new File(workspacePath);
            File file = new File(workspacePath + fileName);
            if (!path.exists()) {
                path.mkdirs();
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    file.delete();
                    file.createNewFile();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(files.get(i));
                bufferedWriter.flush();
            } catch (IOException e) {
                logger = LoggerFactory.getLogger(this.getClass());
                logger.error(e.getMessage());

            }
        }
    }
}