package controller;



import java.util.ResourceBundle;



public class Localisator
{
    private ResourceBundle resourceBundle;



    public Localisator()
    {
        this.resourceBundle = ResourceBundle.getBundle("resources.strings");
    }


    public String getString(String key)
    {
        return resourceBundle.getString(key);
    }
}
