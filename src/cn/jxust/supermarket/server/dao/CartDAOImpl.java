package cn.jxust.supermarket.server.dao;

import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.dao.exception.*;
import cn.jxust.supermarket.domain.Cart;
import cn.jxust.supermarket.domain.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CartDAOImpl implements CartDAO {

    private final Map<Integer, Cart> carts = new ConcurrentHashMap<>();
    private final ProductDAO productDAO;

    public CartDAOImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public void addToCart(int userId, int productId, int quantity) throws UserNotFoundException, ProductException{
        Cart targetCart = carts.get(userId);
        if (targetCart == null)
            throw new UserNotFoundException("不存在ID为 " + userId + "的用户");
        Map<Integer, Integer> cartMap = targetCart.getItemsMap();
        if (!productDAO.havaProduct(productId))
            throw new ProductNotFoundException("不存在ID为 " + productId + "的商品");
        if (productDAO.getProductStock(productId) < quantity)
            throw new OutOfStockException("ID为" + productId + "的库存不足");
        cartMap.merge(productId, quantity, Integer::sum);

    }

    @Override
    public void removeProductFromCart(int userId, int productId, int quantity) throws UserNotFoundException, ProductNotFoundException{
        Cart targetCart = carts.get(userId);
        if (targetCart == null)
            throw new UserNotFoundException("不存在ID为 " + userId + "的用户");
        Map<Integer, Integer> cartMap = targetCart.getItemsMap();
        if (!productDAO.havaProduct(productId))
            throw new ProductNotFoundException("不存在ID为 " + productId + "的商品");
        cartMap.merge(productId, -quantity, Integer::sum);
        if (cartMap.get(productId) <= 0)
            cartMap.remove(productId);
    }

    @Override
    public List<Product> getProductsInCart(int userId) throws UserNotFoundException {
        Cart targetCart = carts.get(userId);
        List<Product> productList = new ArrayList<>();
        if (targetCart == null)
            throw new UserNotFoundException("不存在ID为 " + userId + "的用户");
        Map<Integer, Integer> cartMap = targetCart.getItemsMap();

        // 使用传统的 for-each 循环替换 Lambda 表达式
        for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
            productList.add(productDAO.getProductById(entry.getKey()));
        }

        return productList;
    }


    @Override
    public Cart getCartByUserId(int userId) throws UserNotFoundException {
        Cart targetCart = carts.get(userId);
        if (targetCart == null) {
            throw new UserNotFoundException("不存在ID为 " + userId + "的用户");
        }
        return targetCart;
    }

    @Override
    public int getProductCountInCart(int userId, int productId) throws UserNotFoundException{
        Cart targetCart = carts.get(userId);
        if (targetCart == null) {
            throw new UserNotFoundException("不存在ID为 " + userId + "的用户");
        }
        Map<Integer, Integer> cartMap = targetCart.getItemsMap();
        return cartMap.getOrDefault(productId, 0);
    }

    @Override
    public void clearCart(int userId) {
        carts.get(userId).getItemsMap().clear();
    }

    @Override
    public void addCart(User user) {
        Cart newCart = new Cart(user.getId());
        carts.put(user.getId(), newCart);
        user.setCart(carts.get(user.getId()));
    }

    @Override
    public void deleteCart(int userId) {
        carts.remove(userId);
    }
}

