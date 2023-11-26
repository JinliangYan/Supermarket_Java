package cn.jxust.supermarket.server.util;

import cn.jxust.supermarket.server.controller.*;
import cn.jxust.supermarket.server.service.*;

public class ControllerFactory {
    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final OrderService orderService;

    public ControllerFactory(UserService userService, CartService cartService, ProductService productService, PaymentService paymentService, OrderService orderService) {
        this.userService = userService;
        this.cartService = cartService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    public UserController createUserController() {
        return new UserController(userService);
    }

    public CartController createCartController() {
        return new CartController(cartService, userService);
    }

    public OrderController createOrderController() {
        return new OrderController(orderService);
    }

    public PaymentController createPaymentController() {
        return new PaymentController(paymentService, userService);
    }

    public ProductController createProductController() {
        return new ProductController(productService);
    }
}
