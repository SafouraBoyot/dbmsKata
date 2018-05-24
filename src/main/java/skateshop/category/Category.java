package skateshop.category;

import java.util.Objects;

public class Category {

    private final String type;


    public Category(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(type, category.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

}
