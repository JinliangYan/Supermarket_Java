package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.server.dao.UserDAO;
import cn.jxust.supermarket.server.dao.exception.*;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.service.exception.*;
import cn.jxust.supermarket.server.util.PasswordEncryptor;

import java.util.Random;

public class UserServiceImpl implements UserService {

    private User currentUser;

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

//    @Override
//    public User getUserByUsername(String username) {
//        return userDAO.getUserByUsername(username);
//    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        return userDAO.getUserById(id);
    }

    @Override
    public User addUser(String username, String password) throws UserException{
        if (username == null || username.isEmpty())
            throw new UserException("用户名不能为空");
        if (password.isEmpty())
            throw new UserException("未设置密码");
        Random random = new Random();
        int tmp = random.nextInt(2);
        User newUser = null;
        //随机生成vip用户
        String salt =  PasswordEncryptor.generateSalt();
        if (tmp == 0)
            newUser = userDAO.getUserFactory().ordinary(username,
                    PasswordEncryptor.encrypt(password, salt),
                    salt,
                    0,
                    1000);
        else
            newUser = userDAO.getUserFactory().vip(username,
                    PasswordEncryptor.encrypt(password, salt),
                    salt,
                    0,
                    1000, 0);
        userDAO.addUser(newUser);
        return newUser;
    }

    @Override
    public void updateUser(User user) throws UserException {
        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new UserException("用户名不能为空");
        if (user.getPassword().isEmpty())
            throw new UserException(user.getId() + "未设置密码");
        user.setSalt(PasswordEncryptor.generateSalt());
        user.setPassword(PasswordEncryptor.encrypt(user.getPassword(), user.getSalt()));
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUserById(int id) throws UserNotFoundException {
        userDAO.deleteUserById(id);
    }
    @Override
    public User getCurrentUser() {
        return currentUser;
    }
    @Override
    public void setCurrentUser(User newCurrentUser) throws UserNotFoundException {
        if (!userDAO.haveThisUser(newCurrentUser))
            throw new UserNotFoundException("不存在ID为 " + newCurrentUser.getId() + " 的用户");
        this.currentUser = newCurrentUser;
    }

    @Override
    public void login(int userId, String password) throws UserNotFoundException, LoginException {
        User user;
        user = userDAO.getUserById(userId);
        int maxErrorCount = 3;
        if (this.currentUser != null)
            throw new DuplicateLoginException("重复登陆");
        else if (!user.getPassword().equals(PasswordEncryptor.encrypt(password, user.getSalt()))) {
            user.setErrorCount(user.getErrorCount() + 1);
            throw new LoginException("用户ID或密码错误");
        } else if (user.getErrorCount() > maxErrorCount) {
            throw new LoginException("尝试登录次数耗尽！");
        } else {
            user.setErrorCount(0);
            this.currentUser = user;
        }
    }

    @Override
    public void logout() {
        this.currentUser = null;
    }
}
