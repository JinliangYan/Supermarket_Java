package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.server.dao.exception.ProductException;
import cn.jxust.supermarket.server.dao.exception.ProductNotFoundException;
import cn.jxust.supermarket.server.dao.exception.UserNotFoundException;
import cn.jxust.supermarket.domain.Product;

import java.util.List;
import java.util.Map;

public interface CartService {
    void addToCart(int userId, int productId, int quantity) throws UserNotFoundException, ProductException;

    void removeProductFromCart(int userId, int productId, int quantity) throws UserNotFoundException, ProductException;

    void clearCart(int userId) throws UserNotFoundException;

    List<Product> getCartProducts(int userId) throws UserNotFoundException;

    int getProductCountInCart(int userId, int productId) throws UserNotFoundException, ProductNotFoundException;

    double getTotalPrice(int userId) throws UserNotFoundException;

    int getProductCountSame(int userId) throws UserNotFoundException;

    Map<Integer, Integer> getCartItemsMap(int userId) throws UserNotFoundException;

    double getItemPrice(int userId, int productId);

//    void checkout(int userId) throws UserNotFoundException, PaymentException;
    String getProductName(int productId) throws ProductNotFoundException;
}