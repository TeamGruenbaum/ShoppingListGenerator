package controller;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import view.SettingsMenu;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class ThemeSetter
{
    Preferences preferences;


    public ThemeSetter()
    {
        preferences = Preferences.userNodeForPackage(ThemeSetter.class);
    }

    public void applyCurrentThemeSetting()
    {
        try
        {
            if(preferences.getInt("app_theme", Theme.LIGHT.getValue())==Theme.LIGHT.getValue())
            {
                System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAqua");
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            else
            {
                System.setProperty("apple.awt.application.appearance", "NSAppearanceNameAqua");
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        }
        catch (UnsupportedLookAndFeelException e)
        {
            JOptionPane.showMessageDialog(null, "Theme could not be changed", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void switchThemeSetting()
    {
        if(preferences.getInt("app_theme", Theme.LIGHT.getValue())==Theme.LIGHT.getValue())
        {
            preferences.putInt("app_theme", Theme.DARK.getValue());
        }
        else
        {
            preferences.putInt("app_theme", Theme.LIGHT.getValue());
        }
    }
}
