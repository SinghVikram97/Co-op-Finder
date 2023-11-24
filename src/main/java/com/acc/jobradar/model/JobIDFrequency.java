package com.acc.jobradar.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobIDFrequency {
    private String jobId;
    private int frequency;
}
