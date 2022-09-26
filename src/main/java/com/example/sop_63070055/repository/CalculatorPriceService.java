package com.example.sop_63070055.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculatorPriceService {
    @Autowired
    private ProductRepository repository;

    public CalculatorPriceService(ProductRepository repository){
        this.repository = repository;
    }
    public double getPrice(double productCost,double productProfit){
        return (productCost + productProfit);
    }
}
