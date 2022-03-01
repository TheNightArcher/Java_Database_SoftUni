package entities.joined;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@MappedSuperclass
@Table(name = "trucks")
public class Truck extends Vehicle {
    private Double loadCapacity;

    public Truck(){}

    public Truck(String type, String model, BigDecimal price, String fuelType, Double loadCapacity) {
        super(type, model, price, fuelType);
        this.loadCapacity = loadCapacity;
    }

    public Double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(Double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }
}
