package controller;

import model.DatabaseTableAccessor;
import model.IngredientDatabaseTableAccessor;
import model.Ingredient;
import view.EditWindow;
import view.IngredientEditContentPanel;
import view.ListContentPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IngredientWindowContentProvider implements WindowContentProvider<ListContentPanel<Ingredient>>
{
    private ListContentPanel<Ingredient> content;
    private DatabaseTableAccessor<Ingredient> databaseTableAccessor;
    private List<Comparator<Ingredient>> comparators;
    private int currentComparatorIndex;

    public IngredientWindowContentProvider()
    {
        try
        {
            databaseTableAccessor=new IngredientDatabaseTableAccessor();
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("connection_to_database_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
            sqlException.printStackTrace();
        }
        catch (ClassNotFoundException classNotFoundException)
        {
            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("no_database_driver_available"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
        }

        currentComparatorIndex=0;

        comparators=new ArrayList<>();
        comparators.addAll(List.of(Comparator.comparing(ingredient -> ingredient.getName().toLowerCase()), Comparator.comparing(ingredient -> ingredient.getStore().toLowerCase()), Comparator.comparing(Ingredient::getShelf)));

        content=new ListContentPanel<>();

        updateListModel();
        setSortFunctionality();
        setAddFunctionality();
        setUpContextMenu();
    }

    @Override
    public ListContentPanel<Ingredient> getContent()
    {
        return content;
    }

    @Override
    public String getTitle()
    {
        return Localisator.getInstance().getString("ingredients");
    }

    private void setSortFunctionality()
    {
        content.onSortButtonClick(listContentPanel ->
        {
            changeCurrentComparator();
            updateListModel();
        });
    }

    private void setAddFunctionality()
    {
        content.onAddButtonClick(listContentPanel ->
        {
            Ingredient emptyIngredient=new Ingredient("", "", 0);

            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>(Localisator.getInstance().getString("edit"), new IngredientEditContentPanel(emptyIngredient), new Dimension(300, 200));
            editWindow.onApplyButtonClicl(ingredientEditContentPanel ->
            {
                if(ingredientEditContentPanel.getIngredientName().isBlank() || ingredientEditContentPanel.getStoreFieldValue().isBlank())
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, Localisator.getInstance().getString("all_fields_must_be_filled"), Localisator.getInstance().getString("information"), JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    {
                        databaseTableAccessor.add(new Ingredient(ingredientEditContentPanel.getIngredientName(), ingredientEditContentPanel.getStoreFieldValue(), ingredientEditContentPanel.getShelfFieldValue()));
                        updateListModel();
                    } catch (SQLException throwables)
                    {
                        editWindow.dispose();
                        JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("element_already_available"), Localisator.getInstance().getString("information"), JOptionPane.INFORMATION_MESSAGE);
                        throwables.printStackTrace();
                    }
                }
            });

            editWindow.showWindow();
        });
    }

    private void setUpContextMenu()
    {
        content.addMenuItem(Localisator.getInstance().getString("edit"), (listContentPanel, index) ->
        {
            Ingredient ingredientToChange=listContentPanel.getElementAt(index);

            System.out.println(ingredientToChange.getId());

            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>(Localisator.getInstance().getString("edit"), new IngredientEditContentPanel(ingredientToChange), new Dimension(300, 200));

            editWindow.onApplyButtonClicl((ingredientEditContentPanel ->
            {
                if(ingredientEditContentPanel.getIngredientName().isBlank() || ingredientEditContentPanel.getStoreFieldValue().isBlank())
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, Localisator.getInstance().getString("all_fields_must_be_filled"), Localisator.getInstance().getString("information"), JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    {
                        ingredientToChange.setName(ingredientEditContentPanel.getIngredientName());
                        ingredientToChange.setStore(ingredientEditContentPanel.getStoreFieldValue());
                        ingredientToChange.setShelf(ingredientEditContentPanel.getShelfFieldValue());

                        databaseTableAccessor.update(ingredientToChange);
                        updateListModel();
                    }
                    catch (SQLException throwables)
                    {
                        editWindow.dispose();
                        JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("element_already_available"), Localisator.getInstance().getString("information"), JOptionPane.INFORMATION_MESSAGE);
                        throwables.printStackTrace();
                    }
                }
            }));

            editWindow.showWindow();
        });

        content.addMenuItem(Localisator.getInstance().getString("remove"), (sortableListModel, index)->
        {
            Ingredient currentChoosenIngredient=sortableListModel.getElementAt(index);
            try
            {
                databaseTableAccessor.remove(currentChoosenIngredient.getId());
                updateListModel();
            }
            catch (SQLException throwables)
            {
                JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("removing_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
                throwables.printStackTrace();
            }
        });
    }

    private void updateListModel()
    {
        try
        {
            content.setElements(databaseTableAccessor.getAll());
            content.sortElements(comparators.get(currentComparatorIndex));
        }
        catch (SQLException throwables)
        {
            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("changing_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
            throwables.printStackTrace();
            System.exit(0);
        }
    }

    private void changeCurrentComparator()
    {
        currentComparatorIndex=(currentComparatorIndex+1)%comparators.size();
    }
}
