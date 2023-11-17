package com.acc.jobradar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class JobPosting {
    private String jobTitle;
    private String company;
    private String location;
    private String description;
    private String url;
}
