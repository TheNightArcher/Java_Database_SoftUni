package entities._06_footballBettingDatabase;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "countries")
public class Countries {

    @Id
    @Column(length = 3, unique = true)
    private String id;

    @Column(length = 3)
    private String name;

    @ManyToMany
    private Set<Continents> continents;

    public Countries() {
    }

    public Countries(String id, String name, Set<Continents> continents) {
        this.id = id;
        this.name = name;
        this.continents = continents;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Continents> getContinents() {
        return continents;
    }

    public void setContinents(Set<Continents> continents) {
        this.continents = continents;
    }
}
