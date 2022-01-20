package controller;



import model.*;

import org.apache.commons.lang3.StringUtils;

import view.DishEditContentPanel;
import view.EditWindow;
import view.ListContentPanel;

import javax.swing.*;

import java.awt.*;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class DishWindowContentProvider implements WindowContentProvider<ListContentPanel<Dish>>
{
    private ListContentPanel<Dish> content;
    private DatabaseTableAccessor<Dish> dishDatabaseTableAccessor;
    private DatabaseTableAccessor<Ingredient> ingredientDatabaseTableAccessor;

    private List<Comparator<Dish>> comparators;
    private int currentComparatorIndex;

    private Localisator localisator;



    public DishWindowContentProvider()
    {
        localisator=new Localisator();

        try
        {
            ingredientDatabaseTableAccessor=new IngredientDatabaseTableAccessor();
            dishDatabaseTableAccessor=new DishDatabaseTableAccessor();
        }
        catch(ClassNotFoundException classNotFoundException)
        {
            JOptionPane.showMessageDialog(null, localisator.getString("no_database_driver_available")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
            classNotFoundException.printStackTrace();
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(null, localisator.getString("connection_to_database_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
            sqlException.printStackTrace();
        }

        currentComparatorIndex=0;
        comparators=new ArrayList<>();
        comparators.add(Comparator.comparing(dish->dish.getName().toLowerCase()));

        content=new ListContentPanel<>();

        setSortFunctionality();
        setAddFunctionality();
        setUpContextMenu();
        refreshList();
    }


    @Override
    public ListContentPanel<Dish> getContent()
    {
        return content;
    }

    @Override
    public String getTitle()
    {
        return localisator.getString("dishes");
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
        content.onAddButtonClick((listContentPanel)->
        {
            Dish newDish=new Dish("", new ArrayList<>());

            EditWindow<DishEditContentPanel> editWindow=new EditWindow(localisator.getString("add"), new Dimension(300,450));
            DishEditContentPanel content=new DishEditContentPanel();
            content.setNameFieldValue("");
            try
            {
                content.setAllIngredients(ingredientDatabaseTableAccessor.getAll());
            }
            catch(SQLException sqlException)
            {
                editWindow.dispose();
                JOptionPane.showMessageDialog(null, localisator.getString("loading_not_possible")+"\n"+localisator.getString("error_solution"));
                sqlException.printStackTrace();
            }
            editWindow.setContent(content);
            editWindow.onApplyButtonClick((DishEditContentPanel dishEditContentPanel)->
            {
                if(updateDish(newDish, dishEditContentPanel, editWindow))
                {
                    try
                    {
                        dishDatabaseTableAccessor.add(newDish);
                        refreshList();
                    }
                    catch(SQLException sqlException)
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
        content.addMenuItem(localisator.getString(("edit")), (sortableListModel, lastClickedListItemIndex)->
        {
            Dish currentDish=sortableListModel.getElementAt(lastClickedListItemIndex);

            EditWindow<DishEditContentPanel> editWindow=new EditWindow(localisator.getString("add"), new Dimension(300,450));
            DishEditContentPanel content=new DishEditContentPanel();
            content.setNameFieldValue(currentDish.getName());
            try
            {
                content.setAllIngredients(ingredientDatabaseTableAccessor.getAll());
            }
            catch(SQLException sqlException)
            {
                editWindow.dispose();
                JOptionPane.showMessageDialog(null, localisator.getString("loading_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
                sqlException.printStackTrace();
            }
            content.setSelectedIngredients(currentDish.getUnmodifiableIngredients());
            editWindow.setContent(content);

            editWindow.onApplyButtonClick((DishEditContentPanel dishEditContentPanel)->
            {
                if(updateDish(currentDish, dishEditContentPanel, editWindow))
                {
                    try
                    {
                        dishDatabaseTableAccessor.update(currentDish);
                        refreshList();
                    }
                    catch (SQLException sqlException)
                    {
                        editWindow.dispose();
                        JOptionPane.showMessageDialog(null, localisator.getString("changing_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
                        sqlException.printStackTrace();
                    }
                }
            });
            editWindow.showWindow();
        });

        content.addMenuItem(localisator.getString("remove"), (sortableListModel, lastClickedListItemIndex)->
        {
            try
            {
                dishDatabaseTableAccessor.remove(sortableListModel.getElementAt(lastClickedListItemIndex).getId());
                refreshList();
            }
            catch (SQLException sqlException)
            {
                JOptionPane.showMessageDialog(null, localisator.getString("removing_not_possible")+"\n"+localisator.getString("error_solution"), localisator.getString("warning"), JOptionPane.WARNING_MESSAGE);
                sqlException.printStackTrace();
            }
        });
    }


    private boolean updateDish(Dish dish, DishEditContentPanel dishEditContentPanel, EditWindow editWindow)
    {
        String updatedName=dishEditContentPanel.getNameFieldValue();
        List<Ingredient> ingredientList=dishEditContentPanel.getUnmodifiableSelectedItems();

        if(StringUtils.isBlank(updatedName) || ingredientList.isEmpty())
        {
            editWindow.dispose();
            JOptionPane.showMessageDialog(new JFrame(), localisator.getString("all_fields_must_be_filled"), localisator.getString("information"), JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        dish.setName(updatedName);
        dish.setAllIngredients(ingredientList);
        return true;
    }


    private void refreshList()
    {
        try
        {
            content.setElements(dishDatabaseTableAccessor.getAll());
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