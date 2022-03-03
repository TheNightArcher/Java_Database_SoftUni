package entities._04_hospitalDatabase.historyBase;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "medicament")
@DiscriminatorValue(value = "medicament")
public class Medicament extends Visitation {

    private String name;

    public Medicament() {
    }

    public Medicament(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
