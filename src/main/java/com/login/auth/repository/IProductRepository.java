package com.login.auth.repository;

import com.login.auth.model.MongoProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IProductRepository extends MongoRepository<MongoProduct, String> {
    MongoProduct findMongoProductById(Long id);
    int countAllByCreatedAtAfter(LocalDateTime creationTime);
}
