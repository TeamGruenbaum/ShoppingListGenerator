package model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomListModel<T> extends AbstractListModel<T> implements Sortable<T>, Fillable<T>
{
    private List<T> elements=new ArrayList<>();

    @Override
    public void sort(Comparator<T> criterion)
    {
        Collections.sort(elements, criterion);
        fireContentsChanged(this, 0, elements.size()-1);
    }

    @Override
    public void addElement(T element)
    {
        elements.add(element);
        fireIntervalAdded(this, elements.size()-1, elements.size()-1);
    }

    @Override
    public void load(List<T> data)
    {
        elements=data;
        fireContentsChanged(this, 0, elements.size()-1);
    }

    @Override
    public void remove(int index)
    {
        elements.remove(index);
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
