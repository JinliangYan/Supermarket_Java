package cn.jxust.supermarket.server.dao;

import cn.jxust.supermarket.domain.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDAO {
    void addOrder(Order order);

    void removeOrder(Order order);

    List<Order> getOrdersByDate(LocalDate date);

    List<Order> getOrdersByProduct(int productId);

    double getTotalSalesByDate(LocalDate date);

    double getTotalSalesByProduct(int productId);

    double getTotalSales();

    String getMostPopularProduct(LocalDate date);

    String getLeastPopularProduct(LocalDate date);
}
