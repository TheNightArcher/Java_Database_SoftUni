package bg.softuni.jsonexr.services;

import bg.softuni.jsonexr.models.Category;
import bg.softuni.jsonexr.models.dtos.CategoryProductsDto;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> findRandomCategories();

    List<CategoryProductsDto> findAllCategoriesWithProductCount();
}
