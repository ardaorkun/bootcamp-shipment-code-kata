package com.trendyol.shipment;

import java.util.*;

public class Basket {

    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        return new ShipmentHandler(getProducts()).calculateShipmentSize();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
