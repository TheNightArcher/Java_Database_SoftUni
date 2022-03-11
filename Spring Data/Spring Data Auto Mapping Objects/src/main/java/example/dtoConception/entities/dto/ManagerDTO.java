package example.dtoConception.entities.dto;

import example.dtoConception.entities.Employee;

import java.util.List;

public class ManagerDTO {
    private String firstName;
    private String lastName;
    private List<Employee> subordinates;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String managerInfo = String.format("%s %s | Employees: %d\n", this.firstName,
                this.lastName,
                this.subordinates.size());

        sb.append(managerInfo);

        for (Employee subordinate : subordinates) {
            sb.append(String.format("    - %s ", subordinate.getFirstName()))
                    .append(String.format("%s ", subordinate.getLastName()))
                    .append(String.format("%.2f", subordinate.getSalary()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
