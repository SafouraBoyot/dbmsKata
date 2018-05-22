import infrastructure.db.postgres.PostgresConnectionPool;
import org.junit.Test;
import skateshop.category.Category;
import skateshop.category.CategoryRepository;
import skateshop.category.SQLCategoryRepository;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SkateShopShould {
    @Test
    public void
    create_a_skate_category_in_db() throws SQLException {
        Category category = new Category("Inline skate");
        CategoryRepository categoryRepository = new SQLCategoryRepository(new PostgresConnectionPool("skateshop"));

        categoryRepository.save(category);

        assertThat(categoryRepository.getBy("Inline skate"), is(equalTo(category)));
    }
}
