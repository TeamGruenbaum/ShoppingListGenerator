package view;

import javax.swing.*;
import java.awt.*;

public class TextContentPanel extends JPanel
{
    private JTextArea text;


    public TextContentPanel(String text)
    {
        this.text=new JTextArea(text);
        setLayout(new BorderLayout());
        add(this.text, BorderLayout.CENTER);
    }
}
