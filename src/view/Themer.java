package view;



import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import controller.Localisator;

import javax.swing.*;
import java.util.prefs.Preferences;



public final class Themer
{
    private Preferences preferences;


    public Themer()
    {
        preferences = Preferences.userNodeForPackage(Themer.class);
    }


    public void applyCurrentThemeSetting()
    {
        Localisator localisator=new Localisator();

        try
        {
            if(preferences.getInt("app_theme", Theme.LIGHT.ordinal())==Theme.LIGHT.ordinal())
            {
                System.setProperty("apple.awt.application.appearance", "NSAppearanceNameAqua");
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            else
            {
                System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAqua");
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
        }
        catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException)
        {
            JOptionPane.showMessageDialog(null, localisator.getString("loadling_not_possible"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
            unsupportedLookAndFeelException.printStackTrace();
        }
    }

    public void switchThemeSetting()
    {
        if(preferences.getInt("app_theme", Theme.LIGHT.ordinal())==Theme.LIGHT.ordinal())
        {
            preferences.putInt("app_theme", Theme.DARK.ordinal());
        }
        else
        {
            preferences.putInt("app_theme", Theme.LIGHT.ordinal());
        }
    }

    public Theme getCurrentTheme()
    {
        return Theme.values()[preferences.getInt("app_theme", Theme.LIGHT.ordinal())];
    }
}
