package com.audiencerate.dashboard.sdk.api.examples;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.audiencerate.dashboard.sdk.api.DashboardAPI;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardHttpException;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardMappingException;
import com.audiencerate.dashboard.sdk.api.models.DashboardCredentials;
import com.audiencerate.dashboard.sdk.api.models.requests.DashboardGetRequest;
import com.audiencerate.dashboard.sdk.api.models.requests.DashboardPostRequest;
import com.audiencerate.dashboard.sdk.api.models.responses.DashboardListResponse;
import com.audiencerate.dashboard.sdk.api.models.responses.DashboardSingleResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleLongRunningExampleApplication
{
    public static void main(String... args)
    {
        /*** This is a examples application built as tutorial ***/

        /*** First Step: Cognito Login: ***/

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(System.getenv("IAM_ACCESS_KEY"), System.getenv("IAM_SECRET_KEY"));

        String email = System.getenv("COGNITO_USER_EMAIL");
        String password = System.getenv("COGNITO_USER_PASSWORD");
        String clientId = System.getenv("COGNITO_CLIENT_ID");
        String endpoint = System.getenv("API_ENDPOINT");


        /*** Note that as default we use the US_EAST_REGION ***/

        DashboardCredentials credentials = new DashboardCredentials(email, password, Regions.US_EAST_1);


        DashboardAPI dashboardAPI  = DashboardAPI.newBuilder()
                .withEndpoint(endpoint)
                .withCredentials(credentials)
                .withClientId(clientId)
                .withCognitoClient(AWSCognitoIdentityProviderClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                        .build())
                .build();


        Map<String, Object> getMap = new HashMap<>();
        getMap.put("offset", 0);
        getMap.put("size", 10);
        getMap.put("filter", "");
        getMap.put("sort", "audienceName");
        getMap.put("sortOrder", "asc");

        DashboardGetRequest getRequest = new DashboardGetRequest()
                .withPath("/audience")
                .withData(getMap);



        // run continously and catch
        try{
                while (true) {

                    System.out.println("\nTrying to call an API...");

                    try {
                        DashboardListResponse response = dashboardAPI.call(getRequest);
                        System.out.println("called API response " + response.getHttpStatus());
                        System.out.println("called API response " + response.getRequestId());
                        System.out.println("called API response " + response.getStatus());
                        System.out.println("called API response " + response.getMessages());
                        System.out.println("called API response str " + response.toString());

                    } catch(DashboardHttpException | DashboardMappingException e)
                    {
                        e.printStackTrace();
                        System.out.println("Exception calling API " + e.getMessage());
                    }

                    System.out.println("Sleeping...");
                    Thread.sleep(1000 * 60);
                }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception running continuosly API " + e.getMessage());
        }


    }
}
