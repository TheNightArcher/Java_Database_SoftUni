package entities._06_footballBettingDatabase;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Towns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany
    private Set<Teams> teams;

    @OneToMany
    @JoinColumn(name = "country_id")
    private Set<Countries> countryId;

    public Towns() {
    }

    public Towns(String name, Set<Teams> teams, Set<Countries> countryId) {
        this.name = name;
        this.teams = teams;
        this.countryId = countryId;
    }

    public Long getId() {
        return id;
    }
}
