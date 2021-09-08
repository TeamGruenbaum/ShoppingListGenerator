package model;



import controller.PathHelper;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public final class DatabaseConnection
{
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection(){}


    public Connection getConnection() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");

        if(connection==null)
        {
            PathHelper pathHelper=new PathHelper();

            File directory=new File(pathHelper.getSavePath());
            if(!directory.exists())
            {
                directory.mkdir();
            }

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
