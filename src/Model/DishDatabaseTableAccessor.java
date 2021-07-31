package controller;

import model.Dish;
import model.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishDatabaseTableAccessor implements DatabaseTableAccessor<Dish>
{
    private Connection connection;


    public DishDatabaseTableAccessor() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Database.db");

        Statement statement=connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS Dishes(id integer primary key, name text unique not null)");
        statement.execute("CREATE TABLE IF NOT EXISTS IsNeededFor(dishID integer, ingredientID integer, CONSTRAINT primaryKey PRIMARY KEY (dishID, ingredientID))");
    }

    @Override
    public List<Dish> getAll() throws SQLException
    {
        ResultSet dishesResultSet=connection.createStatement().executeQuery("SELECT * FROM Dishes");
        List<Dish> dishesList=new ArrayList<>();
        while(dishesResultSet.next())
        {
            dishesList.add(new Dish(dishesResultSet.getInt("id"), dishesResultSet.getString("name"), getIngredientsNeededForDish(dishesResultSet.getInt("id"))));
        }

        return dishesList;
    }

    private List<Ingredient> getIngredientsNeededForDish(int dishID) throws SQLException
    {
        Connection helperConnection=DriverManager.getConnection("jdbc:sqlite:IsNeededFor.db");

        PreparedStatement joinStatement=helperConnection.prepareStatement("SELECT i.id, i.name, i.shelf, i.store FROM Ingredients i JOIN IsNeededFor n ON i.id=n.ingredientID WHERE n.dishID=?;");
        joinStatement.setInt(1,dishID);
        ResultSet ingredientsResultSet=joinStatement.executeQuery();

        List<Ingredient> ingredientsList=new ArrayList<>();
        while(ingredientsResultSet.next())
        {
            ingredientsList.add(new Ingredient(ingredientsResultSet.getInt("id"), ingredientsResultSet.getString("name"), ingredientsResultSet.getString("store"), ingredientsResultSet.getInt("shelf")));
        }

        return ingredientsList;
    }

    @Override
    public int update(Dish dishToReplace) throws SQLException
    {
        PreparedStatement updateStatement=connection.prepareStatement("UPDATE Dishes SET name=? WHERE id=?");
        updateStatement.setString(1, dishToReplace.getName());
        updateStatement.setInt(2, dishToReplace.getId());
        return updateStatement.executeUpdate();

    }

    @Override
    public void add(Dish dishToAdd) throws SQLException
    {
        PreparedStatement insertStatement=connection.prepareStatement("INSERT INTO Dishes(name) VALUES(?)");
        insertStatement.setString(1, dishToAdd.getName());
        insertStatement.execute();
    }

    @Override
    public void remove(Dish dishToBeDeleted) throws SQLException
    {
        PreparedStatement insertStatement=connection.prepareStatement("DELETE FROM Dishes WHERE id=?");
        insertStatement.setInt(1, dishToBeDeleted.getId());
        insertStatement.execute();
    }
}
