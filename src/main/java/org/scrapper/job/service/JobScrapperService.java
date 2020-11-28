package org.scrapper.job.service;

import lombok.RequiredArgsConstructor;
import org.scrapper.job.util.RocketPunchScrapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JobScrapperService {

    private final RocketPunchScrapper rocketPunchScrapper;

    public void execute() {
        try {
            rocketPunchScrapper.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
