package org.scrapper.job.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scrapper.job.domain.Company;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RocketPunchScrapper {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public void execute() throws IOException {

        extractJobData();

    }

    private void extractJobData() throws JsonProcessingException {
        while (true) {
            String response = getResponse(1);
            Elements companyElementList = findCompanyList(response);
            List<Company> companyList = extractCompanyList(companyElementList);

            if (companyElementList.size() != companyList.size()) {
                break;
            }
        }
    }

    private List<Company> extractCompanyList(Elements companyElementList) {
        List<Company> companies = new ArrayList<>();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd"));
        for (Element element : companyElementList) {
            Elements jobDates = element.getElementsByClass("job-dates").get(0).getElementsByTag("span");
            String writeDate = jobDates.get(1).text().split(" ")[0];

            if (!writeDate.trim().equals(today)) {
                break;
            }

            Element companyNames = element.getElementsByClass("company-name").get(0);
            String companyKoreanName = companyNames.getElementsByTag("strong").text();
            String companyEnglishName = companyNames.getElementsByTag("small").text();
            Element jobDetail = element.getElementsByClass("company-jobs-detail").get(0);
            String companyName = companyKoreanName + companyEnglishName;
            String description = element.getElementsByClass("description").text();
            String endDate = jobDates.get(0).text().split(" ")[0];
            String link = jobDetail.getElementsByTag("a").get(0).attr("href");

            Company company = Company.builder()
                    .name(companyName)
                    .description(description)
                    .endDate(endDate)
                    .link(link)
                    .build();
            companies.add(company);

        }
        return companies;
    }

    private Elements findCompanyList(String response) throws JsonProcessingException {
        ObjectNode jsonNodes = mapper.readValue(response, ObjectNode.class);
        String data = jsonNodes.get("data").get("template").textValue();
        Document document = Jsoup.parse(data);

        return document.getElementsByClass("company");
    }

    private String getResponse(int page) {
        String location = "서울특별시";
        String[] languages = { "Java", "Spring" };
        String[] tags = { "웹서비스", "e-commerce" };
        String url = createUrl(location, page, languages, tags);
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();

        return restTemplate.getForObject(uri, String.class);
    }

    private String createUrl(String location, int page, String[] languages, String[] tags) {
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
