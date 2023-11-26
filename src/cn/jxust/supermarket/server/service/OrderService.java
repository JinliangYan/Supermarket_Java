package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.domain.Order;
import cn.jxust.supermarket.domain.Product;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    void addOrder(boolean isVipUser, Product product, LocalDate date, int amount, int pointsCost, double priceBeforeDiscount, double priceAfterDiscount);
    void removeOrder(Order order);
    List<Order> getOrdersByDate(LocalDate date);
    List<Order> getOrdersByProduct(int productId);
    double getTotalSalesByDate(LocalDate date);
    double getTotalSalesByProduct(int productId);
    double getTotalSales();
    String getMostPopularProduct(LocalDate date);
    String getLeastPopularProduct(LocalDate date);
}
