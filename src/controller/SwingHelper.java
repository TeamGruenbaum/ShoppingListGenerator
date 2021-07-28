package controller;

import java.awt.*;

public class SwingHelper
{
    public static GridBagConstraints getGridBagConstraints(int columnXPosition, int columnYPosition, int columnWidth, int columnHeight, double columnWeightX, double columnWeightY, int columnFill)
    {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = columnXPosition;
        gridBagConstraints.gridy = columnYPosition;
        gridBagConstraints.gridwidth = columnWidth;
        gridBagConstraints.gridheight = columnHeight;
        gridBagConstraints.weightx=columnWeightX;
        gridBagConstraints.weighty=columnWeightY;
        gridBagConstraints.fill = columnFill;

        return gridBagConstraints;
    }
}
