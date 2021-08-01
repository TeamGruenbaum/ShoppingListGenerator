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
import java.util.Comparator;

public class IngredientWindowContentProvider implements WindowContentProvider<ListContentPanel<Ingredient>>
{
    private ListContentPanel<Ingredient> content;
    private SortableListModel<Ingredient> sortableListModel;
    private DatabaseTableAccessor<Ingredient> databaseTableAccessor;

    public IngredientWindowContentProvider()
    {
        try
        {
            databaseTableAccessor=new IngredientDatabaseTableAccessor();
        }
        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
            catch (ClassNotFoundException classNotFoundException)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Could not find JDBC class.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try
        {
            sortableListModel=new SimpleSortableListModel<>(databaseTableAccessor.getAll());
        }
        catch(SQLException sqlException)
        {

        }

        sortableListModel.sort(Comparator.comparing(Ingredient::getName));
        content=new ListContentPanel<Ingredient>(sortableListModel, ((ingredient1, ingredient2) -> {return ingredient1.getId()== ingredient2.getId();}));
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
    public String getTitle() {
        return "Ingredients";
    }

    private void setSortFunctionality()
    {
        content.onSortClick((sortableListModel)->
        {
            sortableListModel.sort(Comparator.comparing(Ingredient::getName));
        });
    }

    private void setAddFunctionality()
    {
        content.onAddClick((sortableListModel)->
        {
            Ingredient emptyIngredient=new Ingredient("", "", 0);

            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>("Create ingredient", new IngredientEditContentPanel(emptyIngredient), new Dimension(300, 200));
            editWindow.onApplyClick(ingredientEditContentPanel ->
            {
                if(ingredientEditContentPanel.getIngredientName().isBlank() || ingredientEditContentPanel.getStore().isBlank())
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, "All fields must be filled in", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    {
                        databaseTableAccessor.add(new Ingredient(ingredientEditContentPanel.getIngredientName(), ingredientEditContentPanel.getStore(), ingredientEditContentPanel.getShelf()));
                        updateListModel();
                    } catch (SQLException throwables)
                    {
                        editWindow.dispose();
                        JOptionPane.showMessageDialog(new JFrame(), "The ingredient is already available", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            editWindow.showWindow();
        });
    }

    private void setUpContextMenu()
    {
        content.addMenuItem("Change", ((sortableListModel, index) ->
        {
            Ingredient ingredientToChange=sortableListModel.getElementAt(index);

            System.out.println(ingredientToChange.getId());

            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>("Edit Ingredient", new IngredientEditContentPanel(ingredientToChange), new Dimension(300, 200));

            editWindow.onApplyClick((ingredientEditContentPanel ->
            {
                if(ingredientEditContentPanel.getIngredientName().isBlank() || ingredientEditContentPanel.getStore().isBlank())
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, "All fields must be filled in", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    {
                        ingredientToChange.setName(ingredientEditContentPanel.getIngredientName());
                        ingredientToChange.setStore(ingredientEditContentPanel.getStore());
                        ingredientToChange.setShelf(ingredientEditContentPanel.getShelf());

                        databaseTableAccessor.update(ingredientToChange);
                        updateListModel();
                    }
                    catch (SQLException throwables)
                    {
                        editWindow.dispose();
                        JOptionPane.showMessageDialog(new JFrame(), "The ingredient is already available", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }));

            editWindow.showWindow();
        }));

        content.addMenuItem("Remove", (sortableListModel, index)->
        {
            Ingredient currentChoosenIngredient=sortableListModel.getElementAt(index);
            try
            {
                databaseTableAccessor.remove(currentChoosenIngredient.getId());
                updateListModel();
            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
        });
    }

    private void updateListModel()
    {
        try
        {
            sortableListModel.setElements(databaseTableAccessor.getAll());
        }
        catch (SQLException throwables)
        {
            JOptionPane.showMessageDialog(new JFrame(), "During a data update something went wrong", "Alert", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
}
