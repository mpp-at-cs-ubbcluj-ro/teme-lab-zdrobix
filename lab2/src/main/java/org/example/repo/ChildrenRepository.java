package ro.mpp2025.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ChildrenRepository {
    private final List<String> connectionListCredentials;

    public ChildrenRepository(String url, String username, String password)  {
        this.connectionListCredentials = Arrays.asList(url, username, password);
    }

    public static Connection connectToDb (List<String> connectionCredentials)  {
        boolean connected = false;
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(
                            connectionCredentials.get(0),
                            connectionCredentials.get(1),
                            connectionCredentials.get(2)
                    );
            connected = true;
        } catch (SQLException e) {   System.out.println(e.getMessage());
        } finally {
            return connection;
        }
    }


}
