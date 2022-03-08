package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;
import bg.softuni.spring_data_advanced_quering.entities.Shampoo;
import bg.softuni.spring_data_advanced_quering.entities.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ShampoosService {

    List<Shampoo> selectShampoosBySize (Size size);

    List<Shampoo> selectShampoosBySizeOrId (Size size,Long id);

    List<Shampoo> selectShampoosWithPriceBiggerThen(BigDecimal price);

    int countAllShampoosLessThen(BigDecimal price);

    List<Shampoo> selectAllByGivenIngredients(Set<String> names);

    List<Shampoo> getAllLessThenGivenCount(int count);
}
