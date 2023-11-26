package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.server.dao.exception.OutOfStockException;
import cn.jxust.supermarket.domain.exception.FoodExpiredException;
import cn.jxust.supermarket.server.service.exception.PaymentException;

public interface PaymentService {
    PaymentServiceImpl.PaymentInfo payForProduct(int userId, int productId, int quantity) throws PaymentException, OutOfStockException, FoodExpiredException;
    PaymentServiceImpl.PaymentInfo payAllInCart(int userId) throws PaymentException, FoodExpiredException;
    void pay(int userId, double amount) throws PaymentException;
}
