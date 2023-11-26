package cn.jxust.supermarket.server.service;

import java.util.Map;

import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.server.dao.exception.*;

public interface ProductService {
    Map<Integer, Product> getProductsMap();
    Product getProductById(int id) throws ProductNotFoundException;
    void addProduct(Product product) throws ProductException;
    void updateProduct(Product product) throws ProductException;
    void deleteProductById(int id) throws ProductNotFoundException;
}

