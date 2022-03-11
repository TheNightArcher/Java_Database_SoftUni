package example.projection;

import example.projection.entities.Employee;
import example.projection.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CommandRunner implements CommandLineRunner {

    private final EmployeeService employeeService;

    @Autowired
    public CommandRunner(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void run(String... args) throws Exception {

        Employee employee = new Employee("Ivaylo",
                "Manov",
                BigDecimal.ONE,
                LocalDate.of(1975, 5, 12),
                "Boiko Borisov 11");

        Employee secondEmployee = new Employee("Kaloyan",
                "Manov",
                BigDecimal.ONE,
                LocalDate.of(2003, 6, 22),
                "Boiko Borisov 11",employee);

        employeeService.save(secondEmployee);

         this.employeeService.selectEmployeesBornBornGivenYear(1990)
                 .forEach(System.out::println);
    }
}
