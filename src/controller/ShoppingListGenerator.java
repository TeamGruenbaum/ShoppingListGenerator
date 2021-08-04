package controller;


import view.Themer;

public class ShoppingListGenerator
{
    public static void main(String[] args)
    {
        new Themer().applyCurrentThemeSetting();
        new WindowBuilder();
    }
}
