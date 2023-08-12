package com.rishi.iprangesearch.adapter;

import com.rishi.iprangesearch.model.IpRange;
import com.rishi.iprangesearch.model.Ipv4Prefix;
import com.rishi.iprangesearch.model.Ipv6Prefix;

import java.util.List;

public class CsvDataAdapter implements DataAdapter{
    @Override
    public String transform(IpRange data) {
        return transformToCSV(data);
    }

    private static String transformToCSV(IpRange data) {
        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append("SyncToken,CreateDate,IP_Prefix,Region,Service,Network_Border_Group\n");

        List<Ipv4Prefix> prefixes = data.getPrefixes();
        if (prefixes != null) {
            for (Ipv4Prefix prefix : prefixes) {
                csvBuilder.append(
                        data.getSyncToken()).append(",").append(
                        data.getCreateDate()).append(",").append(
                        prefix.getIp_prefix()).append(",").append(
                        prefix.getRegion()).append(",").append(
                        prefix.getService()).append(",").append(
                        prefix.getNetwork_border_group()).append("\n");
            }
        }

        List<Ipv6Prefix> ipv6Prefixes = data.getIpv6_prefixes();
        if (ipv6Prefixes != null) {
            for (Ipv6Prefix ipv6Prefix : ipv6Prefixes) {
                csvBuilder.append(
                        data.getSyncToken()).append(",").append(
                        data.getCreateDate()).append(",").append(
                        ipv6Prefix.getIpv6_prefix()).append(",").append(
                        ipv6Prefix.getRegion()).append(",").append(
                        ipv6Prefix.getService()).append(",").append(
                        ipv6Prefix.getNetwork_border_group()).append("\n");
            }
        }

        return csvBuilder.toString();
    }
}
