package cn.jxust.supermarket.server.controller;

import cn.jxust.supermarket.domain.exception.FoodExpiredException;
import cn.jxust.supermarket.server.service.exception.PaymentException;

public interface Controller {
    // CartController methods
    default Object getCartContent(Object requestData) { return null; }
    default Object getCartItemsMap(Object requestData) { return null; }
    default Object getTotalPrice(Object requestData) { return null; }
    default Object addToCart(Object requestData) { return null; }
    default Object removeFromCart(Object requestData) { return null; }
    default Object getQuantity(Object requestData) { return null; }

    // OrderController methods
    default Object addOrder(Object requestData) { return null; }
    default Object removeOrder(Object requestData) { return null; }
    default Object getOrdersByDate(Object requestData) { return null; }
    default Object getOrdersByProduct(Object requestData) { return null; }
    default Object getTotalSalesByDate(Object requestData) { return null; }
    default Object getTotalSalesByProduct(Object requestData) { return null; }
    default Object getTotalSales(Object requestData) { return null; }
    default Object getMostPopularProduct(Object requestData) { return null; }
    default Object getLeastPopularProduct(Object requestData) { return null; }

    // PaymentController methods
    default Object payForProduct(Object requestData) throws PaymentException, FoodExpiredException { return null; }
    default Object payAllInCart(Object requestData) throws PaymentException, FoodExpiredException { return null; }

    // ProductController methods
    default Object getAllProducts(Object requestData) { return null; }
    default Object getProductById(Object requestData) { return null; }

    // UserController methods
    default Object setCurrentUser(Object requestData) { return null; }
    default Object register(Object requestData) { return null; }
    default Object login(Object requestData) { return null; }
    default Object logout(Object requestData) { return null; }
    default Object addUser(Object requestData) { return null; }
    default Object updateUser(Object requestData) { return null; }
    default Object deleteUserById(Object requestData) { return null; }
}
