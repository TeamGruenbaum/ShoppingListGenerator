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

        add(createFooterPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }


    public T getContent()
    {
        return content;
    }

    public void setContent(T newValue)
    {
        if(this.content!=null)
        {
            remove(content);
        }
        add(newValue, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
        this.content=newValue;
    }


    public void onQuitButtonClick(Consumer<T> newAction)
    {
        quitButton.addActionListener((actionEvent)->
        {
            newAction.accept(content);
            dispose();
        });
    }

    public void onApplyButtonClick(Consumer<T> newAction)
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
        Localisator localisator=new Localisator();
        quitButton=new JButton(localisator.getString("quit"));
        quitButton.addActionListener((actionEvent)->dispose());
        applyButton=new JButton(localisator.getString("apply"));

        JPanel footerPanel=new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        footerPanel.add(quitButton, BorderLayout.WEST);
        footerPanel.add(applyButton, BorderLayout.EAST);

        return footerPanel;
    }
}
