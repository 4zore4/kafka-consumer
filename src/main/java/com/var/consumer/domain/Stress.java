package com.var.consumer.domain;


import lombok.Data;

import java.math.BigInteger;

@Data
public class Stress {
    private String jobId;

    private String name;

    private String mark;

    private Long date;

    private BigInteger time;


}
