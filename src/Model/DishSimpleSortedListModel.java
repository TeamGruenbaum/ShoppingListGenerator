package model;

import java.util.List;

public class DishSimpleSortedListModel extends SimpleSortedListModel<Dish>
{
    public DishSimpleSortedListModel(List<Dish> elements)
    {
        super(elements);
    }



    @Override
    public void add(Dish element)
    {
        super.elements.add(element);
    }

    @Override
    public void remove(int index)
    {
        super.elements.remove(index);
    }
}
