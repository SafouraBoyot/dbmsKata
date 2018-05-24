package skateshop.skate;

import skateshop.category.Category;

import java.util.Objects;

public class Skate {
    private final String name;
    private final Category category;
    private final int stock;

    public Skate(String name , Category category, int stock) {
        this.name = name;
        this.category = category;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skate skate = (Skate) o;
        return stock == skate.stock &&
                Objects.equals(name, skate.name) &&
                Objects.equals(category, skate.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, stock);
    }
}
