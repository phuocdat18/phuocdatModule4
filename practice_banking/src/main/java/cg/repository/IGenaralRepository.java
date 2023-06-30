package com.cg.repository;

import java.util.List;

public interface IGenaralRepository <T> {
    List<T> findAll();

    T findById(Long id);
    List<T> findRecipients(Long id);

    void save(T t);

    void remove(Long id);
}
