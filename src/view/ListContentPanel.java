package view;

import controller.SortableListModel;
import model.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.List;

public class ListContentPanel<T> extends JPanel
{
    private JList<T> elements;
    private SortableListModel<T> listModel;
    private int lastClickedListItemIndex;
    private JButton addButton;
    private JButton sortButton;
    private JPopupMenu contextMenu;
    private BiFunction<T, T, Boolean> selectionFunctionality;


    public ListContentPanel(SortableListModel<T> listModel, BiFunction<T, T, Boolean> selectionFunctionality)
    {
        this.listModel=listModel;
        contextMenu=new JPopupMenu();

        this.selectionFunctionality=selectionFunctionality;

        setLayout(new BorderLayout());
        add(createHeaderPanel(),BorderLayout.NORTH);
        add(createContentPanel(),BorderLayout.CENTER);
    }

    public void onSortClick(BiConsumer<JList<T>, SortableListModel<T>> action)
    {
        sortButton.addActionListener((ActionEvent event)->action.accept(elements, listModel));
    }

    public void onAddClick(Consumer<SortableListModel<T>> action)
    {
        addButton.addActionListener((ActionEvent event)->action.accept(listModel));
    }

    public void setSelectedItems(List<T> itemsToSelect)
    {
        List<Integer> indices=new ArrayList<>();

        for (T currentItem:itemsToSelect)
        {
            for (int j = 0; j < elements.getModel().getSize(); j++)
            {
                if(selectionFunctionality.apply(currentItem, elements.getModel().getElementAt(j)))
                {
                    indices.add(j);
                    break;
                }
            }
        }

        elements.setSelectedIndices(indices.stream().mapToInt(i -> i).toArray());
    }

    public List<T> getUnmodifiableSelectedItems()
    {
        return Collections.unmodifiableList(elements.getSelectedValuesList());
    }

    public void addMenuItem(String label, BiConsumer<SortableListModel<T>, Integer> action)
    {
        JMenuItem menuItem=new JMenuItem(label);
        menuItem.addActionListener((ActionEvent event)->action.accept(listModel,lastClickedListItemIndex));

        contextMenu.add(menuItem);
    }

    public int getMenuSize()
    {
        return contextMenu.getSubElements().length;
    }


    private JPanel createHeaderPanel()
    {
        addButton=new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, addButton.getPreferredSize().height));
        sortButton=new JButton("Sort");
        sortButton.setPreferredSize(new Dimension( 100, sortButton.getPreferredSize().height));

        JPanel headerPanel=new JPanel();
        headerPanel.add(addButton);
        headerPanel.add(sortButton);

        return headerPanel;
    }

    private JScrollPane createContentPanel()
    {
        elements=new JList<T>(listModel);
        setListSelectionModel();
        setListMouseListener();

        JScrollPane contentPanel=new JScrollPane();
        contentPanel.setViewportView(elements);

        return contentPanel;
    }

    private void setListSelectionModel()
    {
        elements.setSelectionModel(new DefaultListSelectionModel()
        {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if(super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                }
                else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
    }

    private void setListMouseListener()
    {
        elements.addMouseListener((new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                if(SwingUtilities.isRightMouseButton(event))
                {
                    lastClickedListItemIndex=elements.locationToIndex(event.getPoint());
                    contextMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        }));
    }
}
