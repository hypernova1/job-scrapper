package org.scrapper.job.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class RocketPunchScrapper {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public void execute() throws IOException {
        boolean isTodayWrite = true;
        String response = getResponse(1);

        ObjectNode jsonNodes = mapper.readValue(response, ObjectNode.class);
        String data = jsonNodes.get("data").get("template").textValue();
        Document document = Jsoup.parse(data);
        System.out.println(document);
    }

    private String getResponse(int page) {
        String location = "서울특별시";
        String[] languages = { "Java", "Spring" };
        String[] tags = { "웹서비스", "e-commerce" };
        String url = createUrl(location, page, languages, tags);
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();

        return restTemplate.getForObject(uri, String.class);
    }

    public String createUrl(String location, int page, String[] languages, String[] tags) {
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
