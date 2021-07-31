package view;

import javax.swing.*;
import java.awt.*;

public class TextContentPanel extends JPanel
{
    private JTextArea text;


    public TextContentPanel()
    {
        this.text=new JTextArea();
        this.text.setEditable(false);
        this.text.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        add(this.text, BorderLayout.CENTER);
    }


    public String getText()
    {
        return this.text.getText();
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }
}
