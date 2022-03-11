package example.projection.services.impl;

import example.projection.entities.Employee;
import example.projection.entities.dto.EmployeeDTO;
import example.projection.repositories.EmployeeRepository;
import example.projection.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void save(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeDTO> selectEmployeesBornBornGivenYear(int year) {

        LocalDate givenYear = LocalDate.of(year, 1, 1);

        return this.employeeRepository.findByBirthdayBeforeOrderBySalaryDesc(givenYear);

    }
}
