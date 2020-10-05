# SOC: Sharing Over the Cloud web app
**If you want to view the output of this poject then go to [SOC](http://ec2-3-8-195-243.eu-west-2.compute.amazonaws.com/SOC/)**

This project is a web app that utilizes the cloud. This web app allows users to sign up, sign in, share photos and communicate with one another. The cloud services that the app uses are provided by Amazon web service (AWS). Services include:
 - EC2 for platform hosting
 - RDS for database storage
 - S3 for file storage 
 - CloudFront for data privacy 
 - Rekognition for machine learning
 
## Sign Up
Users have to sign up to share their images. They need to insert a valid email and password (over 8 characters and include a number) to sign up to this app. When signin up users will have to pass a verification test via Googles Recaptcha V2. This is to stop any bots or spam sign ups. The databse is also protected from SQL injection attacks by using prepared statments. Once they have signed up, users will recieve an email verification that they wuill have to confirm before rthey are aloud to access thier account.

## Sign In
Once users have signed up, they will then have access to their own profile which can be viewed by others. The profile has a name, a profile picture and a bio that the user can customise to make it more personal. From this page, the user now can access uploading photos and instant messaging.

## Uploading Images
The users can upload any images from their own machine. If they are above 5mb then they will be compressed to bellow that value to save storage. Once the image file ahs been chosen, it then appears as a preview to the user so they can see what it looks like. Users can then add attributes to the photo like a name, description, categories and whether they are public or not. These attributes can help other users find these images through the search engine. Their are restriction on what images are allowed to be uploaded. Machine learning is implmented to extract festures and words from the images so that they can be checked against a list of banned features and words. This feture stops offensive images and memes from filling the site.

## Instant Messaging


### Notes
Files that include components like security keys for AWS and Google have been removed to stop any security threats. This means that the files will not work on a local server if downloaded. These files have been included so that the working of the app can be viewed. 

*All code was produced, maintianed and belongs to Sam Abley. The only additional code from third parties are the AWS API's*

This file is still currently being updated
