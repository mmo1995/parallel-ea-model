package iai.kit.edu.core;

/**
 * Representation for one GLEAM chromosome
 */
public class GLEAMChromosome extends Chromosome {

    private String wholeChromosome;

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
