package view;

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

    public MainWindow (String windowTitle, JComponent content, Dimension minimumSize)
    {
        setTitle(windowTitle);
        setMinimumSize(minimumSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.currentContent=content;
        setLayout(new BorderLayout());
        add(createHeaderPanel(), BorderLayout.NORTH);
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

    private JPanel createHeaderPanel()
    {
        currentTitle=new JLabel("Dishes");
        currentTitle.setFont(new Font(currentTitle.getFont().getFontName(), currentTitle.getFont().getStyle(), 17));
        settingsButton=new JButton("Settings");

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
        backButton=new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, backButton.getPreferredSize().height));
        forwardButton=new JButton("Forward");
        forwardButton.setPreferredSize(new Dimension(100, forwardButton.getPreferredSize().height));

        JPanel footerPanel=new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.add(backButton, BorderLayout.WEST);
        footerPanel.add(forwardButton, BorderLayout.EAST);

        return footerPanel;
    }

    //ONLY FOR TESTING
    /*public static void main(String[] args)
    {
        DefaultListModel<Ingredient> dlm=new DefaultListModel<>();
        dlm.addElement(new Ingredient("Eier", "Edeka", 1));
        dlm.addElement(new Ingredient("Tomaten", "Edeka", 1));
        dlm.addElement(new Ingredient("Toast", "Edeka", 2));
        dlm.addElement(new Ingredient("Tee", "Edeka", 4));
        ListContentPanel lcp=new ListContentPanel(dlm);
        lcp.addMenuItem("Change", new BiConsumer()
        {
            @Override
            public void accept(Object o, Object o2)
            {
                System.out.println("Change");
            }
        });
        lcp.addMenuItem("Remove", new BiConsumer()
        {
            @Override
            public void accept(Object o, Object o2)
            {
                System.out.println("Remove " + o2);
            }
        });
        new MainWindow("ShoppingListGenerator", lcp, new Dimension(600,600),new Dimension(800,800));

        new MainWindow("Test", new TextContentPanel("Shopping List\nRegal 1\n-Tomaten\n-Bananen\n-Kiwis\n\nRegal 2\n-Eier\n-Toast\n-Käse"), new Dimension(400,400),new Dimension(600,600));
    }*/
}
