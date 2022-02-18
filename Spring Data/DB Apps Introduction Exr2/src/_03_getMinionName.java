import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _03_getMinionName {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement chekVillainName = connection
                .prepareStatement("SELECT `name` FROM `villains`" +
                        " WHERE `id` = ?;");

        int villainID = Integer.parseInt(scanner.nextLine());

        chekVillainName.setInt(1, villainID);

        ResultSet villainSet = chekVillainName.executeQuery();

        if (!villainSet.next()) {
            System.out.printf("No villain with ID %d exists in the database.", villainID);
        }

        String villainName = villainSet.getString("name");
        System.out.printf("Villain: %s\n", villainName);

        PreparedStatement getMinionName = connection
                .prepareStatement("SELECT `name`,`age` FROM `minions` AS m\n" +
                        "JOIN `minions_villains` AS mv\n" +
                        "ON m.`id` = mv.`minion_id`\n" +
                        "WHERE mv.`villain_id` = ?;");

        getMinionName.setInt(1, villainID);

        ResultSet minionSet = getMinionName.executeQuery();

        int index = 0;
        while (minionSet.next()) {

            String name = minionSet.getString("name");
            int age = minionSet.getInt("age");

            index++;
            System.out.printf("%d. %s %d\n", index, name, age);
        }

        connection.close();
    }
}
