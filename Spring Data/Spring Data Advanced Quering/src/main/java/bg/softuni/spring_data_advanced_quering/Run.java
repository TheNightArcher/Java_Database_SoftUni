package bg.softuni.spring_data_advanced_quering;

import bg.softuni.spring_data_advanced_quering.entities.Size;
import bg.softuni.spring_data_advanced_quering.services.ShampoosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class Run implements CommandLineRunner {


    private final ShampoosService shampoosService;

    @Autowired
    public Run(ShampoosService shampoosService) {
        this.shampoosService = shampoosService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.shampoosService.selectShampoosBySize(Size.MEDIUM)
                .forEach(s -> System.out.printf("%s %s %.2f\n",
                        s.getBrand(),
                        s.getSize(),
                        s.getPrice()));
    }
}
