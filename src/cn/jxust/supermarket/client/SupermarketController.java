package cn.jxust.supermarket.client;

import cn.jxust.supermarket.client.controller.*;
import cn.jxust.supermarket.client.view.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SupermarketController implements ViewSwitcher{
    private final UserController userController;
    private final CartController cartController;
    private final OrderController orderController;
    private final ProductController productController;
    private final PaymentController paymentController;

    private final Map<String, View> viewMap;
    private final Scanner scanner;

    public SupermarketController(ClientRequestHandler requestHandler) {
        this.scanner = new Scanner(System.in);
        this.userController = new UserController(requestHandler);
        this.cartController = new CartController(requestHandler);
        this.orderController = new OrderController(requestHandler);
        this.productController = new ProductController(requestHandler);
        this.paymentController = new PaymentController(requestHandler);
        this.viewMap = new HashMap<>();
    }

    public UserController getUserController() {
        return userController;
    }

    public CartController getCartController() {
        return cartController;
    }

    public OrderController getOrderController() {
        return orderController;
    }

    public ProductController getProductController() {
        return productController;
    }

    public PaymentController getPaymentController() {
        return paymentController;
    }

    public Scanner getScanner() {
        return scanner;
    }

    @Override
    public void switchToView(String viewName) {
        viewMap.get(viewName).show();
    }

    protected void registerView(String viewName, View view) {
        viewMap.put(viewName, view);
    }
}
