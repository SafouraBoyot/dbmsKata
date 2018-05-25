import infrastructure.db.postgres.PostgresConnectionPool;
import org.junit.Test;
import skateshop.category.Category;
import skateshop.category.SQLCategoryRepository;
import skateshop.skate.SQLSkateRepository;
import skateshop.skate.Skate;

import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SkateShopShould {
    @Test
    public void
    create_a_skate_category_in_db() throws SQLException {
        Category category = new Category("Inline skate");
        PostgresConnectionPool connectionPool = new PostgresConnectionPool("skateshop");
        SQLCategoryRepository categoryRepository = new SQLCategoryRepository(connectionPool);

//        categoryRepository.save(category);

        assertThat(categoryRepository.getBy("Inline skate"), is(equalTo(Optional.of(category))));
    }

    @Test public void
    create_a_skate_in_db(){
        Skate skate = new Skate("K2 Inline",new Category("Inline"), 10);
        SQLSkateRepository sqlSkateRepository = new SQLSkateRepository(new PostgresConnectionPool("skateshop") );

        sqlSkateRepository.save(skate);

        assertThat(sqlSkateRepository.getBy("K2 Inline"), is(equalTo(skate)));

    }

}
