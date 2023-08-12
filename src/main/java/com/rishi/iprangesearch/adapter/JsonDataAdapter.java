package com.rishi.iprangesearch.adapter;

import com.rishi.iprangesearch.model.IpRange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonDataAdapter implements DataAdapter{

    @Override
    public String transform(IpRange data) {
        return transformToJson(data);
    }

    private static String transformToJson(IpRange data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
