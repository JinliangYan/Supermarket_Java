package cn.jxust.supermarket.client.view;

import cn.jxust.supermarket.client.SupermarketController;
import cn.jxust.supermarket.client.controller.CartController;
import cn.jxust.supermarket.domain.Food;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.domain.exception.FoodExpiredException;
import cn.jxust.supermarket.server.dao.exception.ProductException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CartView implements UserInputHandler, View {

    private Scanner scanner;
    private final SupermarketController supermarketController;
    private final CartController cartController;

    private static final String MENU = "\n您想要做什么？\n"
            + "1. 将商品移出购物车\n"
            + "2. 添加更多\n"
            + "3. 结账\n"
            + "0. 返回主菜单";

    public CartView(SupermarketController supermarketController) {
        this.supermarketController = supermarketController;
        this.scanner = supermarketController.getScanner();
        this.cartController = supermarketController.getCartController();
    }

    @Override
    public void display() {
        showAll();
        System.out.println(MENU);
    }

    public void showProduct(int productId, int quantity) {
        if (supermarketController.getProductController().getProductById(productId).isEmpty())
            return;
        Product product = supermarketController.getProductController().getProductById(productId).get();
        String tag = "";
        if (product instanceof Food food) {
            try {
                food.checkExpired();
            } catch (FoodExpiredException e) {
                tag = "(已过期)";
            }
        }
        String mes = "ID:%03d %s * %d ￥%.2f\n".formatted(productId, product.getName(), quantity, quantity * product.getPrice()) + tag;
        System.out.println(mes);
    }

    public boolean showAll() {
        String content = cartController.getCartContent().get();
        if (content.isEmpty()) {
            System.out.println();
            System.out.println("您的购物车空空如也，快去添加吧~");
            System.out.println();
            return false;
        }
        System.out.println("您的购物车里有：");
        System.out.println(content);
        return true;
    }

    @Override
    public void handleUserInput() {
        while (true) {
            display();
            String userInput = getUserInput("请选择", scanner);

            int productId;
            int quantity;
            switch (userInput) {
                case "1":
                    if (isEmpty()) break;
                    productId = getUserIntInput("请输入要移出的商品ID", scanner);
                    quantity = getUserIntInput("请输入数量", scanner);
                    try {
                        cartController.removeFromCart(productId, quantity);
                        showMessage("移出成功！");
                    } catch (ProductException e) {
                        showError("出错了，请检查输入后重试！");
                    }
                    break;
                case "2":
                    productId = getUserIntInput("请输入要添加的商品ID", scanner);
                    quantity = getUserIntInput("请输入数量", scanner);
                    try {
                        cartController.addToCart(productId, quantity);
                        showMessage("添加成功！");
                    } catch (RuntimeException e) {
                        showError("出错了，请检查输入后重试！" + e.getMessage());
                    }
                    break;
                case "3":
                    // 显示订单信息并确认
                    if (showAll()) {
                        supermarketController.switchToView("paymentView");
                    }
                    break;
                case "0":
                    return;
                default:
                    showError("输入有误，请重新输入！");
            }
        }
    }

    private boolean isEmpty() {
        if (cartController.getCartItemsMap().isEmpty()) {
            System.out.println("您的购物车空空如也，快去添加吧~");
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        handleUserInput();
    }
}
