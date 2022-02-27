import entities.*;
import javax.persistence.EntityManager;
import java.io.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                case 8 -> getEmployeeWithProject();
                case 9 -> findLatest_10_Projects();
                case 10 -> increaseSalaries();
                case 11 -> findEmployeesByFirstName();
                case 12 -> employeesMaximumSalaries();
                case 13 -> removeTowns();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private void removeTowns() throws IOException {
        System.out.println("Please enter town");
        String townName = bufferedReader.readLine();

        Town town = entityManager.createQuery("SELECT t FROM Town t " +
                        "WHERE t.name = :t_name ", Town.class)
                .setParameter("t_name", townName)
                .getSingleResult();

        int deletedAddresses = removeAddressesByTownId(town.getId());

        System.out.printf("%d address in %s deleted", deletedAddresses, townName);
    }

    private int removeAddressesByTownId(Integer id) {

        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                        "WHERE a.town.id = :t_id", Address.class)
                .setParameter("t_id", id)
                .getResultList();

        entityManager.getTransaction().begin();
        addresses.forEach(entityManager::remove);
        entityManager.getTransaction().commit();

        return addresses.size();
    }

    private void employeesMaximumSalaries() {
        List<Object[]> rows = entityManager.createNativeQuery("SELECT d.name,MAX(e.salary) as `m_salary` FROM departments d\n" +
                "join employees e on d.department_id = e.department_id\n" +
                "Group by d.name\n" +
                "Having `m_salary` not between 30000 and 70000;").getResultList();

        for (var row : rows) {
            System.out.println( Arrays.toString(row));
        }
    }

    private void findEmployeesByFirstName() throws IOException {
        System.out.println("Please enter name start like...");
        String letters = bufferedReader.readLine();

        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.firstName LIKE CONCAT(:l_start,'%') ", Employee.class)
                .setParameter("l_start", letters)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s - %s - ($%.2f)\n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getJobTitle(),
                        e.getSalary()));
    }

    private void increaseSalaries() {
        // First I do update because if we try to print result at the same time will get
        // typeQuery can't be "Update or Delete" that's why I split it at two parts.

        entityManager.getTransaction().begin();

        entityManager.createQuery("UPDATE Employee " +
                        "SET salary = salary + (salary * 0.12) " +
                        "WHERE department.id IN (1,2,4,11) ")
                .executeUpdate();


        entityManager.getTransaction().commit();

        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.id IN (1,2,4,11)", Employee.class)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s ($%.2f)\n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getSalary()));
    }

    private void findLatest_10_Projects() {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Project> projects = entityManager.createQuery("SELECT p FROM Project p " +
                        "ORDER BY p.startDate DESC ", Project.class)
                .setMaxResults(10)
                .getResultList();

        Stream<String> sortedByName = projects
                .stream()
                .map(Project::getName)
                .sorted();

        // First I sorted all the projects then I started to take them out in sort order from the nested loop.
        for (Object orderedName : sortedByName.collect(Collectors.toList())) {
            for (Project p : projects) {

                if (p.getName().equals(orderedName)) {
                    System.out.printf("Project name: %s\n", p.getName());
                    System.out.printf("Project Description: %s\n", p.getDescription());
                    System.out.printf("Project Start Date: %s\n", p.getStartDate() == null
                            ? "null" : date.format(p.getStartDate()));
                    System.out.printf("Project End Date: %s\n", p.getEndDate() == null
                            ? "null" : date.format(p.getEndDate()));
                    break;
                }
            }
        }
    }

    private void getEmployeeWithProject() throws IOException {
        System.out.println("Please enter id of employee");
        Integer id = Integer.parseInt(bufferedReader.readLine());
        Employee employee = entityManager.find(Employee.class, id);

        System.out.printf("%S %s - %s\n",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getJobTitle());

        employee.getProjects()
                .stream()
                .map(Project::getName)
                .sorted()
                .forEach(System.out::println);
    }

    private void addressesWithEmployeeCount() {
        List<Address> result = entityManager
                .createQuery("SELECT a FROM Address a " +
                        "ORDER BY a.employees.size DESC ", Address.class)
                .setMaxResults(10)
                .getResultList();

        result.forEach(a -> System.out.printf("%s, %s - %d employees \n",
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
                .forEach(e -> System.out.printf("%s %s from %s - $%.2f\n",
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

    // Here I will post my GitHub if you like to see how I solve my exercises.
    // link -->  https://github.com/TheNightArcher
}
