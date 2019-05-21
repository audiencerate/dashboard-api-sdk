package com.audiencerate.dashboard.sdk.api.models.responses;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by Alex Sangiuliuano
 */
public class DashboardAuthFailResponse extends DashboardResponse<Map<String, Object>>
{
    private ObjectMapper mapper = new ObjectMapper();
    private String message;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public <T> JavaType getResponseType(Class<T> clazz)
    {
        return mapper.getTypeFactory().constructType(clazz);
    }
}
