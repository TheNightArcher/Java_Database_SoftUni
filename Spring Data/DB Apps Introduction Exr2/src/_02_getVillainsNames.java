import java.sql.*;
import java.util.Properties;

public class _02_getVillainsNames {
    public static void main(String[] args) throws SQLException {

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);


        PreparedStatement getVillainName = connection
                .prepareStatement("SELECT `name`,COUNT(DISTINCT `minion_id`) AS 'count_min' FROM `villains` AS v" +
                " JOIN `minions_villains` AS mv" +
                " ON v.`id` = mv.`villain_id`" +
                " GROUP BY name" +
                " HAVING count_min > 15;");

        ResultSet resultName = getVillainName.executeQuery();

        if (!resultName.next()) {
            System.out.println("Wrong name ");
            return;
        }

        String name = resultName.getString("name");
        int count = resultName.getInt("count_min");

        System.out.printf("%s %d", name, count);

        connection.close();
    }
}
