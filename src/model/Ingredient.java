package model;

import java.io.Serializable;

public class Ingredient implements Serializable
{
    private String name;
    private String store;
    private int shelf;
    private SimpleSortedListModel<Dish> dishesContainedBy;


    public Ingredient(String name, String store, int shelf)
    {
        this.name = name;
        this.store = store;
        this.shelf = shelf;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStore()
    {
        return store;
    }

    public void setStore(String store)
    {
        this.store = store;
    }

    public int getShelf()
    {
        return shelf;
    }

    public void setShelf(int shelf)
    {
        this.shelf = shelf;
    }

    public SimpleSortedListModel<Dish> getDishesContainedBy()
    {
        return dishesContainedBy;
    }
}
