package infrastructure.db.postgres;

import org.apache.commons.dbcp2.BasicDataSource;

public class PostgresConnectionPool extends BasicDataSource {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/%s";

    public PostgresConnectionPool(String dbName) {
        super();
        String fullURL = String.format(URL, dbName);
        this.setDriverClassName("org.postgresql.Driver");
        this.setUrl(fullURL);
        this.setUsername("postgres");
        this.setPassword("postgres");
        this.setInitialSize(1);
        this.setMaxTotal(5);
    }
}
