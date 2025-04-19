### Overview
- This application handles user management functionalities - user profile creation, updation.
- Updates User KYC and bank account details.
- Encrypts and decrypts PII(Personally Identifiable Information) data using AWS KMS.
- Prevents PII(Personally Identifiable Information) data from getting hacked.
- Saves User profile images, KYC documents in Amazon S3 bucket
- Authenticates and authorises users using otp sent to mobile number or email
- The generated OTP are stored in redis cache to authenticate and authorize the users
- Implementation of email service is done using SMTP server hosted in AWS and Spring email 
- Implementation of sending otp to mobile number is done using Twilio API 

### Technologies
- Java 17.0 or more
- Spring Boot
- MySQL
- AWS KMS
- Amazon S3
- Spring Data JPA
- AWS CLI
- Maven
- Spring email
- Twilio

### Creating an AWS KMS Key
-  KMS is a service provided by AWS to encrypt and decrypt PII data.
-  A single KMS key generates different encrypted values for the same data.
-  login to AWS console [https://signin.aws.amazon.com/signin] .
-  Create a AWS KMS Symmetric key. Symmetric key is the one which is used to encrypt and decrypt PII data.
-  Navigate to KMS and create a symmetric customer managed key using the below steps:
    1. Configure Key, select key type - symmetric,Key usage - encrypt and decrypt, Key material origin - KMS, Regionality - Single-region-key.
    2. Add labels and description for the key.
    3. Define key administrative permissions - Select IAM users and roles authorised to manage key via KMS API.
    4. Define key usage permissions - Select IAM users and roles authorised to use this key in cryptographic operations.
    5. Click on finish, AWS KMS Key is generated.
    6. Once the key is generated an ARN is available for the key, use it in code to encrypt and decrypt data.
    7. Please find the screenshots for your reference.

![Screenshot 2025-04-17 at 15 15 11](https://github.com/user-attachments/assets/20d51f6b-eecd-4950-8b79-b6cfcaa69f67)

![Screenshot 2025-04-17 at 15 16 40](https://github.com/user-attachments/assets/5cb61866-83d6-4680-b336-c759cb2643a6)

![Screenshot 2025-04-17 at 15 17 20](https://github.com/user-attachments/assets/c74e7724-d2d6-4285-a0d0-077214cf8291)

![Screenshot 2025-04-17 at 15 17 47](https://github.com/user-attachments/assets/2fd74f7c-1d7d-40a5-bc89-60a5ae51ed8b)

### Configure the IAM users
- Configure the access key and secret key of IAM users authorized to access the KMS key in local environment using AWS CLI.

### Create an Amazon S3 buckets
- Create two buckets with predefined values.
- "mtb-kyc-images" to store kyc images of users.
- "mtb-profile-images" to store profile images of users.
- restrict public access to s3 buckets.
- provide access for above buckets to IAM users who have access to the application.

### Create database tables
- Create a MySQL database
- Execute the provided SQL scripts
- Update the datasource related attributes in application.properties file
### API Documentation
- Run the application and access the below url to view the API documentation:
    - http://localhost:8080/swagger-ui/index.html
