package edu.kit.iai.calculation;

import edu.kit.iai.calculation.core.Calculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

import org.apache.commons.io.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class CalculationApplication2 {
    static ConfigurableApplicationContext ctx;
    @Autowired
    static Calculation calculation;
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws IOException {

        ctx = SpringApplication.run(CalculationApplication2.class, args);
        /*calculation.getPrice = restTemplate.getForEntity("https://api.myjson.com/bins/107l9q", String.class);
        calculation.getConsumption = restTemplate.getForEntity("https://api.myjson.com/bins/e89m6", String.class);*/
        // if you use API use the  in Calcualtion .getbody()

        String pathPrice = "./files/price.txt";
        String pathConsumtion = "./files/consumption.txt";

        File filePathPrice = new File(pathPrice);
        File filePathConsumtion = new File(pathConsumtion);


        calculation.getPrice = new String(Files.readAllBytes(Paths.get(pathPrice)), "UTF-8");
        calculation.getConsumption = new String(Files.readAllBytes(Paths.get(pathConsumtion)), "UTF-8");

    }

}
