package cn.jxust.supermarket.server.controller;

import cn.jxust.supermarket.server.ApiResponse;
import cn.jxust.supermarket.server.dao.exception.*;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.service.exception.*;
import cn.jxust.supermarket.server.service.ProductService;
import cn.jxust.supermarket.server.service.UserService;

public class UserController implements Controller {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Object getCurrentUserId(Object requestData) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null)
            return 0;
        return new ApiResponse(true, currentUser.getId(), null);
    }

    public Object getCurrentUserName(Object requestData) {
        return new ApiResponse(true,
                userService.getCurrentUser().getUsername(),
                null);
    }

    public Object getCurrentUser(Object requestData) {
        return new ApiResponse(true,
                userService.getCurrentUser(),
                null);
    }

    public Object login(Object requestData) throws UserNotFoundException, LoginException {
        try {
            Object[] data = (Object[]) requestData;
            int userId = (int) data[0];
            String password = (String) data[1];
            userService.login(userId, password);
            return new ApiResponse(true,
                    userService.getCurrentUser(),
                    null);
        } catch (UserNotFoundException | LoginException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    public Object register(Object requestData) throws UserException {
        try {
            Object[] data = (Object[]) requestData;
            String userName = (String) data[0];
            String password = (String) data[1];
            User newUser = userService.addUser(userName, password);
            return new ApiResponse(true, newUser, null);
        } catch (UserException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    public Object logout(Object requestDate) {
        userService.logout();
        if (userService.getCurrentUser() == null) {
            return new ApiResponse(true, null, null);
        }
        return ApiResponse.errorResponse(new RuntimeException("失败，请重试！"));
    }
}
