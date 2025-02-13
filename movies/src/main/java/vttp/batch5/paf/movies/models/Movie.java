package vttp.batch5.paf.movies.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.json.JsonObject;

public class Movie {
    private String imdbId;

    private float voteAverage;

    private int voteCount;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date releaseDate;

    private double revenue;

    private double budget;

    private int runtime;

    public static Movie convertJson(JsonObject obj) {
        Movie m = new Movie();
        m.setImdbId(obj.getString("imdb_id", ""));
        float voteAvg;
        try {
            voteAvg = obj.getJsonNumber("vote_average").numberValue().floatValue();
        } catch (Exception e) {
            voteAvg = 0;
        }
        m.setVoteAverage(voteAvg);
        m.setVoteCount(obj.getInt("vote_count", 0));
        Date release;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            release = sdf.parse(obj.getString("release_date", ""));
        } catch (Exception e) {
            release = new Date();
        }
        m.setReleaseDate(release);
        m.setRevenue(doubleErrHandler(obj, "revenue"));
        m.setBudget(doubleErrHandler(obj, "budget"));
        m.setRuntime(obj.getInt("runtime", 0));

        return m;
    }

    private static double doubleErrHandler(JsonObject obj, String key) {
        try {
            return obj.getJsonNumber(key).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    
}
