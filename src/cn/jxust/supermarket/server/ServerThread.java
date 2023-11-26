package cn.jxust.supermarket.server;

import cn.jxust.supermarket.server.controller.SupermarketController;

import java.net.Socket;

public class ServerThread extends Thread {
    private final Socket socket;
    private final SupermarketController supermarketController;

    public ServerThread(Socket socket, SupermarketController supermarketController) {
        this.socket = socket;
        this.supermarketController = supermarketController;
    }

    @Override
    public void run() {
        // 创建 ServerRequestHandler 实例并处理客户端请求
        ServerRequestHandler requestHandler = new ServerRequestHandler(socket, supermarketController);
        requestHandler.start();
    }
}
