package com.audiencerate.dashboard.sdk.api.models.responses;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Alex Sangiuliuano
 */
public class DashboardListResponse extends DashboardResponse<List<Map<String, Object>>>
{
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> JavaType getResponseType(Class<T> clazz)
    {
        return mapper.getTypeFactory().constructCollectionType(List.class, clazz);
    }
}
