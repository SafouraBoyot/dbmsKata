package skateshop.skate;

import infrastructure.db.postgres.PostgresConnectionPool;
import skateshop.category.Category;
import skateshop.category.SQLCategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class SQLSkateRepository {
    private final PostgresConnectionPool postgresConnectionPool;

    private final static String INSERT_SKATE = "INSERT INTO skates (name , category , stock) VALUES (?, ?, ?)";
    private final static String SELECT_SKATE_BY_NAME = "SELECT type FROM category where type = ? ";


    public SQLSkateRepository(PostgresConnectionPool postgresConnectionPool) {
        this.postgresConnectionPool = postgresConnectionPool;
    }


    public void save(Skate skate) {

        try (Connection connection = postgresConnectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SKATE);
            preparedStatement.setString(1, skate.getName());
            try {
                connection.setAutoCommit(false);

                SQLCategoryRepository sqlCategoryRepository = new SQLCategoryRepository(postgresConnectionPool);
                Optional<Category> optionalCategory = sqlCategoryRepository.getBy(skate.getCategory().getType());

                if (optionalCategory==null) {
                    throw new Exception("Category Does Not Exist!!!");
                }
                preparedStatement.setString(2, optionalCategory.get().getType());
                preparedStatement.setInt(3, skate.getStock());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }


        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
    public Skate getBy(String name) {

        return new Skate("K2 Inline", new Category("Inline skate"), 10);
    }
}
