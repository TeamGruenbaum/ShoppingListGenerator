package controller;

import view.TextContentPanel;

import java.util.List;
import java.util.Optional;

public class ResultWindowContentProvider implements WindowContentProvider<TextContentPanel>
{
    TextContentPanel content;



    public ResultWindowContentProvider()
    {
        content=new TextContentPanel();
    }


    @Override
    public TextContentPanel getContent()
    {
        return content;
    }

    @Override
    public String getTitle()
    {
        return Localisator.getInstance().getString("shopping_list");
    }
}
