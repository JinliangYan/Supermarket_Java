package cn.jxust.supermarket.server;

import cn.jxust.supermarket.domain.Cart;
import cn.jxust.supermarket.server.controller.SupermarketController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;

public class ServerRequestHandler extends Thread {
    private final Socket clientSocket;
    private final SupermarketController supermarketController;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ServerRequestHandler(Socket clientSocket, SupermarketController supermarketController) {
        this.clientSocket = clientSocket;
        this.supermarketController = supermarketController;
    }

    @Override
    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                String requestType = (String) objectInputStream.readObject();
                Object requestData = objectInputStream.readObject();
                Object response = supermarketController.handleRequest(requestType, requestData);
                System.out.printf("Received request %s from the client %s.%n",
                        requestType, clientSocket.getInetAddress() + " time:"  +
                                LocalDate.now());

                objectOutputStream.writeObject(response);
                System.out.println("The result has been returned to the client." + " time:" + LocalDate.now());
                objectOutputStream.flush();
            }

        } catch (IOException | ClassNotFoundException e) {
            if (e.getMessage() != null && e.getMessage().equals("Connection reset")) {
                supermarketController.setCurrentUser(null);
                System.out.println("User logout");
            } else {
                System.err.println("Error during request handling: " + e.getMessage());
            }
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
