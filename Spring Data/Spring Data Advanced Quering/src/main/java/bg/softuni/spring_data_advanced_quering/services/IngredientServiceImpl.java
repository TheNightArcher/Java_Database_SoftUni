package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;
import bg.softuni.spring_data_advanced_quering.repositories.IngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Override
    public List<Ingredient> selectAllStartsLike(String letters) {
        return this.ingredientsRepository.findByNameStartingWith(letters);
    }

    @Override
    public List<Ingredient> selectAllByNameOrderByPrice(List<String> names) {
        return this.ingredientsRepository.findByNameInOrderByPriceAsc(names);
    }
}
