package entities._04_hospitalDatabase.historyBase;

import javax.persistence.*;

@Entity
@Table(name = "history_of_visitations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class HistoryOfVisitationsBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

public HistoryOfVisitationsBase(){}

    public Long getId() {
        return id;
    }
}
