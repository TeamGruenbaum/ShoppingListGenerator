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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WindowBuilder
{
    private MainWindow window;
    private SettingsMenu settingsMenu;
    private List<PairedValue<String, JComponent>> contents;
    private int currentContentIndex;

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
        window=new MainWindow("ShoppingListGenerator",contents.get(currentContentIndex).getValue(), new Dimension(400, 400));
        window.setBackVisible(false);

        settingsMenu=new SettingsMenu(window);

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

                JMenuItem copyMenuItem=new JMenuItem("Copy to Clipboard");
                copyMenuItem.addActionListener((event)->
                {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(shoppinglist), null);
                });

                JMenuItem saveMenuItem=new JMenuItem("Save on your Computer");
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
                            JOptionPane.showMessageDialog(new JFrame(), "The file could not be saved.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    resultWindowContentProvider.getContent().setText(generateShoppingList(ingredientWindowContentProvider.getContent().getUnmodifiableSelectedItems()));
                }
            }

            window.setBackVisible(true);
        });
    }

    private String generateShoppingList(List<Ingredient> ingredientsToBuy)
    {
        String shoppingList="";

        List<PairedValue<String, ArrayList<Ingredient>>> ingredientsAndStores=new ArrayList<>();
        for(String storeName:ingredientsToBuy.stream().map(ingredient -> {return ingredient.getStore();}).collect(Collectors.toList()))
        {
            if(!(ingredientsAndStores.stream().map(pair->pair.getKey()).anyMatch(item->item.equals(storeName))))
            {
                ingredientsAndStores.add(new Pair<>(storeName, new ArrayList<>()));
            }
        }
        ingredientsAndStores.sort(Comparator.comparing(PairedValue::getKey));

        for (PairedValue<String, ArrayList<Ingredient>> storeIngredientsPair:ingredientsAndStores)
        {
            shoppingList=addItemsOfStore(shoppingList,ingredientsToBuy,storeIngredientsPair);
        }

        return shoppingList;
    }

    private String addItemsOfStore(String shoppingList, List<Ingredient> ingredientsToBuy, PairedValue<String, ArrayList<Ingredient>> storeIngredientsPair)
    {
        for(Ingredient ingredient:ingredientsToBuy)
        {
            if(ingredient.getStore().equals(storeIngredientsPair.getKey()))
            {
                storeIngredientsPair.getValue().add(ingredient);
            }
        }
        storeIngredientsPair.getValue().sort(Comparator.comparingInt(Ingredient::getShelf));

        int currentShelfNumber=storeIngredientsPair.getValue().get(0).getShelf();
        shoppingList+="# "+storeIngredientsPair.getKey()+"\n## Regal "+currentShelfNumber;

        for(int index=0; index<storeIngredientsPair.getValue().size(); index++)
        {
            if(storeIngredientsPair.getValue().get(index).getShelf()==currentShelfNumber)
            {
                shoppingList+="\n- [ ] "+storeIngredientsPair.getValue().get(index).getName();
            }
            else
            {
                currentShelfNumber=storeIngredientsPair.getValue().get(index).getShelf();
                shoppingList+="\n\n## Regal "+currentShelfNumber;
                index--;
            }
        }
        return shoppingList+"\n\n";
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
