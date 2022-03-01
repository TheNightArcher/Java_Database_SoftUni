import entities.JPA_Inheritance.single.Car;
import entities.JPA_Inheritance.single.Vehicle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("PU_Name");

        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();

        Vehicle car = new Car("VW","Golf", BigDecimal.valueOf(3000.00),"Petrol",5);
        entityManager.persist(car);


        entityManager.getTransaction().commit();
    }
}
