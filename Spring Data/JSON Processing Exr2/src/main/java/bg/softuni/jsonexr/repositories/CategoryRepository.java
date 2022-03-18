package bg.softuni.jsonexr.repositories;

import bg.softuni.jsonexr.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM categories c " +
            "JOIN c.products p " +
            "ORDER BY p.size DESC")
    List<Category> findAllCategoriesProductsCount();
}
