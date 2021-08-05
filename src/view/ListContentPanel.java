package view;

import controller.Localisator;
import controller.SimpleListModel;
import model.Identifiable;

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



public final class ListContentPanel<T extends Identifiable> extends JPanel
{
    private JList<T> list;
    private List<T> elements;
    private int lastClickedListItemIndex;

    private JButton addButton;
    private JButton sortButton;

    private JPopupMenu contextMenu;



    public ListContentPanel()
    {
        list=new JList<>();
        setElements(new ArrayList<>());

        contextMenu=new JPopupMenu();

        setLayout(new BorderLayout());
        add(createHeaderPanel(),BorderLayout.NORTH);
        add(createContentPanel(),BorderLayout.CENTER);
    }


    public void setElements(List<T> newValues)
    {
        List<Integer> selectedBeforeSorting= list.getSelectedValuesList().stream().map(Identifiable::getId).collect(Collectors.toList());
        list.clearSelection();

        this.list.setModel(new SimpleListModel<>(newValues));
        this.elements = newValues;

        for(int i = 0; i< list.getModel().getSize(); i++)
        {
            if(selectedBeforeSorting.contains(list.getModel().getElementAt(i).getId()))
            {
                list.setSelectedIndex(i);
            }
        }
    }

    public void sortElements(Comparator<T> comparator)
    {
        List<Integer> selectedBeforeSorting= list.getSelectedValuesList().stream().map(Identifiable::getId).collect(Collectors.toList());
        list.clearSelection();

        elements.sort(comparator);

        setElements(elements);

        for(int i = 0; i< list.getModel().getSize(); i++)
        {
            if(selectedBeforeSorting.contains(list.getModel().getElementAt(i).getId()))
            {
                list.setSelectedIndex(i);
            }
        }
    }

    public T getElementAt(int index)
    {
        return elements.get(index);
    }


    public void setSelectedItems(List<T> newValues)
    {
        list.clearSelection();

        List<Integer> ids=newValues.stream().map((item) -> item.getId()).collect(Collectors.toList());

        for(int i = 0; i< list.getModel().getSize(); i++)
        {
            if(ids.contains(list.getModel().getElementAt(i).getId()))
            {
                list.setSelectedIndex(i);
            }
        }
    }

    public List<T> getUnmodifiableSelectedItems()
    {
        return Collections.unmodifiableList(list.getSelectedValuesList());
    }


    public void onSortButtonClick(Consumer<ListContentPanel<T>> newAction)
    {
        sortButton.addActionListener((ActionEvent event)->newAction.accept(this));
    }

    public void onAddButtonClick(Consumer<ListContentPanel<T>> newAction)
    {
        addButton.addActionListener((ActionEvent event)->newAction.accept(this));
    }


    public void addMenuItem(String label, BiConsumer<ListContentPanel<T>, Integer> action)
    {
        JMenuItem menuItem=new JMenuItem(label);
        menuItem.addActionListener((ActionEvent event)->action.accept(this, lastClickedListItemIndex));

        contextMenu.add(menuItem);
    }


    private JPanel createHeaderPanel()
    {
        Localisator localisator=new Localisator();

        addButton=new JButton(localisator.getString("add"));
        addButton.setPreferredSize(new Dimension(100, addButton.getPreferredSize().height));
        sortButton=new JButton(localisator.getString("sort"));
        sortButton.setPreferredSize(new Dimension( 100, sortButton.getPreferredSize().height));

        JPanel headerPanel=new JPanel();
        headerPanel.add(addButton);
        headerPanel.add(sortButton);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        return headerPanel;
    }

    private JScrollPane createContentPanel()
    {
        list =new JList<T>();
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setListSelectionModel();
        setListMouseListener();

        JScrollPane contentPanel=new JScrollPane();
        contentPanel.setViewportView(list);

        return contentPanel;
    }

    private void setListSelectionModel()
    {
        list.setSelectionModel(new DefaultListSelectionModel()
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
        list.addMouseListener((new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                if(SwingUtilities.isRightMouseButton(event))
                {
                    lastClickedListItemIndex= list.locationToIndex(event.getPoint());
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
