package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class EditWindow<T extends JComponent> extends JDialog
{
    private T content;
    private JButton quitButton;
    private JButton applyButton;

    public EditWindow(String windowTitle, T content, Dimension size)
    {
        setTitle(windowTitle);

        setMinimumSize(size);
        setMaximumSize(size);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());


        add(content, BorderLayout.CENTER);
        this.content = content;


        quitButton=new JButton("Quit");
        quitButton.addActionListener((actionEvent)->dispose());
        applyButton=new JButton("Apply");

        JPanel bottomBar=new JPanel();

        bottomBar.add(quitButton, BorderLayout.WEST);
        bottomBar.add(applyButton, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);


        setVisible(true);
    }


    public void onQuitClick(Consumer<T> action)
    {
        quitButton.addActionListener((actionEvent)->
        {
            action.accept(content);
            dispose();
        });
    }

    public void onApplyClick(Consumer<T> action)
    {
        applyButton.addActionListener((actionEvent)->
        {
            action.accept(content);
            dispose();
        });
    }
}