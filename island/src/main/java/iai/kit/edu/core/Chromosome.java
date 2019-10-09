package iai.kit.edu.core;

/**
 * Abstract representation of one individual (in GLEAM: one individual=one chromosome)
 */
public abstract class Chromosome implements Comparable<Chromosome>{

    protected double rating;

    public double getRating() {
        return rating;
    }

    public int compareTo(Chromosome chromosome) {
        double result = this.rating - chromosome.rating;
        if (result < 0.0) return -1;
        else if (result > 0.0) return 1;
        return 0;
    }

    public abstract String toFileRepresentation();

    public abstract Chromosome fromString(String string);
}
