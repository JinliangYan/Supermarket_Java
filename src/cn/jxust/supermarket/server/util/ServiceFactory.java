package cn.jxust.supermarket.server.util;

import cn.jxust.supermarket.server.dao.*;
import cn.jxust.supermarket.server.service.*;

public class ServiceFactory {
    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final CartDAO cartDAO;
    private final OrderDAO orderDAO;

    public ServiceFactory(UserDAO userDAO, ProductDAO productDAO, CartDAO cartDAO, OrderDAO orderDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.cartDAO = cartDAO;
        this.orderDAO = orderDAO;
    }

    public UserService createUserService() {
        return new UserServiceImpl(userDAO);
    }

    public CartService creatCartService() {
        return new CartServiceImpl(cartDAO, productDAO);
    }

    public OrderService creatOrderService() {
        return new OrderServiceImpl(orderDAO);
    }

    public PaymentService creatPaymentService() {
        return new PaymentServiceImpl(userDAO, productDAO, orderDAO);
    }

    public ProductService creatProductService() {
        return new ProductServiceImpl(productDAO);
    }
}
