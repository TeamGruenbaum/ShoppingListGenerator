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
        setLayout(new BorderLayout());

        JScrollPane scrollPane=new JScrollPane(this.textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
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
