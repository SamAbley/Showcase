package AWS;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public interface AWSConstants {
    //IAM
    String ACCESSKEY = "AKIAVUQ2Z7HAKK7AASNC";
    String SECRETKEY = "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";
    AWSCredentials credentials = new BasicAWSCredentials(ACCESSKEY, SECRETKEY);

    //S3
    String BUCKETNAME = "pdb-users";

    //CLOUDFRONT
    String ENDPOINT = "https://s3.eu-west-2.amazonaws.com";
    String distributionId = "E1APBE5668GVL5";
    String CloudFrontURL = "http://d3ci5xc1bwzcds.cloudfront.net/";

    //SES
    String SMTP_USERNAME = "AKIAVUQ2Z7HAHVB2Z3PJ";
    String SMTP_PASSWORD = "BHFXZCXMafL4H2lwYkpc+1JpjUKY8w0sXhVZGJ96JAjo";
    String HOST = "email-smtp.eu-west-1.amazonaws.com";
    int PORT = 587;
    String FROM = "SharingOvertheCloud@gmail.com";
    String FROMNAME = "NO-REPLY";

    //RDS
    String DRIVER = "com.mysql.jdbc.Driver";
    String CONNECTION_URL = "jdbc:mysql://pb.clr9xdp7s8wn.eu-west-2.rds.amazonaws.com/pdb";
    String USERNAME = "admin";
    String PASSWORD = "7989Tkhr";
    String confirmationURL = "http://localhost/SOC/SignUp";
}
