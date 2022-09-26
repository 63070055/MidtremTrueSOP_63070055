package com.example.sop_63070055.view;

import com.example.sop_63070055.pojo.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Route("ProductView")
public class ProductView extends VerticalLayout {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ProductView() {
        ComboBox<String> ProductList = new ComboBox("Product List :");
        ProductList.setWidth("600px");

        TextField ProductName = new TextField("ProductName :");
        ProductName.setWidth("600px");

        NumberField ProductCost = new NumberField("ProductCost :");
        ProductCost.setValue(0.0);
        ProductCost.setWidth("600px");
        NumberField ProductProfit = new NumberField("ProductProfit :");
        ProductProfit.setValue(0.0);
        ProductProfit.setWidth("600px");
        NumberField ProductPrice = new NumberField("ProductPrice :");
        ProductPrice.setValue(0.0);
        ProductPrice.setWidth("600px");
        ProductPrice.setEnabled(false);

        Button Add = new Button("Add Product");
        Button Update = new Button("Update Product");
        Button Delete = new Button("Delete Product");
        Button Clear = new Button("Clear Product");

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("600px");

        hl.add(Add, Update, Delete, Clear);
        add(ProductList, ProductName, ProductCost, ProductProfit, ProductPrice, hl);

        ProductList.addFocusListener(e -> {
            List<Product> listall =
                    (List<Product>) rabbitTemplate.convertSendAndReceive("ProductExchange", "getall", "");
            List<String> nameall = new ArrayList<>();
            for (Product p:listall){
                nameall.add(p.getProductName());
            };
            ProductList.setItems(nameall);
        });
        ProductList.addValueChangeListener(e -> {
            String nameProduct = ProductList.getValue();
            Product p = (Product) rabbitTemplate.convertSendAndReceive("ProductExchange","getname",nameProduct);
            System.out.println(p);
            ProductName.setValue(p.getProductName());
            ProductCost.setValue(p.getProductCost());
            ProductPrice.setValue(p.getProductPrice());
            ProductProfit.setValue(p.getProductProfit());
        });

        ProductProfit.addKeyPressListener(e ->{
           if(e.getKey().toString().equals("Enter")){
               double price = WebClient.create()
                       .get()
                       .uri("http://localhost:8080/getPrice/" + ProductCost.getValue() + "/" + ProductProfit.getValue())
                       .retrieve()
                       .bodyToMono(double.class)
                       .block();
               ProductPrice.setValue(price);
           }
        });

        ProductCost.addKeyPressListener(e ->{
            if(e.getKey().toString().equals("Enter")){
                double price = WebClient.create()
                        .get()
                        .uri("http://localhost:8080/getPrice/" + ProductCost.getValue() + "/" + ProductProfit.getValue())
                        .retrieve()
                        .bodyToMono(double.class)
                        .block();
                ProductPrice.setValue(price);
            }
        });

        Add.addClickListener(e -> {
            double price = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/" + ProductCost.getValue() + "/" + ProductProfit.getValue())
                    .retrieve()
                    .bodyToMono(double.class)
                    .block();
            ProductPrice.setValue(price);
            Product product = new Product(null, ProductName.getValue(), ProductCost.getValue(), ProductProfit.getValue(), ProductPrice.getValue());
            boolean check = (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange","add", product);
            if (check){
                Notification.show("Add Product Complete", 500, Notification.Position.BOTTOM_START);
            }
        });
        Update.addClickListener(e -> {
            double price = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/" + ProductCost.getValue() + "/" + ProductProfit.getValue())
                    .retrieve()
                    .bodyToMono(double.class)
                    .block();
            ProductPrice.setValue(price);
            Product product = (Product) rabbitTemplate.convertSendAndReceive("ProductExchange", "getname",ProductList.getValue());
            Product upproduct = new Product(product.get_id(), ProductName.getValue(), ProductCost.getValue(), ProductProfit.getValue(), product.getProductPrice());
            System.out.println(upproduct);
            boolean check = (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange","update", upproduct);
            if (check){
                Notification.show("Update Product Complete", 500, Notification.Position.BOTTOM_START);
            }
        });
        Delete.addClickListener(e ->{
            String name = ProductList.getValue();
            System.out.println(name);
            Product select = (Product) rabbitTemplate.convertSendAndReceive("ProductExchange", "getname", name);
            boolean check = (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "delete", select);
            if (check){
                Notification.show("Delete Product Complete", 500, Notification.Position.BOTTOM_START);
            }
        });
        Clear.addClickListener(e -> {
            ProductName.setValue("");
            ProductCost.setValue(0.0);
            ProductPrice.setValue(0.0);
            ProductProfit.setValue(0.0);
            Notification.show("Clear Product Complete", 500, Notification.Position.BOTTOM_START);
        });
    }
}
