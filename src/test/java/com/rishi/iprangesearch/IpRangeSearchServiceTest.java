package com.rishi.iprangesearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishi.iprangesearch.model.IpRange;
import com.rishi.iprangesearch.model.Ipv4Prefix;
import com.rishi.iprangesearch.model.Ipv6Prefix;
import com.rishi.iprangesearch.model.Region;
import com.rishi.iprangesearch.repository.RegionRepository;
import com.rishi.iprangesearch.service.IpRangeSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IpRangeSearchServiceTest {

    @Autowired
    private IpRangeSearchService ipRangeSearchService;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Mock
    private RegionRepository regionRepository;

    @Test
    void testValidRegion_ValidRegion_ReturnsTrue() throws URISyntaxException, JsonProcessingException {
        var region1 = new Region();
        region1.setId(1);
        region1.setName("EU");
        var region2 = new Region();
        region2.setId(2);
        region2.setName("SA");

        ipRangeMock();

        List<Region> regions = Arrays.asList(region1, region2);
        when(regionRepository.findAll()).thenReturn(regions);
        assertTrue(ipRangeSearchService.validRegion("EU"));
    }

    @Test
    void testValidRegion_InvalidRegion_ReturnsFalse() throws URISyntaxException, JsonProcessingException {
        var region1 = new Region();
        region1.setId(1);
        region1.setName("EU");
        var region2 = new Region();
        region2.setId(2);
        region2.setName("SA");

        ipRangeMock();

        List<Region> regions = Arrays.asList(region1, region2);
        when(regionRepository.findAll()).thenReturn(regions);
        assertFalse(ipRangeSearchService.validRegion("WR"));
    }

    @Test
    void testFilterIpRangeByRegion_WithValidRegion_ReturnsFilteredIpRange() throws URISyntaxException, JsonProcessingException {
        ipRangeMock();

        IpRange filteredIpRange = ipRangeSearchService.filterIpRangeByRegion("US");
        assertEquals(1, filteredIpRange.getPrefixes().size());
        assertEquals("192.168.1.0/24", filteredIpRange.getPrefixes().get(0).getIp_prefix());
    }

    private void ipRangeMock() throws URISyntaxException, JsonProcessingException {
        IpRange ipRange = new IpRange();
        List<Ipv4Prefix> ipv4Prefixes = new ArrayList<>();
        List<Ipv6Prefix> ipv6Prefixes = new ArrayList<>();

        var ipv4 = new Ipv4Prefix();
        ipv4.setIp_prefix("192.168.1.0/24");
        ipv4.setRegion("us-west-1");
        ipv4Prefixes.add(ipv4);
        var ipv6 = new Ipv6Prefix();
        ipv6.setIpv6_prefix("2a05:d07a:a000::/40");
        ipv6.setRegion("us-east-1");
        ipv6Prefixes.add(ipv6);
        ipRange.setPrefixes(ipv4Prefixes);
        ipRange.setIpv6_prefixes(ipv6Prefixes);

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("https://ip-ranges.amazonaws.com/ip-ranges.json")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(ipRange))
                );
    }

}
