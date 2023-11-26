package cn.jxust.supermarket.server.dao;

import java.io.*;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import cn.jxust.supermarket.server.dao.exception.UserAlreadyExistsException;
import cn.jxust.supermarket.server.dao.exception.UserNotFoundException;
import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.domain.VipUser;
import cn.jxust.supermarket.server.util.UserFactory;

public class UserDAOImpl implements UserDAO {

    // 模拟数据库数据
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private static final String USERS_FILE = "users_data.txt";
    private final CartDAO cartDAO;

    private final UserFactory userFactory = new UserFactory();

    public UserFactory getUserFactory() {
        return userFactory;
    }

    public UserDAOImpl(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
        loadUsersFromFile();
        OptionalInt maxKey = users.keySet().stream().mapToInt(Integer::intValue).max();
        if (maxKey.isPresent()) {
            userFactory.setNextId(maxKey.getAsInt() + 1);
            // 这里使用最大的key值
        }
    }


    private void saveUsersToFile() {
        try (FileWriter fileWriter = new FileWriter(USERS_FILE);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (User user : users.values()) {
                //type;id;username;password;salt;errorCount;balance;vipPoints
                if (user instanceof VipUser vipUser) {
                    bufferedWriter.write(String.format("vip;%d;%s;%s;%s;%d;%f;%d",
                            user.getId(), user.getUsername(), user.getPassword(), user.getSalt(), user.getErrorCount(), user.getBalance(), vipUser.getVipPoints()));
                } else {
                    //type;id;username;password;salt;errorCount;balance
                    bufferedWriter.write(String.format("ordinary;%d;%s;%s;%s;%d;%f",
                            user.getId(), user.getUsername(), user.getPassword(), user.getSalt(), user.getErrorCount(), user.getBalance()));
                }
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return;
        }

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] userData = line.split(";");
                String type = userData[0];
                int id = Integer.parseInt(userData[1]);
                String userName = userData[2];
                String password = userData[3];
                String salt = userData[4];
                int errorCount = Integer.parseInt(userData[5]);
                double balance = Double.parseDouble(userData[6]);
                if ("vip".equals(type)) {
                    int vipPoints = Integer.parseInt(userData[7]);
                    VipUser vipUser = new VipUser(id, userName, password, salt, errorCount, balance, vipPoints);
                    users.put(id, vipUser);
                    cartDAO.addCart(vipUser);
                    vipUser.setCart(cartDAO.getCartByUserId(vipUser.getId()));
                } else {
                    User user = new User(id, userName, password, salt, errorCount, balance);
                    users.put(id, user);
                    cartDAO.addCart(user);
                    user.setCart(cartDAO.getCartByUserId(user.getId()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public User getUserById(int id) throws UserNotFoundException {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("不存在ID为 " + id + " 的用户");
        }
        return users.get(id);
    }

    @Override
    public void addUser(User user) throws UserAlreadyExistsException {
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException("ID为 " + user.getId() + " 的用户已存在");
        }
        users.put(user.getId(), user);
        cartDAO.addCart(user);
        saveUsersToFile();
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("不存在ID为 " + user.getId() + " 的用户");
        }
        users.put(user.getId(), user);
        saveUsersToFile();
    }

    @Override
    public void deleteUserById(int id) throws UserNotFoundException {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("不存在ID为 " + id + " 的用户");
        }
        users.remove(id);
        cartDAO.deleteCart(id);
        saveUsersToFile();
    }


    @Override
    public boolean haveThisUser(User user) {
        return user != null && users.containsKey(user.getId());
    }
}