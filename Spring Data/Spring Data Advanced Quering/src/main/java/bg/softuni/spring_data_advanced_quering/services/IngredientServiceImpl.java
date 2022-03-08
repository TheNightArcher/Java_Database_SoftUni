package bg.softuni.spring_data_advanced_quering.services;

import bg.softuni.spring_data_advanced_quering.entities.Ingredient;
import bg.softuni.spring_data_advanced_quering.repositories.IngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    @Override
    public void deleteSelectedName(String name) {
        this.ingredientsRepository.deleteByName(name);
    }

    @Override
    public void updatePriceForAll() {
        this.ingredientsRepository.updateAllIngredientsPrice();
    }

    @Override
    public void updatePriceByGivenNames(Set<String> names) {
        this.ingredientsRepository.updatePriceByName(names);
    }
}
