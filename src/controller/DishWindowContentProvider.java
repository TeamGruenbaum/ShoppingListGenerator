package controller;

import model.*;
import model.Dish;
import model.Ingredient;

import view.DishEditContentPanel;
import view.EditWindow;
import view.ListContentPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


public class DishWindowContentProvider implements WindowContentProvider<ListContentPanel<Dish>>
{
    private ListContentPanel<Dish> content;
    private List<Dish> sortableListModel;
    private DatabaseTableAccessor<Dish> dishDatabaseTableAccessor;
    private DatabaseTableAccessor<Ingredient> ingredientDatabaseTableAccessor;
    private List<Comparator<Dish>> comparators;
    private int currentComparatorIndex;


    public DishWindowContentProvider()
    {
        try
        {
            ingredientDatabaseTableAccessor=new IngredientDatabaseTableAccessor();
            dishDatabaseTableAccessor=new DishDatabaseTableAccessor();
        }
        catch (ClassNotFoundException classNotFoundException)
        {
            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("no_database_driver_available"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("connection_to_database_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
            PathHelper pathHelper=new PathHelper();
            new File(pathHelper.getSavePath()+pathHelper.getDatabaseName()).delete();
            sqlException.printStackTrace();
        }

        currentComparatorIndex=0;

        comparators=new ArrayList<>();
        comparators.addAll(List.of(Comparator.comparing(dish -> dish.getName().toLowerCase())));

        content=new ListContentPanel<>();

        updateListModel();
        setAddFunctionality();
        setSortFunctionality();
        setUpContextMenu();
    }

    @Override
    public ListContentPanel<Dish> getContent()
    {
        return content;
    }

    @Override
    public String getTitle()
    {
        return Localisator.getInstance().getString("dishes");
    }

    private void setSortFunctionality()
    {
        content.onSortClick(listContentPanel ->
        {
            changeCurrentComparator();
            updateListModel();
        });
    }

    private void setAddFunctionality()
    {
        content.onAddClick((listContentPanel)->
        {
            Dish newDish=new Dish("", new ArrayList<Ingredient>());

            EditWindow<DishEditContentPanel> editWindow=new EditWindow<>(Localisator.getInstance().getString("add"), new DishEditContentPanel(newDish, new SimpleListModel<>(getIngredientList())), new Dimension(300,200));
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
                        JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("adding_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
                        sqlException.printStackTrace();
                    }
                }
            });

            editWindow.showWindow();
        });
    }

    private void setUpContextMenu()
    {
        content.addMenuItem(Localisator.getInstance().getString(("add")), (sortableListModel, lastClickedListItemIndex)->
        {
            editDish(sortableListModel.getElementAt(lastClickedListItemIndex));
        });

        content.addMenuItem(Localisator.getInstance().getString("remove"), (sortableListModel, lastClickedListItemIndex)->
        {
            try
            {
                dishDatabaseTableAccessor.remove(sortableListModel.getElementAt(lastClickedListItemIndex).getId());
                updateListModel();
            }
            catch (SQLException sqlException)
            {
                JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("removing_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
                sqlException.printStackTrace();
            }
        });
    }

    private void editDish(Dish currentDish)
    {
        EditWindow<DishEditContentPanel> editWindow=new EditWindow<>(Localisator.getInstance().getString("edit"), new DishEditContentPanel(currentDish, new SimpleListModel<>(getIngredientList())), new Dimension(300,200));
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
                    JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("changing_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
                    sqlException.printStackTrace();
                }
            }
        });
        editWindow.showWindow();
    }

    private boolean updateDish(Dish dish, DishEditContentPanel dishEditContentPanel, EditWindow editWindow)
    {
        String updatedName=dishEditContentPanel.getUpdatedName();
        List<Ingredient> ingredientList=dishEditContentPanel.getUnmodifiableSelectedItems();

        if(updatedName.isBlank() || ingredientList.isEmpty())
        {
            editWindow.dispose();
            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("all_fields_must_be_filled"), Localisator.getInstance().getString("information"), JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("loading_not_possible"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
            sqlException.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    private void updateListModel()
    {
        try
        {
            content.setElements(dishDatabaseTableAccessor.getAll());
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