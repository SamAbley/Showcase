package AWS;

import java.sql.*;

public class Photo {


    public static ResultSet publicPhotos(int image, String date) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("select photos.url, users.uname, photos.name, photos.description, photos.categories, photos.date, photos.uid, users.url from photos, users where public = 1 and photos.date <= ? and photos.uid = users.uid ORDER BY date DESC Limit ?, ?");
            ps.setString(1, date);
            ps.setInt(2, image);
            ps.setInt(3, 10);

            return ps.executeQuery();

        } catch (SQLException ignored) {
        }
        return null;
    }

    public static ResultSet searchPhoto(String n, int image) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select photos.url, users.uname, photos.name, photos.description, photos.categories, photos.date, photos.uid, users.url from photos, users where public = 1 AND photos.uid = users.uid AND photos.name LIKE ? ORDER BY date DESC Limit ?, ?");
            ps.setString(1, "%" + n + "%");
            ps.setInt(2, image);
            ps.setInt(3, 10);

            return ps.executeQuery();

        } catch (SQLException ignored) {
        }
        return null;
    }

    public static ResultSet searchCreator(String n, int image) {
        try {

            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select photos.url, users.uname, photos.name, photos.description, photos.categories, photos.date, photos.uid, users.url from photos, users where public = 1 AND photos.uid = users.uid AND users.uname LIKE ? ORDER BY date DESC Limit ?, ?");
            ps.setString(1, "%" + n + "%");
            ps.setInt(2, image);
            ps.setInt(3, 10);
            return ps.executeQuery();

        } catch (SQLException ignored) {
        }
        return null;
    }

    public static ResultSet searchCategory(String n, int image) {
        try {

            Connection con = ConnectionProvider.getCon();

            PreparedStatement ps = con.prepareStatement("Select photos.url, users.uname, photos.name, photos.description, photos.categories, photos.date, photos.uid, users.url from photos, users where public = 1 AND photos.uid = users.uid AND photos.categories LIKE ? ORDER BY date DESC Limit ?, ?");
            ps.setString(1, "%" + n + "%");
            ps.setInt(2, image);
            ps.setInt(3, 10);
            return ps.executeQuery();

        } catch (SQLException ignored) {
        }
        return null;
    }

    public static ResultSet profile(int uid) {

        try {

            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select url, pid, name, description, categories, date, public from photos where uid =? ORDER BY date DESC");
            ps.setInt(1, uid);
            return ps.executeQuery();

        } catch (SQLException ignored) {
        }
        return null;
    }

    public static ResultSet profilePub(int uid) {

        try {

            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select url, uid, name, description, categories, date from photos where uid =? AND public = 1 ORDER BY date DESC");
            ps.setInt(1, uid);
            return ps.executeQuery();

        } catch (SQLException ignored) {
        }
        return null;
    }

    public static ResultSet editPhoto(int pid) {
        try {

            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select url, pid, name, description, categories, public from photos where pid =?");
            ps.setInt(1, pid);
            return ps.executeQuery();

        } catch (SQLException ignored) {
        }
        return null;
    }


}
