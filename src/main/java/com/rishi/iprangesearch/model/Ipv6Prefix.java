package com.rishi.iprangesearch.model;

import lombok.Data;

@Data
public class Ipv6Prefix {
    private String network_border_group;
    private String ipv6_prefix;
    private String service;
    private String region;
}
