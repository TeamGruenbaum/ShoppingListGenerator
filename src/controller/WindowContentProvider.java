package controller;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface WindowContentProvider<T extends JComponent>
{
    T getContent();
    String getTitle();
    Optional<List<Integer>> getSelectedItemIds();
}
