package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;

import java.util.List;

public interface IngredientService {

    List<Ingredient> selectAllStartsLike(String letters);

    List<Ingredient> selectAllByNameOrderByPrice(List<String> names);
}
