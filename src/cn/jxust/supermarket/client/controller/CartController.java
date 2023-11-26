package cn.jxust.supermarket.client.controller;

import cn.jxust.supermarket.client.ClientRequestHandler;
import cn.jxust.supermarket.server.dao.exception.ProductException;
import cn.jxust.supermarket.server.service.exception.NotLoggedInException;

import java.util.Map;
import java.util.Optional;

public class CartController {
    private final ClientRequestHandler requestHandler;

    public CartController(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public Optional<String> getCartContent() throws NotLoggedInException {
        String requestType = "CartController:GET_CART_CONTENT";
        String cartContent = (String) requestHandler.sendRequest(requestType, null);
        return Optional.ofNullable(cartContent);
    }

    @SuppressWarnings("unchecked")
    public Optional<Map<Integer, Integer>> getCartItemsMap() throws NotLoggedInException {
        String requestType = "CartController:GET_CART_ITEMS_MAP";
        Map<Integer, Integer> cartItemsMap = (Map<Integer, Integer>) requestHandler.sendRequest(requestType, null);
        return Optional.ofNullable(cartItemsMap);
    }

    public double getTotalPrice() throws NotLoggedInException {
        String requestType = "CartController:GET_TOTAL_PRICE";
        Double totalPrice = (Double) requestHandler.sendRequest(requestType, null);
        return totalPrice != null ? totalPrice : 0;
    }

    public void addToCart(int productId, int quantity) throws ProductException, NotLoggedInException {
        try {
            String requestType = "CartController:ADD_TO_CART";
            Object[] requestData = new Object[]{productId, quantity};
            requestHandler.sendRequest(requestType, requestData);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFromCart(int productId, int quantity) throws ProductException, NotLoggedInException {
        String requestType = "CartController:REMOVE_FROM_CART";
        Object[] requestData = new Object[]{productId, quantity};
        ProductException exception = (ProductException) requestHandler.sendRequest(requestType, requestData);
        if (exception != null) {
            throw exception;
        }
    }

    public int getQuantity(int productId) {
        String requestType = "CartController:GET_QUANTITY";
        Integer quantity = (Integer) requestHandler.sendRequest(requestType, productId);
        return quantity != null ? quantity : 0;
    }
}
