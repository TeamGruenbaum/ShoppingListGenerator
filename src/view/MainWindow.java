package view;

import controller.Localisator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public final class MainWindow extends JFrame
{
    private JLabel currentTitle;
    private JButton settingsButton;

    private JComponent currentContent;

    private JButton backButton;
    private JButton forwardButton;



    public MainWindow (String windowTitle, JComponent content, Dimension minimumSize)
    {
        setTitle(windowTitle);
        setMinimumSize(minimumSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.currentContent=content;
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(content,BorderLayout.CENTER);
        add(createFooter(),BorderLayout.SOUTH);
    }

    public String getCurrentTitle()
    {
        return currentTitle.getText();
    }

    public void setCurrentTitle(String newValue)
    {
        currentTitle.setText(newValue);
    }


    public void onSettingsButtonClick(Consumer<JButton> newAction)
    {
        settingsButton.addActionListener((ActionEvent event)->newAction.accept(settingsButton));
    }


    public void setContent(JComponent newValue)
    {
        remove(this.currentContent);
        add(newValue,BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
        currentContent=newValue;
    }


    public void onBackClick(Consumer<JButton> newAction) //TODO
    {
        backButton.addActionListener((ActionEvent event)->newAction.accept(backButton));
    }

    public void setBackVisible(boolean visible)
    {
        backButton.setVisible(visible);
    } //TODO


    public void onForwardClick(Consumer<JButton> newAction) //TODO
    {
        forwardButton.addActionListener((ActionEvent event)->newAction.accept(forwardButton));
    }

    public void showWindow()
    {
        setVisible(true);
    }


    private JPanel createHeader()
    {
        currentTitle=new JLabel();
        currentTitle.setFont(new Font(currentTitle.getFont().getFontName(), Font.BOLD, 17));
        settingsButton=new JButton(Localisator.getInstance().getString("settings"));

        JPanel headerPanel=new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel,BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(currentTitle);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(settingsButton);

        return headerPanel;
    }

    private JPanel createFooter()
    {
        backButton=new JButton(Localisator.getInstance().getString("back"));
        backButton.setPreferredSize(new Dimension(100, backButton.getPreferredSize().height));
        forwardButton=new JButton(Localisator.getInstance().getString("forward"));
        forwardButton.setPreferredSize(new Dimension(100, forwardButton.getPreferredSize().height));

        JPanel footerPanel=new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.add(backButton, BorderLayout.WEST);
        footerPanel.add(forwardButton, BorderLayout.EAST);

        return footerPanel;
    }
}
