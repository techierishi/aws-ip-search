package com.rishi.iprangesearch.service;

import com.rishi.iprangesearch.exception.InvalidRegionException;
import com.rishi.iprangesearch.model.IpRange;
import com.rishi.iprangesearch.model.Ipv4Prefix;
import com.rishi.iprangesearch.model.Ipv6Prefix;
import com.rishi.iprangesearch.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class IpRangeSearchService {

    @Autowired
    private RegionRepository regionRepository;

    @Value("${otto.config.ip_range_api}")
    private String ipRangeApi;

    private RestTemplate restTemplate;

    public IpRangeSearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validRegion(String region) {
        var regions = regionRepository.findAll();
        var regionRes = regions.stream()
                .filter(iRegion -> region.equals(iRegion.getName()))
                .findAny()
                .orElse(null);

        return regionRes != null;
    }

    public IpRange filterIpRangeByRegion(String regionToSearch) {
        var responseEntity = restTemplate.getForEntity(ipRangeApi, IpRange.class);
        var ipRange = responseEntity.getBody();

        if (StringUtils.isEmpty(regionToSearch)){
            return ipRange;
        }

        List<Ipv4Prefix> filteredIPv4Prefixes = ipRange.getPrefixes().stream()
                .filter(ipv4Prefix -> ipv4Prefix.getRegion().startsWith(regionToSearch.toLowerCase()))
                .toList();

        ipRange.setPrefixes(filteredIPv4Prefixes);

        List<Ipv6Prefix> filteredIPv6Prefixes = ipRange.getIpv6_prefixes().stream()
                .filter(ipv6Prefix -> ipv6Prefix.getRegion().startsWith(regionToSearch.toLowerCase()))
                .toList();

        ipRange.setIpv6_prefixes(filteredIPv6Prefixes);
        return ipRange;
    }


    public IpRange getIpRange(String region) throws InvalidRegionException {
        if (!StringUtils.isEmpty(region) && !validRegion(region)) {
            throw new InvalidRegionException("Invalid region in the parameter");
        }
        return filterIpRangeByRegion(region);
    }

}
