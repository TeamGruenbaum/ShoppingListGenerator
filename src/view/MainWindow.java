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


    public MainWindow (String windowTitle, JComponent content, Dimension minimumSize, Dimension maximumSize)
    {
        setTitle(windowTitle);
        setMinimumSize(minimumSize);
        setMaximumSize(maximumSize);
        setLocation(0,0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        add(content,BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void onSettingsClick(Runnable action)
    {
        settingsButton.addActionListener((ActionEvent event)->action.run());
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
        settingsButton=new JButton("Settings");

        JPanel headerPanel=new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel,BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        headerPanel.add(currentTitle);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(settingsButton);

        return headerPanel;
    }

    private JPanel createFooterPanel()
    {
        backButton=new JButton("Back");
        forwardButton=new JButton("Forward");

        JPanel footerPanel=new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        footerPanel.add(backButton);
        footerPanel.add(forwardButton);

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
