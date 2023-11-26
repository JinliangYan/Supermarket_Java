package cn.jxust.supermarket.client.controller;

import cn.jxust.supermarket.client.ClientRequestHandler;
import cn.jxust.supermarket.domain.Product;

import java.util.List;
import java.util.Optional;

public class ProductController {
    private final ClientRequestHandler requestHandler;

    public ProductController(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @SuppressWarnings("unchecked")
    public Optional<List<Product>> getAllProducts() {
        String requestType = "ProductController:GET_ALL_PRODUCTS";
        List<Product> products = (List<Product>) requestHandler.sendRequest(requestType, null);
        return Optional.ofNullable(products);
    }

    public Optional<Product> getProductById(int id) {
        String requestType = "ProductController:GET_PRODUCT_BY_ID";
        Product product = (Product) requestHandler.sendRequest(requestType, id);

        return Optional.ofNullable(product);
    }
}
