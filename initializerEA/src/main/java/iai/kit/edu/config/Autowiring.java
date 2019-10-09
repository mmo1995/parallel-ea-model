package iai.kit.edu.config;

import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.algorithm.GLEAMStarter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;


/**
 * Autowires the components of the application
 */
@Configuration
@ComponentScan("iai.kit.edu")
public class Autowiring {

    /**
     * Workspace path to initializer
     * @return workspace path
     */
    @Bean
    String workspacePath() {
        return ConstantStrings.initializerPath;
    }

    /**
     * Creates instance of GLEAMStarter
     * @return GLEAMStarter
     */
    @Bean
    AlgorithmStarter algorithmStarter() {
        return new GLEAMStarter(workspacePath());
    }

    /**
     * Path to population
     * @return population file
     */
    @Bean(name = "populationFile")
    File populationFile() {
        return new File(ConstantStrings.initializerPath + ConstantStrings.initialPopulationFileName);
    }
}
