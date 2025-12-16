package com.c3.travleteller;

import com.c3.travleteller.config.OciVaultApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TravleTellerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TravleTellerApplication.class);
        app.addInitializers(new OciVaultApplicationContextInitializer());
        app.run(args);
    }

}
