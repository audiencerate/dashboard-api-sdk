package com.audiencerate.dashboard.sdk.api;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
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
public class CognitoAuthTest {

    @Test
    public void testCognitoLogin() {

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("IAM_ACCESS_KEY", "IAM_SECRET_KEY");

        AWSCognitoIdentityProvider client = AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();



        DashboardCredentials credentials = new DashboardCredentials(
                System.getenv("COGNITO_USER_EMAIL"),
                System.getenv("COGNITO_USER_PASSWORD"),
                Regions.US_EAST_1);


        HashMap authParams = new HashMap<>();
        authParams.put("USERNAME", credentials.getEmail());
        authParams.put("PASSWORD", credentials.getPassword());

        InitiateAuthRequest authRequest = new InitiateAuthRequest();
        authRequest.addAuthParametersEntry("USERNAME", credentials.getEmail());
        authRequest.addAuthParametersEntry("PASSWORD", credentials.getPassword());
        authRequest.setClientId(System.getenv("COGNITO_CLIENT_ID"));
        authRequest.setAuthFlow(AuthFlowType.USER_PASSWORD_AUTH);

        InitiateAuthResult result = null;

        try
        {
            result = client.initiateAuth(authRequest);
        }
        catch (RuntimeException e)
        {
            System.out.println("Login Error: " + e.getMessage());
        }

        AuthenticationResultType authenticationResult = result.getAuthenticationResult();
        DashboardAPI.accessToken = authenticationResult.getAccessToken();
        DashboardAPI.tokenId = authenticationResult.getIdToken();
        DashboardAPI.refreshToken = authenticationResult.getRefreshToken();




    }


    @Test
    public void testLoginWithBadInvalidToken() {

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


        // 2 - set an invalid token and see how it's returns an error code

        // id token expired for my account
        DashboardAPI.tokenId = "xxxxxxxxx";

        /*** GET call() method ***/
        response = null;

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