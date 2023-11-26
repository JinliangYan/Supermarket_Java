package cn.jxust.supermarket.client.view;

import cn.jxust.supermarket.client.SupermarketController;

import java.util.Scanner;

public class MainView implements UserInputHandler, View {
    private final Scanner scanner;
    private final SupermarketController supermarketController;
    private static final String MENU = "\n您想要做什么？\n"
            + "1. 浏览商品\n"
            + "2. 购物车\n"
            + "3. 注销或切换用户\n"
            + "4. 数据分析\n"
            + "0. 退出";

    public MainView(SupermarketController supermarketController) {
        this.scanner = supermarketController.getScanner();
        this.supermarketController = supermarketController;
    }

    @Override
    public void display() {
        System.out.println(MENU);
    }

    @Override
    public void handleUserInput() {
        while (true) {
            display(); //显示主菜单
            String userInput = getUserInput("请选择", scanner);
            switch (userInput) {
                case "1":
                    //进入商品页面
                    supermarketController.switchToView("productView");
                    break;
                case "2":
                    //进入到购物车页面
                    supermarketController.switchToView("cartView");
                    break;
                case "3":
                    //进入注销页面
                    supermarketController.getUserController().logout();
                    supermarketController.switchToView("loginAndLogoutView");
                    break;
                case  "4":
                    supermarketController.switchToView("orderView");
                case "0":
                    //退出程序
                    System.exit(0);
                default:
                    showError("输入有误，请重新输入！");
            }
        }
    }

    @Override
    public void show() {
        handleUserInput();
    }
}
