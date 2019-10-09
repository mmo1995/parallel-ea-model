package iai.kit.edu.core;

/**
 * Generic representation of one chromosome
 */
public  class Chromosome implements Comparable<Chromosome>{

    protected double rating;
    private String wholeChromosome;

    public double getRating() {
        return rating;
    }

    public int compareTo(Chromosome chromosome) {
        double result = this.rating - chromosome.rating;
        if (result < 0.0) return -1;
        else if (result > 0.0) return 1;
        return 0;
    }


    public String toFileRepresentation() {
        return wholeChromosome;
    }


    public Chromosome fromString(String chromosomeString) {
        String[] chromosomeStringSplitted = chromosomeString.split("\\s+");
        this.rating = Double.parseDouble(chromosomeStringSplitted[1]);
        this.wholeChromosome = chromosomeString;
        return this;
    }
}
