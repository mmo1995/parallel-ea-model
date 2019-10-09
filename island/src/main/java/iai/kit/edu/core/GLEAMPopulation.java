package iai.kit.edu.core;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds one GLEAM specific population
 */
public class GLEAMPopulation extends Population {
    public void read(File file) {
        chromosomeList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        String line;
        try {
            line = bufferedReader.readLine();
            String chromosomeString = new String();
            while (line != "" && line != null) {
                if (line.endsWith(ConstantStrings.chromosomeEnding)) {
                    chromosomeString = chromosomeString + line;
                    chromosomeList.add(new GLEAMChromosome().fromString(chromosomeString));
                    chromosomeString = new String();
                } else {
                    chromosomeString = chromosomeString + line + "\n";
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    public void write(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (Chromosome chromosome : chromosomeList) {
                bufferedWriter.write(chromosome.toFileRepresentation() + "\n");
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void readFromJSON(String initialPopulationJson) {
        Gson gson = new Gson();
        List<String> initialPopulation = gson.fromJson(initialPopulationJson, List.class);
        for (String chromosomeString : initialPopulation) {
            this.chromosomeList.add(new GLEAMChromosome().fromString(chromosomeString));
        }
    }

    @Override
    public void writeInitialPopulation() {
        File path = new File(ConstantStrings.islandPath);
        File file = new File(ConstantStrings.islandPath + ConstantStrings.initialPopulationFileName);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        write(file);
    }

}
