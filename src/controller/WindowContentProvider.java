package controller;



import javax.swing.*;



public interface WindowContentProvider<T extends JComponent>
{
    T getContent();
    String getTitle();
}
