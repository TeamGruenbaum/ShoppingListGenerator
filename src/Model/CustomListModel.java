package model;

import javax.swing.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomListModel<T> extends AbstractListModel implements Sortable<T>, Fillable<T>
{
    private List<T> elements;

    @Override
    public void sort(Comparator<T> criterion)
    {
        Collections.sort(elements, criterion);
    }

    @Override
    public void addElement(T element)
    {
        elements.add(element);
    }

    @Override
    public void load(List<T> data)
    {
        elements=data;
    }

    @Override
    public void remove(int index)
    {
        elements.remove(index);
    }

    @Override
    public int getSize()
    {
        return elements.size();
    }

    @Override
    public Object getElementAt(int index)
    {
        return elements.get(index);
    }
}
