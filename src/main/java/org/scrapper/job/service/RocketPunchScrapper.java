package org.scrapper.job.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scrapper.job.domain.JobOfferPost;
import org.scrapper.job.util.CSVWRiter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log
public class RocketPunchScrapper {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public void execute() throws IOException {
        List<JobOfferPost> jobOfferPostList = extractJobOfferData();
        log.info(jobOfferPostList.size() + "개 데이터 수집");
        CSVWRiter.writeSVS(jobOfferPostList);
        log.info("CSV 출력 완료");
    }

    private List<JobOfferPost> extractJobOfferData() throws JsonProcessingException {
        List<JobOfferPost> jobOfferPostList = new ArrayList<>();
        int page = 1;
        while (true) {
            String response = getResponse(page);
            Elements jobOfferElementList = findCompanyList(response);
            jobOfferPostList.addAll(extractJobOfferList(jobOfferElementList));
            if (isLastPage(jobOfferPostList, jobOfferElementList)) {
                return jobOfferPostList;
            }
            page++;
        }
    }

    private List<JobOfferPost> extractJobOfferList(Elements jobOfferElementList) {
        List<JobOfferPost> JobOfferList = new ArrayList<>();

        for (Element element : jobOfferElementList) {
            Elements jobDates = element.getElementsByClass("job-dates").get(0).getElementsByTag("span");
            String writeDate = jobDates.get(1).text().split(" ")[0];

            if (!isTodayPost(writeDate)) {
                break;
            }

            Element companyNames = element.getElementsByClass("company-name").get(0);
            Element jobDetail = element.getElementsByClass("company-jobs-detail").get(0);
            String title = element.getElementsByClass("company-jobs-detail").get(0).getElementsByTag("a").get(0).text();
            String companyKoreanName = companyNames.getElementsByTag("strong").text();
            String companyEnglishName = companyNames.getElementsByTag("small").text();
            String companyName = companyKoreanName + companyEnglishName;
            String description = element.getElementsByClass("description").text();
            String endDate = jobDates.get(0).text().split(" ")[0];
            String jobStatInfo = jobDetail.getElementsByClass("job-stat-info").text();
            String[] jobStatInfos = jobStatInfo.split(" / ");
            String careers = jobStatInfos[jobStatInfos.length - 1];
            String link = jobDetail.getElementsByTag("a").get(0).attr("href");

            JobOfferPost jobOfferPost = JobOfferPost.builder()
                    .title(title)
                    .companyName(companyName)
                    .description(description)
                    .endDate(endDate)
                    .careers(careers)
                    .link(link)
                    .regDate(LocalDate.now())
                    .build();
            JobOfferList.add(jobOfferPost);
        }
        return JobOfferList;
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
        int[] careers = { 1, 2 };
        String url = createUrl(location, page, languages, tags, careers);
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();

        return restTemplate.getForObject(uri, String.class);
    }

    private String createUrl(String location, int page, String[] languages, String[] tags, int[] careers) {
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
        for (int career : careers) {
            sb.append("&career_type").append(career);
        }
        return sb.toString();
    }

    private boolean isLastPage(List<JobOfferPost> jobOfferPostList, Elements jobOfferElementList) {
        return jobOfferElementList.size() != jobOfferPostList.size();
    }

    private boolean isTodayPost(String writeDate) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd"));
        return writeDate.trim().equals(today);
    }

}
