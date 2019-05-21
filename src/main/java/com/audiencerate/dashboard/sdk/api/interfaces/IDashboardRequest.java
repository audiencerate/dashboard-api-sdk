package com.audiencerate.dashboard.sdk.api.interfaces;

import com.audiencerate.dashboard.sdk.api.enums.HttpMethod;

import java.util.Map;

public interface IDashboardRequest {

    HttpMethod getMethod();
    String getPath();
    Map<String, Object> getData();
}
