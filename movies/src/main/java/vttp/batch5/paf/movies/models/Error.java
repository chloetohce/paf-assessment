package vttp.batch5.paf.movies.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Error {
    private String[] ids;

    private String error;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public Error() {
    }

    public Error(String[] ids, String error, Date date) {
        this.ids = ids;
        this.error = error;
        this.date = date;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
}
