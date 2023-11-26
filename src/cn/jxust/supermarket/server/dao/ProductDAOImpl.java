package cn.jxust.supermarket.server.dao;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import cn.jxust.supermarket.server.dao.exception.*;
import cn.jxust.supermarket.domain.Product;

public class ProductDAOImpl implements ProductDAO {

    private final Map<Integer, Product> productsMap = Collections.synchronizedMap(new TreeMap<>(Integer::compareTo)); //储存超市的所有商品
    @Override
    public Map<Integer, Product> getProductsMap() {
        return productsMap;
    }

    @Override
    public Product getProductById(int id) throws ProductNotFoundException {
        Product product = productsMap.get(id);
        if (product == null)
            throw new ProductNotFoundException("不存在ID为 " + id + " 的商品");
        return product;
    }

    @Override
    public void addProduct(Product product) throws ProductAlreadyExistsException {
        if (productsMap.containsKey(product.getId())) {
            throw new ProductAlreadyExistsException("已存在ID为 " + product.getId() + " 的商品");
        } else {
            productsMap.put(product.getId(), product);
        }
    }

    @Override
    public void updateProduct(Product product) throws ProductNotFoundException {
        if (!productsMap.containsKey(product.getId()))
            throw new ProductNotFoundException("不存在ID为 " + product + " 的商品");
        productsMap.put(product.getId(), product);
    }

    @Override
    public void deleteProductById(int id) throws ProductNotFoundException {
        if (!productsMap.containsKey(id))
            throw new ProductNotFoundException("不存在ID为 " + id + " 的商品");
        productsMap.remove(id);
    }

    @Override
    public boolean havaProduct(int productId) {
        return productsMap.containsKey(productId);
    }

    @Override
    public int getProductStock(int productId) throws ProductNotFoundException {
        return getProductById(productId).getStock();
    }

    @Override
    public void decreaseStock(int productId, int quantity) throws ProductNotFoundException, OutOfStockException {
        Product product = getProductById(productId);
        int newStock = product.getStock() - quantity;
        if (newStock < 0) {
            throw new OutOfStockException("ID为 " + productId + " 的商品库存不足");
        }
        product.setStock(newStock);
        updateProduct(product);
    }

}
