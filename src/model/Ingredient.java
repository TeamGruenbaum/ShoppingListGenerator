package model;

import java.io.Serializable;

public class Ingredient
{
    private int id;
    private String name;
    private String store;
    private int shelf;


    public Ingredient(int id, String name, String store, int shelf)
    {
        this.id=id;
        this.name=name;
        this.store=store;
        this.shelf=shelf;
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

    @Override
    public String toString()
    {
        return name;
    }
}
