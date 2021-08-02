package view;

import controller.Identifiable;
import controller.SimpleListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;



public class ListContentPanel<T extends Identifiable> extends JPanel
{
    private JList<T> jList;
    private List<T> elements;
    private int lastClickedListItemIndex;
    private JButton addButton;
    private JButton sortButton;
    private JPopupMenu contextMenu;



    public ListContentPanel()
    {
        jList=new JList<>();
        setElements(new ArrayList<>());

        contextMenu=new JPopupMenu();

        setLayout(new BorderLayout());
        add(createHeaderPanel(),BorderLayout.NORTH);
        add(createContentPanel(),BorderLayout.CENTER);
    }

    public void setElements(List<T> elements)
    {
        List<Integer> selectedBeforeSorting= jList.getSelectedValuesList().stream().map(Identifiable::getId).collect(Collectors.toList());
        jList.clearSelection();

        this.jList.setModel(new SimpleListModel<>(elements));
        this.elements = elements;

        for(int i = 0; i< jList.getModel().getSize(); i++)
        {
            if(selectedBeforeSorting.contains(jList.getModel().getElementAt(i).getId()))
            {
                jList.setSelectedIndex(i);
            }
        }
    }

    public void sortElements(Comparator<T> comparator)
    {
        List<Integer> selectedBeforeSorting= jList.getSelectedValuesList().stream().map(Identifiable::getId).collect(Collectors.toList());
        jList.clearSelection();

        elements.sort(comparator);

        setElements(elements);

        for(int i = 0; i< jList.getModel().getSize(); i++)
        {
            if(selectedBeforeSorting.contains(jList.getModel().getElementAt(i).getId()))
            {
                jList.setSelectedIndex(i);
            }
        }
    }

    public void setSelectedItems(List<T> items)
    {
        jList.clearSelection();

        List<Integer> ids=items.stream().map((item) -> item.getId()).collect(Collectors.toList());

        for(int i = 0; i< jList.getModel().getSize(); i++)
        {
            if(ids.contains(jList.getModel().getElementAt(i).getId()))
            {
                jList.setSelectedIndex(i);
            }
        }
    }

    public T getElementAt(int index)
    {
        return elements.get(index);
    }

    public List<T> getUnmodifiableSelectedItems()
    {
        return Collections.unmodifiableList(jList.getSelectedValuesList());
    }


    public void onSortClick(Consumer<ListContentPanel<T>> action)
    {
        sortButton.addActionListener((ActionEvent event)->action.accept(this));
    }

    public void onAddClick(Consumer<ListContentPanel<T>> action)
    {
        addButton.addActionListener((ActionEvent event)->action.accept(this));
    }


    public void addMenuItem(String label, BiConsumer<ListContentPanel<T>, Integer> action)
    {
        JMenuItem menuItem=new JMenuItem(label);
        menuItem.addActionListener((ActionEvent event)->action.accept(this, lastClickedListItemIndex));

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
        jList =new JList<T>();
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setListSelectionModel();
        setListMouseListener();

        JScrollPane contentPanel=new JScrollPane();
        contentPanel.setViewportView(jList);

        return contentPanel;
    }

    private void setListSelectionModel()
    {
        jList.setSelectionModel(new DefaultListSelectionModel()
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
        jList.addMouseListener((new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                if(SwingUtilities.isRightMouseButton(event))
                {
                    lastClickedListItemIndex= jList.locationToIndex(event.getPoint());
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
