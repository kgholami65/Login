package com.login.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("product")
@Data
public class MongoProduct {

    @Id
    private String id;
    private String name;
    private String type;
    private Long price;
    @Field("created_at")
    private LocalDateTime createdAt;

    public MongoProduct(String name, String type, Long price, LocalDateTime createdAt){
        this.price = price;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
    }
}
