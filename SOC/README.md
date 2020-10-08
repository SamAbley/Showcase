# SOC: Sharing Over the Cloud web app
**If you want to view the output of this project then go to [SOC](http://ec2-3-9-176-86.eu-west-2.compute.amazonaws.com/SOC/)**

This project is a web app that utilizes the cloud. This web app allows users to sign up, sign in, share photos and communicate with one another. The cloud services that the app uses are provided by Amazon web service (AWS). Services include:
 - EC2 for platform hosting
 - RDS for database storage
 - S3 for file storage 
 - CloudFront for data privacy 
 - Rekognition for machine learning
 
## Sign Up
Users have to sign up to share their images. They need to insert a valid email and password (over 8 characters and include a number) to sign up to this app. When signing up, users will have to pass a verification test via Googles Recaptcha V2. This is to stop any bots or spam sign-ups. The database is also protected from SQL injection attacks by using prepared statements. Once they have signed up, users will receive an email verification that they will have to confirm before they are allowed to access their account.

## Sign In
Once users have signed up, they will then have access to their profile which can be viewed by others. The profile has a name, a profile picture and a bio that the user can customise to make it more personal. From this page, the user now can access uploading photos and instant messaging. They can also use the search bar to search for different categories, profile or image names. Another feature on the profile is the users top categories. The top 3 categories they the user has shared images of appears next to their bio to give other users a quick overview of the types of images that that user shares.

## Uploading Images
The users can upload any images from their machine. If they are above 5mb then they will be compressed to bellow that value to save storage. Once the image file has been chosen, it then appears as a preview to the user so they can see what it looks like. Users can then add attributes to the photo like a name, description, categories and whether they are public or not. These attributes can help other users find these images through the search engine. The categories can be automatically generated through the machine learning feature. It extracts feature from the image and sets them as categories which allow the image to be group with others in the same category. There is a restriction on what images are allowed to be uploaded. Machine learning is implemented to extract features and words from the images so that they can be checked against a list of banned features and words. This feature stops offensive images and memes from filling the site.

## Instant Messaging
Users can communicate with one another using the instant messaging feature. Users can send text and image messages to each other. Unread messages produce a red notification bubble that shows the user how many unread messages are in their inbox. This notification will disappear once the message has been read. Users can see who else is online via the green bubble that appears next to a users name when they log in; it disappears when they log off. There is a search engine so users can find other users based on their names.


### Notes
Files that include components like security keys for AWS and Google have been removed to stop any security threats. This means that the files will not work on a local server if downloaded. These files have been included so that the working of the app can be viewed. 

*All code was produced, maintained and belongs to Sam Abley. The only additional code from third parties are the AWS API's*
