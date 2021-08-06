package controller;

import model.DatabaseConnection;
import model.Dish;
import model.Ingredient;
import view.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.apple.eawt.*;

public class WindowBuilder
{
    private MainWindow window;
    private List<PairedValue<String, JComponent>> contents;
    private int currentContentIndex;
    private Function<List<Ingredient>, String> listToShoppingListStringConverter;


    private WindowContentProvider<ListContentPanel<Dish>> dishWindowContentProvider;
    private WindowContentProvider<ListContentPanel<Ingredient>> ingredientWindowContentProvider;
    private WindowContentProvider<TextContentPanel> resultWindowContentProvider;
    private AboutDialog aboutDialog;

    private Localisator localisator;

    public WindowBuilder()
    {
        localisator=new Localisator();

        contents=new ArrayList<>();

        dishWindowContentProvider=new DishWindowContentProvider();
        contents.add(new Pair<>(dishWindowContentProvider.getTitle(), dishWindowContentProvider.getContent()));

        ingredientWindowContentProvider=new IngredientWindowContentProvider();
        contents.add(new Pair<>(ingredientWindowContentProvider.getTitle(), ingredientWindowContentProvider.getContent()));

        resultWindowContentProvider=new ResultWindowContentProvider();
        contents.add(new Pair<>(resultWindowContentProvider.getTitle(), resultWindowContentProvider.getContent()));

        currentContentIndex=0;
        window=new MainWindow("ShoppingListGenerator", new Dimension(500, 500));
        window.setContent(dishWindowContentProvider.getContent());
        window.setCurrentTitle(dishWindowContentProvider.getTitle());
        window.setBackButtonVisible(false);

        listToShoppingListStringConverter=new MarkdownListToShoppingListStringConverter();

        aboutDialog=new AboutDialog(window, new Dimension(280, 250));

        Image iconsSmall= null;
        try
        {
            iconsSmall = ImageIO.read(getClass().getClassLoader().getResource("icon_small.png"));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            System.exit(0);
        }
        aboutDialog.setIcon(iconsSmall);
        aboutDialog.setApplicationName(localisator.getString("application_name"));
        aboutDialog.setVersion(localisator.getString("version"));
        aboutDialog.addDeveloper(localisator.getString("steven_solleder").toUpperCase(), localisator.getString("steven_solleder_link"));
        aboutDialog.addDeveloper(localisator.getString("isabell_waas").toUpperCase(), localisator.getString("isabell_waas_link"));

        setSettingsFunctionality();
        setForwardFunctionality();
        setBackwardFunctionality();

        Image icon= null;
        try
        {
            icon = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            System.exit(0);
        }

        if(System.getProperty("os.name").toLowerCase().contains("mac"))
        {
            Application application=Application.getApplication();
            application.setAboutHandler((e)->
            {
                aboutDialog.showDialog();
            });

            application.setDockIconImage(icon);
        }

        window.setIconImage(icon);


        window.showWindow();
    }

    private void setSettingsFunctionality()
    {
        window.onSettingsButtonClick((JButton settingsButton) ->
        {
            JPopupMenu settingsMenu=new JPopupMenu();

            JMenuItem aboutMenuItem=new JMenuItem(localisator.getString("about")+" "+localisator.getString("application_name"));
            aboutMenuItem.addActionListener((aboutEvent)->
            {
                aboutDialog.showDialog();
            });

            JMenuItem appearanceMenuItem=new JMenuItem(localisator.getString("switch_application_theme"));
            appearanceMenuItem.addActionListener((actionEvent) ->
            {
                new Themer().switchThemeSetting();

                JOptionPane.showMessageDialog(null, localisator.getString("please_restart_application"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
            });

            JMenuItem resetMenuItem=new JMenuItem(localisator.getString("reset_application"));
            resetMenuItem.addActionListener((aboutEvent)->
            {
                PathHelper pathHelper=new PathHelper();
                try {
                    DatabaseConnection.getInstance().getConnection().close();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                new File(pathHelper.getSavePath()+pathHelper.getDatabaseName()).delete();

                JOptionPane.showMessageDialog(window, localisator.getString("please_restart_application"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            });

            settingsMenu.add(aboutMenuItem);
            settingsMenu.add(appearanceMenuItem);
            settingsMenu.add(resetMenuItem);

            settingsMenu.show(settingsButton, settingsButton.getWidth()/2, settingsButton.getHeight()/2);
        });
    }

    private void setForwardFunctionality()
    {
        window.onForwardButtonClick((JButton forwardButton) ->
        {
            if(currentContentIndex+1>contents.size()-1)
            {
                JPopupMenu lastActionsMenu=new JPopupMenu();
                String shoppinglist = resultWindowContentProvider.getContent().getText();

                JMenuItem copyMenuItem=new JMenuItem(localisator.getString("copy_to_clipboard"));
                copyMenuItem.addActionListener((event)->
                {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(shoppinglist), null);
                });

                JMenuItem saveMenuItem=new JMenuItem(localisator.getString("save_on_computer"));
                saveMenuItem.addActionListener((event)->
                {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        try {
                            FileWriter fileWriter=new FileWriter(fileChooser.getSelectedFile()+".txt");
                            fileWriter.write(shoppinglist);
                            fileWriter.close();
                        }
                        catch (Exception ex)
                        {
                            JOptionPane.showMessageDialog(new JFrame(), localisator.getString("saving_not_possible"), localisator.getString("warning"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                lastActionsMenu.add(copyMenuItem);
                lastActionsMenu.add(saveMenuItem);

                lastActionsMenu.show(forwardButton, forwardButton.getWidth()/2, forwardButton.getHeight()/2);
            }
            else
            {
                ++currentContentIndex;
                updateWindowContent();

                if(currentContentIndex==contents.size()-2)
                {
                    ingredientWindowContentProvider.getContent().setSelectedItems(dishWindowContentProvider.getContent().getUnmodifiableSelectedItems().stream().map((Dish dish)->{return dish.getUnmodifiableIngredients();}).flatMap(list->{return list.stream();}).collect(Collectors.toList()));
                }
                if(currentContentIndex==contents.size()-1)
                {
                    resultWindowContentProvider.getContent().setText(listToShoppingListStringConverter.apply(ingredientWindowContentProvider.getContent().getUnmodifiableSelectedItems()));
                }
            }

            window.setBackButtonVisible(true);
        });
    }


    private void setBackwardFunctionality()
    {
        window.onBackButtonClick((JButton backButton)->
        {
            if(currentContentIndex--==1)
            {
                window.setBackButtonVisible(false);
            }
            else
            {
                window.setBackButtonVisible(true);
            }
            updateWindowContent();
        });
    }

    private void updateWindowContent()
    {
        window.setCurrentTitle(contents.get(currentContentIndex).getKey());
        window.setContent(contents.get(currentContentIndex).getValue());
    }
}
