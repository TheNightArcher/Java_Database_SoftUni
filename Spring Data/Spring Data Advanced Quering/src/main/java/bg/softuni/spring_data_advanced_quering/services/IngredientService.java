package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;

import java.util.List;
import java.util.Set;

public interface IngredientService {

    List<Ingredient> selectAllStartsLike(String letters);

    List<Ingredient> selectAllByNameOrderByPrice(List<String> names);

    void deleteSelectedName(String name);

    void updatePriceForAll();

    void updatePriceByGivenNames(Set<String> names);
}
