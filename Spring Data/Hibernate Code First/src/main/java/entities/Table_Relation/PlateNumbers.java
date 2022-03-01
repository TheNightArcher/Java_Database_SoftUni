package entities.Table_Relation;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "plates")
public class PlateNumbers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String number;

    public PlateNumbers(){}

    public PlateNumbers(String number) {
        this.number = number;
    }
}
