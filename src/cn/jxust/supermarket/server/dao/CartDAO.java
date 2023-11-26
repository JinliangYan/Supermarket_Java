package cn.jxust.supermarket.server.dao;

import cn.jxust.supermarket.domain.Cart;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.dao.exception.*;

import java.util.List;

public interface CartDAO {
    void addToCart(int userId, int productId, int quantity) throws UserNotFoundException;
    void removeProductFromCart(int userId, int productId, int quantity) throws UserNotFoundException;
    List<Product> getProductsInCart(int userId) throws UserNotFoundException;
    Cart getCartByUserId(int userId) throws  UserNotFoundException;
    int getProductCountInCart(int userId, int productId) throws UserNotFoundException, ProductNotFoundException;
    void clearCart(int userId) throws UserNotFoundException;
    void addCart(User user);

    void deleteCart(int userId);
}


