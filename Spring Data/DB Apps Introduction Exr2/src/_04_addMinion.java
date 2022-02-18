import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _04_addMinion {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        String[] minionInf = scanner.nextLine().split("\\s+");

        String minionName = minionInf[1];
        int minionAge = Integer.parseInt(minionInf[2]);

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement checkTown = connection
                .prepareStatement("SELECT `name` FROM `towns`\n" +
                        "WHERE `name` = ?;");


        String townName = minionInf[3];
        checkTown.setString(1, townName);

        ResultSet townSet = checkTown.executeQuery();

        if (!townSet.next()) {

            PreparedStatement insertTown = connection
                    .prepareStatement("INSERT INTO `towns` (`name`)\n" +
                            "VALUES" +
                            " (?);");

            insertTown.setString(1, townName);
            insertTown.executeUpdate();

            System.out.printf("Town %s was added to the database.\n", townName);
        }

        // Villain Name

        String villainName = scanner.nextLine().split("\\s+")[1];

        PreparedStatement checkVillain = connection
                .prepareStatement("SELECT `id` FROM `villains`\n" +
                        "WHERE `name` = ?;");

        checkVillain.setString(1, villainName);

        ResultSet villainSet = checkVillain.executeQuery();

        int villainID = 0;
        if (!villainSet.next()) {

            PreparedStatement insertVillain = connection
                    .prepareStatement("INSERT INTO `villains` (`name`,`evilness_factor`)\n" +
                            "VALUES\n" +
                            "(?,?);");

            insertVillain.setString(1, villainName);
            insertVillain.setString(2, "evil");
            insertVillain.executeUpdate();

            ResultSet newVillainSet = checkVillain.executeQuery();
            newVillainSet.next();
            villainID = newVillainSet.getInt("id");

            System.out.printf("Villain %s was added to the database.\n", villainName);
        }

        PreparedStatement insertMinion = connection
                .prepareStatement("INSERT INTO `minions` (`name`,`age`)\n" +
                        "VALUES (?,?)");

        insertMinion.setString(1, minionName);
        insertMinion.setInt(2, minionAge);

        insertMinion.executeUpdate();

        PreparedStatement lastMinionID = connection
                .prepareStatement("SELECT `id` FROM `minions`\n" +
                        "ORDER BY `id` DESC LIMIT 1;");

        ResultSet idSet = lastMinionID.executeQuery();
        idSet.next();

        int minionID = idSet.getInt("id");

        PreparedStatement setMinionToVillain = connection
                .prepareStatement("INSERT INTO `minions_villains` \n" +
                        "VALUES(?,?);");

        setMinionToVillain.setInt(1,minionID);
        setMinionToVillain.setInt(2,villainID);

        System.out.printf("Successfully added %s to be minion of %s.",minionName,villainName);

    }
}
