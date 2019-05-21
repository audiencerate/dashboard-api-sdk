package com.audiencerate.dashboard.sdk.api.models;

import com.amazonaws.regions.Regions;

public class DashboardCredentials
{
    private String email;
    private String password;
    private Regions region;

    public DashboardCredentials(String mail, String password, Regions region)
    {
        this.email = mail;
        this.password = password;
        this.region = region;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Regions getRegion() {return  region; }
}
