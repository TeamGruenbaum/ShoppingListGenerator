package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dish implements Serializable
{
    private String name;
    private SimpleSortedListModel<Ingredient> ingredients;


    public Dish(String name)
    {
        this.name = name;
        this.ingredients= new SimpleSortedListModel<Ingredient>(new ArrayList<Ingredient>(), (List<Ingredient> list, Ingredient item) ->
        {
            item.getDishesContainedBy().add(this);
            list.add(item);
        },
        (list, item) ->
        {
            list.remove(item);
        });
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
