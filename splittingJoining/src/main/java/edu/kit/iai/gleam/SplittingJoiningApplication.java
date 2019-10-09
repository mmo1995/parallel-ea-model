package edu.kit.iai.gleam;

import edu.kit.iai.gleam.controller.MasterSlave;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SplittingJoiningApplication {
    private static MasterSlave conn;

    public static void main(String[] args) {
        SpringApplication.run(SplittingJoiningApplication.class, args);
        conn = new MasterSlave();
        //	conn.ConnectZookeeper();


    }
}
			
