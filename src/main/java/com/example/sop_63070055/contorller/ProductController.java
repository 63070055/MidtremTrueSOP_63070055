package com.example.sop_63070055.contorller;

import com.example.sop_63070055.pojo.Product;
import com.example.sop_63070055.repository.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "AddProductQueue")
    public boolean serviceAddProduct(Product product){
        return productService.addProduct(product);
    }
    @RabbitListener(queues = "UpdateProductQueue")
    public boolean serviceUpdateProduct(Product product){
        return productService.updateProduct(product);
    }
    @RabbitListener(queues = "DeleteProductQueue")
    public boolean serviceDeleteProduct(Product product){
        return productService.deleteProduct(product);
    }
    @RabbitListener(queues = "GetNameProductQueue")
    public Product serviceGetProductName(String productName){
        return productService.getProductByName(productName);
    }
    @RabbitListener(queues = "GetAllProductQueue")
    public List<Product> serviceGetAllProduct(){
        return productService.getAllProduct();
    }
}
