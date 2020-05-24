package iai.kit.edu;

public class Configuration {
    
      public int globalPopulationSize = 400;
      public int numberOfIslands= 1;
      public int migrationRate= 3;
      public String topology= "ring";
      public String initialSelectionPolicy= "new";
      public String experimentFileName= "fox.exp";
      public int amountFitness = 0;
      public String selectionPolicy= "best";
      public String replacementPolicy= "worst";
      public String epochTerminationCriterion= "generation";
      public int epochTerminationEvaluation= 1000000;
      public double epochTerminationFitness= 100000.0;
      public int epochTerminationGeneration= 50;
      public int epochTerminationTime= 5;
      public int epochTerminationGDV= 500;
      public int epochTerminationGAK= 100;
      public String globalTerminationCriterion= "generation";
      public int globalTerminationEpoch= 3;
      public int globalTerminationEvaluation= 1000000;
      public double globalTerminationFitness= 100000.0;
      public int globalTerminationGeneration= 1000;
      public int globalTerminationTime= 90;
      public int globalTerminationGDV= 500;
      public int globalTerminationGAK= 100;
      public int delay=0;
      public int demeSize=8;
      public String acceptRuleForOffspring = "localLeast-ES";
      double rankingParameter = 1.45;
      double minimalHammingDistance = 0.1;
}
