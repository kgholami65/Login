package com.login.auth.service.product;

import com.login.auth.model.MongoProduct;

import java.util.List;

public interface IProductService {
    List<MongoProduct> findAll();
    void addProduct(String name, String type, Long price);
    boolean deleteProduct(String id);
}
