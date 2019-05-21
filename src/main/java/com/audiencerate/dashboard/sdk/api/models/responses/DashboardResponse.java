package com.audiencerate.dashboard.sdk.api.models.responses;

import com.audiencerate.dashboard.sdk.api.exceptions.DashboardMappingException;
import com.audiencerate.dashboard.sdk.api.interfaces.IDashboardResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;


public abstract class DashboardResponse<B> implements IDashboardResponse<B>
{
    private B body;
    private String status;
    private List<String> messages;
    private String httpStatus;

    @Override
    public <T> T getBody(Class<T> clazz) throws DashboardMappingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.readValue(mapper.writeValueAsString(body), this.getResponseType(clazz));
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new DashboardMappingException("Mapping error");
        }
    }

    public <T> List<T> getBodyAsList(Class<T> clazz) throws DashboardMappingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.readValue(mapper.writeValueAsString(body), this.getResponseType(clazz));
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new DashboardMappingException("Mapping error");
        }
    }


    @Override
    public String getStatus()
    {
        return status;
    }

    @Override
    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public void setBody(B body)
    {
        this.body = body;
    }

    @Override
    public List<String> getMessages()
    {
        return messages;
    }

    @Override
    public void setMessages(List<String> messages)
    {
        this.messages = messages;
    }

    @Override
    public String getHttpStatus()
    {
        return httpStatus;
    }

    @Override
    public void setHttpStatus(String httpStatus)
    {
        this.httpStatus = httpStatus;
    }


}
