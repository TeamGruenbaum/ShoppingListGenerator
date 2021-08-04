package view;

import controller.Localisator;
import controller.SwingHelper;

import javax.swing.*;
import java.awt.*;

public final class IngredientEditContentPanel extends JPanel
{
    private JTextField nameField;
    private JTextField storeField;
    private JSpinner shelfField;

    public IngredientEditContentPanel()
    {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.nameField =new JTextField();
        add(new JLabel(Localisator.getInstance().getString("name")+":"), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.nameField, SwingHelper.getGridBagConstraints(1, 0, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.storeField =new JTextField();
        add(new JLabel(Localisator.getInstance().getString("shelf")+":"), SwingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.storeField, SwingHelper.getGridBagConstraints(1, 1, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.shelfField =new JSpinner(new SpinnerNumberModel(0,0,100,1));
        add(new JLabel(Localisator.getInstance().getString("store")+":"), SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.shelfField, SwingHelper.getGridBagConstraints(1, 2, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));
    }


    public String getNameFieldValue() {
        return nameField.getText();
    }

    public void setNameFieldValue(String newValue) {
        nameField.setText(newValue);
    }


    public String getStoreFieldValue() {
        return storeField.getText();
    }

    public void setStoreFieldValue(String newValue) {
         storeField.setText(newValue);
    }


    public int getShelfFieldValue() {
        return (Integer) shelfField.getModel().getValue();
    }

    public void setShelfFieldValue(int newValue) {
        shelfField.getModel().setValue(newValue);
    }

}
