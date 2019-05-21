package com.audiencerate.dashboard.sdk.api.console;

/**
 * Created by Alex Sangiuliuano
 */

/**
 * This is a Pojo object to map responses. Actually ( 21/05/2019 )
 * it covers everything.
 */

public class Audience
{
    private String audienceId;
    private String partnerId;
    private String audienceName;
    private String integrationCode;
    private long creation;
    private int durationInDays;
    private int size = -1;

    public String getAudienceId()
    {
        return audienceId;
    }

    public void setAudienceId(String audienceId) {
        this.audienceId = audienceId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
    }

    public String getAudienceName()
    {
        return audienceName;
    }

    public void setAudienceName(String audienceName)
    {
        this.audienceName = audienceName;
    }

    public String getIntegrationCode()
    {
        return integrationCode;
    }

    public void setIntegrationCode(String integrationCode)
    {
        this.integrationCode = integrationCode;
    }

    public long getCreation()
    {
        return creation;
    }

    public void setCreation(long creation)
    {
        this.creation = creation;
    }

    public int getDurationInDays()
    {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays)
    {
        this.durationInDays = durationInDays;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String toString()
    {
        return  this.getAudienceId() + " " +
                this.getPartnerId() + " " +
                this.getAudienceName() + " " +
                this.getIntegrationCode() + " " +
                this.getCreation() + " " +
                this.getDurationInDays();
    }
}
