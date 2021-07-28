package model;

import java.util.ArrayList;
import java.util.List;

public class Dish
{
    private String name;
    private List<Ingredient> ingredients;


    public Dish(String name)
    {
        this.name = name;
        ingredients=new ArrayList<>();
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public List<Ingredient> getIngredients()
    {
        return new ArrayList<Ingredient>(this.ingredients);
    }

    @Override
    public String toString()
    {
        return name;
    }
}
