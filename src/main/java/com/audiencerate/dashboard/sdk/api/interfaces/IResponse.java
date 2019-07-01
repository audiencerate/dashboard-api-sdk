package com.audiencerate.dashboard.sdk.api.interfaces;

/**
 * Created by Alex Sangiuliuano
 */
public interface IResponse
{
    String getStatus();
    void setStatus(String status);
    String getHttpStatus();
    void setHttpStatus(String httpStatus);
    String getRequestId();
    void setRequestId(String requestId);
}
