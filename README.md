
This is an open source Java SDK to access with AWS Cognito to our Dashboard APIs.

Look at the ConsoleExampleApplication inside the project for a tutorial.

Report bugs here on GitHub using the Issue system please.


========================================================

**How to upload files**

Under _**com.audiencerate.dashboard.sdk.api.console**_, there is a class called:

_**ConsoleExampleUploader**_

This class is an example on how to upload file.

The access key and the a secret key are required to build a token request.

Acttually we use a token to get temporary security credentials, that is
required to assume a role.

When you got the token, is possible to assume a role and start a session. 
Look at the _FIRST STEP_ paragraph inside the code to have a better idea.

At this point you should have a session started; is here that the **_SECOND STEP_** starts.

In the second step you will put and list s3 objects inside a bucket, building the
s3 client.

This is quite a relative simple procedure.

What you need to do is paying attention on how the prefix and the key are built
when you want to _LIST_ or _PUT_ objects in th bucket
Look inside the code, they are quite commented.

In the _**THIRD STEP**_ you will notify, with a post request using our API
/upload_notification

Look at the **_THIRD STEP_** inside the code for the comments, they are self explained.

=====================================================

**How to Cognito login using curl**

Build a json (In this example we call the json: auth.json):

{
   "AuthParameters" : {
      "USERNAME" : "user_mail@example.com",
      "PASSWORD" : "mysecret"
   },
   "AuthFlow" : "USER_PASSWORD_AUTH",
   "ClientId" : "123456789abcdefghilmn"
}

In the client id _key_ put your client id _value_, the one in the example will not work.

bash command:

[user$] curl -X POST --data @auth.json -H 'X-Amz-Target: AWSCognitoIdentityProviderService.InitiateAuth' -H 'Content-Type: application/x-amz-json-1.1' https://cognito-idp.us-east-1.amazonaws.com/ > authResponse.json

Look at the authResponse.json file to get the tokens.

Note that the response json will have no indentation.

=====================================================================

**_Create an Audience using curl_**

Build a json file (post.json):

{
    "audience_name": "AnAudienceName"
}

curl 'https://THE.ENDPOINT.WEPROVIDE' -H 'Authentication: YOUR_TOKEN_ID' -H 'Content-Type: application/json;charset=UTF-8' --data @post.json --compressed


Have fun! 
