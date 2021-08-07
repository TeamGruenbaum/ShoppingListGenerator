package view;



import controller.Localisator;
import controller.SwingHelper;

import javax.swing.*;

import java.awt.*;



public final class IngredientEditContentPanel extends JPanel
{
    private JTextField nameField;
    private JTextField storeField;
    private JSpinner shelfSpinner;



    public IngredientEditContentPanel()
    {
        Localisator localisator=new Localisator();
        SwingHelper swingHelper=new SwingHelper();

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.nameField=new JTextField();
        add(new JLabel(localisator.getString("name")+":"), swingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.nameField, swingHelper.getGridBagConstraints(1, 0, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.storeField=new JTextField();
        add(new JLabel(localisator.getString("shelf")+":"), swingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.storeField, swingHelper.getGridBagConstraints(1, 1, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));

        this.shelfSpinner =new JSpinner(new SpinnerNumberModel(0,0,100,1));
        add(new JLabel(localisator.getString("store")+":"), swingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 1, GridBagConstraints.HORIZONTAL));
        add(this.shelfSpinner, swingHelper.getGridBagConstraints(1, 2, 1, 1, 1, 0.9, GridBagConstraints.HORIZONTAL));
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


    public int getShelfSpinnerValue() {
        return (Integer) shelfSpinner.getModel().getValue();
    }

    public void setShelfSpinnerValue(int newValue) {
        shelfSpinner.getModel().setValue(newValue);
    }
}
