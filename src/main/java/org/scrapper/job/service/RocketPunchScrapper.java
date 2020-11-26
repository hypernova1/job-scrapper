package org.scrapper.job.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class RocketPunchScrapper {

    public static void execute() {
        try {
            String url = "https://www.rocketpunch.com/api/jobs/template?location=서울특별시&specialty=Java&specialty=JSP&tag=웹서비스&tag=e-commerce";
            Document document = Jsoup.connect(url).ignoreContentType(true).get();
            System.out.println(document.getAllElements());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
