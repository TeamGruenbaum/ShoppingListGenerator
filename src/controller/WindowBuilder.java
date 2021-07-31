package controller;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WindowBuilder
{
    private MainWindow window;
    private List<PairedValue<String, JComponent>> contents;
    private int currentContentIndex;

    public WindowBuilder(WindowContentProvider... windowSetuper)
    {
        contents=new ArrayList<>();

        for(WindowContentProvider windowContentProvider:windowSetuper)
        {
            contents.add(new Pair<>(windowContentProvider.getTitle(), windowContentProvider.getContent()));
        }

        currentContentIndex=0;
        window=new MainWindow("ShoppingListGenerator",contents.get(currentContentIndex).getValue(), new Dimension(400, 400));
        window.setBackVisible(false);

        setSettingsFunctionality();
        setForwardFunctionality();
        setBackwardFunctionality();
    }

    private void setSettingsFunctionality()
    {
        window.onSettingsClick(new Runnable()
        {
            @Override
            public void run()
            {
                //TODO: settings action
            }
        });
    }

    private void setForwardFunctionality()
    {
        window.onForwardClick(new Consumer<JButton>()
        {
            @Override
            public void accept(JButton forwardButton)
            {
                if(currentContentIndex+1>contents.size()-1)
                {
                    JPopupMenu lastActionsMenu=new JPopupMenu();

                    JMenuItem copyMenuItem=new JMenuItem("Copy to Clipboard");
                    copyMenuItem.addActionListener((event)->
                    {
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("Text für die Zwischenablage"), null);
                    });

                    JMenuItem saveMenuItem=new JMenuItem("Save on your Computer");
                    saveMenuItem.addActionListener((event)->
                    {
                        String test = "Habe heute keine Lust zum einkaufen :D";
                        JFileChooser chooser = new JFileChooser();
                        int retrival = chooser.showSaveDialog(null);

                        if (retrival == JFileChooser.APPROVE_OPTION) {
                            try {
                                FileWriter fileWriter=new FileWriter(chooser.getSelectedFile()+".txt");
                                fileWriter.write(test);
                                fileWriter.close();
                            }
                            catch (Exception ex) 
                            {
                                JOptionPane.showMessageDialog(new JFrame(), "The file could not be saved.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    lastActionsMenu.add(copyMenuItem);
                    lastActionsMenu.add(saveMenuItem);

                    lastActionsMenu.show(forwardButton, forwardButton.getWidth()/2, forwardButton.getHeight()/2);
                }
                else
                {
                    ++currentContentIndex;
                    updateWindowContent();
                }

                window.setBackVisible(true);
            }
        });
    }

    private void setBackwardFunctionality()
    {
        window.onBackClick(new Consumer<JButton>()
        {
            @Override
            public void accept(JButton backButton)
            {
                if(currentContentIndex--==1)
                {
                    window.setBackVisible(false);
                }
                else
                {
                    window.setBackVisible(true);
                }
                updateWindowContent();
            }
        });
    }

    private void updateWindowContent()
    {
        window.setCurrentTitle(contents.get(currentContentIndex).getKey());
        window.setContent(contents.get(currentContentIndex).getValue());
    }
}
