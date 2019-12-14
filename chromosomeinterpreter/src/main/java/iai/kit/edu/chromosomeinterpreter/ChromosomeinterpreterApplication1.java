package iai.kit.edu.chromosomeinterpreter;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@SpringBootApplication
public class ChromosomeinterpreterApplication1 {

private static RestTemplate restTemplate = new RestTemplate();

static ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws IOException {
        ctx = SpringApplication.run(ChromosomeinterpreterApplication1.class, args);
        /*chromosomeinterpreter.getGeneration =  restTemplate.getForEntity("https://api.myjson.com/bins/k0lm6", String.class);
        chromosomeinterpreter.getConsumption = restTemplate.getForEntity("https://api.myjson.com/bins/e89m6", String.class);*/

        // if you use API use the  in Chromnosmeinterpreter .getbody()




    }
}
