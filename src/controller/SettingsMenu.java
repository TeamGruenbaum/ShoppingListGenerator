package controller;



import model.DatabaseConnection;

import view.AboutDialog;
import view.LicensesDialog;
import view.Themer;

import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

import javax.swing.*;

import java.awt.*;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.charset.StandardCharsets;

import java.sql.SQLException;



public class SettingsMenu extends JPopupMenu
{
    private Localisator localisator;
    private AboutDialog aboutDialog;
    private JComponent invoker;



    public SettingsMenu(Window ownerWindow, JComponent invoker, AboutDialog aboutDialog)
    {
        this.localisator=new Localisator();

        this.aboutDialog=aboutDialog;

        this.invoker=invoker;

        JMenuItem aboutMenuItem=new JMenuItem(localisator.getString("about")+" "+localisator.getString("application_name"));
        aboutMenuItem.addActionListener((actionEvent)->this.aboutDialog.showDialog());

        JMenuItem appearanceMenuItem=new JMenuItem(localisator.getString("switch_theme"));
        appearanceMenuItem.addActionListener((actionEvent) ->
        {
            new Themer().switchThemeSetting();

            JOptionPane.showMessageDialog(null, localisator.getString("please_restart_application"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem resetMenuItem=new JMenuItem(localisator.getString("reset_data"));
        resetMenuItem.addActionListener((actionEvent)->
        {
            PathHelper pathHelper=new PathHelper();
            try
            {
                DatabaseConnection.getInstance().getConnection().close();
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
            new File(pathHelper.getSavePath()+pathHelper.getDatabaseName()).delete();

            JOptionPane.showMessageDialog(ownerWindow, localisator.getString("please_restart_application"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem updateApplication=new JMenuItem(localisator.getString("search_for_updates"));
        updateApplication.addActionListener((aboutEvent)->
        {
            try
            {
                boolean newVersionAvailable=false;
                JSONObject latestRelease;
                latestRelease = new JSONObject(IOUtils.toString(new URL("https://api.github.com/repos/TeamGruenbaum/ShoppingListGenerator/releases/latest"), StandardCharsets.UTF_8));

                if(Integer.parseInt(localisator.getString("version_number").substring(0, 4))<Integer.parseInt(latestRelease.getString("tag_name").substring(0, 4)))
                {
                    newVersionAvailable=true;
                }
                else
                {
                    if(Integer.parseInt(localisator.getString("version_number").substring(5, 6))<Integer.parseInt(latestRelease.getString("tag_name").substring(5, 6)))
                    {
                        newVersionAvailable=true;
                    }
                }

                if(newVersionAvailable)
                {
                    JOptionPane.showMessageDialog(ownerWindow, localisator.getString("newer_version_available"), localisator.getString("information"),JOptionPane.INFORMATION_MESSAGE);

                    Desktop desktop;
                    if(Desktop.isDesktopSupported() && (desktop=Desktop.getDesktop()).isSupported(Desktop.Action.BROWSE))
                    {
                        desktop.browse(new URI(latestRelease.getString("html_url")));
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(ownerWindow, localisator.getString("this_is_the_latest_version"), localisator.getString("information"),JOptionPane.INFORMATION_MESSAGE);
                }
            }
            catch (IOException | URISyntaxException exception)
            {
                JOptionPane.showMessageDialog(ownerWindow, localisator.getString("an_error_occurred"), localisator.getString("warning"),JOptionPane.WARNING_MESSAGE);
                exception.printStackTrace();
            }
        });

        JMenuItem licensesMenuItem=new JMenuItem(localisator.getString("licenses"));
        licensesMenuItem.addActionListener((actionEvent)->
        {
            LicensesDialog licensesDialog=new LicensesDialog(ownerWindow, new Dimension(500, 400));

            licensesDialog.addLicensePanel(localisator.getString("apache_license_libraries"), "apache_license.txt");
            licensesDialog.addLicensePanel(localisator.getString("json_license_libraries"), "json_license.txt");

            licensesDialog.showDialog();
        });

        add(appearanceMenuItem);
        add(updateApplication);
        add(resetMenuItem);
        add(licensesMenuItem);
        add(aboutMenuItem);
    }


    public void showMenu()
    {
        show(invoker, invoker.getWidth()/2, invoker.getHeight()/2);
    }
}
