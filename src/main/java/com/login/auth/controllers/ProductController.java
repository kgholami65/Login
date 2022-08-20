package com.login.auth.controllers;

import com.login.auth.model.MongoProduct;
import com.login.auth.model.ProductReqModel;
import com.login.auth.repository.IProductRepository;
import com.login.auth.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody ProductReqModel productReqModel){
        productService.addProduct(productReqModel.getName(), productReqModel.getType(), productReqModel.getPrice());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/view")
    public ResponseEntity<List<MongoProduct>> getAll(){
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id){
        if(productService.deleteProduct(id))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
