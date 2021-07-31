package controller;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface WindowContentProvider
{
    JComponent getContent();
    String getTitle();
    Optional<List<Integer>> getSelectedItemIds();
}
