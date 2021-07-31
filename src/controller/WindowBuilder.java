package controller;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
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

                    lastActionsMenu.add("Test1");
                    lastActionsMenu.add("Test2");

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
