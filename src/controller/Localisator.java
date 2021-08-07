package controller;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
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
