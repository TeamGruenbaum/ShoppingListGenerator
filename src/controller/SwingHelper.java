package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    public static JLabel getLinkedJLabel(String text, String uri)
    {
        JLabel linkedLabel=new JLabel(text);
        linkedLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkedLabel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        linkedLabel.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI(uri));
                }
                catch(IOException | URISyntaxException exception)
                {
                    JOptionPane.showMessageDialog(new JFrame(), Localisator.getInstance().getString("uri_can_not_be_opened"), Localisator.getInstance().getString("warning"), JOptionPane.WARNING_MESSAGE);
                }
            }
            @Override
            public void mousePressed(MouseEvent e){}
            @Override
            public void mouseReleased(MouseEvent e){}
            @Override
            public void mouseEntered(MouseEvent e){}
            @Override
            public void mouseExited(MouseEvent e){}
        });
        return linkedLabel;
    }
}
