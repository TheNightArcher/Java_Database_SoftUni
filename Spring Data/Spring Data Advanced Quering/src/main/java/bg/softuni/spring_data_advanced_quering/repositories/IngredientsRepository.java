package bg.softuni.spring_data_advanced_quering.repositories;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByNameStartingWith(String letters);

    List<Ingredient> findByNameInOrderByPriceAsc(List<String> names);
}
