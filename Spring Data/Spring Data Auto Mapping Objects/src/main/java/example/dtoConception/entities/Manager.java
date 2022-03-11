package example.dtoConception.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager extends Employee{
    private Boolean onHoliday;
    private List<Employee> subordinates;

    public Manager(String firstName, String lastName, BigDecimal salary, LocalDate birthday, String address, Boolean onHoliday) {
        super(firstName, lastName, salary, birthday, address);
        this.onHoliday = onHoliday;
        this.subordinates = new ArrayList<>();
    }

    public void addEmployee(Employee employee){
        subordinates.add(employee);
    }

    public Boolean getOnHoliday() {
        return onHoliday;
    }

    public void setOnHoliday(Boolean onHoliday) {
        this.onHoliday = onHoliday;
    }

    public List<Employee> getSubordinates() {
        return Collections.unmodifiableList(subordinates);
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }
}
