package controller;



import model.DatabaseTableAccessor;
import model.IngredientDatabaseTableAccessor;
import model.Ingredient;

import org.apache.commons.lang.StringUtils;

import view.EditWindow;
import view.IngredientEditContentPanel;
import view.ListContentPanel;

import javax.swing.*;

import java.awt.*;

import java.io.File;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class IngredientWindowContentProvider implements WindowContentProvider<ListContentPanel<Ingredient>>
{
    private ListContentPanel<Ingredient> content;
    private DatabaseTableAccessor<Ingredient> ingredientDatabaseTableAccessor;

    private List<Comparator<Ingredient>> comparators;
    private int currentComparatorIndex;

    private Localisator localisator;



    public IngredientWindowContentProvider()
    {
        localisator=new Localisator();

        try
        {
            ingredientDatabaseTableAccessor=new IngredientDatabaseTableAccessor();
        }
        catch (ClassNotFoundException classNotFoundException)
        {
            JOptionPane.showMessageDialog(null, localisator.getString("no_database_driver_available")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(null, localisator.getString("connection_to_database_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
            sqlException.printStackTrace();
        }

        currentComparatorIndex=0;
        comparators=new ArrayList<>();
        comparators.add(Comparator.comparing(ingredient->ingredient.getName().toLowerCase()));
        comparators.add(Comparator.comparing(ingredient->ingredient.getStore().toLowerCase()));
        comparators.add(Comparator.comparing(Ingredient::getShelf));

        content=new ListContentPanel<>();

        refreshList();
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
        return localisator.getString("ingredients");
    }


    private void setSortFunctionality()
    {
        content.onSortButtonClick(listContentPanel ->
        {
            changeCurrentComparator();
            refreshList();
        });
    }

    private void setAddFunctionality()
    {
        content.onAddButtonClick(listContentPanel ->
        {
            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>(localisator.getString("add"), new Dimension(300, 250));
            IngredientEditContentPanel content=new IngredientEditContentPanel();
            content.setNameFieldValue("");
            content.setStoreFieldValue("");
            content.setShelfSpinnerValue(0);
            editWindow.setContent(content);

            editWindow.onApplyButtonClick(ingredientEditContentPanel ->
            {
                if(StringUtils.isBlank(ingredientEditContentPanel.getNameFieldValue()) || StringUtils.isBlank(ingredientEditContentPanel.getStoreFieldValue()))
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, localisator.getString("all_fields_must_be_filled"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    {
                        ingredientDatabaseTableAccessor.add(new Ingredient(ingredientEditContentPanel.getNameFieldValue(), ingredientEditContentPanel.getStoreFieldValue(), ingredientEditContentPanel.getShelfSpinnerValue()));
                        refreshList();
                    } catch (SQLException sqlException)
                    {
                        editWindow.dispose();
                        if(sqlException.getMessage().contains("UNIQUE"))
                        {
                            JOptionPane.showMessageDialog(null, localisator.getString("element_already_available"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, localisator.getString("adding_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
                        }

                        sqlException.printStackTrace();
                    }
                }
            });

            editWindow.showWindow();
        });
    }

    private void setUpContextMenu()
    {
        content.addMenuItem(localisator.getString("edit"), (listContentPanel, index) ->
        {
            Ingredient ingredientToChange=listContentPanel.getElementAt(index);

            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>(localisator.getString("edit"), new Dimension(300, 250));
            IngredientEditContentPanel content=new IngredientEditContentPanel();
            content.setNameFieldValue(ingredientToChange.getName());
            content.setStoreFieldValue(ingredientToChange.getStore());
            content.setShelfSpinnerValue(ingredientToChange.getShelf());
            editWindow.setContent(content);

            editWindow.onApplyButtonClick((ingredientEditContentPanel) ->
            {
                if(StringUtils.isBlank(ingredientEditContentPanel.getNameFieldValue()) || StringUtils.isBlank(ingredientEditContentPanel.getStoreFieldValue()))
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, localisator.getString("all_fields_must_be_filled"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    { //TODO
                        ingredientToChange.setName(ingredientEditContentPanel.getNameFieldValue());
                        ingredientToChange.setStore(ingredientEditContentPanel.getStoreFieldValue());
                        ingredientToChange.setShelf(ingredientEditContentPanel.getShelfSpinnerValue());

                        ingredientDatabaseTableAccessor.update(ingredientToChange);
                        refreshList();
                    }
                    catch (SQLException sqlException)
                    {
                        editWindow.dispose();
                        if(sqlException.getMessage().contains("UNIQUE"))
                        {
                            JOptionPane.showMessageDialog(null, localisator.getString("element_already_available"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, localisator.getString("adding_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        sqlException.printStackTrace();
                    }
                }
            });

            editWindow.showWindow();
        });

        content.addMenuItem(localisator.getString("remove"), (sortableListModel, index)->
        {
            Ingredient currentChosenIngredient=sortableListModel.getElementAt(index);
            try
            {
                ingredientDatabaseTableAccessor.remove(currentChosenIngredient.getId());
                refreshList();
            }
            catch (SQLException sqlException)
            {
                JOptionPane.showMessageDialog(null, localisator.getString("removing_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
                sqlException.printStackTrace();
            }
        });
    }


    private void refreshList()
    {
        try
        {
            content.setElements(ingredientDatabaseTableAccessor.getAll());
            content.sortElements(comparators.get(currentComparatorIndex));
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(null, localisator.getString("changing_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
            sqlException.printStackTrace();
        }
    }

    private void changeCurrentComparator()
    {
        currentComparatorIndex=(currentComparatorIndex+1)%comparators.size();
    }
}
