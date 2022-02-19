import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class _07_printMinionNames {
    public static void main(String[] args) throws SQLException {


        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement getNames = connection
                .prepareStatement("SELECT `name` FROM `minions`;");

        ResultSet resultOfNames = getNames.executeQuery();

        List<String> names = new ArrayList<>();

        while (resultOfNames.next()) {

            String currentName = resultOfNames.getString("name");
            names.add(currentName);
        }


        int counter = 1;
        for (int i = 0; i < names.size() / 2; i++) {

            int numbersBackward = names.size() - counter;

            System.out.println(names.get(i));
            System.out.println(names.get(numbersBackward));
            counter++;
        }

        connection.close();
    }
}
