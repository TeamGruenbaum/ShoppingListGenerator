package controller;

import model.*;
import model.Dish;
import model.Ingredient;

import view.DishEditContentPanel;
import view.EditWindow;
import view.ListContentPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public class DishWindowContentProvider implements WindowContentProvider<ListContentPanel<Dish>>
{
    private ListContentPanel<Dish> content;
    private SortableListModel<Dish> sortableListModel;
    private DatabaseTableAccessor<Dish> dishDatabaseTableAccessor;
    private DatabaseTableAccessor<Ingredient> ingredientDatabaseTableAccessor;


    public DishWindowContentProvider()
    {
        try
        {
            ingredientDatabaseTableAccessor=new IngredientDatabaseTableAccessor();
            dishDatabaseTableAccessor=new DishDatabaseTableAccessor();
        }
        catch (ClassNotFoundException classNotFoundException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not find JDBC class.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try
        {
            sortableListModel=new SimpleSortableListModel<Dish>(dishDatabaseTableAccessor.getAll());
            sortableListModel.sort(Comparator.comparing(Dish::getName));
            content=new ListContentPanel<Dish>(sortableListModel, ((dish1, dish2) -> {return dish1.getId()== dish2.getId();}));

            setAddFunctionality();
            setSortFunctionality();
            setUpContextMenu();
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not access dishes in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ListContentPanel<Dish> getContent()
    {
        return content;
    }

    @Override
    public String getTitle()
    {
        return "Dishes";
    }

    private void setSortFunctionality()
    {
        content.onSortClick((sortableListModel)->
        {
            sortableListModel.sort(Comparator.comparing(Dish::getName));
        });
    }

    private void setAddFunctionality()
    {
        content.onAddClick((SortableListModel<Dish> sortableListModel)->
        {
            addDish(new Dish("", new ArrayList<Ingredient>()), sortableListModel);
        });
    }

    private void setUpContextMenu()
    {
        content.addMenuItem("Change", (SortableListModel<Dish> sortableListModel, Integer lastClickedListItemIndex)->
        {
            editDish(sortableListModel.getElementAt(lastClickedListItemIndex), sortableListModel);
        });

        content.addMenuItem("Remove", (SortableListModel<Dish> sortableListModel, Integer lastClickedListItemIndex)->
        {
            try
            {
                dishDatabaseTableAccessor.remove(sortableListModel.getElementAt(lastClickedListItemIndex).getId());
                updateListModel();
            }
            catch (SQLException sqlException)
            {
                JOptionPane.showMessageDialog(new JFrame(), "Could not remove dish from database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addDish(Dish newDish, SortableListModel<Dish> sortableListModel)
    {
        EditWindow<DishEditContentPanel> editWindow=new EditWindow<>("Create Dish", new DishEditContentPanel(newDish, new SimpleSortableListModel<>(getIngredientList())), new Dimension(300,200));
        editWindow.onApplyClick((DishEditContentPanel dishEditContentPanel)->
        {
            if (updateDish(newDish, dishEditContentPanel, editWindow))
            {
                try
                {
                    dishDatabaseTableAccessor.add(newDish);
                    updateListModel();
                }
                catch (SQLException sqlException)
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(new JFrame(), "Could not add dish to database.", "Alert", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        editWindow.showWindow();
    }

    private void editDish(Dish currentDish, SortableListModel<Dish> sortableListModel)
    {
        EditWindow<DishEditContentPanel> editWindow=new EditWindow<>("Edit Dish", new DishEditContentPanel(currentDish, new SimpleSortableListModel<>(getIngredientList())), new Dimension(300,200));
        editWindow.onApplyClick((DishEditContentPanel dishEditContentPanel)->
        {
            if(updateDish(currentDish, dishEditContentPanel, editWindow))
            {
                try
                {
                    dishDatabaseTableAccessor.update(currentDish);
                    updateListModel();
                }
                catch (SQLException sqlException)
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(new JFrame(), "Could not edit dish in database.", "Alert", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        editWindow.showWindow();
    }

    private boolean updateDish(Dish dish, DishEditContentPanel dishEditContentPanel, EditWindow editWindow)
    {
        String updatedName=dishEditContentPanel.getUpdatedName();
        List<Ingredient> ingredientList=dishEditContentPanel.getUnmodifiableSelectedItems();

        if(updatedName.isBlank())
        {
            editWindow.dispose();
            JOptionPane.showMessageDialog(new JFrame(), "The name textfield has to be filled in.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if(ingredientList.isEmpty())
        {
            editWindow.dispose();
            JOptionPane.showMessageDialog(new JFrame(), "At least one ingredient has to be selected.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        dish.setName(updatedName);
        dish.getIngredients().clear();
        dish.getIngredients().addAll(ingredientList);
        return true;
    }

    private List<Ingredient> getIngredientList()
    {
        try
        {
            return ingredientDatabaseTableAccessor.getAll();
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not load ingredient list.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return null;
        }
    }

    private void updateListModel()
    {
        try
        {
            sortableListModel.setElements(dishDatabaseTableAccessor.getAll());
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "During a data update something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}