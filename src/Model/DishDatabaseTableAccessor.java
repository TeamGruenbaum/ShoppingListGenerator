package model;

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
        statement.execute("CREATE TABLE IF NOT EXISTS Ingredients(id integer primary key , name text not null, store text not null, shelf integer not null, CONSTRAINT uniqueIngredient UNIQUE(name, store, shelf))");
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
        PreparedStatement joinStatement=connection.prepareStatement("SELECT i.id, i.name, i.shelf, i.store FROM Ingredients i JOIN IsNeededFor n ON i.id=n.ingredientID WHERE n.dishID=?;");
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
    public int update(Dish item) throws SQLException
    {
        if(item.getId()==-1) throw new SQLException("Invalid ID (-1)");

        removeFromIsNeededFor(item.getId());
        addToIsNeededFor(item);

        PreparedStatement updateDishesStatement=connection.prepareStatement("UPDATE Dishes SET name=? WHERE id=?");
        updateDishesStatement.setString(1, item.getName());
        updateDishesStatement.setInt(2, item.getId());
        return updateDishesStatement.executeUpdate();
    }

    @Override
    public void add(Dish item) throws SQLException
    {
        PreparedStatement insertIntoDishesStatement=connection.prepareStatement("INSERT INTO Dishes(name) VALUES(?)");
        insertIntoDishesStatement.setString(1, item.getName());
        insertIntoDishesStatement.execute();

        PreparedStatement idQuery = connection.prepareStatement("SELECT id FROM Dishes WHERE name=?");
        idQuery.setString(1, item.getName());
        ResultSet dishIdResultSet = idQuery.executeQuery();
        item.setId(dishIdResultSet.getInt("id"));

        addToIsNeededFor(item);
    }

    @Override
    public void remove(int id) throws SQLException
    {
        removeFromIsNeededFor(id);

        PreparedStatement deleteFromDishesStatement=connection.prepareStatement("DELETE FROM Dishes WHERE id=?");
        deleteFromDishesStatement.setInt(1, id);
        deleteFromDishesStatement.execute();
    }

    private void addToIsNeededFor(Dish item) throws SQLException
    {
        PreparedStatement insertIntoIsNeededForStatement=connection.prepareStatement("INSERT INTO IsNeededFor(dishID, IngredientID) VALUES(?,?)");
        insertIntoIsNeededForStatement.setInt(1,item.getId());;
        for (Ingredient ingredient:item.getIngredients())
        {
            insertIntoIsNeededForStatement.setInt(2,ingredient.getId());
            insertIntoIsNeededForStatement.execute();
        }
    }

    private void removeFromIsNeededFor(int id) throws SQLException
    {
        PreparedStatement deleteFromIsNeededForStatement=connection.prepareStatement("DELETE FROM IsNeededFor WHERE dishID=?");
        deleteFromIsNeededForStatement.setInt(1, id);
        deleteFromIsNeededForStatement.execute();
    }
}