package com.l024.webmanager.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageEntity<T>{
    private long totalElements;//总记录数
    private int currentPage;//当前页数
    private int totalPage;//总页数
    private List<T> content;
}
