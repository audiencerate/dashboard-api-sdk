package com.audiencerate.dashboard.sdk.api.utils;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.audiencerate.dashboard.sdk.api.DashboardAPI;
import com.audiencerate.dashboard.sdk.api.models.DashboardCredentials;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import java.util.HashMap;

/**
 * Created by Alex Sangiuliano
 */
public class CognitoAuthenticator implements Authenticator
{
    private DashboardCredentials credentials;
    private String clientId;
    private AWSCognitoIdentityProvider client;
    private String header = "Authentication";


    public CognitoAuthenticator(DashboardCredentials credentials, String clientId,  AWSCognitoIdentityProvider client)
    {
        this.credentials = credentials;
        this.clientId = clientId;
        this.client = client;
    }

    @Override
    public Request authenticate(Route route, Response response)
    {
        Request fixedRequest = response.request();

        if (response.code() == 401 && response.request().header(header) == null)
        {
            HashMap authParams = new HashMap<>();
            authParams.put("USERNAME", credentials.getEmail());
            authParams.put("PASSWORD", credentials.getPassword());

            InitiateAuthRequest authRequest = new InitiateAuthRequest();
            authRequest.addAuthParametersEntry("USERNAME", credentials.getEmail());
            authRequest.addAuthParametersEntry("PASSWORD", credentials.getPassword());
            authRequest.setClientId(clientId);
            authRequest.setAuthFlow(AuthFlowType.USER_PASSWORD_AUTH);

            InitiateAuthResult result = null;

            try
            {
                result = client.initiateAuth(authRequest);
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
                System.out.println("Login Error: " + e.getMessage());
                return null;
            }

            AuthenticationResultType authenticationResult = result.getAuthenticationResult();
            DashboardAPI.accessToken = authenticationResult.getAccessToken();
            DashboardAPI.tokenId = authenticationResult.getIdToken();
            DashboardAPI.refreshToken = authenticationResult.getRefreshToken();

            fixedRequest = response.request().newBuilder().removeHeader(header)
                    .addHeader(header, DashboardAPI.tokenId)
                    .build();

            return fixedRequest;

        }


        // ISSUE - refreshing also when getting 400 expire token
        if ( (response.message() != null && response.message().toLowerCase().contains("expired") && response.code() >= 400)
           || response.code() == 401 && response.request().header(header) != null )
        {

            InitiateAuthRequest authRequest = new InitiateAuthRequest();
            authRequest.addAuthParametersEntry("REFRESH_TOKEN", DashboardAPI.refreshToken);
            authRequest.setClientId(clientId);
            authRequest.setAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH);

            InitiateAuthResult initiateAuthResult = null;

            try
            {
                initiateAuthResult = client.initiateAuth(authRequest);
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
                System.out.println("Login Error: " + e.getMessage());
                return null;
            }

            AuthenticationResultType authenticationResult = initiateAuthResult.getAuthenticationResult();
            DashboardAPI.accessToken = authenticationResult.getAccessToken();
            // fix: check if Cognito returns the refesh token: the refresh token can be null if it's not expired already, so reuse the old refresh for all
            // the other sessions. If Cognito returns a token it'll be replaced with a new one (refreshToken has a default of 30 days of expiration)
            if(authenticationResult.getRefreshToken() != null )
                DashboardAPI.refreshToken = authenticationResult.getRefreshToken();
            DashboardAPI.tokenId = authenticationResult.getIdToken();

            fixedRequest = response.request().newBuilder().removeHeader(header)
                    .addHeader(header, DashboardAPI.tokenId)
                    .build();

            return fixedRequest;
        }

        return fixedRequest;
    }
}
