package view;

import controller.SwingHelper;

import javax.swing.*;
import java.awt.*;

public class IngredientEditContentPanel extends JPanel
{
    private JTextField name;
    private JTextField store;
    private JSpinner shelf;

    public IngredientEditContentPanel(String name, String store, int shelf)
    {
        setLayout(new GridBagLayout());

        this.name=new JTextField(name);
        this.store=new JTextField(store);
        this.shelf=new JSpinner(new SpinnerNumberModel(shelf,0,100,1));

        add(new JLabel("Name:"), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.name, SwingHelper.getGridBagConstraints(1, 0, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        add(new JLabel("Store:"), SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.store, SwingHelper.getGridBagConstraints(1, 1, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        add(new JLabel("Shelf:"), SwingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.shelf, SwingHelper.getGridBagConstraints(1, 2, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));
    }


    public String getIngredientName()
    {
        return name.getText();
    }

    public String getStore()
    {
        return store.getText();
    }

    public int getShelf()
    {
        return (Integer) shelf.getModel().getValue();
    }
}
