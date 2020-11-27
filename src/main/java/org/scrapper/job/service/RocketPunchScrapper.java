package org.scrapper.job.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class RocketPunchScrapper {

    public static void execute() throws IOException {

        boolean isTodayWrite = true;
        String location = "서울특별시";
        int page = 1;
        String[] languages = { "Java", "Spring" };
        String[] tags = { "웹서비스", "e-commerce" };
        String url = createUrl(location, page, languages, tags);

        Document document = Jsoup.connect(url).ignoreContentType(true).get();

        System.out.println(document.getAllElements());
//        while (isTodayWrite) {
//            if (true) {
//                isTodayWrite = false;
//            }
//        }

    }

    public static String createUrl(String location, int page, String[] languages, String[] tags) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.rocketpunch.com/api/jobs/template?");
        sb.append("&location=").append(location);
        sb.append("&page=").append(page);
        for (String language : languages) {
            sb.append("&specialty=").append(language);
        }
        for (String tag : tags) {
            sb.append("&tag=").append(tag);
        }
        return sb.toString();
    }

}
