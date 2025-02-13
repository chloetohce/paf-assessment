package vttp.batch5.paf.movies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static final String DATABASE = "movies";

    public static final String COL_IMDB = "imdb";

    public static final String COL_ERRORS = "errors";

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public static final String DATE_FILTER = "2018-01-01";

    public static boolean afterFilterDate(Date d) {
        try {
            return !d.before(SDF.parse(DATE_FILTER));
        } catch (ParseException e) {
            return false;
        }
    }


}
