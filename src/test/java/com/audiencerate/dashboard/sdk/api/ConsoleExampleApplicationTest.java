package com.audiencerate.dashboard.sdk.api;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.audiencerate.dashboard.sdk.api.DashboardAPI;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardHttpException;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardMappingException;
import com.audiencerate.dashboard.sdk.api.models.DashboardCredentials;
import com.audiencerate.dashboard.sdk.api.models.requests.DashboardGetRequest;
import com.audiencerate.dashboard.sdk.api.models.responses.DashboardListResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class ConsoleExampleApplicationTest {

    @Test
    public void testConsoleApplication() {

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


        Assert.assertTrue( response != null );
        Assert.assertTrue( "SUCCESS".equals(response.getStatus()) );

    }

}