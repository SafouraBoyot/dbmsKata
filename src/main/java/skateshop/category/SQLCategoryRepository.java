package skateshop.category;

import infrastructure.db.postgres.PostgresConnectionPool;

import java.sql.*;
import java.util.Optional;

public class SQLCategoryRepository {
    private final static String SELECT_CATEGORY_BY_NAME = "SELECT type FROM category where type = ? ";
    private final static String INSERT_CATEGORY = "INSERT INTO category (type) VALUES (?)";
    private final PostgresConnectionPool postgresConnectionPool;

    public SQLCategoryRepository(PostgresConnectionPool postgresConnectionPool){
        this.postgresConnectionPool = postgresConnectionPool;
    }

    public int saveWithConnection(Category category, Connection connection) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY);
            preparedStatement.setString(1,category.getType());
            return preparedStatement.executeUpdate();
    }

    public int save(Category category) {
        try (Connection connection = postgresConnectionPool.getConnection()){
            return saveWithConnection(category, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Optional<Category> getBy(String type) {


        try (Connection connection = postgresConnectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY_BY_NAME);
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Category(resultSet.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

