import entities.Address;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

public class Engine implements Runnable {
    private final EntityManager entityManager;
    private BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Please select Exr number:");

        try {
            int exNumber = Integer.parseInt(bufferedReader.readLine());

            switch (exNumber) {
                case 2 -> changeCasing();
                case 3 -> containsEmployee();
                case 4 -> employeesWithSalaryOver50_000();
                case 5 -> employeesFromDepartment();
                case 6 -> addingNewAddress();
                case 7 -> addressesWithEmployeeCount();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private void addressesWithEmployeeCount() {
        List<Address> result = entityManager
                .createQuery("SELECT a FROM Address a " +
                        "ORDER BY a.employees.size DESC ", Address.class)
                .setMaxResults(10)
                .getResultList();

        result.forEach(a -> System.out.printf("%s, %s - %d employees %n",
                a.getText(),
                a.getTown() == null
                ? "Unknown" : a.getTown().getName(),
                a.getEmployees().size()));
    }

    private void addingNewAddress() throws IOException {
        System.out.println("Please enter last name of some employee.");
        String lastName = bufferedReader.readLine();

        Employee currentEmployee = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.lastName = :l_name", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        Address address = addingGivenAddress("Vitoshka 15");

        entityManager.getTransaction().begin();
        currentEmployee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private Address addingGivenAddress(String givenAddress) {
        Address address = new Address();
        address.setText(givenAddress);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void employeesFromDepartment() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.id = 6 " +
                        "ORDER BY e.salary,e.id ", Employee.class)
                .getResultList()
                .forEach(e -> System.out.printf("%s %s from %s - $%.2f%n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getDepartment().getName(),
                        e.getSalary()));
    }

    private void employeesWithSalaryOver50_000() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.salary > :given_salary", Employee.class)
                .setParameter("given_salary", BigDecimal.valueOf(50000))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void containsEmployee() throws IOException {
        System.out.println("Enter names of the person.");

        String[] names = bufferedReader.readLine().split("\\s+");
        String firstName = names[0];
        String lastName = names[1];

        Long result = entityManager.createQuery("SELECT count (e) FROM Employee e " +
                        "WHERE e.firstName = :f_name AND e.lastName = :l_name", Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();

        // If result is 0 that means it's not exist otherwise we have that person.
        System.out.println(result == 0
                ? "No" : "Yes");
    }

    private void changeCasing() {
        entityManager.getTransaction().begin();

        int updatedTowns = entityManager.createQuery("UPDATE  Town t " +
                        "SET t.name = upper(t.name) " +
                        "WHERE length(t.name) <= 5 ")
                .executeUpdate();

        System.out.println(updatedTowns);

        entityManager.getTransaction().commit();
    }
}
