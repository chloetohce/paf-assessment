package vttp.batch5.paf.movies.utils;

public class SqlQuery {
    public static final String DATALOADED = "select count(*) as size from imdb;";

    public static final String INSERT_MOVIE = """
        insert into imdb(imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime)
        values(?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String REVENUE_BUDGET = """
            select sum(revenue) as total_revenue, sum(budget) as total_budget from imdb where imdb_id in (:listIds);
            """;
    
}
