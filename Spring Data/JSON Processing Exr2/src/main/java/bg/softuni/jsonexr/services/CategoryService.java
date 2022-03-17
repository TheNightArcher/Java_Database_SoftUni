package bg.softuni.jsonexr.services;

import bg.softuni.jsonexr.models.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> findRandomCategories();
}
