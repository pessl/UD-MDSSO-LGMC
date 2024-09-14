package com.cursos.api.springsecuritycourse.persistence.repository;

import com.cursos.api.springsecuritycourse.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRespository extends JpaRepository<Product, Long> {
}
