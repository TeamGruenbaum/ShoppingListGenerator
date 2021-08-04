package model;


public class Ingredient implements Identifiable
{
    private int id;
    private String name;
    private String store;
    private int shelf;


    public Ingredient(String name, String store, int shelf)
    {
        this.id=-1;
        this.name=name;
        this.store=store;
        this.shelf=shelf;
    }

    Ingredient(int id, String name, String store, int shelf)
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

    public void setName(String newValue)
    {
        this.name = newValue;
    }

    public String getStore()
    {
        return store;
    }

    public void setStore(String newValue)
    {
        this.store = newValue;
    }

    public int getShelf()
    {
        return shelf;
    }

    public void setShelf(int newValue)
    {
        this.shelf = newValue;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
