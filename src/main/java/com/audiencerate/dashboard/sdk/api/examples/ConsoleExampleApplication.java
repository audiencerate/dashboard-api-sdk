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

public class ConsoleExampleApplication
{
    public static void main(String... args)
    {
        /*** This is a examples application built as tutorial ***/

        /*** First Step: Cognito Login: ***/

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("IAM_ACCESS_KEY", "IAM_SECRET_KEY");

        String email = "COGNITO_USER_EMAIL";
        String password = "COGNITO_USER_PASSWORD";
        String clientId = "COGNITO_CLIENT_ID";
        String endpoint = "API_ENDPOINT";


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



        /*** Step 2: make a GET and A POST ***/

        Map<String, Object> getMap = new HashMap<>();
        getMap.put("offset", 0);
        getMap.put("size", 10);
        getMap.put("filter", "");
        getMap.put("sort", "audienceName");
        getMap.put("sortOrder", "asc");

        DashboardGetRequest getRequest = new DashboardGetRequest()
                .withPath("/audience")
                .withData(getMap);

        /*** GET call() method ***/
        DashboardListResponse response = null;

        try
        {
            response = dashboardAPI.call(getRequest);
        }
        catch (DashboardHttpException | DashboardMappingException e)
        {
            e.printStackTrace();
        }


        List<Audience> body = null;
        try
        {
            body = response.getBodyAsList(Audience.class);
        } catch (DashboardMappingException e)
        {
            e.printStackTrace();
        }


        System.out.println(body.toString());




        /*** Build a POST request for an Audience ***/

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("audienceName", "Audience Name");
        postMap.put("branded", 1);                           // optional parameter
        postMap.put("description", "Audience description");  // optional parameter

        DashboardPostRequest postRequest =  new DashboardPostRequest()
                .withPath("/audience")
                .withData(postMap);

        DashboardSingleResponse responseMessage = null;

        try
        {
            /*** POST call() method ***/
            responseMessage = dashboardAPI.call(postRequest);
        } catch (DashboardHttpException | DashboardMappingException e)
        {
            e.printStackTrace();
        }

        Audience audience = null;
        try
        {
            audience = responseMessage.getBody(Audience.class);
        } catch (DashboardMappingException e)
        {
            e.printStackTrace();
        }

        System.out.println(audience.toString());

    }
}
