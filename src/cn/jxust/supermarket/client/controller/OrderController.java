package cn.jxust.supermarket.client.controller;

import cn.jxust.supermarket.client.ClientRequestHandler;
import cn.jxust.supermarket.domain.Order;
import cn.jxust.supermarket.domain.Product;

import java.time.LocalDate;
import java.util.List;

public class OrderController {
    private final ClientRequestHandler requestHandler;

    public OrderController(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void addOrder(boolean isVipUser, Product product, LocalDate date, int quantity, int pointsCost, double priceBeforeDiscount, double priceAfterDiscount) {
        String requestType = "OrderController:ADD_ORDER";
        Object[] requestDate = {isVipUser, product, date, quantity, pointsCost, priceBeforeDiscount, priceAfterDiscount};
        requestHandler.sendRequest(requestType, requestDate);
    }

    public void removeOrder(Order order) {
        String requestType = "OrderController:REMOVE_ORDER";
        requestHandler.sendRequest(requestType, order);
    }

    @SuppressWarnings("unchecked")
    public List<Order> getOrdersByDate(LocalDate date) {
        String requestType = "OrderController:GET_ORDERS_BY_DATE";
        return (List<Order>) requestHandler.sendRequest(requestType, date);
    }

    @SuppressWarnings("unchecked")
    public List<Order> getOrdersByProduct(int productId) {
        String requestType = "OrderController:GET_ORDERS_BY_PRODUCT";
        return (List<Order>) requestHandler.sendRequest(requestType, productId);
    }

    public double getTotalSalesByDate(LocalDate date) {
        String requestType = "OrderController:GET_TOTAL_SALES_BY_DATE";
        return (double) requestHandler.sendRequest(requestType, date);
    }

    public double getTotalSalesByProduct(int productId) {
        String requestType = "OrderController:GET_TOTAL_SALES_BY_PRODUCT";
        return (double) requestHandler.sendRequest(requestType, productId);
    }

    public double getTotalSales() {
        String requestType = "OrderController:GET_TOTAL_SALES";
        return (double) requestHandler.sendRequest(requestType, null);
    }

    public String getMostPopularProduct(LocalDate date) {
        String requestType = "OrderController:GET_MOST_POPULAR_PRODUCT";
        return (String) requestHandler.sendRequest(requestType, date);
    }

    public String getLeastPopularProduct(LocalDate date) {
        String requestType = "OrderController:GET_LEAST_POPULAR_PRODUCT";
        return (String) requestHandler.sendRequest(requestType, date);
    }
}
