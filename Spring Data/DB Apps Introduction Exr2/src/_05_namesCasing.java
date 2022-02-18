import java.sql.*;
import java.util.*;

public class _05_namesCasing {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        String country = scanner.nextLine();

        PreparedStatement updateTownsNames = connection
                .prepareStatement("UPDATE `towns`\n" +
                        "SET `name` = UPPER(`name`)\n" +
                        "WHERE `country` = ?;");

        updateTownsNames.setString(1, country);

        int countOfAffected = updateTownsNames.executeUpdate();

        if (countOfAffected == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        System.out.printf("%d town names were affected.\n", countOfAffected);

        PreparedStatement selectAllTowns = connection
                .prepareStatement("SELECT `name` FROM `towns`\n" +
                        "WHERE `country` = ?;");

        selectAllTowns.setString(1,country);

        ResultSet townsSet = selectAllTowns.executeQuery();

        List<String> towns = new ArrayList<>();

        while (townsSet.next()){

            String name = townsSet.getString("name");
            towns.add(name);
        }

        System.out.println(towns);
    }
}
