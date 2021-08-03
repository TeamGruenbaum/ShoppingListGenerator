package controller;

import java.util.ResourceBundle;

public class PathHelper
{
    public String getSavePath()
    {
        return System.getProperty("user.home")+(System.getProperty("os.name").contains("Windows")?"\\ShoppingListGenerator\\":"/ShoppingListGenerator/");
    }

    public String getDatabaseName()
    {
        return "DishesAndIngredients.db";
    }
}
