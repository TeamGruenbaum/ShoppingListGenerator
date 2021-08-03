package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection
{
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {}

    public Connection getConnection() throws SQLException
    {
        if(connection==null)
        {
            PathHelper pathHelper=new PathHelper();
            connection=DriverManager.getConnection("jdbc:sqlite:"+pathHelper.getSavePath()+pathHelper.getDatabaseName());
        }
        return connection;
    }

    public static DatabaseConnection getInstance()
    {
        if(instance==null)
        {
            instance=new DatabaseConnection();
        }
        return instance;
    }
}
