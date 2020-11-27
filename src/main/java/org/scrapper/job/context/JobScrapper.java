package org.scrapper.job.context;

import org.scrapper.job.service.RocketPunchScrapper;

import java.io.IOException;

public class JobScrapper {

    public static void execute() {
        try {
            RocketPunchScrapper.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
