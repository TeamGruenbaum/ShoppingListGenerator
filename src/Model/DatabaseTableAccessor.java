package model;


import java.sql.SQLException;

import java.util.List;



public interface DatabaseTableAccessor<T extends Identifiable>
{
    List<T> getAll() throws SQLException;
    int update(T element) throws SQLException;
    void add(T element) throws SQLException;
    void remove(int id) throws SQLException;
}