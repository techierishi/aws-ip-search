package com.rishi.iprangesearch.model;

import lombok.Data;

@Data
public class Ipv4Prefix {
    private String network_border_group;
    private String ip_prefix;
    private String service;
    private String region;
}
