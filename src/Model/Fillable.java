package model;

import java.util.List;

public interface Fillable<T>
{
    public void addElement(T element);
    public void load(List<T> data);
    public void remove(int index);
}
