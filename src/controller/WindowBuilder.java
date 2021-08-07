package controller;



import model.Dish;
import model.Ingredient;

import view.*;

import javax.imageio.ImageIO;

import javax.swing.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

//Only for the macOS build
//import java.awt.desktop.AboutEvent;
//import java.awt.desktop.AboutHandler;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;



public class WindowBuilder
{
    private MainWindow window;

    private List<PairedValue<String, JComponent>> contents;
    private int currentContentIndex;
    private WindowContentProvider<ListContentPanel<Dish>> dishWindowContentProvider;
    private WindowContentProvider<ListContentPanel<Ingredient>> ingredientWindowContentProvider;
    private WindowContentProvider<TextContentPanel> resultWindowContentProvider;
    private SettingsMenu settingsMenu;
    private AboutDialog aboutDialog;

    private Function<List<Ingredient>, String> listToShoppingListStringConverter;

    private Localisator localisator;



    public WindowBuilder()
    {
        contents=new ArrayList<>();

        currentContentIndex=0;

        dishWindowContentProvider=new DishWindowContentProvider();
        contents.add(new Pair<>(dishWindowContentProvider.getTitle(), dishWindowContentProvider.getContent()));

        ingredientWindowContentProvider=new IngredientWindowContentProvider();
        contents.add(new Pair<>(ingredientWindowContentProvider.getTitle(), ingredientWindowContentProvider.getContent()));

        resultWindowContentProvider=new ResultWindowContentProvider();
        contents.add(new Pair<>(resultWindowContentProvider.getTitle(), resultWindowContentProvider.getContent()));

        window=new MainWindow("ShoppingListGenerator", new Dimension(500, 500));
        window.setContent(dishWindowContentProvider.getContent());
        window.setCurrentTitle(dishWindowContentProvider.getTitle());
        window.setBackButtonVisible(false);

        listToShoppingListStringConverter=new MarkdownListToShoppingListStringConverter();

        localisator=new Localisator();

        Image iconSmall= null;
        try
        {
            iconSmall = ImageIO.read(getClass().getClassLoader().getResource("icon_small.png"));
        }
        catch (IOException IOException)
        {
            JOptionPane.showMessageDialog(window, localisator.getString("an_error_occurred")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"),JOptionPane.WARNING_MESSAGE);
            IOException.printStackTrace();
        }

        aboutDialog=new AboutDialog(window, new Dimension(280, 250));
        aboutDialog.setIcon(iconSmall);
        aboutDialog.setApplicationName(localisator.getString("application_name"));
        aboutDialog.setVersion(localisator.getString("version")+" "+localisator.getString("version_number"));
        aboutDialog.addDeveloper(localisator.getString("steven_solleder").toUpperCase(), localisator.getString("steven_solleder_link"));
        aboutDialog.addDeveloper(localisator.getString("isabell_waas").toUpperCase(), localisator.getString("isabell_waas_link"));

        Image icon= null;
        try
        {
            icon = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
        }
        catch (IOException ioException)
        {
            JOptionPane.showMessageDialog(window, localisator.getString("an_error_occurred")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"),JOptionPane.WARNING_MESSAGE);
            ioException.printStackTrace();
        }

        //Only for the macOS build
        /*Desktop desktop;
        if(Desktop.isDesktopSupported()&&(desktop=Desktop.getDesktop()).isSupported(Desktop.Action.APP_ABOUT))
        {
            desktop.setAboutHandler((e)-> aboutDialog.showDialog());
        }*/


        window.setIconImage(icon);

        setSettingsFunctionality();
        setForwardFunctionality();
        setBackwardFunctionality();

        window.showWindow();
    }

    private void setSettingsFunctionality()
    {
        window.onSettingsButtonClick((JButton settingsButton) ->
        {
            if(settingsMenu==null)
            {
                settingsMenu=new SettingsMenu(window, settingsButton, aboutDialog);
            }

            settingsMenu.showMenu();
        });
    }

    private void setForwardFunctionality()
    {
        window.onForwardButtonClick((JButton forwardButton) ->
        {
            if(currentContentIndex+1>contents.size()-1)
            {
                JPopupMenu lastActionsMenu=new JPopupMenu();
                String shoppinglist=resultWindowContentProvider.getContent().getText();

                JMenuItem copyMenuItem=new JMenuItem(localisator.getString("copy_to_clipboard"));
                copyMenuItem.addActionListener((event)->Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(shoppinglist), null));

                JMenuItem saveMenuItem=new JMenuItem(localisator.getString("save_on_computer"));
                saveMenuItem.addActionListener((event)->
                {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
                    {
                        try
                        {
                            FileWriter fileWriter=new FileWriter(fileChooser.getSelectedFile()+".txt");
                            fileWriter.write(shoppinglist);
                            fileWriter.close();
                        }
                        catch (IOException ioException)
                        {
                            JOptionPane.showMessageDialog(new JFrame(), localisator.getString("saving_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.ERROR_MESSAGE);
                            ioException.printStackTrace();
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
                    ingredientWindowContentProvider.getContent().setSelectedItems(dishWindowContentProvider.getContent().getUnmodifiableSelectedItems().stream().map((Dish dish)->dish.getUnmodifiableIngredients()).flatMap(list->list.stream()).collect(Collectors.toList()));
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
            window.setBackButtonVisible(currentContentIndex--!= 1);
            updateWindowContent();
        });
    }


    private void updateWindowContent()
    {
        window.setCurrentTitle(contents.get(currentContentIndex).getKey());
        window.setContent(contents.get(currentContentIndex).getValue());
    }
}
