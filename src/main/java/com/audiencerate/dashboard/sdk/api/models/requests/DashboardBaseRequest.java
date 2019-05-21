package com.audiencerate.dashboard.sdk.api.models.requests;

import com.audiencerate.dashboard.sdk.api.interfaces.IDashboardRequest;

import java.util.Map;

public abstract class DashboardBaseRequest implements IDashboardRequest
{

    private String path;
    private Map<String, Object> data;

    @Override
    public String getPath()
    {
        return path;
    }

    public Map<String, Object> getData()
    {
        return data;
    }

    public <T extends DashboardBaseRequest> T withData(Map<String, Object> aData)
    {
        data = aData;
        return (T) this;
    }

    public <T extends DashboardBaseRequest> T withPath(String aPath)
    {
        path = aPath;
        return (T) this;
    }


}
