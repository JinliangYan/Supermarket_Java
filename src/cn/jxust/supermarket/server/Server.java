package cn.jxust.supermarket.server;

import cn.jxust.supermarket.domain.Food;
import cn.jxust.supermarket.domain.Phone;
import cn.jxust.supermarket.domain.Product;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.controller.SupermarketController;
import cn.jxust.supermarket.server.dao.*;
import cn.jxust.supermarket.server.service.*;
import cn.jxust.supermarket.server.util.ControllerFactory;
import cn.jxust.supermarket.server.util.ServiceFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

import static cn.jxust.supermarket.server.util.ProductFactory.*;

public class Server {
    public static void main(String[] args) {
        // 创建DAO类实例
        ProductDAO productDAO = new ProductDAOImpl();
        CartDAO cartDAO = new CartDAOImpl(productDAO);
        UserDAO userDao = new UserDAOImpl(cartDAO);
        OrderDAO orderDAO = new OrderDAOimpl(productDAO);

        //创建Service工厂
        ServiceFactory serviceFactory = new ServiceFactory(userDao, productDAO, cartDAO, orderDAO);

        //创建Service类实例
        CartService cartService = serviceFactory.creatCartService();
        OrderService orderService = serviceFactory.creatOrderService();
        PaymentService paymentService = serviceFactory.creatPaymentService();
        ProductService productService = serviceFactory.creatProductService();
        UserService userService = serviceFactory.createUserService();

        //创建Controller工厂
        ControllerFactory controllerFactory = new ControllerFactory(userService, cartService, productService, paymentService, orderService);

        //初始化超市
        Phone discountPhone = createDiscountProduct(0.7,
                createPhone("HUAWEI Mate 50Pro", "手机", "超光变 XMAGE 影像，物理光圈十档可调 | 超可靠昆仑玻璃 | 北斗卫星消息，创新应急模式，鸿蒙操作系统 3.0.",
                        6799, 10, "HUAWEI", "第一代骁龙 8+ 4G", 16, 256, "鸿蒙操作系统 3.0"));
        Food bread = creatBread("帕帕罗蒂面包", "面包", "金黄色的外皮，咖啡浓香的味道\n" +
                "松软的面包内层，夹杂浓郁的奶香\n" +
                "这就是，帕帕罗蒂面包", 15, 10, LocalDate.of(2023, 6, 1));
        Product laTiao = createProduct("卫龙小面筋", "普通商品", "精选优质小麦，一级大豆油，味道醇正浓郁、香辣适口0反式脂肪酸、0甜蜜素 压熟制，非油炸，小而韧，辣\n" +
                "感柔，丝丝入味，柔韧带劲。", 2,5);
        productService.addProduct(discountPhone);
        productService.addProduct(bread);
        productService.addProduct(laTiao);


        int port = 8080;
        SupermarketController supermarketController = new SupermarketController(controllerFactory);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("client " + socket.getInetAddress() + " connected");
                // 创建一个新的线程来处理客户端请求
                new ServerThread(socket, supermarketController).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}