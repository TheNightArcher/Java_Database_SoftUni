import java.sql.*;
import java.util.*;

public class _08_increaseMinionAge {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);


        PreparedStatement increaseAge = connection
                .prepareStatement("UPDATE `minions`\n" +
                        "SET `age` = `age` + 1, `name` = LOWER(`name`)\n" +
                        "WHERE `id` IN(?);");


        int[] IDs = Arrays.stream(scanner.nextLine().split("\\s+"))
                .mapToInt(Integer::parseInt).toArray();

        for (int i = 0; i < IDs.length; i++) {

            increaseAge.setInt(1, IDs[i]);
            increaseAge.executeUpdate();
        }

        PreparedStatement checkIDs = connection
                .prepareStatement("SELECT `name`,`age` FROM `minions`");

        ResultSet resultSet = checkIDs.executeQuery();

        while (resultSet.next()){

            String names = resultSet.getString("name");
            int age = resultSet.getInt("age");

            System.out.printf("%s %d\n",names,age);
        }

        connection.close();
    }
}
