package iai.kit.edu.core;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.producer.MigrationCompletedPublisher;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import java.io.File;
import java.util.*;

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
    @Autowired
    private AlgorithmWrapper algorithmWrapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    RedisAtomicInteger islandsWithCompleteMigrantsCounter;
    List<Chromosome> cachedMigrants = new ArrayList<>();
    List<List<Pair<String, List<GLEAMChromosome>>>> generations = new ArrayList<>(50);

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
                if (islandsWithCompleteMigrants == islandConfig.getMigrationConfig().getNumberOfIslands()) {
                    islandsWithCompleteMigrantsCounter.set(0);
                    migrationCompletedPublisher.publish();
                }
            }
        }
    }
    public void asyncCacheMigrants(List<GLEAMChromosome> migrants, String neighborNumber){
        synchronized (population) {
            boolean found;
            ImmutablePair<String, List<GLEAMChromosome>> mappedMigrants = new ImmutablePair<>(neighborNumber,migrants);
            if(generations.isEmpty()){
                List<Pair<String, List<GLEAMChromosome>>> firstGeneration = new ArrayList<>(islandConfig.getNeighbors().size());
                firstGeneration.add(mappedMigrants);
                generations.add(firstGeneration);
            } else{
                for(int i = 0; i<generations.size(); i++){
                    found = false;
                    for(int j = 0; j<generations.get(i).size(); j++){
                        if(mappedMigrants.getKey().equals(generations.get(i).get(j).getKey())){
                            j = generations.get(i).size();
                            found = true;
                        }
                    }
                    if(!found){
                        generations.get(i).add(mappedMigrants);
                        i = generations.size();
                    }
                    if(found && i == generations.size() -1){
                        List<Pair<String, List<GLEAMChromosome>>> newGeneration = new ArrayList<>(islandConfig.getNeighbors().size());
                        newGeneration.add(mappedMigrants);
                        generations.add(newGeneration);
                        i = generations.size();
                    }
                }
            }
            this.checkAsyncMigration();
        }
    }

    public void checkAsyncMigration() {
        if(!generations.isEmpty()){
            if(generations.get(0).size() == islandConfig.getNeighbors().size() && islandConfig.isReceivedIntermediatePopulation()){
                islandConfig.setReceivedIntermediatePopulation(false);
                logger.info("completing migration");
                logger.info("received start signal");
                this.prepareAsyncMigrants();
                this.replace();
                generations.remove(0);
                algorithmWrapper.startEpoch();
            }
        }
    }
    private  void prepareAsyncMigrants(){
        for(int i = 0; i< generations.get(0).size(); i++){
            cachedMigrants.addAll(generations.get(0).get(i).getValue());
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
