package controller;

import model.Dish;
import model.Ingredient;
import view.ListContentPanel;
import view.MainWindow;
import view.TextContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WindowBuilder
{
    private MainWindow window;
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

        setSettingsFunctionality();
        setForwardFunctionality();
        setBackwardFunctionality();
    }

    private void setSettingsFunctionality()
    {
        window.onSettingsClick(() ->
        {
            //TODO: settings action
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
