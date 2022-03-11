package example.projection.services;

import example.projection.entities.Employee;
import example.projection.entities.dto.EmployeeDTO;

import java.util.List;


public interface EmployeeService {

    void save(Employee employee);

    List<EmployeeDTO> selectEmployeesBornBornGivenYear(int year);
}
