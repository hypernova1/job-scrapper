package org.scrapper.job;

import lombok.RequiredArgsConstructor;
import org.scrapper.job.service.JobScrapperService;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    private final JobScrapperService jobScrapper;

    @Override
    public void run(String... args) throws Exception {
        jobScrapper.execute();
    }
}
