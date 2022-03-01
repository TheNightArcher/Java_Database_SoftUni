package entities.Table_Relation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "cars")
public class Cars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String type;
    private String model;
    private BigDecimal price;
    private String fuelType;

    @OneToOne
    @JoinColumn(name = "plate_id",
    referencedColumnName = "id")

    //or if we like to have bidirectional we
        //    @OneToOne(mappedBy = "plate",
        //    targetEntity = Cars.class)


    private PlateNumbers plateNumbers;

    public Cars(){}

    public Cars(String type, String model, BigDecimal price, String fuelType) {

        this.type = type;
        this.model = model;
        this.price = price;
        this.fuelType = fuelType;
    }



}
