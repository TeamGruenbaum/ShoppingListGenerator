package controller;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;

public interface SortableListModel<T> extends ListModel<T>
{
    void setElements(List<T> elements);
    void sort(Comparator<T> criterion);
}
