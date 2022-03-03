package entities._04_hospitalDatabase.infoBase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue(value = "patient")
public class Patient extends InformationBase {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String address;
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "BLOB")
    private String picture;

    @Column(name = "is_has_medical_insurance")
    private Boolean isHasMedicalInsurance;

    public Patient(){

    }

    public Patient(String firstName, String lastName, String address, String email, LocalDate dateOfBirth, String picture, Boolean isHasMedicalInsurance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.picture = picture;
        this.isHasMedicalInsurance = isHasMedicalInsurance;
    }

    public Patient(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getHasMedicalInsurance() {
        return isHasMedicalInsurance;
    }

    public void setHasMedicalInsurance(Boolean hasMedicalInsurance) {
        isHasMedicalInsurance = hasMedicalInsurance;
    }
}
