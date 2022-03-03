package entities._04_hospitalDatabase.historyBase;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "visitation")
@DiscriminatorValue(value = "visitation")
public class Visitation extends HistoryOfVisitationsBase{

    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String comment;

    public Visitation(){}

    public Visitation(LocalDate date, String comment) {
        this.date = date;
        this.comment = comment;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
