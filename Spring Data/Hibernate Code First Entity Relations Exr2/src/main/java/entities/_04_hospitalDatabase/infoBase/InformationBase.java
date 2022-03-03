package entities._04_hospitalDatabase.infoBase;

import javax.persistence.*;

@Entity
@Table(name = "info_base")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class InformationBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public InformationBase(){}

    public Long getId() {
        return id;
    }
}
