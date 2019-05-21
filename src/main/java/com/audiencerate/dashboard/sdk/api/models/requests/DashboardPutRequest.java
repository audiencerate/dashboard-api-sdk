package com.audiencerate.dashboard.sdk.api.models.requests;

import com.audiencerate.dashboard.sdk.api.enums.HttpMethod;

public class DashboardPutRequest extends DashboardBaseRequest
{
    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PUT;
    }

}
