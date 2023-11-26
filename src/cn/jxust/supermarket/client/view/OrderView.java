package cn.jxust.supermarket.client.view;

import cn.jxust.supermarket.client.SupermarketController;
import cn.jxust.supermarket.client.controller.OrderController;
import cn.jxust.supermarket.domain.Food;
import cn.jxust.supermarket.domain.Order;
import cn.jxust.supermarket.domain.Phone;
import cn.jxust.supermarket.domain.Product;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import static cn.jxust.supermarket.server.util.ProductFactory.*;

public class OrderView implements UserInputHandler, View {

    private Scanner scanner;
    private final OrderController orderController;
    private final SupermarketController supermarketController;

    private static final String MENU = "\n数据分析子菜单 - 您想要做什么？\n"
            + "1. 按日期查看订单\n"
            + "2. 按产品查看订单\n"
            + "3. 按日期查询总销售额\n"
            + "4. 查询指定产品的总销售额\n"
            + "5. 查询总销售额\n"
            + "6. 按日期查询最畅销产品\n"
            + "7. 按日期查询最滞销产品\n"
            + "0. 返回主菜单";

    public OrderView(SupermarketController supermarketController) {
        this.scanner = supermarketController.getScanner();
        this.orderController = supermarketController.getOrderController();
        this.supermarketController = supermarketController;
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

            int productId;
            LocalDate date;
            List<Order> orders;
            switch (userInput) {
                case "1":
                    date = getUserDateInput(scanner);
                    orders = orderController.getOrdersByDate(date);
                    showOrders(orders);
                    break;
                case "2":
                    productId = getUserIntInput("请输入产品ID", scanner);
                    orders = orderController.getOrdersByProduct(productId);
                    showOrders(orders);
                    break;
                case "3":
                    date = getUserDateInput(scanner);
                    double totalSalesByDate = orderController.getTotalSalesByDate(date);
                    showMessage("该日期的总销售额为: " + totalSalesByDate);
                    break;
                case "4":
                    productId = getUserIntInput("请输入产品ID", scanner);
                    double totalSalesByProduct = orderController.getTotalSalesByProduct(productId);
                    showMessage("该产品的总销售额为: " + totalSalesByProduct);
                    break;
                case "5":
                    double totalSales = orderController.getTotalSales();
                    showMessage("总销售额为: " + totalSales);
                    break;
                case "6":
                    date = getUserDateInput(scanner);
                    String mostPopularProduct = orderController.getMostPopularProduct(date);
                    showMessage("该日期最畅销的产品为: " + mostPopularProduct);
                    break;
                case "7":
                    date = getUserDateInput(scanner);
                    String leastPopularProduct = orderController.getLeastPopularProduct(date);
                    showMessage("该日期最滞销的产品为: " + leastPopularProduct);
                    break;
                case "0":
                    supermarketController.switchToView("mainView");
                    return;
                default:
                    showError("输入有误，请重新输入！");
            }
        }
    }

    private LocalDate getUserDateInput(Scanner scanner) {
        LocalDate date = null;
        while (date == null) {
            System.out.print("请输入要查询的日期 (格式: yyyy-MM-dd)" + ">");
            String input = scanner.nextLine();
            try {
                date = LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                showError("日期格式错误，请按照格式重新输入！");
            }
        }
        return date;
    }
    private void showOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            showMessage("没有找到相关订单信息");
        } else {
            for (Order order : orders) {
                showMessage(String.format("时间: %s, 商品: %s, 数量: %d, 价格: %.2f",
                        order.getCreatTime(),
                        order.getProduct().getName(),
                        order.getQuantity(),
                        order.getProduct().getPrice()));
            }
        }
    }

    @Override
    public void show() {
        handleUserInput();
    }
}
