package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnector {

    private static Connection connection;
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";

    public static void createConnection(String username,String password,String dbname) throws SQLException {


        Properties properties = new Properties();
        properties.setProperty("user",username);
        properties.setProperty("password",password);

        connection = DriverManager.getConnection(CONNECTION_STRING + dbname,properties);

    }

    public static Connection getConnection(){
        return connection;
    }

}
