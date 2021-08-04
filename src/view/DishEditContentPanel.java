package view;

import controller.SimpleListModel;
import model.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public final class DishEditContentPanel extends JPanel
{
    private JTextField nameField;
    private JList<Ingredient> list;



    public DishEditContentPanel()
    {
        setLayout(new BorderLayout());

        this.nameField =new JTextField();
        add(this.nameField, BorderLayout.NORTH);

        this.list =new JList<>();
        this.list.setSelectionModel(new DefaultListSelectionModel()
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
        
        add(new JScrollPane(this.list), BorderLayout.CENTER);
    }


    public String getNameFieldValue()
    {
        return nameField.getText();
    }

    public void setNameFieldValue(String newValue)
    {
        nameField.setText(newValue);
    }


    public void setAllIngredients(List<Ingredient> newValues)
    {
        list.setModel(new SimpleListModel<>(newValues));
    }


    public List<Ingredient> getUnmodifiableSelectedItems()
    {
        return Collections.unmodifiableList(list.getSelectedValuesList());
    }

    public void setSelectedIngredients(List<Ingredient> newValues)
    {
        List<Integer> indices=new ArrayList<>();
        for (Ingredient ingredient:newValues)
        {
            for (int j = 0; j < list.getModel().getSize(); j++)
            {
                if(ingredient.getId()== list.getModel().getElementAt(j).getId())
                {
                    indices.add(j);
                    break;
                }
            }
        }

        list.setSelectedIndices(indices.stream().mapToInt(i -> i).toArray());
    }
}
