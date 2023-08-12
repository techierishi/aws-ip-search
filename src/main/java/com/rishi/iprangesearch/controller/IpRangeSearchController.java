package com.rishi.iprangesearch.controller;

import com.rishi.iprangesearch.adapter.DataAdapter;
import com.rishi.iprangesearch.exception.InvalidRegionException;
import com.rishi.iprangesearch.factory.DataAdapterFactory;
import com.rishi.iprangesearch.model.IpRange;
import com.rishi.iprangesearch.service.IpRangeSearchService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/ip-range")
@Slf4j
public class IpRangeSearchController {

    private IpRangeSearchService ipRangeSearchService;

    public IpRangeSearchController(IpRangeSearchService ipRangeSearchService){
        this.ipRangeSearchService = ipRangeSearchService;
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @CircuitBreaker(name = "getIpRange", fallbackMethod = "fallBackIpRange")
    @RateLimiter(name = "getIpRange", fallbackMethod = "rateLimitingFallback")
    public ResponseEntity getIpRange(@RequestParam(name = "region", required = false) String region, @RequestParam(name = "dt", required = false) String dt) {

        IpRange ipRange = null;
        try{
            ipRange = ipRangeSearchService.getIpRange(region);
        } catch (InvalidRegionException irEx){
            log.error("Invalid region provided: {}", region);
            return ResponseEntity.badRequest().body(irEx.getMessage());
        } catch (Exception ex){
            log.error("Error while getting data from upstream for region: {}", region, ex);
            return ResponseEntity.internalServerError().body("Error getting data from upstream!");
        }

        var dataType = !StringUtils.isEmpty(dt) ? dt.trim().toLowerCase() : "csv";
        DataAdapter dataAdapter = DataAdapterFactory.getAdapter(dataType);

        if (dataAdapter == null) {
            return ResponseEntity.badRequest().body("Wrong data type passed!");
        }

        return ResponseEntity.ok(dataAdapter.transform(ipRange));
    }

    public ResponseEntity fallBackIpRange(Throwable throwable) {
        return ResponseEntity.internalServerError().body("Upstream service down!");
    }

    public ResponseEntity rateLimitingFallback(String id, String arg, Throwable throwable) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "60s");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(responseHeaders)
                .body("Too many requests - Retry in a moment");
    }
}
