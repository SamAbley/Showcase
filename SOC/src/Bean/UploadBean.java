package Bean;

public class UploadBean {
    private String file, pname, description, purl;
    private boolean pub;
    private int amount, strength;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String name) {
        this.pname = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public boolean isPub() {
        return pub;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}

