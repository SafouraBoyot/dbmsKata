package skateshop.skate;

import infrastructure.db.postgres.PostgresConnectionPool;
import skateshop.category.Category;
import skateshop.category.SQLCategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SQLSkateRepository {
    private final PostgresConnectionPool postgresConnectionPool;

    private final static String INSERT_SKATE = "INSERT INTO skate (name , category , stock) VALUES (?, ?, ?)";
    private final static String SELECT_SKATE_BY_NAME = "SELECT name,category,stock FROM skate where name = ? ";
    private final static String UPDATE_SKATE_BY_NAME = "UPDATE skate set category = ?, stock = ? where name = ?";
    private final static String DELETE_SKATE_BY_NAME = "DELETE FROM skate where name = ?";


    public SQLSkateRepository(PostgresConnectionPool postgresConnectionPool) {
        this.postgresConnectionPool = postgresConnectionPool;
    }


    public void save(Skate skate) {

        try (Connection connection = postgresConnectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SKATE);
            preparedStatement.setString(1, skate.getName());
            SQLCategoryRepository sqlCategoryRepository = new SQLCategoryRepository(postgresConnectionPool);
            try {
                connection.setAutoCommit(false);
                Optional<Category> optionalCategory = sqlCategoryRepository.getBy(skate.getCategory().getType());

                if (!optionalCategory.isPresent()) {
                    sqlCategoryRepository.saveWithConnection(skate.getCategory(), connection);
                }

                preparedStatement.setString(2, skate.getCategory().getType());
                preparedStatement.setInt(3, skate.getStock());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                e.printStackTrace();
                connection.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Optional<Skate> getBy(String name) {
        try (Connection connection = postgresConnectionPool.getConnection()) {
            PreparedStatement selectSkatePreparedStaement = connection.prepareStatement(SELECT_SKATE_BY_NAME);
            selectSkatePreparedStaement.setString(1, name);
            ResultSet resultSet = selectSkatePreparedStaement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Skate(resultSet.getString("name"),
                        new Category(resultSet.getString("category")),
                        resultSet.getInt("stock")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

    public int update(Skate skate) {
        try (Connection connection = postgresConnectionPool.getConnection()) {

            PreparedStatement selectSkatePreparedStaement = connection.prepareStatement(UPDATE_SKATE_BY_NAME);
            selectSkatePreparedStaement.setString(1, skate.getCategory().getType());
            selectSkatePreparedStaement.setInt(2, skate.getStock());
            selectSkatePreparedStaement.setString(3, skate.getName());
            return selectSkatePreparedStaement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteBy(String name) {

        try (Connection connection = postgresConnectionPool.getConnection()) {

            PreparedStatement selectSkatePreparedStaement = connection.prepareStatement(DELETE_SKATE_BY_NAME);
            selectSkatePreparedStaement.setString(1, name);
            selectSkatePreparedStaement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
