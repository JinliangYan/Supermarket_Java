package cn.jxust.supermarket.server.controller;

import cn.jxust.supermarket.server.ApiResponse;
import cn.jxust.supermarket.server.dao.exception.ProductException;
import cn.jxust.supermarket.server.dao.exception.ProductNotFoundException;
import cn.jxust.supermarket.server.dao.exception.UserNotFoundException;
import cn.jxust.supermarket.server.service.CartService;
import cn.jxust.supermarket.server.service.UserService;
import cn.jxust.supermarket.server.service.exception.NotLoggedInException;

import java.util.Map;

public class CartController implements Controller {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    private ApiResponse handleUserException(UserNotFoundException e) throws NotLoggedInException {
        return new ApiResponse(false, null, e);
    }

    public Object getCartContent(Object requestData) throws NotLoggedInException {
        int userId = userService.getCurrentUser().getId();
        try {
            Map<Integer, Integer> cartItemsMap = cartService.getCartItemsMap(userId);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-30s %-10s %-10s%n", "商品ID", "商品名称", "单价", "数量"));
            for (Map.Entry<Integer, Integer> entry : cartItemsMap.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();
                String productName;
                double price;
                try {
                    productName = cartService.getProductName(productId);
                    price = cartService.getItemPrice(userId, productId);
                } catch (ProductNotFoundException e) {
                    // 处理商品未找到异常
                    continue;
                }
                sb.append(String.format("%-10d %-30s %-10.2f %-10d%n", productId, productName, price, quantity));
            }
            return new ApiResponse(true, sb.toString(), null);
        } catch (UserNotFoundException e) {
            return handleUserException(e);
        }
    }

    public Object getCartItemsMap(Object requestData) throws NotLoggedInException {
        int userId = userService.getCurrentUser().getId();
        try {
            return new ApiResponse(true, cartService.getCartItemsMap(userId), null);
        } catch (UserNotFoundException e) {
            return handleUserException(e);
        }
    }

    public Object getTotalPrice(Object requestData) throws NotLoggedInException {
        int userId = userService.getCurrentUser().getId();
        try {
            return new ApiResponse(true, cartService.getTotalPrice(userId), null);
        } catch (UserNotFoundException e) {
            return handleUserException(e);
        }
    }

    public Object addToCart(Object requestData) throws ProductException, NotLoggedInException {
        int userId = userService.getCurrentUser().getId();
        Object[] data = (Object[]) requestData;
        int productId = (int) data[0];
        int quantity = (int) data[1];
        try {
            cartService.addToCart(userId, productId, quantity);
            return new ApiResponse(true, null, null);
        } catch (UserNotFoundException e) {
            return handleUserException(e);
        }
    }

    public Object removeFromCart(Object requestData) throws ProductException, NotLoggedInException {
        int userId = userService.getCurrentUser().getId();
        Object[] data = (Object[]) requestData;
        int productId = (int) data[0];
        int quantity = (int) data[1];
        try {
            cartService.removeProductFromCart(userId, productId, quantity);
            return new ApiResponse(true, null, null);
        } catch (UserNotFoundException e) {
            return handleUserException(e);
        }
    }

    public Object getQuantity(Object requestData) {
        int productId = (int) requestData;
        return new ApiResponse(true,
                cartService.getProductCountInCart(userService.getCurrentUser().getId(), productId),
                null);
    }
}
