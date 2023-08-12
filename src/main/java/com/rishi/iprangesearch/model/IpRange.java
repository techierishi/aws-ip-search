package com.rishi.iprangesearch.model;

import lombok.Data;

import java.util.List;

@Data
public class IpRange {
    private List<Ipv4Prefix> prefixes;
    private Integer syncToken;
    private List<Ipv6Prefix> ipv6_prefixes;
    private String createDate;
}
