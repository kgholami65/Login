package com.login.auth.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("product")
@Data
public class MongoProduct {

    @Id
    private String id;
    private String name;
    private String type;
    private Long price;

    public MongoProduct(String name, String type, Long price){
        this.price = price;
        this.name = name;
        this.type = type;
    }
}
