package iai.kit.edu.core;

import java.util.HashMap;
import java.util.Map;

public class ExecutionTime {

    private static long startEvolution = 0 ;
    private static long endEvolution = 0 ;
    private Map<Integer, Double> executionTimeIslands = new HashMap<>();


    public void resetExecutionTimeIslands() {
        executionTimeIslands = new HashMap<>();
    }

    public void setExecutionTimeIslands(int taskid,  double executionTime) {
        executionTimeIslands.put(taskid, executionTime);
    }

    public double returnSumExecutionTime ()
    {

        double sum = 0;
        for (double f : executionTimeIslands.values()) {
            sum += f;
        }
        return sum;
    }

    public int returnNumberOfMigrations ()
    {

        return executionTimeIslands.size();
    }

    public static long getStartEvolution() {
        return startEvolution;
    }

    public static void setStartEvolution(long startEvolution) {
        ExecutionTime.startEvolution = startEvolution;
    }

    public static long getEndEvolution() {
        return endEvolution;
    }

    public static void setEndEvolution(long endEvolution) {
        ExecutionTime.endEvolution = endEvolution;
    }


}
