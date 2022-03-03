import entities._04_hospitalDatabase.historyBase.HistoryOfVisitationsBase;
import entities._04_hospitalDatabase.historyBase.Medicament;
import entities._04_hospitalDatabase.historyBase.Visitation;
import entities._04_hospitalDatabase.infoBase.InformationBase;
import entities._04_hospitalDatabase.infoBase.Patient;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        EntityManager entityManager = Persistence
                .createEntityManagerFactory("HibernateExr")
                .createEntityManager();

        entityManager.getTransaction().begin();

        InformationBase patient = new Patient("Galin");
        HistoryOfVisitationsBase hvb = new Visitation(LocalDate.now(), "You are good");
        HistoryOfVisitationsBase hvb2 = new Medicament("Strepsils");

        entityManager.persist(patient);
        entityManager.persist(hvb);
        entityManager.persist(hvb2);

        entityManager.getTransaction().commit();
    }
}
