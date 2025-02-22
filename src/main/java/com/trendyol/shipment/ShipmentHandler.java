package com.trendyol.shipment;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class ShipmentHandler {

    private final List<Product> products;

    private static final Integer BASKET_SIZE_THRESHOLD = 3;

    private static final List<ShipmentSize> sizes = Arrays.asList(ShipmentSize.values());

    public ShipmentHandler(List<Product> products) {
        this.products = products;
    }

    public ShipmentSize calculateShipmentSize() {
        return (basketHasSizeOfThreeOrMore(products))
                ? (basketHasTheSameProductSizeOfThreeOrMore(products))
                    ? (basketHasOnlyXLargeProducts(products))
                        ? ShipmentSize.X_LARGE
                        : getLargerSize(getShipmentSizeOfRecurringProducts(products))
                    : getTheLargestProductInBasket(products)
                : getTheLargestProductInBasket(products);
    }

    private Boolean basketHasSizeOfThreeOrMore(List<Product> products) {
        return products.size() >= BASKET_SIZE_THRESHOLD;
    }

    private Map<ShipmentSize, Long> getShipmentSizeCounts(List<Product> products) {
        return products.stream().collect(groupingBy(Product::getSize, counting()));
    }

    private Boolean basketHasTheSameProductSizeOfThreeOrMore(List<Product> products) {
        return getShipmentSizeCounts(products).values().stream().anyMatch(count -> count >= BASKET_SIZE_THRESHOLD);
    }

    private ShipmentSize getShipmentSizeOfRecurringProducts(List<Product> products) {
        return getShipmentSizeCounts(products).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }

    private ShipmentSize getLargerSize(ShipmentSize size) {
        return sizes.stream().skip(sizes.indexOf(size) + 1).findFirst().orElse(null);
    }

    private ShipmentSize getTheLargestProductInBasket(List<Product> products) {
        return products.stream().map(Product::getSize).max(Comparator.naturalOrder()).orElse(null);
    }

    private Boolean basketHasOnlyXLargeProducts(List<Product> products) {
        return products.stream().allMatch(product -> product.getSize() == ShipmentSize.X_LARGE);
    }
}
