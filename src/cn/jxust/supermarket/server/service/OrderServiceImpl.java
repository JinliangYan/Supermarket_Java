package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.server.dao.OrderDAO;
import cn.jxust.supermarket.domain.Order;
import cn.jxust.supermarket.domain.OrderItem;
import cn.jxust.supermarket.domain.Product;

import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public void addOrder(boolean isVipUser, Product product, LocalDate date, int amount, int pointsCost, double priceBeforeDiscount, double priceAfterDiscount) {
        Order order = new OrderItem(isVipUser, product, date, amount, pointsCost, priceBeforeDiscount, priceAfterDiscount);
        orderDAO.addOrder(order);
    }

    public void removeOrder(Order order) {
        orderDAO.removeOrder(order);
    }

    public List<Order> getOrdersByDate(LocalDate date) {
        return orderDAO.getOrdersByDate(date);
    }

    public List<Order> getOrdersByProduct(int productId) {
        return orderDAO.getOrdersByProduct(productId);
    }

    public double getTotalSalesByDate(LocalDate date) {
        return orderDAO.getTotalSalesByDate(date);
    }

    public double getTotalSalesByProduct(int productId) {
        return orderDAO.getTotalSalesByProduct(productId);
    }

    public double getTotalSales() {
        return orderDAO.getTotalSales();
    }

    public String getMostPopularProduct(LocalDate date) {
        return orderDAO.getMostPopularProduct(date);
    }

    public String getLeastPopularProduct(LocalDate date) {
        return orderDAO.getLeastPopularProduct(date);
    }
}
