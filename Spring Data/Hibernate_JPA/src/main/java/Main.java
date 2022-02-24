import entities.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("school");

        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Student student = new Student("Galin",21);
        Student student2 = new Student("Kaloyn",18);

        entityManager.persist(student);
        entityManager.persist(student2);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
