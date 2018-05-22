package skateshop.category;

public interface CategoryRepository {
    int save(Category category);

    Category getBy(String type);
}
