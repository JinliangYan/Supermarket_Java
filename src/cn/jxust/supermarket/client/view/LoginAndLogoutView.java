package cn.jxust.supermarket.client.view;

import cn.jxust.supermarket.client.SupermarketController;
import cn.jxust.supermarket.client.controller.UserController;
import cn.jxust.supermarket.server.dao.exception.UserException;
import cn.jxust.supermarket.server.dao.exception.UserNotFoundException;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.service.exception.LoginException;

import java.util.Scanner;

public class LoginAndLogoutView implements UserInputHandler, View {
    private final Scanner scanner;

    private final UserController userController;
    private final SupermarketController supermarketController;
    private static final String LOGIN_MENU = "\n您想要做什么？：\n"
            + "1. 登录\n"
            + "2. 注册\n"
            + "0. 退出";

    public LoginAndLogoutView(SupermarketController supermarketController) {
        this.supermarketController = supermarketController;
        this.scanner = supermarketController.getScanner();
        this.userController = supermarketController.getUserController();
    }

    @Override
    public void display() {
        System.out.println(LOGIN_MENU);
    }


    public void handleUserInput() {
        label: while (true) {
            display(); // 显示登录菜单
            String userInput = getUserInput("请选择", scanner); // 获取用户输入
            switch (userInput) {
                case "1":
                    // 处理登录操作
                    try {
                        int userId = getUserIntInput("请输入用户ID", scanner);
                        String password = getUserInput("请输入密码", scanner);
                        userController.login(userId, password);
                        showMessage("欢迎" + userController.getCurrentUserName());
                        supermarketController.switchToView("mainView");
                        break label; // 登录成功后结束循环
                    } catch (UserNotFoundException | LoginException e) {
                        showError(e.getMessage());
                    }
                    break;
                case "2":
                    // 处理注册操作
                    String newUsername = getUserInput("请设置用户名", scanner);
                    String newPassword = getUserInput("请设置密码", scanner);

                    try {
                        User newUser = userController.register(newUsername, newPassword);
                        if (newUser != null) {
                            showMessage("注册成功！");
                            int newUserId = newUser.getId();
                            showMessage("您的用户ID是：" + newUserId);
                            userController.login(newUserId, newPassword);
                            supermarketController.switchToView("mainView");
                        }
                        break label; // 登录成功后结束循环
                    } catch (UserException e) {
                        //e.printStackTrace();
                        showError("注册失败，请重试！");
                    }
                    break;
                case "0":
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
