package view;

import controller.Localisator;
import controller.PathHelper;
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
        aboutMenuItem=new JMenuItem(Localisator.getInstance().getString("about")+" "+Localisator.getInstance().getString("application_name"));
        aboutMenuItem.addActionListener((aboutEvent)->getAboutDialog(window));

        appearanceMenuItem=new JMenuItem(Localisator.getInstance().getString("switch_application_theme"));
        appearanceMenuItem.addActionListener((actionEvent) ->
        {
            new Themer().switchThemeSetting();

            JOptionPane.showMessageDialog(null, Localisator.getInstance().getString("please_restart_application"));
        });

        resetMenuItem=new JMenuItem(Localisator.getInstance().getString("reset_application"));
        resetMenuItem.addActionListener((aboutEvent)->
        {
            ResourceBundle resourceBundle=ResourceBundle.getBundle("resources.strings");
            PathHelper pathHelper=new PathHelper();
            new File(pathHelper.getSavePath()+pathHelper.getDatabaseName()).delete();

            JOptionPane.showMessageDialog(window, Localisator.getInstance().getString("please_restart_application"));
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
        JDialog aboutDialog=new JDialog(window, Localisator.getInstance().getString("about"), Dialog.ModalityType.APPLICATION_MODAL);
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

        ImageIcon imageIcon=new ImageIcon(new ImageIcon("src/resources/icon_small.png").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));

        JLabel applicationName=new JLabel(Localisator.getInstance().getString("application_name"));
        applicationName.setBorder(BorderFactory.createEmptyBorder(8,0,4,0));
        applicationName.setFont(new Font(applicationName.getFont().getFontName(), applicationName.getFont().getStyle(), 17));

        JLabel version=new JLabel(Localisator.getInstance().getString("version"));
        version.setBorder(BorderFactory.createEmptyBorder(4,0,5,0));
        version.setFont(new Font(version.getFont().getFontName(), version.getFont().getStyle(), 11));

        header.add(new JLabel(imageIcon), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.VERTICAL));
        header.add(applicationName, SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));
        header.add(version, SwingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));

        return header;
    }

    private JPanel createAboutCenterPanel()
    {
        JPanel center=new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        center.add(SwingHelper.getLinkedJLabel((Localisator.getInstance().getString("steven_solleder")).toUpperCase(), Localisator.getInstance().getString("steven_solleder_link")), SwingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));
        center.add(SwingHelper.getLinkedJLabel(Localisator.getInstance().getString("isabell_waas").toUpperCase(), Localisator.getInstance().getString("isabell_waas_link")), SwingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));

        return center;
    }
}
