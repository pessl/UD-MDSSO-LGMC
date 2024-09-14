package com.cursos.api.springsecuritycourse.service.impl;

import com.cursos.api.springsecuritycourse.dto.SaveCategory;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.entity.Category;
import com.cursos.api.springsecuritycourse.persistence.repository.CategoryRespository;
import com.cursos.api.springsecuritycourse.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRespository categoryRespository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRespository.findAll(pageable);
    }

    @Override
    public Optional<Category> findOneById(Long categoryId) {
        return categoryRespository.findById(categoryId);
    }

    @Override
    public Category createOne(SaveCategory saveCategory) {
        Category category = new Category();
        category.setName(saveCategory.getName());
        category.setStatus(Category.CategoryStatus.ENABLED);

        return categoryRespository.save(category);
    }

    @Override
    public Category updateOneById(Long categoryId, SaveCategory saveCategory) {
        Category categoryFromDb = categoryRespository.findById(categoryId).
                orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + categoryId));
        categoryFromDb.setName(saveCategory.getName());

        return categoryRespository.save(categoryFromDb);
    }

    @Override
    public Category disableOneById(Long categoryId) {
        Category categoryFromDb = categoryRespository.findById(categoryId).
                orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + categoryId));
        categoryFromDb.setStatus(Category.CategoryStatus.DISABLED);
        return categoryRespository.save(categoryFromDb);
    }
}
