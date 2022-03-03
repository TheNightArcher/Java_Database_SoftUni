package entities._04_hospitalDatabase.historyBase;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "diagnose")
@DiscriminatorValue(value = "diagnose")
public class Diagnose extends Visitation {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String comments;

    public Diagnose(){}

    public Diagnose(String name, String comments) {
        this.name = name;
        this.comments = comments;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
