package view;

import javax.swing.*;
import java.awt.*;

public final class TextContentPanel extends JPanel
{
    private JTextArea textArea;



    public TextContentPanel()
    {
        this.textArea =new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setBorder(BorderFactory.createEmptyBorder(5, 11, 0, 0));
        setLayout(new BorderLayout());

        JScrollPane scrollPane=new JScrollPane(this.textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(new JSeparator(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(new JSeparator(), BorderLayout.SOUTH);
    }


    public String getText()
    {
        return this.textArea.getText();
    }

    public void setText(String newValue)
    {
        this.textArea.setText(newValue);
        this.textArea.setCaretPosition(0);
    }
}
