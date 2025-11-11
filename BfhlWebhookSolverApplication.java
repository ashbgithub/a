package Jar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// IMPORT the WebhookService using the folder path (service/service)
import Jar.service.service.WebhookService;

@SpringBootApplication
public class BfhlWebhookSolverApplication {

    public static void main(String[] args) {
        SpringApplication.run(BfhlWebhookSolverApplication.class, args);
    }

    // Use the fully-matching package type here
    @Bean
    CommandLineRunner run(WebhookService service) {
        return args -> {
            service.performFlow();
        };
    }
}
