package AWS;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.model.CreateInvalidationRequest;
import com.amazonaws.services.cloudfront.model.InvalidationBatch;
import com.amazonaws.services.cloudfront.model.Paths;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import static AWS.AWSConstants.*;

public class Upload {

    public static boolean upload(String file, int uid, String name, String description, String categories, boolean pub, String date) {
        int pid = fileIndex();
        String stringObjKeyName = uid + "/photo" + pid;
        try {
            AmazonS3 s3Client = new AmazonS3Client(credentials);
            s3Client.setEndpoint(ENDPOINT);


            PutObjectRequest request = new PutObjectRequest(BUCKETNAME, stringObjKeyName, new File(file));
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType("image/jpeg");
            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            request.setMetadata(metadata);

            dbUpdate(pid, stringObjKeyName, uid, name, description, categories, pub, date);
            s3Client.putObject(request);
            cloudFrontReset(stringObjKeyName);
        } catch (SdkClientException e) {
            return false;
        }
        return true;
    }

    public static String uploadMes(String file, int uid) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String stringObjKeyName = uid + "/messages/photo" + timeStamp;
        try {
            AmazonS3 s3Client = new AmazonS3Client(credentials);
            s3Client.setEndpoint(ENDPOINT);


            PutObjectRequest request = new PutObjectRequest(BUCKETNAME, stringObjKeyName, new File(file));
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType("image/jpeg");
            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            request.setMetadata(metadata);


            s3Client.putObject(request);
            cloudFrontReset(stringObjKeyName);
        } catch (SdkClientException ignored) {

        }
        return CloudFrontURL + stringObjKeyName;
    }


    public static boolean uploadProfile(String file, int uid) {
        String stringObjKeyName = uid + "/profile" + uid;
        try {
            AmazonS3 s3Client = new AmazonS3Client(credentials);
            s3Client.setEndpoint(ENDPOINT);

            PutObjectRequest request = new PutObjectRequest(BUCKETNAME, stringObjKeyName, new File(file));
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType("image/jpeg");
            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            request.setMetadata(metadata);

            String url = CloudFrontURL + stringObjKeyName;
            dbProfilePicture(uid, url);
            s3Client.putObject(request);
            cloudFrontReset(stringObjKeyName);
        } catch (SdkClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void dbProfilePicture(int uid, String url) {
        try {

            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();

            s.executeUpdate("UPDATE users SET url = '" + url + "' WHERE uid =" + uid);

        } catch (SQLException ignored) {
        }
    }

    public static void dbProfile(int uid, String name, String bio) {
        try {

            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();

            s.executeUpdate("UPDATE users SET uname = '" + name + "', bio ='" + bio + "' WHERE uid =" + uid);

        } catch (SQLException ignored) {
        }
    }

    private static synchronized int fileIndex() {
        int max = 0;
        try {
            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("Select pid from photos");

            while (rs.next()) {
                int id = rs.getInt(1);
                if (max < id)
                    max = id;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return max + 1;
    }


    private static void dbUpdate(int pid, String fn, int uid, String name, String description, String categories, boolean pub, String date) {
        try {

            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();
            s.executeUpdate("INSERT INTO photos VALUES (" + pid + ",'" + CloudFrontURL + fn + "', " + uid + ", '" + name + "', '" + description + "', '" + categories + "', " + pub + ", '" + date + "')");

        } catch (SQLException ignored) {
        }
    }

    public static void dbEdit(int pid, String name, String description, String categories, boolean pub) {
        try {

            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();

            s.executeUpdate("UPDATE photos SET name = '" + name + "', description = '" + description + "', categories = '" + categories + "', public = " + pub + " WHERE pid =" + pid);

        } catch (SQLException ignored) {
        }
    }

    public static void dbDelete(int pid, int uid) {
        AmazonS3 s3Client = null;
        try {
            String stringObjKeyName = uid + "/photo" + pid;
            s3Client = new AmazonS3Client(credentials);
            s3Client.setEndpoint(ENDPOINT);
            DeleteObjectRequest request = new DeleteObjectRequest(BUCKETNAME, stringObjKeyName);
            s3Client.deleteObject(request);
            cloudFrontReset(stringObjKeyName);

            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();
            s.executeUpdate("DELETE FROM photos WHERE pid =" + pid);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cloudFrontReset(String stringObjKeyName) {
        AmazonCloudFront client = new AmazonCloudFrontClient(credentials);
        Paths invalidation_paths = new Paths().withItems("/" + stringObjKeyName).withQuantity(1);
        InvalidationBatch invalidation_batch = new InvalidationBatch(invalidation_paths, new Date().toString());
        CreateInvalidationRequest invalidation = new CreateInvalidationRequest(distributionId, invalidation_batch);
        client.createInvalidation(invalidation);
    }

    public static String removeIllegal(String n) {
        if (n == null)
            return "";
        n = n.replace("\"", "");
        n = n.replace("'", "");
        return n;
    }


    public static void JavaImageResizer(File input, int max) throws IOException {

        BufferedImage img = ImageIO.read(input);
        int height = img.getHeight();
        int width = img.getWidth();

        if (height > max || width > max) {
            if (width < height) {
                float ans = ((float) max) / height;
                float widthDec = width * ans;
                width = (int) widthDec;
                height = max;
            } else if (height < width) {
                float ans = ((float) max) / width;
                float heightDec = height * ans;
                height = (int) heightDec;
                width = max;
            } else {
                height = max;
                width = max;
            }

            BufferedImage tempJPG = resizeImage(img, width, height); //width and height needs to be calculated with algorithm
            ImageIO.write(tempJPG, "jpg", input);
        }
    }

    public static BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);

        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }
}
