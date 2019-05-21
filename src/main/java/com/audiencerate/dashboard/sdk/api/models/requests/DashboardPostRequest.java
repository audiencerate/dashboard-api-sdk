package com.audiencerate.dashboard.sdk.api.models.requests;

import com.audiencerate.dashboard.sdk.api.enums.HttpMethod;

public class DashboardPostRequest extends DashboardBaseRequest
{

    public DashboardPostRequest()
    {
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }


}
