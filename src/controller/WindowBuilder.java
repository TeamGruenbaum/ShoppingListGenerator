package controller;

import model.Dish;
import model.Ingredient;
import view.ListContentPanel;
import view.MainWindow;
import view.SettingsMenu;
import view.TextContentPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WindowBuilder
{
    private MainWindow window;
    private SettingsMenu settingsMenu;
    private List<PairedValue<String, JComponent>> contents;
    private int currentContentIndex;
    private Function<List<Ingredient>, String> listToShoppingListStringConverter;

    private WindowContentProvider<ListContentPanel<Dish>> dishWindowContentProvider;
    private WindowContentProvider<ListContentPanel<Ingredient>> ingredientWindowContentProvider;
    private WindowContentProvider<TextContentPanel> resultWindowContentProvider;

    public WindowBuilder()
    {
        contents=new ArrayList<>();

        dishWindowContentProvider=new DishWindowContentProvider();
        contents.add(new Pair<>(dishWindowContentProvider.getTitle(), dishWindowContentProvider.getContent()));

        ingredientWindowContentProvider=new IngredientWindowContentProvider();
        contents.add(new Pair<>(ingredientWindowContentProvider.getTitle(), ingredientWindowContentProvider.getContent()));

        resultWindowContentProvider=new ResultWindowContentProvider();
        contents.add(new Pair<>(resultWindowContentProvider.getTitle(), resultWindowContentProvider.getContent()));

        currentContentIndex=0;
        window=new MainWindow("ShoppingListGenerator",dishWindowContentProvider.getTitle() ,contents.get(currentContentIndex).getValue(), new Dimension(500, 500));
        window.setBackVisible(false);

        settingsMenu=new SettingsMenu(window);

        listToShoppingListStringConverter=new MarkdownListToShoppingListStringConverter();

        setSettingsFunctionality();
        setForwardFunctionality();
        setBackwardFunctionality();

        Desktop.getDesktop().setAboutHandler((e)->settingsMenu.getAboutDialog(window));

        try
        {
            Image taskImage= ImageIO.read(getClass().getResource("../resources/icon.png"));
            Taskbar.getTaskbar().setIconImage(taskImage);
            window.setIconImage(taskImage);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    private void setSettingsFunctionality()
    {
        window.onSettingsClick((JButton settingsButton) ->
        {
            settingsMenu.showMenuAt(settingsButton);
        });
    }

    private void setForwardFunctionality()
    {
        window.onForwardClick((JButton forwardButton) ->
        {
            if(currentContentIndex+1>contents.size()-1)
            {
                JPopupMenu lastActionsMenu=new JPopupMenu();
                String shoppinglist = resultWindowContentProvider.getContent().getText();

                JMenuItem copyMenuItem=new JMenuItem(Localisator.getInstance().getString("copy_to_clipboard"));
                copyMenuItem.addActionListener((event)->
                {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(shoppinglist), null);
                });

                JMenuItem saveMenuItem=new JMenuItem(Localisator.getInstance().getString("save_on_computer"));
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
                            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("saving_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.ERROR_MESSAGE);
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
                    System.out.println(dishWindowContentProvider.getContent().getUnmodifiableSelectedItems().stream().map((Dish dish) -> {
                        return dish.getIngredients();
                    }).flatMap(list -> {
                        return list.stream();
                    }).collect(Collectors.toList()));
                    ingredientWindowContentProvider.getContent().setSelectedItems(dishWindowContentProvider.getContent().getUnmodifiableSelectedItems().stream().map((Dish dish)->{return dish.getIngredients();}).flatMap(list->{return list.stream();}).collect(Collectors.toList()));
                }
                if(currentContentIndex==contents.size()-1)
                {
                    resultWindowContentProvider.getContent().setText(listToShoppingListStringConverter.apply(ingredientWindowContentProvider.getContent().getUnmodifiableSelectedItems()));
                }
            }

            window.setBackVisible(true);
        });
    }


    private void setBackwardFunctionality()
    {
        window.onBackClick((JButton backButton)->
        {
            if(currentContentIndex--==1)
            {
                window.setBackVisible(false);
            }
            else
            {
                window.setBackVisible(true);
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
