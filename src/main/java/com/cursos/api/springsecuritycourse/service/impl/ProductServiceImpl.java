package com.cursos.api.springsecuritycourse.service.impl;

import com.cursos.api.springsecuritycourse.dto.SaveProduct;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.entity.Category;
import com.cursos.api.springsecuritycourse.persistence.entity.Product;
import com.cursos.api.springsecuritycourse.persistence.repository.ProductRespository;
import com.cursos.api.springsecuritycourse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRespository productRespository;

    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRespository.findAll(pageable);
    }

    @Override
    public Optional<Product> findOneById(Long productId) {
        return productRespository.findById(productId);
    }

    @Override
    public Product createOne(SaveProduct saveProduct) {
        Product product = new Product();
        product.setName(saveProduct.getName());
        product.setPrice(saveProduct.getPrice());
        product.setStatus(Product.ProductStatus.ENABLED);

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        product.setCategory(category);

        return productRespository.save(product);
    }

    @Override
    public Product updateOneById(Long productId, SaveProduct saveProduct) {
        Product productFromDb = productRespository.findById(productId).
                orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));
        productFromDb.setName(saveProduct.getName());
        productFromDb.setPrice(saveProduct.getPrice());

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        productFromDb.setCategory(category);

        return productRespository.save(productFromDb);
    }

    @Override
    public Product disableOneById(Long productId) {
        Product productFromDb = productRespository.findById(productId).
                orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));
        productFromDb.setStatus(Product.ProductStatus.DISABLED);
        return productRespository.save(productFromDb);
    }
}
