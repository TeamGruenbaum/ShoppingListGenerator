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
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DishWindowContentProvider implements WindowContentProvider<ListContentPanel<Dish>>
{
    private ListContentPanel<Dish> content;
    private EditWindow<DishEditContentPanel> editWindow;
    private DatabaseTableAccessor<Dish> dishDatabaseTableAccessor;
    private DatabaseTableAccessor<Ingredient> ingredientDatabaseTableAccessor;


    public DishWindowContentProvider()
    {
        try
        {
            ingredientDatabaseTableAccessor=new IngredientDatabaseTableAccessor();
            dishDatabaseTableAccessor=new DishDatabaseTableAccessor();

            SortableListModel<Dish> listModel=new SimpleSortableListModel<Dish>(dishDatabaseTableAccessor.getAll());
            listModel.sort((Dish dish1, Dish dish2) -> {return dish1.getName().compareTo(dish2.getName());});
            content=new ListContentPanel<Dish>(listModel);

            setAddFunctionality();
            setSortFunctionality();
            setUpContextMenu();
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (ClassNotFoundException classNotFoundException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not find JDBC class.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ListContentPanel getContent()
    {
        return content;
    }

    @Override
    public String getTitle()
    {
        return "Dishes";
    }

    @Override
    public Optional<List<Integer>> getSelectedItemIds()
    {
        return Optional.of(content.getUnmodifiableSelectedItems().stream().map((Dish dish)->{return dish.getId();}).collect(Collectors.toList()));
    }

    private void setSortFunctionality()
    {
        content.onSortClick(new Consumer<SortableListModel<Dish>>()
        {
            @Override
            public void accept(SortableListModel<Dish> listModel)
            {
                listModel.sort((Dish dish1, Dish dish2) -> {return dish1.getName().compareTo(dish2.getName());});
            }
        });
    }

    private void setAddFunctionality()
    {
        content.onAddClick(new Consumer<SortableListModel<Dish>>()
        {
            @Override
            public void accept(SortableListModel<Dish> listModel)
            {
                addDish(new Dish("", new ArrayList<Ingredient>()), listModel);
            }
        });
    }

    private void setUpContextMenu()
    {
        content.addMenuItem("Change", new BiConsumer<SortableListModel<Dish> , Integer>()
        {
            @Override
            public void accept(SortableListModel<Dish> listModel, Integer lastClickedListItemIndex)
            {
                editDish(listModel.getElementAt(lastClickedListItemIndex), listModel);
            }
        });

        content.addMenuItem("Remove", new BiConsumer<SortableListModel<Dish> , Integer>()
        {
            @Override
            public void accept(SortableListModel<Dish>  listModel, Integer lastClickedListItemIndex)
            {
                try
                {
                    dishDatabaseTableAccessor.remove(listModel.getElementAt(lastClickedListItemIndex).getId());
                    updateListModel(listModel);
                }
                catch (SQLException sqlException)
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Could not remove dish from database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void editDish(Dish currentDish, SortableListModel<Dish> listModel)
    {
        editWindow=new EditWindow<>("Edit Dish", new DishEditContentPanel(currentDish, new SimpleSortableListModel<>(getIngredientList())), new Dimension(400,400));
        editWindow.onApplyClick(new Consumer<DishEditContentPanel>()
        {
            @Override
            public void accept(DishEditContentPanel dishEditContentPanel)
            {
                if(updateDish(currentDish, dishEditContentPanel))
                {
                    try
                    {
                        dishDatabaseTableAccessor.update(currentDish);
                        updateListModel(listModel);
                    }
                    catch (SQLException sqlException)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), "Could not edit dish in database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        editWindow.showWindow();
    }

    private void addDish(Dish newDish, SortableListModel<Dish> listModel)
    {
        editWindow=new EditWindow<>("Create Dish", new DishEditContentPanel(newDish, new SimpleSortableListModel<>(getIngredientList())), new Dimension(400,400));
        editWindow.onApplyClick(new Consumer<DishEditContentPanel>()
        {
            @Override
            public void accept(DishEditContentPanel dishEditContentPanel)
            {
                if (updateDish(newDish, dishEditContentPanel))
                {
                    try
                    {
                        dishDatabaseTableAccessor.add(newDish);
                        updateListModel(listModel);
                    }
                    catch (SQLException sqlException)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), "Could not add dish to database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        editWindow.showWindow();
    }

    private boolean updateDish(Dish dish, DishEditContentPanel dishEditContentPanel)
    {
        String updatedName=dishEditContentPanel.getUpdatedName();
        List<Ingredient> ingredientList=dishEditContentPanel.getUnmodifiableSelectedItems();

        if(updatedName.isBlank())
        {
            editWindow.dispose();
            JOptionPane.showMessageDialog(new JFrame(), "The name textfield has to be filled in.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(ingredientList.isEmpty())
        {
            editWindow.dispose();
            JOptionPane.showMessageDialog(new JFrame(), "At least one ingredient has to be selected.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void updateListModel(SortableListModel<Dish> listModel)
    {
        try
        {
            listModel.setElements(dishDatabaseTableAccessor.getAll());
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "During a data update something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
