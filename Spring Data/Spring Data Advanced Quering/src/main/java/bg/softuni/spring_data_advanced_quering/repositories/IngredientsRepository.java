package bg.softuni.spring_data_advanced_quering.repositories;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByNameStartingWith(String letters);

    List<Ingredient> findByNameInOrderByPriceAsc(List<String> names);

    @Transactional
    void deleteByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Ingredient SET price = price + price * 0.10")
    void updateAllIngredientsPrice();

    @Transactional
    @Modifying
    @Query("UPDATE Ingredient " +
            "SET price = price + price * 0.10 " +
            "WHERE name IN :names")
    void updatePriceByName(Set<String> names);
}
