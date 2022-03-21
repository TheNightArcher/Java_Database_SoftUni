package example.services;

import example.models.Category;
import example.models.dtos.CategorySeedDto;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories(List<CategorySeedDto> categories);

    long getCount();

    Set<Category> getRandomCategories();
}
