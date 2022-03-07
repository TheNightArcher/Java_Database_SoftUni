package bg.softuni.spring_data_advanced_quering;

import bg.softuni.spring_data_advanced_quering.services.IngredientService;
import bg.softuni.spring_data_advanced_quering.services.ShampoosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public class Run implements CommandLineRunner {


    private final ShampoosService shampoosService;

    private final IngredientService ingredientService;

    @Autowired
    public Run(ShampoosService shampoosService, IngredientService ingredientService) {
        this.shampoosService = shampoosService;
        this.ingredientService = ingredientService;
    }

    @Override
    public void run(String... args) throws Exception {

//        this.shampoosService.selectShampoosWithPriceBiggerThen(BigDecimal.valueOf(5))
//                .forEach(s -> System.out.printf("%s %s %.2f\n",
//                        s.getBrand(),
//                        s.getSize(),
//                        s.getPrice()));

//        this.ingredientService.selectAllByNameOrderByPrice(List.of("Lavender","Herbs","Apple"))
//                .forEach(i -> System.out.println(i.getName()));

        System.out.println(this.shampoosService.countAllShampoosLessThen(BigDecimal.valueOf(8.50)));
    }
}
