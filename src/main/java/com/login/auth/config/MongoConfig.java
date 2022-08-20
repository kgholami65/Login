package com.login.auth.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "auth";
    }
    @Bean
    public MongoClient mongoClient(){
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/auth");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString).build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(),getDatabaseName());
    }
}
