package model;


import controller.Localisator;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;



public class DishDatabaseTableAccessor implements DatabaseTableAccessor<Dish>
{
    private Connection connection;


    public DishDatabaseTableAccessor() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        connection=DatabaseConnection.getInstance().getConnection();

        Statement statement=connection.createStatement();

        if(!(connection.getMetaData().getTables(null, null, "Dishes", null).next()))
        {
            statement.execute("CREATE TABLE IF NOT EXISTS Dishes(id integer primary key, name text unique not null)");
            statement.execute("CREATE TABLE IF NOT EXISTS IsNeededFor(dishID integer, ingredientID integer, CONSTRAINT primaryKey PRIMARY KEY (dishID, ingredientID))");

            Localisator localisator=new Localisator();
            statement.execute("INSERT INTO Dishes(name) VALUES ('"+ localisator.getString("spaghetti") +"'), ('"+ localisator.getString("pizza")+"')");
            statement.execute("INSERT INTO IsNeededFor(dishID, ingredientID) VALUES (1, 1), (1, 4), (1, 5), (2, 2), (2, 6), (2, 7), (2, 8)");
        }
    }


    @Override
    public List<Dish> getAll() throws SQLException
    {
        ResultSet dishesResultSet=connection.createStatement().executeQuery("SELECT * FROM Dishes");

        List<Dish> allDishes=new ArrayList<>();
        while(dishesResultSet.next())
        {
            allDishes.add(new Dish(dishesResultSet.getInt("id"), dishesResultSet.getString("name"), getIngredientsNeededForDish(dishesResultSet.getInt("id"))));
        }

        return allDishes;
    }

    @Override
    public int update(Dish element) throws SQLException
    {
        if(element.getId()==-1) throw new SQLException("Invalid ID (-1)");

        removeFromIsNeededFor(element.getId());
        addToIsNeededFor(element.getId(), element.getUnmodifiableIngredients());

        PreparedStatement updateDishesStatement=connection.prepareStatement("UPDATE Dishes SET name=? WHERE id=?");
        updateDishesStatement.setString(1, element.getName());
        updateDishesStatement.setInt(2, element.getId());

        return updateDishesStatement.executeUpdate();
    }

    @Override
    public void add(Dish element) throws SQLException
    {
        PreparedStatement insertIntoDishesStatement=connection.prepareStatement("INSERT INTO Dishes(name) VALUES(?)");
        insertIntoDishesStatement.setString(1, element.getName());
        insertIntoDishesStatement.execute();

        PreparedStatement idQuery = connection.prepareStatement("SELECT id FROM Dishes WHERE name=?");
        idQuery.setString(1, element.getName());

        addToIsNeededFor(idQuery.executeQuery().getInt("id"), element.getUnmodifiableIngredients());
    }

    @Override
    public void remove(int id) throws SQLException
    {
        removeFromIsNeededFor(id);

        PreparedStatement deleteFromDishesStatement=connection.prepareStatement("DELETE FROM Dishes WHERE id=?");
        deleteFromDishesStatement.setInt(1, id);
        deleteFromDishesStatement.execute();
    }


    private List<Ingredient> getIngredientsNeededForDish(int dishId) throws SQLException
    {
        PreparedStatement joinStatement=connection.prepareStatement("SELECT i.id, i.name, i.shelf, i.store FROM Ingredients i JOIN IsNeededFor n ON i.id=n.ingredientID WHERE n.dishID=?;");
        joinStatement.setInt(1,dishId);
        ResultSet ingredientsResultSet=joinStatement.executeQuery();

        List<Ingredient> ingredientsList=new ArrayList<>();
        while(ingredientsResultSet.next())
        {
            ingredientsList.add(new Ingredient(ingredientsResultSet.getInt("id"), ingredientsResultSet.getString("name"), ingredientsResultSet.getString("store"), ingredientsResultSet.getInt("shelf")));
        }

        return ingredientsList;
    }

    private void addToIsNeededFor(int dishId, List<Ingredient> ingredients) throws SQLException
    {
        PreparedStatement insertIntoIsNeededForStatement=connection.prepareStatement("INSERT INTO IsNeededFor(dishID, IngredientID) VALUES(?,?)");
        insertIntoIsNeededForStatement.setInt(1,dishId);;

        for (Ingredient ingredient:ingredients)
        {
            insertIntoIsNeededForStatement.setInt(2,ingredient.getId());
            insertIntoIsNeededForStatement.execute();
        }
    }

    private void removeFromIsNeededFor(int dishId) throws SQLException
    {
        PreparedStatement deleteFromIsNeededForStatement=connection.prepareStatement("DELETE FROM IsNeededFor WHERE dishID=?");
        deleteFromIsNeededForStatement.setInt(1, dishId);
        deleteFromIsNeededForStatement.execute();
    }
}
