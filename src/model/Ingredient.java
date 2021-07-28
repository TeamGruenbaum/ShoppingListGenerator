package model;

public class Ingredient
{
    private String name;
    private String store;
    private int shelf;


    public Ingredient(String name, String store, int shelf)
    {
        this.name = name;
        this.store = store;
        this.shelf = shelf;
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
}
