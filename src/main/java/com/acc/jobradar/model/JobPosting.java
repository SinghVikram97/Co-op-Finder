package com.acc.jobradar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class JobPosting {
    private String jobId;
    private String jobTitle;
    private String company;
    private String location;
    private String description;
    private String url;

    public JobPosting(String jobTitle, String company, String location, String description, String url) {
        this.jobTitle = jobTitle;
        this.company = company;
        this.location = location;
        this.description = description;
        this.url = url;
        this.jobId=computeJobId(jobTitle, company, location, description, url);
    }

    @Override
    public String toString() {
        return "JobPosting{" +
                "jobTitle='" + jobTitle + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static String computeJobId(String jobTitle, String company, String location, String description, String url) {
        String jobString = jobTitle + company + location + description + url;
        return UUID.nameUUIDFromBytes(jobString.getBytes()).toString();
    }
}
