package controller;

import java.util.Locale;
import java.util.ResourceBundle;

public class Localisator
{
    private static Localisator instance=null;

    private ResourceBundle resourceBundle;


    
    private Localisator()
    {
        this.resourceBundle = ResourceBundle.getBundle("resource.string");
    }

    public String getString(String key)
    {
        return resourceBundle.getString(key);
    }


    public static Localisator getInstance()
    {
        if(instance==null)
        {
            instance=new Localisator();
        }

        return instance;
    }
}
