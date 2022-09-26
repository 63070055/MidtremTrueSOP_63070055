package com.example.sop_63070055.contorller;

import com.example.sop_63070055.repository.CalculatorPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorPriceController {
    @Autowired
    private CalculatorPriceService calculatorPriceService;

    @RequestMapping(value = "/getPrice/{cost}/{profit}", method = RequestMethod.GET)
    public ResponseEntity<?> serviceGetProducts(@PathVariable("cost") double cost,@PathVariable("profit") double profit){
        double totle = calculatorPriceService.getPrice(cost,profit);
        return ResponseEntity.ok(totle);
    }
}
