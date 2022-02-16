import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/diablo", props);

        PreparedStatement statement = connection
                .prepareStatement("SELECT user_name,`first_name`,`last_name`,COUNT(`user_id`) AS 'games_played' FROM `users` AS u\n" +
                        "JOIN `users_games` AS ug\n" +
                        "ON u.`id` = ug.`user_id`\n" +
                        "WHERE u.`user_name` = ?\n" +
                        "GROUP BY ug.`user_id`;");

        String username = scanner.nextLine();

        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String dbUsername = resultSet.getString("user_name");
            String dbFirstName = resultSet.getString("first_name");
            String dbLastName = resultSet.getString("last_name");
            Integer countOfGames = resultSet.getInt("games_played");

            System.out.printf("User: %s\n%s %s has played %d games\n", dbUsername, dbFirstName, dbLastName, countOfGames);

        } else {
            System.out.println("No such user exists");
        }
    }
}
