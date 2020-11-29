package org.scrapper.job.util;

import org.scrapper.job.domain.JobOfferPost;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVWRiter {

    private static final String CSV_SEPARATOR = ",";

    public static void writeSVS(List<JobOfferPost> jobOfferPostList) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/melchor/" + today + ".csv"), StandardCharsets.UTF_8))) {
            br.write("\ufeff");
            br.write("번호, 제목,회사명,경력,설명,마감일,등록일,링크");
            br.newLine();
            for (int i = 0; i < jobOfferPostList.size(); i++) {
                String jobOffer = (i + 1) + CSV_SEPARATOR +
                        jobOfferPostList.get(i).getTitle() + CSV_SEPARATOR +
                        jobOfferPostList.get(i).getCompanyName() + CSV_SEPARATOR +
                        jobOfferPostList.get(i).getCareers().replace(",", "/") + CSV_SEPARATOR +
                        jobOfferPostList.get(i).getDescription() + CSV_SEPARATOR +
                        jobOfferPostList.get(i).getEndDate() + CSV_SEPARATOR +
                        jobOfferPostList.get(i).getRegDate() + CSV_SEPARATOR +
                        jobOfferPostList.get(i).getLink() + CSV_SEPARATOR;

                br.write(jobOffer);
                br.newLine();
            }
            br.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
