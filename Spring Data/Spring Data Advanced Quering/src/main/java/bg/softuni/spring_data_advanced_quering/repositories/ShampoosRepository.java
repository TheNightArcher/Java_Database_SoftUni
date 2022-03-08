package bg.softuni.spring_data_advanced_quering.repositories;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;
import bg.softuni.spring_data_advanced_quering.entities.Shampoo;
import bg.softuni.spring_data_advanced_quering.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampoosRepository extends JpaRepository<Shampoo, Long> {

    List<Shampoo> findShampoosBySizeOrderByIdAsc(Size size);

    List<Shampoo> findShampoosBySizeOrLabelIdOrderByPriceAsc(Size size, Long id);

    List<Shampoo> findShampoosByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    int countByPriceLessThan(BigDecimal price);

    @Query("SELECT s FROM Shampoo AS s " +
            "JOIN s.ingredients AS i WHERE i.name IN :names")
    List<Shampoo> findByIngredientsName(Set<String> names);

    @Query("SELECT s FROM Shampoo AS s" +
            " where s.ingredients.size < :count")
    List<Shampoo> findByCountOfIngredients(int count);
}

