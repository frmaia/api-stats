package stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages={"stats"})
@EnableAutoConfiguration
@EnableScheduling
public class ApiStatsApp {

    /**
     * Application boot
     */
    public static void main(String [] args) {
        SpringApplication.run(ApiStatsApp.class, args);
    }

}
