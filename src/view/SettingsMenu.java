package view;

import controller.SwingHelper;
import controller.Themer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

public class SettingsMenu extends JPopupMenu
{
    private JMenuItem aboutMenuItem;
    private JMenuItem appearanceMenuItem;
    private JMenuItem resetMenuItem;


    public SettingsMenu(MainWindow window)
    {
        aboutMenuItem=new JMenuItem("About ShoppingListGenerator");
        aboutMenuItem.addActionListener((aboutEvent)->getAboutDialog(window));

        appearanceMenuItem=new JMenuItem("Switch application theme");
        appearanceMenuItem.addActionListener((actionEvent) ->
        {
            new Themer().switchThemeSetting();

            JOptionPane.showMessageDialog(null, "Please restart the application, so the new settings can be applied");
        });

        resetMenuItem=new JMenuItem("Reset application");
        resetMenuItem.addActionListener((aboutEvent)->
        {
            ResourceBundle resourceBundle=ResourceBundle.getBundle("resources.strings");
            String path=System.getProperty("user.home")+(System.getProperty("os.name").contains("Windows")?"\\"+resourceBundle.getString("application_name")+"\\test.txt":"/"+resourceBundle.getString("application_name"))+"/test.txt";
            new File(path).delete();

            JOptionPane.showMessageDialog(window, "Please restart the application");
            System.exit(0);
        });

        add(aboutMenuItem);
        add(appearanceMenuItem);
        add(resetMenuItem);
    }

    public void showMenuAt(Component invoker)
    {
        show(invoker, invoker.getWidth()/2, invoker.getHeight()/2);
    }

    public JDialog getAboutDialog(MainWindow window)
    {
        JDialog aboutDialog=new JDialog(window, "About", Dialog.ModalityType.APPLICATION_MODAL);
        aboutDialog.setBounds(132, 132, 300, 300);
        aboutDialog.setLocationRelativeTo(null);
        aboutDialog.setMinimumSize(new Dimension(300, 300));
        aboutDialog.setMaximumSize(new Dimension(300, 300));

        Container dialogContainer=aboutDialog.getContentPane();
        dialogContainer.setLayout(new BoxLayout(dialogContainer, BoxLayout.Y_AXIS));

        dialogContainer.add(createAboutHeaderPanel());
        dialogContainer.add(createAboutCenterPanel());

        aboutDialog.setVisible(true);

        return aboutDialog;
    }

    private JPanel createAboutHeaderPanel()
    {
        JPanel header=new JPanel(new GridBagLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 5, 0));

        ImageIcon imageIcon=new ImageIcon(new ImageIcon("src/controller/icon_small.png").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));

        JLabel applicationName=new JLabel("ShoppingListGenerator");
        applicationName.setBorder(BorderFactory.createEmptyBorder(8,0,4,0));
        applicationName.setFont(new Font(applicationName.getFont().getFontName(), applicationName.getFont().getStyle(), 17));

        JLabel version=new JLabel("Version 2021.1");
        version.setBorder(BorderFactory.createEmptyBorder(4,0,5,0));
        version.setFont(new Font(version.getFont().getFontName(), version.getFont().getStyle(), 11));
        version.setForeground(Color.DARK_GRAY);

        header.add(new JLabel(imageIcon), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.VERTICAL));
        header.add(applicationName, SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));
        header.add(version, SwingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));

        return header;
    }

    private JPanel createAboutCenterPanel()
    {
        JPanel center=new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        center.add(SwingHelper.getLinkedJLabel(("Steven Solleder").toUpperCase(), "https://stevensolleder.de/"), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));
        center.add(SwingHelper.getLinkedJLabel(("Isabell Waas").toUpperCase(), "https://github.com/isabellwaas"), SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));

        return center;
    }
}
