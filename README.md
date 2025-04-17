### Overview
- This application handles user management functionalities - user profile creation, updation.
- Updates User KYC and bank account details.
- Encrypts and decrypts PII(Personally Identifiable Information) data using AWS KMS.
- Prevents PII(Personally Identifiable Information) data from getting hacked.
- Saves User profile images, KYC documents

## Technologies
- Java 17.0 or more
- Spring Boot
- MySQL
- AWS KMS
- Spring Data JPA
- AWS CLI
- Maven

## Creating an AWS KMS Key
-  KMS is a service provided by AWS to encrypt and decrypt PII data.
-  A single KMS key generates different encrypted values for the same data.
-  login to AWS console [https://signin.aws.amazon.com/signin]
-  Create a AWS KMS Symmetric key. Symmetric key is the one which is used to encrypt and decrypt PII data
-  Naivgate to KMS and create a symmetric customer managed key using the below steps:

  
