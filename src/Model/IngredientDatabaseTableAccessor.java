package model;


import controller.Localisator;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;



public class IngredientDatabaseTableAccessor implements DatabaseTableAccessor<Ingredient>
{
    private Connection connection;


    public IngredientDatabaseTableAccessor() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        connection=DatabaseConnection.getInstance().getConnection();

        Statement statement=connection.createStatement();

        if(!(connection.getMetaData().getTables(null, null, "Ingredients", null).next()))
        {
            statement.execute("CREATE TABLE IF NOT EXISTS Ingredients(id integer primary key , name text not null, store text not null, shelf integer not null, CONSTRAINT uniqueIngredient UNIQUE(name, store, shelf))");

            Localisator localisator=new Localisator();
            String kaufland= localisator.getString("kaufland");
            String lidl=localisator.getString("lidl");
            statement.execute("INSERT INTO Ingredients(name, store, shelf) VALUES ('"+localisator.getString("bolognese_sauce")+"', '"+kaufland+"', 4), ('"+localisator.getString("grated_cheese")+"', '"+lidl+"', 2), ('"+localisator.getString("lemonade")+"', '"+kaufland+"', 5), ('"+localisator.getString("noodles")+"', '"+kaufland+"', 4), ('"+localisator.getString("parmesan")+"', '"+kaufland+"', 2), ('"+localisator.getString("pizza_dough")+"', '"+kaufland+"', 3), ('"+localisator.getString("tomato_paste")+"', '"+lidl+"', 4), ('"+localisator.getString("broccoli")+"', '"+lidl+"', 0)");
        }
    }


    @Override
    public List<Ingredient> getAll() throws SQLException
    {
        ResultSet resultSet=connection.createStatement().executeQuery("SELECT * FROM Ingredients");

        List<Ingredient> result=new ArrayList<>();
        while(resultSet.next())
        {
            result.add(new Ingredient(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("store"), resultSet.getInt("shelf")));
        }

        return result;
    }

    @Override
    public int update(Ingredient item) throws SQLException
    {
        if(item.getId()==-1) throw new SQLException("Invalid ID (-1)");

        PreparedStatement updateStatement=connection.prepareStatement("UPDATE Ingredients SET name=?, store=?, shelf=? WHERE id=?");
        updateStatement.setString(1, item.getName());
        updateStatement.setString(2, item.getStore());
        updateStatement.setInt(3, item.getShelf());
        updateStatement.setInt(4, item.getId());
        return updateStatement.executeUpdate();
    }

    @Override
    public void add(Ingredient item) throws SQLException
    {
        PreparedStatement insertStatement=connection.prepareStatement("INSERT INTO Ingredients(name, store, shelf) VALUES(?, ?, ?)");
        insertStatement.setString(1, item.getName());
        insertStatement.setString(2, item.getStore());
        insertStatement.setInt(3, item.getShelf());
        insertStatement.execute();
    }

    @Override
    public void remove(int id) throws SQLException
    {
        PreparedStatement deleteFromIsNeededForStatment=connection.prepareStatement("DELETE FROM IsNeededFor WHERE ingredientID=?");
        deleteFromIsNeededForStatment.setInt(1, id);
        deleteFromIsNeededForStatment.execute();

        PreparedStatement deleteFromIngredients=connection.prepareStatement("DELETE FROM Ingredients WHERE id=?");
        deleteFromIngredients.setInt(1, id);
        deleteFromIngredients.execute();
    }
}
