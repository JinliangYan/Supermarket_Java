package cn.jxust.supermarket.client.view;

import cn.jxust.supermarket.client.SupermarketController;
import cn.jxust.supermarket.client.controller.CartController;
import cn.jxust.supermarket.client.controller.PaymentController;
import cn.jxust.supermarket.client.controller.UserController;
import cn.jxust.supermarket.domain.Order;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.domain.VipUser;
import cn.jxust.supermarket.domain.exception.FoodExpiredException;
import cn.jxust.supermarket.server.service.PaymentServiceImpl;
import cn.jxust.supermarket.server.service.exception.PaymentException;

import java.util.Scanner;

public class PaymentView implements UserInputHandler, View {

    private final Scanner scanner;
    private final PaymentController paymentController;
    private final CartController cartController;
    private final UserController userController;

    public PaymentView(SupermarketController supermarketController) {
        this.scanner = supermarketController.getScanner();
        this.paymentController = supermarketController.getPaymentController();
        this.cartController = supermarketController.getCartController();
        this.userController = supermarketController.getUserController();
    }

    @Override
    public void display() {
        // 该方法可以留空
    }

    @Override
    public void handleUserInput() {
        String confirm = getUserInput("确认支付" + "%.2f".formatted(cartController.getTotalPrice()) + "购买所有商品？(Y/N)", scanner);
        if (confirm.equalsIgnoreCase("Y")) {
            try {
                User currentUser = userController.getCurrentUser();
                // 用户确认购买所有商品
                PaymentServiceImpl.PaymentInfo paymentInfo = paymentController.payAllInCart().get();
                showPaymentMes(currentUser, paymentInfo);
            } catch (PaymentException e) {
                showError(e.getMessage());
            } catch (FoodExpiredException e) {
                System.out.println("购物车中有过期商品" + e.getMessage());
            }
        } else {
            int productId = getUserIntInput("请输入要购买的商品ID", scanner);
            try {
                // 用户购买单个商品
                PaymentServiceImpl.PaymentInfo paymentInfo = paymentController.payForProduct(productId, cartController.getQuantity(productId)).get();
                showPaymentMes(userController.getCurrentUser(), paymentInfo);
            } catch (PaymentException e) {
                showError(e.getMessage());
            } catch (FoodExpiredException e) {
                System.out.println("商品过期了：" + e.getMessage());
            }
        }
    }

    private void showPaymentMes(User currentUser, PaymentServiceImpl.PaymentInfo paymentInfo) {
        showMessage("支付成功！即将打印订单……");

        Order[] orders = paymentInfo.orders();
        for (Order order : orders) {
            Product product = order.getProduct();
            showMessage("名称：" + product.getName() +
                    "\n原价：" + String.format("%.2f", order.getPriceBeforeDiscount()));
            if (product.getDiscount() != 1.0)
                showMessage("折后价：" + String.format("%.2f", product.getPrice() * product.getDiscount()));
            showMessage("数量：" + order.getQuantity());
            if (order.isVipUser()) {
                showMessage("积分抵扣：" + order.getPointsCost());
            }
            showMessage("实际支付：" + String.format("%.2f", order.getPriceAfterDiscount()));

            System.out.println();
        }
        showMessage("共支付：" + String.format("%.2f", paymentInfo.totalPayed()));
        showMessage("账户余额：" + String.format("%.2f", currentUser.getBalance()) + "￥");
        if (currentUser instanceof VipUser vipUser) {
            showMessage("获得积分：" + paymentInfo.earnPoints());
            showMessage("积分余额：" + vipUser.getVipPoints() + "点");
        }
    }

    @Override
    public void show() {
        handleUserInput();
    }
}

