package cn.jxust.supermarket.client;

import cn.jxust.supermarket.client.view.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORT = 8080;

        try {
            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            ClientRequestHandler requestHandler = new ClientRequestHandler(objectInputStream, objectOutputStream);

            // Instantiate controllers
            SupermarketController supermarketController = new SupermarketController(requestHandler);

            // Instantiate views
            MainView mainView = new MainView(supermarketController);
            CartView cartView = new CartView(supermarketController);
            OrderView orderView = new OrderView(supermarketController);
            PaymentView paymentView = new PaymentView(supermarketController);
            ProductView productView = new ProductView(supermarketController);
            LoginAndLogoutView loginAndLogoutView = new LoginAndLogoutView(supermarketController);

            supermarketController.registerView("cartView", cartView);
            supermarketController.registerView("orderView", orderView);
            supermarketController.registerView("paymentView", paymentView);
            supermarketController.registerView("productView", productView);
            supermarketController.registerView("mainView", mainView);
            supermarketController.registerView("loginAndLogoutView", loginAndLogoutView);

            // Initialize application
            // show the loginAndLogoutView to start~
            supermarketController.switchToView("loginAndLogoutView");
            socket.close();

        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }
}

