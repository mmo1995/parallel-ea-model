package iai.kit.edu.core;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a GLEAM specific population that is read and written from/to files
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
                    chromosomeList.add(new Chromosome().fromString(chromosomeString));
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


    public void writeInitialPopulation(File file) {

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
