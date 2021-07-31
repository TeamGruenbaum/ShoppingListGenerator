package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public final class EditWindow<T extends JComponent> extends JDialog
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

        this.content = content;
        add(content, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

    public void showWindow()
    {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setVisible(true);
    }

    private JPanel createFooterPanel()
    {
        quitButton=new JButton("Quit");
        quitButton.addActionListener((actionEvent)->dispose());
        applyButton=new JButton("Apply");

        JPanel footerPanel=new JPanel();

        footerPanel.add(quitButton, BorderLayout.WEST);
        footerPanel.add(applyButton, BorderLayout.EAST);

        return footerPanel;
    }
}
