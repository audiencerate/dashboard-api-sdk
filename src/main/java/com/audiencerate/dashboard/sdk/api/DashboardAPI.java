package com.audiencerate.dashboard.sdk.api;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.audiencerate.dashboard.sdk.api.enums.HttpMethod;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardAuthException;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardHttpException;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardMappingException;
import com.audiencerate.dashboard.sdk.api.interfaces.IDashboardRequest;
import com.audiencerate.dashboard.sdk.api.interfaces.IDashboardResponse;
import com.audiencerate.dashboard.sdk.api.models.*;
import com.audiencerate.dashboard.sdk.api.models.requests.*;
import com.audiencerate.dashboard.sdk.api.models.responses.DashboardAuthFailResponse;
import com.audiencerate.dashboard.sdk.api.models.responses.DashboardListResponse;
import com.audiencerate.dashboard.sdk.api.models.responses.DashboardSingleResponse;
import com.audiencerate.dashboard.sdk.api.utils.CognitoAuthenticator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class DashboardAPI
{
    private String endpoint;
    private OkHttpClient httpClient;
    private DashboardCredentials credentials;

    public static String tokenId;
    public static String accessToken;
    public static String refreshToken;

    private String clientId;
    private AWSCognitoIdentityProvider client;
    private ObjectMapper mapper = new ObjectMapper();


    private DashboardAPI(Builder builder)
    {
        credentials = builder.credentials;
        clientId = builder.clientId;
        client =  builder.cognitoClient;

        CognitoAuthenticator cognitoAuthenticator = new CognitoAuthenticator(credentials, clientId, client);

        httpClient = new OkHttpClient.Builder()
                .authenticator(cognitoAuthenticator)
                .build();

        endpoint = builder.endpoint;
    }


    public DashboardListResponse call(DashboardGetRequest request) throws DashboardHttpException, DashboardMappingException
    {
        return callInternally(request);
    }

    public DashboardSingleResponse call(DashboardPostRequest request) throws DashboardHttpException, DashboardMappingException
    {
        return callInternally(request);
    }

    public DashboardSingleResponse call(DashboardPatchRequest request) throws DashboardHttpException, DashboardMappingException
    {
        return callInternally(request);
    }

    public DashboardSingleResponse call(DashboardDeleteRequest request) throws DashboardHttpException, DashboardMappingException
    {
        return callInternally(request);
    }

    public DashboardSingleResponse call(DashboardPutRequest request) throws DashboardHttpException, DashboardMappingException
    {
        return callInternally(request);
    }


    private <R extends IDashboardResponse> R callInternally(IDashboardRequest request) throws DashboardHttpException, DashboardMappingException
    {
        Request.Builder builder = new Request.Builder();

        if(request.getMethod().equals(HttpMethod.DELETE) || request.getMethod().equals(HttpMethod.GET))
        {
            HttpUrl.Builder httpUrlBuilder = HttpUrl.get(endpoint + request.getPath()).newBuilder();
            Map<String, Object> map =  request.getData();
            map.forEach((key, value) -> httpUrlBuilder.addQueryParameter(key, String.valueOf(value)));
            builder.url(httpUrlBuilder.build());

            if (tokenId != null)
                builder.addHeader("Authentication", tokenId);
        }
        else if(request.getMethod().equals(HttpMethod.PUT))
        {
            builder.url(endpoint + request.getPath());
            String mapConversion= null;

            try
            {
                mapConversion = new ObjectMapper().writeValueAsString(request.getData());
            } catch (JsonProcessingException e)
            {
                System.out.println("Error whie mapping: " + e.getMessage());
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), mapConversion);
            builder.method(request.getMethod().name(), body);

            if (tokenId != null)
                builder.addHeader("Authentication", tokenId);
        }
        else if (request.getMethod().equals(HttpMethod.PATCH))
        {
            builder.url(endpoint + request.getPath());
            String mapConversion= null;

            try
            {
                mapConversion = new ObjectMapper().writeValueAsString(request.getData());
            } catch (JsonProcessingException e)
            {
                System.out.println("Error whie mapping: " + e.getMessage());
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), mapConversion);
            builder.method(request.getMethod().name(), body);

            if (tokenId != null)
                builder.addHeader("Authentication", tokenId);
        }
        else {
            builder.url(endpoint + request.getPath());

            String mapConversion= null;

            try
            {
                mapConversion = new ObjectMapper().writeValueAsString(request.getData());
            } catch (JsonProcessingException e)
            {
                System.out.println("Error whie mapping: " + e.getMessage());
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), mapConversion);
            builder.method(request.getMethod().name(), body);

            if (tokenId != null)
                builder.addHeader("Authentication", tokenId);
        }


        try
        {
            Response response = httpClient.newCall(builder.build()).execute();

            // CHECK STATUS CODE
            if(response.code() == 200)
            {
                return request.getMethod().equals(HttpMethod.GET) ?
                        (R)this.map(DashboardListResponse.class, response) :
                        (R)this.map(DashboardSingleResponse.class, response);
            }
            else if (response.code() == 401)
            {
                throw new DashboardAuthException( this.map(DashboardAuthFailResponse.class, response).getMessage());
            }
            //TODO: handle 403 error from dash
            else {
                throw new DashboardHttpException("Response with status code: " + response.message(), response.code());
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    private <T extends IDashboardResponse> T map(Class<T> tClass, Response response) throws DashboardMappingException
    {
        try
        {
            assert response.body() != null;
            return mapper.readValue(response.body().byteStream(), tClass);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new DashboardMappingException("Cannot deserialize request");
        }
    }

    public String getClientId()
    {
        return clientId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private AWSCognitoIdentityProvider cognitoClient;
        private DashboardCredentials credentials;
        private String endpoint;
        private String clientId;

        private Builder()
        {
        }

        public Builder withCredentials(DashboardCredentials val)
        {
            credentials = val;
            return this;
        }

        public Builder withCognitoClient(AWSCognitoIdentityProvider val)
        {
            cognitoClient = val;
            return this;
        }

        public Builder withEndpoint(String val)
        {
            endpoint = val;
            return this;
        }

        public Builder withClientId(String val)
        {
            clientId = val;
            return this;
        }

        public DashboardAPI build()
        {
            Objects.requireNonNull(endpoint, "The endopoint can't be null...");
            return new DashboardAPI(this);
        }
    }
}
