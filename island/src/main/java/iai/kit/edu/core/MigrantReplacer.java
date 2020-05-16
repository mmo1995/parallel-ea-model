package iai.kit.edu.core;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.producer.MigrationCompletedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Caches all received migrants and replaces them in the population as soon as the migration is completed.
 */
public class MigrantReplacer {
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    Population population;
    @Autowired
    @Qualifier("integerTemplate")
    private RedisTemplate<String, Integer> template;
    @Autowired
    private MigrationCompletedPublisher migrationCompletedPublisher;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AlgorithmWrapper algorithmWrapper;

    RedisAtomicInteger islandsWithCompleteMigrantsCounter;
    List<Chromosome> cachedMigrants = new ArrayList<>();

    int migrantsFromNeighborReceived = 0;

    public void cacheMigrants(List<GLEAMChromosome> migrants) {
        synchronized (population) {
            this.cachedMigrants.addAll(migrants);
            migrantsFromNeighborReceived++;
            if (migrantsFromNeighborReceived == islandConfig.getNeighbors().size()) {
                migrantsFromNeighborReceived = 0;
                islandsWithCompleteMigrantsCounter = new RedisAtomicInteger(ConstantStrings.islandMigrantsReceivedCounter, template.getConnectionFactory());
                int islandsWithCompleteMigrants = islandsWithCompleteMigrantsCounter.incrementAndGet();
                logger.debug("islands with complete migrants = " + islandsWithCompleteMigrants);

                // check whether the migrants from all neighbours have been received
/*                if (islandsWithCompleteMigrants == islandConfig.getMigrationConfig().getNumberOfIslands()) {
                    islandsWithCompleteMigrantsCounter.set(0);
                    migrationCompletedPublisher.publish();
                }*/
                this.replace();
                algorithmWrapper.startEpoch();

            }
        }
    }

    public void replace() {
        synchronized (population) {
            applyReplacementStrategy();
            logger.trace("Population size: "+population.size());
            //population.write(populationFile);
            cachedMigrants.clear();
        }
    }

    private void applyReplacementStrategy() {
        if (islandConfig.getMigrationConfig().getReplacementPolicy().equals(ConstantStrings.randomReplacement)) {
            replaceRandom();
        } else if (islandConfig.getMigrationConfig().getReplacementPolicy().equals(ConstantStrings.worstReplacement)) {
            replaceWorst();
        }
    }

    private void replaceRandom() {
        for (Chromosome migrant : cachedMigrants) {
            int randomIndex = (int) Math.random() * population.size();
            population.replace(randomIndex, migrant);
        }

    }

    private void replaceWorst() {
        population.sort();
        // remove as many (worst) chromosomes from the population as there are migrants and add the migrants
        for (int i = 0; i < cachedMigrants.size(); i++) {
            population.remove(0);
            population.add(cachedMigrants.get(0));
            population.sort();
        }

    }

}
