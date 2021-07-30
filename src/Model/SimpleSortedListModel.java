package model;

import controller.SerializableBiConsumer;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

public class SimpleSortedListModel<T> extends AbstractListModel<T>
{
    private List<T> elements;
    private SerializableBiConsumer<List<T>, T> addAction;
    private SerializableBiConsumer<List<T>, T> removeAction;


    public SimpleSortedListModel(List<T> elements, SerializableBiConsumer<List<T>, T> addAction, SerializableBiConsumer<List<T>, T> removeAction)
    {
        this.elements=new ArrayList<>();
        this.elements.addAll(elements);
        fireContentsChanged(this, 0, elements.size()-1);

        this.addAction=addAction;
        this.removeAction=removeAction;
    }


    public void add(T element)
    {
        addAction.accept(elements, element);
    }

    public void remove(T element)
    {
        removeAction.accept(elements, element);
    }


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
