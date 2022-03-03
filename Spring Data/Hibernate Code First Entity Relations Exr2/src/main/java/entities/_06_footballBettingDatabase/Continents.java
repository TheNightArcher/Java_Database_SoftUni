package entities._06_footballBettingDatabase;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "continents")
public class Continents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "continents")
    private Set<Countries> countries;

    public Continents(){}

    public Continents(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

