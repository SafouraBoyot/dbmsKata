package infrastructure.db.postgres;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Properties;

public class PostgresConnectionPool extends BasicDataSource {
    private static final String URL = "jdbc:postgresql://%s:%s/%s";

    public PostgresConnectionPool(Properties properties) {
        super();

        String fullURL = String.format(URL,
                properties.getProperty("host", "127.0.0.1"),
                properties.getProperty("port", "5432"),
                properties.getProperty("dbName"));
        this.setDriverClassName("org.postgresql.Driver");
        this.setUrl(fullURL);
        this.setUsername(properties.getProperty("username"));
        this.setPassword(properties.getProperty("password"));
        this.setInitialSize(Integer.parseInt(properties.getProperty("initialConnectionPoolSize", "1")));
        this.setMaxTotal(Integer.parseInt(properties.getProperty("maxConnectionPoolSize", "5")));
    }
}
