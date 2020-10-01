package AWS;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import static AWS.AWSConstants.*;


public class Users {

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static int validate(String email, String pass, HttpSession session) {
        int status = 0;
        try {
            Connection con = ConnectionProvider.getCon();

            PreparedStatement ps = con.prepareStatement("select * from users where email=? and pass=?");
            ps.setString(1, email);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                status = 1;

                if (!rs.getBoolean(5)) {
                    sendVerify(email, pass);
                    return -1;
                }
            }

            session.setAttribute("uname", rs.getString(4));
            session.setAttribute("uid", rs.getInt(1));
            session.setAttribute("url", rs.getString(6));
            session.setAttribute("session", "TRUE");

        } catch (Exception ignored) {
        }
        return status;
    }

    public static boolean signUp(String email, String pass) {
        boolean status = false;

        if (exist(email))
            return false;

        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (uid, email, pass, uname, verified, url, bio) VALUES (null,?,?,?,false,?,?)");
            pass = getMd5(pass);
            ps.setString(1, email);
            ps.setString(2, pass);
            ps.setString(3, "User#" + uidIndex());
            ps.setString(4, "http://d3ci5xc1bwzcds.cloudfront.net/default/default.jpg");
            ps.setString(5, " ");
            ps.executeUpdate();
            status = true;
            sendVerify(email, pass);

            /* £hour timeout for unverified users */
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!checkVerification(email))
                        delete(email);
                }
            }, 10800000);

        } catch (Exception ignored) {
        }
        return status;
    }

    private static boolean checkVerification(String email) { //signup username

        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select verified from users where email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getBoolean(1))
                    return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }


    public static boolean verify(String email, String pass) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("UPDATE users SET verified = true where email =? AND pass = ?");
            ps.setString(1, email);
            ps.setString(2, pass);
            ps.executeUpdate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean exist(String email) {
        boolean status = false;
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("select * from users where email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            status = rs.next();

        } catch (Exception e) {
        }
        return status;
    }

    private static void delete(String email) {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();
            s.executeUpdate("DELETE FROM users WHERE email = '" + email + "'");
        } catch (Exception e) {
        }
    }

    private static int uidIndex() { //signup username
        int max = 0;
        try {
            Connection con = ConnectionProvider.getCon();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("Select uid from users");

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


    public static ResultSet users() {
        ResultSet rs = null;
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("select uid, uname, url from users where verified = true");
            rs = ps.executeQuery();
        } catch (Exception ignored) {
        }
        return rs;
    }

    public static ResultSet user(String name) {
        ResultSet rs = null;
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("select uid, uname, url from users where uname LIKE ? and verified = true");
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

        } catch (Exception ignored) {
        }
        return rs;
    }

    public static String user(int id) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("select uname from users where uid =? and verified = true");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            rs.next();
            return rs.getString(1);

        } catch (Exception ignored) {
        }
        return null;
    }


    public static ResultSet profile(int uid) {
        ResultSet rs = null;
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select uname, bio, url from users WHERE uid = ?");
            ps.setInt(1, uid);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


    private static void sendVerify(String to, String md5) {
        String subject = "SOC: Email Verification";
        String body = "Sharing On the Cloud: \n " +
                "You must verify your email address to access you account within 3 hours of signing up\n " +
                "You can do this by following this url: " + confirmationURL + "?id=" + to + "&hash=" + md5 + " \n" +
                "Otherwise you may have to sign up again";

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", HOST);

        Session session = Session.getDefaultInstance(props);

        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(FROM, FROMNAME));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");

            Transport transport = session.getTransport();
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


