package Bean;

import java.sql.ResultSet;

public class PageBean {
    private ResultSet rs;
    private int page = 1;
    private int image, cat;
    private String date, query;

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public ResultSet getRs() {
        return rs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getCat() {
        return cat;
    }

    public void setCat(int cat) {
        this.cat = cat;
    }
}
