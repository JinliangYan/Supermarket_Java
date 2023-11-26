package cn.jxust.supermarket.client.view;

import cn.jxust.supermarket.client.SupermarketController;
import cn.jxust.supermarket.client.controller.ProductController;
import cn.jxust.supermarket.server.dao.exception.ProductNotFoundException;
import cn.jxust.supermarket.domain.Food;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.domain.exception.FoodExpiredException;

import java.util.List;
import java.util.Scanner;

public class ProductView implements UserInputHandler, View {
    private final Scanner scanner;
    private final ProductController productController;

    private static final String MENU = "\n请选择一个选项：\n"
            + "1. 浏览所有商品\n"
            + "2. 查看商品详情\n"
            + "0. 返回";

    public ProductView(SupermarketController supermarketController) {
        this.scanner = supermarketController.getScanner();
        this.productController = supermarketController.getProductController();
    }

    public void showProduct(Product product, int quantity) {
        String tag = "";
        if (product instanceof Food food) {
            try {
                food.checkExpired();
            } catch (FoodExpiredException e) {
                tag = "(已过期)";
            }
        }
        String mes = "ID:%03d %s * %d ￥%.2f\n".formatted(product.getId(), product.getName(), quantity, quantity * product.getPrice()) + tag;
        System.out.println(mes);
    }


    @Override
    public void display() {
        System.out.println(MENU);
    }

    @Override
    public void handleUserInput() {
        while (true) {
            display();
            String userInput = getUserInput("请选择", scanner);
            switch (userInput) {
                case "1":
                    //浏览所有商品
                    List<Product> productList = productController.getAllProducts().get();
                    for (Product product : productList) {
                        showProduct(product, product.getStock());
                    }
                    break;
                case "2":
                    //根据商品ID查询商品
                    try {
                        int productId = getUserIntInput("请输入商品ID", scanner);
                        Product product = productController.getProductById(productId).get();
                        product.showDetails();
                    } catch (ProductNotFoundException e) {
                        showError(e.getMessage());
                    }
                    break;
                case "0":
                    return;
                default:
                    showError("无效的选项，请重试！");
            }
        }
    }

    @Override
    public void show() {
        handleUserInput();
    }
}