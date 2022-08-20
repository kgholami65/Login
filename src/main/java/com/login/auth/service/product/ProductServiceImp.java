package com.login.auth.service.product;

import com.login.auth.model.MongoProduct;
import com.login.auth.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImp implements IProductService{
    private final IProductRepository productRepository;
    @Autowired
    public ProductServiceImp(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<MongoProduct> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void addProduct(String name, String type, Long price) {
        productRepository.save(new MongoProduct(name,type,price));
    }

    @Override
    public boolean deleteProduct(String id) {
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
