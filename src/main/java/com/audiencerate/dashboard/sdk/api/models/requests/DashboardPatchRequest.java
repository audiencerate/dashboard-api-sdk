package com.audiencerate.dashboard.sdk.api.models.requests;

import com.audiencerate.dashboard.sdk.api.enums.HttpMethod;

/**
 * Created by Alex Sangiuliuano
 */
public class DashboardPatchRequest extends DashboardBaseRequest
{

    @Override
    public HttpMethod getMethod()
    {
        return HttpMethod.PATCH;
    }


}
