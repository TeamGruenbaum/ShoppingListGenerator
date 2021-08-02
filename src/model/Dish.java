package model;

import controller.Identifiable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dish implements Identifiable {
    private int id;
    private String name;
    private List<Ingredient> ingredients;


    public Dish(String name, List<Ingredient> ingredients)
    {
        this.id=-1;
        this.name=name;
        this.ingredients=ingredients;
    }

    Dish(int id, String name, List<Ingredient> ingredients)
    {
        this.id=id;
        this.name=name;
        this.ingredients=ingredients;
    }

    public int getId()
    {
        return id;
    }

    void setId(int id)
    {
        this.id=id;
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
