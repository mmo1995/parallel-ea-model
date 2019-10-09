package iai.kit.edu.core;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Selects migrants for migration
 */
public class MigrantSelector {
    @Autowired
    private IslandConfig islandConfig;
    @Autowired
    Population population;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Chromosome> selectMigrants() {
        synchronized (population) {
            //population.read(populationFile);
            logger.trace("Population size: "+population.size());
            List<Chromosome> selectedMigrants = applySelectionStrategy();
            return selectedMigrants;
        }
    }


    private List<Chromosome> applySelectionStrategy() {
        List<Chromosome> selectedMigrants = new ArrayList<>();
        if (islandConfig.getMigrationConfig().getSelectionPolicy().equals(ConstantStrings.randomSelection)) {
            selectedMigrants = selectRandom();
        } else if (islandConfig.getMigrationConfig().getSelectionPolicy().equals(ConstantStrings.bestSelection)) {
            selectedMigrants = selectBest();
        }
        return selectedMigrants;

    }

    private List<Chromosome> selectRandom() {
        List<Chromosome> selectedMigrants = new ArrayList<>();
        Set<Integer> alreadySelected = new HashSet<>();
        for (int i = 0; i < islandConfig.getMigrationConfig().getMigrationRate(); i++) {
            int randomIndex = (int) Math.random() * population.size();
            while (alreadySelected.contains(randomIndex)) {
                randomIndex = (int) Math.random() * population.size();
            }
            selectedMigrants.add(population.get(randomIndex));
            alreadySelected.add(randomIndex);
        }
        return selectedMigrants;
    }

    private List<Chromosome> selectBest() {
        List<Chromosome> selectedMigrants = new ArrayList<>();
        population.sort();
        for (int i = 1; i <= islandConfig.getMigrationConfig().getMigrationRate(); i++) {
            selectedMigrants.add(population.get(population.size() - i));
        }
        return selectedMigrants;
    }
}
