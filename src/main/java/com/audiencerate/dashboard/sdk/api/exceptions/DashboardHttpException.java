package com.audiencerate.dashboard.sdk.api.exceptions;

public class DashboardHttpException extends Exception {

    private int httpCode;

    public DashboardHttpException(String message, int httpCode){
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
