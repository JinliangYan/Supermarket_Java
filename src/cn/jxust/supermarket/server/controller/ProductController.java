package cn.jxust.supermarket.server.controller;

import cn.jxust.supermarket.server.ApiResponse;
import cn.jxust.supermarket.server.dao.exception.ProductException;
import cn.jxust.supermarket.server.dao.exception.ProductNotFoundException;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.server.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductController implements Controller{

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public Object getAllProducts(Object requestData) {
        List<Product> productList = new ArrayList<>();
        Map<Integer, Product> productsMap = productService.getProductsMap();

        for (Map.Entry<Integer, Product> entry : productsMap.entrySet()) {
            Product product = entry.getValue();
            productList.add(product);
        }

        return new ApiResponse(true, productList, null);
    }


    // 根据 ID 获取商品
    public Object getProductById(Object requestData) throws ProductNotFoundException {
        try {
            int productId = (int) requestData;
            return new ApiResponse(true, productService.getProductById(productId), null);
        } catch (ProductNotFoundException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    // 添加商品
    public Object addProduct(Object requestData) throws ProductException {
        Product product = (Product) requestData;
        productService.addProduct(product);
        return new ApiResponse(true, null, null);
    }

    // 删除商品
    public Object deleteProduct(Object requestData) throws ProductNotFoundException {
        int productId = (int) requestData;
        productService.deleteProductById(productId);
        return new ApiResponse(true, null, null);
    }

    // 更新商品
    public Object updateProduct(Object requestData) throws ProductException {
        Product product = (Product) requestData;
        productService.updateProduct(product);
        return new ApiResponse(true, null, null);
    }
}
