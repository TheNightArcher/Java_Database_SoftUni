package entities.joined;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@MappedSuperclass
@Table(name = "bikes")
public class Bike extends Vehicle {

    public Bike(String type, String model, BigDecimal price, String fuelType) {
        super(type, model, price, fuelType);
    }
}
