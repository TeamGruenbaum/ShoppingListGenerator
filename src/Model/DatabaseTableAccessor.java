package model;

import java.sql.SQLException;
import java.util.List;



public interface DatabaseTableAccessor<T>
{
    List<T> getAll() throws SQLException;
    int update(T item) throws SQLException;
    void add(T item) throws SQLException;
    void remove(int id) throws SQLException;
}