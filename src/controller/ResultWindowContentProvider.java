package controller;



import view.TextContentPanel;



public class ResultWindowContentProvider implements WindowContentProvider<TextContentPanel>
{
    private TextContentPanel content;



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
        return new Localisator().getString("shopping_list");
    }
}
