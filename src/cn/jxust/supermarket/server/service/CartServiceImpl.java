package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.server.dao.CartDAO;
import cn.jxust.supermarket.server.dao.ProductDAO;
import cn.jxust.supermarket.domain.Cart;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.server.dao.exception.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    private void updateTotPrice(int userId, int productId, int quantity) {
        Cart cart = cartDAO.getCartByUserId(userId);
        cart.setTotPrise(cart.getTotPrise() + productDAO.getProductById(productId).getPrice() * quantity);
        if (cart.getTotPrise() < 0)
            cart.setTotPrise(0);
    }
    @Override
    public void addToCart(int userId, int productId, int quantity) throws UserNotFoundException, ProductException {
        Product product = productDAO.getProductById(productId);
        if (product.getStock() < quantity)
            throw new OutOfStockException("ID为 " + productId + "的商品库存不足");
        cartDAO.addToCart(userId, productId, quantity);
        updateTotPrice(userId, productId, quantity);
    }

    @Override
    public void removeProductFromCart(int userId, int productId, int quantity) throws ProductException, UserNotFoundException {
        int cartQuantity = getProductCountInCart(userId, productId);
        if (cartQuantity < quantity)
            throw new ProductException("购物车中没有足够的ID为 " + productId + "的商品");
        cartDAO.removeProductFromCart(userId, productId, quantity);
    }

    @Override
    public void clearCart(int userId) throws UserNotFoundException {
        cartDAO.clearCart(userId);
        cartDAO.getCartByUserId(userId).setTotPrise(0);
    }

    @Override
    public List<Product> getCartProducts(int userId) throws UserNotFoundException {
        return cartDAO.getProductsInCart(userId);
    }

    @Override
    public int getProductCountInCart(int userId, int productId) throws UserNotFoundException, ProductNotFoundException {
        return cartDAO.getProductCountInCart(userId, productId);
    }

    @Override
    public double getTotalPrice(int userId) throws UserNotFoundException {
        return cartDAO.getCartByUserId(userId).getTotPrise();
    }

    @Override
    public int getProductCountSame(int userId) throws UserNotFoundException {
        class Count implements Serializable {
            int tot = 0;
            void plus(int count) {
                tot += count;
            }
        }
        Count count = new Count();

        Map<Integer, Integer> itemsMap = cartDAO.getCartByUserId(userId).getItemsMap();
        for (Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
            count.plus(entry.getValue());
        }

        return count.tot;
    }


    @Override
    public Map<Integer, Integer> getCartItemsMap(int userId) throws UserNotFoundException{
        return cartDAO.getCartByUserId(userId).getItemsMap();
    }

    @Override
    public double getItemPrice(int userId, int productId) throws UserNotFoundException, ProductNotFoundException {
        int quantity = cartDAO.getProductCountInCart(userId, productId);
        if (quantity == 0) {
            throw new ProductNotFoundException("购物车中没有该商品");
        }
        Product product = productDAO.getProductById(productId);
        return quantity * product.getPrice();
    }

    @Override
    public String getProductName(int productId) throws ProductNotFoundException {
        return productDAO.getProductById(productId).getName();
    }
}
