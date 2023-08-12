package com.rishi.iprangesearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.rishi.iprangesearch.controller.IpRangeSearchController;
import com.rishi.iprangesearch.exception.InvalidRegionException;
import com.rishi.iprangesearch.model.IpRange;
import com.rishi.iprangesearch.service.IpRangeSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class IpRangeSearchControllerTest {


    @Test
    public void testGetIpRangeWithValidRegionAndHtmlDataAdapter() throws InvalidRegionException{
        IpRangeSearchService ipRangeSearchService = mock(IpRangeSearchService.class);
        IpRange mockIpRange = new IpRange();
        when(ipRangeSearchService.getIpRange("Region")).thenReturn(mockIpRange);

        IpRangeSearchController controller = new IpRangeSearchController(ipRangeSearchService);
        ResponseEntity responseEntity = controller.getIpRange("Region", null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetIpRangeWithInvalidRegion()  throws InvalidRegionException{
        IpRangeSearchService ipRangeSearchService = mock(IpRangeSearchService.class);
        when(ipRangeSearchService.getIpRange("InvalidRegion")).thenThrow(new InvalidRegionException("Invalid region"));
        IpRangeSearchController controller = new IpRangeSearchController(ipRangeSearchService);
        ResponseEntity responseEntity = controller.getIpRange("InvalidRegion", null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid region", responseEntity.getBody());
    }

    @Test
    public void testGetIpRangeWithJsonDataAdapter() throws InvalidRegionException {
        // Mocking the IpRangeSearchService
        IpRangeSearchService ipRangeSearchService = mock(IpRangeSearchService.class);
        IpRange mockIpRange = new IpRange();
        when(ipRangeSearchService.getIpRange("Region")).thenReturn(mockIpRange);

        // Create an instance of the controller
        IpRangeSearchController controller = new IpRangeSearchController(ipRangeSearchService);

        // Call the controller method with dt parameter set to "json"
        ResponseEntity responseEntity = controller.getIpRange("Region", "json");

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions based on the response if needed
    }

    @Test
    public void testGetIpRangeWithNullDataAdapter()  throws InvalidRegionException {
        IpRangeSearchService ipRangeSearchService = mock(IpRangeSearchService.class);
        IpRange mockIpRange = new IpRange();
        when(ipRangeSearchService.getIpRange("Region")).thenReturn(mockIpRange);

        IpRangeSearchController controller = new IpRangeSearchController(ipRangeSearchService);

        ResponseEntity responseEntity = controller.getIpRange("Region", null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}