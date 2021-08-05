package controller;


import javax.swing.*;

import java.util.List;



public class SimpleListModel<T> extends AbstractListModel<T>
{
    List<T> elements;


    public SimpleListModel(List<T> elements)
    {
        this.elements=elements;
        fireContentsChanged(this, 0, elements.size());
    }


    @Override
    public int getSize()
    {
        return elements.size();
    }

    @Override
    public T getElementAt(int index) {
        return elements.get(index);
    }
}
