package iai.kit.edu.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds one generic population as a list of individuals (chromosomes)
 */
public abstract class Population {
    protected List<Chromosome> chromosomeList = new ArrayList<>();

    public List<String> getResult() {
        List<String> result=new ArrayList<>();
        for(Chromosome chromosome:chromosomeList){
            result.add(chromosome.toFileRepresentation());
        }
        return result;
    }

    public int size() {
        return chromosomeList.size();
    }

    public Chromosome remove(int index) {
        return this.chromosomeList.remove(index);
    }

    public Chromosome get(int index) {
        return this.chromosomeList.get(index);
    }

    public void add(Chromosome chromosome) {
        this.chromosomeList.add(chromosome);
    }

    public void replace(int index, Chromosome chromosome) {
        chromosomeList.remove(index);
        chromosomeList.add(index, chromosome);
    }

    public void sort() {
        Collections.sort(chromosomeList);
    }

    public boolean isFitnessLimitReached(double fitnessLimit) {
        return this.chromosomeList.stream().anyMatch(chromosome -> chromosome.getRating() >= fitnessLimit);
    }

    public void clear() {
        chromosomeList.clear();
    }

    public void addAll(Population population) {
        this.chromosomeList.addAll(population.chromosomeList);
    }

    public abstract void read(File file);

    public abstract void write(File file);

    public abstract void readFromJSON(String JSON);

    public abstract void writeInitialPopulation();

}
