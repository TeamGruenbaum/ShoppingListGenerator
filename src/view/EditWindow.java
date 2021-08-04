package view;

import controller.Localisator;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public final class EditWindow<T extends JComponent> extends JDialog
{
    private T content;

    private JButton quitButton;
    private JButton applyButton;

    public EditWindow(String windowTitle, Dimension size)
    {
        setTitle(windowTitle);

        setMinimumSize(size);
        setMaximumSize(size);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(content, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void onQuitButtonClick(Consumer<T> newAction)
    {
        quitButton.addActionListener((actionEvent)->
        {
            newAction.accept(content);
            dispose();
        });
    }

    public void onApplyButtonClicl(Consumer<T> newAction)
    {
        applyButton.addActionListener((actionEvent)->
        {
            newAction.accept(content);
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
        quitButton=new JButton(Localisator.getInstance().getString("quit"));
        quitButton.addActionListener((actionEvent)->dispose());
        applyButton=new JButton(Localisator.getInstance().getString("apply"));

        JPanel footerPanel=new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        footerPanel.add(quitButton, BorderLayout.WEST);
        footerPanel.add(applyButton, BorderLayout.EAST);

        return footerPanel;
    }
}
