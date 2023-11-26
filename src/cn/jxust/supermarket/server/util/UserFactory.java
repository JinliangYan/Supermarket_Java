package cn.jxust.supermarket.server.util;

import cn.jxust.supermarket.domain.User;
import cn.jxust.supermarket.domain.VipUser;

public class UserFactory {
    private int nextId;
    public User ordinary(String username, String password, String salt, int errorCount, double balance) {
        return new User(nextId++, username, password, salt, errorCount, balance);
    }
    public VipUser vip(String username, String password, String salt, int errorCount, double balance, int vipPoints) {
        return new VipUser(nextId++, username, password, salt, errorCount, balance, vipPoints);
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
