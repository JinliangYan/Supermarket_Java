package cn.jxust.supermarket.server.service;

import java.util.Map;

import cn.jxust.supermarket.server.dao.ProductDAO;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.server.dao.exception.*;

public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Map<Integer, Product> getProductsMap() {
        return productDAO.getProductsMap();
    }

    @Override
    public Product getProductById(int id) throws ProductNotFoundException{
        return productDAO.getProductById(id);
    }

    @Override
    public void addProduct(Product product) throws ProductException {
        // 校验产品信息
        if (nameIsBlank(product)) {
            throw new ProductException("商品的名字不能为空");
        }
        // ...省略其他校验
        productDAO.addProduct(product);
    }

    @Override
    public void updateProduct(Product product) throws ProductException {
        if (nameIsBlank(product)) {
            throw new ProductException("商品的名字不能为空");
        }
        // ...省略其他校验
        productDAO.updateProduct(product);
    }

    private static boolean nameIsBlank(Product product) {
        return product.getName() == null || product.getName().trim().isEmpty();
    }

    @Override
    public void deleteProductById(int id) throws ProductNotFoundException {
        productDAO.deleteProductById(id);
    }
}
