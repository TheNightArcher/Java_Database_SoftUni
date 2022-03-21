package example.services;

import example.models.dtos.CategorySeedDto;

import java.util.List;

public interface CategoryService {
    void seedCategories(List<CategorySeedDto> categories);
}
