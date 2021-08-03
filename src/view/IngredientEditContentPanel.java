package view;

import controller.Localisator;
import controller.SwingHelper;
import model.Ingredient;

import javax.swing.*;
import java.awt.*;

public class IngredientEditContentPanel extends JPanel
{
    private JTextField name;
    private JTextField store;
    private JSpinner shelf;

    public IngredientEditContentPanel(Ingredient ingredient)
    {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.name=new JTextField(ingredient.getName());
        add(new JLabel(Localisator.getInstance().getString("name")+":"), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.name, SwingHelper.getGridBagConstraints(1, 0, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.store=new JTextField(ingredient.getStore());
        add(new JLabel(Localisator.getInstance().getString("shelf")+":"), SwingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.store, SwingHelper.getGridBagConstraints(1, 1, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.shelf=new JSpinner(new SpinnerNumberModel(ingredient.getShelf(),0,100,1));
        add(new JLabel(Localisator.getInstance().getString("store")+":"), SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.shelf, SwingHelper.getGridBagConstraints(1, 2, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

    }


    public String getIngredientName() {
        return name.getText();
    }

    public String getStore() {
        return store.getText();
    }

    public int getShelf() {
        return (Integer) shelf.getModel().getValue();
    }
}
