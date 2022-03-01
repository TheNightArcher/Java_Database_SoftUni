package entities.joined;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@MappedSuperclass
@Table(name = "cars")
public class Car extends Vehicle {
    private Integer seats;

    public Car(){}

    public Car(String type, String model, BigDecimal price, String fuelType, Integer seats) {
        super(type, model, price, fuelType);
        this.seats = seats;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
