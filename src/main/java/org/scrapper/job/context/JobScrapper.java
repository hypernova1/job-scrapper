package org.scrapper.job.context;

import lombok.RequiredArgsConstructor;
import org.scrapper.job.service.RocketPunchScrapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JobScrapper {

    private final RocketPunchScrapper rocketPunchScrapper;

    public void execute() {
        try {
            rocketPunchScrapper.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
