package cn.jxust.supermarket.client.controller;

import cn.jxust.supermarket.client.ClientRequestHandler;
import cn.jxust.supermarket.server.dao.exception.UserException;
import cn.jxust.supermarket.server.dao.exception.UserNotFoundException;
import cn.jxust.supermarket.server.service.exception.LoginException;
import cn.jxust.supermarket.domain.User;

public class UserController {

    private final ClientRequestHandler requestHandler;
    private User currentUser;

    public UserController(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserName() {
        return currentUser.getUsername();
    }

    public void login(int userId, String password) throws UserNotFoundException, LoginException {
        String requestType = "UserController:LOGIN";

        Object[] requestData = new Object[]{userId, password};

        currentUser = (User) requestHandler.sendRequest(requestType, requestData);
    }

    public void logout() {
        String requestType = "UserController:LOGOUT";
        requestHandler.sendRequest(requestType, null);
        currentUser = null;
    }


    public User register(String newUsername, String newPassword) {
        String requestType = "UserController:REGISTER";
        Object requestDate = new Object[] {newUsername, newPassword};
        return (User) requestHandler.sendRequest(requestType, requestDate);
    }
}
