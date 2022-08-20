package com.login.auth.service.product;

import com.login.auth.model.MongoProduct;
import com.login.auth.repository.ICustomProductRepository;
import com.login.auth.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImp implements IProductService{
    private final IProductRepository productRepository;
    private final ICustomProductRepository customProductRepository;


    @Autowired
    public ProductServiceImp(IProductRepository productRepository, ICustomProductRepository customProductRepository) {
        this.productRepository = productRepository;
        this.customProductRepository = customProductRepository;
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

    @Override
    public boolean checkProductById(String id) {
        return productRepository.existsById(id);
    }

    @Override
    public void updatePriceById(String id, Long price) {
        customProductRepository.updatePriceById(id, price);
    }
}
