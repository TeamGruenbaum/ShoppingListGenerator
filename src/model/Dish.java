package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dish
{
    private int id;
    private String name;
    private List<Ingredient> ingredients;


    public Dish(int id, String name, List<Ingredient> ingredients)
    {
        this.id=id;
        this.name=name;
        this.ingredients=ingredients;
    }


    public int getId()
    {
        return id;
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
        return ingredients;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
