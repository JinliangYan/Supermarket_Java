package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.server.dao.exception.*;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.service.exception.LoginException;

/**
 * UserService接口，定义用户服务相关方法
 */
public interface UserService {


    User getUserById(int id) throws UserNotFoundException;


    User addUser(String username, String password) throws UserException;


    void updateUser(User user) throws UserException;


    void deleteUserById(int id) throws UserNotFoundException;


    User getCurrentUser();


    void setCurrentUser(User currentUser) throws UserNotFoundException;

    void login(int userid, String password) throws UserNotFoundException, LoginException;

    void logout();
}
