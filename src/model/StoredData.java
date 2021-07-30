package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StoredData implements Serializable
{
    private List<Dish> dishes;
    private List<Ingredient> ingredients;

    public List<Dish> getUnmodifiableDishes()
    {
        return Collections.unmodifiableList(dishes);
    }

    public List<Ingredient> getUnmodifiableIngredients()
    {
        return Collections.unmodifiableList(ingredients);
    }

    public void setUnmodifiableDishes(List<Dish> dishes)
    {
        this.dishes=dishes;
    }

    public void setUnmodifiableIngredients(List<Ingredient> ingredients)
    {
        this.ingredients=ingredients;
    }
}
