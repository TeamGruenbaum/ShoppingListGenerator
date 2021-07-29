package controller;

public class Launcher
{
    public static void main(String[] args)
    {
        //new WindowBuilder(new DishWindowContentProvider(), new IngredientWindowContentProvider(), new ResultWindowContentProvider());
        new WindowBuilder(new DishWindowContentProvider());
    }
}
