package cn.jxust.supermarket.server.controller;

import cn.jxust.supermarket.domain.exception.FoodExpiredException;
import cn.jxust.supermarket.server.ApiResponse;
import cn.jxust.supermarket.server.dao.exception.OutOfStockException;
import cn.jxust.supermarket.server.service.PaymentService;
import cn.jxust.supermarket.server.service.PaymentServiceImpl;
import cn.jxust.supermarket.server.service.UserService;
import cn.jxust.supermarket.server.service.exception.PaymentException;

public class PaymentController implements Controller {
    private final PaymentService paymentService;
    private final UserService userService;

    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    public Object payForProduct(Object requestData) throws PaymentException, FoodExpiredException {
        Object[] data = (Object[]) requestData;
        int productId = (int) data[0];
        int quantity = (int) data[1];
        try {
            return new ApiResponse(true,
                    paymentService.payForProduct(userService.getCurrentUser().getId(), productId, quantity),
                    null);
        } catch (PaymentException | OutOfStockException | FoodExpiredException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    public Object payAllInCart(Object requestData) throws PaymentException, FoodExpiredException {
        try {
            return new ApiResponse(true,
                    paymentService.payAllInCart(userService.getCurrentUser().getId()),
                    null);
        } catch (PaymentException | FoodExpiredException e) {
            return ApiResponse.errorResponse(e);
        }
    }
}
