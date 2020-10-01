package AWS;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.util.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static AWS.AWSConstants.BUCKETNAME;
import static AWS.AWSConstants.credentials;

public class Rekognition {

    static AmazonRekognitionClient rekognitionClient = new AmazonRekognitionClient(credentials).withRegion(Region.getRegion(Regions.EU_WEST_2));

    public static String detectText(String fileName) {
        String filePath = (new File("").getAbsolutePath()).replace("\\", "/");
        filePath = filePath.replace("/bin", "/webapps/SOC/AWS/");
        String txt = filePath + "Banned words.txt";

        ByteBuffer imageBytes = imageBuffer(fileName);

        DetectTextRequest request = new DetectTextRequest()
                .withImage(new Image()
                        .withBytes(imageBytes));

        StringBuilder detectedText = new StringBuilder();
        StringBuilder bannedText = new StringBuilder();
        String r = "";
        try {
            DetectTextResult result = rekognitionClient.detectText(request);
            List<TextDetection> textDetections = result.getTextDetections();

            for (TextDetection text : textDetections) {
                detectedText.append(text.getDetectedText());
                detectedText.append(" ");
            }

            try (BufferedReader br = new BufferedReader(new FileReader(txt))) {
                String line;

                while ((line = br.readLine()) != null) {

                    if (detectedText.toString().toLowerCase().contains(line)) {
                        bannedText.append(line);
                        bannedText.append(", ");
                    }

                }
                if (bannedText.length() > 0) {
                    r = bannedText.substring(0, bannedText.length() - 2);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }


    public static ArrayList<String> detectLabels(String fileName, int amount) throws IOException {
        ByteBuffer imageBytes = imageBuffer(fileName);
        ArrayList<String> results = new ArrayList<>();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMaxLabels(amount);

        return getLabels(results, rekognitionClient, request);
    }

    public static ArrayList<String> detectLabelsS3(String photo, int amount) {

        ArrayList<String> results = new ArrayList<>();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new S3Object()
                                .withName(photo).withBucket(BUCKETNAME)))
                .withMaxLabels(amount);

        return getLabels(results, rekognitionClient, request);
    }

    private static ArrayList<String> getLabels(ArrayList<String> results, AmazonRekognitionClient rekognitionClient, DetectLabelsRequest request) {
        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();


            for (Label label : labels) {
                results.add(label.getName());
            }
        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static String[] top3(int uid) {

        HashMap<String, Integer> hm = new HashMap<>();
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("select categories from photos where uid = ?");
            ps.setInt(1, uid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String cats = rs.getString(1);
                String[] cat = cats.split(" ");
                for (String label : cat) {
                    if (label.length() > 0)
                        if (!hm.containsKey(label)) {
                            hm.put(label, 1);
                        } else {
                            int count = hm.get(label);
                            count++;
                            hm.replace(label, count);
                        }
                }
            }
            Set<Map.Entry<String, Integer>> set = hm.entrySet();
            List<Map.Entry<String, Integer>> list = new ArrayList<>(
                    set);
            list.sort(new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            String[] results = new String[3];
            int i = 0;
            for (Map.Entry<String, Integer> entry : list) {
                if (i == 3)
                    break;
                results[i] = entry.getKey();
                i++;
            }
            return results;


        } catch (SQLException ignored) {
        }
        return null;
    }

    public static ByteBuffer imageBuffer(String fileName) {
        String filePath = (new File("").getAbsolutePath()).replace("\\", "/");
        filePath = filePath.replace("/bin", "/webapps/SOC/upload/");
        String photo = filePath + fileName;

        ByteBuffer imageBytes = null;
        try (InputStream inputStream = new FileInputStream(new File(photo))) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        } catch (Exception e) {
        }
        return imageBytes;
    }

    public static String detectModeration(String fileName) {

        ByteBuffer imageBytes = imageBuffer(fileName);
        DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMinConfidence(60F);
        StringBuilder sb = new StringBuilder();
        String r = "";
        try {
            DetectModerationLabelsResult result = rekognitionClient.detectModerationLabels(request);
            List<ModerationLabel> labels = result.getModerationLabels();
            if (labels.size() > 0) {
                for (ModerationLabel label : labels) {
                    sb.append(label.getName());
                    sb.append(", ");
                }
                r = sb.substring(0, sb.length() - 2);
            }
        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }
        return r;
    }


}
