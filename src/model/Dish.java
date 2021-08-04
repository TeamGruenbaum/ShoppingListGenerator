package model;


import java.util.Collections;
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

    public String getName()
    {
        return name;
    }

    public void setName(String newValue)
    {
        this.name=newValue;
    }

    public List<Ingredient> getUnmodifiableIngredients()
    {
        return Collections.unmodifiableList(ingredients);
    }

    public void setAllIngredients(List<Ingredient> newValues)
    {
        ingredients.clear();
        ingredients.addAll(newValues);
    }

    @Override
    public String toString()
    {
        return name;
    }
}
