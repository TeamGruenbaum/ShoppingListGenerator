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
    private JButton backButton;
    private JButton forwardButton;
    private JComponent currentContent;

    public MainWindow (String windowTitle, String contentTitle, JComponent content, Dimension minimumSize)
    {
        setTitle(windowTitle);
        setMinimumSize(minimumSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.currentContent=content;
        setLayout(new BorderLayout());
        add(createHeaderPanel(contentTitle), BorderLayout.NORTH);
        add(content,BorderLayout.CENTER);
        add(createFooterPanel(),BorderLayout.SOUTH);
        setVisible(true);
    }

    public String getCurrentTitle()
    {
        return currentTitle.getText();
    }

    public void setCurrentTitle(String text)
    {
        currentTitle.setText(text);
    }

    public void setContent(JComponent content)
    {
        remove(this.currentContent);
        add(content,BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
        currentContent=content;
    }

    public void onSettingsClick(Consumer<JButton> action)
    {
        settingsButton.addActionListener((ActionEvent event)->action.accept(settingsButton));
    }

    public void onBackClick(Consumer<JButton> action)
    {
        backButton.addActionListener((ActionEvent event)->action.accept(backButton));
    }

    public void setBackVisible(boolean visible)
    {
        backButton.setVisible(visible);
    }

    public void onForwardClick(Consumer<JButton> action)
    {
        forwardButton.addActionListener((ActionEvent event)->action.accept(forwardButton));
    }

    public void setForwardVisible(boolean visible)
    {
        forwardButton.setVisible(visible);
    }

    private JPanel createHeaderPanel(String contentTitle)
    {
        currentTitle=new JLabel(contentTitle);
        currentTitle.setFont(new Font(currentTitle.getFont().getFontName(), currentTitle.getFont().getStyle(), 17));
        settingsButton=new JButton(Localisator.getInstance().getString("settings"));

        JPanel headerPanel=new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel,BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(currentTitle);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(settingsButton);

        return headerPanel;
    }

    private JPanel createFooterPanel()
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
