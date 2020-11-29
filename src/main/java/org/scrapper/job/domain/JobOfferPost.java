package org.scrapper.job.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobOfferPost implements Serializable {

    private String title;

    private String companyName;

    private String description;

    private String endDate;

    private String link;

    private String careers;

    private LocalDate regDate;

}
