package controller;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleSortableListModel<T> extends AbstractListModel<T> implements SortableListModel<T>
{
    private List<T> elements;


    public SimpleSortableListModel(List<T> elements) {
        this.elements=new ArrayList<>();
        this.elements.addAll(elements);
        fireContentsChanged(this, 0, elements.size() - 1);
    }


    public void setElements(List<T> elements)
    {
        elements.clear();
        elements.addAll(elements);
        fireContentsChanged(this, 0, elements.size() - 1);
    }

    public void sort(Comparator<T> criterion)
    {
        Collections.sort(elements, criterion);
        fireContentsChanged(this, 0, elements.size()-1);
    }


    @Override
    public int getSize()
    {
        return elements.size();
    }

    @Override
    public T getElementAt(int index)
    {
        return elements.get(index);
    }
}
