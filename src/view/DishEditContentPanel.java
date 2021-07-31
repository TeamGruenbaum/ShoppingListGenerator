package view;

import model.Dish;
import model.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class DishEditContentPanel extends JPanel
{
    private JTextField name;
    private JList<Ingredient> ingredients;

    private Dish dish;



    public <T extends ListModel<Ingredient>> DishEditContentPanel(Dish dish, T listModel)
    {
        this.dish=dish;


        setLayout(new BorderLayout());


        this.name=new JTextField(dish.getName());
        add(this.name, BorderLayout.NORTH);


        ingredients =new JList<>();
        ingredients.setModel(listModel);
        ingredients.setSelectionModel(new DefaultListSelectionModel()
        {
            @Override
            public void setSelectionInterval(int index0, int index1)
            {
            if(super.isSelectedIndex(index0))
            {
                super.removeSelectionInterval(index0, index1);
            }
            else
            {
                super.addSelectionInterval(index0, index1);
            }
            }
        });
        setSelectedIngredients();
        
        add(new JScrollPane(this.ingredients), BorderLayout.CENTER);
    }

    public String getUpdatedName()
    {
        return name.getText();
    }

    public List<Ingredient> getUnmodifiableSelectedItems()
    {
        return Collections.unmodifiableList(ingredients.getSelectedValuesList());
    }


    private void setSelectedIngredients()
    {
        List<Integer> indices=new ArrayList<>();
        for (Ingredient ingredient:dish.getIngredients())
        {
            for (int j = 0; j < ingredients.getModel().getSize(); j++)
            {
                if(ingredient== ingredients.getModel().getElementAt(j))
                {
                    indices.add(j);
                    break;
                }
            }
        }
        ingredients.setSelectedIndices(indices.stream().mapToInt(i -> i).toArray());
    }
}
