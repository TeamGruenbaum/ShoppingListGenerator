package view;



import controller.Localisator;
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
        Localisator localisator=new Localisator();
        SwingHelper swingHelper=new SwingHelper();

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        this.nameField=new JTextField();
        add(new JLabel(localisator.getString("name")+":"), swingHelper.getGridBagConstraints(0, 0, 1, 1, 1, 0.1, GridBagConstraints.HORIZONTAL, 16));
        add(this.nameField, swingHelper.getGridBagConstraints(0, 1, 1, 1, 1, 0.1, GridBagConstraints.HORIZONTAL, 10));

        add(new JLabel(localisator.getString("ingredients")+":"), swingHelper.getGridBagConstraints(0, 2, 1, 1, 1, 0.1, GridBagConstraints.BOTH, 16));
        this.list=new JList<>();
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
        add(new JScrollPane(this.list), swingHelper.getGridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.BOTH, 10));
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
            for (int index=0; index<list.getModel().getSize(); index++)
            {
                if(ingredient.getId()==list.getModel().getElementAt(index).getId())
                {
                    indices.add(index);
                    break;
                }
            }
        }
        list.setSelectedIndices(indices.stream().mapToInt(i -> i).toArray());
    }
}
