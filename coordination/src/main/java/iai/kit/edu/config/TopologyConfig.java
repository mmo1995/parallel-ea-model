package iai.kit.edu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates different topologies for the islands
 */
public class TopologyConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param numberOfIslands in the topology
     * @param topology to be created
     * @return List of lists, where each island has one list of neighbors
     */
    public List<List<String>> getNeighbors(int numberOfIslands, String topology) {
        List<List<String>> allNeighbors = new ArrayList<>();
        if (topology.equals(ConstantStrings.topologyRing)) {
            allNeighbors = getRingTopology(numberOfIslands);
        } else if (topology.equals(ConstantStrings.topologyBiRing)) {
            allNeighbors = getBiRingTopology(numberOfIslands);
        } else if (topology.equals(ConstantStrings.topologyLadder)) {
            allNeighbors = getLadderTopology(numberOfIslands);
        } else if (topology.equals(ConstantStrings.topologyComplete)){
            allNeighbors =getCompleteTopology(numberOfIslands);
        } else{
            logger.info("topology not supported");
        }
        return allNeighbors;
    }

    /**
     * Creates ring topology
     * @param numberOfIslands in the ring topology
     * @return neighbors in the ring topology
     */
    private List<List<String>> getRingTopology(int numberOfIslands) {
        List<List<String>> allNeighbors = new ArrayList<>();
        for (int i = 1; i <= numberOfIslands; i++) {
            List<String> neighbors = new ArrayList<>();
            if (i == numberOfIslands) {
                neighbors.add(1 + "");
            } else {
                neighbors.add(i + 1 + "");
            }
            allNeighbors.add(neighbors);
        }
        return allNeighbors;
    }

    /**
     * Creates bi-directional ring topology
     * @param numberOfIslands in the bi-directional ring topology
     * @return neighbors in the bi-directional ring topology
     */
    private List<List<String>> getBiRingTopology(int numberOfIslands) {
        List<List<String>> allNeighbors = new ArrayList<>();
        for (int i = 1; i <= numberOfIslands; i++) {
            List<String> neighbors = new ArrayList<>();
            if (i == 1) {
                neighbors.add(i + 1 + "");
                neighbors.add(numberOfIslands + "");
            } else if (i == numberOfIslands) {
                neighbors.add(1 + "");
                neighbors.add(i - 1 + "");
            } else {
                neighbors.add(i + 1 + "");
                neighbors.add(i - 1 + "");
            }
            allNeighbors.add(neighbors);
        }
        return allNeighbors;
    }

    /**
     * Creates ladder topology
     * @param numberOfIslands in the ladder topology
     * @return neighbors in the ladder topology
     */
    private List<List<String>> getLadderTopology(int numberOfIslands) {
        List<List<String>> allNeighbors = new ArrayList<>();
        for (int i = 1; i <= numberOfIslands; i++) {
            List<String> neighbors = new ArrayList<>();
            if (i == 1) {
                neighbors.add(numberOfIslands - 1 + "");
                neighbors.add(i + 1 + "");
                neighbors.add(i + 2 + "");
            } else if (i == 2) {
                neighbors.add(numberOfIslands + "");
                neighbors.add(i - 1 + "");
                neighbors.add(i + 2 + "");
            } else if (i == numberOfIslands - 1) {
                neighbors.add(i - 2 + "");
                neighbors.add(i + 1 + "");
                neighbors.add(1 + "");
            } else if (i == numberOfIslands) {
                neighbors.add(i - 2 + "");
                neighbors.add(i - 1 + "");
                neighbors.add(2 + "");
            } else if (i % 2 == 1) {
                neighbors.add(i - 2 + "");
                neighbors.add(i + 1 + "");
                neighbors.add(i + 2 + "");
            } else if (i % 2 == 0) {
                neighbors.add(i - 2 + "");
                neighbors.add(i - 1 + "");
                neighbors.add(i + 2 + "");
            }
            allNeighbors.add(neighbors);
        }
        return allNeighbors;
    }

    /**
     * Creates a complete graph topology
     * @param numberOfIslands in the complete graph topology
     * @return neighbors in the complete graph topology
     */
    private List<List<String>> getCompleteTopology(int numberOfIslands) {
        List<List<String>> allNeighbors = new ArrayList<>();
        for (int i = 1; i <= numberOfIslands; i++) {
            List<String> neighbors = new ArrayList<>();
            for(int j=1; j<= numberOfIslands; j++){
                if(i!=j){
                    neighbors.add(j +"");
                }
            }
            allNeighbors.add(neighbors);
        }
        return allNeighbors;
    }
}
