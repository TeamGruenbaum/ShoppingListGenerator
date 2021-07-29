package model;

import java.util.List;

public interface Fillable<T>
{
    void addElement(T element);
    void load(List<T> data);
    void remove(int index);
}
