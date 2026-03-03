package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

public interface Repository<T> {
    T create(T object);
    Iterator<T> findAll();
    T findById(String id);
    T edit(T object);
    void delete(String id);
}
