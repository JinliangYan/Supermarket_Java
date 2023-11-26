package cn.jxust.supermarket.server.dao;

import java.util.Map;

import cn.jxust.supermarket.server.dao.exception.OutOfStockException;
import cn.jxust.supermarket.server.dao.exception.ProductAlreadyExistsException;
import cn.jxust.supermarket.server.dao.exception.ProductNotFoundException;
import cn.jxust.supermarket.domain.Product;

public interface ProductDAO {
    /**
     * 获取所有商品列表
     *
     * @return 商品列表
     */
    Map<Integer, Product> getProductsMap();

    /**
     * 根据商品id获取商品信息
     *
     * @param id 商品id
     * @return 商品信息
     */
    Product getProductById(int id) throws ProductNotFoundException;

    /**
     * 添加商品
     *
     * @param product 要添加的商品对象
     */
    void addProduct(Product product) throws ProductAlreadyExistsException;

    /**
     * 更新商品信息
     *
     * @param product 要更新的商品对象
     */
    void updateProduct(Product product) throws ProductNotFoundException;

    /**
     * 根据商品id删除商品
     *
     * @param id 商品id
     */
    void deleteProductById(int id) throws ProductNotFoundException;

    int getProductStock(int productId) throws ProductNotFoundException;

    boolean havaProduct(int productId);

    public void decreaseStock(int productId, int quantity) throws ProductNotFoundException, OutOfStockException;
}