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
        this.ingredients=new ArrayList<>();
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
}
