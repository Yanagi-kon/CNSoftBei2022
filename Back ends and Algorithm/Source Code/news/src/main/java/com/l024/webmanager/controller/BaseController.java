package com.l024.webmanager.controller;

import com.l024.webmanager.vo.R;

public interface BaseController<T> {
    R add(T t);
    R del(Long id);
    R update(T t);
    R getAll();
    R get(T t);
}
