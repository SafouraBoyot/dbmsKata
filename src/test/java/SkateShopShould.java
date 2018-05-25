import infrastructure.db.LoadProperties;
import infrastructure.db.postgres.PostgresConnectionPool;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import skateshop.category.Category;
import skateshop.category.SQLCategoryRepository;
import skateshop.skate.SQLSkateRepository;
import skateshop.skate.Skate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SkateShopShould {


    public static final String SKATESHOP_DB_NAME = "skateshop";
    public static final String INLINE_SKATE_CATEGORY = "Inline skate";
    public static final String INLINE_CATEGORY = "Inline";
    public static final String K2_SKATE_MODEL = "K2 Inline";
    public static final String OTHER_CATEGORY = "Inline 2";
    public static final String FILE_PATH = "db/configuration/postgres.properties";
    private PostgresConnectionPool connectionPool;
    private SQLSkateRepository sqlSkateRepository;
    private SQLCategoryRepository sqlCategoryRepository;

    @Before
    public void
    setUp() {
        connectionPool = new PostgresConnectionPool(LoadProperties.getProperties(FILE_PATH));
        truncate_tables();
        sqlSkateRepository = new SQLSkateRepository(connectionPool);
        sqlCategoryRepository = new SQLCategoryRepository(connectionPool);
    }


    private static void truncate_tables() {

        PostgresConnectionPool connectionPool = new PostgresConnectionPool(LoadProperties.getProperties(FILE_PATH));
        try (Connection connection = connectionPool.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement truncateSkatesPreparedStatement =
                         connection.prepareStatement("DELETE FROM skates");
                 PreparedStatement truncateCategoryPrepapredStatement =
                         connection.prepareStatement("DELETE FROM category")) {
                truncateSkatesPreparedStatement.execute();
                truncateCategoryPrepapredStatement.execute();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void
    create_a_skate_category_in_db() throws SQLException {
        Category category = new Category(INLINE_SKATE_CATEGORY);

        sqlCategoryRepository.save(category);

        assertThat(sqlCategoryRepository.getBy(INLINE_SKATE_CATEGORY), is(equalTo(Optional.of(category))));
    }

    @Test
    public void
    create_a_skate_in_db() {
        Skate skate = new Skate(K2_SKATE_MODEL, new Category(INLINE_CATEGORY), 10);

        sqlSkateRepository.save(skate);
        Optional<Skate> expectedSkate = sqlSkateRepository.getBy(K2_SKATE_MODEL);

        assertTrue(expectedSkate.isPresent());
        assertThat(expectedSkate, is(equalTo(Optional.of(skate))));

    }

    @Test public void
    fail_to_save_skate_already_existing(){
        Skate skateOne = new Skate(K2_SKATE_MODEL, new Category(INLINE_CATEGORY), 10);
        Skate skateTwo = new Skate(K2_SKATE_MODEL, new Category(OTHER_CATEGORY), 10);
        sqlSkateRepository.save(skateOne);


        sqlSkateRepository.save(skateTwo);
        Optional<Category> inline2Category = sqlCategoryRepository.getBy(OTHER_CATEGORY);

        assertFalse(inline2Category.isPresent());
    }


    @Test public void
    update_skate(){
        Skate skate = new Skate(K2_SKATE_MODEL, new Category(INLINE_CATEGORY), 10);
        sqlSkateRepository.save(skate);

        sqlSkateRepository.update(new Skate(K2_SKATE_MODEL, new Category(INLINE_CATEGORY), 6));
        Optional<Skate> expectedSkate = sqlSkateRepository.getBy(K2_SKATE_MODEL);

        assertThat(expectedSkate.get().getStock(),is(6));

    }

    @Test public void
    delete_skate(){
        Skate skate = new Skate(K2_SKATE_MODEL, new Category(INLINE_CATEGORY), 10);
        sqlSkateRepository.save(skate);

        sqlSkateRepository.deleteBy(K2_SKATE_MODEL);
        Optional<Skate> expectedSkate = sqlSkateRepository.getBy(K2_SKATE_MODEL);

        assertFalse(expectedSkate.isPresent());
    }
    @AfterClass
    public static void cleanUp(){
        truncate_tables();
    }

}
