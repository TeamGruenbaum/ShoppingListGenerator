package model;

import model.Ingredient;
import model.SimpleSortedListModel;

import java.io.Serializable;

public class Dish implements Serializable
{
    private String name;
    private SimpleSortedListModel<Ingredient> ingredients;


    public Dish(String name)
    {
        this.name = name;
        this.ingredients=new Ingredient;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public SimpleSortedListModel<Ingredient> getIngredientsSimpleSortedListModel()
    {
        return ingredients;
    }


    @Override
    public String toString()
    {
        return name;
    }
}
