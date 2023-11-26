package cn.jxust.supermarket.server.dao;

import cn.jxust.supermarket.server.dao.exception.UserAlreadyExistsException;
import cn.jxust.supermarket.server.dao.exception.UserNotFoundException;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.server.util.UserFactory;

public interface UserDAO {
//    /**
//     * 根据用户名查找用户
//     * @param username 用户名
//     * @return 如果找到了返回用户对象，否则返回null
//     */
//    User getUserByUsername(String username);

    public UserFactory getUserFactory();
        /**
         * 根据用户id查找用户
         * @param id 用户id
         * @return 如果找到了返回用户对象，否则返回null
         */
    User getUserById(int id) throws UserNotFoundException;

    /**
     * 添加用户
     *
     * @param user 要添加的用户对象
     */
    void addUser(User user) throws UserAlreadyExistsException;

    /**
     * 更新用户信息
     *
     * @param user 要更新的用户对象
     */
    void updateUser(User user) throws UserNotFoundException;

    /**
     * 根据用户id删除用户
     *
     * @param id 用户id
     */
    void deleteUserById(int id) throws UserNotFoundException;

    boolean haveThisUser(User User);
}
