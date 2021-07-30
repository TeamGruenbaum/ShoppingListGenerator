package view;

import controller.SwingHelper;
import model.Ingredient;

import javax.swing.*;
import java.awt.*;

public class IngredientEditContentPanel extends JPanel
{
    private JTextField name;
    private JTextField store;
    private JSpinner shelf;

    private Ingredient ingredient;

    public IngredientEditContentPanel(Ingredient ingredient)
    {
        this.ingredient=ingredient;


        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.name=new JTextField(ingredient.getName());
        add(new JLabel("Name:"), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.name, SwingHelper.getGridBagConstraints(1, 0, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.store=new JTextField(ingredient.getStore());
        add(new JLabel("Shelf:"), SwingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.store, SwingHelper.getGridBagConstraints(1, 1, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.shelf=new JSpinner(new SpinnerNumberModel(ingredient.getShelf(),0,100,1));
        add(new JLabel("Store:"), SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.shelf, SwingHelper.getGridBagConstraints(1, 2, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

    }

    public void updateIngredient()
    {
        ingredient.setName(name.getText());
        ingredient.setStore(store.getText());
        ingredient.setShelf((Integer) shelf.getModel().getValue());
    }
}
