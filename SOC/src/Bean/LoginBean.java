package Bean;

public class LoginBean {
    private String email, pass, name, signup;
    private int uid;
    private int p;
    private boolean verified = false;


    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSignup() {
        return signup;
    }

    public void setSignup(String signup) {
        this.signup = signup;
    }


    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
