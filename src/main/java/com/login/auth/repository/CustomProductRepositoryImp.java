package com.login.auth.repository;

import com.login.auth.model.MongoProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomProductRepositoryImp implements ICustomProductRepository{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomProductRepositoryImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public void updatePriceById(String id, Long price) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("price",price);
        mongoTemplate.updateFirst(query,update, MongoProduct.class);
    }
}
