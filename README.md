
# dashboard-api-sdk
This is a Java Library to interact with the [audiencerate.com](http://www.audiencerate.com/) Dashboard REST API.

# About Audiencerate
[AudienceRate Ltd.](https://apidocs.audiencerate.com/) provides online advertising platform products and services.

# Documentation
The full API is documented in swagger format here: https://apidocs.audiencerate.com/


# Authentication
It's required to have:

- an AWS IAM user [Access Keys](https://docs.aws.amazon.com/en_us/general/latest/gr/aws-sec-cred-types.html#access-keys-and-secret-access-keys)
- a valid Dashboard User with email and password


# Gradle Dependency

Add it in your root build.gradle at the end of repositories:

```gradle
   allprojects {
      repositories {
         ...
         maven { url 'https://jitpack.io' }
      }
   }
```

Step 2. Add the dependency
```gradle
   dependencies {
         implementation 'com.github.audiencerate:dashboard-api-sdk:VERSION'
   }
```

Follow instruction for other package managers from: https://jitpack.io/#audiencerate/dashboard-api-sdk


# Examples 
Under **com.audiencerate.dashboard.sdk.api.examples** there are two example applications:

#### ConsoleExampleApplication
A simple console application that call a GET Endpoint to fetch Audiences.

#### ConsoleExampleUploader
An example on how to upload an audience file in the dashboard via API.

# License
This project is released under MIT License. See [license](LICENSE) file for more detailed information.

