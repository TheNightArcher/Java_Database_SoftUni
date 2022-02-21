import entities.User;
import orm.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static orm.MyConnector.createConnection;
import static orm.MyConnector.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        createConnection("root", "123", "custom-orm");
        Connection connection = getConnection();

        EntityManager<User> userEntityManager = new EntityManager<>(connection);


        User user = new User("Pesho", 25, LocalDate.now());
        user.setUsername("pesho2");


        userEntityManager.persist(user);

        Iterable<User> first = userEntityManager.find(User.class);
        System.out.println(first.toString());

        User toDelete = userEntityManager.findFirst(User.class, "id > 3");
        System.out.println(toDelete);

        userEntityManager.delete(toDelete);

        Iterable<User> second = userEntityManager.find(User.class);
        System.out.println(second.toString());

        connection.close();
    }
}
