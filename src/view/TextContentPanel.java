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
        setLayout(new BorderLayout());

        JScrollPane scrollPane=new JScrollPane(this.text);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }


    public String getText()
    {
        return this.text.getText();
    }

    public void setText(String text)
    {
        this.text.setText(text);
        this.text.setCaretPosition(0);
    }
}
