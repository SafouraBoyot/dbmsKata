package skateshop.category;

import infrastructure.db.postgres.PostgresConnectionPool;

import java.sql.*;
import java.util.Properties;

public class SQLCategoryRepository implements CategoryRepository {
    private final static String SELECT_CATEGORY_BY_NAME = "SELECT type FROM category where type = ? ";
    private final static String INSERT_CATEGORY = "INSERT INTO category (type) VALUES (?)";
    private final PostgresConnectionPool postgresConnectionPool;

    public SQLCategoryRepository(PostgresConnectionPool postgresConnectionPool){
        this.postgresConnectionPool = postgresConnectionPool;
    }

    public int save(Category category) {
        try (Connection connection = postgresConnectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY);
            preparedStatement.setString(1,category.getType());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Category getBy(String type) {


        try (Connection connection = postgresConnectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY_BY_NAME);
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Category(resultSet.getString("type"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Connection createConnection(String url) throws SQLException {
        String user = "postgres";
        String password = "postgres";
        return DriverManager.getConnection(url, user, password);
    }


    }

