package model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class SimpleSortedListModel<T> extends AbstractListModel<T>
{
    private List<T> elements;



    public SimpleSortedListModel(List<T> elements)
    {
        this.elements=new ArrayList<>();
        this.elements.addAll(elements);
        fireContentsChanged(this, 0, elements.size()-1);
    }


    public abstract void add(T element);

    public abstract void remove(int index);


    public void sort(Comparator<T> criterion)
    {
        Collections.sort(elements, criterion);
        fireContentsChanged(this, 0, elements.size()-1);
    }

    public List<T> getUnmodifiableList()
    {
        return Collections.unmodifiableList(elements);
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
