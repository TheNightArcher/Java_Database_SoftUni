package softuni.exam.models.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity{

    private ApartmentType apartmentType;
    private Double area;
    private Town town;

    public Apartment() {
    }

    @Enumerated(value = EnumType.STRING)
    @NotNull
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    @Column(name = "area", nullable = false)
    @Min(40)
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    @ManyToOne
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
