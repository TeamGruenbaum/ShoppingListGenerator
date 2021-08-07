package view;



import controller.Localisator;
import controller.SwingHelper;

import javax.swing.*;

import java.awt.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;



public class LicensesDialog extends JDialog
{
    private List<JPanel> licenses;
    private Container dialogContainer;
    private JScrollPane center;

    private SwingHelper swingHelper;



    public LicensesDialog(Window owner, Dimension size)
    {
        super(owner, new Localisator().getString("licenses"), Dialog.ModalityType.APPLICATION_MODAL);

        setMinimumSize(size);
        setMaximumSize(size);
        setLocationRelativeTo(null);

        licenses=new ArrayList<>();
        swingHelper=new SwingHelper();

        dialogContainer=getContentPane();
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

    public void addLicensePanel(String libraries, String filePath)
    {
        JPanel license=new JPanel();
        license.setLayout(new BoxLayout(license, BoxLayout.Y_AXIS));

        JTextArea librariesArea=new JTextArea(libraries);
        librariesArea.setLineWrap(true);
        librariesArea.setWrapStyleWord(true);
        librariesArea.setEditable(false);
        librariesArea.setFont(new Font(librariesArea.getFont().getFontName(), Font.BOLD, librariesArea.getFont().getSize()));
        librariesArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JEditorPane licensePane = new JEditorPane();
        licensePane.setEditable(false);
        try
        {
            licensePane.setPage(getClass().getClassLoader().getResource(filePath));
        }
        catch (IOException e)
        {
            licensePane.setContentType("text/html");
            licensePane.setText("<html>Page not found.</html>");
        }

        license.add(librariesArea);
        license.add(licensePane);

        licenses.add(license);
    }


    private JScrollPane createCenter()
    {
        JPanel scrollContainer=new JPanel();
        scrollContainer.setLayout(new GridBagLayout());

        int gridBagLayoutIndex=0;
        for(int i=0; i<licenses.size(); i++)
        {
            if(i != 0)
            {
                JSeparator jSeparator=new JSeparator(JSeparator.HORIZONTAL);
                jSeparator.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));
                scrollContainer.add(jSeparator, swingHelper.getGridBagConstraints(0, gridBagLayoutIndex++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));
            }
            scrollContainer.add(licenses.get(i), swingHelper.getGridBagConstraints(0, gridBagLayoutIndex++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));
        }

        JScrollPane scrollPane=new JScrollPane(scrollContainer);
        javax.swing.SwingUtilities.invokeLater(()->scrollPane.getViewport().setViewPosition(new Point(0,0)));

        return scrollPane;
    }
}
