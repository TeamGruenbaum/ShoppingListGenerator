package view;

import model.Fillable;
import model.Sortable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ListContentPanel<U,T extends ListModel<U> & Sortable<U> & Fillable<U>> extends JPanel
{
    private JList<U> elements;
    private T listModel;
    private int lastClickedListItemIndex;
    private JButton addButton;
    private JButton sortButton;
    private JPopupMenu contextMenu;


    public ListContentPanel(T listModel)
    {
        this.listModel=listModel;
        contextMenu=new JPopupMenu();

        setLayout(new BorderLayout());
        add(createHeaderPanel(),BorderLayout.NORTH);
        add(createContentPanel(),BorderLayout.CENTER);
    }

    public void onSortClick(Consumer<T> action)
    {
        sortButton.addActionListener((ActionEvent event)->action.accept(listModel));
    }

    public void onAddClick(Consumer<T> action)
    {
        addButton.addActionListener((ActionEvent event)->action.accept(listModel));
    }

    public void addMenuItem(String label, BiConsumer<T, Integer> action)
    {
        JMenuItem menuItem=new JMenuItem(label);
        menuItem.addActionListener((ActionEvent event)->action.accept(listModel,lastClickedListItemIndex));

        if (getMenuSize()>0) contextMenu.addSeparator();
        contextMenu.add(menuItem);
    }

    public int getMenuSize()
    {
        return contextMenu.getSubElements().length;
    }

    private JPanel createHeaderPanel()
    {
        addButton=new JButton("Add");
        sortButton=new JButton("Sort");

        JPanel headerPanel=new JPanel();
        headerPanel.add(addButton);
        headerPanel.add(sortButton);

        return headerPanel;
    }

    private JScrollPane createContentPanel()
    {
        elements=new JList<U>(listModel);
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