package com.audiencerate.dashboard.sdk.api.console;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityResult;
import com.audiencerate.dashboard.sdk.api.DashboardAPI;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardHttpException;
import com.audiencerate.dashboard.sdk.api.exceptions.DashboardMappingException;
import com.audiencerate.dashboard.sdk.api.models.DashboardCredentials;
import com.audiencerate.dashboard.sdk.api.models.requests.DashboardPostRequest;
import com.audiencerate.dashboard.sdk.api.models.responses.DashboardSingleResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex Sangiuliano
 */


public class ConsoleExampleUploader
{
    public static void main (String... args)
    {

        /**
         * FIRST STEP: Get the aws security token, assume the IAM role and start a session;
         */

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY");

        String clientRegion = "THE_AWS_REGION";
        String roleARN = "THE_ROLE_ARN";
        String roleSessionName = "THE_ROLE_SESSION_NAME";
        String bucketName = "THE_BUCKET_NAME";
        String file = "THE_FILE_TO_UPLOAD";

        String email = "YOUR_EMAIL";
        String password = "YOUR_PASSWORD";
        String clientId = "THE_CLIENT_ID";
        String endpoint = "END_POINT";

        try
        {
            // Creating the STS client is part of your trusted code. It has
            // the security credentials you use to obtain temporary security credentials.

            AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                    .withRegion(clientRegion)
                    .build();


            // Assume the IAM role. Note that you cannot assume the role of an AWS root account;
            // Amazon S3 will deny access. You must use credentials for an IAM user or an IAM role.

            AssumeRoleRequest roleRequest = new AssumeRoleRequest()
                    .withRoleArn(roleARN)
                    .withRoleSessionName(roleSessionName);
            AssumeRoleResult assumeResult = stsClient.assumeRole(roleRequest);


            // Start a session.
            BasicSessionCredentials temporaryCredentials =
                    new BasicSessionCredentials(
                            assumeResult.getCredentials().getAccessKeyId(),
                            assumeResult.getCredentials().getSecretAccessKey(),
                            assumeResult.getCredentials().getSessionToken());

            /**
             * SECOND STEP: Get a caller identity, build an s3 client, list and put in the bucket a file.
             */

            // GET CALLER IDENTITY

            GetCallerIdentityRequest request = new GetCallerIdentityRequest()
                    .withRequestCredentialsProvider(new AWSStaticCredentialsProvider(temporaryCredentials));

            /**
             * The identity variable contains:
             * UserId;
             * Account;
             * Arn;
             */

            /**
             * identity is not used, but you can use it to get infos on the
             * user like: identity.getAccount()
             */
            GetCallerIdentityResult identity = stsClient.getCallerIdentity(request);


            // Provide temporary security credentials so that the Amazon S3 client
            // can send authenticated requests to Amazon S3. You create the client
            // using the basicSessionCredentials object.

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(temporaryCredentials))
                    .withRegion(clientRegion)
                    .build();


            // Verify that assuming the role worked and the permissions are set correctly
            // by getting a set of object keys from the bucket.

            /*** LIST OBJECTS ***/

            /**
             * The prefix is built in this way:
             * A_USER_LIST/A_USER/
             */

            ObjectListing objectListing = s3Client.listObjects(bucketName, "A_USER_LIST/A_USER" + "/");

            // JUST FOR DEBUG
            objectListing.getObjectSummaries().size();

            /*** PUT A FILE ***/

            File f = new File(file);

            /**
             * The key is built in this way:
             * The key: A_USER_LIST/A_USER/
             */

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "A_USER_LIST/A_USER" + "/" +f.getName(), f);
            putObjectRequest.setCannedAcl(CannedAccessControlList.BucketOwnerFullControl);


            PutObjectResult putObjectResult = s3Client.putObject(putObjectRequest);

            // JUST FOR DEBUG
            ObjectMetadata metadata = putObjectResult.getMetadata();

            // When you put an object on S3, it will be versioned by S3.
            System.out.println("Version id: " + metadata.getVersionId());

            /*** THIRD STEP ***/

            /***
             * Notify that the audience was uploaded.
             */

            DashboardCredentials credentials = new DashboardCredentials(email, password, Regions.US_EAST_1);

            DashboardAPI dashboardAPI = DashboardAPI.newBuilder()
                    .withClientId(clientId)
                    .withCredentials(credentials)
                    .withEndpoint(endpoint)
                    .build();

            List<String> s3Files = new ArrayList<>();
            s3Files.add("s3://BUCKET_NAME/partner=PARTNER_ID/A_FILE.csv");

            Map<String, Object> notificationMap = new HashMap<>();
            notificationMap.put("audience_id", "AN_AUDIENCE_ID");
            notificationMap.put("cookie_type", "A_COOKIE_TYPE");
            notificationMap.put("file_list", s3Files);

            DashboardPostRequest postRequest = new DashboardPostRequest()
                    .withData(notificationMap)
                    .withPath("/upload_notification");

            DashboardSingleResponse response = null;

            /**
             * Send the notify upload post request.
             */

            try
            {
                response = dashboardAPI.call(postRequest);
            }
            catch (DashboardMappingException | DashboardHttpException e)
            {
                e.printStackTrace();
                return;
            }

            Map<String, Object> bodyMap = null;
            try
            {
                bodyMap = response.getBody(Map.class);

            }
            catch (DashboardMappingException e)
            {
                e.printStackTrace();
                return;
            }

            System.out.println(response.getBody(Map.class));
        }
        catch(DashboardMappingException | SdkClientException e)
        {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }// Amazon S3 couldn't be contacted for a response, or the client
// couldn't parse the response from Amazon S3.


    }
}
