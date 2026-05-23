package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.util.EntityIdResolver;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class CrudService<T, ID> {
    private final JpaRepository<T, ID> repository;
    private final String resourceName;

    protected CrudService(JpaRepository<T, ID> repository, String resourceName) {
        this.repository = repository;
        this.resourceName = resourceName;
    }

    protected abstract ID parseId(String id);

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public T findById(String rawId) {
        ID id = parseId(rawId);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, id));
    }

    @Transactional
    public T create(T entity) {
        return repository.save(entity);
    }

    @Transactional
    public T update(String rawId, T entity) {
        ID id = parseId(rawId);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(resourceName, id);
        }
        EntityIdResolver.assignId(entity, id);
        return repository.save(entity);
    }

    @Transactional
    public void delete(String rawId) {
        ID id = parseId(rawId);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(resourceName, id);
        }
        repository.deleteById(id);
    }

    protected Integer parseIntegerId(String id) {
        return Integer.valueOf(id);
    }

    protected Long parseLongId(String id) {
        return Long.valueOf(id);
    }
}
