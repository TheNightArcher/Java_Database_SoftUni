package bg.softuni.services;

import bg.softuni.models.Category;

import java.util.Set;

public interface CategoryService {

    Set<Category> getRandomCategories();
}
