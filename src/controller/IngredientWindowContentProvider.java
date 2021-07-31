package controller;

import model.DatabaseTableAccessor;
import model.IngredientDatabaseTableAccessor;
import model.Ingredient;
import view.EditWindow;
import view.IngredientEditContentPanel;
import view.ListContentPanel;
import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IngredientWindowContentProvider implements WindowContentProvider<ListContentPanel<Ingredient>>
{
    private ListContentPanel<Ingredient> content;
    private SortableListModel<Ingredient> sortableListModel;
    private DatabaseTableAccessor<Ingredient> databaseTableAccessor;

    public IngredientWindowContentProvider() throws SQLException, ClassNotFoundException
    {
        databaseTableAccessor=new IngredientDatabaseTableAccessor();


        sortableListModel=new SimpleSortableListModel<>(databaseTableAccessor.getAll());
        sortableListModel.sort(Comparator.comparing(Ingredient::getName));


        content=new ListContentPanel<Ingredient>(sortableListModel);

        content.onAddClick((sortableListModel)->
        {
            Ingredient emptyIngredient=new Ingredient("", "", 0);

            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>("Create ingredient", new IngredientEditContentPanel(emptyIngredient), new Dimension(300, 200));
            editWindow.onApplyClick(ingredientEditContentPanel ->
            {
                if(ingredientEditContentPanel.getIngredientName().isBlank() || ingredientEditContentPanel.getStore().isBlank())
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, "All field must be filled out", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    {
                        databaseTableAccessor.add(new Ingredient(ingredientEditContentPanel.getIngredientName(), ingredientEditContentPanel.getStore(), ingredientEditContentPanel.getShelf()));
                        refillListModel();
                    } catch (SQLException throwables)
                    {
                        editWindow.dispose();
                        JOptionPane.showMessageDialog(new JFrame(), "The ingredient is already available", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            editWindow.showWindow();
        });

        content.onSortClick((lm)->
        {
            lm.sort(Comparator.comparing(Ingredient::getName));
        });

        content.addMenuItem("Change", ((sortableListModel, index) ->
        {
            Ingredient ingredientToChange=sortableListModel.getElementAt(index);

            System.out.println(ingredientToChange.getId());

            EditWindow<IngredientEditContentPanel> editWindow=new EditWindow<>("Change ingredient", new IngredientEditContentPanel(ingredientToChange), new Dimension(300, 200));

            editWindow.onApplyClick((ingredientEditContentPanel ->
            {
                if(ingredientEditContentPanel.getIngredientName().isBlank() || ingredientEditContentPanel.getStore().isBlank())
                {
                    editWindow.dispose();
                    JOptionPane.showMessageDialog(editWindow, "All field must be filled out", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try
                    {
                        ingredientToChange.setName(ingredientEditContentPanel.getIngredientName());
                        ingredientToChange.setStore(ingredientEditContentPanel.getStore());
                        ingredientToChange.setShelf(ingredientEditContentPanel.getShelf());

                        databaseTableAccessor.update(ingredientToChange);
                        refillListModel();
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
                refillListModel();
            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
        });
    }

    private void refillListModel()
    {
        try
        {
            sortableListModel.setElements(databaseTableAccessor.getAll());
        } catch (SQLException throwables)
        {
            JOptionPane.showMessageDialog(new JFrame(), "During a data update something went wrong", "Alert", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
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

    @Override
    public Optional<List<Integer>> getSelectedItemIds()
    {
        return Optional.of(Collections.unmodifiableList(content.getUnmodifiableSelectedItems().stream().map(ingredient->{return ingredient.getId();}).collect(Collectors.toList())));
    }
}
