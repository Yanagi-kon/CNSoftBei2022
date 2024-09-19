package com.l024.webmanager.service;

import java.util.List;

public interface BaseService<T> {
    boolean add(T t);
    boolean del(long id);
    List<T> getAll();
    boolean update(T t);
}
