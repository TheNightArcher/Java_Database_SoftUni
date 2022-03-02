package entities._03_universitySystem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "teacher")
public class Teacher extends User {

    private String email;
    @Column(name = "salary_per_hour")
    private BigDecimal salaryPerHour;

    @OneToMany
    private Set<Course> courses;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String phoneNumber, String email, BigDecimal salaryPerHour, Set<Course> courses) {
        super(firstName,lastName,phoneNumber);
        this.email = email;
        this.salaryPerHour = salaryPerHour;
        this.courses = courses;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(BigDecimal salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }
}
