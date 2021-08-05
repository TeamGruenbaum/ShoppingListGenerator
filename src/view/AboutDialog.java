package view;


import controller.Localisator;
import controller.SwingHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public final class AboutDialog extends JDialog
{
    private JLabel icon;
    private JLabel applicationName;
    private JLabel version;
    private List<JLabel> developers;
    private SwingHelper swingHelper;
    private Container dialogContainer;
    private JPanel center;


    public AboutDialog(Window owner, Dimension size)
    {
        super(owner, new Localisator().getString("about"), Dialog.ModalityType.APPLICATION_MODAL);

        setMinimumSize(size);
        setMaximumSize(size);
        setLocationRelativeTo(null);

        developers=new ArrayList<>();
        swingHelper=new SwingHelper();

        dialogContainer = getContentPane();
        dialogContainer.setLayout(new BoxLayout(dialogContainer, BoxLayout.Y_AXIS));

        dialogContainer.add(createHeader());
    }


    public void setIcon(Image newValue)
    {
        icon.setIcon(new ImageIcon(newValue.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    }

    public void setApplicationName(String newValue)
    {
        applicationName.setText(newValue);
    }

    public void setVersion(String newValue)
    {
        version.setText(newValue);
    }

    public void addDeveloper(String name, String uri)
    {
        developers.add(swingHelper.getLinkedJLabel(name, uri));
    }

    public void showDialog()
    {
        if(center!=null)
        {
            remove(center);
        }
        center=createCenter();
        dialogContainer.add(center);
        this.setVisible(true);
    }

    private JPanel createHeader()
    {
        JPanel header=new JPanel(new GridBagLayout());
        header.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        icon=new JLabel();
        icon.setBorder(BorderFactory.createEmptyBorder(0,0,6,0));

        applicationName=new JLabel();
        applicationName.setBorder(BorderFactory.createEmptyBorder(0,0,4,0));
        applicationName.setFont(new Font(applicationName.getFont().getFontName(), Font.BOLD, 17));

        version=new JLabel();
        version.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        version.setFont(new Font(version.getFont().getFontName(), version.getFont().getStyle(), 11));

        header.add(icon, swingHelper.getGridBagConstraints(0, 0, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));
        header.add(applicationName, swingHelper.getGridBagConstraints(0, 1, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));
        header.add(version, swingHelper.getGridBagConstraints(0, 2, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));

        return header;
    }

    private JPanel createCenter()
    {
        JPanel center=new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));


        int i=0;
        for(JLabel developer:developers)
        {
            center.add(developer, new SwingHelper().getGridBagConstraints(0, i++, 1, 1, 0.1, 0, GridBagConstraints.VERTICAL));
        }

        return center;
    }
}
