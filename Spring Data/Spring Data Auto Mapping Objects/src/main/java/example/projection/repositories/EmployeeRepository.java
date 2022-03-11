package example.projection.repositories;

import example.projection.entities.Employee;
import example.projection.entities.dto.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    List<EmployeeDTO> findByBirthdayBeforeOrderBySalaryDesc(LocalDate date);
}
