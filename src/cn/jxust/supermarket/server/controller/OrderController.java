package cn.jxust.supermarket.server.controller;

import cn.jxust.supermarket.domain.Order;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.server.ApiResponse;
import cn.jxust.supermarket.server.service.OrderService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class OrderController implements Controller {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public Object addOrder(Object requestData) {
        Object[] data = (Object[]) requestData;
        boolean isVipUser = (boolean) data[0];
        Product product = (Product) data[1];
        LocalDate date = (LocalDate) data[2];
        int quantity = (int) data[3];
        int pointsCost = (int) data[4];
        double priceBeforeDiscount = (double) data[5];
        double priceAfterDiscount = (double) data[6];
        orderService.addOrder(isVipUser, product, date, quantity, pointsCost, priceBeforeDiscount, priceAfterDiscount);
        return new ApiResponse(true, null, null);
    }

    public Object removeOrder(Object requestData) {
        Order order = (Order) requestData;
        orderService.removeOrder(order);
        return new ApiResponse(true, null, null);
    }

    public Object getOrdersByDate(Object requestData) {
        LocalDate date = (LocalDate) requestData;
        return new ApiResponse(true, orderService.getOrdersByDate(date), null);
    }

    public Object getOrdersByProduct(Object requestData) {
        int productId = (int) requestData;
        return new ApiResponse(true, orderService.getOrdersByProduct(productId), null);
    }

    public Object getTotalSalesByDate(Object requestData) {
        LocalDate date = (LocalDate) requestData;
        return new ApiResponse(true, orderService.getTotalSalesByDate(date), null);
    }

    public Object getTotalSalesByProduct(Object requestData) {
        int productId = (int) requestData;
        return new ApiResponse(true, orderService.getTotalSalesByProduct(productId), null);
    }

    public Object getTotalSales(Object requestData) {
        return new ApiResponse(true, orderService.getTotalSales(), null);
    }

    public Object getMostPopularProduct(Object requestData) {
        LocalDate date = (LocalDate) requestData;
        return new ApiResponse(true, orderService.getMostPopularProduct(date), null);
    }

    public Object getLeastPopularProduct(Object requestData) {
        LocalDate date = (LocalDate) requestData;
        return new ApiResponse(true, orderService.getLeastPopularProduct(date), null);
    }
}
