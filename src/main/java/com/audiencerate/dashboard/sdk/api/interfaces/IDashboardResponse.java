package com.audiencerate.dashboard.sdk.api.interfaces;

import com.audiencerate.dashboard.sdk.api.exceptions.DashboardMappingException;
import com.fasterxml.jackson.databind.JavaType;
import java.util.List;

/**
 * Created by Alex Sangiuliuano
 */
public interface IDashboardResponse<B> extends IResponse
{
    <T> JavaType getResponseType(Class<T> clazz);
    void setBody(B body);
    List<String> getMessages();
    void setMessages(List<String> messages);
    <T> T getBody(Class<T> clazz) throws DashboardMappingException;
    <T> List<T> getBodyAsList(Class<T> clazz) throws DashboardMappingException;
}
