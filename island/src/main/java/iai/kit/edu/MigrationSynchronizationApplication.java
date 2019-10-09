package iai.kit.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MigrationSynchronizationApplication {

    static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(MigrationSynchronizationApplication.class, args);
    }

    public static void stop() {
        ctx.close();
    }

}
