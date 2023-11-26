package cn.jxust.supermarket.client.controller;

import cn.jxust.supermarket.client.ClientRequestHandler;
import cn.jxust.supermarket.domain.exception.FoodExpiredException;
import cn.jxust.supermarket.server.service.exception.PaymentException;
import static cn.jxust.supermarket.server.service.PaymentServiceImpl.PaymentInfo;

import java.util.Optional;

public class PaymentController {
    private final ClientRequestHandler requestHandler;

    public PaymentController(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public Optional<PaymentInfo> payForProduct(int productId, int quantity) throws PaymentException, FoodExpiredException {
        String requestType = "PaymentController:PAY_FOR_PRODUCT";
        int[] requestData = new int[]{productId, quantity};
        PaymentInfo paymentInfo = (PaymentInfo) requestHandler.sendRequest(requestType, requestData);
        return Optional.ofNullable(paymentInfo);
    }

    public Optional<PaymentInfo> payAllInCart() throws PaymentException, FoodExpiredException {
        String requestType = "PaymentController:PAY_ALL_IN_CART";
        PaymentInfo paymentInfo = (PaymentInfo) requestHandler.sendRequest(requestType, null);

        return Optional.ofNullable(paymentInfo);
    }
}
