package example.dtoConception;

import example.dtoConception.entities.Employee;
import example.dtoConception.entities.Manager;
import example.dtoConception.entities.dto.ManagerDTO;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Employee employee =
                new Employee("Kaloyan",
                        "Manov",
                        BigDecimal.valueOf(3000.00),
                        LocalDate.of(2003, 6, 22),
                        "Boiko Borisov 11");

        Manager manager = new Manager("Ivaylo",
                "Manov",
                BigDecimal.valueOf(3000.00),
                LocalDate.of(2003, 6, 22),
                "Boiko Borisov 11",
                Boolean.FALSE);

        manager.addEmployee(employee);

        ModelMapper mapper = new ModelMapper();

        ManagerDTO dto = mapper.map(manager, ManagerDTO.class);

        System.out.println(dto);
    }
}
