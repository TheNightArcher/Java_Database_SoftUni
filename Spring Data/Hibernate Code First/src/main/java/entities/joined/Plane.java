package entities.joined;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@MappedSuperclass
@Table(name = "planes")
public class Plane extends Vehicle {
    private Integer passengerCapacity;

    public Plane(){}

    public Plane(String type, String model, BigDecimal price, String fuelType, Integer passengerCapacity) {
        super(type, model, price, fuelType);
        this.passengerCapacity = passengerCapacity;
    }

    public Integer getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(Integer passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }
}
