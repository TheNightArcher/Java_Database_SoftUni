package entities._06_footballBettingDatabase;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "teams")
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "BLOB")
    private String logo;

    @Column(length = 3,unique = true)
    private String initials;

    @OneToOne
    @JoinColumn(name = "primary_kit_color")
    private Colors primaryKitColor;

    @OneToOne
    @JoinColumn(name = "secondary_kit_color")
    private Colors secondaryKitColor;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Towns town;
    private BigDecimal budget;

    public Teams(){}

    public Teams(String name, String logo, String initials, Colors primaryKitColor, Colors secondaryKitColor, Towns town, BigDecimal budget) {
        this.name = name;
        this.logo = logo;
        this.initials = initials;
        this.primaryKitColor = primaryKitColor;
        this.secondaryKitColor = secondaryKitColor;
        this.town = town;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Colors getPrimaryKitColor() {
        return primaryKitColor;
    }

    public void setPrimaryKitColor(Colors primaryKitColor) {
        this.primaryKitColor = primaryKitColor;
    }

    public Colors getSecondaryKitColor() {
        return secondaryKitColor;
    }

    public void setSecondaryKitColor(Colors secondaryKitColor) {
        this.secondaryKitColor = secondaryKitColor;
    }

    public Towns getTown() {
        return town;
    }

    public void setTown(Towns town) {
        this.town = town;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
