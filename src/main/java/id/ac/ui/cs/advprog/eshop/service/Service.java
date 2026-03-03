package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;

import java.util.List;

public interface Service<T> {
    public T create(T Object);
    public List<T> findAll();
    public T findById(String id);
    public T edit(T object);
    public void delete(String id);
}
